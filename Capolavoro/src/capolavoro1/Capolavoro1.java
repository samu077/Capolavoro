package capolavoro1;

/*IMPORTAZIONE DELLE CLASSI*/

import java.io.*;
import java.util.Scanner;

public class Capolavoro1 {
    /*DICHIARAZIONI DELLA VARIABILI*/
    private static final String PATHFILE = "DATABASE.txt"; //path (allocazione del file) 
    static File f = new File(PATHFILE); //creazione del file 
   
    public static void main(String[] args) throws Exception {
       Scanner in = new Scanner (System.in);
       char risposta;
        try {
            /*VERIFICA SE IL FILE ESISTE NEL CASO LO CREA*/
            if (!f.exists()) f.createNewFile();

            Accesso_Registrazione.intrudizione();

            System.out.println("-----------------------------PAGINA INIZIALE-------------------------");
            System.out.println("                      Ciao e' il tuo primo accesso?                  "); //RICHIESTA ALL'UTENTE DI ACCEDERE O REGISTRARSI

        risposta = in.nextLine().toLowerCase().charAt(0);
            System.out.println("---------------------------------------------------------------------");
            System.out.println(" "); // UN PO' DI SPAZIO 

            /*COSTRUTTO SWITCH MI PERMETTE DI AVERE DIVERSI SCENARI IN BASE ALLA RISPOSTA PRECEDENTE DELL'UTENTE */
            
            switch (risposta) {

                /*NEL CASO LA RISPOSTA PRECEDENTE SIA SI, ALLORA L'UTENTE REGISTRA IL PROPRIO ACCOUNT*/
                case 's':
                    Accesso_Registrazione.RegistrazioneAccount();

                /*NEL CASO LA RISPOSTA PRECEDENTI SIA NO, ALLORA L'UTENTE ACCEDE CON IL PROPRIO ACCOUNT CREATO IN PASSATO*/
                case 'n':
                    Accesso_Registrazione.AccessoAccount();
                    break;

                /* NEL CASO LA RISPOSTA NON SIA NE SI NE NO ALLORA STAMPA CHE E' IMPOSSIBILE*/
                default:
                    System.out.println("Impossibile.");
                    break;
            }
        } catch (IOException ex) {
            System.out.println("Qualcosa e' andato storto." + ex.getMessage());
       }
    }
}

