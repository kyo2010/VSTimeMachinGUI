/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KKV.Utils;

/**
 *
 * @author kyo
 */
public class UserException extends Exception{
  public String error = "User Exception";
  public String details = "";

  public UserException(String message, String deatils) {
    super(message);
    this.error = message;
    this.details = deatils;
  }    
    
}
