package senac.senacfx.model.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long codigo;
    private Long codigoEntregador;
    private Long codigoCliente;
    private LocalDateTime dataPedido;

    public Pedido() {}

    public Pedido(Long codigo, Long codigoEntregador, Long codigoCliente, LocalDateTime dataPedido) {
        this.codigo = codigo;
        this.codigoEntregador = codigoEntregador;
        this.codigoCliente = codigoCliente;
        this.dataPedido = dataPedido;
    }

    public Long getCodigo() { return codigo; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }

    public Long getCodigoEntregador() { return codigoEntregador; }
    public void setCodigoEntregador(Long codigoEntregador) { this.codigoEntregador = codigoEntregador; }

    public Long getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(Long codigoCliente) { this.codigoCliente = codigoCliente; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido other = (Pedido) o;
        return codigo != null && codigo.equals(other.codigo);
    }

    @Override
    public int hashCode() {
        return (codigo != null ? codigo.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "codigo=" + codigo +
                ", codigoEntregador=" + codigoEntregador +
                ", codigoCliente=" + codigoCliente +
                ", dataPedido=" + dataPedido +
                '}';
    }
}
