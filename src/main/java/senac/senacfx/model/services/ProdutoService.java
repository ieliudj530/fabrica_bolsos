package senac.senacfx.model.services;

import senac.senacfx.model.dao.DaoFactory;
import senac.senacfx.model.dao.ProdutoDao;
import senac.senacfx.model.entities.Produto;

import java.util.List;

public class ProdutoService {

    private ProdutoDao dao = DaoFactory.createProdutoDao();

    public List<Produto> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Produto obj) {
        if (obj.getCodigo() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void remove(Produto obj) {
        dao.deleteById(obj.getCodigo().intValue());
    }
}