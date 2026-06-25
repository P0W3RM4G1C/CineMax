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
import cinemax.service.ServizioProiezioni;
import cinemax.util.Costanti;
import cinemax.util.InputUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Funzioni di supporto condivise tra i vari menu testuali (guest, cliente,
 * proiezionista, bigliettaio): stampare un elenco di proiezioni, far
 * scegliere una proiezione dall'elenco, chiedere i criteri di ricerca, ecc.
 * Evitiamo cosi' di duplicare lo stesso codice in piu' classi.
 */
public final class MenuUtil {

    private MenuUtil() {
    }

    /**
     * Stampa a video un elenco numerato di proiezioni (titolo, data/ora, prezzo).
     */
    public static void stampaElencoProiezioni(List<Proiezione> proiezioni) {
        if (proiezioni.isEmpty()) {
            System.out.println("Nessuna proiezione trovata.");
            return;
        }
        for (int i = 0; i < proiezioni.size(); i++) {
            Proiezione p = proiezioni.get(i);
            System.out.println((i + 1) + ") " + p.getFilm().getTitolo()
                    + " - " + p.getDataOra().format(Costanti.FORMATO_DATA_ORA)
                    + " - " + String.format("%.2f", p.getCostoBiglietto()) + " EUR"
                    + " (id #" + p.getId() + ")");
        }
    }

    /**
     * Stampa tutti i dettagli di una proiezione: caratteristiche del film,
     * data/ora, costo del biglietto e posti liberi (implementa visualizzaProiezione()).
     */
    public static void stampaDettagliProiezione(Proiezione p, int postiLiberi) {
        System.out.println("---------------------------------------------");
        System.out.println("Proiezione #" + p.getId());
        System.out.println("Titolo:        " + p.getFilm().getTitolo());
        System.out.println("Genere:        " + p.getFilm().getGenere());
        System.out.println("Regista:       " + p.getFilm().getRegista());
        System.out.println("Anno:          " + p.getFilm().getAnno());
        System.out.println("Durata:        " + p.getFilm().getDurataMinuti() + " minuti");
        System.out.println("Eta' minima:   " + (p.getFilm().getEtaMinima() > 0 ? p.getFilm().getEtaMinima() + "+" : "nessuna"));
        System.out.println("Data e ora:    " + p.getDataOra().format(Costanti.FORMATO_DATA_ORA));
        System.out.println("Prezzo:        " + String.format("%.2f", p.getCostoBiglietto()) + " EUR");
        System.out.println("Posti liberi:  " + postiLiberi + " / " + Costanti.CAPIENZA_SALA);
        System.out.println("---------------------------------------------");
    }

    /**
     * Chiede all'utente i criteri di ricerca di una proiezione (tutti
     * facoltativi: si puo' premere invio per non filtrare su un campo) e
     * restituisce la lista di proiezioni trovate, implementando
     * cercaProiezione().
     */
    public static List<Proiezione> chiediCriteriERicerca(Scanner scanner, ServizioProiezioni servizioProiezioni) {
        System.out.println("Inserisci i criteri di ricerca (lascia vuoto per non filtrare su un campo):");

        String titolo = InputUtil.leggiStringa(scanner, "Titolo (anche parziale): ");
        String genere = InputUtil.leggiStringa(scanner, "Genere: ");

        LocalDate dataDa = null;
        LocalDate dataA = null;
        if (InputUtil.leggiSiNo(scanner, "Vuoi filtrare per intervallo di date?")) {
            dataDa = InputUtil.leggiDataFacoltativa(scanner, "Data iniziale");
            dataA = InputUtil.leggiDataFacoltativa(scanner, "Data finale");
        }

        Double costoMin = null;
        Double costoMax = null;
        if (InputUtil.leggiSiNo(scanner, "Vuoi filtrare per costo del biglietto?")) {
            String minTesto = InputUtil.leggiStringa(scanner, "Costo minimo (lascia vuoto per nessun minimo): ");
            String maxTesto = InputUtil.leggiStringa(scanner, "Costo massimo (lascia vuoto per nessun massimo): ");
            if (!minTesto.isEmpty()) {
                costoMin = Double.parseDouble(minTesto.replace(',', '.'));
            }
            if (!maxTesto.isEmpty()) {
                costoMax = Double.parseDouble(maxTesto.replace(',', '.'));
            }
        }

        return servizioProiezioni.cercaProiezione(
                titolo.isEmpty() ? null : titolo,
                genere.isEmpty() ? null : genere,
                dataDa, dataA, costoMin, costoMax);
    }

    /**
     * Mostra l'elenco di proiezioni passato e fa scegliere all'utente quale
     * selezionare (implementa la parte "selezione" prevista prima di
     * visualizzaProiezione() o creaPrenotazione()). Restituisce null se
     * l'elenco e' vuoto o se l'utente annulla la scelta.
     */
    public static Proiezione selezionaProiezione(Scanner scanner, List<Proiezione> proiezioni) {
        if (proiezioni.isEmpty()) {
            return null;
        }
        stampaElencoProiezioni(proiezioni);
        int scelta = InputUtil.leggiInteroInRange(scanner,
                "Scegli il numero della proiezione (0 per annullare): ", 0, proiezioni.size());
        if (scelta == 0) {
            return null;
        }
        return proiezioni.get(scelta - 1);
    }
}
