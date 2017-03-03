package br.com.jeffque.ia.nim;

import br.com.jeffque.ia.AcaoInvalidaException;

public class Estado {
	public final boolean jogador;
	public final int tamanhoPilha;
	public final Estado estadoPai;
	public final int profundidade;
	
	public Estado(boolean jogador, int tamanhoPilha, Estado estadoPai) {
		this.jogador = jogador;
		this.tamanhoPilha = tamanhoPilha;
		this.estadoPai = estadoPai;
		
		if (estadoPai == null) {
			profundidade = 0;
		} else {
			profundidade = estadoPai.profundidade + 1;
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (other == this) {
			return true;
		} else if (!(other instanceof Estado)) {
			return false;
		}
		Estado otherMyClass = (Estado) other;
		
		return this.jogador == otherMyClass.jogador && this.tamanhoPilha == otherMyClass.tamanhoPilha;
	}
	
	@Override
	public int hashCode() {
		return (this.jogador? +1: -1) * this.tamanhoPilha;
	}
	
	public boolean fim() {
		return tamanhoPilha == 0;
	}
	
	public boolean vencedor(boolean candidato) {
		return fim() && candidato != jogador;
	}
	
	public Estado jogada(boolean jogador, int nRemovidas) throws AcaoInvalidaException {
		validarJogada(jogador, nRemovidas);
		return new Estado(!jogador, tamanhoPilha - nRemovidas, this);
	}

	private void validarJogada(boolean jogador, int nRemovidas) throws AcaoInvalidaException {
		if (this.jogador != jogador) {
			throw new AcaoInvalidaException("Jogador inválido");
		}
		
		if (nRemovidas <= 0 || nRemovidas > 3) {
			throw new AcaoInvalidaException("A quantidade de peças removidas deve ser de 1 a 3");
		}
		
		if (nRemovidas > tamanhoPilha) {
			throw new AcaoInvalidaException("A quantidade de peças removidas não pode ultrapassar o restante da pilha");
		}
	}
}
