package model;

public class TokenOperador extends Token {

	public TokenOperador(Lexema lexema) {
		this.setLexema(lexema);
		this.setSimbolo(lexema.getLexema());

	}

	@Override
	public String getToken() {
		return "< " + this.getSimbolo() + " , >";
	}

}
