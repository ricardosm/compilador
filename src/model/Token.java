package model;

public abstract class Token {
	private String token;
	private String simbolo;
	private String valor;
	private Lexema lexema;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Lexema getLexema() {
		return lexema;
	}

	public void setLexema(Lexema lexema) {
		this.lexema = lexema;
	}

}
