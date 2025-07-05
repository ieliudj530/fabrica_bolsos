package senac.senacfx.model.dao;

import senac.senacfx.db.DB;
import senac.senacfx.model.dao.impl.*;
import senac.senacfx.model.entities.Entregador;

import java.util.List;

public class DaoFactory {


    public static ClienteDao createClienteDao() {
        return new ClienteDaoJDBC(DB.getConnection());
    }

    public static ProdutoDao createProdutoDao() {
        return new ProdutoDaoJDBC(DB.getConnection());
    }

    public static EntregadorDao createEntregadorDao() {
        return new EntregadorDaoJDBC(DB.getConnection());
    }

    public static PedidoDao createPedidoDao() {
        return new PedidoDaoJDBC(DB.getConnection());
    }

    public static DetalhePedidoDao createDetalhePedidoDao() {
        return new DetalhePedidoDaoJDBC(DB.getConnection());
    }
}