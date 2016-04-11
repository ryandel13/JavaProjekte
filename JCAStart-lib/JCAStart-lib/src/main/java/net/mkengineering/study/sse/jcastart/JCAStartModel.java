/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkengineering.study.sse.jcastart;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Malte Christjan Koch
 */
public class JCAStartModel {

    private static String SYMMETRIC_ALG = "DES";
    private static String ASYMMETRIC_ALG = "RSA";
    private static String RAND_PROVIDER = "SUN";
    private static String RAND_ALG = "SHA1PRNG";

    private KeyPairGenerator kpg;
    private SecretKeyFactory skf;
    private SecureRandom sr;
    private Cipher symmetricCipher;
    private Cipher asymmetricCipher;

    public JCAStartModel() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {

        kpg = KeyPairGenerator.getInstance(JCAStartModel.ASYMMETRIC_ALG);
        skf = SecretKeyFactory.getInstance(JCAStartModel.SYMMETRIC_ALG);
        sr = SecureRandom.getInstance(JCAStartModel.RAND_ALG, JCAStartModel.RAND_PROVIDER);
        symmetricCipher = Cipher.getInstance(JCAStartModel.SYMMETRIC_ALG);
        asymmetricCipher = Cipher.getInstance(JCAStartModel.ASYMMETRIC_ALG);

    }

    public String createSecret() throws InvalidKeyException, InvalidKeySpecException {
        byte[] seed = sr.generateSeed(50);
        SecretKey sk = null;

        KeySpec ks = new DESKeySpec(seed);
        sk = skf.generateSecret(ks);

        return sk != null ? Base64.getEncoder().encodeToString(sk.getEncoded()) : null;
    }

    public String encryptStringWithSecret(String clear, String secret) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decKey = Base64.getDecoder().decode(secret);
        // rebuild key using SecretKeySpec
        SecretKey sk = new SecretKeySpec(decKey, 0, decKey.length, JCAStartModel.SYMMETRIC_ALG);
        String out = null;

        symmetricCipher.init(Cipher.ENCRYPT_MODE, sk);
        byte[] cipherText = symmetricCipher.doFinal(clear.getBytes());
        out = Base64.getEncoder().encodeToString(cipherText);

        return out != null ? out : null;
    }

    public String[] generateAsyncKeyPair() {
        kpg.initialize(512, sr);
        KeyPair kp = kpg.generateKeyPair();
        String[] keyPair = new String[2];
        keyPair[0] = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());
        keyPair[1] = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());

        return keyPair;
    }

    public void storeKey(String key, String filename) throws IOException {
        File privateKeyFile = new File(filename);

        InputStream is = new ByteArrayInputStream(key.getBytes());
        if (!privateKeyFile.exists()) {
            privateKeyFile.createNewFile();
        }
        Files.copy(is, privateKeyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public String encryptSecretWithPublicKey(String publicKeyString, String secret) throws CertificateException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        String out = null;
        byte[] encKey = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory kf = KeyFactory.getInstance(JCAStartModel.ASYMMETRIC_ALG);
        PublicKey pk = kf.generatePublic(publicKeySpec);
        asymmetricCipher.init(Cipher.ENCRYPT_MODE, pk);
        byte[] cipherTextByte = asymmetricCipher.doFinal(secret.getBytes());
        out = Base64.getEncoder().encodeToString(cipherTextByte);

        return out;
    }

    public String decryptSecretWithPrivateKey(String privateKey, String encSecret) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String out = null;
        byte[] encKey = Base64.getDecoder().decode(privateKey);
        KeyFactory kf = KeyFactory.getInstance(JCAStartModel.ASYMMETRIC_ALG);
        KeySpec privateKeySpec = new PKCS8EncodedKeySpec(encKey);
        PrivateKey pk = kf.generatePrivate(privateKeySpec);
        asymmetricCipher.init(Cipher.DECRYPT_MODE, pk);
        byte[] cipherTextByte = asymmetricCipher.doFinal(Base64.getDecoder().decode(encSecret));
        out = new String(cipherTextByte);

        return out;
    }

    public String decryptStringWithSecret(String crypted, String decSecret) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decKey = Base64.getDecoder().decode(decSecret);
        // rebuild key using SecretKeySpec
        SecretKey sk = new SecretKeySpec(decKey, 0, decKey.length, JCAStartModel.SYMMETRIC_ALG);
        String out = null;
        symmetricCipher.init(Cipher.DECRYPT_MODE, sk);
        byte[] cipherText = symmetricCipher.doFinal(Base64.getDecoder().decode(crypted));
        out = new String(cipherText);

        return out != null ? out : null;
    }
}
