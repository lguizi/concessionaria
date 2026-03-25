package Concessionaria.model;

public class FipeItem {
    private String codigo;
    private String nome;

    public FipeItem(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
}