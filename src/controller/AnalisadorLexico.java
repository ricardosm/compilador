package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import model.Erro;
import model.ErroLexico;
import model.ExpressoesRegulares;
import model.Lexema;
import model.Token;
import model.TokenIdentificador;
import model.TokenLiteral;
import model.TokenNumero;
import model.TokenOperador;
import model.TokenPalavraReservada;
import model.TokenSeparador;

public class AnalisadorLexico {		
	private final ArrayList<Token> tokens = new ArrayList<>();
	private final ArrayList<Erro> erros = new ArrayList<>();
	
	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public ArrayList<Erro> getErros() {
		return erros;
	}
	
	public void scanear(BufferedReader br) throws IOException {
		String linha;
		String lexema = "";
		char c;
		
		linha = br.readLine();
		for(int i = 0; linha != null; linha = br.readLine(), i++) {
			//linha = arquivo.getArquivo().get(i);
			for(int j = 0; j < linha.length(); ++j) {
				c = linha.charAt(j);
				
				/* Comentários de unica linha */
				if(c == '/') {
					++j;
					c = linha.charAt(j);
					if(c == '/') {
						if(lexema.length() > 0) {
							gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length() + 1));
							lexema = "";
						}						
						break;
					} else if(c == '*') {
						if(lexema.length() > 0) {
							gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length() + 1));
							lexema = "";
						}						
						break;					
					} else {
						--j;
						c = linha.charAt(j);
					}
				}
				
				/* Literal em aspas simples */
				if(c == '\'') {
					lexema = lexema + c;
					++j;
					for(; j < linha.length() && linha.charAt(j) != '\''; ++j) {
						c = linha.charAt(j);
						lexema = lexema + c;
					}
					c = linha.charAt(j);
					lexema = lexema + c;
					continue;
				}
				
				/* Literal em aspas duplas */
				if(c == '"') {
					lexema = lexema + c;
					++j;
					for(; j < linha.length() && linha.charAt(j) != '"'; ++j) {
						c = linha.charAt(j);
						lexema = lexema + c;
					}
					c = linha.charAt(j);
					lexema = lexema + c;
					continue;
				}
				
				/* Espaço, Tab e Qubera de Linha */				
				if((int)c == 32 || (int)c == 9 || (int)c == 10) {
					if(lexema.length() > 0) {
						gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length()));
						lexema = "";
					}	
					continue;
				}
				
				/* Separadores */
				if(c == ';' || c == '[' || c == ']' || c == '(' || c == ')' || c == '{' || c == '}' || c == ',') {
					if(lexema.length() > 0) {
						gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length()));
						lexema = "";
					}
					if(c == ';' || c == '[' || c == ']' || c == '(' || c == ')' || c == '{' || c == '}' || c == ',') {
						gerarTokenValido(new Lexema("" + c, i + 1, (j+1) - lexema.length()));
					}
					continue;
				}
				
				/* Operadores Lógicos  e atribuição */
				if(c == '&' || c == '|' || c == '<' || c == '>' || c == '=' || c == '!') {
					if(lexema.length() > 0) {
						gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length()));
						lexema = "";
					}
					lexema = lexema + c;
					++j;
					c = linha.charAt(j);
					if(c == '+') {
						--j;
						continue;
					}
					if(c == '&' || c == '|' || c == '=') {
						lexema = lexema + c;
						gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length()));
						lexema = "";
					} else {
						gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length()));
						lexema = "";
						if((int)c != 32 && (int)c != 9 && (int)c != 10) {
							lexema = lexema + c;
						}
					}
					continue;
				}
				
				/* Aritméticos e pós-incremento */
				if(c == '^' || c == '+' || c == '-' || c == '/' || c == '*') {
					if(lexema.length() > 0) {
						gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length()));
						lexema = "";
					}
					lexema = lexema + c;
					++j;
					c = linha.charAt(j);
					if(lexema.charAt(0) == '+' && c == '+') {
						lexema = lexema + c;
						gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length()));
						lexema = "";
					} else {
						gerarTokenValido(new Lexema(lexema, i + 1, (j+1) - lexema.length()));
						lexema = "";
						if((int)c != 32 && (int)c != 9 && (int)c != 10) {
							lexema = lexema + c;
						}
					}
					continue;
				}
				
				lexema = lexema + c;
			}
		}		
	}
	
	public void gerarTokenValido(Lexema l) {
		if(l.getLexema().matches(ExpressoesRegulares.regexPalavrasReservadas)) {
			tokens.add(new TokenPalavraReservada(l));
		} else if(l.getLexema().matches(ExpressoesRegulares.regexIndentificadores)) {
			tokens.add(new TokenIdentificador(l));		
		} else if(l.getLexema().matches(ExpressoesRegulares.regexStrings)) {
			tokens.add(new TokenLiteral(l));			
		} else if(l.getLexema().matches(ExpressoesRegulares.regexNumeros)) {
			tokens.add(new TokenNumero(l));				
		} else if(l.getLexema().matches(ExpressoesRegulares.regexOperadoresAritmeticos)) {
			tokens.add(new TokenOperador(l));								
		} else if(l.getLexema().matches(ExpressoesRegulares.regexOperadorAtribuicao)) {
			tokens.add(new TokenOperador(l));			
		} else if(l.getLexema().matches(ExpressoesRegulares.regexOperadorPosIncremento)) {
			tokens.add(new TokenOperador(l));			
		} else if(l.getLexema().matches(ExpressoesRegulares.regexOperadoresLogicos)) {
			tokens.add(new TokenOperador(l));				
		} else if(l.getLexema().matches(ExpressoesRegulares.reexSeparadores)) {		
			tokens.add(new TokenSeparador(l));
		} else {
			erros.add(new ErroLexico(l.getLinha(), l.getColuna()));
		}
	}
}
