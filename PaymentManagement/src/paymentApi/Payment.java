package paymentApi;

import java.sql.*;

import connection.PaymentConnection;

public class Payment {

	PaymentConnection paymentConnection = new PaymentConnection();
    
	public String insertPayment(String cardType, String cardNumber , String expireDate, String cvc) 
	{ 
		String output = ""; 
		try
		{ 
			Connection connect = paymentConnection.getConnection(); 
			if (connect == null) 
			{
				return "Error while connecting to the database for inserting."; 
			} 
			// create a prepared statement
			String query = " insert into payment(`cardID`,`cardType`,`cardNumber`,`expireDate`,`cvc`)"
					+ " values (?, ?, ?, ?, ?)"; 
			PreparedStatement preparedStmt = connect.prepareStatement(query); 
			// binding values
			preparedStmt.setInt(1, 0); 
			preparedStmt.setString(2, cardType);
			preparedStmt.setString(3, cardNumber);
			preparedStmt.setString(4, expireDate);
			preparedStmt.setInt(5, Integer.parseInt(cvc));
			// execute the statement3
			preparedStmt.execute(); 
			connect.close(); 
			String newPayment = readPayments();
			output =  "{\"status\":\"success\", \"data\": \""
					+ newPayment + "\"}";
		} 
		catch (Exception e) 
		{ 
			output =  "{\"status\":\"error\", \"data\": \"Error while inserting the Card.\"}";  
			System.err.println(e.getMessage()); 
		} 
		return output; 
	 } 
	
	public String readPayments(){
    	
		String output = "";
		
		try{
			Connection connect = paymentConnection.getConnection();
			if (connect == null){
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table class='table table-bordered' style='border-color:#1A237E; overflow-x:auto;'>"
					+ "<thead>"
					+ "<tr>"
					+ "<th scope='col'>#</th>"
					+ "<th scope='col'>ID</th>"
					+ "<th scope='col'>Type</th>"
					+ "<th scope='col'>Number</th>"
					+ "<th scope='col'>Expire Date</th>"
					+ "<th scope='col'>CVC</th>"
					+ "<th scope='col'>Update</th>"
					+ "<th scope='col'>Delete</th>"
					+ "</tr>"
					+ "</thead>";
			String query = "select * from `payment`";
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			int i=0;
			
			output += "<tbody>";
			
			// iterate through the rows in the result set
			while (rs.next()){
				i++;
				String hash = Integer.toString(i);
				String cardID = Integer.toString(rs.getInt("cardID"));
				String cardType = rs.getString("cardType");
				String cardNumber = rs.getString("cardNumber");
				String expireDate = rs.getString("expireDate");
				String cvc = Integer.toString(rs.getInt("cvc"));
				
				// Add into the html table
				output += "<tr>";
				output += "<td scope='row'>" + hash + "</td>";
				output += "<td>" + cardID + "</td>";
				output += "<td>" + cardType + "</td>"; 
				output += "<td>" + cardNumber + "</td>";
				output += "<td>" + expireDate + "</td>";
				output += "<td>" + cvc + "</td>"; 
				// buttons
				output += "<td>"
						+ "<button type='button' name='btnUpdate' class='btn operation-update btnUpdate' style='text-decoration: none;' data-cardid='" + cardID + "'>"
						+ "<svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' class='bi bi-arrow-clockwise operation-update' viewBox='0 0 16 16'>"
						+ "<path fill-rule='evenodd' d='M8 3a5 5 0 1 0 4.546 2.914.5.5 0 0 1 .908-.417A6 6 0 1 1 8 2v1z'/>"
						+ "<path d='M8 4.466V.534a.25.25 0 0 1 .41-.192l2.36 1.966c.12.1.12.284 0 .384L8.41 4.658A.25.25 0 0 1 8 4.466z'/>"
						+ "</svg>Update"
						+ "</button>"
						+ "</td>"
						+ "<td>"
						+ "<button type='button' name='btnRemove' class='btn operation-delete btnRemove' style='text-decoration: none;' data-cardid='" + cardID + "'>"
						+ "<svg xmlns='http://www.w3.org/2000/svg' width='24' height='24' fill='currentColor' class='bi bi-x operation-delete' viewBox='0 0 16 16'>"
						+ "<path d='M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z'/>"
						+ "</svg>Delete"
						+ "</button>"
						+ "</td>"
						+ "</tr>";
				}
				connect.close();
				
				output+= "</tbody>";
				
				// Complete the html table
				output += "</table>";
			}
			catch (Exception e){
				output = "Error while reading the card.";
				System.err.println(e.getMessage());
			}
		return output;
	}
    
    public String updatePayment(String ID, String cardType, String cardNumber, String expireDate, String cvc)
    { 
    	String output = ""; 
    try
    { 
    	Connection connect = paymentConnection.getConnection(); 
    	if (connect == null) 
    	{
    		return "Error while connecting to the database for updating."; 
    	} 
    	// create a prepared statement
    	String query = "UPDATE payment SET cardType=?, cardNumber=?, expireDate=?, cvc=? WHERE cardID=?"; 
    	
    	PreparedStatement preparedStmt = connect.prepareStatement(query); 
    	// binding values
    	preparedStmt.setString(1, cardType);
    	preparedStmt.setString(2, cardNumber);
    	preparedStmt.setString(3, expireDate); 
    	preparedStmt.setInt(4, Integer.parseInt(cvc)); 
    	preparedStmt.setInt(5, Integer.parseInt(ID)); 
    	// execute the statement
    	preparedStmt.execute(); 
    	connect.close(); 
    	String newPayment = readPayments();    
		output = "{\"status\":\"success\", \"data\": \"" +        
				newPayment + "\"}";  
    	} 
    	catch (Exception e) 
    	{ 
    		output =  "{\"status\":\"error\", \"data\": \"Error while updating the Card.\"}";
    		System.err.println(e.getMessage()); 
    	} 
    	return output; 
    }   

    public String deletePayment(String cardID){
    	
		String output = "";
		
		try{
			Connection connect = paymentConnection.getConnection();
			if (connect == null){
				return "Error while connecting to the database for deleting.";
			}
			// create a prepared statement
			String query = "delete from `payment` where cardID=?";
			PreparedStatement preparedStmt = connect.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(cardID));

			// execute the statement
			preparedStmt.execute();
			connect.close();
			String newPayment = readPayments();    
			output = "{\"status\":\"success\", \"data\": \"" +       
					newPayment + "\"}";  
		}
		catch (Exception e){
			output = "Error while deleting the Card.";
			System.err.println(e.getMessage());
		}
		return output;
	}
}
