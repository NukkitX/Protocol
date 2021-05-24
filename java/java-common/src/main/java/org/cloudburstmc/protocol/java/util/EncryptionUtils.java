package org.cloudburstmc.protocol.java.util;

import lombok.experimental.UtilityClass;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

@UtilityClass
public class EncryptionUtils {
    public final KeyPair KEY_PAIR;

    static {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(1024);
            KEY_PAIR = gen.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Failed to generate shared key:", ex);
        }
    }

    public String createServerId() {
        try {
            KeyGenerator gen = KeyGenerator.getInstance("AES");
            gen.init(128);
            byte[] sharedKey = encrypt(Cipher.ENCRYPT_MODE, KEY_PAIR.getPublic(), gen.generateKey().getEncoded());
            SecretKey secretKey = new SecretKeySpec(encrypt(Cipher.DECRYPT_MODE, KEY_PAIR.getPrivate(), sharedKey), "AES");
            return createServerId(secretKey);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Failed to generate server id:", ex);
        }
    }

    public String createServerId(SecretKey secretKey) {
        return createServerId(KEY_PAIR.getPublic(), secretKey);
    }

    public String createServerId(PublicKey publicKey, SecretKey secretKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update("".getBytes(StandardCharsets.ISO_8859_1));
            digest.update(secretKey.getEncoded());
            digest.update(publicKey.getEncoded());
            return new BigInteger(digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Failed to generate server id:", ex);
        }
    }

    public SecretKey getSecret(PrivateKey key, byte[] sharedKey) {
        return new SecretKeySpec(encrypt(Cipher.DECRYPT_MODE, key, sharedKey), "AES");
    }

    public byte[] getVerifyToken(PrivateKey key, byte[] verifyToken) {
        return encrypt(Cipher.DECRYPT_MODE, key, verifyToken);
    }

    public byte[] getSharedKey(PublicKey publicKey, SecretKey secretKey) {
        return encrypt(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
    }

    public byte[] getVerifyToken(PublicKey publicKey, byte[] verifyToken) {
        return encrypt(Cipher.ENCRYPT_MODE, publicKey, verifyToken);
    }

    private byte[] encrypt(int cipherMode, Key key, byte[] encoded) {
        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm().equals("RSA") ? "RSA/ECB/PKCS1Padding" : "AES/CFB8/NoPadding");
            cipher.init(cipherMode, key);
            return cipher.doFinal(encoded);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException("Failed to encrypt data:", ex);
        }
    }
}
