CineMax - Istruzioni di installazione, compilazione ed esecuzione
====================================================================

Requisiti:
- Java JDK 11 o superiore installato (multipiattaforma: Windows, macOS, Linux)
- Nessuna libreria esterna richiesta (la cartella lib/ e' vuota)

Struttura del repository:
- autori.txt        elenco autori del progetto
- doc/               manuale utente, manuale tecnico, javadoc generata
- src/                codice sorgente Java (package cinemax)
- bin/                file eseguibile CineMax.jar
- data/               file di testo con i dati (utenti, proiezioni, prenotazioni)
- lib/                eventuali librerie esterne (vuota in questo progetto)

------------------------------------------------------------------
COMPILAZIONE (da riga di comando, posizionati nella cartella radice CineMax)
------------------------------------------------------------------

1) Creare la cartella per i file compilati (se non esiste già):

   Linux/macOS:  mkdir -p out
   Windows:      mkdir out

2) Compilare tutti i file sorgenti:

   Linux/macOS:
   javac -d out -encoding UTF-8 $(find src -name "*.java")

   Windows (PowerShell) - javac non espande i caratteri "*", quindi si genera
   prima un elenco dei file con i percorsi tra virgolette e si passa a javac
   con la sintassi @file (attenzione: usare "/" e non "\" nei percorsi
   all'interno del file elenco, perché javac interpreta "\" come carattere
   di escape):

   Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { '"' + ($_.FullName -replace '\\','/') + '"' } | Set-Content sources.txt
   javac -d out -encoding UTF-8 --% @sources.txt

------------------------------------------------------------------
CREAZIONE DEL FILE ESEGUIBILE (.jar)
------------------------------------------------------------------

3) Il file manifest e' gia' incluso come bin/MANIFEST.MF (contiene solo la
   riga "Main-Class: cinemax.CineMax"). Generare il jar con:
   jar cfm bin/CineMax.jar bin/MANIFEST.MF -C out .

   Questo comando crea bin/CineMax.jar. Va rieseguito ogni volta che si
   modifica il codice sorgente (dopo aver ricompilato al passo 2).

------------------------------------------------------------------
ESECUZIONE
------------------------------------------------------------------

4) Eseguire l'applicazione dalla cartella radice del progetto (importante:
   l'applicazione legge/scrive i file dati nella cartella data/ con percorso
   relativo "data/..."):

   java -jar bin/CineMax.jar

   In alternativa, senza creare il jar, eseguire direttamente le classi compilate:

   java -cp out cinemax.CineMax

------------------------------------------------------------------
NOTE
------------------------------------------------------------------

- I dati vengono salvati in file di testo (formato CSV con separatore ';')
  nella cartella data/: utenti.txt, proiezioni.txt, prenotazioni.txt
- Il file data/utenti.txt contiene già 2 utenti con ruolo "proiezionista"
  (username proiezionista1, proiezionista2) e 5 utenti con ruolo
  "bigliettaio" (username bigliettaio1...bigliettaio5), come richiesto
  dalle specifiche. La password di prova per tutti questi account e'
  "cinemax123". E' presente anche un cliente di esempio (username
  "cliente1", password "cliente123").
- Il file data/proiezioni.txt contiene le proiezioni reali fornite dal
  docente (file proiezioni.csv), convertite nel formato interno
  dell'applicazione (id;titolo;genere;regista;anno;durataMinuti;
  etaMinima;dataOra;costoBiglietto). Sono 8878 proiezioni con date sia
  passate (2018-2026) sia future (2026-2027), utili per testare sia
  eliminaPrenotazione/visualizzazioni storiche sia creaPrenotazione su
  proiezioni future.
- Eseguire sempre il programma a partire dalla cartella radice del progetto,
  altrimenti i percorsi relativi ai file dati non verranno trovati.

------------------------------------------------------------------
STATO DEL PROGETTO
------------------------------------------------------------------

- Tutto il codice sorgente (modello, eccezioni, persistenza su file,
  logica applicativa, interfaccia testuale e classe main) e' stato scritto,
  compilato ed eseguito con successo (JDK 26, testato su Windows). Il
  comando "javac" non genera errori e l'applicazione si avvia mostrando
  correttamente il menu principale, il login con gli account di prova e i
  menu specifici per ciascun ruolo.
- bin/CineMax.jar va generato eseguendo i passi 1-3 di questo file (il jar
  compilato non viene incluso pre-costruito nella consegna, perché dipende
  dalla versione del JDK usata sulla macchina di destinazione).
- Manuale utente, manuale tecnico e javadoc (cartella doc/) non sono stati
  ancora prodotti: il codice contiene già commenti in stile javadoc che
  possono essere usati come base.
