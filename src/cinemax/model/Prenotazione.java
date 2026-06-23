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
 * Rappresenta la prenotazione di uno o piu' posti per una proiezione,
 * effettuata da un cliente registrato.
 * Il costo unitario e totale non vengono salvati qui: si ricavano al
 * momento della visualizzazione leggendo il prezzo della proiezione
 * collegata (cosi' i dati non sono duplicati su file).
 */
public class Prenotazione {

    // Codice univoco generato alla creazione (vedi cinemax.util.CodiceUtil)
    private final String codice;
    // Username del cliente che ha effettuato la prenotazione
    private String usernameCliente;
    // Identificativo della proiezione a cui la prenotazione si riferisce
    private int idProiezione;
    // Numero di biglietti/posti prenotati
    private int numeroBiglietti;

    public Prenotazione(String codice, String usernameCliente, int idProiezione, int numeroBiglietti) {
        this.codice = codice;
        this.usernameCliente = usernameCliente;
        this.idProiezione = idProiezione;
        this.numeroBiglietti = numeroBiglietti;
    }

    public String getCodice() {
        return codice;
    }

    public String getUsernameCliente() {
        return usernameCliente;
    }

    public int getIdProiezione() {
        return idProiezione;
    }

    public void setIdProiezione(int idProiezione) {
        this.idProiezione = idProiezione;
    }

    public int getNumeroBiglietti() {
        return numeroBiglietti;
    }

    public void setNumeroBiglietti(int numeroBiglietti) {
        this.numeroBiglietti = numeroBiglietti;
    }

    @Override
    public String toString() {
        return "Prenotazione " + codice + " - cliente: " + usernameCliente
                + " - proiezione #" + idProiezione
                + " - biglietti: " + numeroBiglietti;
    }
}
