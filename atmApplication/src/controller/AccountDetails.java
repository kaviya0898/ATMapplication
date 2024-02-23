package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import model.DataBase;
import view.MenuScreen;

public class AccountDetails {

	MenuScreen menu = new MenuScreen();

	public boolean checkAccount(String cardNumber) throws SQLException {

		String query = "SELECT * FROM UsersList WHERE CardNumber=?";

		PreparedStatement preparedStatement = DataBase.DataBaseConnectivity().prepareStatement(query);
		preparedStatement.setString(1, cardNumber);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			return true;
		}

		return false;
	}

	public int checkPin(String pinNumber, String cardNumber) throws SQLException {

		LocalTime currentTime = LocalTime.now();
		Time time = Time.valueOf(currentTime);

		String query = "SElECT Pin,IsAccBlock,IncorrectPin FROM UsersList WHERE CardNumber=?";
		PreparedStatement preparedStatement = DataBase.DataBaseConnectivity().prepareStatement(query);
		preparedStatement.setString(1, cardNumber);
		ResultSet resultSet = preparedStatement.executeQuery();

		String accPinNumber = "";
		int incorrectPin = 0;
		boolean istrue = false;
		while (resultSet.next()) {
			accPinNumber = resultSet.getString("Pin");
			incorrectPin = resultSet.getInt("IncorrectPin");
			istrue = resultSet.getBoolean("IsAccBlock");
		}

		if (pinNumber.equals(accPinNumber)) {
			if (istrue) {
				String timeQuery = "SELECT TimeOfTransaction FROM UsersList WHERE CardNumber=?";
				PreparedStatement preparedStatementTime = DataBase.DataBaseConnectivity().prepareStatement(timeQuery);
				preparedStatementTime.setString(1, cardNumber);
				ResultSet resultSetTime = preparedStatementTime.executeQuery();
				java.sql.Timestamp timeStamp = null;

				while (resultSetTime.next()) {
					timeStamp = resultSetTime.getTimestamp("TimeOfTransaction");
				}
				// System.out.println(timeStamp);
				java.time.LocalDateTime dateTime = timeStamp.toLocalDateTime();
				java.time.Duration duration = java.time.Duration.between(dateTime.toLocalTime(), currentTime);
				long diffInHours = duration.toHours();

				//System.out.println("Difference in hours: " + diffInHours);
				if (diffInHours > 24) {
					String updateQuery = "UPDATE UsersList SET IncorrectPin = null, IsAccBlock = null WHERE CardNumber = ?";

					PreparedStatement preparedStatementUpdate = DataBase.DataBaseConnectivity()
							.prepareStatement(updateQuery);
					preparedStatementUpdate.setString(1, cardNumber);
					int rowsAffected = preparedStatementUpdate.executeUpdate();

					if (rowsAffected > 0) {

						menu.proceed(cardNumber);
					}
				} else {

					System.out.println("Your Account is in hold wait for 24 hours.");
					return 3;
				}
			}

			else {
				String blockAccount="UPDATE UsersList SET IncorrectPin=0 WHERE CardNumber=?";
				PreparedStatement preparedStatementUpdate = DataBase.DataBaseConnectivity().prepareStatement(blockAccount);

				
				preparedStatementUpdate.setString(1, cardNumber);

				int rowsAffected = preparedStatementUpdate.executeUpdate();
				menu.proceed(cardNumber);
			}

		} else {

			if (incorrectPin == 2) 
			{
				System.out.println("Your account has been withheld due to multiple login attempts");
				String blockAccount="UPDATE UsersList SET IsAccBlock=true WHERE CardNumber=?";
				PreparedStatement preparedStatementUpdate = DataBase.DataBaseConnectivity().prepareStatement(blockAccount);

				
				preparedStatementUpdate.setString(1, cardNumber);

				int rowsAffected = preparedStatementUpdate.executeUpdate();
				return 3;
			}
			else 
			{
				System.out.println("You have entered wrong pin.Enter the correct pin");

				String wrongPin = "UPDATE UsersList SET IncorrectPin = IncorrectPin+1, TimeOfTransaction = ? WHERE CardNumber = ?";

				PreparedStatement preparedStatementUpdate = DataBase.DataBaseConnectivity().prepareStatement(wrongPin);

				preparedStatementUpdate.setTime(1, time);
				preparedStatementUpdate.setString(2, cardNumber);

				int rowsAffected = preparedStatementUpdate.executeUpdate();

			}

		}
		return 0;

	}
}
