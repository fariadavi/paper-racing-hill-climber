package model;

import java.util.ArrayList;
import java.util.List;

public class Caminho {

	private List<Ponto> pontos;
	private boolean percursoCompleto;

	public Caminho() {
		this.pontos = new ArrayList<Ponto>();
		this.percursoCompleto = false;
	}

	public boolean isPercursoCompleto() {
		return percursoCompleto;
	}

	public void setPercursoCompleto(boolean percursoCompleto) {
		this.percursoCompleto = percursoCompleto;
	}

	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> pontos) {
		this.pontos = pontos;
	}
	
	public void addPontoCaminho(Ponto ponto) {
		this.pontos.add(ponto);
	}
}
