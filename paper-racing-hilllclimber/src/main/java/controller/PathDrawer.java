package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import controller.util.Resizer;
import model.Caminho;
import model.Ponto;
import model.Track;

public class PathDrawer {

	// Parâmetro (divisor) que controla a escala da imagem final
	private static final int ESCALA_IMAGENS = 2;

	/**
	 * Renderiza o mapa com o caminho por cima.
	 * 
	 * @param caminho Caminho a ser renderizado
	 * @param track Pista a ser renderizada
	 * @throws IOException
	 */
	public static void mapRender(Caminho caminho, Track track) throws IOException {
		
		// Montar largura e altura do mapa com
		// Borda x 2: bordas aparecem dos dois lados do mapa
		// (Número de colunas/linhas + 1 x 32): cada ponto terá 32 pixels de distância para o próximo (+1*32 de distância do primeiro ponto à margem) 
		int width = (track.getBorderWidth() * 2) + (track.getColumns() + 1) * 32 / ESCALA_IMAGENS;
		int height = (track.getBorderHeight() * 2) + (track.getRows() + 1) * 32 / ESCALA_IMAGENS;

		JFrame frame = buildFrame();

		final BufferedImage image = track.getTrackImg();
		
		// ajustar tamanho da imagem à escala
		BufferedImage resizedImage = Resizer.NEAREST_NEIGHBOR.resize(image, width, height);

		List<Ponto> pontos = caminho.getPontos();
		JPanel pane = createJPanel(pontos, track, resizedImage);

		// set tamanho do JPanel para o tamanho da imagem
		pane.setPreferredSize(new Dimension(width, height));
		pane.repaint();

		// adicionar JPanel ao JFrame
		frame.add(pane);
		frame.pack();
	}

	/**
	 * Criar JPanel com método paintComponent personalizado.
	 * 
	 * @param pontos Lista dos pontos do caminho a serem desenhados na tela
	 * @param track Pista na qual os pontos deverão ser desenhados
	 * @param resizedImage Imagem redimensionada da pista
	 * @return JPanel com método paintComponent personalizado.
	 */
	private static JPanel createJPanel(final List<Ponto> pontos, final Track track, final BufferedImage resizedImage) {
		return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.drawImage(resizedImage, 0, 0, this);

				g.setColor(Color.BLACK);

				for (int i = 0; i < pontos.size()-1; i++) {
					Ponto ponto = pontos.get(i);
					Ponto proximoPonto = pontos.get(i+1);
					g.drawLine(track.getBorderWidth() / ESCALA_IMAGENS + ponto.getPosicaoX() * 32 / ESCALA_IMAGENS,
							track.getBorderHeight() / ESCALA_IMAGENS + ponto.getPosicaoY() * 32 / ESCALA_IMAGENS,
							track.getBorderWidth() / ESCALA_IMAGENS + proximoPonto.getPosicaoX() * 32 / ESCALA_IMAGENS,
							track.getBorderHeight() / ESCALA_IMAGENS + proximoPonto.getPosicaoY() * 32 / ESCALA_IMAGENS);
//					g.fillRect(track.getBorderWidth() / ESCALA_IMAGENS + posicao.getPosicaoX() * 32 / ESCALA_IMAGENS,
//							track.getBorderHeight() / ESCALA_IMAGENS + posicao.getPosicaoY() * 32 / ESCALA_IMAGENS,
//							32 / ESCALA_IMAGENS, 32 / ESCALA_IMAGENS);
				}
				g.dispose();
			}
		};
	}

	/**
	 * Montar JFrame onde o mapa será exibido.
	 * 
	 * @return JFrame montado
	 */
	private static JFrame buildFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		return frame;
	}
}
