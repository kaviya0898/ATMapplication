package view;

import java.sql.SQLException;
import java.util.Scanner;

import controller.OtherServices;
import controller.PinHashing;
import controller.TransactionDetails;

public class MenuScreen {

	Scanner scanner=new Scanner(System.in);
	
	TransactionDetails transaction=new TransactionDetails();
	PinHashing pinHashing=new PinHashing();
	OtherServices others=new OtherServices();
	
	public void proceed(String cardNumber) throws SQLException {
		
		System.out.println("Dear customer,please select transaction..");
		System.out.println("1. BANKING" + "\t\t" + "2.MINI STATEMENT");
		System.out.println("3. BALANCE ENQUIRY" + "\t" + "4. SERVICES");
		System.out.println("5. TRANSFER" + "\t\t" + "6.QUICK CASH");
		System.out.println("7. EXIT");


		System.out.println("-----------------------");

		System.out.print("ENTER YOUR OPTION:");
		int option = scanner.nextInt();
		
		switch(option)
		{
		case 1:
			banking(cardNumber);
			break;
		case 2:
			miniStatement(cardNumber);
			break;
		case 3:
			transaction.balanceEnquiry(cardNumber);
		case 4:
			break;
		case 5:
			break;
		case 6:
			fastCash(cardNumber);
			break;
		case 7:
			System.out.println("Thank you for banking with us");
			System.exit(0);
		}

	}

	
	private void banking(String cardNumber) throws SQLException {
		
		
		System.out.println("Dear customer,please select transaction..");
		System.out.println("1. DEPOSIT" + "\t" + "2.FAST CASH");
		System.out.println("3. WITHDRAWAL" + "\t" + "4.BALANCE ENQUIRY");
		System.out.println("5. PIN CHANGE" + "\t" + "6.MINI STATEMENT");
		System.out.println("7.EXIT");
		int option=scanner.nextInt();
		
		switch(option)
		{
		case 1:
			deposit(cardNumber);
			break;
		case 2:
			fastCash(cardNumber);
			break;
		case 3:
			withdrawl(cardNumber);
			break;
		case 4:
			balanceEnquiry(cardNumber);
			break;
		case 5:
			pinChange(cardNumber);
			break;
		case 6:
			miniStatement(cardNumber);
		
		}

		
	}

	private void deposit(String cardNumber) throws SQLException {
		
		System.out.println("Enter the amount to deposit(below 1,00,0000):");
		double depositedAmount=scanner.nextDouble();
		if(depositedAmount>100000)
		{
			System.out.println("For large transaction please contact bank");
		}
		else
		{
		transaction.depositAmount(depositedAmount,cardNumber,"Deposit");
		}
		
	}
	
	private void fastCash(String cardNumber) throws SQLException 
	{
		System.out.println("Enter the cash");
		System.out.println("1.100" + "\t" + "2.200");
		System.out.println("3.500" + "\t" + "4.2000");
		
		double amount=0;
		int choice=scanner.nextInt();
		
		amount = (choice == 1) ? 100 : ((choice == 2) ? 200 :(choice==3)?500 : 2000);
        
		transaction.withdrawl(amount,cardNumber,"withdrawl");
	}

	private void withdrawl(String cardNumber) throws SQLException {
		
		System.out.println("Enter the amount");
		double amount=scanner.nextDouble();
		
		transaction.withdrawl(amount, cardNumber,"withdrawl");
		
	}

	private void balanceEnquiry(String cardNumber) throws SQLException {
		
		transaction.balanceEnquiry(cardNumber);
		
	}
	private void pinChange(String cardNumber) throws SQLException {
		
		
		boolean isNumberValid=false;
		
		do
		{
			System.out.println("Enter you mobile number:");
			String phoneNumber=scanner.next();
			if(pinHashing.isValidNumber(phoneNumber))
			{
				others.changePin(cardNumber,phoneNumber);
				isNumberValid=true;
			}
		}while(isNumberValid==false);
		
		
	}

	private void miniStatement(String cardNumber) throws SQLException {
		
		others.printMinistatement(cardNumber);
		
	}









}
