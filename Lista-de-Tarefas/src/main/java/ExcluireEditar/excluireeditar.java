package ExcluireEditar;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import conexao.conexaobanco;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import verificarusuario.consultanome;

/**
 * Servlet implementation class excluir
 */
@MultipartConfig
@WebServlet("/excluireeditar")
public class excluireeditar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TESTE
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		conexaobanco dao = new conexaobanco();
		Connection con = null;

		String id_tarefa = request.getParameter("id_tarefa");
		String nometarefa = request.getParameter("editar-nome-tarefa");
		String custo = request.getParameter("editar-custo");
		String data = request.getParameter("editar-data");

		String TarefaAtual = null;

		consultanome consultanome = new consultanome();
		try {
			con = dao.conexaologin();

			String sqlSelect = "SELECT Nome_da_tarefa FROM Tarefas WHERE id = ?";
			PreparedStatement stmtSelect = con.prepareStatement(sqlSelect);
			stmtSelect.setString(1, id_tarefa);
			ResultSet rs = stmtSelect.executeQuery();

			if (rs.next()) {
				TarefaAtual = rs.getString("Nome_da_tarefa");
			}

			if (!nometarefa.equals(TarefaAtual) && consultanome.nameExistente(nometarefa)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Nome da tarefa já existe.");
			} else {

				String sql = "UPDATE Tarefas SET Nome_da_tarefa = ?, Custo = ?, data_limite = ?  WHERE id = ?";
				PreparedStatement statement = con.prepareStatement(sql);
				statement.setString(1, nometarefa);
				statement.setString(2, custo);
				statement.setString(3, data);
				statement.setString(4, id_tarefa);

				statement.executeUpdate();

				int rows = statement.executeUpdate();

				if (rows > 0) {
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write("Tarefa atualizado com sucesso.");
				} else {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tarefa não encontrado.");
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Erro ao acessar o banco de dados: " + e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		conexaobanco dao = new conexaobanco();
		Connection con = null;

		String id_tarefa = request.getParameter("id_tarefa");

		if (id_tarefa == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetros inválidos.");
			return;
		}

		try {
			con = dao.conexaologin();
			String sql = "DELETE FROM Tarefas WHERE id = ? ";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, id_tarefa);

			int rowsDeleted = statement.executeUpdate();

			if (rowsDeleted > 0) {
				atualizarbd();
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("Tarefa excluída com sucesso.");
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tarefa não encontrada.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Erro ao acessar o banco de dados: " + e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void atualizarbd() {
		conexaobanco dao = new conexaobanco();
		Connection con = null;

		try {
			con = dao.conexaologin();

			// Contar o número total de registros na tabela
			String countSql = "SELECT COUNT(*) FROM Tarefas";
			PreparedStatement countStmt = con.prepareStatement(countSql);
			ResultSet countRs = countStmt.executeQuery();

			int totalTarefas = 0;
			if (countRs.next()) {
				totalTarefas = countRs.getInt(1); // Obtém o número total de registros
			}

			// Reorganizar a ordem de todos os registros
			String selectSql = "SELECT id FROM Tarefas ORDER BY ordem_apresentacao";
			PreparedStatement selectStmt = con.prepareStatement(selectSql);
			ResultSet rs = selectStmt.executeQuery();

			int novaOrdem = 1;
			while (rs.next()) {
				String idTarefa = rs.getString("id");

				// Atualiza a ordem das tarefas para uma sequência contínua
				String updateSql = "UPDATE Tarefas SET ordem_apresentacao = ? WHERE id = ?";
				PreparedStatement updateStmt = con.prepareStatement(updateSql);
				updateStmt.setInt(1, novaOrdem); // Atribui a nova ordem sequencial
				updateStmt.setString(2, idTarefa); // ID da tarefa
				updateStmt.executeUpdate();

				novaOrdem++; // Incrementa a ordem para a próxima tarefa
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
