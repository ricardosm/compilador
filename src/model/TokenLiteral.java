package model;

public class TokenLiteral extends Token {
	
	public TokenLiteral(Lexema lexema) {
		this.setLexema(lexema);
		this.setSimbolo("Literal");
		this.setValor(lexema.getLexema());
	}

	@Override
	public String getToken() {
		return "< " + this.getSimbolo() + " , " + this.getValor() + " >";
	}

}
