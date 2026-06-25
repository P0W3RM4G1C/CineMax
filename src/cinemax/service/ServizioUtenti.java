/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.service;

import cinemax.data.GestoreUtenti;
import cinemax.eccezioni.CredenzialiErrateException;
import cinemax.eccezioni.UsernameEsistenteException;
import cinemax.model.Cliente;
import cinemax.model.Utente;
import cinemax.util.PasswordUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Contiene la logica applicativa relativa agli utenti: login e
 * registrazione di nuovi clienti. Mantiene in memoria la lista di tutti
 * gli utenti caricata dal file, e la tiene sincronizzata con il file ogni
 * volta che cambia.
 */
public class ServizioUtenti {

    private final GestoreUtenti gestoreUtenti;
    private final List<Utente> utenti;

    public ServizioUtenti() throws IOException {
        this.gestoreUtenti = new GestoreUtenti();
        this.utenti = gestoreUtenti.caricaUtenti();
    }

    /**
     * Cerca un utente per username, ignorando maiuscole/minuscole.
     * Restituisce null se non esiste.
     */
    public Utente trovaPerUsername(String username) {
        for (Utente u : utenti) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Effettua il login: controlla che l'username esista e che la password
     * (una volta cifrata) corrisponda a quella salvata.
     */
    public Utente login(String username, String password) throws CredenzialiErrateException {
        Utente utente = trovaPerUsername(username);
        if (utente == null || !PasswordUtil.verifica(password, utente.getPasswordCifrata())) {
            throw new CredenzialiErrateException("Username o password non corretti.");
        }
        return utente;
    }

    /**
     * Registra un nuovo cliente: controlla che l'username non sia già in
     * uso, cifra la password, crea il Cliente e lo salva su file.
     */
    public Cliente registraCliente(String nome, String cognome, String username, String password,
                                    LocalDate dataNascita, String domicilio)
            throws UsernameEsistenteException, IOException {

        if (trovaPerUsername(username) != null) {
            throw new UsernameEsistenteException("L'username '" + username + "' e' gia' in uso, scegline un altro.");
        }

        String passwordCifrata = PasswordUtil.cifra(password);
        Cliente nuovoCliente = new Cliente(nome, cognome, username, passwordCifrata, dataNascita, domicilio);
        utenti.add(nuovoCliente);
        gestoreUtenti.salvaUtenti(utenti);
        return nuovoCliente;
    }
}
