/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.model;

import java.time.LocalDateTime;

/**
 * Rappresenta una singola proiezione, cioe' la programmazione di un film
 * in una data e ora precisa, con un certo prezzo del biglietto.
 * La sala e' unica (cinema monosala) quindi non puo' esistere piu' di una
 * proiezione che si sovrappone nello stesso intervallo di tempo.
 */
public class Proiezione {

    // Identificativo numerico univoco, generato automaticamente
    // (vedi cinemax.util.CodiceUtil)
    private final int id;
    // Il film proiettato (titolo, genere, regista, anno, durata, eta' minima)
    private Film film;
    // Data e ora di inizio della proiezione
    private LocalDateTime dataOra;
    // Costo del biglietto per questa proiezione
    private double costoBiglietto;

    public Proiezione(int id, Film film, LocalDateTime dataOra, double costoBiglietto) {
        this.id = id;
        this.film = film;
        this.dataOra = dataOra;
        this.costoBiglietto = costoBiglietto;
    }

    public int getId() {
        return id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public double getCostoBiglietto() {
        return costoBiglietto;
    }

    public void setCostoBiglietto(double costoBiglietto) {
        this.costoBiglietto = costoBiglietto;
    }

    /**
     * Calcola l'istante in cui termina la proiezione, usando la durata
     * del film. Serve per controllare le sovrapposizioni in sala.
     */
    public LocalDateTime getDataOraFine() {
        return dataOra.plusMinutes(film.getDurataMinuti());
    }

    /**
     * Controlla se questa proiezione si sovrappone in sala con un'altra:
     * essendo un cinema monosala, due proiezioni non possono mai accavallarsi
     * nello stesso intervallo di tempo.
     */
    public boolean siSovrapponeCon(Proiezione altra) {
        // Due intervalli [inizio1,fine1) e [inizio2,fine2) si sovrappongono
        // se inizio1 < fine2 E inizio2 < fine1
        return this.dataOra.isBefore(altra.getDataOraFine())
                && altra.getDataOra().isBefore(this.getDataOraFine());
    }

    @Override
    public String toString() {
        return "Proiezione #" + id + " - " + film.getTitolo()
                + " - " + dataOra
                + " - prezzo: " + String.format("%.2f", costoBiglietto) + " EUR";
    }
}
