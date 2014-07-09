package org.robocup_logistics.llsf_encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.robocup_logistics.llsf_exceptions.UnknownEncryptionMethodException;

/**
 * The BufferDecryptor is responsible for decrypting incoming messages.
 */
public class BufferDecryptor {
	
	private byte[] keyBytes;
	
	private static String ALGORITHM;
	private static int KEY_SIZE_BITS;
	
	private byte[][] keyAndIV;
	private SecretKeySpec keySpec;
	
	/**
	 * Instantiates a new BufferDecryptor with an encryption key.
	 * 
	 * @param key The encryption key as String
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public BufferDecryptor(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException {
		keyBytes = key.getBytes("UTF-8");
	}
	
	/**
	 * Decrypts an incoming message. This method is called by the {@link ProtobufBroadcastPeer}.
	 * 
	 * @param cipher The cipher as defined in the refbox integration manual in section 2.2.1
	 * @param toDecrypt Encrypted data as byte array
	 * @param iv Initialization vector from the incoming message (pass null if you use ECB)
	 * @return Decrypted data as byte array
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public byte[] decrypt(int cipher, byte[] toDecrypt, byte[] iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if (cipher == 1) {
			ALGORITHM = "AES/ECB/PKCS5Padding";
			KEY_SIZE_BITS = 128;
		} else if (cipher == 2) {
			ALGORITHM = "AES/CBC/PKCS5Padding";
			KEY_SIZE_BITS = 128;
		} else if (cipher == 3) {
			ALGORITHM = "AES/ECB/PKCS5Padding";
			KEY_SIZE_BITS = 256;
		} else if (cipher == 4) {
			ALGORITHM = "AES/CBC/PKCS5Padding";
			KEY_SIZE_BITS = 256;
		} else {
			throw new UnknownEncryptionMethodException("The encryption method related to cipher " + cipher + " is unknown.");
		}
		
		Cipher c = Cipher.getInstance(ALGORITHM);
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		
		keyAndIV = KeyConverter.EVP_BytesToKey(KEY_SIZE_BITS / Byte.SIZE, c.getBlockSize(), sha, null, keyBytes, 8);
        keySpec = new SecretKeySpec(keyAndIV[0], "AES");

        if (cipher == 1 || cipher == 3) { //ECB
        	c.init(Cipher.DECRYPT_MODE, keySpec);
		} else if (cipher == 2 || cipher == 4) { //CBC
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
		}
		
		byte[] decryptedData = c.doFinal(toDecrypt);
		
		return decryptedData;
	}
	
}
