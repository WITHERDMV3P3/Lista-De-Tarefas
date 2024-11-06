package verificarusuario;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;

import conexao.conexaobanco;

public class consultanome {

	public boolean nameExistente(String nome) {

		Connection con = null;
		conexaobanco dao = new conexaobanco();
		boolean existe = false;

		
		try {
			con = dao.conexaologin();
			String sql = "SELECT COUNT(*) FROM Tarefas WHERE nome_da_tarefa = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, nome);

			ResultSet resultado = statement.executeQuery();

			if (resultado.next()) {
				existe = resultado.getInt(1) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close(); // Fecha a conexão
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return existe;
	}
}
