/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkengineering.study.sse.jcastart;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Malte Christjan Koch
 */
public class JCAStartTest {

    @Test
    public void CreateSecretTest() throws Exception {
        JCAStartModel jcaModel = new JCAStartModel();
        String secret = jcaModel.createSecret();
        System.out.println(secret);
    }

    @Test
    public void EncryptStringWithSecret() throws Exception {
        JCAStartModel jcaModel = new JCAStartModel();
        String secret = jcaModel.createSecret();
        String clear = "TestTestTest";
        String crypted = jcaModel.encryptStringWithSecret(clear, secret);
        System.out.println(crypted);
        Assert.assertNotSame(clear, crypted);
    }

    @Test
    public void DecryptStringWithSecret() throws Exception {
        JCAStartModel jcaModel = new JCAStartModel();
        String secret = jcaModel.createSecret();
        String clear = "TestTestTest";
        String crypted = jcaModel.encryptStringWithSecret(clear, secret);
        System.out.println(crypted);
        Assert.assertNotSame(clear, crypted);
        String deCrypted = jcaModel.decryptStringWithSecret(crypted, secret);
        Assert.assertEquals(deCrypted, clear);
    }
    
    @Test
    public void GenerateKeyPair() throws Exception {
        JCAStartModel jcaModel = new JCAStartModel();
        String[] keyPair;
        keyPair = jcaModel.generateAsyncKeyPair();
        Assert.assertNotNull(keyPair);
    }

    @Test
    public void EncryptSecretWithPublicKey() throws Exception {
        JCAStartModel jcaModel = new JCAStartModel();
        String secret = jcaModel.createSecret();

        String[] keyPair;
        keyPair = jcaModel.generateAsyncKeyPair();
        String encSecret = jcaModel.encryptSecretWithPublicKey(keyPair[1], secret);
        Assert.assertNotSame(encSecret, secret);
    }

    @Test
    public void StoreKeyPair() throws Exception {
        JCAStartModel jcaModel = new JCAStartModel();
        String[] keyPair;
        keyPair = jcaModel.generateAsyncKeyPair();
        jcaModel.storeKey(keyPair[0], "privateKey.pem");
        jcaModel.storeKey(keyPair[1], "publicKey.crt");
    }

    @Test
    public void DecryptSecret() throws Exception {
        JCAStartModel jcaModel = new JCAStartModel();
        String secret = jcaModel.createSecret();

        String[] keyPair;
        keyPair = jcaModel.generateAsyncKeyPair();
        String encSecret = jcaModel.encryptSecretWithPublicKey(keyPair[1], secret);

        String decSecret = jcaModel.decryptSecretWithPrivateKey(keyPair[0], encSecret);

        Assert.assertEquals(secret, decSecret);
    }

    @Test
    public void DecryptFile() throws Exception {
        JCAStartModel jcaModel = new JCAStartModel();
       String secret = jcaModel.createSecret();
       
        byte[] encoded = Files.readAllBytes(Paths.get("cleartext.txt"));
        String clear = new String(encoded, Charset.defaultCharset());
       
        String crypted = jcaModel.encryptStringWithSecret(clear, secret);

        String[] keyPair;
        keyPair = jcaModel.generateAsyncKeyPair();
        String encSecret = jcaModel.encryptSecretWithPublicKey(keyPair[1], secret);

        String decSecret = jcaModel.decryptSecretWithPrivateKey(keyPair[0], encSecret);

        String decClear = jcaModel.decryptStringWithSecret(crypted, decSecret);
        
        Assert.assertEquals(clear, decClear);
    }
}
