package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import controller.util.TrackParser;
import model.Caminho;
import model.Carrinho;
import model.Track;

public class Main {

	private static Scanner console;	

	public static void main(String[] args) {

		// Pergunta ao usu�rio qual a pista escolhida dentre as op��es
		String pista = getEscolhaPista();

		//monta o objeto da pista
		Track track = TrackParser.parse(pista);

		//cria uma lista de X caminhos aleat�rios
		List<Caminho> caminhos = new ArrayList<Caminho>();
		for (int i = 0; i < 1000000; i++) {
			if ((i + 1) % 1000 == 0)
				System.out.println("Fazendo caminho " + (i + 1));

			Caminho caminho = NewPathRandomizer.randomizarCaminho(new Carrinho(track.getPontoInicial()), track);
			if (caminho.isPercursoCompleto()) {
				System.out.println("Caminho achado!");
				caminhos.add(caminho);
			}
		}
		
		// Compara caminhos at� pegar o com menor n�mero de pontos
		Caminho melhorCaminho = null;
		for (Caminho caminho : caminhos)
			if (caminho.isPercursoCompleto())
				melhorCaminho = melhorCaminho == null || caminho.getPontos().size() < melhorCaminho.getPontos().size() ? caminho : melhorCaminho;
		
		// Desenha melhor caminho
		desenhaCaminho(track, melhorCaminho);
	}

	private static void desenhaCaminho(Track track, Caminho melhorCaminho) {
		try {
			PathDrawer.mapRender(melhorCaminho, track);
		} catch (IOException e) {
			System.out.println("Imagem da pista n�o encontrada em resources!\n " + e.getMessage());
		}
	}

	private static String getEscolhaPista() {
		String trackChoice = "Entre com a pista desejada: \nOp��es - be, br, hu, it, jp, uk";
		System.out.println(trackChoice);

		boolean pistaValida = false;

		String pista = "";

		// Valida��o por regex
		// Caso a entrada do usu�rio n�o atenda ao pattern, lan�ar� uma exce��o e a escolha dever� ser refeita
		while (!pistaValida) {
			try {
				console = new Scanner(System.in);

				pista = console.next("(?i)^be$|^br$|^hu$|^it$|^jp$|^uk$");
				pistaValida = true;
			} catch (InputMismatchException e) {
				System.out.println("Pista Inv�lida! " + trackChoice);
			}
		}
		return pista;
	}
}