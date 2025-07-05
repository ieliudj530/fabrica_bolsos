package senac.senacfx.model.dao.impl;

import senac.senacfx.db.DbException;
import senac.senacfx.model.dao.PedidoDao;
import senac.senacfx.model.entities.Cliente;
import senac.senacfx.model.entities.Pedido;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDaoJDBC implements PedidoDao {

    private Connection conn;

    public PedidoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Pedido obj) {
        String sql = "INSERT INTO pedidos (codigo_cliente, codigo_entregador, data_pedido) VALUES (?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, obj.getCodigoCliente());
            st.setLong(2, obj.getCodigoEntregador());
            st.setTimestamp(3, Timestamp.valueOf(obj.getDataPedido()));
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
    public void update(Pedido obj) {
        String sql = "UPDATE pedidos SET codigo_cliente = ?, codigo_entregador = ?, data_pedido = ? WHERE codigo = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setLong(1, obj.getCodigoCliente());
            st.setLong(2, obj.getCodigoEntregador());
            st.setTimestamp(3, Timestamp.valueOf(obj.getDataPedido()));
            st.setLong(4, obj.getCodigo());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Pedido findById(Integer id) {
        return null;
    }

    @Override
    public List<Pedido> findAll() {
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM pedidos ORDER BY data_pedido DESC")) {
            ResultSet rs = st.executeQuery();
            List<Pedido> list = new ArrayList<>();
            while (rs.next()) {
                Pedido obj = instantiatePedido(rs);
                list.add(obj);
            }
            rs.close();
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private Pedido instantiatePedido(ResultSet rs) throws SQLException {
        Pedido obj = new Pedido();
        obj.setCodigo(rs.getLong("codigo"));
        obj.setCodigoCliente(rs.getLong("codigo_cliente"));
        obj.setCodigoEntregador(rs.getLong("codigo_entregador"));
        Timestamp ts = rs.getTimestamp("data_pedido");
        if (ts != null) {
            obj.setDataPedido(ts.toLocalDateTime());
        }
        return obj;
    }
}
