package senac.senacfx.model.entities;

import java.io.Serializable;

public class Entregador implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long codigo;
    private String nome;
    private String telefone;

    public Entregador() {}

    public Entregador(Long codigo, String nome, String telefone) {
        this.codigo = codigo;
        this.nome = nome;
        this.telefone = telefone;
    }

    public Long getCodigo() { return codigo; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entregador)) return false;
        Entregador other = (Entregador) o;
        return codigo != null && codigo.equals(other.codigo);
    }

    @Override
    public int hashCode() {
        return (codigo != null ? codigo.hashCode() : 0);
    }

    @Override
    public String toString() {
        return "Entregador{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
