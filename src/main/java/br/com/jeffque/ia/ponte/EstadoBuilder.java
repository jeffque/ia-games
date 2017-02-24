package br.com.jeffque.ia.ponte;

import java.util.ArrayList;
import java.util.Comparator;

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
		
		// no estado anterior, tava no lado esquerdo?
		if (estado.lanternaEsquerda) {
			builder.lanternaEsquerda = false;
			builder.pessoasDireito.addAll(estado.pessoasDireito);
		} else {
			builder.lanternaEsquerda = true;
			builder.pessoasEsquerdo.addAll(estado.pessoasEsquerdo);
		}
		
		builder.custoTotal = estado.custoTotal;
		
		return builder;
	}


	public Estado build() {
		return new Estado(pessoasEsquerdo, pessoasDireito, custoTotal, estadoPai, lanternaEsquerda);
	}

	public static Estado ordenacoes(Estado estado) {
		EstadoBuilder builder = new EstadoBuilder();
		builder.estadoPai = estado;
		
		builder.lanternaEsquerda = estado.lanternaEsquerda;
		builder.pessoasDireito.addAll(estado.pessoasDireito);
		builder.pessoasEsquerdo.addAll(estado.pessoasEsquerdo);
		
		builder.custoTotal = estado.custoTotal;
		
		builder.pessoasEsquerdo.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return -o1.compareTo(o2);
			}
		});
		builder.pessoasDireito.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		
		return builder.build();
	}
}
