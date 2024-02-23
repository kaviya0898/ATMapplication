package view;

import java.sql.SQLException;
import java.util.Scanner;

import controller.AccountDetails;
import controller.PinHashing;

public class InitialScreen {

	Scanner scanner=new Scanner(System.in);
	AccountDetails account=new AccountDetails();
	PinHashing hashing=new PinHashing();
	
	public void start() throws SQLException {
	
		boolean isCardValid=false;
		
		do
		{
			System.out.print("Enter card number:");
			String cardNumber=scanner.next();
			
			if(cardValid(cardNumber))
			{
				int range=0;
				
				do
				{
				System.out.print("Enter your 4-digit pin:");
				String pinNumber=scanner.next();
				//System.out.println( hashing.hashPin(pinNumber));
				if(account.checkPin(hashing.hashPin(pinNumber),cardNumber)==3)
				{
					range=3;
				}
				isCardValid=true;
				}while(range!=3);
				
			}
			else
			{
				System.out.println("Enter correct cardNumber");
				System.out.println();
			}
		}while(isCardValid==false);
		
	}

	

	private boolean cardValid(String cardNumber) throws SQLException {
		
		String regex = "\\d{16}";
		
		if(!(cardNumber.matches(regex)))
		{
			return false;
		}
		else if(account.checkAccount(cardNumber))
		{
			return true;
		}
		return false;
	}

}
