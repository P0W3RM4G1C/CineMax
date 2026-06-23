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
 * Lanciata quando una data fornita dall'utente non rispetta i vincoli
 * richiesti dalle specifiche (es. la modifica di una prenotazione richiede
 * che sia la vecchia sia la nuova data di proiezione siano successive ad
 * oggi; l'eliminazione richiede invece che la data di proiezione sia
 * precedente ad oggi).
 */
public class DataNonValidaException extends Exception {

    public DataNonValidaException(String messaggio) {
        super(messaggio);
    }
}
