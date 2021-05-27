
package databasecommunication;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Password {

	private Password() {
	}

	/**
	 * Generiert ein Passwort mit dem PBKDF2 und SHA1 Algorithmus
	 * 
	 * @param password Der String der gehasht werden soll
	 * @return hashedPassword (String), iterations (int), salt (String), hash
	 *         (String)
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */

	public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt().getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);

	}

	/**
	 * Erstellt einen Salt, der aus einem PseudeRandom Wert in Hash besteht, für das
	 * zugehörige Passwort mit dem SHA1PRNG Algorithmus
	 * 
	 * @return salt (String)
	 * 
	 * @throws NoSuchAlgorithmException
	 */

	private static String getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	/**
	 * Nimmt das plain-text Passwort und verwandelt es zu Hexadezimal
	 * 
	 * @param array nimmt den Passwort String und verwandelt ihn zu einem Hashwert.
	 * @return hex (String)
	 */

	private static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	/**
	 * Überprüft, dass das plain-text Password, mit dem gehashten Passwort
	 * übereinstimmt
	 * 
	 * 
	 * @param originalPassword Das eingegebene Passwort vom Benutzer
	 * @param hashedPassword   Dass Passwort aus der Datenbank
	 * @param saltDB           Der dazugehörige Salt(Salz)
	 * @param iterations       Die anzahl an Iterationen für das hergestellte
	 *                         Passwort
	 * 
	 * @return diff (boolean)
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */

	public static boolean validatePassword(String originalPassword, String hashedPassword, String saltDB, int iterations) throws NoSuchAlgorithmException, InvalidKeySpecException {

		byte[] hash = fromHex(hashedPassword);
		byte[] salt = fromHex(saltDB);

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);

		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length;

		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			diff |= hash[i] ^ testHash[i];
		}

		return diff == 0;
	}

	/**
	 * Verwandelt das gehashte Password von HexaDezimal zu plain-Text
	 * 
	 * @param hex (String)
	 * @return bytes (byte[] Array)
	 */

	private static byte[] fromHex(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	/**
	 * Generiert ein zufallspasswort
	 * 
	 * @return RandomPassword (String)
	 */

	public static String randomPassword() {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();

	}
}
