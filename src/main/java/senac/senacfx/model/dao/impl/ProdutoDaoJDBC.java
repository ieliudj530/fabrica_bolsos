package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.ProdutoDao;
import senac.senacfx.model.entities.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDaoJDBC implements ProdutoDao {

    private Connection conn;

    public ProdutoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Produto obj) {
        String sql = "INSERT INTO produto (nome, descricao, preco, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, obj.getNome());
            st.setString(2, obj.getDescricao());
            st.setDouble(3, obj.getPreco());
            st.setInt(4, obj.getStock());
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
    public void update(Produto obj) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, stock = ? WHERE codigo = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, obj.getNome());
            st.setString(2, obj.getDescricao());
            st.setDouble(3, obj.getPreco());
            st.setInt(4, obj.getStock());
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
    public Produto findById(Integer id) {
        return null;
    }

    @Override
    public List<Produto> findAll() {
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM produto ORDER BY nome")) {
            ResultSet rs = st.executeQuery();
            List<Produto> list = new ArrayList<>();
            while (rs.next()) {
                Produto obj = instantiateProduto(rs);
                list.add(obj);
            }
            rs.close();
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Produto instantiateProduto(ResultSet rs) throws SQLException {
        Produto obj = new Produto();
        obj.setCodigo(rs.getLong("codigo"));
        obj.setNome(rs.getString("nome"));
        obj.setDescricao(rs.getString("descricao"));
        obj.setPreco(rs.getDouble("preco"));
        obj.setStock(rs.getInt("stock"));
        return obj;
    }
}
