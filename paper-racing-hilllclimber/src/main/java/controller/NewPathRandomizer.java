package controller;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import model.Caminho;
import model.Carrinho;
import model.Direcao;
import model.Ponto;
import model.TipoPonto;
import model.Track;

public class NewPathRandomizer {

	/**
	 * Randomiza uma sequencia de acões, gerando um percurso válido da pista.
	 * 
	 * @param carrinho Objeto carrinho a percorrer a pista
	 * @param track Pista em que o percurso será traçado
	 **/
	public static Caminho randomizarCaminho(Carrinho carrinho, Track track) {
		Caminho caminho = new Caminho();
		Ponto[][] matrizPontos = track.getMatrizPontos();
		List<Ponto> passes = track.getPasses();
		
		// variavel de controle do numero de repetições. Zerado quando há sucesso
		// impede o programa ficar preso em máximos locais
		int i = 0;
		// variavel de controle de numero checkpoint alvo do carrinho		
		int index = 1;
		
		// enquanto o indexador não alcançar o tamanho do vetor de checkpoints, o ultimo checkpoint não foi verificado,
		// Logo o percurso não está completo, roda-se um novo turno de escolha
		while (!caminho.isPercursoCompleto() && i++ < 100) {
			// segura o próximo checkpoint alvo do carrinho
			Ponto nextPass = passes.get(index);

			// randomiza direcao por um numero de 1 a 9
			int randomNum = ThreadLocalRandom.current().nextInt(1, 10);
			Direcao direcao = Direcao.valueOf(randomNum);

			// nova posicão do carrinho após o movimento
			int novaVelocidadeX = carrinho.getVelocidadeX() + direcao.getAceleracaoX();
			int novaVelocidadeY = carrinho.getVelocidadeY() + direcao.getAceleracaoY();
			int novoPX = carrinho.getPosicaoX() + novaVelocidadeX;
			int novoPY = carrinho.getPosicaoY() + novaVelocidadeY;

			// limita movimento do carrinho às margens do mapa
			if (novoPX < 0 || novoPY < 0 || novoPY >= track.getRows() || novoPX >= track.getColumns())
				continue;

			Ponto novoPonto = matrizPontos[novoPX][novoPY];

			// limita movimento do carrinho às margens da pista
			if (novoPonto.getTipo() == TipoPonto.INACESSIVEL)
				continue;

			// verifica se o passo dado diminuiu a distância entre o carrinho e o próximo checkpoint
			Ponto pontoCarrinho = matrizPontos[carrinho.getPosicaoX()][carrinho.getPosicaoY()];
			double distanciaaAtual = getDistance(pontoCarrinho, nextPass);
			double distanciaPasso = getDistance(novoPonto, nextPass);
			
			if(distanciaPasso > distanciaaAtual)
				continue;
			
			// adiciona ponto válido no caminho
			caminho.addPontoCaminho(novoPonto);

			// update posicao e velocidade do carrinho
			updateCarrinho(carrinho, novaVelocidadeX, novaVelocidadeY, novoPX, novoPY);
			
			// zera iterador de repetições sem sucesso
			i = 0;
			
			// verifica se caminho passou por todos os checkpoints em ordem
			if(isCheckpointHit(nextPass, novoPonto))
//				caminho.setPercursoCompleto(true);
				index++;
			
			if(index >= passes.size())
				caminho.setPercursoCompleto(true);
		}

		return caminho;
	}

	/**
	 * Calcula a distância entre dois pontos usando o teorema de Pitágoras.
	 * 
	 * @param ponto1 Primeiro ponto a ser comparado
	 * @param ponto2 Segundo ponto a ser comparado
	 * @return Retorna a hipotenusa do triângulo  
	 */
	private static double getDistance(Ponto ponto1, Ponto ponto2) {
		int dx = ponto1.getPosicaoX() - ponto2.getPosicaoX();
		int dy = ponto1.getPosicaoY() - ponto2.getPosicaoY();
		return Math.sqrt((dx*dx)+(dy*dy));
	}

	/**
	 * Atualiza os atributos do carrinho.
	 * 
	 * @param carrinho Carrinho a ser atualizado
	 * @param novaVelocidadeX Nova velocidade do carrinho no eixo X
	 * @param novaVelocidadeY Nova velocidade do carrinho no eixo Y
	 * @param novoPX Nova posição do carrinho no eixo X
	 * @param novoPY Nova posição do carrinho no eixo Y
	 */
	private static void updateCarrinho(Carrinho carrinho, int novaVelocidadeX, int novaVelocidadeY, int novoPX, int novoPY) {
		carrinho.setPosicaoX(novoPX);
		carrinho.setPosicaoY(novoPY);
		carrinho.setVelocidadeX(novaVelocidadeX);
		carrinho.setVelocidadeY(novaVelocidadeY);
	}

	/**
	 * Verifica se ponto está no range do checkpoint (em até 2 casas de raio).
	 * 
	 * @param nextCheckpoint Ponto do checkpoint (alvo)
	 * @param ponto Ponto a ser comparado
	 * 
	 * @return Verdadeiro se o ponto estiver no range, caso contrário, falso
	 **/
	private static boolean isCheckpointHit(Ponto nextCheckpoint, Ponto ponto) {
		int passPX = nextCheckpoint.getPosicaoX();
		int passPY = nextCheckpoint.getPosicaoY();

		// compara a posicao X e Y do ponto com um raio de 2 pontos do checkpoint atual
		if (passPX - 2 < ponto.getPosicaoX() && ponto.getPosicaoX() < passPX + 2 && 
			passPY - 2 < ponto.getPosicaoY() && ponto.getPosicaoY() < passPY + 2)
			return true;
		return false;
	}
}
