*
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
 * Classe astratta che rappresenta un utente registrato a CineMax.
 * Le sottoclassi {@link Cliente}, {@link Proiezionista} e {@link Bigliettaio}
 * rappresentano i tre ruoli previsti dal sistema: ogni sottoclasse
 * definisce solo il proprio ruolo, i dati comuni stanno tutti qui.
 */
public abstract class Utente {

    private String nome;
    private String cognome;
    private String username;
    // La password non viene mai salvata in chiaro: qui dentro c'e' sempre
    // il valore gia' cifrato (vedi cinemax.util.PasswordUtil)
    private String passwordCifrata;
    // Campo facoltativo: puo' essere null se l'utente non l'ha indicata
    private LocalDate dataNascita;
    private String domicilio;

    public Utente(String nome, String cognome, String username, String passwordCifrata,
                   LocalDate dataNascita, String domicilio) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.passwordCifrata = passwordCifrata;
        this.dataNascita = dataNascita;
        this.domicilio = domicilio;
    }

    /**
     * Ogni sottoclasse deve indicare il proprio ruolo: questo permette di
     * salvare/ricostruire l'utente corretto leggendo il file di testo
     * (vedi cinemax.data.GestoreUtenti).
     */
    public abstract Ruolo getRuolo();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordCifrata() {
        return passwordCifrata;
    }

    public void setPasswordCifrata(String passwordCifrata) {
        this.passwordCifrata = passwordCifrata;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getNomeCompleto() {
        return nome + " " + cognome;
    }

    @Override
    public String toString() {
        return getRuolo() + " - " + getNomeCompleto() + " (username: " + username + ")";
    }
}
