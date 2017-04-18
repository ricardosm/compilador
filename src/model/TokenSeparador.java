package model;

public class TokenSeparador extends Token {

	public TokenSeparador(Lexema lexema) {
		this.setLexema(lexema);
		this.setSimbolo(lexema.getLexema());

	}

	@Override
	public String getToken() {
		return "< " + this.getSimbolo() + " , >";
	}

}
