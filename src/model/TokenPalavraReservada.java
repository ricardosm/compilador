package model;

public class TokenPalavraReservada extends Token {

	public TokenPalavraReservada(Lexema lexema) {
		this.setLexema(lexema);
		this.setSimbolo(lexema.getLexema());

	}

	@Override
	public String getToken() {
		return "< " + this.getSimbolo() + " , >";
	}

}
