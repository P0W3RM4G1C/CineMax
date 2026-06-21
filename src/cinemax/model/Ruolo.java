/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.model;

/**
 * Rappresenta il ruolo di un utente all'interno del sistema CineMax.
 * Ogni utente registrato ha uno ed un solo ruolo tra quelli elencati.
 */
public enum Ruolo {
    /** Cliente: cerca proiezioni e gestisce le proprie prenotazioni. */
    CLIENTE,
    /** Proiezionista: gestisce il palinsesto (film e proiezioni). */
    PROIEZIONISTA,
    /** Bigliettaio: cerca e visualizza le prenotazioni dei clienti. */
    BIGLIETTAIO
}
