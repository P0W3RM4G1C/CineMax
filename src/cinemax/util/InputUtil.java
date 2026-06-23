/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Classe di utilita' per leggere l'input da tastiera nell'interfaccia
 * testuale (TUI). Tutti i metodi continuano a chiedere il dato finche'
 * l'utente non inserisce un valore valido, cosi' evitiamo che un input
 * sbagliato faccia crashare il programma.
 */
public final class InputUtil {

    private InputUtil() {
    }

    /**
     * Legge una riga di testo qualsiasi (puo' anche essere vuota).
     */
    public static String leggiStringa(Scanner scanner, String messaggio) {
        System.out.print(messaggio);
        return scanner.nextLine().trim();
    }

    /**
     * Legge una riga di testo che non puo' essere vuota: se l'utente preme
     * solo invio, ripete la domanda.
     */
    public static String leggiStringaObbligatoria(Scanner scanner, String messaggio) {
        String valore;
        do {
            System.out.print(messaggio);
            valore = scanner.nextLine().trim();
            if (valore.isEmpty()) {
                System.out.println("Il campo non puo' essere vuoto, riprova.");
            }
        } while (valore.isEmpty());
        return valore;
    }

    /**
     * Legge un numero intero, ripetendo la domanda se l'utente scrive
     * qualcosa che non e' un numero.
     */
    public static int leggiIntero(Scanner scanner, String messaggio) {
        while (true) {
            System.out.print(messaggio);
            String riga = scanner.nextLine().trim();
            try {
                return Integer.parseInt(riga);
            } catch (NumberFormatException e) {
                System.out.println("Valore non valido: inserisci un numero intero.");
            }
        }
    }

    /**
     * Legge un numero intero compreso tra un minimo e un massimo (inclusi),
     * utile per leggere la scelta di una voce di menu.
     */
    public static int leggiInteroInRange(Scanner scanner, String messaggio, int minimo, int massimo) {
        while (true) {
            int valore = leggiIntero(scanner, messaggio);
            if (valore >= minimo && valore <= massimo) {
                return valore;
            }
            System.out.println("Scelta non valida: inserisci un numero tra " + minimo + " e " + massimo + ".");
        }
    }

    /**
     * Legge un numero decimale (es. per il costo del biglietto).
     */
    public static double leggiDouble(Scanner scanner, String messaggio) {
        while (true) {
            System.out.print(messaggio);
            String riga = scanner.nextLine().trim().replace(',', '.');
            try {
                return Double.parseDouble(riga);
            } catch (NumberFormatException e) {
                System.out.println("Valore non valido: inserisci un numero (es. 8.50).");
            }
        }
    }

    /**
     * Legge una data nel formato gg/mm/aaaa.
     */
    public static LocalDate leggiData(Scanner scanner, String messaggio) {
        while (true) {
            System.out.print(messaggio + " (formato gg/mm/aaaa): ");
            String riga = scanner.nextLine().trim();
            try {
                String[] parti = riga.split("/");
                int giorno = Integer.parseInt(parti[0]);
                int mese = Integer.parseInt(parti[1]);
                int anno = Integer.parseInt(parti[2]);
                return LocalDate.of(anno, mese, giorno);
            } catch (Exception e) {
                System.out.println("Data non valida: usa il formato gg/mm/aaaa (es. 25/12/2026).");
            }
        }
    }

    /**
     * Legge una data facoltativa (puo' essere lasciata vuota premendo invio):
     * restituisce null se l'utente non inserisce nulla.
     */
    public static LocalDate leggiDataFacoltativa(Scanner scanner, String messaggio) {
        System.out.print(messaggio + " (formato gg/mm/aaaa, lascia vuoto se non vuoi specificarla): ");
        String riga = scanner.nextLine().trim();
        if (riga.isEmpty()) {
            return null;
        }
        try {
            String[] parti = riga.split("/");
            int giorno = Integer.parseInt(parti[0]);
            int mese = Integer.parseInt(parti[1]);
            int anno = Integer.parseInt(parti[2]);
            return LocalDate.of(anno, mese, giorno);
        } catch (Exception e) {
            System.out.println("Data non valida, la lascio vuota.");
            return null;
        }
    }

    /**
     * Legge una data con ora nel formato gg/mm/aaaa hh:mm.
     */
    public static LocalDateTime leggiDataOra(Scanner scanner, String messaggio) {
        while (true) {
            System.out.print(messaggio + " (formato gg/mm/aaaa hh:mm): ");
            String riga = scanner.nextLine().trim();
            try {
                String[] partiPrincipali = riga.split(" ");
                String[] partiData = partiPrincipali[0].split("/");
                String[] partiOra = partiPrincipali[1].split(":");
                int giorno = Integer.parseInt(partiData[0]);
                int mese = Integer.parseInt(partiData[1]);
                int anno = Integer.parseInt(partiData[2]);
                int ora = Integer.parseInt(partiOra[0]);
                int minuti = Integer.parseInt(partiOra[1]);
                return LocalDateTime.of(anno, mese, giorno, ora, minuti);
            } catch (Exception e) {
                System.out.println("Data/ora non valida: usa il formato gg/mm/aaaa hh:mm (es. 25/12/2026 21:00).");
            }
        }
    }

    /**
     * Chiede una domanda con risposta si/no e restituisce true per "si".
     */
    public static boolean leggiSiNo(Scanner scanner, String messaggio) {
        while (true) {
            System.out.print(messaggio + " (s/n): ");
            String riga = scanner.nextLine().trim().toLowerCase();
            if (riga.equals("s") || riga.equals("si")) {
                return true;
            }
            if (riga.equals("n") || riga.equals("no")) {
                return false;
            }
            System.out.println("Rispondi con 's' o 'n'.");
        }
    }
}
