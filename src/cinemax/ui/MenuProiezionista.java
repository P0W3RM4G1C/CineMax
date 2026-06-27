/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.ui;

import cinemax.eccezioni.ElementoNonTrovatoException;
import cinemax.eccezioni.PrenotazioneEsistenteException;
import cinemax.eccezioni.ProiezioneSovrappostaException;
import cinemax.model.Film;
import cinemax.model.Proiezionista;
import cinemax.model.Proiezione;
import cinemax.service.ServizioPrenotazioni;
import cinemax.service.ServizioProiezioni;
import cinemax.util.InputUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Menu per il proiezionista registrato (dopo il login): aggiungere,
 * modificare ed eliminare proiezioni dal palinsesto.
 */
public final class MenuProiezionista {

    private MenuProiezionista() {
    }

    public static void avvia(Scanner scanner, Proiezionista proiezionista,
                              ServizioProiezioni servizioProiezioni,
                              ServizioPrenotazioni servizioPrenotazioni) {
        boolean continua = true;
        while (continua) {
            System.out.println();
            System.out.println("=== Menu Proiezionista (" + proiezionista.getNomeCompleto() + ") ===");
            System.out.println("1) Visualizza tutte le proiezioni");
            System.out.println("2) Aggiungi una proiezione");
            System.out.println("3) Modifica una proiezione");
            System.out.println("4) Elimina una proiezione");
            System.out.println("5) Logout");
            int scelta = InputUtil.leggiInteroInRange(scanner, "Scelta: ", 1, 5);

            switch (scelta) {
                case 1:
                    visualizzaTutte(servizioProiezioni, servizioPrenotazioni);
                    break;
                case 2:
                    aggiungiProiezione(scanner, servizioProiezioni);
                    break;
                case 3:
                    modificaProiezione(scanner, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 4:
                    eliminaProiezione(scanner, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 5:
                    continua = false;
                    break;
            }
        }
    }

    private static void visualizzaTutte(ServizioProiezioni servizioProiezioni, ServizioPrenotazioni servizioPrenotazioni) {
        List<Proiezione> tutte = servizioProiezioni.getTutte();
        System.out.println();
        System.out.println("Elenco completo delle proiezioni:");
        MenuUtil.stampaElencoProiezioni(tutte);
    }

    /**
     * Implementa aggiungiProiezione(): come richiesto dalle specifiche,
     * prima si inseriscono i dati del film, poi data/ora e prezzo del
     * biglietto.
     */
    private static void aggiungiProiezione(Scanner scanner, ServizioProiezioni servizioProiezioni) {
        System.out.println();
        System.out.println("Inserisci i dati del film:");
        String titolo = InputUtil.leggiStringaObbligatoria(scanner, "Titolo: ");
        String genere = InputUtil.leggiStringaObbligatoria(scanner, "Genere: ");
        String regista = InputUtil.leggiStringaObbligatoria(scanner, "Regista: ");
        int anno = InputUtil.leggiIntero(scanner, "Anno di uscita: ");
        int durataMinuti = InputUtil.leggiIntero(scanner, "Durata (minuti): ");
        int etaMinima = InputUtil.leggiIntero(scanner, "Eta' minima (0 se nessun limite): ");
        Film film = new Film(titolo, genere, regista, anno, durataMinuti, etaMinima);

        System.out.println("Ora inserisci data, ora e prezzo della proiezione:");
        LocalDateTime dataOra = InputUtil.leggiDataOra(scanner, "Data e ora della proiezione");
        double costo = InputUtil.leggiDouble(scanner, "Prezzo del biglietto: ");

        try {
            Proiezione nuova = servizioProiezioni.aggiungiProiezione(film, dataOra, costo);
            System.out.println("Proiezione aggiunta con successo (id #" + nuova.getId() + ").");
        } catch (ProiezioneSovrappostaException e) {
            System.out.println("Impossibile aggiungere la proiezione: " + e.getMessage());
        } catch (java.io.IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    /**
     * Implementa modificaProiezione(): puo' essere fatta solo se non
     * esistono prenotazioni associate alla proiezione (controllo fatto dal
     * servizio).
     */
    private static void modificaProiezione(Scanner scanner, ServizioProiezioni servizioProiezioni,
                                            ServizioPrenotazioni servizioPrenotazioni) {
        visualizzaTutte(servizioProiezioni, servizioPrenotazioni);
        int id = InputUtil.leggiIntero(scanner, "Id della proiezione da modificare: ");

        LocalDateTime nuovaDataOra = null;
        if (InputUtil.leggiSiNo(scanner, "Vuoi modificare la data/ora?")) {
            nuovaDataOra = InputUtil.leggiDataOra(scanner, "Nuova data e ora");
        }
        Double nuovoCosto = null;
        if (InputUtil.leggiSiNo(scanner, "Vuoi modificare il prezzo del biglietto?")) {
            nuovoCosto = InputUtil.leggiDouble(scanner, "Nuovo prezzo del biglietto: ");
        }

        try {
            servizioProiezioni.modificaProiezione(id, nuovaDataOra, nuovoCosto, servizioPrenotazioni.getTutte());
            System.out.println("Proiezione modificata con successo.");
        } catch (ElementoNonTrovatoException | PrenotazioneEsistenteException | ProiezioneSovrappostaException e) {
            System.out.println("Impossibile modificare la proiezione: " + e.getMessage());
        } catch (java.io.IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    /**
     * Implementa eliminaProiezione(): puo' essere fatta solo se non
     * esistono prenotazioni associate alla proiezione (controllo fatto dal
     * servizio).
     */
    private static void eliminaProiezione(Scanner scanner, ServizioProiezioni servizioProiezioni,
                                           ServizioPrenotazioni servizioPrenotazioni) {
        visualizzaTutte(servizioProiezioni, servizioPrenotazioni);
        int id = InputUtil.leggiIntero(scanner, "Id della proiezione da eliminare: ");

        if (!InputUtil.leggiSiNo(scanner, "Confermi l'eliminazione della proiezione #" + id + "?")) {
            return;
        }

        try {
            servizioProiezioni.eliminaProiezione(id, servizioPrenotazioni.getTutte());
            System.out.println("Proiezione eliminata con successo.");
        } catch (ElementoNonTrovatoException | PrenotazioneEsistenteException e) {
            System.out.println("Impossibile eliminare la proiezione: " + e.getMessage());
        } catch (java.io.IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }
}
