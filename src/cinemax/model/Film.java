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
 * Rappresenta un film del catalogo di CineMax.
 * Un film puo' essere proiettato in piu' giorni/orari diversi: ogni
 * proiezione (vedi {@link Proiezione}) fa riferimento ad un oggetto Film.
 */
public class Film {

    // Titolo del film (es. "Inception")
    private String titolo;
    // Genere del film (es. "Fantascienza", "Commedia", ...)
    private String genere;
    // Nome del regista
    private String regista;
    // Anno di uscita
    private int anno;
    // Durata in minuti (serve per calcolare quando finisce la proiezione)
    private int durataMinuti;
    // Eta' minima consigliata per la visione (0 se nessun limite)
    private int etaMinima;

    /**
     * Crea un nuovo film con tutti i suoi dati.
     */
    public Film(String titolo, String genere, String regista, int anno, int durataMinuti, int etaMinima) {
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durataMinuti = durataMinuti;
        this.etaMinima = etaMinima;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public String getRegista() {
        return regista;
    }

    public void setRegista(String regista) {
        this.regista = regista;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getDurataMinuti() {
        return durataMinuti;
    }

    public void setDurataMinuti(int durataMinuti) {
        this.durataMinuti = durataMinuti;
    }

    public int getEtaMinima() {
        return etaMinima;
    }

    public void setEtaMinima(int etaMinima) {
        this.etaMinima = etaMinima;
    }

    /**
     * Restituisce una descrizione leggibile del film, utile per stamparlo
     * a video nel menu testuale.
     */
    @Override
    public String toString() {
        return titolo + " (" + anno + ") - genere: " + genere
                + " - regista: " + regista
                + " - durata: " + durataMinuti + " min"
                + " - eta' minima: " + (etaMinima > 0 ? etaMinima + "+" : "nessuna");
    }
}
