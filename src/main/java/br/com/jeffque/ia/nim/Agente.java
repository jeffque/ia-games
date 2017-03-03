package br.com.jeffque.ia.nim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.jeffque.ia.AcaoInvalidaException;

public class Agente {
	Estado estadoFinal = null;
	long iteracoes = 0;
	Map<Estado, Boolean> cache = new HashMap<>();
	
	private class ResultValue {
		Estado estadoPai;
		Boolean vitoriaPrincipal;
		
		ResultValue(Estado estadoPai) {
			this.estadoPai = estadoPai;
		}
		
		public void setValorVitoriaPrincipal(boolean valorVitoria) {
			cache.put(estadoPai, valorVitoria);
			setValorVitoriaPrincipalNoCache(valorVitoria);
		}

		private void setValorVitoriaPrincipalNoCache(boolean valorVitoria) {
			this.vitoriaPrincipal = valorVitoria;
		}

		Boolean defineVitoria() {
			// se já achei a vitória, acabou a conversa...
			if (vitoriaPrincipal != null) {
				return vitoriaPrincipal;
			}
			
			// se tem um valor cached para isso, aproveita ele
			Boolean cachedValue = cache.get(estadoPai);
			if (cachedValue != null) {
				setValorVitoriaPrincipalNoCache(cachedValue);
			}
			
			// se chegou no estado final, então quem tá jogando perdeu
			if (estadoPai.fim()) {
				setValorVitoriaPrincipal(estadoPai.vencedor(true));
				
				if (vitoriaPrincipal && estadoFinal == null) {
					estadoFinal = estadoPai;
				}
				
				return vitoriaPrincipal;
			}
			// se for o jogador atual, se tiver uma vitoria é o suficiente
			// para garantir vencer
			if (estadoPai.jogador) {
				return defineUmaVitoriaAtual();
			} else {
				// caso contrário, o adversário que vai jogar,
				// então precisa garantir todas as vitórias
				return defineTodasVitoriaAtual();
			}
		}
		
		Boolean defineTodasVitoriaAtual() {
			if (vitoriaPrincipal != null) {
				return vitoriaPrincipal;
			}
			iteracoes++;
			
			// procura nos filhos por nulos e verdadeiros
			// no primeiro verdadeira que achar, deu tudo certo
			for (Estado avaliacao: createFilhos(estadoPai)) {
				ResultValue nextResult = new ResultValue(avaliacao);
				
				if (nextResult == null || nextResult.defineVitoria() == null) {
					return null;
				} else if (!nextResult.defineVitoria()) {
					setValorVitoriaPrincipal(false);
					return vitoriaPrincipal;
				}
			}
			
			setValorVitoriaPrincipal(true);
			return vitoriaPrincipal;
		}
		
		Boolean defineUmaVitoriaAtual() {
			if (vitoriaPrincipal != null) {
				return vitoriaPrincipal;
			}
			iteracoes++;
			boolean findOneNull = false;
			
			// procura nos filhos por nulos e verdadeiros
			// no primeiro verdadeira que achar, deu tudo certo
			for (Estado avaliacao: createFilhos(estadoPai)) {
				ResultValue nextResult = new ResultValue(avaliacao);
				
				if (nextResult == null || nextResult.defineVitoria() == null) {
					findOneNull = true;
				} else if (nextResult.defineVitoria()) {
					setValorVitoriaPrincipal(true);
					return vitoriaPrincipal;
				}
			}
			
			if (findOneNull) {
				return null;
			}
			
			setValorVitoriaPrincipal(false);
			return vitoriaPrincipal;
		}
	}
	
	private Boolean defineVitoria(Estado julgamento) {
		if (julgamento != null) {
			ResultValue result = new ResultValue(julgamento);
			return result.defineVitoria();
		} else {
			return null;
		}
	}
	
	final Estado estadoInicial;
	
	public Agente(Estado estadoInicial) {
		this.estadoInicial = estadoInicial;
	}
	
	public List<Estado> createFilhos(Estado avaliacao) {
		List<Estado> lista = new ArrayList<>();
		for (int i = Math.min(avaliacao.tamanhoPilha, 3); i >= 1; i--) {
			try {
				Estado candidato = avaliacao.jogada(avaliacao.jogador, i);
				
				lista.add(candidato);
			} catch (AcaoInvalidaException e) {
				e.printStackTrace();
			}
		}
		
		return lista;
	}
	
	public void menorCusto() {
		defineVitoria(estadoInicial);
		System.out.println("Chegou no fim depois de " + iteracoes + " iteracoes");
		printResultadoVitorioso();
	}
	
	public void printResultadoVitorioso() {
		if (estadoFinal != null) {
//			printResultadoVitorioso(estadoFinal);
			System.out.println("Jogador principal consegue facilmente vencer sempre com pilha de tamanho " + estadoInicial.tamanhoPilha);
		} else {
			System.out.println("Não há como vencer pilha de tamanho " + estadoInicial.tamanhoPilha);
		}
	}

	private void printResultadoVitorioso(Estado estado) {
		if (estado != null) {
			printResultadoVitorioso(estado.estadoPai);
			System.out.println("tamanho " + estado.tamanhoPilha + " jogador " + (estado.jogador? "principal": "adversário"));
		}
	}

}
