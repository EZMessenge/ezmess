package messenger.client.cryptography;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

public class DES {

	private Cipher ecipher;
	private Cipher dcipher;

	private SecretKey key;
	/*public DES(String keyString){
		new DES(keyString.getBytes());
	}*/
	
	
	public DES(byte[] encodedKey) {
		System.out.println("Sto utilzzando una chiave di "+encodedKey.length);
		try {
			key = new SecretKeySpec(encodedKey,0, encodedKey.length, "DES");

			this.ecipher = Cipher.getInstance("DES");

			dcipher = Cipher.getInstance("DES");
			ecipher.init(Cipher.ENCRYPT_MODE, key);
			dcipher.init(Cipher.DECRYPT_MODE, key);


		}
		catch (NoSuchAlgorithmException e) {
			System.out.println("No Such Algorithm:" + e.getMessage());
			return;
		}
		catch (NoSuchPaddingException e) {
			System.out.println("No Such Padding:" + e.getMessage());
			return;
		}
		catch (InvalidKeyException e) {
			System.out.println("Invalid Key:" + e.getMessage());
			return;
		}

	}

	public String encrypt(String str) {

		try {
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = ecipher.doFinal(utf8);
			enc = BASE64EncoderStream.encode(enc);
			return new String(enc);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String decrypt(String str) {
		try {
			byte[] dec = BASE64DecoderStream.decode(str.getBytes());
			byte[] utf8 = dcipher.doFinal(dec);
			return new String(utf8, "UTF8");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}