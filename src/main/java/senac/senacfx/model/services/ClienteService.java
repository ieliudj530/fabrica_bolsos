package senac.senacfx.model.services;

import senac.senacfx.model.dao.ClienteDao;
import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.entities.Cliente;

import java.util.List;

public class ClienteService {

    private ClienteDao dao = DaoFactory.createClienteDao();

    public List<Cliente> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Cliente obj) {
        if (obj.getCodigo() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Cliente obj){
        dao.deleteById(obj.getCodigo().intValue());
    }
}