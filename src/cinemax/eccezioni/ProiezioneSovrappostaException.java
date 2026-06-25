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
 * Lanciata quando si cerca di aggiungere o modificare una proiezione in un
 * orario che si sovrappone con una proiezione gia' esistente (la sala e'
 * unica, quindi non puo' esserci piu' di un film proiettato alla volta).
 */
public class ProiezioneSovrappostaException extends Exception {

    public ProiezioneSovrappostaException(String messaggio) {
        super(messaggio);
    }
}
