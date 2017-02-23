package br.com.jeffque.ia.ponte;

public class Agente {
	final Estado inicial;
	int melhorResultado;
	int iteracoesMenor = 0;
	int iteracoes = 0;
	Estado melhorEstado = null;
	
	public Agente(Estado inicial) {
		this.inicial = inicial;
		
		// heurística para um upper cut:
		// 		* ignora a existência do lado direito, já que posso fazer toda a migração sócom gente do esquerdo 
		// 		* repetir até não sobrar ninguém:
		// 		* 		vão as duas pessoas de maior peso
		// 		* 		se tiver algum do outro lado, retornar a pessoa mais leve
		// com isso, temos que:
		// 		* o mais pesado só conta uma vez
		// 		* o mais leve não pe levado em consideração
		// 		* todos os demais são contados duas vezes
		// qualquer resultado que isso contém trabalho em vão
		// em outras palavras:
		// 		heurística = soma todos * 2 - maior - 2*menor
		int somaHeuristica = 0;
		int maior = inicial.pessoasEsquerdo.get(0);
		int menor = inicial.pessoasEsquerdo.get(0);
		
		for (int peso: inicial.pessoasEsquerdo) {
			somaHeuristica += 2*peso;
			if (peso > maior) {
				maior = peso;
			} else if (peso < menor) {
				menor = peso;
			}
		}
		somaHeuristica = somaHeuristica - maior - 2 * menor;
		melhorResultado = somaHeuristica + 1;
		System.out.println("Soma heurística: " + somaHeuristica + "; resultado a priori: " + melhorResultado);
	}
	
	public void menorCusto() {
		Estado maquiado = EstadoBuilder.ordenacoes(inicial);
		menorCusto(maquiado, 0);
	}

	private void menorCusto(Estado estadoBase, int level) {
		iteracoes++;
		if (estadoBase.custoTotal >= melhorResultado) {
			return;
		}
		
		if (estadoBase.fim()) {
			melhorResultado = estadoBase.custoTotal;
			melhorEstado = estadoBase;
			iteracoesMenor = iteracoes;
			
			System.out.println(
					"Achou bom resultado!\n" +
					"\tmelhorResultado " + melhorResultado + "\n" +
					"\titeracoesMenor " + iteracoesMenor
				);
			
			return;
		}
		
		
		// se o level estiver par, então a lanterna tá no lado esquerdo
		if (estadoBase.lanternaLadoEsquerdo()) {
			try {
				if (estadoBase.pessoasEsquerdo.size() > 1) {
					// todas as possibilidades move 2 LR
					for (int i = 0; i < estadoBase.pessoasEsquerdo.size() - 1; i++) {
						for (int j = i + 1; j < estadoBase.pessoasEsquerdo.size(); j++) {
							menorCusto(estadoBase.move2personLR(i, j), level + 1);
						}
					}
				} else {
					for (int i = 0; i < estadoBase.pessoasEsquerdo.size(); i++) {
						menorCusto(estadoBase.move1personLR(i), level + 1);
					}
				}
			} catch (AcaoInvalidaException e) {
				e.printStackTrace();
				System.err.println("Ação inválida, ignorando outras ações possíveis");
			}
		} else {
			try {
				// todas as possibilidades move 1 RL
				for (int i = 0; i < estadoBase.pessoasDireito.size(); i++) {
					menorCusto(estadoBase.move1personRL(i), level + 1);
				}
			} catch (AcaoInvalidaException e) {
				e.printStackTrace();
				System.err.println("Ação inválida, ignorando outras ações possíveis");
			}
		}
	}

	private String tabs(int level) {
		String saida = "";
		
		for (int i = 0;i < level; i++) {
			saida += "\t";
		}
		return saida;
	}

	public Estado getMelhorEstado() {
		return melhorEstado;
	}

	public int iteracoesAteMenor() {
		return iteracoesMenor;
	}

	public int iteracoesTotal() {
		return iteracoes;
	}
}
