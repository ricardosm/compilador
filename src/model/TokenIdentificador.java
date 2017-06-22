package model;

public class TokenIdentificador extends Token {
	
	public TokenIdentificador(Lexema lexema) {
		this.setLexema(lexema);
		this.setSimbolo("id");
		this.setValor(lexema.getLexema());
	}
	
	@Override
	public String getToken () {
		return "< " + this.getSimbolo() + " , " + this.getValor() + " >";
	}

}
