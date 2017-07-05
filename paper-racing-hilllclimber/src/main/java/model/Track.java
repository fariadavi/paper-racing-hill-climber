package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Track {

	private String imgPath;
	private BufferedImage trackImg;
	private int rows;
	private int columns;
	private int borderWidth;
	private int borderHeight;
	private int firstColumnWidth;
	private int firstRowHeight;
	
	private List<Ponto> passes;
	private Ponto[][] matrizPontos;
	
	public Track(String imgPath, int rows, int columns, int borderWidth, int borderHeight, int firstColumnWidth, int firstRowHeight) {
		this.imgPath = "src/main/resources/" + imgPath;
		this.rows = rows;
		this.columns = columns;
		this.borderWidth = borderWidth;
		this.borderHeight = borderHeight;
		this.firstColumnWidth = firstColumnWidth;
		this.firstRowHeight = firstRowHeight;
		
		this.setPasses(new ArrayList<Ponto>());
		this.matrizPontos = new Ponto[columns][rows];
		iniciaMatriz();
	}

	/**
	 * Preenche a matriz da classe com objetos Ponto de tipo TipoPonto.INACESSIVEL
	 */
	private void iniciaMatriz() {
		for (int i = 0; i < this.columns; i++) {
			for (int j = 0; j < this.rows; j++) {
				matrizPontos[i][j] = new Ponto(i, j);
				matrizPontos[i][j].setTipo(TipoPonto.INACESSIVEL);
			}
		}
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public int getBorderHeight() {
		return borderHeight;
	}

	public void setBorderHeight(int borderHeight) {
		this.borderHeight = borderHeight;
	}

	public int getFirstColumnWidth() {
		return firstColumnWidth;
	}

	public void setFirstColumnWidth(int firstColumnWidth) {
		this.firstColumnWidth = firstColumnWidth;
	}

	public int getFirstRowHeight() {
		return firstRowHeight;
	}

	public void setFirstRowHeight(int firstRowHeight) {
		this.firstRowHeight = firstRowHeight;
	}

	public Ponto[][] getMatrizPontos() {
		return matrizPontos;
	}
	
	public void setMatrizPontos(Ponto[][] matrizPontos) {
		this.matrizPontos = matrizPontos;
	}

	public List<Ponto> getPasses() {
		return passes;
	}

	public void setPasses(List<Ponto> passes) {
		this.passes = passes;
	}
	
	public Ponto getPontoInicial() {
		return passes.get(0);
	}

	public BufferedImage getTrackImg() {
		if(trackImg == null) {
			try {
				this.trackImg = ImageIO.read(new File(imgPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		return trackImg;
	}
}
