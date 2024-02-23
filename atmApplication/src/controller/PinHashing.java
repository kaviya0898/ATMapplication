package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinHashing {

	public String hashPin(String pinNumber) {
		
		 StringBuilder sb = new StringBuilder();
		try {
           
           MessageDigest md = MessageDigest.getInstance("SHA-256");
           
           
           md.update(pinNumber.getBytes());
           
           
           byte[] hashedPinBytes = md.digest();
           
          
          
           for (byte b : hashedPinBytes) 
           {
               sb.append(String.format("%02x", b));
           }
           
           
           return sb.toString();
       }
		catch (NoSuchAlgorithmException e) 
		{
          
           e.printStackTrace();
       }
		return sb.toString();
		
   }
	public boolean isValidNumber(String mobileNumber)
	{
		String regex = "^\\d{10}$";

        
        Pattern pattern = Pattern.compile(regex);

       
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
	}
}
