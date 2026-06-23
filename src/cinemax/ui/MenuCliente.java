/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.ui;

import cinemax.eccezioni.DataNonValidaException;
import cinemax.eccezioni.ElementoNonTrovatoException;
import cinemax.eccezioni.PostiNonDisponibiliException;
import cinemax.model.Cliente;
import cinemax.model.Prenotazione;
import cinemax.model.Proiezione;
import cinemax.service.ServizioPrenotazioni;
import cinemax.service.ServizioProiezioni;
import cinemax.util.Costanti;
import cinemax.util.InputUtil;

import java.util.List;
import java.util.Scanner;

/**
 * Menu per il cliente registrato (dopo il login): cercare proiezioni e
 * prenotare, visualizzare le proprie prenotazioni, modificarle o eliminarle.
 */
public final class MenuCliente {

    private MenuCliente() {
    }

    public static void avvia(Scanner scanner, Cliente cliente,
                              ServizioProiezioni servizioProiezioni,
                              ServizioPrenotazioni servizioPrenotazioni) {
        boolean continua = true;
        while (continua) {
            System.out.println();
            System.out.println("=== Menu Cliente (" + cliente.getNomeCompleto() + ") ===");
            System.out.println("1) Cerca proiezioni e prenota");
            System.out.println("2) Visualizza le mie prenotazioni");
            System.out.println("3) Modifica una prenotazione");
            System.out.println("4) Elimina una prenotazione");
            System.out.println("5) Logout");
            int scelta = InputUtil.leggiInteroInRange(scanner, "Scelta: ", 1, 5);

            switch (scelta) {
                case 1:
                    cercaEPrenota(scanner, cliente, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 2:
                    visualizzaPrenotazioni(cliente, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 3:
                    modificaPrenotazione(scanner, cliente, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 4:
                    eliminaPrenotazione(scanner, cliente, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 5:
                    continua = false;
                    break;
            }
        }
    }

    /**
     * Implementa cercaProiezione() + creaPrenotazione(): l'utente cerca le
     * proiezioni con dei criteri, ne scegli una e indica quanti biglietti
     * vuole prenotare.
     */
    private static void cercaEPrenota(Scanner scanner, Cliente cliente,
                                       ServizioProiezioni servizioProiezioni,
                                       ServizioPrenotazioni servizioPrenotazioni) {
        List<Proiezione> trovate = MenuUtil.chiediCriteriERicerca(scanner, servizioProiezioni);
        System.out.println();
        System.out.println("Risultati della ricerca:");
        Proiezione scelta = MenuUtil.selezionaProiezione(scanner, trovate);
        if (scelta == null) {
            return;
        }

        int postiLiberi = servizioProiezioni.postiLiberi(scelta, servizioPrenotazioni.getTutte());
        MenuUtil.stampaDettagliProiezione(scelta, postiLiberi);

        if (!InputUtil.leggiSiNo(scanner, "Vuoi prenotare per questa proiezione?")) {
            return;
        }

        int numeroBiglietti = InputUtil.leggiIntero(scanner, "Quanti biglietti vuoi prenotare? ");
        try {
            Prenotazione prenotazione = servizioPrenotazioni.creaPrenotazione(cliente, scelta, numeroBiglietti, servizioProiezioni);
            System.out.println("Prenotazione effettuata con successo! Codice prenotazione: " + prenotazione.getCodice());
        } catch (PostiNonDisponibiliException e) {
            System.out.println("Impossibile completare la prenotazione: " + e.getMessage());
        } catch (java.io.IOException e) {
            System.out.println("Errore durante il salvataggio della prenotazione: " + e.getMessage());
        }
    }

    /**
     * Implementa visualizzaPrenotazione() per il cliente: mostra tutte le
     * proprie prenotazioni con i dettagli della proiezione collegata.
     */
    private static void visualizzaPrenotazioni(Cliente cliente, ServizioProiezioni servizioProiezioni,
                                                 ServizioPrenotazioni servizioPrenotazioni) {
        List<Prenotazione> proprie = servizioPrenotazioni.prenotazioniPerCliente(cliente.getUsername());
        System.out.println();
        if (proprie.isEmpty()) {
            System.out.println("Non hai nessuna prenotazione.");
            return;
        }
        System.out.println("Le tue prenotazioni:");
        for (Prenotazione p : proprie) {
            stampaRigaPrenotazione(p, servizioProiezioni);
        }
    }

    private static void stampaRigaPrenotazione(Prenotazione p, ServizioProiezioni servizioProiezioni) {
        try {
            Proiezione proiezione = servizioProiezioni.trovaPerId(p.getIdProiezione());
            double costoTotale = proiezione.getCostoBiglietto() * p.getNumeroBiglietti();
            System.out.println("Codice: " + p.getCodice()
                    + " - Film: " + proiezione.getFilm().getTitolo()
                    + " - Data: " + proiezione.getDataOra().format(Costanti.FORMATO_DATA_ORA)
                    + " - Biglietti: " + p.getNumeroBiglietti()
                    + " - Totale: " + String.format("%.2f", costoTotale) + " EUR");
        } catch (ElementoNonTrovatoException e) {
            System.out.println("Codice: " + p.getCodice() + " - (la proiezione collegata non esiste piu')");
        }
    }

    /**
     * Implementa modificaPrenotazione(): chiede il codice della prenotazione
     * da modificare e la nuova proiezione scelta. Il controllo sulle date
     * (entrambe future) e' fatto dal servizio.
     */
    private static void modificaPrenotazione(Scanner scanner, Cliente cliente,
                                              ServizioProiezioni servizioProiezioni,
                                              ServizioPrenotazioni servizioPrenotazioni) {
        String codice = InputUtil.leggiStringaObbligatoria(scanner, "Codice della prenotazione da modificare: ");
        Prenotazione prenotazione;
        try {
            prenotazione = servizioPrenotazioni.trovaPerCodice(codice);
        } catch (ElementoNonTrovatoException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (!prenotazione.getUsernameCliente().equalsIgnoreCase(cliente.getUsername())) {
            System.out.println("Questa prenotazione non appartiene al tuo account.");
            return;
        }

        System.out.println("Scegli la nuova proiezione per questa prenotazione:");
        List<Proiezione> trovate = MenuUtil.chiediCriteriERicerca(scanner, servizioProiezioni);
        Proiezione nuovaProiezione = MenuUtil.selezionaProiezione(scanner, trovate);
        if (nuovaProiezione == null) {
            return;
        }

        try {
            servizioPrenotazioni.modificaPrenotazione(codice, nuovaProiezione.getId(), servizioProiezioni);
            System.out.println("Prenotazione modificata con successo.");
        } catch (ElementoNonTrovatoException | DataNonValidaException | PostiNonDisponibiliException e) {
            System.out.println("Impossibile modificare la prenotazione: " + e.getMessage());
        } catch (java.io.IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    /**
     * Implementa eliminaPrenotazione(): chiede il codice della prenotazione
     * da eliminare. NOTA: secondo le specifiche, e' permesso eliminare solo
     * prenotazioni la cui proiezione e' in una data precedente a oggi.
     */
    private static void eliminaPrenotazione(Scanner scanner, Cliente cliente,
                                             ServizioProiezioni servizioProiezioni,
                                             ServizioPrenotazioni servizioPrenotazioni) {
        String codice = InputUtil.leggiStringaObbligatoria(scanner, "Codice della prenotazione da eliminare: ");
        Prenotazione prenotazione;
        try {
            prenotazione = servizioPrenotazioni.trovaPerCodice(codice);
        } catch (ElementoNonTrovatoException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (!prenotazione.getUsernameCliente().equalsIgnoreCase(cliente.getUsername())) {
            System.out.println("Questa prenotazione non appartiene al tuo account.");
            return;
        }

        if (!InputUtil.leggiSiNo(scanner, "Confermi l'eliminazione della prenotazione " + codice + "?")) {
            return;
        }

        try {
            servizioPrenotazioni.eliminaPrenotazione(codice, servizioProiezioni);
            System.out.println("Prenotazione eliminata con successo.");
        } catch (ElementoNonTrovatoException | DataNonValidaException e) {
            System.out.println("Impossibile eliminare la prenotazione: " + e.getMessage());
        } catch (java.io.IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }
}
