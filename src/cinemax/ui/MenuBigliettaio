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
import cinemax.model.Bigliettaio;
import cinemax.model.Prenotazione;
import cinemax.model.Proiezione;
import cinemax.model.Utente;
import cinemax.service.ServizioPrenotazioni;
import cinemax.service.ServizioProiezioni;
import cinemax.service.ServizioUtenti;
import cinemax.util.Costanti;
import cinemax.util.InputUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Menu per il bigliettaio registrato (dopo il login): visualizzare le
 * prenotazioni della giornata e cercare prenotazioni con vari criteri.
 */
public final class MenuBigliettaio {

    private MenuBigliettaio() {
    }

    public static void avvia(Scanner scanner, Bigliettaio bigliettaio,
                              ServizioUtenti servizioUtenti,
                              ServizioProiezioni servizioProiezioni,
                              ServizioPrenotazioni servizioPrenotazioni) {
        boolean continua = true;
        while (continua) {
            System.out.println();
            System.out.println("=== Menu Bigliettaio (" + bigliettaio.getNomeCompleto() + ") ===");
            System.out.println("1) Visualizza le prenotazioni di oggi");
            System.out.println("2) Cerca prenotazioni");
            System.out.println("3) Logout");
            int scelta = InputUtil.leggiInteroInRange(scanner, "Scelta: ", 1, 3);

            switch (scelta) {
                case 1:
                    visualizzaPrenotazioniDiOggi(servizioProiezioni, servizioPrenotazioni, servizioUtenti);
                    break;
                case 2:
                    cercaPrenotazioni(scanner, servizioUtenti, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 3:
                    continua = false;
                    break;
            }
        }
    }

    /**
     * Implementa la funzionalita' "visualizzare le prenotazioni nella data
     * odierna" prevista per il bigliettaio.
     */
    private static void visualizzaPrenotazioniDiOggi(ServizioProiezioni servizioProiezioni,
                                                       ServizioPrenotazioni servizioPrenotazioni,
                                                       ServizioUtenti servizioUtenti) {
        List<Prenotazione> diOggi = servizioPrenotazioni.prenotazioniDiOggi(servizioProiezioni);
        System.out.println();
        if (diOggi.isEmpty()) {
            System.out.println("Non ci sono prenotazioni per oggi.");
            return;
        }
        System.out.println("Prenotazioni di oggi:");
        for (Prenotazione p : diOggi) {
            stampaRigaPrenotazioneCompleta(p, servizioProiezioni, servizioUtenti);
        }
    }

    /**
     * Implementa cercaPrenotazione() per il bigliettaio: tutti i criteri
     * sono facoltativi e vengono combinati con un AND logico.
     */
    private static void cercaPrenotazioni(Scanner scanner, ServizioUtenti servizioUtenti,
                                           ServizioProiezioni servizioProiezioni,
                                           ServizioPrenotazioni servizioPrenotazioni) {
        System.out.println();
        System.out.println("Inserisci i criteri di ricerca (lascia vuoto per non filtrare su un campo):");
        String codice = InputUtil.leggiStringa(scanner, "Codice prenotazione (anche parziale): ");
        String nomeCognome = InputUtil.leggiStringa(scanner, "Nome e cognome del cliente (anche parziale): ");
        String titoloFilm = InputUtil.leggiStringa(scanner, "Titolo del film (anche parziale): ");

        LocalDate dataDa = null;
        LocalDate dataA = null;
        if (InputUtil.leggiSiNo(scanner, "Vuoi filtrare per intervallo di date?")) {
            dataDa = InputUtil.leggiDataFacoltativa(scanner, "Data iniziale");
            dataA = InputUtil.leggiDataFacoltativa(scanner, "Data finale");
        }

        List<Prenotazione> trovate = servizioPrenotazioni.cercaPrenotazione(
                codice.isEmpty() ? null : codice,
                nomeCognome.isEmpty() ? null : nomeCognome,
                titoloFilm.isEmpty() ? null : titoloFilm,
                dataDa, dataA, servizioUtenti, servizioProiezioni);

        System.out.println();
        if (trovate.isEmpty()) {
            System.out.println("Nessuna prenotazione trovata.");
            return;
        }
        System.out.println("Prenotazioni trovate:");
        for (Prenotazione p : trovate) {
            stampaRigaPrenotazioneCompleta(p, servizioProiezioni, servizioUtenti);
        }
    }

    private static void stampaRigaPrenotazioneCompleta(Prenotazione p, ServizioProiezioni servizioProiezioni,
                                                         ServizioUtenti servizioUtenti) {
        Utente cliente = servizioUtenti.trovaPerUsername(p.getUsernameCliente());
        String nomeCliente = (cliente == null) ? p.getUsernameCliente() : cliente.getNomeCompleto();
        try {
            Proiezione proiezione = servizioProiezioni.trovaPerId(p.getIdProiezione());
            System.out.println("Codice: " + p.getCodice()
                    + " - Cliente: " + nomeCliente
                    + " - Film: " + proiezione.getFilm().getTitolo()
                    + " - Data: " + proiezione.getDataOra().format(Costanti.FORMATO_DATA_ORA)
                    + " - Biglietti: " + p.getNumeroBiglietti());
        } catch (ElementoNonTrovatoException e) {
            System.out.println("Codice: " + p.getCodice() + " - Cliente: " + nomeCliente
                    + " - (la proiezione collegata non esiste piu')");
        }
    }
}
