package model;

public enum TipoPonto {
	ACESSIVEL(true, false), INACESSIVEL(false, false), PASS(true, true);
	
	private boolean acessivel;
	private boolean pass;
	
	private TipoPonto(boolean acessivel, boolean pass) {
		this.setAcessivel(acessivel);
		this.setPass(pass);
	}

	public boolean isAcessivel() {
		return acessivel;
	}

	public void setAcessivel(boolean acessivel) {
		this.acessivel = acessivel;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}
}