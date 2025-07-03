package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.DetalhePedidoDao;
import senac.senacfx.model.entities.DetalhePedido;

import java.util.List;

public class DetalhePedidoService {

    private DetalhePedidoDao dao = DaoFactory.createDetalhePedidoDao();

    public List<DetalhePedido> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(DetalhePedido obj) {
        if (obj.getCodigo() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(DetalhePedido obj) {
        dao.deleteById(obj.getCodigo().intValue());
    }
}
