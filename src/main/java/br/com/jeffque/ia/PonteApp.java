package br.com.jeffque.ia;

import br.com.jeffque.ia.ponte.Agente;
import br.com.jeffque.ia.ponte.Estado;
import br.com.jeffque.ia.ponte.EstadoBuilder;

/**
 * Trabalha do disciplina de IA do curso de Games da Unichristus
 * 
 * 1- Canibais e Missionários
 * 2- Bode, Capim e Lobo
 * 3- Ponte * (já implementado)
 * 4- Jogo da Velha
 * 5- Torre de Hanoi 4 discos
 */
public class PonteApp {
	public static void main(String[] args) {
		Agente ambiente = new Agente(buildEstadoInicial());
		ambiente.menorCusto();
		System.out.println("Menor custo: " + ambiente.getMelhorEstado().getCustoTotal());
		
		System.out.println("\nMelhor evolução de estados:");
		printEvolucaoEstados(ambiente.getMelhorEstado());
		System.out.println("Quantidade de iterações para o melhor: " + ambiente.iteracoesAteMenor());
		System.out.println("Quantidade de iterações para o melhor: " + ambiente.iteracoesTotal());
	}

	private static Estado buildEstadoInicial() {
		EstadoBuilder builder = new EstadoBuilder();
		
		builder.pessoasEsquerdo.add(1);
		builder.pessoasEsquerdo.add(2);
		builder.pessoasEsquerdo.add(5);
		builder.pessoasEsquerdo.add(10);
		
		builder.custoTotal = 0;
		builder.lanternaEsquerda = true;
		
		return builder.build();
	}

	private static void printEvolucaoEstados(Estado melhorEstado) {
		if (melhorEstado.getEstadoPai() != null) {
			printEvolucaoEstados(melhorEstado.getEstadoPai());
			System.out.println("");
		}
		System.out.println(melhorEstado);
	}
}
