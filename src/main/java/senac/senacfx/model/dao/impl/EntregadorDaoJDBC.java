package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.EntregadorDao;
import senac.senacfx.model.entities.Cliente;
import senac.senacfx.model.entities.Entregador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregadorDaoJDBC implements EntregadorDao {

    private Connection conn;

    public EntregadorDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Entregador obj) {
        String sql = "INSERT INTO entregadores (nome, telefone) VALUES (?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, obj.getNome());
            st.setString(2, obj.getTelefone());
            int rows = st.executeUpdate();
            if (rows > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    obj.setCodigo(rs.getLong(1));
                }
                rs.close();
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha foi inserida.");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Entregador obj) {
        String sql = "UPDATE entregadores SET nome = ?, telefone = ? WHERE codigo = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, obj.getNome());
            st.setString(2, obj.getTelefone());
            st.setLong(3, obj.getCodigo());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Entregador findById(Integer id) {
        return null;
    }

    @Override
    public List<Entregador> findAll() {
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM entregadores ORDER BY nome")) {
            ResultSet rs = st.executeQuery();
            List<Entregador> list = new ArrayList<>();
            while (rs.next()) {
                Entregador obj = instantiateEntregador(rs);
                list.add(obj);
            }
            rs.close();
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Entregador instantiateEntregador(ResultSet rs) throws SQLException {
        Entregador obj = new Entregador();
        obj.setCodigo(rs.getLong("codigo"));
        obj.setNome(rs.getString("nome"));
        obj.setTelefone(rs.getString("telefone"));
        return obj;
    }
}
