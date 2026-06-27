/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.service;

import cinemax.data.GestoreProiezioni;
import cinemax.eccezioni.ElementoNonTrovatoException;
import cinemax.eccezioni.PrenotazioneEsistenteException;
import cinemax.eccezioni.ProiezioneSovrappostaException;
import cinemax.model.Film;
import cinemax.model.Prenotazione;
import cinemax.model.Proiezione;
import cinemax.util.CodiceUtil;
import cinemax.util.Costanti;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contiene la logica applicativa relativa alle proiezioni: ricerca,
 * aggiunta, modifica ed eliminazione del palinsesto. Mantiene in memoria
 * la lista delle proiezioni caricata dal file e la tiene sincronizzata
 * con il file ogni volta che cambia.
 */
public class ServizioProiezioni {

    private final GestoreProiezioni gestoreProiezioni;
    private final List<Proiezione> proiezioni;

    public ServizioProiezioni() throws IOException {
        this.gestoreProiezioni = new GestoreProiezioni();
        this.proiezioni = gestoreProiezioni.caricaProiezioni();
    }

    /**
     * Restituisce una copia della lista di tutte le proiezioni
     * (una copia, cosi' chi la riceve non puo' modificare la lista interna).
     */
    public List<Proiezione> getTutte() {
        return new ArrayList<>(proiezioni);
    }

    /**
     * Implementa cercaProiezione(): filtra le proiezioni in base ai criteri
     * indicati. Ogni criterio e' opzionale: se viene passato null (o stringa
     * vuota per il titolo/genere) significa "non filtrare per questo campo".
     * I criteri passati vengono combinati con un AND logico.
     */
    public List<Proiezione> cercaProiezione(String titoloParziale, String genere,
                                             LocalDate dataDa, LocalDate dataA,
                                             Double costoMin, Double costoMax) {
        List<Proiezione> risultato = new ArrayList<>();

        for (Proiezione p : proiezioni) {
            // Filtro per titolo (anche parziale, case insensitive)
            if (titoloParziale != null && !titoloParziale.isEmpty()
                    && !p.getFilm().getTitolo().toLowerCase().contains(titoloParziale.toLowerCase())) {
                continue;
            }
            // Filtro per genere (anche parziale, case insensitive)
            if (genere != null && !genere.isEmpty()
                    && !p.getFilm().getGenere().toLowerCase().contains(genere.toLowerCase())) {
                continue;
            }
            // Filtro per data minima (proiezioni dopo una certa data, inclusa)
            if (dataDa != null && p.getDataOra().toLocalDate().isBefore(dataDa)) {
                continue;
            }
            // Filtro per data massima (proiezioni prima di una certa data, inclusa)
            if (dataA != null && p.getDataOra().toLocalDate().isAfter(dataA)) {
                continue;
            }
            // Filtro per costo minimo
            if (costoMin != null && p.getCostoBiglietto() < costoMin) {
                continue;
            }
            // Filtro per costo massimo
            if (costoMax != null && p.getCostoBiglietto() > costoMax) {
                continue;
            }
            risultato.add(p);
        }
        return risultato;
    }

    /**
     * Cerca una proiezione tramite il suo id, lanciando un'eccezione se
     * non viene trovata.
     */
    public Proiezione trovaPerId(int id) throws ElementoNonTrovatoException {
        for (Proiezione p : proiezioni) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new ElementoNonTrovatoException("Nessuna proiezione trovata con id " + id);
    }

    /**
     * Controlla se una proiezione (nuova o modificata) si sovrapporrebbe con
     * un'altra proiezione gia' esistente in sala. L'id da escludere serve
     * quando si sta modificando una proiezione gia' presente (non deve
     * confrontarsi con se' stessa).
     */
    private void controllaSovrapposizione(Proiezione candidata, int idDaEscludere) throws ProiezioneSovrappostaException {
        for (Proiezione esistente : proiezioni) {
            if (esistente.getId() == idDaEscludere) {
                continue;
            }
            if (candidata.siSovrapponeCon(esistente)) {
                throw new ProiezioneSovrappostaException(
                        "La proiezione si sovrappone con la proiezione #" + esistente.getId()
                                + " (" + esistente.getFilm().getTitolo() + " - " + esistente.getDataOra() + ")");
            }
        }
    }

    /**
     * Implementa aggiungiProiezione(): crea una nuova proiezione per il film
     * indicato, controllando che non si sovrapponga con una proiezione
     * esistente, e la salva su file.
     */
    public Proiezione aggiungiProiezione(Film film, LocalDateTime dataOra, double costoBiglietto)
            throws ProiezioneSovrappostaException, IOException {

        int nuovoId = CodiceUtil.prossimoIdProiezione(proiezioni);
        Proiezione nuovaProiezione = new Proiezione(nuovoId, film, dataOra, costoBiglietto);

        // id -1 perche' la nuova proiezione non e' ancora nella lista,
        // quindi non c'e' bisogno di escludere nulla
        controllaSovrapposizione(nuovaProiezione, -1);

        proiezioni.add(nuovaProiezione);
        gestoreProiezioni.salvaProiezioni(proiezioni);
        return nuovaProiezione;
    }

    /**
     * Implementa modificaProiezione(): permette di cambiare data/ora e
     * costo di una proiezione esistente, a patto che non ci siano
     * prenotazioni associate (vincolo richiesto dalle specifiche) e che la
     * nuova data non si sovrapponga con un'altra proiezione.
     */
    public void modificaProiezione(int id, LocalDateTime nuovaDataOra, Double nuovoCosto,
                                    List<Prenotazione> tuttePrenotazioni)
            throws ElementoNonTrovatoException, PrenotazioneEsistenteException, ProiezioneSovrappostaException, IOException {

        Proiezione proiezione = trovaPerId(id);

        if (esistonoPrenotazioniPer(id, tuttePrenotazioni)) {
            throw new PrenotazioneEsistenteException(
                    "Impossibile modificare la proiezione #" + id + ": esistono prenotazioni associate.");
        }

        LocalDateTime vecchiaData = proiezione.getDataOra();
        double vecchioCosto = proiezione.getCostoBiglietto();

        if (nuovaDataOra != null) {
            proiezione.setDataOra(nuovaDataOra);
        }
        if (nuovoCosto != null) {
            proiezione.setCostoBiglietto(nuovoCosto);
        }

        try {
            controllaSovrapposizione(proiezione, id);
        } catch (ProiezioneSovrappostaException e) {
            // Se la modifica genera una sovrapposizione, annulliamo la modifica
            // (ripristiniamo i valori precedenti) e rilanciamo l'eccezione
            proiezione.setDataOra(vecchiaData);
            proiezione.setCostoBiglietto(vecchioCosto);
            throw e;
        }

        gestoreProiezioni.salvaProiezioni(proiezioni);
    }

    /**
     * Implementa eliminaProiezione(): elimina una proiezione esistente,
     * a patto che non ci siano prenotazioni associate.
     */
    public void eliminaProiezione(int id, List<Prenotazione> tuttePrenotazioni)
            throws ElementoNonTrovatoException, PrenotazioneEsistenteException, IOException {

        Proiezione proiezione = trovaPerId(id);

        if (esistonoPrenotazioniPer(id, tuttePrenotazioni)) {
            throw new PrenotazioneEsistenteException(
                    "Impossibile eliminare la proiezione #" + id + ": esistono prenotazioni associate.");
        }

        proiezioni.remove(proiezione);
        gestoreProiezioni.salvaProiezioni(proiezioni);
    }

    private boolean esistonoPrenotazioniPer(int idProiezione, List<Prenotazione> tuttePrenotazioni) {
        for (Prenotazione p : tuttePrenotazioni) {
            if (p.getIdProiezione() == idProiezione) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calcola quanti posti sono ancora liberi per una proiezione, sottraendo
     * dalla capienza della sala il numero di biglietti gia' prenotati.
     */
    public int postiLiberi(Proiezione proiezione, List<Prenotazione> tuttePrenotazioni) {
        int bigliettiVenduti = 0;
        for (Prenotazione p : tuttePrenotazioni) {
            if (p.getIdProiezione() == proiezione.getId()) {
                bigliettiVenduti += p.getNumeroBiglietti();
            }
        }
        return Costanti.CAPIENZA_SALA - bigliettiVenduti;
    }
}
