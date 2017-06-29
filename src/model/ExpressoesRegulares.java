package model;

public interface ExpressoesRegulares {
	public static final String regexStrings = "^\".+\"$|^\'.+\'$";
	public static final String regexNumeros = "\\d+\\.\\d+|\\d+";
	public static final String regexIndentificadores = "[a-zA-Z][\\da-zA-Z_]*";
	public static final String regexPalavrasReservadas = "print|char|void|int|float|double|if|else|for|while|return|continue|break|read";
	public static final String regexOperadoresAritmeticos = "\\^|\\+|-|/|\\*";
	public static final String regexOperadorAtribuicao = "=";
	public static final String regexOperadorPosIncremento = "\\+\\+";
	public static final String regexOperadoresLogicos = "&&|\\|\\||<|>|<=|>=|==|!=";
	public static final String reexSeparadores = ";|\\[|\\]|\\(|\\)|\\{|\\}|,";
	public static final String regexFloat = "\\d+\\.\\d+";
	public static final String regexInt = "\\d+";

}
