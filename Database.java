import java.sql.*;

// for reading from the command line
import java.io.*;

// for the login window
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Database {

    // command line reader 
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private Connection con;

public Database() {
	return;
}

private void connect() {
	try {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		System.out.println("driver loaded");
		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug"; 
		String username = "ora_w4p8";
		String password = "a80401110";
		con = DriverManager.getConnection(connectURL,username,password);
		System.out.println("connection made");
}
	catch (SQLException ex)
	{
	    System.out.println("Message: " + ex.getMessage());
	}	 

}

private void showMenu()
{
int choice;
boolean quit;

quit = false;

try 
{
    // disable auto commit mode
    con.setAutoCommit(false);

    while (!quit)
    {
	System.out.print("\n\nPlease choose one of the following: \n");
	System.out.print("1.  Insert into table \n");
	System.out.print("2.  Delete from table\n");
	//System.out.print("3.  Update branch\n");
	System.out.print("4.  Show table\n");
	System.out.print("5.  Quit\n>> ");

	choice = Integer.parseInt(in.readLine());
	
	System.out.println(" ");

	switch(choice)
	{
	   case 1:  insert(); break;
	   case 2:  delete(); break;
	   //case 3:  updateBranch(); break;
	   case 4:  showtable(); break;
	   case 5:  quit = true;
	}
    }

    con.close();
    in.close();
    System.out.println("\nGood Bye!\n\n");
    System.exit(0);
}
catch (IOException e)
{
    System.out.println("IOException!");

    try
    {
	con.close();
	System.exit(-1);
    }
    catch (SQLException ex)
    {
	 System.out.println("Message: " + ex.getMessage());
    }
}
catch (SQLException ex)
{
    System.out.println("Message: " + ex.getMessage());
}

}

private void showtable() throws IOException {
	System.out.print("\n\nPlease choose a table: \n");
	String table = in.readLine();
		try {
			Statement stm = con.createStatement(); 
			ResultSet rs = stm.executeQuery("SELECT * FROM " + table );
			
			 // get info on ResultSet
			  ResultSetMetaData rsmd = rs.getMetaData();

			  // get number of columns
			  int numCols = rsmd.getColumnCount();

			  System.out.println(" ");
			  
			  // display column names;
			  for (int i = 0; i < numCols; i++)
			  {
			      // get column name and print it

			      System.out.printf("%-20.20s", rsmd.getColumnName(i+1));    
			  }
			  System.out.println(" ");
			  rs.next();
			  for(int i = 0; i < numCols; i++) {
				  System.out.printf("%-20.20s",  rs.getString(i+1));
			  }
			  System.out.println(" ");
			 
			  while(rs.next())
			  {
				
				  for(int i = 0; i < numCols; i++) {
					
				  if(rs.wasNull()) {
				
					  System.out.printf("%-20.20s", " ");
				  }
				  
				  else {
					
					  System.out.printf("%-20.20s",  rs.getString(i+1));
					 
				  }
				  }
				  System.out.println(" ");
				  }
	
		 
			  // close the statement; 
			  // the ResultSet will also be closed
			  stm.close();
			}
			catch (SQLException ex)
			{
			 
			    System.out.println("Message: " + ex.getMessage());
			}	
}

private void insert() throws IOException {
	System.out.print("\n\nPlease choose a table: \n");
	String table = in.readLine();
	String query = "insert into " + table + " values(";
	Statement stm;
	String temp;
	try {
		stm = con.createStatement();
		ResultSet rs = stm.executeQuery("SELECT * FROM " + table );
		 ResultSetMetaData rsmd = rs.getMetaData();
         int numCols = rsmd.getColumnCount();
         for (int i = 0; i < numCols; i++)
		  {
        	 if (i>0) {
        		 query = query.concat(", ");
        	 }
        	 
		     temp =rsmd.getColumnTypeName(i+1);
		     
		     if (temp.toLowerCase().contains("char")) {
		    	 query = query.concat("'");
		     }
        	 // get column name and print it
		      System.out.println("Specify " + rsmd.getColumnName(i+1) + ": ");
		      temp = in.readLine();
		     query = query.concat(temp);
		     temp =rsmd.getColumnTypeName(i+1);
		     if (temp.toLowerCase().contains("char")) {
		    	 query = query.concat("'");
		     }
		  }
         query = query.concat(")");
         stm.execute(query);  
         
	} catch (SQLException ex) {
		System.out.println("Message: " + ex.getMessage());
	} 
	
	
}

private void delete() throws IOException {
	Statement stm;
	System.out.println("\n\nPlease choose a table: \n");
	String table = in.readLine();
	System.out.println("\n\nPlease choose an attribute: \n");
	int colNum;
	String query;
	String temp;
	 try {
		stm = con.createStatement();
		 ResultSet rs = stm.executeQuery("SELECT * FROM " + table );
		 ResultSetMetaData rsmd = rs.getMetaData();
	     int numCols = rsmd.getColumnCount();
	     for (int i = 0; i < numCols; i++) {
	    	System.out.println((i+1)+" for " + rsmd.getColumnName(i+1));
	     }
		colNum = Integer.parseInt(in.readLine());
		
		System.out.println("Specify entry to delete");
		temp = in.readLine();
		if (temp.toLowerCase().contains("char")) {
			temp = "'"+temp+"'";
			
		}
		query = "DELETE FROM " + table + " WHERE " + rsmd.getColumnName(colNum) + " = " + temp;
		stm.execute(query);
		
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		} 
		
	
	
}

public static void main(String[] args) {
	Database d = new Database();	
	d.connect();
	d.showMenu();				
}
}
			
			
			
			
			
			
			
			
			
			
			
			

