/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.eccezioni;

/**
 * Lanciata durante il login quando username o password non sono corretti.
 */
public class CredenzialiErrateException extends Exception {

    public CredenzialiErrateException(String messaggio) {
        super(messaggio);
    }
}
