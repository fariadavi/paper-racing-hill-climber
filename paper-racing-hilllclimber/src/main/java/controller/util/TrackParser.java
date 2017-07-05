package controller.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Ponto;
import model.TipoPonto;
import model.Track;

public class TrackParser {
	
	private static final String XML_TAG_FILE = "file";
	private static final String XML_TAG_ROWS = "rows";
	private static final String XML_TAG_LINE = "line";
	private static final String XML_TAG_PASSES = "passes";
	private static final String XML_TAG_COLUMN = "column";
	private static final String XML_TAG_NUMCOLUMNS = "columns";
	private static final String XML_TAG_BORDERWIDTH = "borderWidth";
	private static final String XML_TAG_BORDERHEIGHT = "borderHeight";
	private static final String XML_TAG_FIRST_ROW_HEIGHT = "firstRowHeight";
	private static final String XML_TAG_FIRSTCOLUMNWIDTH = "firstColumnWidth";

	public static Track parse(String trackName) {

		final String trackPath = "src/main/resources/data/tracks/" + trackName.toLowerCase() + "/track.xml";

		try {
			// Lê XML
			Document doc = getDocumentFromXML(trackPath);

			// Monta objeto da pista a partir do document passado
			Track track = assembleTrack(doc);

			Ponto[][] matrizPontos = track.getMatrizPontos();

			// Preenche a matriz de Ponto da pista mudando os pontos que forem parte da pista
			NodeList lineList = doc.getElementsByTagName(XML_TAG_LINE);
			preencheMatrizPontos(lineList, matrizPontos);

			// Preenche lista de passes (Ponto) da pista
			NodeList passesList = doc.getElementsByTagName(XML_TAG_PASSES).item(0).getChildNodes();
			preencheListaCheckpoints(passesList, track, matrizPontos);

			// Atualiza matriz de acordo com as mudanças
			track.setMatrizPontos(matrizPontos);

			return track;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Transforma XML em Document
	 * 
	 * @param trackPath Caminho do XML a ser transformado
	 * @return Document com as informações do XML
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private static Document getDocumentFromXML(String trackPath) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = dbf.newDocumentBuilder().parse(new File(trackPath));

		doc.getDocumentElement().normalize();
		return doc;
	}
	
	/**
	 * Monta Track com as informações contidas no Document de acordo com as tags.
	 * 
	 * @param doc Document contendo as informações necessárias
	 * @return Track da pista escolhida pelo usuário
	 */
	private static Track assembleTrack(Document doc) {
		String file = doc.getElementsByTagName(XML_TAG_FILE).item(0).getTextContent();
		int rows = Integer.parseInt(doc.getElementsByTagName(XML_TAG_ROWS).item(0).getTextContent());
		int columns = Integer.parseInt(doc.getElementsByTagName(XML_TAG_NUMCOLUMNS).item(0).getTextContent());
		int borderWidth = Integer.parseInt(doc.getElementsByTagName(XML_TAG_BORDERWIDTH).item(0).getTextContent());
		int borderHeight = Integer.parseInt(doc.getElementsByTagName(XML_TAG_BORDERHEIGHT).item(0).getTextContent());
		int firstColumnWidth = Integer.parseInt(doc.getElementsByTagName(XML_TAG_FIRSTCOLUMNWIDTH).item(0).getTextContent());
		int firstColumnHeight = Integer.parseInt(doc.getElementsByTagName(XML_TAG_FIRST_ROW_HEIGHT).item(0).getTextContent());

		return new Track(file, rows, columns, borderWidth, borderHeight, firstColumnWidth, firstColumnHeight);
	}

	/**
	 * Itera os nós da pista a partir da lista recebida e muda o TipoPonto dos Pontos da matriz para ACESSÍVEL.
	 * 
	 * @param lineList Lista de linhas da pista 
	 * @param matrizPontos Matriz de todos os pontos da pista
	 */
	private static void preencheMatrizPontos(NodeList lineList, Ponto[][] matrizPontos) {
		for (int i = 0; i < lineList.getLength(); i++) {

			Node lineNode = lineList.item(i);

			if (lineNode.getNodeType() == Node.ELEMENT_NODE) {

				Element lineElement = (Element) lineNode;

				int row = Integer.parseInt(lineElement.getAttribute("y"));
				NodeList lineColumnList = lineElement.getElementsByTagName(XML_TAG_COLUMN);

				for (int j = 0; j < lineColumnList.getLength(); j++) {

					Node lineColumnNode = lineColumnList.item(j);

					if (lineColumnNode.getNodeType() == Node.ELEMENT_NODE) {
						int column = Integer.parseInt(lineColumnNode.getTextContent());

						matrizPontos[column][row].setTipo(TipoPonto.ACESSIVEL);

					}
				}
			}
		}
	}

	/**
	 * Itera os nós da pista a partir da lista recebida e muda o TipoPonto dos Pontos da matriz para ACESSÍVEL
	 * 
	 * @param passesList Lista de checkpoints da pista
	 * @param track Pista escolhida
	 * @param matrizPontos Matriz de todos os pontos da pista
	 */
	private static void preencheListaCheckpoints(NodeList passesList, Track track, Ponto[][] matrizPontos) {
		List<Ponto> passes = track.getPasses();

		Ponto pontoInicial = null;
		for (int i = 0; i < passesList.getLength(); i++) {

			Node passNode = passesList.item(i);

			if (passNode.getNodeType() == Node.ELEMENT_NODE) {

				Element passElement = (Element) passNode;

				int row = Integer.parseInt(passElement.getAttribute("y"));
				int column = Integer.parseInt(passElement.getAttribute("x"));

				Ponto ponto = matrizPontos[column][row];
				ponto.setTipo(TipoPonto.PASS);

				// guarda checkpoint inicial
				if (passes.size() == 0)
					pontoInicial = ponto;

				passes.add(ponto);
			}
		}

		// adiciona o checkpoint inicial como checkpoint final também
		passes.add(pontoInicial);

		track.setPasses(passes);
	}
}
