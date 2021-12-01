
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class AES {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(byte[] strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);//AES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//AES/CBC/PKCS5Padding
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(strToEncrypt);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static byte[] decrypt(byte[] strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(strToDecrypt);
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}

class Test
{
    public static void main(String[] args) throws IOException{
        final String secretKey = "thesecretkey";

        String originalString = "dataToTest123456";
        byte[] encryptedString = AES.encrypt(originalString.getBytes("UTF-8"), secretKey) ;
        byte[] decryptedString = AES.decrypt(encryptedString, secretKey) ;

        System.out.println(originalString);
        //System.out.println(encryptedString.toString());
        StringBuilder es = new StringBuilder();
        for (byte b : encryptedString) {
            es.append(String.format("%02X ", b));
        }
        System.out.println(es.toString());

        System.out.println(new String(decryptedString));
    }
}
