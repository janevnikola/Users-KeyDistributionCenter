import java.sql.SQLOutput;
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

    private int nonce_A;

    public int getNonce_A() {
        return nonce_A;
    }

    //generiranje random vrednost za nonce
    public int generate_Nonce() {
        Random random = new Random(); //instance of random class
        int gorna_granica = 100;
        //generate random values from 0-24
        int nonce = random.nextInt(gorna_granica);
       // System.out.println(nonce);
        nonce_A = nonce;
        return nonce;
    }

}


class KDCServer {
    // private User Bob;
    //private User Alice;
    private String Alice_key;

    private String Bob_key;
    private String sesiski_kluc;
    private int alice;

    //Alice.getID(),Bob.getID(),Alice.getKluc(),Bob.getKluc());
    public KDCServer() {

    }


    public String getAlice_key() {
        return Alice_key;
    }

    public void setAlice_key(String alice_key) {
        Alice_key = alice_key;
    }

    public String getBob_key() {
        return Bob_key;
    }

    private int lifetimeT;


    public int getLifetimeT() {
        return lifetimeT;
    }

    public int GenerateLifetime() {
        int min = 180;//3
        int max = 600;//10 min
        int random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);
        lifetimeT = random_int;
        return random_int;
    }


    public void setBob_key(String bob_key) {
        Bob_key = bob_key;
    }

    public String getSesiski_kluc() {
        return sesiski_kluc;
    }


    public byte[] getKripto() {
        return kripto;
    }

    public byte[] getKriptiranaAlice() {
        return kriptiranaAlice;
    }

    public byte[] getKriptiranaBob() {
        return kriptiranaBob;
    }

    byte[] kripto;
    public String poraka_aliceKEy;
    public String poraka_bobKey;

    public byte[] kriptiranaAlice;
    public byte[] kriptiranaBob;

    //public String kript;
    public void receiveKDC(int id_Alice, int id_Bob, int nonce_Alice) {
        try {
            int lifetime = GenerateLifetime();
            sesiski_kluc = generiraj();
            Timestamp timestamp = generate_timestamp();
            //instanca AES i aes.encrypt ama seto toa treba vo string da go napravime
            // poraka = new String();
            poraka_aliceKEy += sesiski_kluc;//sessiski
            poraka_aliceKEy += " ";
            poraka_aliceKEy += String.valueOf(lifetime);
            poraka_aliceKEy += " ";
            poraka_aliceKEy += String.valueOf(nonce_Alice);//nonce alice
            poraka_aliceKEy += " ";
            poraka_aliceKEy += String.valueOf(id_Bob);//id bob
//za bob

            poraka_bobKey += sesiski_kluc;
            poraka_bobKey += " ";
            poraka_bobKey += String.valueOf(id_Alice);
            poraka_bobKey += " ";
            poraka_bobKey += String.valueOf(lifetime);
            System.out.println("Vrednosti koi treba da se kriptiraat: " + poraka_aliceKEy);
            byte[] kriptirano_aliceKey = AES.encrypt(poraka_aliceKEy.getBytes("UTF-8"), Alice_key);// so klucot na ALice
           // System.out.println("Kriptirana poraka so Alice Key: " + kriptirano_aliceKey.toString());//kriptirano
            kriptiranaAlice = kriptirano_aliceKey;
            StringBuilder ak = new StringBuilder();
            for (byte b : kriptirano_aliceKey) {
                ak.append(String.format("%02X ", b));//porakata da ja pretocime vo 02x?
            }
            System.out.println("Kriptirano so ALice key:" + ak.toString());//enkriptirana poraka
            byte[] kriptirano_bobKey = AES.encrypt(poraka_bobKey.getBytes("UTF-8"), Bob_key);
          //  System.out.println("Kriptirana poraka so Bob Key" + kriptirano_bobKey.toString());

            StringBuilder bk = new StringBuilder();
            for (byte b : kriptirano_bobKey) {
                bk.append(String.format("%02X ", b));//porakata da ja pretocime vo 02x?
            }
            kriptiranaBob = kriptirano_bobKey;
            System.out.println("Kriptirana poraka Bob Key: " + bk.toString());

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
    private Timestamp timestampSaved;

    public Timestamp getTimestampSaved() {
        return timestampSaved;
    }

    public Timestamp generate_timestamp() {
        //Date object
        Date date = new Date();
        //getTime() returns current time in milliseconds
        long time = date.getTime();
        //Passed the milliseconds to constructor of Timestamp class
        Timestamp timestamp = new Timestamp(time);
        System.out.println("Current Time Stamp: " + time);
        timestampSaved = timestamp;
        return timestamp;
    }


}


public class KerberosProtocol {
    //prakjanje od alice do KDC
    public static byte[] kriptiranoAB;
    public static String klucot;

    public static void request_Alice(KDCServer server, int id_alice, int id_Bob, int nonce_alice) {
        System.out.println("\n");
        System.out.println("Request od Alice do KDC");
        server.receiveKDC(id_alice, id_Bob, nonce_alice);
    }

    public static void KDCToAlice(KDCServer kdc_server, User Alice, User Bob) {
        System.out.println("\n");
        System.out.println("KDC TO ALICE: ");
        //  System.out.println("ALice bytes:" + kdc_server.getKriptiranaAlice());
        //  System.out.println("Bob bytes: " + kdc_server.getKriptiranaBob());
        byte[] decryptedString = AES.decrypt(kdc_server.getKriptiranaAlice(), kdc_server.getAlice_key());
      //  StringBuilder ak = new StringBuilder();
        //    for (byte b : kdc_server.getKriptiranaAlice()) {
        //        ak.append(String.format("%02X ", b));//porakata da ja pretocime vo 02x?
        //     }
        // System.out.println("Test kript: "+ak.toString());

        //System.out.println(new String(decryptedString));


        // string [] parts = string.split()
        String str = new String(decryptedString);
        String[] arrOfStr = str.split(" ", 4);


        String kluc = arrOfStr[0];
        int lifetime = Integer.parseInt(arrOfStr[1]);
        int nonce_Alice = Integer.parseInt(arrOfStr[2]);
        // int idBob= Integer.parseInt(arrOfStr[3]);
        klucot = kluc;
    //    System.out.println("kluc: " + kluc);

     //   System.out.println("Lifetime: " + lifetime);

       // System.out.println("Nonce na alice: " + nonce_Alice);

        System.out.println("Verificiranje: ");

        if (nonce_Alice == Alice.getNonce_A()) {
            if (kdc_server.getLifetimeT() == lifetime) {
                System.out.println("Nisto ne e smeneto");
            } else {
                System.out.println("Porakata se otfrla bidejki e smeneta");
            }
        }
        Timestamp timestamp = kdc_server.generate_timestamp();
        String poraka = new String();
        poraka += timestamp.toString();
        poraka += " ";
        poraka += Alice.getID();

        try {
            byte[] kriptirano_AB = AES.encrypt(poraka.getBytes("UTF-8"), kluc);// so klucot na ALice
          //  System.out.println("Kriptirana poraka so Alice Key: " + kriptirano_AB.toString());//kriptirano


            //  kriptiranaAlice = kriptirano_aliceKey;
            StringBuilder ab = new StringBuilder();
            for (byte b : kriptirano_AB) {
                ab.append(String.format("%02X ", b));//porakata da ja pretocime vo 02x?
            }
            kriptiranoAB = kriptirano_AB;
            System.out.println("Kriptirana poraka AB: " + ab.toString());
            //System.out.println("Porakata glasi: " + poraka);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void AliceToBob(KDCServer kdcServer, User alice, User bob) {
        System.out.println("\n");
        System.out.println("Alice to BOb: ");
       // System.out.println(kriptiranoAB);
        byte[] dekriptirano = AES.decrypt(kriptiranoAB, klucot);

        StringBuilder test = new StringBuilder();
     for (byte b : kriptiranoAB) {
          test.append(String.format("%02X ", b));//porakata da ja pretocime vo 02x?
       }
        System.out.println("Kriptirana poraka AB:  " + test.toString());


        System.out.println(new String(dekriptirano));

        String strYab = new String(dekriptirano);

        String[] nizaStr = strYab.split(" ", 3);

        String timestampPRVDEL = nizaStr[0];

        String timestampVTORDEL = nizaStr[1];
        String rezultatTimestamp = new String();

        rezultatTimestamp += timestampPRVDEL;
        rezultatTimestamp += " ";
        rezultatTimestamp += timestampVTORDEL;

        int idAodAB = Integer.parseInt(nizaStr[2]);




        //dekripcija so klucot na bob

        byte[] dekriptiranoBob = AES.decrypt(kdcServer.getKriptiranaBob(), kdcServer.getBob_key());
        System.out.println("Dekripcija na Yb: ");
      //  System.out.println(new String(dekriptiranoBob));

//dekripcija prvo na ybob
        String strYbob = new String(dekriptiranoBob);
        String[] arrOfStr = strYbob.split(" ", 4);

        String klucOdYbob = arrOfStr[0];
        int id_ALice = Integer.parseInt(arrOfStr[1]);

        int lifetimeYbob = Integer.parseInt(arrOfStr[2]);

        String timestampSaved = String.valueOf(kdcServer.getTimestampSaved());
        if (idAodAB == id_ALice) {
            if (lifetimeYbob == kdcServer.getLifetimeT()) {
                if (rezultatTimestamp.equals(timestampSaved)) {
                    System.out.println("Porakata ne e izmeneta");
                }
            }
        }
        else{
            System.out.println("Porakata bila izmeneta");
        }
    }


    public static void main(String args[]) {

        User Alice = new User(1, "thesecretkeyAlice");
        User Bob = new User(2, "thesecretkeyBob");
        KDCServer kdc_server = new KDCServer();

        kdc_server.setAlice_key("thesecretkeyAlice");
        kdc_server.setBob_key("thesecretkeyBob");

        int nonce_Alice = Alice.generate_Nonce();

        System.out.println("Timestamp na KDC:" + kdc_server.generate_timestamp());
        //alice isprakja request do kdc server
        request_Alice(kdc_server, Alice.getID(), Bob.getID(), nonce_Alice);//izlez: [B@5680a178
        //  kdc_server.receiveKDC(Alice.getID(),Bob.getID(),nonce_Alice);
        //receive_Alice(kdc_server);
        KDCToAlice(kdc_server, Alice, Bob);
        //  System.out.println("Kriptirano: "+kdc_server.getKripto());
        AliceToBob(kdc_server, Alice, Bob);
    }
}
