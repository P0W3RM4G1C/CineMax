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
 * Lanciata quando si cerca di prenotare piu' posti di quanti ne siano
 * effettivamente liberi per una proiezione.
 */
public class PostiNonDisponibiliException extends Exception {

    public PostiNonDisponibiliException(String messaggio) {
        super(messaggio);
    }
}
