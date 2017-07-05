package model;

public class Carrinho {

	private int posicaoX;
	private int posicaoY;
	private int velocidadeX;
	private int velocidadeY;
	
	public Carrinho(Ponto posicao) {
		this.posicaoX = posicao.getPosicaoX();
		this.posicaoY = posicao.getPosicaoY();
		this.velocidadeX = 0;
		this.velocidadeY = 0;
	}

	public int getPosicaoX() {
		return posicaoX;
	}

	public void setPosicaoX(int posicaoX) {
		this.posicaoX = posicaoX;
	}

	public int getPosicaoY() {
		return posicaoY;
	}

	public void setPosicaoY(int posicaoY) {
		this.posicaoY = posicaoY;
	}

	public int getVelocidadeX() {
		return velocidadeX;
	}

	public void setVelocidadeX(int velocidadeX) {
		this.velocidadeX = velocidadeX;
	}

	public int getVelocidadeY() {
		return velocidadeY;
	}

	public void setVelocidadeY(int velocidadeY) {
		this.velocidadeY = velocidadeY;
	}
}
