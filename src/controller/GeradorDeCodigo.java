package controller;

import java.util.ArrayList;

import model.InstrucoesArquitetura;
import model.Token;
import model.TokenIdentificador;

public class GeradorDeCodigo {
	private ArrayList<String> codigo;
	private int contadorRegistradorS;

	public GeradorDeCodigo() {
		codigo = new ArrayList<>();
		contadorRegistradorS = 0;
	}
	
	public ArrayList<String> getCodigo() {
		return codigo;
	}

	public void setCodigo(ArrayList<String> codigo) {
		this.codigo = codigo;
	}

	public void gerarCodigo(int instrucao, Token token) {
		TokenIdentificador t = (TokenIdentificador) token;
		switch (instrucao) {
		case InstrucoesArquitetura.LOAD_WORD:
			codigo.add("LW $S" + ++contadorRegistradorS + ", " + token.getStringLexema());
			t.setRegistrador("$S" + contadorRegistradorS);
			break;
		case InstrucoesArquitetura.STORE_WORD:

			break;
		default:
			System.out.println("Instrução Inválida!");
		}
	}

	public void gerarCodigo(int instrucao, Token tokenA, Token tokenB, Token resultado) {
		TokenIdentificador ta = (TokenIdentificador) tokenA;
		TokenIdentificador tb = (TokenIdentificador) tokenB;
		TokenIdentificador tr = (TokenIdentificador) resultado;
		
		switch (instrucao) {
		case InstrucoesArquitetura.ADD:
			codigo.add("ADD " + tr.getRegistrador() + ", " + ta.getRegistrador() + ", " + tb.getRegistrador());
			break;
		case InstrucoesArquitetura.SUB:
			codigo.add("SUB " + tr.getRegistrador() + ", " + ta.getRegistrador() + ", " + tb.getRegistrador());
			break;
		case InstrucoesArquitetura.MULT:
			codigo.add("MULT " + tr.getRegistrador() + ", " + ta.getRegistrador() + ", " + tb.getRegistrador());
			break;
		case InstrucoesArquitetura.DIV:
			codigo.add("DIV " + tr.getRegistrador() + ", " + ta.getRegistrador() + ", " + tb.getRegistrador());
			break;
		default:
			System.out.println("Instrução Inválida!");
		}
	}
	
	public void imprimirCodigo() {
		for(String linhaCodigo: codigo) {
			System.out.println(linhaCodigo);
		}
	}
}
