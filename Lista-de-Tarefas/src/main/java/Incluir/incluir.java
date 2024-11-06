package Incluir;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.conexaobanco;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import verificarusuario.consultanome;

/**
 * Servlet implementation class incluir
 */
@MultipartConfig
@WebServlet("/incluir")
public class incluir extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection con = null;
		conexaobanco dao = new conexaobanco();

		String tarefa = request.getParameter("nome-tarefa");
		String custo = request.getParameter("custo");
		String data = request.getParameter("data");

		consultanome consultanome = new consultanome();

		if (consultanome.nameExistente(tarefa)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

		} else {

			try {
				con = dao.conexaologin();
				
				String ordem = "SELECT MAX(ordem_apresentacao) FROM Tarefas";
				PreparedStatement ordemmax = con.prepareStatement(ordem);
				ResultSet resultadoordem = ordemmax.executeQuery();
				
				int novaordem = 1;
				if(resultadoordem.next()) {
					novaordem = resultadoordem.getInt(1) + 1;
				}
				
				
			
				String sql = "INSERT INTO Tarefas (Nome_da_tarefa , Custo, data_limite, ordem_apresentacao) VALUES (?, ?, ?, ?)";
				PreparedStatement statement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				statement.setString(1, tarefa);
				statement.setString(2, custo);
				statement.setString(3, data);
				statement.setInt(4, novaordem);

				statement.executeUpdate();
			} catch (Exception e) {
				response.getWriter().println("Erro ao registrar a tarefa: " + e.getMessage());
			} finally {
				if (con != null) {
					try {
						con.close(); // Fecha a conexão
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
