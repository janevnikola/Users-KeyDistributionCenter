import java.sql.Timestamp;
import java.util.Date;


import java.util.Hashtable;

import java.util.Random;

class User {
    private int ID;
    private String kluc; //="sdsdadasdas5sssf" samo so setString posle vo main
    //a kaj KDC server random string generate za sesiski kluc???

    public User(int ID, String kluc) {
        this.ID = ID;
        this.kluc = kluc;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getKluc() {
        return kluc;
    }

    public void setKluc(String kluc) {
        this.kluc = kluc;
    }

    //generiranje random vrednost za nonce
    public int generate_Nonce() {
        Random random = new Random(); //instance of random class
        int gorna_granica = 100;
        //generate random values from 0-24
        int nonce = random.nextInt(gorna_granica);
        System.out.println(nonce);
        return nonce;
    }

}


class KDCServer {
    // private User Bob;
    //private User Alice;
    public Hashtable klucevi;
    private String sesiski_kluc;
    private int alice;

    //Alice.getID(),Bob.getID(),Alice.getKluc(),Bob.getKluc());
    public KDCServer(int ID_Alice, int ID_Bob, String Alice_kluc, String Bob_kluc) {
        // Hashtable<Integer, String>
        klucevi = new Hashtable<Integer, String>();
        klucevi.put(ID_Alice, Alice_kluc);
        klucevi.put(ID_Bob, Bob_kluc);
        alice = ID_Alice;
    }

    public String getSesiski_kluc() {
        return sesiski_kluc;
    }


    public byte[] getKripto() {
        return kripto;
    }

    byte[] kripto;
    public String poraka;

    //public String kript;
    public void receiveKDC(int id_Alice, int id_Bob, int nonce_Alice) {
        try {
            sesiski_kluc = generiraj();
            Timestamp timestamp = generate_timestamp();
            //instanca AES i aes.encrypt ama seto toa treba vo string da go napravime
            // poraka = new String();
            poraka += sesiski_kluc;
            poraka += " ";
            poraka += timestamp.toString();
            poraka += " ";
            poraka += String.valueOf(id_Alice);
            poraka += " ";
            poraka += String.valueOf(id_Bob);

            String kluc_alice = String.valueOf(klucevi.get(id_Alice));
            System.out.println("KLuc " + kluc_alice);
            byte[] kriptirano = AES.encrypt(poraka.getBytes("UTF-8"), kluc_alice);// so klucot na ALice
            System.out.println(kriptirano);//kriptirano
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String generiraj() {
        int n = 33;
        // lower limit for LowerCase Letters
        int lowerLimit = 97;

        // lower limit for LowerCase Letters
        int upperLimit = 122;

        Random random = new Random();

        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer(n);

        for (int i = 0; i < n; i++) {

            // take a random value between 97 and 122
            int nextRandomChar = lowerLimit
                    + (int) (random.nextFloat()
                    * (upperLimit - lowerLimit + 1));

            // append a character at the end of bs
            r.append((char) nextRandomChar);
        }

        // return the resultant string
        return r.toString();
    }

    /*
        public static Key generateSymmetricKey() throws Exception {
            KeyGenerator generator = KeyGenerator.getInstance( "AES" );
            SecretKey key = generator.generateKey();
            return key;
        }
      */
    public Timestamp generate_timestamp() {
        //Date object
        Date date = new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class
        Timestamp timestamp = new Timestamp(time);
        System.out.println("Current Time Stamp: " + time);
        return timestamp;
    }


}


public class KerberosProtocol {
    //prakjanje od alice do KDC
    public static void request_Alice(KDCServer server, int id_alice, int id_Bob, int nonce_alice) {
        server.receiveKDC(id_alice, id_Bob, nonce_alice);
    }

    public static void main(String args[]) {
        System.out.println("Zdravo");
        User Alice = new User(1, "28FDDEF86DA4244ACCC0A4FE3B316F26");
        User Bob = new User(2, "BFE2BF904559FAB2A16480B4F7F1CBD8");
        KDCServer kdc_server = new KDCServer(Alice.getID(), Bob.getID(), Alice.getKluc(), Bob.getKluc());

        int nonce_Alice = Alice.generate_Nonce();
        System.out.println("Nonce na alice: " + nonce_Alice);
        System.out.println("Timestamp na kDC:" + kdc_server.generate_timestamp());
        //alice isprakja request do kdc server
        request_Alice(kdc_server, Alice.getID(), Bob.getID(), nonce_Alice);//izlez: [B@5680a178
        //  kdc_server.receiveKDC(Alice.getID(),Bob.getID(),nonce_Alice);
        //receive_Alice(kdc_server);

        //  System.out.println("Kriptirano: "+kdc_server.getKripto());
    }
}
