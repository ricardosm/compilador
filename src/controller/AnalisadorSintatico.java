package controller;

import java.util.ArrayList;

import model.Erro;
import model.ErroSintatico;
import model.Lexema;
import model.Token;
import model.TokenSeparador;

public class AnalisadorSintatico {
	private static final String regexNumeros = "\\d+\\.\\d+|\\d+";
	private static final String regexIndentificadores = "[a-zA-Z][\\da-zA-Z_]*";
	private int indice;
	private ArrayList<Erro> erros;

	public AnalisadorSintatico() {
		this.indice = 0;
		erros = new ArrayList<>();
	}
	
	public ArrayList<Erro> getErros() {
		return erros;
	}

	public void setErros(ArrayList<Erro> erros) {
		this.erros = erros;
	}
	
	public void imprimeErrosSintaticos() {
		for(Erro e : erros) {
			System.out.println(e.getMensagemErro());;
		}
	}

	public void E(ArrayList<Token> tokens) {
		T(tokens);
		Elinha(tokens);
	}

	public void T(ArrayList<Token> tokens) {
		F(tokens);
		Tlinha(tokens);
	}

	public void F(ArrayList<Token> tokens) {
		if(tokens.get(indice).getStringLexema().matches(regexNumeros) 
		 || tokens.get(indice).getStringLexema().matches(regexIndentificadores)) {
			this.indice++;
		} else if(tokens.get(indice).getStringLexema().equals("(")) {
			this.indice++;
			E(tokens);
			if(tokens.get(indice).getStringLexema().equals(")")) {
				this.indice++;
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErrosSintaticos();
				System.exit(1);
			}
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErrosSintaticos();
			System.exit(1);
		}
	}

	public void Elinha(ArrayList<Token> tokens) {
		if (tokens.get(indice).getStringLexema().equals("+")) {
			this.indice++;
			T(tokens);
			Elinha(tokens);
		} else if (tokens.get(indice).getStringLexema().equals("$") || tokens.get(indice).getStringLexema().equals(")")
				|| tokens.get(indice).getStringLexema().equals(";") || tokens.get(indice).getStringLexema().equals("{")) {
			return;
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErrosSintaticos();
			System.exit(1);
		}
	}

	public void Tlinha(ArrayList<Token> tokens) {
		if (tokens.get(indice).getStringLexema().equals("*")) {
			this.indice++;
			F(tokens);
			Tlinha(tokens);
		} else if (tokens.get(indice).getStringLexema().equals("$") || tokens.get(indice).getStringLexema().equals("+")
				|| tokens.get(indice).getStringLexema().equals(")") || tokens.get(indice).getStringLexema().equals("{")
				|| tokens.get(indice).getStringLexema().equals(";")) {
			return;
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErrosSintaticos();
			System.exit(1);
		}
	}

	public boolean expressao(ArrayList<Token> tokens) {
		E(tokens);
		return true;
	}

	public void valor_atribuido(ArrayList<Token> tokens) {
		if (expressao(tokens)) {
			return;
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErrosSintaticos();
			System.exit(1);
		}
	}

	public void atribuicao(ArrayList<Token> tokens) {
		if (tokens.get(indice).getStringLexema().matches(regexIndentificadores)) {
			this.indice++;
			if (tokens.get(indice).getStringLexema().equals("=")) {
				this.indice++;
				valor_atribuido(tokens);
				if (tokens.get(indice).getStringLexema().equals(";")) {
					this.indice++;
				} else {
					this.indice--;
					erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
					this.imprimeErrosSintaticos();
					System.exit(1);
				}
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErrosSintaticos();
				System.exit(1);
			}

		}
	}

	public void declaracao_multipla(ArrayList<Token> tokens) {
		this.indice++;
		if (tokens.get(indice).getStringLexema().matches(regexIndentificadores)) {
			this.indice++;
			if (tokens.get(indice).getStringLexema().equals(";")) {
				return;
			} else if (tokens.get(indice).getStringLexema().equals(",")) {
				declaracao_multipla(tokens);
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErrosSintaticos();
				System.exit(1);
			}
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErrosSintaticos();
			System.exit(1);
		}
	}

	public void declaracao(ArrayList<Token> tokens) {
		this.indice++;
		if (tokens.get(indice).getStringLexema().matches(regexIndentificadores)) {
			this.indice++;
			if (tokens.get(indice).getStringLexema().equals(",")) {
				declaracao_multipla(tokens);
			} else if (tokens.get(indice).getStringLexema().equals(";")) {
				this.indice++;
			} else if (tokens.get(indice).getStringLexema().equals("=")) {
				this.indice--;
				atribuicao(tokens);
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErrosSintaticos();
				System.exit(1);
			}
		} else {	
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErrosSintaticos();
			System.exit(1);
		}
	}

	public void bloco(ArrayList<Token> tokens) {
		while(!tokens.get(indice).getStringLexema().equals("}")) {
			if(tokens.get(indice).getStringLexema().equals("int") || tokens.get(indice).getStringLexema().equals("float") 
					|| tokens.get(indice).getStringLexema().equals("char")) {
				declaracao(tokens);
			} else if(tokens.get(indice).getStringLexema().equals("while")) {
				repeticao(tokens);
			} else if(tokens.get(indice).getStringLexema().equals("if")) {
				condicao(tokens);
			} else if(tokens.get(indice).getStringLexema().matches(regexIndentificadores)) {
				atribuicao(tokens);
			}
		}
	}

	public void repeticao(ArrayList<Token> tokens) {
		this.indice++;
		if(expressao(tokens)) {
			if(tokens.get(indice).getStringLexema().equals("{")) {
				this.indice++;
				bloco(tokens);
				if(tokens.get(indice).getStringLexema().equals("}")) {
					this.indice++;
				} else {
					erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
					this.imprimeErrosSintaticos();
					System.exit(1);
				}
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErrosSintaticos();
				System.exit(1);
			}
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErrosSintaticos();
			System.exit(1);
		}
	}
	
	public void condicao(ArrayList<Token> tokens) {
		this.indice++;
		if(expressao(tokens)) {
			if(tokens.get(indice).getStringLexema().equals("{")) {
				this.indice++;
				bloco(tokens);
				if(tokens.get(indice).getStringLexema().equals("}")) {
					this.indice++;
				} else {
					erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
					this.imprimeErrosSintaticos();
					System.exit(1);
				}
				if(tokens.get(indice).getStringLexema().equals("else")) {
					this.indice++;
					if(tokens.get(indice).getStringLexema().equals("{")) {
						this.indice++;
						bloco(tokens);
						if(tokens.get(indice).getStringLexema().equals("}")) {
							this.indice++;
						} else {
							erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
							this.imprimeErrosSintaticos();
							System.exit(1);
						}
					} else {
						erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
						this.imprimeErrosSintaticos();
						System.exit(1);
					}
				} 
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErrosSintaticos();
				System.exit(1);
			}
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErrosSintaticos();
			System.exit(1);
		}
	}
                
	public void programa(ArrayList<Token> tokens) {
		tokens.add(new TokenSeparador(new Lexema("$", 0, 0)));
		while(this.indice < tokens.size()) {
			if(tokens.get(indice).getStringLexema().equals("$")) {
				this.indice++;
				System.out.println("Analise sintática concluída.");
				return;
			} else if(tokens.get(indice).getStringLexema().equals("int") || 
					tokens.get(indice).getStringLexema().equals("float") || 
					tokens.get(indice).getStringLexema().equals("char")) {
				declaracao(tokens);
			} else if(tokens.get(indice).getStringLexema().equals("while")) {
				repeticao(tokens);
			} else if(tokens.get(indice).getStringLexema().equals("if")) {
				condicao(tokens);
			} else if(tokens.get(indice).getStringLexema().matches(regexIndentificadores)) {
				atribuicao(tokens);
			} else {
				this.indice++;
			}
		}
	}

}
