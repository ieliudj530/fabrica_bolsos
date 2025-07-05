package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.ClienteDao;
import senac.senacfx.model.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDaoJDBC implements ClienteDao {

    private Connection conn;

    public ClienteDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Cliente obj) {
        String sql = "INSERT INTO cliente (nome, endereco, telefone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, obj.getNome());
            st.setString(2, obj.getEndereco());
            st.setString(3, obj.getTelefone());
            st.setString(4, obj.getEmail());

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
    public void update(Cliente obj) {
        String sql = "UPDATE cliente SET nome = ?, endereco = ?, telefone = ?, email = ? WHERE codigo = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, obj.getNome());
            st.setString(2, obj.getEndereco());
            st.setString(3, obj.getTelefone());
            st.setString(4, obj.getEmail());
            st.setLong(5, obj.getCodigo());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Cliente findById(Integer id) {
        return null;
    }

    @Override
    public List<Cliente> findAll() {
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM cliente ORDER BY nome")) {
            ResultSet rs = st.executeQuery();
            List<Cliente> list = new ArrayList<>();
            while (rs.next()) {
                Cliente obj = instantiateCliente(rs);
                list.add(obj);
            }
            rs.close();
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Cliente instantiateCliente(ResultSet rs) throws SQLException {
        Cliente obj = new Cliente();
        obj.setCodigo(rs.getLong("codigo"));
        obj.setNome(rs.getString("nome"));
        obj.setEndereco(rs.getString("endereco"));
        obj.setTelefone(rs.getString("telefone"));
        obj.setEmail(rs.getString("email"));
        return obj;
    }
}
