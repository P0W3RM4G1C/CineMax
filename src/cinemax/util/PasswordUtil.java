/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe di utilita' per cifrare le password prima di salvarle su file.
 * Le specifiche richiedono che la password venga salvata "cifrata": qui
 * usiamo l'algoritmo di hashing SHA-256, che e' una funzione "one-way"
 * (dal testo in chiaro si ottiene l'hash, ma non e' possibile fare il
 * percorso inverso). Per verificare la password al login si rifa' lo
 * stesso hash sul testo digitato e si confrontano le due stringhe.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Cifra una password in chiaro e restituisce l'hash in formato
     * esadecimale, pronto per essere salvato su file.
     */
    public static String cifra(String passwordInChiaro) {
        try {
            // Calcoliamo l'hash SHA-256 della password
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(passwordInChiaro.getBytes(StandardCharsets.UTF_8));

            // Convertiamo i byte dell'hash in una stringa esadecimale leggibile
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 e' sempre disponibile nelle JVM standard, ma gestiamo
            // comunque l'eccezione come richiesto da una buona gestione degli errori
            throw new RuntimeException("Algoritmo di cifratura non disponibile", e);
        }
    }

    /**
     * Verifica se una password in chiaro corrisponde all'hash salvato.
     */
    public static boolean verifica(String passwordInChiaro, String passwordCifrata) {
        return cifra(passwordInChiaro).equals(passwordCifrata);
    }
}
