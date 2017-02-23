package br.com.jeffque.ia.ponte;

public class Agente {
	final Estado inicial;
	int melhorResultado;
	int iteracoesMenor = 0;
	int iteracoes = 0;
	Estado melhorEstado = null;
	
	public Agente(Estado inicial) {
		this.inicial = inicial;
		
		// heurística para um upper cut: todasas pessoas andam duas vezes
		// qualquer resultado que isso contém trabalho em vão
		int somaHeuristica = 0;
		for (int peso: inicial.pessoasEsquerdo) {
			somaHeuristica += 2*peso;
		}
		
		for (int peso: inicial.pessoasDireito) {
			somaHeuristica += 2*peso;
		}
		
		melhorResultado = somaHeuristica + 1;
	}
	
	public int menorCusto() {
		return menorCusto(inicial, 0, "Inicial");
	}

	private int menorCusto(Estado estadoBase, int level, String obs) {
		
		System.out.println(tabs(level) + "Iteração " + iteracoes++);
		System.out.println(tabs(level) + "Observação: " + obs);
		System.out.println(tabs(level) + "Fazendo a busca no estado (level " + level + "):\n" + estadoBase.toString(tabs(level)));
		System.out.println(tabs(level) + "Custo atual " + melhorResultado);
		System.out.println("\n");
		if (estadoBase.custoTotal >= melhorResultado) {
			return melhorResultado;
		}
		
		if (estadoBase.fim()) {
			melhorResultado = estadoBase.custoTotal;
			melhorEstado = estadoBase;
			iteracoesMenor = iteracoes;
			return melhorResultado;
		}
		
		
		// se o level estiver par, então a lanterna tá no lado esquerdo
		if (estadoBase.lanternaLadoEsquerdo()) {
			try {
				if (estadoBase.pessoasEsquerdo.size() > 1) {
					// todas as possibilidades move 2 LR
					for (int i = 0; i < estadoBase.pessoasEsquerdo.size() - 1; i++) {
						for (int j = i + 1; j < estadoBase.pessoasEsquerdo.size(); j++) {
							menorCusto(estadoBase.move2personLR(i, j), level + 1, "Movendo as pessoas " + estadoBase.getPessoaEsquerdo(i) + " e a pessoa " + estadoBase.getPessoaEsquerdo(j) + " da esquerda pra direita");
						}
					}
				} else {
					for (int i = 0; i < estadoBase.pessoasEsquerdo.size(); i++) {
						menorCusto(estadoBase.move1personLR(i), level + 1, "Movendo a pessoa " + estadoBase.getPessoaEsquerdo(i) + " da esquerda pra direita");
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
					menorCusto(estadoBase.move1personRL(i), level + 1, "Movendo a pessoa " + estadoBase.getPessoaDireito(i) + " da direita pra esquerda");
				}
			} catch (AcaoInvalidaException e) {
				e.printStackTrace();
				System.err.println("Ação inválida, ignorando outras ações possíveis");
			}
		}
		
		return melhorResultado;
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
