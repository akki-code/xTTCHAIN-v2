package classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Registration extends HttpServlet{

	private Connection conn = null;
	private PreparedStatement pst  = null;
	ResultSet rs = null;
	private String Ano = null;
	private int cutbal = 0;
	String unam = null;
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("name");
		String loc = request.getParameter("loc");
		
		switch(loc)
		{
		case "Delhi":
			cutbal = 50;
			break;
		case "Agra":
			cutbal = 30;
			break;
		case "Bombay":
			cutbal = 20;
			break;
		case "Meerut":
			cutbal = 70;
			break;
		}
		
		
		name = name.replaceAll("[^a-zA-z0-9]", "");
        
		System.out.println(name+"  "+loc);
		
		conn = (Connection) getServletContext().getAttribute("Conn");
		
		Boolean bb = false;
		
		try
		{
		pst = conn.prepareStatement("Select Name,Vehicle_Number,Aadhar_No from UserDetails");
        
        rs = pst.executeQuery();
        
        while(rs.next())
        {
        if(rs.getString("Vehicle_Number").equals(name))
        {
           	Ano = rs.getString("Aadhar_No");
           	unam = rs.getString("Name");
           	System.out.println(Ano+ "  is the ano for vno = "+name);
            bb = true;
           	break;
        }
		}
        
        if(bb)
        {
        String  M = null;
        pst = conn.prepareStatement("Select Money from Bank where AadharNo=?");
        pst.setString(1, Ano);
        rs = pst.executeQuery();
        
        while(rs.next())
        {
     	   M  = rs.getString("Money");
     	   System.out.println(M);
        }
        
        Integer money = Integer.parseInt(M);
        money = money - cutbal;
        
        M = Integer.toString(money);
        
        pst = conn.prepareStatement("Update Bank set Money=? where AadharNo=?");
        pst.setString(1, M);
        pst.setString(2, Ano);
        pst.execute();
        
        System.out.println(M);
        
        try
        {
        pst = conn.prepareStatement("Insert into "+unam+" values(?,?,?,?)");
        pst.setString(2, loc);
        pst.setString(3, Integer.toString(cutbal));
        pst.setString(4, M);
        
        pst.execute();
        }
        catch(Exception k)
        {
        System.out.println(k.getMessage());
        	
        }
        
        
        
        
        
        
        
        }
		}catch(Exception k)
		{
		   System.out.println("Error for inserting the data from Ankit"+k.getMessage());
		}
		
		
		
	}
	

}
