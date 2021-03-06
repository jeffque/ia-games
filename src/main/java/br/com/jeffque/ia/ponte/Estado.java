package br.com.jeffque.ia.ponte;

import java.util.ArrayList;

import br.com.jeffque.ia.AcaoInvalidaException;

public class Estado {
	public Estado(ArrayList<Integer> pessoasEsquerdo, ArrayList<Integer> pessoasDireito, int custoTotal, Estado estadoPai, boolean lanternaEsquerda) {
		this.pessoasEsquerdo = pessoasEsquerdo;
		this.pessoasDireito = pessoasDireito;
		
		this.custoTotal = custoTotal;
		this.estadoPai = estadoPai;
		this.lanternaEsquerda = lanternaEsquerda;
	}
	
	final ArrayList<Integer> pessoasEsquerdo;
	final ArrayList<Integer> pessoasDireito;
	final int custoTotal;
	final Estado estadoPai;
	final boolean lanternaEsquerda;
	
	public Estado move2personLR(int idx1, int idx2) throws AcaoInvalidaException {
		checkPosicaoLanterna(true);
		
		int custo1;
		int custo2;
		
		custo1 = pessoasEsquerdo.get(idx1);
		custo2 = pessoasEsquerdo.get(idx2);
		
		EstadoBuilder builder = EstadoBuilder.createEstadoFilho(this);
		
		for (int i = 0; i < pessoasEsquerdo.size(); i++) {
			if (i != idx1 && i != idx2) {
				builder.pessoasEsquerdo.add(pessoasEsquerdo.get(i));
			}
		}
		
		builder.pessoasDireito.add(custo1);
		builder.pessoasDireito.add(custo2);
		
		builder.custoTotal += Math.max(custo1, custo2);
		
		return builder.build();
	}
	
	public Estado move1personLR(int idx1) throws AcaoInvalidaException {
		checkPosicaoLanterna(true);
		
		int custo1;
		custo1 = pessoasEsquerdo.get(idx1);
		EstadoBuilder builder = EstadoBuilder.createEstadoFilho(this);
		
		for (int i = 0; i < pessoasEsquerdo.size(); i++) {
			if (i != idx1) {
				builder.pessoasEsquerdo.add(pessoasEsquerdo.get(i));
			}
		}
		
		builder.pessoasDireito.add(custo1);
		builder.custoTotal += custo1;
		
		return builder.build();
	}
	
	public Estado move1personRL(int idx1) throws AcaoInvalidaException {
		checkPosicaoLanterna(false);
		
		int custo1;
		custo1 = pessoasDireito.get(idx1);
		EstadoBuilder builder = EstadoBuilder.createEstadoFilho(this);

		for (int i = 0; i < pessoasDireito.size(); i++) {
			if (i != idx1) {
				builder.pessoasDireito.add(pessoasDireito.get(i));
			}
		}
		builder.pessoasEsquerdo.add(custo1);
		builder.custoTotal += custo1;
		
		return builder.build();
	}
	
	private void checkPosicaoLanterna(boolean lanternaEsquerda) throws AcaoInvalidaException {
		if (lanternaEsquerda != this.lanternaEsquerda) {
			throw new AcaoInvalidaException(
					"Lanterna deveria estar na " +
						(lanternaEsquerda? "esquerda": "direita") + ", mas ela estava na " +
						(this.lanternaEsquerda? "esquerda": "direita")
					);
		}
	}

	public boolean fim() {
		return pessoasEsquerdo.isEmpty();
	}
	
	@Override
	public String toString() {
		return toString("");
	}

	public int getPessoaEsquerdo(int i) {
		return pessoasEsquerdo.get(i);
	}
	
	public int getPessoaDireito(int i) {
		return pessoasDireito.get(i);
	}

	public String toString(String prepend) {
		return prepend + "Esquerdo: " + pessoasEsquerdo + (lanternaEsquerda? "*": "") + "\n" + prepend + "Direito: " + pessoasDireito + (lanternaEsquerda? "": "*") + "\n" + prepend + "Custo: " + custoTotal;
	}

	public Estado getEstadoPai() {
		return estadoPai;
	}

	public boolean lanternaLadoEsquerdo() {
		return lanternaEsquerda;
	}
	
	public int getCustoTotal() {
		return custoTotal;
	}
}
