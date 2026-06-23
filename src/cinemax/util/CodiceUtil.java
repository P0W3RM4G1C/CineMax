/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.util;

import cinemax.model.Proiezione;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe di utilita' per generare identificativi univoci: l'id numerico
 * delle proiezioni e il codice alfanumerico delle prenotazioni.
 */
public final class CodiceUtil {

    // Contatore usato per generare codici di prenotazione sempre diversi
    // anche se vengono creati nello stesso istante
    private static final AtomicInteger CONTATORE_PRENOTAZIONI = new AtomicInteger(0);

    private CodiceUtil() {
    }

    /**
     * Calcola il prossimo id libero per una nuova proiezione: prende il
     * massimo tra gli id esistenti e aggiunge 1 (se la lista e' vuota
     * si parte da 1).
     */
    public static int prossimoIdProiezione(List<Proiezione> proiezioniEsistenti) {
        int massimo = 0;
        for (Proiezione p : proiezioniEsistenti) {
            if (p.getId() > massimo) {
                massimo = p.getId();
            }
        }
        return massimo + 1;
    }

    /**
     * Genera un codice univoco per una nuova prenotazione, nel formato
     * "PRN" seguito da un numero progressivo e dal timestamp corrente,
     * cosi' da evitare collisioni anche riavviando il programma.
     */
    public static String generaCodicePrenotazione() {
        int progressivo = CONTATORE_PRENOTAZIONI.incrementAndGet();
        long timestamp = System.currentTimeMillis();
        return "PRN" + timestamp + "-" + progressivo;
    }
}
