package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Entregador;

import java.util.List;

public interface EntregadorDao {

    void insert(Entregador obj);
    void update(Entregador obj);
    void deleteById(Integer id);
    Entregador findById(Integer id);
    List<Entregador> findAll();

}