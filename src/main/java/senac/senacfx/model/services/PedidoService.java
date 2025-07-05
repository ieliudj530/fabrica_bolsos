package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.PedidoDao;
import senac.senacfx.model.entities.Pedido;

import java.util.List;

public class PedidoService {

    private PedidoDao dao = DaoFactory.createPedidoDao();

    public List<Pedido> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Pedido obj) {
        if (obj.getCodigo() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Pedido obj) {
        dao.deleteById(obj.getCodigo().intValue());
    }
}