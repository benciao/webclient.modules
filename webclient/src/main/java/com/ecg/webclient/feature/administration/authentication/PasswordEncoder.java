package com.ecg.webclient.feature.administration.authentication;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.codec.Hex;

public class PasswordEncoder
{
	static final Logger logger = LogManager.getLogger(PasswordEncoder.class.getName());

	private static final char[]	PASSWORD	= "enfldsgbnlsngdlksdsgm".toCharArray();
	private static final byte[]	SALT		= { (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, (byte) 0xde,
			(byte) 0x33, (byte) 0x10, (byte) 0x12, };

	public static String decode2Way(String property)
	{
		try
		{
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
			return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
		}
		catch (Exception ex)
		{
			logger.error(ex);
			return null;
		}
	}

	public static String encode2Way(String property)
	{
		try
		{
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));

			return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
		}
		catch (Exception ex)
		{
			logger.error(ex);
			return null;
		}
	}

	public static String encodeComplex(String simpleEncodedPassword, String rid)
	{
		try
		{
			MessageDigest messageDigestRid = MessageDigest.getInstance("MD5");
			messageDigestRid.reset();
			messageDigestRid.update(rid.getBytes(Charset.forName("UTF8")));
			final byte[] ridResultByte = messageDigestRid.digest();
			final String ridResult = new String(Hex.encode(ridResultByte));

			String finalString = simpleEncodedPassword + ridResult;

			MessageDigest messageDigestFinal = MessageDigest.getInstance("MD5");
			messageDigestFinal.reset();
			messageDigestFinal.update(finalString.getBytes(Charset.forName("UTF8")));
			final byte[] finalResultByte = messageDigestFinal.digest();
			final String finalResult = new String(Hex.encode(finalResultByte));

			return finalResult;
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error(e);
		}

		return null;
	}

	private static byte[] base64Decode(String property) throws IOException
	{
		return Hex.decode(property);
	}

	private static String base64Encode(byte[] bytes)
	{
		return new String(Hex.encode(bytes));
	}
}
