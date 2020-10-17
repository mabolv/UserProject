import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static Connection con;
    private static String pass = "calvin";

    public static Connection getConnection(){
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root",pass);
            System.out.println("Connected");
        } catch (SQLException e) {
        	e.printStackTrace();
            System.out.println("Could not connect to database");
        }
    	
        return con;
    }
    	
}
