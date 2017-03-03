package br.com.jeffque.ia;

import br.com.jeffque.ia.nim.Agente;
import br.com.jeffque.ia.nim.Estado;

public class NimApp {

	public static void main(String[] args) {
		for (int i = 1; i <= 10000; i++) {
			Estado inicial = new Estado(true, i, null);
			Agente ag = new Agente(inicial);
			ag.menorCusto();
		}
	}

}
