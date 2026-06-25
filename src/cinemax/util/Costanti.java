/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.util;

import java.time.format.DateTimeFormatter;

/**
 * Raccoglie le costanti usate in piu' punti del progetto, cosi' se cambia
 * un valore (es. la capienza della sala) lo si modifica in un solo posto.
 */
public final class Costanti {

    // Numero di posti della sala (cinema monosala, vedi specifiche)
    public static final int CAPIENZA_SALA = 200;

    // Percorsi dei file dati (relativi alla cartella da cui si lancia il programma)
    public static final String FILE_UTENTI = "data/utenti.txt";
    public static final String FILE_PROIEZIONI = "data/proiezioni.txt";
    public static final String FILE_PRENOTAZIONI = "data/prenotazioni.txt";

    // Formato usato per mostrare le date/ore a video in modo leggibile
    public static final DateTimeFormatter FORMATO_DATA_ORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Costruttore privato: questa classe contiene solo costanti statiche,
    // non deve essere istanziata
    private Costanti() {
    }
}
