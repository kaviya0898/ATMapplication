package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import model.DataBase;

public class OtherServices {
	Scanner scanner=new Scanner(System.in);
    PinHashing hashing=new PinHashing();
	public void changePin(String cardNumber, String phoneNumber) throws SQLException {

		String pinQuery = "SELECT PhoneNumber FROM UsersList WHERE CardNumber=?";
		PreparedStatement preparedStatementMax = DataBase.DataBaseConnectivity().prepareStatement(pinQuery);
		preparedStatementMax.setString(1, cardNumber);

		ResultSet resultSet = preparedStatementMax.executeQuery(); 

	if(resultSet.next())
	{
		boolean isValidPin=false;
		
		do
		{
			System.out.println("Enter 4-digit pin:");
			String pin=scanner.next();
			if(pin.length()==4)
			{
				isValidPin=true;
				pin=hashing.hashPin(pin);
				String query="UPDATE UsersList SET Pin=? WHERE CardNumber=?";
				PreparedStatement preparedStatement=DataBase.DataBaseConnectivity().prepareStatement(query);
				preparedStatement.setString(1,pin);
				preparedStatement.setString(2,cardNumber);
				
				int rowsAffectedPin=preparedStatement.executeUpdate();
				System.out.println("PIN updation sucessfull");
				System.out.println("Thank you for banking with us");
					
			}
			else
			{
				isValidPin=false;
			}
		}while(isValidPin=false);
	}
		
		
	}
	public void printMinistatement(String cardNumber) throws SQLException {
		
		LocalTime currentTime = LocalTime.now();
		LocalDate today = LocalDate.now();
		String accountNumber="";
		double balance=0;
		String miniStatement="SELECT AccountNumber,CurrentBalance FROM UsersList WHERE CardNumber=?";
		PreparedStatement preparedStatement = DataBase.DataBaseConnectivity().prepareStatement(miniStatement);
		preparedStatement.setString(1, cardNumber);
        
		String encryptedCard=cardNumber.substring(0,7)+"xxxxxxxxx"+cardNumber.substring(cardNumber.length()-1);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next())
		{
			accountNumber=resultSet.getString("AccountNumber");
			balance=resultSet.getDouble("CurrentBalance");
		}
		String encryptedAccount="xxxxxxxxxxxx"+accountNumber.substring(11,13);
		
		
		
		    String query = "SELECT DateOfTransaction, Amount, TransactionType FROM Transaction WHERE CardNumber=?";
		    PreparedStatement preparedStatementTrans = DataBase.DataBaseConnectivity().prepareStatement(query);
		    preparedStatementTrans.setString(1, cardNumber);
		    ResultSet resultSetTrans = preparedStatementTrans.executeQuery();
		    System.out.println("Date" + "\t\t\t" + "Time");
			System.out.println(today + "\t" + currentTime);
			System.out.println();
			System.out.println("Card number:"+encryptedCard);
			System.out.println("Statement for "+encryptedAccount);
			System.out.println();
		    while(resultSetTrans.next())
			 {
				 System.out.printf("%10s %10s %10s",resultSetTrans.getDate("DateOfTransaction"),
						 resultSetTrans.getDouble("Amount"),resultSetTrans.getString("TransactionType"));
				 System.out.println();
			 }
		    
		} 
		
		
		
		
		
	}


