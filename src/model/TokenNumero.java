package model;

public class TokenNumero extends Token {

	public TokenNumero(Lexema lexema) {
		this.setLexema(lexema);
		this.setSimbolo("number");
		this.setValor(lexema.getLexema());
	}

	@Override
	public String getToken() {
		return "< " + this.getSimbolo() + " , " + this.getValor() + " >";
	}
}
