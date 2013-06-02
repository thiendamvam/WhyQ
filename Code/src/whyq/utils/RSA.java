package whyq.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Base64;
import android.util.Log;

public class RSA {

	private KeyPairGenerator kpg;
	private Cipher cipher;
	private KeyPair keyPair;
	private PrivateKey privKey;
	private PublicKey pubKey;

	// "-----BEGIN PUBLIC KEY-----
	public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQXdqfSG3anZ/oBgfBCErLZiaNyv+N1FjnSyKTP0q37/ihP/50n/HpCHrEoQPv4tvxZZb8w069bbxtMlb9mwGqjzWohcpfNle+dtch3AEliZ2jhPfT750Wr9zlk6q3/uGlcLaj10cL4R0FLBbTiEhLePqblDb/L4dD3Ck6qEhqJwIDAQAB";
	public static String PRIVATE_KEY = "MIICXQIBAAKBgQDQXdqfSG3anZ/oBgfBCErLZiaNyv+N1FjnSyKTP0q37/ihP/50n/HpCHrEoQPv4tvxZZb8w069bbxtMlb9mwGqjzWohcpfNle+dtch3AEliZ2jhPfT750Wr9zlk6q3/uGlcLaj10cL4R0FLBbTiEhLePqblDb/L4dD3Ck6qEhqJwIDAQABAoGBAMBxvoaYjaV5KYRRdX3qW7IqQXd6QrdKpWXR9jgLH8Zso7TCdxBd3T6+by0GDa3UWBFHI8GF3UaFgJ9V/BQ0wr4rYLmi5P1y8B1jiT7caHXJE46g1vhxkXiTrg6uxoK7oBGdvZd81a9cJ6Dl/Trz15+2odglIs+veqGj0pbInc9pAkEBvbOg3xo1Ykmdj6OacFeq+tRqEugzQ3n7bS2U8uPTmERFwu92poRxoTwbpdkA10rh9CS0q0tbLSBL80Bca1ithQJAd649UKjyVcFQLSX1n2g399nM8pkGtlsNVR9KWR4ldfWy9MEreuEYdG/lAToeSosguMUbqsyvxSJVY8sR0H4iuwJBAaLBzckwb63cKqyVc3pP9DZdMxh2kAu65U8L/6FCC0FKDB1+LgV/9N8fgX6OZ6rgEXH4tsMmNxEsAhAXjX3SQJ0CQGzssTN7QGvaMF2XAdEeamny2bwmmFxZzG2ft+waKuAMSd7G/QquWbKHsHaIbc9MhvCDvOWbu5IA6JSV2tXWJ0cCQQFb5Qgs/ZOWjoiZvN4qjpPxSHCOnXWC8vgC96jVHHLm9CpB26xlf7HBMXJsbyFZ7qXs6lNLT61g/VKGBaVG6zgr";
	KeyPair kp;
	PublicKey publicKey;
	PrivateKey privateKey;
	byte[] encryptedBytes, decryptedBytes;
	Cipher cipher1;
	String encrypted, decrypted;

	public RSA() {
		// try {
		// kpg = KeyPairGenerator.getInstance("RSA");
		// cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		//
		// kpg.initialize(1024);
		// keyPair = kpg.generateKeyPair();
		// privKey = keyPair.getPrivate();
		// pubKey = keyPair.getPublic();
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
	}

	// public String encript(String text) {
	// try {
	// cipher.init(Cipher.ENCRYPT_MODE, pubKey);
	//
	// String ciphertextFile = "ciphertextRSA.txt";
	// InputStream fis = new ByteArrayInputStream(text.getBytes("UTF-8"));
	//
	// FileOutputStream fos = new FileOutputStream(ciphertextFile);
	// CipherOutputStream cos = new CipherOutputStream(fos, cipher);
	// Log.d("decript", "" + cos.toString());
	// byte[] block = new byte[32];
	// int i;
	// while ((i = fis.read(block)) != -1) {
	// cos.write(block, 0, i);
	// }
	// cos.close();
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return null;
	// }
	//
	// public String decript(String text) {
	// try {
	// String cleartextAgainFile = "cleartextAgainRSA.txt";
	//
	// cipher.init(Cipher.DECRYPT_MODE, privKey);
	// String ciphertextFile = "ciphertextRSA.txt";
	// FileOutputStream fos = new FileOutputStream(ciphertextFile);
	// InputStream fis = new FileInputStream(ciphertextFile);
	// CipherInputStream cis = new CipherInputStream(fis, cipher);
	// Log.d("decript", "" + cis.toString());
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return null;
	// }

	public String RSAEncrypt(final String pin) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// kpg = KeyPairGenerator.getInstance("RSA");
		// kpg.initialize(1024);
		// kp = kpg.genKeyPair();
		// publicKey = kp.getPublic();
		// privateKey = kp.getPrivate();
		String result = null;
		// String key =
		// "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiOnM5t6w2ZD6dpA4/MzSTAOt0IYpnsmGSAIfIVgGntI+fI4wbvUvMIhaLN3fHrjyuNGFdYw+yuoXYkapajt6VTZJniaatSiq6bwQ7R0UAop6haFSAwjnmReqexJvcKyqUsTfcfFypPpsYRewh/48/jmc/6ND+ugxDd52prkPUrbj+nnO0z3DBoUCpgDMRvW2hWXv6kZ654gp+wIAQnxbdwRMy6FZbrHjkA3tc6U0CHK+KjxAfzWAK+yI+ofskM4qk50J7y9hUZ7lLikqWZWKiqh8xiDk1kgu+FIjVh+fylKpa3gWmPPn0fSpBJjuenc1OQVmZ718a3388DjzFlYOLwIDAQAB";
		byte[] sigBytes2 = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
		Log.d("WS", "new key is: " + PUBLIC_KEY);
		try {
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
					new X509EncodedKeySpec(sigBytes2));
			// encryptedBase64PIN = encode(publicKey, pin);
			// Log.d("WSA", "encoded key is: " + encryptedBase64PIN);
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encryptedBytes = cipher.doFinal(pin.getBytes());

			try {
				result = new String(encryptedBytes, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d("encrypt", "EEncrypted?????" + result);
			// encryptedBytes;
			// getSecToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String RSADecrypt(final String data)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// cipher1 = Cipher.getInstance("RSA");
		// cipher1.init(Cipher.DECRYPT_MODE, privateKey);
		// decryptedBytes = cipher1.doFinal(plain.getBytes());
		// decrypted = new String(decryptedBytes);
		// System.out.println("DDecrypted?????" + decrypted);
		// return decrypted;
		byte[] byteData = data.getBytes(); // convert string to byte array

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedByteData = cipher.doFinal(byteData);

		String s = Base64.encodeToString(encryptedByteData, Base64.NO_WRAP);
		return s; // convert encrypted byte array to string and return it
	}
}
