import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;


public class Users {
	private static Connection con = DB.getConnection();	

	public static void main(String[] args) throws SQLException {
		//Meny för val
		System.out.println("Välkommen, vänligen ange ett val \n"
				+ "1. Logga in \n"
				+ "2. Registrera ny användare \n"
				+ "3. Glömt lösenord \n");
		
		Scanner scan = new Scanner(System.in);
		String s = scan.next();
		
		//Inlogg och ändra info
		if(s.equals("1")) {
			System.out.println("Ange e-post: ");
			String epost = scan.next();
			System.out.println("Ange lösenord: ");
			String pass = scan.next();
			
			System.out.println(Login(epost, pass));
			if(Login(epost, pass)!="Ogiltig e-post eller lösenord") {
			System.out.println("Dina uppgifter, mata in siffra för att ändra info: \n "+getInfo(epost));
			
			String r = scan.next();
			System.out.println("Ange nytt värde: ");
			String t = scan.next();
			System.out.println("Nytt värde sparat: "+changeInfo(epost, t, r));
			}
		}
		
		//Registrering
		if(s.equals("2")) {
			System.out.println("Ange förnamn: ");
			String fname = scan.next();
			System.out.println("Ange efternamn: ");
			String sname = scan.next();
			System.out.println("Ange e-post: ");
			String epost = scan.next();
			System.out.println("Ange lösenord: ");
			String pass = scan.next();
			
			System.out.println(Register(fname, sname, epost, pass));    
		}

		//Glömt lösenord
		if(s.equals("3")) {
			System.out.println("Ange e-post");
			String epost = scan.next();
			
			System.out.println(Password(epost));
		}
	}
	
	//Metod för inlogg, kontrollerar att kombinationen av e-post och lösen finns
	static String Login(String epost, String pass) throws SQLException {

		String g = "";
		String h;
		PreparedStatement sta = con.prepareStatement("SELECT * FROM users WHERE email = '"+epost+"' and pass = '"+pass+"'");
		ResultSet r = sta.executeQuery();

		while (r.next()){
		g = r.getString("email");
		}
		if(g=="") {
	    h = "Ogiltig e-post eller lösenord";
		}
		else {
	    h = "Inloggad";
		}
		return h;
	}
	
	//Hämta info om användare
	static String getInfo(String ep) throws SQLException {
		String fname="", sname="", epost="", k="";
		
		PreparedStatement sta = con.prepareStatement("SELECT * FROM users WHERE email = '"+ep+"'");
		ResultSet r = sta.executeQuery();
		
		while (r.next()){
		fname = "Förnamn: "+r.getString("firstname");
		sname = "Efternamn: "+r.getString("surname");
		epost = "E-post: "+r.getString("email");
		}
		
		k = "1. "+fname+" 2. "+sname+" 3. "+epost;
		return k;
	}
	
	//Ändra info om användare
	static String changeInfo(String epost, String t, String r) throws SQLException {
		
		if(r.equals("1")) {
		PreparedStatement stat = con.prepareStatement("UPDATE users SET firstname = '"+t+"' WHERE email = '"+epost+"'");
	    stat.executeUpdate();
		}
		if(r.equals("2")) {
		PreparedStatement stat = con.prepareStatement("UPDATE users SET surname = '"+t+"' WHERE email = '"+epost+"'");
	    stat.executeUpdate();
		}
		if(r.equals("3")) {
		PreparedStatement stat = con.prepareStatement("UPDATE users SET email = '"+t+"' WHERE email = '"+epost+"'");
	    stat.executeUpdate();
		}
		
		return t;
	}
	
	//Registrera ny användare
	static String Register(String fname, String sname, String epost, String pass) throws SQLException {
		
		PreparedStatement stat = con.prepareStatement("CREATE TABLE IF NOT EXISTS users (firstname varchar(255), surname varchar(255), email varchar(255), pass varchar(255))");
	    stat.executeUpdate();
	    
	    String g = "";		
	    String h;
	    PreparedStatement sta = con.prepareStatement("SELECT email FROM users where email = '"+epost+"'");
		ResultSet r = sta.executeQuery();
	    while (r.next()){
    		g = r.getString("email");
    		}

    		if(g=="") {
    			PreparedStatement stat2 = con.prepareStatement("INSERT INTO users (firstname, surname, email, pass) VALUES ('"+fname+"', '"+sname+"', '"+epost+"', '"+pass+"')");
    		    stat2.executeUpdate();
    		    h = "Användare registrerad";
    		}
    		else {
    			h = "E-post redan registrerad";
    		}
    		return h;
	}
	
	//Nytt lösen till användare
	static String Password(String epost) throws SQLException {
		String g = "";		 
		String h;
	    PreparedStatement sta = con.prepareStatement("SELECT email FROM users where email = '"+epost+"'");
		ResultSet r = sta.executeQuery();
	    while (r.next()){
    		g = r.getString("email");
    		}
    		if(g=="") {
    			h = "Ogiltig e-post";
    		}
    		else {
    			char[] c = generatePassword(8);
    			String newpass = String.valueOf(c);
    			
    			PreparedStatement stat = con.prepareStatement("UPDATE users SET pass = '"+newpass+"' WHERE email = '"+g+"'");
    		    stat.executeUpdate();
    		    h = "Ditt nya lösenord är: "+newpass;
    		}
    		return h;
	}

	//Lösenordsgenerator
	static char[] generatePassword(int length) {
	      String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	      String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
	      String specialCharacters = "!@#$";
	      String numbers = "1234567890";
	      String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
	      Random random = new Random();
	      char[] password = new char[length];

	      password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
	      password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
	      password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
	      password[3] = numbers.charAt(random.nextInt(numbers.length()));
	   
	      for(int i = 4; i< length ; i++) {
	         password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
	      }
	      return password;
	   }
	
}
