package controller;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Scanner;

import model.DataBase;

public class TransactionDetails {

	Scanner scanner=new Scanner(System.in);
	
	public void depositAmount(double depositedAmount, String cardNumber,String transactionType) throws SQLException {
		
		LocalTime currentTime = LocalTime.now();
		LocalDate today = LocalDate.now();
		java.sql.Date sqlDate = java.sql.Date.valueOf(today);
		java.sql.Time sqlTime = java.sql.Time.valueOf(currentTime);
		LocalDateTime currentDateTime = LocalDateTime.of(today, currentTime);
        long timestamp = currentDateTime.toEpochSecond(ZoneOffset.UTC);
      //  System.out.println(sqlTime);
		String depositQuery="UPDATE UsersList SET CurrentBalance=CurrentBalance+? WHERE CardNumber=?";
		
		PreparedStatement preparedStatement = DataBase.DataBaseConnectivity().prepareStatement(depositQuery);
		preparedStatement.setDouble(1,depositedAmount);
		preparedStatement.setString(2,cardNumber);
		
		int rowsAffected=preparedStatement.executeUpdate();
		
		
		if(rowsAffected>0)
		{
			System.out.println("Amount credited");
			String transactionQuery="INSERT INTO Transaction (CardNumber,TransactionType,Amount,Time,DateOfTransaction) VALUES (?,?,?,?,?)";
			PreparedStatement preparedStatementUpdate = DataBase.DataBaseConnectivity().prepareStatement(transactionQuery);
			preparedStatementUpdate.setString(1,cardNumber);
			preparedStatementUpdate.setString(2,transactionType);
			preparedStatementUpdate.setDouble(3,depositedAmount);
			preparedStatementUpdate.setTime(4,sqlTime);
			preparedStatementUpdate.setDate(5,sqlDate);
			
			
			
			
			preparedStatementUpdate.executeUpdate();
			
			
			
			
			System.out.println("Do you want to see the balance");
			System.out.println("1.Yes");
			System.out.println("2.No");
			int choice=scanner.nextInt();
			if(choice==1)
			{
				String balanceCheck="SELECT CurrentBalance FROM UsersList WHERE CardNumber=?";
				PreparedStatement preparedStatementBalance = DataBase.DataBaseConnectivity().prepareStatement(balanceCheck);
				preparedStatementBalance.setString(1,cardNumber);
				ResultSet resultSet=preparedStatementBalance.executeQuery();
				
				while(resultSet.next())
				{
					System.out.println("Your current balance is:"+resultSet.getDouble("CurrentBalance"));
					System.out.println("Thank you");
				}
			}
			else
			{
				System.out.println("Thank you");
			}
		}
		
		
		
		
	}

	public void withdrawl(double amount, String cardNumber, String transactionType) throws SQLException {
		
		LocalTime currentTime = LocalTime.now();
		LocalDate today = LocalDate.now();
		java.sql.Date sqlDate = java.sql.Date.valueOf(today);
		java.sql.Time sqlTime = java.sql.Time.valueOf(currentTime);
		LocalDateTime currentDateTime = LocalDateTime.of(today, currentTime);
        long timestamp = currentDateTime.toEpochSecond(ZoneOffset.UTC);
		double currentBalance=0;
		double maximumLimit=0;
		LocalTime timeOfTransaction =null;
		long secondsDifference=0;
		long hoursDifference=0;
		
		String balanceCheck="SELECT CurrentBalance,MaximumLimit,TimeOfTransaction FROM UsersList WHERE CardNumber=?";
		
		PreparedStatement preparedStatement=DataBase.DataBaseConnectivity().prepareStatement(balanceCheck);
		preparedStatement.setString(1,cardNumber);
		
		ResultSet resultSet=preparedStatement.executeQuery();
		
		while(resultSet.next())
		{
			currentBalance=resultSet.getDouble("CurrentBalance");
			maximumLimit=resultSet.getDouble("MaximumLimit");
			if(resultSet.getTime("TimeOfTransaction")!=null)
			timeOfTransaction = resultSet.getTime("TimeOfTransaction").toLocalTime();
		}
		if(timeOfTransaction!=null)
		{
		 secondsDifference = Duration.between(timeOfTransaction, currentTime).getSeconds();
		 hoursDifference = secondsDifference / 3600; 
		}

//		System.out.println("Difference in hours: " + hoursDifference);
       //System.out.println(currentBalance);
//		
        if( maximumLimit<40000)
        {
		    if(currentBalance>=amount && amount<=40000)
		   {
			updateWithdraw(cardNumber,sqlTime,transactionType,amount,sqlDate);
			
		   }
		
		   else
		   {
			System.out.println("Insufficient Balance or maximum withdrawl amount reached");
		   }
        }
        else if(hoursDifference>=24)
        {
        	String query="UPDATE UsersList SET MaximumLimit=0 WHERE CardNumber=?";
        	PreparedStatement preparedStatementMax=DataBase.DataBaseConnectivity().prepareStatement(query);
        	preparedStatementMax.setString(1,cardNumber);
        	int rowsAffected=preparedStatementMax.executeUpdate();
        	updateWithdraw(cardNumber,sqlTime,transactionType,amount,sqlDate);
			
        }
        else
		{
			System.out.println("Maximum withdrawl amount reached.");
		}
		
	}

	private void updateWithdraw(String cardNumber, Time sqlTime,String transactionType, double amount,Date sqlDate) throws SQLException {
		
		String withdrawlQuery="UPDATE UsersList SET CurrentBalance=CurrentBalance-?,MaximumLimit=MaximumLimit+?,TimeOfTransaction=? WHERE CardNumber=?";
		PreparedStatement preparedStatementWithdraw = DataBase.DataBaseConnectivity().prepareStatement(withdrawlQuery);
		preparedStatementWithdraw.setDouble(1,amount);
		preparedStatementWithdraw.setDouble(2,amount);
		preparedStatementWithdraw.setTime(3,sqlTime);
		preparedStatementWithdraw.setString(4, cardNumber);
		
		
		int rowsAffected=preparedStatementWithdraw.executeUpdate();
		
		//System.out.println(rowsAffected);
		if(rowsAffected>0)
		{
			System.out.println("Please collect your cash");
			String transactionQuery="INSERT INTO Transaction (CardNumber,TransactionType,Amount,Time,DateOfTransaction) VALUES (?,?,?,?,?)";
			PreparedStatement preparedStatementUpdate = DataBase.DataBaseConnectivity().prepareStatement(transactionQuery);
			preparedStatementUpdate.setString(1,cardNumber);
			preparedStatementUpdate.setString(2,transactionType);
			preparedStatementUpdate.setDouble(3,amount);
			preparedStatementUpdate.setTime(4,sqlTime);
			preparedStatementUpdate.setDate(5,sqlDate);
			
			
			
			
			preparedStatementUpdate.executeUpdate();
			
			
			
			
			System.out.println("Do you want to see the balance");
			System.out.println("1.Yes");
			System.out.println("2.No");
			int choice=scanner.nextInt();
			if(choice==1)
			{
				String checkBalance="SELECT CurrentBalance FROM UsersList WHERE CardNumber=?";
				PreparedStatement preparedStatementBalance = DataBase.DataBaseConnectivity().prepareStatement(checkBalance);
				preparedStatementBalance.setString(1,cardNumber);
				ResultSet resultSetBalance=preparedStatementBalance.executeQuery();
				
				while(resultSetBalance.next())
				{
					System.out.println("Your current balance is:"+resultSetBalance.getDouble("CurrentBalance"));
					System.out.println("Thank you");
				}
			}
			else
			{
				System.out.println("Thank you");
			}
	     }
		return;
		
	}

	public void balanceEnquiry(String cardNumber) throws SQLException {
		
		double currentBalance=0;
      String balanceCheck="SELECT CurrentBalance FROM UsersList WHERE CardNumber=?";
		
		PreparedStatement preparedStatement=DataBase.DataBaseConnectivity().prepareStatement(balanceCheck);
		preparedStatement.setString(1,cardNumber);
		
		ResultSet resultSet=preparedStatement.executeQuery();
		
		while(resultSet.next())
		{
			currentBalance=resultSet.getDouble("CurrentBalance");
		}
		System.out.println("Your current balance is:"+currentBalance);
		System.out.println("Thank you for banking with us");
	}

}
