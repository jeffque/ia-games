package br.com.jeffque.ia.ponte;

import java.util.ArrayList;

public class EstadoBuilder {
	public ArrayList<Integer> pessoasEsquerdo = new ArrayList<>();
	public ArrayList<Integer> pessoasDireito = new ArrayList<>();
	public int custoTotal;
	public Estado estadoPai;
	public boolean lanternaEsquerda;
	
	
	public EstadoBuilder() {
	}
	
	public static EstadoBuilder createEstadoFilho(Estado estado) {
		EstadoBuilder builder = new EstadoBuilder();
		builder.estadoPai = estado;
		
		builder.pessoasEsquerdo.addAll(estado.pessoasEsquerdo);
		builder.pessoasDireito.addAll(estado.pessoasDireito);
		
		builder.lanternaEsquerda = !estado.lanternaEsquerda;
		builder.custoTotal = estado.custoTotal;
		
		return builder;
	}


	public Estado build() {
		return new Estado(pessoasEsquerdo, pessoasDireito, custoTotal, estadoPai, lanternaEsquerda);
	}
}
