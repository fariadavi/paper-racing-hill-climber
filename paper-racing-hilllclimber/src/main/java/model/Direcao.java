package model;

import java.util.HashMap;
import java.util.Map;

public enum Direcao {
	NOROESTE(1, -1, -1), 	NORTE	(2, 0, -1), NORDESTE(3, 1, -1), 
	OESTE	(4, -1, 0), 	CENTRO	(5, 0, 0), 	LESTE	(6, 1, 0), 
	SUDOESTE(7, -1, 1), 	SUL		(8, 0, 1), 	SUDESTE	(9, 1, 1);
	
	private final int escolha;
	private final int aceleracaoX;
	private final int aceleracaoY;
	
	private static Map<Integer, Direcao> map = new HashMap<Integer, Direcao>();
    static {
        for (Direcao dir : Direcao.values()) {
            map.put(dir.escolha, dir);
        }
    }
    
    public static Direcao valueOf(int escolha) {
        return map.get(escolha);
    }
	    
	private Direcao(final int escolha, final int aceleracaoX, final int aceleracaoY) {
		this.escolha = escolha;
		this.aceleracaoX = aceleracaoX;
		this.aceleracaoY = aceleracaoY;
	}

	public int getAceleracaoX() {
		return aceleracaoX;
	}

	public int getAceleracaoY() {
		return aceleracaoY;
	}
}
