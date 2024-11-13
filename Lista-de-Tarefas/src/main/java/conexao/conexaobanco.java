package conexao;
import java.sql.Connection;
import java.sql.DriverManager;

public class conexaobanco {

	private Connection con;	
	private String DRIVER = "com.mysql.cj.jdbc.Driver";
	   private static final String URL = "jdbc:mysql://database-1.c14ws6mwshj4.us-east-1.rds.amazonaws.com:3306/Tarefas?useSSL=false&allowPublicKeyRetrieval=true";
	    private static final String USER = "root";
	    private static final String PASSWORD = "YoFmkHRvR6p2366k93YT";
	
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
