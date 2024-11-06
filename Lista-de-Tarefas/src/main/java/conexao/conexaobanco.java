package conexao;
import java.sql.Connection;
import java.sql.DriverManager;

public class conexaobanco {

	private Connection con;	
	private String DRIVER = "com.mysql.cj.jdbc.Driver";
	   private static final String URL = "jdbc:mysql://" + System.getenv("MYSQLHOST") + ":" + System.getenv("MYSQLPORT") + "/" + System.getenv("MYSQLDATABASE") + "?useSSL=false";
	    private static final String USER = System.getenv("MYSQLUSER");
	    private static final String PASSWORD = System.getenv("MYSQLPASSWORD");
	
	public Connection conexaologin() {
		try {
			Class.forName(DRIVER);
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
