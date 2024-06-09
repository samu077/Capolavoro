package capolavoro1;

import java.io.*;
import java.util.Scanner;

public class Accesso_Registrazione {

    /*DICHIARAZIONI DELLA VARIABILI*/
    private static final String PATHFILE = "DATABASE.txt"; //path (allocazione del file)
    static String Divisorio_Campi = "-";  //separatore dei campi per la scrittura nel file
    static char aCapo = '\n'; // variabile per andare a capo 
    static File f = new File(PATHFILE); //creazione del file

    static Scanner in = new Scanner(System.in); // CREAZIONE DELL OGGETTO IN PER LEGGERE DATI DI INPUT

    public static void intrudizione() {
        System.out.println("---------------------------------INTRODUZIONE------------------------------------");
        System.out.println("Ciao! Benvenuto nel mio capolavoro, simula un accesso con password e nome utente. ");
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println(" ");
    }

    public static void RegistrazioneAccount() {
        /*REGISTRAZIONE DI UN NUOVO ACCOUNT*/
        try {

            FileWriter myWriter = new FileWriter(PATHFILE, true); //CREAZIONE OGGETTO WRITER PER SCRIVERE NEL FILE 

            System.out.println("------------ REGISTRA UN ACCOUNT -------------");
            System.out.println("******** inserisci il nome utente : **********");
            String NomeUtente = in.nextLine();
            System.out.println("********* inserisci la password : ************");
            String PassWord = in.nextLine();
            System.out.println("----------------------------------------------");
            System.out.println(" "); // UN PO' DI SPAZIO

            String computed_hash = CryptPassword.hashPassword(PassWord);
            String Linea = NomeUtente + Divisorio_Campi + computed_hash + aCapo; //CREAZIONE DELLA VARIABILE LINEA CHE VERRA' SCRITTA NEL FILE  
            myWriter.write(Linea); // SCRITTURA NEL FILE 
            myWriter.close(); // PULIZIA DEL BUFFER, CHIUDENDOLO
        } catch (IOException ex) {
            System.out.println("Qualcosa e' andato storto." + ex.getMessage());
        }
    }

    public static void AccessoAccount() throws FileNotFoundException {
       
        boolean isAutenticato = false; // AUTENTICAZIONE
        int tentativi = 3; // TENTATIVI DA POTER EFFETTUARE PER ACCEDERE 
        do {

            /*ACCESSO DI UN ACCOUNT CREATO IN PASSATO*/
            System.out.println("----- Accedi con il tuo account, hai " + tentativi + " tentativi poi si blocchera da solo -----");
            System.out.println("--------------------------------- inserisci il nome utente : --------------------------------");
            String NomeUtente = in.nextLine();
            System.out.println("--------------------------------- inserisci la passoword : ----------------------------------");
            String PassWord = in.nextLine();
            System.out.println(" ");

            Scanner lettore = new Scanner(f);//CREAZIONE OGGETTO LETTORE PER LEGGERE DA UN FILE
            
            while (lettore.hasNextLine() && !isAutenticato) {
                
                
                    String dati = lettore.nextLine(); //LETTURA DELLA PRIMA LINEA DEL FILE
                    if(!dati.isBlank()){ // SE LA LINEA NON E' BIANCA ALLORA ESEGUE LE ISTRUZIONI
                    String[] separatoreCampi = dati.split(Divisorio_Campi); // SEPARA I DUE CAMPI NOME UTENTE E PASSWORD 
                    String UserNameFromF = separatoreCampi[0]; //MEMORIZZA IL NOME UTENTE IN UNA VARIABILE
                    String PasswordFromF = separatoreCampi[1]; //MEMORIZZA LA PASSWORD IN UNA VARIABILE
                    
                    if (UserNameFromF.equals(NomeUtente)) {

                        isAutenticato = CryptPassword.checkPassword(PassWord, PasswordFromF);
                    }
                    }
                
            }
            lettore.close();
            if (isAutenticato) {
                System.out.println("Credenziali verificate puoi accedere");
            } else {
                System.out.println("Credenziali sbagliate");
                tentativi--;
            }
        } while (tentativi != 0 && !isAutenticato); // IL CICLO CONTINUA FINCHE' NON SI FINISCONO I TENTATIVI O FINCHE' L'UTENTE NON E' VERIFICATO

        
    }
}
