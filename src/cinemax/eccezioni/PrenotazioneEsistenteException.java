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
 * Lanciata quando il proiezionista cerca di modificare o eliminare una
 * proiezione che ha gia' una o piu' prenotazioni associate: le specifiche
 * richiedono che questa operazione sia permessa solo se non ci sono
 * prenotazioni per quella proiezione.
 */
public class PrenotazioneEsistenteException extends Exception {

    public PrenotazioneEsistenteException(String messaggio) {
        super(messaggio);
    }
}
