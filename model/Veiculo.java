package Concessionaria.model;

import Concessionaria.helper.Utils;

public class Veiculo {
    private String marca;
    private String modelo;
    private String ano;
    private Double valorCompra;
    private Double valorVenda;
    private Double valorFipe;

    public Veiculo(String marca, String modelo, String ano, Double valorCompra, Double valorVenda, Double valorFipe) {
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.valorFipe = valorFipe;
    }

    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getAno() { return ano; }
    public Double getValorCompra() { return valorCompra; }
    public Double getValorVenda() { return valorVenda; }
    public void setValorVenda(Double valorVenda) { this.valorVenda = valorVenda; }
    public Double getValorFipe() { return valorFipe; }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + ano + ") | " +
                "Compra: " + Utils.doubleParaString(valorCompra) + " | " +
                "Venda: " + Utils.doubleParaString(valorVenda) + " | " +
                "FIPE: " + Utils.doubleParaString(valorFipe);
    }
}