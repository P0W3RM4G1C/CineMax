/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.data;

import cinemax.model.Film;
import cinemax.model.Proiezione;
import cinemax.util.Costanti;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Si occupa di leggere e scrivere su file il "repository" delle proiezioni.
 * Per ogni proiezione salviamo, in un'unica riga, sia i dati del film sia
 * i dati della proiezione stessa (data/ora e costo), cosi' come previsto
 * dal file proiezioni.csv fornito dal docente.
 * Formato di ogni riga (campi separati da ';'):
 * id;titolo;genere;regista;anno;durataMinuti;etaMinima;dataOra;costoBiglietto
 * (dataOra in formato ISO, es. 2026-06-20T21:00)
 */
public class GestoreProiezioni {

    public List<Proiezione> caricaProiezioni() throws IOException {
        List<Proiezione> proiezioni = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(Costanti.FILE_PROIEZIONI))) {
            String riga;
            while ((riga = reader.readLine()) != null) {
                riga = riga.trim();
                if (riga.isEmpty() || riga.startsWith("#")) {
                    continue;
                }
                Proiezione proiezione = parseRiga(riga);
                if (proiezione != null) {
                    proiezioni.add(proiezione);
                }
            }
        }
        return proiezioni;
    }

    private Proiezione parseRiga(String riga) {
        String[] campi = riga.split(";", -1);
        if (campi.length < 9) {
            System.out.println("Attenzione: riga proiezioni.txt ignorata (formato non valido): " + riga);
            return null;
        }
        try {
            int id = Integer.parseInt(campi[0].trim());
            String titolo = campi[1];
            String genere = campi[2];
            String regista = campi[3];
            int anno = Integer.parseInt(campi[4].trim());
            int durataMinuti = Integer.parseInt(campi[5].trim());
            int etaMinima = Integer.parseInt(campi[6].trim());
            LocalDateTime dataOra = LocalDateTime.parse(campi[7].trim());
            double costoBiglietto = Double.parseDouble(campi[8].trim().replace(',', '.'));

            Film film = new Film(titolo, genere, regista, anno, durataMinuti, etaMinima);
            return new Proiezione(id, film, dataOra, costoBiglietto);
        } catch (Exception e) {
            System.out.println("Attenzione: riga proiezioni.txt ignorata (errore: " + e.getMessage() + "): " + riga);
            return null;
        }
    }

    public void salvaProiezioni(List<Proiezione> proiezioni) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Costanti.FILE_PROIEZIONI))) {
            writer.write("# id;titolo;genere;regista;anno;durataMinuti;etaMinima;dataOra;costoBiglietto");
            writer.newLine();
            for (Proiezione p : proiezioni) {
                Film f = p.getFilm();
                String riga = p.getId() + ";" + f.getTitolo() + ";" + f.getGenere() + ";" + f.getRegista() + ";"
                        + f.getAnno() + ";" + f.getDurataMinuti() + ";" + f.getEtaMinima() + ";"
                        + p.getDataOra() + ";" + String.format(Locale.US, "%.2f", p.getCostoBiglietto());
                writer.write(riga);
                writer.newLine();
            }
        }
    }
}
