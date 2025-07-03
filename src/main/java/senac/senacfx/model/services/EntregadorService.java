package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.EntregadorDao;
import senac.senacfx.model.entities.Entregador;

import java.util.List;

public class EntregadorService {

    private EntregadorDao dao = DaoFactory.createEntregadorDao();

    public List<Entregador> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Entregador obj) {
        if (obj.getCodigo() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Entregador obj) {
        dao.deleteById(obj.getCodigo().intValue());
    }
}