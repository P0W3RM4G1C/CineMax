/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.service;

import cinemax.data.GestorePrenotazioni;
import cinemax.eccezioni.DataNonValidaException;
import cinemax.eccezioni.ElementoNonTrovatoException;
import cinemax.eccezioni.PostiNonDisponibiliException;
import cinemax.model.Cliente;
import cinemax.model.Prenotazione;
import cinemax.model.Proiezione;
import cinemax.model.Utente;
import cinemax.util.CodiceUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contiene la logica applicativa relativa alle prenotazioni: ricerca,
 * creazione, modifica ed eliminazione. Mantiene in memoria la lista delle
 * prenotazioni caricata dal file e la tiene sincronizzata con il file ogni
 * volta che cambia.
 */
public class ServizioPrenotazioni {

    private final GestorePrenotazioni gestorePrenotazioni;
    private final List<Prenotazione> prenotazioni;

    public ServizioPrenotazioni() throws IOException {
        this.gestorePrenotazioni = new GestorePrenotazioni();
        this.prenotazioni = gestorePrenotazioni.caricaPrenotazioni();
    }

    public List<Prenotazione> getTutte() {
        return new ArrayList<>(prenotazioni);
    }

    /**
     * Restituisce tutte le prenotazioni di un certo cliente (usato dal
     * cliente per visualizzare le proprie prenotazioni).
     */
    public List<Prenotazione> prenotazioniPerCliente(String username) {
        List<Prenotazione> risultato = new ArrayList<>();
        for (Prenotazione p : prenotazioni) {
            if (p.getUsernameCliente().equalsIgnoreCase(username)) {
                risultato.add(p);
            }
        }
        return risultato;
    }

    /**
     * Restituisce tutte le prenotazioni effettuate per oggi (usato dal
     * bigliettaio per "Visualizzare le prenotazioni nella data odierna").
     */
    public List<Prenotazione> prenotazioniDiOggi(ServizioProiezioni servizioProiezioni) {
        List<Prenotazione> risultato = new ArrayList<>();
        LocalDate oggi = LocalDate.now();
        for (Prenotazione p : prenotazioni) {
            try {
                Proiezione proiezione = servizioProiezioni.trovaPerId(p.getIdProiezione());
                if (proiezione.getDataOra().toLocalDate().isEqual(oggi)) {
                    risultato.add(p);
                }
            } catch (ElementoNonTrovatoException e) {
                // La proiezione collegata non esiste piu': ignoriamo la prenotazione "orfana"
            }
        }
        return risultato;
    }

    public Prenotazione trovaPerCodice(String codice) throws ElementoNonTrovatoException {
        for (Prenotazione p : prenotazioni) {
            if (p.getCodice().equalsIgnoreCase(codice)) {
                return p;
            }
        }
        throw new ElementoNonTrovatoException("Nessuna prenotazione trovata con codice " + codice);
    }

    /**
     * Implementa cercaPrenotazione() per il bigliettaio: filtra le
     * prenotazioni in base a codice, nome/cognome cliente, titolo film e
     * intervallo di date. Ogni criterio e' opzionale (null/vuoto = non filtrare).
     */
    public List<Prenotazione> cercaPrenotazione(String codiceParziale, String nomeCognomeParziale,
                                                 String titoloFilmParziale, LocalDate dataDa, LocalDate dataA,
                                                 ServizioUtenti servizioUtenti, ServizioProiezioni servizioProiezioni) {
        List<Prenotazione> risultato = new ArrayList<>();

        for (Prenotazione p : prenotazioni) {
            if (codiceParziale != null && !codiceParziale.isEmpty()
                    && !p.getCodice().toLowerCase().contains(codiceParziale.toLowerCase())) {
                continue;
            }

            // Per filtrare per nome/cognome cliente e per titolo del film dobbiamo
            // recuperare i dati collegati (utente e proiezione)
            Utente cliente = servizioUtenti.trovaPerUsername(p.getUsernameCliente());
            if (nomeCognomeParziale != null && !nomeCognomeParziale.isEmpty()) {
                String nomeCompleto = (cliente == null) ? "" : cliente.getNomeCompleto().toLowerCase();
                if (!nomeCompleto.contains(nomeCognomeParziale.toLowerCase())) {
                    continue;
                }
            }

            Proiezione proiezione = null;
            try {
                proiezione = servizioProiezioni.trovaPerId(p.getIdProiezione());
            } catch (ElementoNonTrovatoException e) {
                // prenotazione orfana: se ci sono filtri su film/data non puo' soddisfarli
                if ((titoloFilmParziale != null && !titoloFilmParziale.isEmpty()) || dataDa != null || dataA != null) {
                    continue;
                }
            }

            if (titoloFilmParziale != null && !titoloFilmParziale.isEmpty()) {
                if (proiezione == null || !proiezione.getFilm().getTitolo().toLowerCase().contains(titoloFilmParziale.toLowerCase())) {
                    continue;
                }
            }

            if (dataDa != null) {
                if (proiezione == null || proiezione.getDataOra().toLocalDate().isBefore(dataDa)) {
                    continue;
                }
            }

            if (dataA != null) {
                if (proiezione == null || proiezione.getDataOra().toLocalDate().isAfter(dataA)) {
                    continue;
                }
            }

            risultato.add(p);
        }
        return risultato;
    }

    /**
     * Implementa creaPrenotazione(): controlla che ci siano posti
     * disponibili, genera un codice univoco e salva la prenotazione.
     */
    public Prenotazione creaPrenotazione(Cliente cliente, Proiezione proiezione, int numeroBiglietti,
                                          ServizioProiezioni servizioProiezioni)
            throws PostiNonDisponibiliException, IOException {

        int postiLiberi = servizioProiezioni.postiLiberi(proiezione, prenotazioni);
        if (numeroBiglietti > postiLiberi) {
            throw new PostiNonDisponibiliException(
                    "Posti disponibili insufficienti: richiesti " + numeroBiglietti
                            + ", disponibili " + postiLiberi + ".");
        }

        String codice = CodiceUtil.generaCodicePrenotazione();
        Prenotazione nuovaPrenotazione = new Prenotazione(codice, cliente.getUsername(), proiezione.getId(), numeroBiglietti);
        prenotazioni.add(nuovaPrenotazione);
        gestorePrenotazioni.salvaPrenotazioni(prenotazioni);
        return nuovaPrenotazione;
    }

    /**
     * Implementa modificaPrenotazione() (cambio proiezione/data) per il
     * cliente: secondo le specifiche e' permesso solo se sia la vecchia
     * data di proiezione sia la nuova sono successive alla data odierna.
     */
    public void modificaPrenotazione(String codice, int nuovoIdProiezione, ServizioProiezioni servizioProiezioni)
            throws ElementoNonTrovatoException, DataNonValidaException, PostiNonDisponibiliException, IOException {

        Prenotazione prenotazione = trovaPerCodice(codice);
        Proiezione vecchiaProiezione = servizioProiezioni.trovaPerId(prenotazione.getIdProiezione());
        Proiezione nuovaProiezione = servizioProiezioni.trovaPerId(nuovoIdProiezione);

        LocalDateTime adesso = LocalDateTime.now();
        if (!vecchiaProiezione.getDataOra().isAfter(adesso)) {
            throw new DataNonValidaException(
                    "Non e' possibile modificare la prenotazione: la proiezione originale non e' futura.");
        }
        if (!nuovaProiezione.getDataOra().isAfter(adesso)) {
            throw new DataNonValidaException(
                    "Non e' possibile modificare la prenotazione: la nuova proiezione scelta non e' futura.");
        }

        // Controlliamo che ci siano posti liberi sulla nuova proiezione
        // (i posti gia' occupati dalla prenotazione stessa sulla vecchia
        // proiezione non contano, perche' verranno liberati)
        int postiLiberiNuova = servizioProiezioni.postiLiberi(nuovaProiezione, prenotazioni);
        if (prenotazione.getNumeroBiglietti() > postiLiberiNuova) {
            throw new PostiNonDisponibiliException(
                    "Posti disponibili insufficienti sulla nuova proiezione: richiesti "
                            + prenotazione.getNumeroBiglietti() + ", disponibili " + postiLiberiNuova + ".");
        }

        prenotazione.setIdProiezione(nuovoIdProiezione);
        gestorePrenotazioni.salvaPrenotazioni(prenotazioni);
    }

    /**
     * Implementa eliminaPrenotazione() per il cliente.
     * NOTA: come indicato esplicitamente nelle specifiche di progetto, il
     * vincolo per l'eliminazione e' che la data di proiezione sia
     * PRECEDENTE alla data odierna (a differenza della modifica, che invece
     * richiede date future). Abbiamo implementato il vincolo esattamente
     * come riportato nel testo delle specifiche.
     */
    public void eliminaPrenotazione(String codice, ServizioProiezioni servizioProiezioni)
            throws ElementoNonTrovatoException, DataNonValidaException, IOException {

        Prenotazione prenotazione = trovaPerCodice(codice);
        Proiezione proiezione = servizioProiezioni.trovaPerId(prenotazione.getIdProiezione());

        if (!proiezione.getDataOra().isBefore(LocalDateTime.now())) {
            throw new DataNonValidaException(
                    "Non e' possibile eliminare la prenotazione: la data di proiezione non e' precedente a oggi.");
        }

        prenotazioni.remove(prenotazione);
        gestorePrenotazioni.salvaPrenotazioni(prenotazioni);
    }
}
