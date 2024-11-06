package Consulta;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import conexao.conexaobanco;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class ConsultaBD
 */
@WebServlet("/consultabd")
public class ConsultaBD extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		conexaobanco dao = new conexaobanco();


		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		List<String[]> dados = new ArrayList<>();

		try {
			con = dao.conexaologin();
			String sql = "SELECT id , Nome_da_tarefa , Custo, data_limite FROM Tarefas ORDER BY ordem_apresentacao";
			PreparedStatement statement = con.prepareStatement(sql);

			ResultSet resultado = statement.executeQuery();

			while (resultado.next()) {
				String[] linha = { resultado.getString("id"), resultado.getString("Nome_da_tarefa"),
						resultado.getString("Custo"), resultado.getString("data_limite") };
				dados.add(linha);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("erro", "Erro ao acessar o banco de dados: " + e.getMessage());
		}finally {
			if (con != null) {
				try {
					con.close(); // Fecha a conexão
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		String json = new Gson().toJson(dados);
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}