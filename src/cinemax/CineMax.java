/*
 * Progetto CineMax - Laboratorio Interdisciplinare A 2025/2026
 * Autori:
 * - Alessandro Panarotto, matricola 757930, sede VA
 * - Federico Trentini, matricola 760478, sede VA
 * - Mohan Thomas Paolo, matricola 761573, sede VA
 * - Davide Paolo Calabrese, matricola 763012, sede VA
 */
package cinemax;

import cinemax.eccezioni.CredenzialiErrateException;
import cinemax.eccezioni.UsernameEsistenteException;
import cinemax.model.Bigliettaio;
import cinemax.model.Cliente;
import cinemax.model.Proiezionista;
import cinemax.model.Utente;
import cinemax.service.ServizioPrenotazioni;
import cinemax.service.ServizioProiezioni;
import cinemax.service.ServizioUtenti;
import cinemax.ui.MenuBigliettaio;
import cinemax.ui.MenuCliente;
import cinemax.ui.MenuGuest;
import cinemax.ui.MenuProiezionista;
import cinemax.util.InputUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Classe principale del programma CineMax. Contiene il metodo main() e
 * mostra il menu iniziale: login, registrazione di un nuovo cliente, oppure
 * continuare come utente non registrato (guest) per cercare proiezioni.
 *
 * Nota: il programma va eseguito dalla cartella principale del progetto,
 * perche' i file di dati vengono letti/scritti con percorsi relativi
 * (cartella "data/").
 */
public class CineMax {

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("        Benvenuto su CineMax");
        System.out.println("====================================");

        ServizioUtenti servizioUtenti;
        ServizioProiezioni servizioProiezioni;
        ServizioPrenotazioni servizioPrenotazioni;
        try {
            servizioUtenti = new ServizioUtenti();
            servizioProiezioni = new ServizioProiezioni();
            servizioPrenotazioni = new ServizioPrenotazioni();
        } catch (IOException e) {
            // Se non riusciamo nemmeno a leggere i file di dati iniziali,
            // il programma non puo' funzionare correttamente: terminiamo.
            System.out.println("Errore durante la lettura dei file di dati: " + e.getMessage());
            System.out.println("Verifica che la cartella 'data' esista e contenga i file richiesti.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean continua = true;

        while (continua) {
            System.out.println();
            System.out.println("=== Menu Principale ===");
            System.out.println("1) Login");
            System.out.println("2) Registrati come nuovo cliente");
            System.out.println("3) Continua come visitatore (cerca un film)");
            System.out.println("4) Esci");
            int scelta = InputUtil.leggiInteroInRange(scanner, "Scelta: ", 1, 4);

            switch (scelta) {
                case 1:
                    effettuaLogin(scanner, servizioUtenti, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 2:
                    registraCliente(scanner, servizioUtenti);
                    break;
                case 3:
                    String nomeFilm = InputUtil.leggiStringaObbligatoria(scanner, "Inserisci il nome (anche parziale) del film che cerchi: ");
                    MenuGuest.avviaConNomeFilm(scanner, nomeFilm, servizioProiezioni, servizioPrenotazioni);
                    break;
                case 4:
                    continua = false;
                    break;
            }
        }

        System.out.println("Grazie per aver usato CineMax. Arrivederci!");
        scanner.close();
    }

    /**
     * Gestisce il login: chiede username e password, e in base al ruolo
     * dell'utente autenticato mostra il menu corrispondente.
     */
    private static void effettuaLogin(Scanner scanner, ServizioUtenti servizioUtenti,
                                       ServizioProiezioni servizioProiezioni,
                                       ServizioPrenotazioni servizioPrenotazioni) {
        String username = InputUtil.leggiStringaObbligatoria(scanner, "Username: ");
        String password = InputUtil.leggiStringaObbligatoria(scanner, "Password: ");

        Utente utente;
        try {
            utente = servizioUtenti.login(username, password);
        } catch (CredenzialiErrateException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Login effettuato. Benvenuto, " + utente.getNomeCompleto() + "!");

        // In base al ruolo dell'utente autenticato, mostriamo il menu giusto
        if (utente instanceof Cliente) {
            MenuCliente.avvia(scanner, (Cliente) utente, servizioProiezioni, servizioPrenotazioni);
        } else if (utente instanceof Proiezionista) {
            MenuProiezionista.avvia(scanner, (Proiezionista) utente, servizioProiezioni, servizioPrenotazioni);
        } else if (utente instanceof Bigliettaio) {
            MenuBigliettaio.avvia(scanner, (Bigliettaio) utente, servizioUtenti, servizioProiezioni, servizioPrenotazioni);
        }
    }

    /**
     * Implementa registraCliente(): chiede tutti i dati necessari per
     * creare un nuovo account cliente.
     */
    private static void registraCliente(Scanner scanner, ServizioUtenti servizioUtenti) {
        System.out.println();
        System.out.println("Registrazione nuovo cliente:");
        String nome = InputUtil.leggiStringaObbligatoria(scanner, "Nome: ");
        String cognome = InputUtil.leggiStringaObbligatoria(scanner, "Cognome: ");
        String username = InputUtil.leggiStringaObbligatoria(scanner, "Scegli un username: ");
        String password = InputUtil.leggiStringaObbligatoria(scanner, "Scegli una password: ");

        LocalDate dataNascita = null;
        if (InputUtil.leggiSiNo(scanner, "Vuoi indicare la tua data di nascita? (facoltativo)")) {
            dataNascita = InputUtil.leggiData(scanner, "Data di nascita");
        }
        String domicilio = InputUtil.leggiStringa(scanner, "Domicilio (facoltativo): ");

        try {
            servizioUtenti.registraCliente(nome, cognome, username, password, dataNascita,
                    domicilio.isEmpty() ? null : domicilio);
            System.out.println("Registrazione completata! Ora puoi effettuare il login.");
        } catch (UsernameEsistenteException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio dei dati: " + e.getMessage());
        }
    }
}
