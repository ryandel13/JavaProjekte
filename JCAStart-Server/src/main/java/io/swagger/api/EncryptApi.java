package io.swagger.api;

import io.swagger.model.*;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import net.mkengineering.study.sse.jcastart.JCAStartModel;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.io.IOUtils;

import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/encrypt", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/encrypt", description = "the encrypt API")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringMVCServerCodegen", date = "2016-04-11T19:23:56.446Z")
public class EncryptApi {

  @ApiOperation(value = "", notes = "Set the current Volume", response = String.class)
  @io.swagger.annotations.ApiResponses(value = { 
    @io.swagger.annotations.ApiResponse(code = 200, message = "Successful response") })
  @RequestMapping(value = "", 
    
    
    method = RequestMethod.POST)
  public ResponseEntity<String> encryptPost(@ApiParam(value = "", required = true) 
    @RequestParam(value = "message", required = true) String message,
    @RequestParam(value = "secret", required = true) String secret


)
      throws NotFoundException {
      
      try {
          JCAStartModel jcm = new JCAStartModel();
         
          String pKey = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("privateKey.pem"));
          String encSecret = jcm.decryptSecretWithPrivateKey(pKey, secret);
          
          String out = jcm.decryptStringWithSecret(encSecret, message);
          
          return new ResponseEntity<String>(out, HttpStatus.OK);
      } catch (NoSuchAlgorithmException ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } catch (NoSuchProviderException ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } catch (NoSuchPaddingException ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvalidKeySpecException ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IllegalBlockSizeException ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } catch (BadPaddingException ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } catch (InvalidKeyException ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Exception ex) {
          Logger.getLogger(EncryptApi.class.getName()).log(Level.SEVERE, null, ex);
      } 
      
      return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
