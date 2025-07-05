package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.DetalhePedidoDao;
import senac.senacfx.model.entities.Cliente;
import senac.senacfx.model.entities.DetalhePedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalhePedidoDaoJDBC implements DetalhePedidoDao {

    private Connection conn;

    public DetalhePedidoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(DetalhePedido obj) {
        String sql = "INSERT INTO detalhe_pedido (codigo_pedido, codigo_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, obj.getCodigoPedido());
            st.setLong(2, obj.getCodigoProduto());
            st.setInt(3, obj.getQuantidade());
            st.setDouble(4, obj.getPrecoUnitario());
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
    public void update(DetalhePedido obj) {
        String sql = "UPDATE detalhe_pedido SET codigo_pedido = ?, codigo_produto = ?, quantidade = ?, preco_unitario = ? WHERE codigo = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setLong(1, obj.getCodigoPedido());
            st.setLong(2, obj.getCodigoProduto());
            st.setInt(3, obj.getQuantidade());
            st.setDouble(4, obj.getPrecoUnitario());
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
    public DetalhePedido findById(Integer id) {
        return null;
    }

    @Override
    public List<DetalhePedido> findAll() {
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM detalhe_pedido ORDER BY codigo")) {
            ResultSet rs = st.executeQuery();
            List<DetalhePedido> list = new ArrayList<>();
            while (rs.next()) {
                DetalhePedido obj = instantiateDetalhePedido(rs);
                list.add(obj);
            }
            rs.close();
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private DetalhePedido instantiateDetalhePedido(ResultSet rs) throws SQLException {
        DetalhePedido obj = new DetalhePedido();
        obj.setCodigo(rs.getLong("codigo"));
        obj.setCodigoPedido(rs.getLong("codigo_pedido"));
        obj.setCodigoProduto(rs.getLong("codigo_produto"));
        obj.setQuantidade(rs.getInt("quantidade"));
        obj.setPrecoUnitario(rs.getDouble("preco_unitario"));
        return obj;
    }
}
