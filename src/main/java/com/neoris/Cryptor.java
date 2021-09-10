package com.neoris;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * Hello world!
 *
 */
public class Cryptor {

	private StringEncryptor encriptor;

	public Cryptor() {
		this.encriptor = stringEncryptor();
	}

	public static void main(String[] args) {
		Cryptor crypto = new Cryptor();
		String cryptedMsg = "%s = ENC(%s)";
		String decryptedMsg = "Text Decrypted is: %s";
		String encrypt = System.getProperty("encrypt");
		String decrypt = System.getProperty("decrypt");

		if ((args == null || args.length == 0) && (encrypt == null && decrypt == null)) {
			System.out.println("Usage: CryptUtils [e|d] words to [E]ncrypt or [D]ecrypt");
			System.out.println("Encrypt: CryptUtils e word1 word2 \"words with spaces\" ... wordn");
			System.out.println("Decrypt: CryptUtils d crypted1 crypted2 ... cryptedn");
			System.out.println("or use:");
			System.out.println("CryptUtils -Dencrypt=[property to encrypt]");
			System.out.println("CryptUtils -Ddecrypt=[property to decrypt]");
			return;
		}

		System.out.println("-----------------------------------------------------");
		if (args[0].equalsIgnoreCase("e")) {
			for (int i = 1; i < args.length; i++) {
				System.out.println(String.format(cryptedMsg, args[i], crypto.propertyEncryptor(args[i])));
			}
			System.out.println("-----------------------------------------------------");
			System.out.println("Remeber to copy the \"ENC()\" in the properties file");
			System.out.println("");
			System.out.println("ie: database.password=ENC(xxxxxx)");
			System.out.println("-----------------------------------------------------");
		} else {
			for (int i = 1; i < args.length; i++) {
				System.out.println(String.format(decryptedMsg, crypto.propertyDecryptor(args[i])));
			}
		}

		if (encrypt != null && encrypt.trim().length() > 0) {
			System.out.println(String.format(cryptedMsg, encrypt, crypto.propertyEncryptor(encrypt)));
		}

		if (decrypt != null && decrypt.trim().length() > 0) {
			System.out.println(String.format(decryptedMsg, crypto.propertyDecryptor(decrypt)));
		}
	}

	public String propertyEncryptor(String property) {
		String value = encriptor.encrypt(property);
		return value;
	}

	public String propertyDecryptor(String value) {
		String property = encriptor.decrypt(value);
		return property;
	}

	private StringEncryptor stringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("N30r1stlc");
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}

}
