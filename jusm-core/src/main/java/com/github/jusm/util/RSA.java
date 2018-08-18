package com.github.jusm.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * RSA算法
 *
 */
public final class RSA {

	private final static int DEFAULT_KEYSIZE = 512;

	public static void main(String[] args) {
		KeyPairs keyPairs = RSA.generateKey();

		String text = "wenhaoran";

		String pubKey = keyPairs.getPubKey();
		String priKey = keyPairs.getPriKey();
		String ciphertext = encryptByPubKey(pubKey, text);
		System.out.println("公钥加密结果:" + ciphertext);
		System.out.println("私钥解密结果:" + decryptByPriKey(priKey, ciphertext));
		System.out.println("=========================");

		String ciphertext2 = encryptByPriKey(priKey, text);
		System.out.println("私钥加密结果:" + ciphertext2);
		System.out.println("公钥解密结果:" + decryptByPubKey(pubKey, ciphertext2));
	}

	/**
	 * 生成公钥私钥键值对
	 * 
	 * @return
	 */
	public static KeyPairs generateKey() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(DEFAULT_KEYSIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
			String pubKey = Base64.encodeBase64String(rsaPublicKey.getEncoded());
			// System.out.println("Public Key:" + pubKey);
			String priKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
			// System.out.println("Private Key:" + priKey);
			return new KeyPairs(pubKey, priKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 私钥加密
	 * 
	 * @param priKey
	 *            base64压缩后的私钥
	 * @param plaintext
	 *            明文 公钥(这里公钥pubKey一定要与私钥priKey成对生成的)加密的密文
	 * @return 公钥加密的密文
	 */
	public static String encryptByPriKey(String priKey, String plaintext) {
		if (StringUtils.isBlank(priKey) || StringUtils.isBlank(plaintext)) {
			return StringUtils.EMPTY;
		}
		try {
			byte[] encodedKey = Base64.decodeBase64(priKey);
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] result = cipher.doFinal(plaintext.getBytes());
			String ciphertext = Base64.encodeBase64String(result);
			return ciphertext;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 公钥加密
	 * 
	 * @param pubKey
	 *            base64压缩后的公钥
	 * @param ciphertext
	 *            公钥(这里公钥pubkey一定要与私钥priKey成对生成的)加密的密文
	 * @return
	 */
	public static String encryptByPubKey(String pubKey, String plaintext) {
		if (StringUtils.isBlank(pubKey) || StringUtils.isBlank(plaintext)) {
			return StringUtils.EMPTY;
		}
		try {
			byte[] encodedKey = Base64.decodeBase64(pubKey);
			X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] result = cipher.doFinal(plaintext.getBytes());
			String ciphertext = Base64.encodeBase64String(result);
			return ciphertext;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 公钥解密
	 * 
	 * @param pubKey
	 *            base64压缩后的公钥
	 * @param ciphertext
	 *            私钥(这里私钥prikey一定要与公钥pubKey成对生成的)加密的密文
	 * @return
	 */
	public static String decryptByPubKey(String pubKey, String ciphertext) {
		if (StringUtils.isBlank(pubKey) || StringUtils.isBlank(ciphertext)) {
			return StringUtils.EMPTY;
		}
		try {

			byte[] d = Base64.decodeBase64(pubKey);
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(d);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] result = cipher.doFinal(Base64.decodeBase64(ciphertext));
			String plaintext = new String(result);
			return plaintext;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 私钥解密
	 * 
	 * @param priKey
	 *            base64压缩后的私钥
	 * @param ciphertext
	 *            公钥(这里公钥pubkey一定要与私钥priKey成对生成的)加密的密文
	 * @return
	 */
	public static String decryptByPriKey(String priKey, String ciphertext) {
		if (StringUtils.isBlank(priKey) || StringUtils.isBlank(ciphertext)) {
			return StringUtils.EMPTY;
		}
		try {
			byte[] d = Base64.decodeBase64(priKey);
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(d);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] result = cipher.doFinal(Base64.decodeBase64(ciphertext));
			String plaintext = new String(result);
			return plaintext;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 只允许本类实例化的公私钥对象
	 */
	public static class KeyPairs {

		private String pubKey;

		private String priKey;

		/**
		 * 只允许本类实例化
		 * 
		 * @param pubKey
		 * @param priKey
		 */
		private KeyPairs(String pubKey, String priKey) {
			this.pubKey = pubKey;
			this.priKey = priKey;
		}

		/**
		 * 获取base64压缩后的公钥
		 * 
		 * @return
		 */
		public String getPubKey() {
			return pubKey;
		}

		/**
		 * 获取base64压缩后的私钥
		 * 
		 * @return
		 */
		public String getPriKey() {
			return priKey;
		}
	}

}
