package Tarefa;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.conexaobanco;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class moverTarefa
 */
@WebServlet("/moverTarefa")
public class moverTarefa extends HttpServlet {
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

		String tarefaid = request.getParameter("idtarefa");
		String direcao = request.getParameter("direcao");


		try {
			con = dao.conexaologin();
			String sql = "SELECT ordem_apresentacao FROM Tarefas WHERE id = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, tarefaid);

			ResultSet resultado = statement.executeQuery();

			if (resultado.next()) {

				int ordemAtual = resultado.getInt("ordem_apresentacao");
				
			    String sqlMaxOrdem = "SELECT MAX(ordem_apresentacao) FROM Tarefas";
			    PreparedStatement stmtMaxOrdem = con.prepareStatement(sqlMaxOrdem);
			    ResultSet rsMaxOrdem = stmtMaxOrdem.executeQuery();
			    
			    int maxOrdem = 1; 
			    if (rsMaxOrdem.next()) {
			        maxOrdem = rsMaxOrdem.getInt(1);
			    }
			    
			    if ((direcao.equals("down") && ordemAtual == maxOrdem) || (direcao.equals("up") && ordemAtual == 1)) {

			        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			        return; 
			    }
				
				int novaOrdem = direcao.equals("up") ? ordemAtual - 1 : ordemAtual + 1;

				if (novaOrdem > 0) {

					String trocarordemsql = "UPDATE Tarefas SET ordem_apresentacao = ? WHERE ordem_apresentacao = ?";
					PreparedStatement trocarstatement = con.prepareStatement(trocarordemsql);
					trocarstatement.setInt(1, ordemAtual);
					trocarstatement.setInt(2, novaOrdem);
					trocarstatement.executeUpdate();

					String updateOrderSQL = "UPDATE Tarefas SET ordem_apresentacao = ? WHERE id = ?";
					PreparedStatement updateOrderStmt = con.prepareStatement(updateOrderSQL);
					updateOrderStmt.setInt(1, novaOrdem);
					updateOrderStmt.setString(2, tarefaid);
					updateOrderStmt.executeUpdate();

					response.setStatus(HttpServletResponse.SC_OK);

				} else {
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("erro", "Erro ao acessar o banco de dados: " + e.getMessage());
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
