package conexao;
import java.sql.Connection;
import java.sql.DriverManager;

public class conexaobanco {

//	private Connection con;	
//	private String DRIVER = "com.mysql.cj.jdbc.Driver";
//    private static final String URL = System.getenv("DB_URL");
//    private static final String USER = System.getenv("DB_USER");
//    private static final String PASSWORD = System.getenv("DB_PASSWORD");
	
	private Connection con;	
	private String DRIVER = "com.mysql.cj.jdbc.Driver";
	private String URL = "jdbc:mysql://localhost:3306/Tarefas"; // TODO precisa verificar em outros computadores porque não é um banco online;
	private String USER = "root";
	private String PASSWORD = "Cc01102003+"; // TODO precisa verificar em outros computadores porque não é um banco
												// online;

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
