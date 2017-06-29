package controller;

import java.util.ArrayList;

import model.Erro;
import model.ErroSemantico;
import model.ErroSintatico;
import model.ExpressoesRegulares;
import model.InstrucoesArquitetura;
import model.Lexema;
import model.Token;
import model.TokenIdentificador;
import model.TokenNumero;
import model.TokenSeparador;

public class AnalisadorSintatico {
	private String tipoDeclaracao;
	private int indice;
	private ArrayList<Erro> erros;
	private ArrayList<TokenIdentificador> variaveisDeclaradas;
	private ArrayList<Token> expressaoAtual;
	private GeradorDeCodigo gerador;
	
	public AnalisadorSintatico(GeradorDeCodigo gerador) {
		this.indice = 0;
		erros = new ArrayList<>();
		variaveisDeclaradas = new ArrayList<>();
		expressaoAtual = new ArrayList<>();
		this.gerador = gerador;
	}
	
	/*
	 * Getters e Setters
	 */
	public ArrayList<Erro> getErros() {
		return erros;
	}

	public void setErros(ArrayList<Erro> erros) {
		this.erros = erros;
	}
	
	/*
	 * Método para imprimir erros sintáticos e/ou semânticos
	 */
	public void imprimeErros() {
		for(Erro e : erros) {
			System.out.println(e.getMensagemErro());;
		}
	}
	
	/*
	 * Métodos referentes a Análise Sintática
	 */
	
	public void E(ArrayList<Token> tokens) {
		T(tokens);
		Elinha(tokens);
	}

	public void T(ArrayList<Token> tokens) {
		F(tokens);
		Tlinha(tokens);
	}

	public void F(ArrayList<Token> tokens) {
		if(tokens.get(indice).getStringLexema().matches(ExpressoesRegulares.regexNumeros) || tokens.get(indice).getStringLexema().matches(ExpressoesRegulares.regexIndentificadores)) {
			// Adiciona indetificador ou número na expressao atual
			expressaoAtual.add(tokens.get(indice));
			this.indice++; 
		} else if(tokens.get(indice).getStringLexema().equals("(")) {
			this.indice++;
			E(tokens);
			if(tokens.get(indice).getStringLexema().equals(")")) {
				this.indice++;
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErros();
				System.exit(1);
			}
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErros();
			System.exit(1);
		}
	}

	public void Elinha(ArrayList<Token> tokens) {
		if (tokens.get(indice).getStringLexema().equals("+")) {
			this.indice++;
			T(tokens);
			// Gera Instrução ADD
			gerador.gerarCodigo(InstrucoesArquitetura.ADD,  getVariavelDecladrada(tokens.get(indice - 3).getStringLexema()),  getVariavelDecladrada(tokens.get(indice - 1).getStringLexema()),  getVariavelDecladrada(tokens.get(indice - 5).getStringLexema()));
			Elinha(tokens);
		} else if(tokens.get(indice).getStringLexema().equals("-")) {
			this.indice++;
			T(tokens);
			// Gera Instrução SUB
			gerador.gerarCodigo(InstrucoesArquitetura.SUB,  getVariavelDecladrada(tokens.get(indice - 3).getStringLexema()),  getVariavelDecladrada(tokens.get(indice - 1).getStringLexema()),  getVariavelDecladrada(tokens.get(indice - 5).getStringLexema()));
			Elinha(tokens);
		} else if (tokens.get(indice).getStringLexema().equals("$") || tokens.get(indice).getStringLexema().equals(")")
				|| tokens.get(indice).getStringLexema().equals(";") || tokens.get(indice).getStringLexema().equals("{")) {
			return;
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErros();
			System.exit(1);
		}
	}

	public void Tlinha(ArrayList<Token> tokens) {
		if (tokens.get(indice).getStringLexema().equals("*")) {
			this.indice++;
			F(tokens);
			// Gera Instrução MULT
			gerador.gerarCodigo(
					InstrucoesArquitetura.MULT,  getVariavelDecladrada(tokens.get(indice - 3).getStringLexema()),  
					getVariavelDecladrada(tokens.get(indice - 1).getStringLexema()),  
					getVariavelDecladrada(tokens.get(indice - 5).getStringLexema())
			);
			Tlinha(tokens);
		} else if (tokens.get(indice).getStringLexema().equals("/")) {
			this.indice++;
			F(tokens);			
			// Gera Instrução DIV
			gerador.gerarCodigo(
					InstrucoesArquitetura.DIV,  getVariavelDecladrada(tokens.get(indice - 3).getStringLexema()),  
					getVariavelDecladrada(tokens.get(indice - 1).getStringLexema()),  
					getVariavelDecladrada(tokens.get(indice - 5).getStringLexema())
			);
			Tlinha(tokens);
		} else if (tokens.get(indice).getStringLexema().equals("$") || tokens.get(indice).getStringLexema().equals("+")
				|| tokens.get(indice).getStringLexema().equals("-")	|| tokens.get(indice).getStringLexema().equals(")") 
				|| tokens.get(indice).getStringLexema().equals("{") || tokens.get(indice).getStringLexema().equals(";")) {
			return;
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErros();
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
			this.imprimeErros();
			System.exit(1);
		}
	}

	public void atribuicao(ArrayList<Token> tokens) {
		if (tokens.get(indice).getStringLexema().matches(ExpressoesRegulares.regexIndentificadores)) {
			TokenIdentificador token = (TokenIdentificador)tokens.get(indice);
			// Verifica se a variável já foi declarada
			if(!this.verificarDeclaracao(token)) {
				//System.out.println("Variável " + token.getStringLexema() + " não declarada.");
				erros.add(new ErroSemantico("variável " + token.getLexema() + " não declarada."));
				this.imprimeErros();
				System.exit(-1);
			}
			token = this.getVariavelDecladrada(token.getStringLexema());
			String tipoIdentificador = token.getTipo();
			this.indice++;
			if (tokens.get(indice).getStringLexema().equals("=")) {
				this.indice++;
				valor_atribuido(tokens);
				// Verifica se a variável de atribuição possui o mesmo tipo das variáveis ou números da expressão
				if(tipoIdentificador.equals(this.verificarTipoExpressao())) {
					expressaoAtual.clear();
				} else {
					//System.out.println("Expressão com tipos diferentes");
					erros.add(new ErroSemantico("expressão com tipos diferentes."));
					this.imprimeErros();
					System.exit(1);
				}
				if (tokens.get(indice).getStringLexema().equals(";")) {
					this.indice++;
				} else {
					this.indice--;
					erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
					this.imprimeErros();
					System.exit(1);
				}
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErros();
				System.exit(1);
			}

		}
	}

	public void declaracao_multipla(ArrayList<Token> tokens) {
		this.indice++;
		if (tokens.get(indice).getStringLexema().matches(ExpressoesRegulares.regexIndentificadores)) {
			TokenIdentificador token = (TokenIdentificador)tokens.get(indice);
			// Verifica se a variável já foi declarada
			if(!verificarDeclaracao(token)) {
				token.setTipo(tipoDeclaracao);
				token.setDeclarado(true);
				variaveisDeclaradas.add(token);
			} else {
				//System.out.println("Variavel ja declarada: " + token.getStringLexema());
				erros.add(new ErroSemantico("variável " + token.getStringLexema() + " já declarada."));
				imprimeErros();
				System.exit(-1);
			}
			this.indice++;
			if (tokens.get(indice).getStringLexema().equals(";")) {
				// Gera instrução LOAD
				gerador.gerarCodigo(InstrucoesArquitetura.LOAD_WORD, getVariavelDecladrada(tokens.get(indice - 1).getStringLexema()));
				this.indice++;
				return;
			} else if (tokens.get(indice).getStringLexema().equals(",")) {
				// Gera instrução LOAD
				gerador.gerarCodigo(InstrucoesArquitetura.LOAD_WORD, getVariavelDecladrada(tokens.get(indice - 1).getStringLexema()));
				declaracao_multipla(tokens);
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErros();
				System.exit(1);
			}
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErros();
			System.exit(1);
		}
	}
	
	public void declaracao(ArrayList<Token> tokens) {
		this.indice++;
		if (tokens.get(indice).getStringLexema().matches(ExpressoesRegulares.regexIndentificadores)) {
			TokenIdentificador token = (TokenIdentificador)tokens.get(indice);
			// Verifica se a variável já foi declarada
			if(!verificarDeclaracao(token)) {
				token.setTipo(tokens.get(indice - 1 ).getStringLexema());
				token.setDeclarado(true);
				variaveisDeclaradas.add(token);
			} else {
				//System.out.println("Variavel ja declarada: " + token.getStringLexema());
				erros.add(new ErroSemantico("variável " + token.getStringLexema() + " já declarada."));
				imprimeErros();
				System.exit(-1);
			}
			this.indice++;
			if (tokens.get(indice).getStringLexema().equals(",")) {
				this.tipoDeclaracao = tokens.get(indice - 2).getStringLexema();
				// Gera instrução LOAD 
				gerador.gerarCodigo(InstrucoesArquitetura.LOAD_WORD, getVariavelDecladrada(tokens.get(indice - 1).getStringLexema()));
				declaracao_multipla(tokens);
			} else if (tokens.get(indice).getStringLexema().equals(";")) {
				// Gera instrucao LOAD
				gerador.gerarCodigo(InstrucoesArquitetura.LOAD_WORD, getVariavelDecladrada(tokens.get(indice - 1).getStringLexema()));
				this.indice++;
			} else if (tokens.get(indice).getStringLexema().equals("=")) {
				this.indice--;
				atribuicao(tokens);
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErros();
				System.exit(1);
			}
		} else {	
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErros();
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
			} else if(tokens.get(indice).getStringLexema().matches(ExpressoesRegulares.regexIndentificadores)) {
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
					this.imprimeErros();
					System.exit(1);
				}
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErros();
				System.exit(1);
			}
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErros();
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
					this.imprimeErros();
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
							this.imprimeErros();
							System.exit(1);
						}
					} else {
						erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
						this.imprimeErros();
						System.exit(1);
					}
				} 
			} else {
				erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
				this.imprimeErros();
				System.exit(1);
			}
		} else {
			erros.add(new ErroSintatico(tokens.get(indice).getLexema().getLinha(), tokens.get(indice).getLexema().getColuna()));
			this.imprimeErros();
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
			} else if(tokens.get(indice).getStringLexema().matches(ExpressoesRegulares.regexIndentificadores)) {
				atribuicao(tokens);
			} else {
				this.indice++;
			}
		}
	}
	
	/** 
	 * Métodos referentes a Análise Semântica
	 */

	public boolean verificarTipoFloat(int posicao) {
		Token t;
		for(int i = posicao; i < expressaoAtual.size(); i++) {
			t = expressaoAtual.get(i);
			if(t instanceof TokenIdentificador) {
				TokenIdentificador token = (TokenIdentificador)t;
				if(verificarDeclaracao(token)) {
					token = getVariavelDecladrada(token.getStringLexema());
					if(token.getTipo().equals("float")) {
						return true;
					} else {						
						return false;
					}
				} else {
					erros.add(new ErroSemantico("variável " + token.getStringLexema() + "não declarada."));
					imprimeErros();
					System.exit(-1);
					return false;
				}
			} else if( t instanceof TokenNumero) {
				if(t.getStringLexema().matches(ExpressoesRegulares.regexFloat)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	public boolean verificarTipoInt(int posicao) {
		Token t;
		for(int i = posicao; i < expressaoAtual.size(); i++) {
			t = expressaoAtual.get(i);
			if(t instanceof TokenIdentificador) {
				TokenIdentificador token = (TokenIdentificador)t;
				if(verificarDeclaracao(token)) {
					token = getVariavelDecladrada(token.getStringLexema());
					if(token.getTipo().equals("int")) {
						return true;
					} else {
						return false;
					}
				} else {
					erros.add(new ErroSemantico("variável " + token.getStringLexema() + "não declarada."));
					imprimeErros();
					System.exit(-1);
				}
			} else if( t instanceof TokenNumero) {
				if(t.getStringLexema().matches(ExpressoesRegulares.regexInt)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
	
	public String verificarTipoExpressao() {
		for(int i = 0; i < expressaoAtual.size(); i++) {
			Token t = expressaoAtual.get(i);
			if(t instanceof TokenIdentificador) {
				TokenIdentificador token = (TokenIdentificador) t;
				token = getVariavelDecladrada(token.getStringLexema());
				if(token.getTipo().equals("float")) {
					if(this.verificarTipoFloat(i + 1)) {
						return "float";
					} else {
						return null;
					}
				} else if(token.getTipo().equals("int")) {
					if(this.verificarTipoInt(i + 1)) {
						return "int";
					} else {
						return null;
					}
				}
			}
			if(t instanceof TokenNumero) {
				if(t.getStringLexema().matches(ExpressoesRegulares.regexFloat)) {
					return "float";
				}
				
				if(t.getStringLexema().matches(ExpressoesRegulares.regexInt)) {
					return "int";
				} 
				 
				
			}
		}
		return null;
	}
	
	TokenIdentificador getVariavelDecladrada(String lexema) {
		for(TokenIdentificador t: variaveisDeclaradas) {
			if(lexema.equals(t.getStringLexema())) {
				return t;
			}
		}
		return null;
	}
	
	
	public boolean verificarDeclaracao(TokenIdentificador token) {
		for(TokenIdentificador t : variaveisDeclaradas) {
			if(token.getStringLexema().equals(t.getStringLexema())) {
				return true;
			}			
		}
		return false;
	}
}
