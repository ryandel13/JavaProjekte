/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkengineering.study.sse.jcastart.client;

import io.swagger.client.api.DefaultApi;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.mkengineering.study.sse.jcastart.JCAStartModel;

/**
 *
 * @author baphs
 */
public class JCAStartClient {

    private static JCAStartModel jsm;
    private static DefaultApi dApi;
    
    public static void main(String[] args) {
        try {
            dApi = new DefaultApi();
            dApi.getApiClient().setBasePath("http://localhost:8080/jcastart-server-1.0.0");
            jsm = new JCAStartModel();
        } catch (Exception e) {
            System.out.println("Error on setup");
        }

        Scanner a = new Scanner(System.in);
        System.out.println("Type one of the following commands: genkeys, sendmessage, quit");
        String cmd;

        do {
            cmd = a.nextLine();

            try {
                if (cmd.equalsIgnoreCase("genkeys")) {
                    generateNewKeys();
                } else if (cmd.equalsIgnoreCase("sendmessage")) {
                    sendMessage();
                } else if (cmd.equalsIgnoreCase("selftest")) {
                    selfTest();
                }
            } catch (Exception ex) {
                Logger.getLogger(JCAStartClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!cmd.equals("quit"));

        System.out.println("APPLICATION EXIT");
    }

    private static void sendMessage() throws Exception {
        String secret = null;
        String message = null;

        byte[] strBytes = Files.readAllBytes(Paths.get("clear.txt"));
        String clearText = new String(strBytes);

        secret = jsm.createSecret();
        message = jsm.encryptStringWithSecret(clearText, secret);

        byte[] bPublic = Files.readAllBytes(Paths.get("publicKey.crt"));
        String strPublic = new String(bPublic);

        secret = jsm.encryptSecretWithPublicKey(strPublic, secret);
        System.out.println("Sending encrypted message...");
        System.out.println(dApi.encryptPost(secret, message));
    }

    private static void selfTest() throws Exception {
        
       String secret = jsm.createSecret();
       
        byte[] encoded = Files.readAllBytes(Paths.get("clear.txt"));
        String clear = new String(encoded, Charset.defaultCharset());
       
        String crypted = jsm.encryptStringWithSecret(clear, secret);

        byte[] bPublic = Files.readAllBytes(Paths.get("publicKey.crt"));
        String strPublic = new String(bPublic);
        System.out.println("PublicKey64: " + strPublic);
        String encSecret = jsm.encryptSecretWithPublicKey(strPublic, secret);

        byte[] bPrivate = Files.readAllBytes(Paths.get("privateKey.pem"));
        String strPrivate = new String(bPrivate);
        System.out.println("PrivateKey64: " + strPrivate);
        String decSecret = jsm.decryptSecretWithPrivateKey(strPrivate, encSecret);

        String decClear = jsm.decryptStringWithSecret(crypted, decSecret);
        
        if(decClear.equals(clear)) {
            System.out.println("selfttest success");
        } else {
            System.out.println("Selftest failed");
    }
    }
    
    private static void generateNewKeys() throws Exception {
        String[] keyPair;
        keyPair = jsm.generateAsyncKeyPair();
        jsm.storeKey(keyPair[0], "privateKey.pem");
        jsm.storeKey(keyPair[1], "publicKey.crt");
        
        System.out.println("New keys generated. Copy privateKey.pem to WEB-INF/classes/ within the server.war");
    }
}
