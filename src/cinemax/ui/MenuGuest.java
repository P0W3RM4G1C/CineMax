/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.ui;

import cinemax.model.Proiezione;
import cinemax.service.ServizioPrenotazioni;
import cinemax.service.ServizioProiezioni;
import cinemax.util.InputUtil;

import java.util.List;
import java.util.Scanner;

/**
 * Menu per gli utenti non autenticati (utenti "guest"): possono cercare
 * proiezioni e vederne i dettagli, senza bisogno di login.
 */
public final class MenuGuest {

    private MenuGuest() {
    }

    /**
     * Versione "rapida" prevista dalle specifiche: l'utente guest indica
     * solo il (eventuale) nome parziale di un film e vede subito l'elenco
     * delle proiezioni disponibili per quel film.
     */
    public static void avviaConNomeFilm(Scanner scanner, String nomeFilmParziale,
                                         ServizioProiezioni servizioProiezioni,
                                         ServizioPrenotazioni servizioPrenotazioni) {
        List<Proiezione> trovate = servizioProiezioni.cercaProiezione(nomeFilmParziale, null, null, null, null, null);
        System.out.println();
        System.out.println("Proiezioni disponibili per \"" + nomeFilmParziale + "\":");
        MenuUtil.stampaElencoProiezioni(trovate);

        Proiezione scelta = MenuUtil.selezionaProiezione(scanner, trovate);
        if (scelta != null) {
            int postiLiberi = servizioProiezioni.postiLiberi(scelta, servizioPrenotazioni.getTutte());
            MenuUtil.stampaDettagliProiezione(scelta, postiLiberi);
        }

        // Da qui il guest puo' comunque continuare a navigare nel menu completo
        avvia(scanner, servizioProiezioni, servizioPrenotazioni);
    }

    /**
     * Menu completo per il guest: cercare proiezioni e visualizzarne i dettagli.
     */
    public static void avvia(Scanner scanner, ServizioProiezioni servizioProiezioni,
                              ServizioPrenotazioni servizioPrenotazioni) {
        boolean continua = true;
        while (continua) {
            System.out.println();
            System.out.println("=== Menu Guest ===");
            System.out.println("1) Cerca proiezioni");
            System.out.println("2) Torna al menu principale");
            int scelta = InputUtil.leggiInteroInRange(scanner, "Scelta: ", 1, 2);

            switch (scelta) {
                case 1:
                    List<Proiezione> trovate = MenuUtil.chiediCriteriERicerca(scanner, servizioProiezioni);
                    System.out.println();
                    System.out.println("Risultati della ricerca:");
                    Proiezione selezionata = MenuUtil.selezionaProiezione(scanner, trovate);
                    if (selezionata != null) {
                        int postiLiberi = servizioProiezioni.postiLiberi(selezionata, servizioPrenotazioni.getTutte());
                        MenuUtil.stampaDettagliProiezione(selezionata, postiLiberi);
                    }
                    break;
                case 2:
                    continua = false;
                    break;
            }
        }
    }
}
