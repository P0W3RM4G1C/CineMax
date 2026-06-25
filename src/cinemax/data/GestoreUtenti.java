/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.data;

import cinemax.model.Bigliettaio;
import cinemax.model.Cliente;
import cinemax.model.Proiezionista;
import cinemax.model.Ruolo;
import cinemax.model.Utente;
import cinemax.util.Costanti;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Si occupa di leggere e scrivere su file il "repository" degli utenti
 * (clienti, proiezionisti, bigliettai).
 * Formato di ogni riga del file (campi separati da ';'):
 * ruolo;nome;cognome;username;passwordCifrata;dataNascita;domicilio
 * (dataNascita puo' essere una stringa vuota, dato che e' facoltativa)
 */
public class GestoreUtenti {

    /**
     * Legge il file degli utenti e restituisce la lista di oggetti Utente
     * (in realta' ognuno sara' un Cliente, un Proiezionista o un Bigliettaio
     * in base al campo ruolo letto dal file).
     */
    public List<Utente> caricaUtenti() throws IOException {
        List<Utente> utenti = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(Costanti.FILE_UTENTI))) {
            String riga;
            while ((riga = reader.readLine()) != null) {
                riga = riga.trim();
                // Ignoriamo righe vuote o di commento (che iniziano con #)
                if (riga.isEmpty() || riga.startsWith("#")) {
                    continue;
                }
                Utente utente = parseRiga(riga);
                if (utente != null) {
                    utenti.add(utente);
                }
            }
        }
        return utenti;
    }

    /**
     * Trasforma una riga del file in un oggetto Utente della sottoclasse
     * corretta, in base al ruolo indicato nel primo campo.
     */
    private Utente parseRiga(String riga) {
        // split con limite -1 per non perdere eventuali campi vuoti finali
        String[] campi = riga.split(";", -1);
        if (campi.length < 7) {
            // riga malformata: la saltiamo invece di far crashare il programma
            System.out.println("Attenzione: riga utenti.txt ignorata (formato non valido): " + riga);
            return null;
        }

        Ruolo ruolo = Ruolo.valueOf(campi[0].trim().toUpperCase());
        String nome = campi[1];
        String cognome = campi[2];
        String username = campi[3];
        String passwordCifrata = campi[4];
        String dataNascitaTesto = campi[5];
        String domicilio = campi[6];

        LocalDate dataNascita = dataNascitaTesto.isEmpty() ? null : LocalDate.parse(dataNascitaTesto);

        switch (ruolo) {
            case CLIENTE:
                return new Cliente(nome, cognome, username, passwordCifrata, dataNascita, domicilio);
            case PROIEZIONISTA:
                return new Proiezionista(nome, cognome, username, passwordCifrata, dataNascita, domicilio);
            case BIGLIETTAIO:
                return new Bigliettaio(nome, cognome, username, passwordCifrata, dataNascita, domicilio);
            default:
                return null;
        }
    }

    /**
     * Sovrascrive il file degli utenti con la lista passata come parametro.
     * Viene chiamato ogni volta che la lista cambia (es. nuova registrazione).
     */
    public void salvaUtenti(List<Utente> utenti) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Costanti.FILE_UTENTI))) {
            writer.write("# ruolo;nome;cognome;username;passwordCifrata;dataNascita;domicilio");
            writer.newLine();
            for (Utente u : utenti) {
                String dataNascitaTesto = (u.getDataNascita() == null) ? "" : u.getDataNascita().toString();
                String riga = u.getRuolo() + ";" + u.getNome() + ";" + u.getCognome() + ";"
                        + u.getUsername() + ";" + u.getPasswordCifrata() + ";"
                        + dataNascitaTesto + ";" + u.getDomicilio();
                writer.write(riga);
                writer.newLine();
            }
        }
    }
}
