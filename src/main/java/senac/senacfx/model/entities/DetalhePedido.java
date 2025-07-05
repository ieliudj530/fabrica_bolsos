package senac.senacfx.model.entities;

import java.io.Serializable;

public class DetalhePedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long codigo;
    private Long codigoPedido;
    private Long codigoProduto;
    private Integer quantidade;
    private Double precoUnitario;

    public DetalhePedido() {}

    public DetalhePedido(Long codigo, Long codigoPedido, Long codigoProduto, Integer quantidade, Double precoUnitario) {
        this.codigo = codigo;
        this.codigoPedido = codigoPedido;
        this.codigoProduto = codigoProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Long getCodigo() { return codigo; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }

    public Long getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(Long codigoPedido) { this.codigoPedido = codigoPedido; }

    public Long getCodigoProduto() { return codigoProduto; }
    public void setCodigoProduto(Long codigoProduto) { this.codigoProduto = codigoProduto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(Double precoUnitario) { this.precoUnitario = precoUnitario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalhePedido)) return false;
        DetalhePedido other = (DetalhePedido) o;
        return codigo != null && codigo.equals(other.codigo);
    }

    @Override
    public int hashCode() {
        return (codigo != null ? codigo.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "DetalhePedido{" +
                "codigo=" + codigo +
                ", codigoPedido=" + codigoPedido +
                ", codigoProduto=" + codigoProduto +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                '}';
    }
}
