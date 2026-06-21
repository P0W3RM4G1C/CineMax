/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.model;

import java.time.LocalDate;

/**
 * Utente con ruolo PROIEZIONISTA: gestisce il palinsesto, cioe' inserisce,
 * modifica ed elimina le proiezioni.
 */
public class Proiezionista extends Utente {

    public Proiezionista(String nome, String cognome, String username, String passwordCifrata,
                          LocalDate dataNascita, String domicilio) {
        super(nome, cognome, username, passwordCifrata, dataNascita, domicilio);
    }

    @Override
    public Ruolo getRuolo() {
        return Ruolo.PROIEZIONISTA;
    }
}
