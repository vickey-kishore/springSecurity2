package com.learn.springSecurity2.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorUtil {

	// step-1: generating the keyPair
	public static KeyPair generateRsaKey() {

		KeyPair keyPair;

		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048); // initializing key size
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			throw new IllegalStateException();
		}
		return keyPair;
	}

}


/*
	notes:
		1) keyPair is needed to encode and decode the JWT token.
*/