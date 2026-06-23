/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.data;

import cinemax.model.Prenotazione;
import cinemax.util.Costanti;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Si occupa di leggere e scrivere su file il "repository" delle prenotazioni.
 * Formato di ogni riga (campi separati da ';'):
 * codice;usernameCliente;idProiezione;numeroBiglietti
 */
public class GestorePrenotazioni {

    public List<Prenotazione> caricaPrenotazioni() throws IOException {
        List<Prenotazione> prenotazioni = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(Costanti.FILE_PRENOTAZIONI))) {
            String riga;
            while ((riga = reader.readLine()) != null) {
                riga = riga.trim();
                if (riga.isEmpty() || riga.startsWith("#")) {
                    continue;
                }
                Prenotazione prenotazione = parseRiga(riga);
                if (prenotazione != null) {
                    prenotazioni.add(prenotazione);
                }
            }
        }
        return prenotazioni;
    }

    private Prenotazione parseRiga(String riga) {
        String[] campi = riga.split(";", -1);
        if (campi.length < 4) {
            System.out.println("Attenzione: riga prenotazioni.txt ignorata (formato non valido): " + riga);
            return null;
        }
        try {
            String codice = campi[0];
            String usernameCliente = campi[1];
            int idProiezione = Integer.parseInt(campi[2].trim());
            int numeroBiglietti = Integer.parseInt(campi[3].trim());
            return new Prenotazione(codice, usernameCliente, idProiezione, numeroBiglietti);
        } catch (Exception e) {
            System.out.println("Attenzione: riga prenotazioni.txt ignorata (errore: " + e.getMessage() + "): " + riga);
            return null;
        }
    }

    public void salvaPrenotazioni(List<Prenotazione> prenotazioni) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Costanti.FILE_PRENOTAZIONI))) {
            writer.write("# codice;usernameCliente;idProiezione;numeroBiglietti");
            writer.newLine();
            for (Prenotazione p : prenotazioni) {
                String riga = p.getCodice() + ";" + p.getUsernameCliente() + ";"
                        + p.getIdProiezione() + ";" + p.getNumeroBiglietti();
                writer.write(riga);
                writer.newLine();
            }
        }
    }
}
