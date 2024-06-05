package capolavoro1;

/*IMPORTAZIONE DELLE CLASSI*/

import java.io.*;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Capolavoro1 {

    /*DICHIARAZIONI DELLA VARIABILI*/
    
    private static final String PATHFILE = "DATABASE.txt"; //path (allocazione del file)
    static String Divisorio_Campi = "-";  //separatore dei campi per la scrittura nel file
    static char aCapo = '\n'; // variabile per andare a capo 
    static File f = new File(PATHFILE); //creazione del file 
    private static final Random random = new SecureRandom(); //generatrice del salt value
    private static final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // caratteri usati per la cryptazione
    private static final int iterations = 10000;
    private static final int keylength = 256;

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in); // CREAZIONE DELL OGGETTO IN PER LEGGERE DATI DI INPUT  
        
        String saltvalue = Capolavoro1.PassWordDaConfrontareSalt(40);
  
        try {

            /*VERIFICA SE IL FILE ESISTE NEL CASO LO CREA*/
            if (!f.exists()) {
                f.createNewFile();
            }

            Scanner lettore = new Scanner(f);//CREAZIONE OGGETTO LETTORE PER LEGGERE DA UN FILE 

            FileWriter myWriter = new FileWriter(PATHFILE, true); //CREAZIONE OGGETTO WRITER PER SCRIVERE NEL FILE 

            System.out.println("---------------------------------INTRODUZIONE------------------------------------");
            System.out.println("Ciao! Benvenuto nel mio capolavoro, simula un accesso con password e nome utente. ");
            System.out.println("---------------------------------------------------------------------------------");

            System.out.println(" "); // UN PO' DI SPAZIO 

            char risposta;
            System.out.println("-----------------------------PAGINA INIZIALE-------------------------");
            System.out.println("                      Ciao e' il tuo primo accesso?                  "); //RICHIESTA ALL'UTENTE DI ACCEDERE O REGISTRARSI
            risposta = in.nextLine().toLowerCase().charAt(0);
            System.out.println("---------------------------------------------------------------------");
            
            System.out.println(" "); // UN PO' DI SPAZIO 
            
            boolean isAutenticato = false; // AUTENTICAZIONE
            int tentativi = 3; // TENTATIVI DA POTER EFFETTUARE PER ACCEDERE 

            /*COSTRUTTO SWITCH MI PERMETTE DI AVERE DIVERSI SCENARI IN BASE ALLA RISPOSTA PRECEDENTE DELL'UTENTE */
            
            switch (risposta) {

                /*NEL CASO LA RISPOSTA PRECEDENTE SIA SI, ALLORA L'UTENTE REGISTRA IL PROPRIO ACCOUNT*/
                case 's':
                    
                    /*REGISTRAZIONE DI UN NUOVO ACCOUNT*/
                    System.out.println("------------ REGISTRA UN ACCOUNT -------------");
                    System.out.println("******** inserisci il nome utente : **********");
                    String NomeUtente = in.nextLine();
                    System.out.println("********* inserisci la password : ************");
                    String PassWord = in.nextLine();
                    System.out.println("----------------------------------------------");
                    System.out.println(" "); // UN PO' DI SPAZIO
                    String CryptedPass = Capolavoro1.generateSecurePassword(PassWord, saltvalue); //CRYPT DELLA PASSWORD 
                    String Linea = NomeUtente + Divisorio_Campi + CryptedPass + aCapo; //CREAZIONE DELLA VARIABILE LINEA CHE VERRA' SCRITTA NEL FILE  
                    myWriter.write(Linea); // SCRITTURA NEL FILE 
                    myWriter.close(); // PULIZIA DEL BUFFER, CHIUDENDOLO

                /*NEL CASO LA RISPOSTA PRECEDENTI SIA NO, ALLORA L'UTENTE ACCEDE CON IL PROPRIO ACCOUNT CREATO IN PASSATO*/
                case 'n':

                    do {
                        
                        /*ACCESSO DI UN ACCOUNT CREATO IN PASSATO*/
                        System.out.println("----- Accedi con il tuo account, hai " + tentativi + " tentativi poi si blocchera da solo -----");
                        System.out.println("--------------------------------- inserisci il nome utente : --------------------------------");
                        NomeUtente = in.nextLine();
                        System.out.println("--------------------------------- inserisci la passoword : ----------------------------------");
                        PassWord = in.nextLine();
                        System.out.println(" ");

                        lettore.hasNextLine(); // SALTO LA PRIMA LINEA DEL FILE
                        
                        while (lettore.hasNextLine()) {
                            String dati = lettore.nextLine(); //LETTURA DELLA PRIMA LINEA DEL FILE
                            String[] separatoreCampi = dati.split(Divisorio_Campi); // SEPARA I DUE CAMPI NOME UTENTE E PASSWORD 
                            String UserNameFromF = separatoreCampi[0]; //MEMORIZZA IL NOME UTENTE IN UNA VARIABILE
                            String PasswordFromF = separatoreCampi[1]; //MEMORIZZA LA PASSWORD IN UNA VARIABILE
                            boolean PassWordMatched = Capolavoro1.verifyUserPassword(PassWord, 
                                    PasswordFromF, saltvalue); // VERIFICA SE LA PASSWORD CRYPTATA CORRISPONDE
                            if (UserNameFromF.equals(NomeUtente) && PassWordMatched ) { // SE ENTRAMBE LE COSE SONO VERE ALLORA L'UTENTE E' AUTENTIFICATO
                                isAutenticato = true;
                            }
                        }
                        if (isAutenticato) {
                            System.out.println("Credenziali verificate puoi accedere");
                        } else {
                            System.out.println("Credenziali sbagliate");
                            tentativi--;
                        }
                    } while (tentativi != 0 && !isAutenticato); // IL CICLO CONTINUA FINCHE' NON SI FINISCONO I TENTATIVI O FINCHE' L'UTENTE NON E' VERIFICATO

                    lettore.close();
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
    

 
  /*CREAZIONE DEL METODO DOVE SI CREA UNA SERIE DI CARATTERI ATTRAVERSO IL SALT*/
    public static String PassWordDaConfrontareSalt(int length) {
        StringBuilder finalval = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(finalval);
    }

    /*QUESTO METODO CREA UN HASH*/
    public static byte[] Hasing(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

   /* CREAZIONE DEL METODO PER CRYPTARE LA PASSWORD ATTRAVERSO LA PASSWORD INSERITA DALL'UTENTE E IL SALT CREATO PRIMA*/
    public static String generateSecurePassword(String password, String salt) {
        String finalval = null;

        byte[] securePassword = Hasing(password.toCharArray(), salt.getBytes());

        finalval = Base64.getEncoder().encodeToString(securePassword);

        return finalval;
    }

    /*CREAZIONE DEL METODO PER CONFRONTARE SE LA PASSWORD CRYPTATA E UNA NUOVA CREATA ATTRAVERSO IL SALT CORRISPONDONO*/
    public static boolean verifyUserPassword(String providedPassword,
            String securedPassword, String salt) {
        boolean finalval = false;

        String newSecurePassword = generateSecurePassword(providedPassword, salt);

        finalval = newSecurePassword.equalsIgnoreCase(securedPassword);

        return finalval;
    }
}


