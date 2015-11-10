package mts.web.client;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.springframework.security.crypto.codec.Hex;

public class Abc
{
    @Test
    public void test()
    {
        String finalString = "098f6bcd4621d373cade4e832627b4f68740fa70fe32d85ad793fabf3dc45412";

        MessageDigest messageDigestFinal;
        try
        {
            messageDigestFinal = MessageDigest.getInstance("MD5");
            messageDigestFinal.reset();
            messageDigestFinal.update(finalString.getBytes(Charset.forName("UTF8")));
            final byte[] finalResultByte = messageDigestFinal.digest();
            final String finalResult = new String(Hex.encode(finalResultByte));

            System.out.print(finalResult);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
