package senac.senacfx.model.dao;

import senac.senacfx.model.entities.Cliente;

import java.util.List;

public interface ClienteDao {

    void insert(Cliente obj);
    void update(Cliente obj);
    void deleteById(Integer id);
    Cliente findById(Integer id);
    List<Cliente> findAll();
}