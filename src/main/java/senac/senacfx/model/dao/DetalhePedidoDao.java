package senac.senacfx.model.dao;

import senac.senacfx.model.entities.DetalhePedido;

import java.util.List;

public interface DetalhePedidoDao {

    void insert(DetalhePedido obj);
    void update(DetalhePedido obj);
    void deleteById(Integer id);
    DetalhePedido findById(Integer id);
    List<DetalhePedido> findAll();

}