package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.InstrucoesArquitetura;
import model.RegistradoresArquitetura;
import model.Token;

public class GeradorDeCodigo {
	private Map<Integer, String> registradoresEmUso;
	private ArrayList<String> codigo;

	public GeradorDeCodigo() {
		registradoresEmUso = new HashMap<Integer, String>();
		codigo = new ArrayList<>();
	}

	public Map<Integer, String> getRegistradoresEmUso() {
		return registradoresEmUso;
	}

	public void setRegistradoresEmUso(Map<Integer, String> registradoresEmUso) {
		this.registradoresEmUso = registradoresEmUso;
	}

	public ArrayList<String> getCodigo() {
		return codigo;
	}

	public void setCodigo(ArrayList<String> codigo) {
		this.codigo = codigo;
	}

	public void gerarCodigo(int instrucao, Token token) {
		switch (instrucao) {
		case InstrucoesArquitetura.LOAD_WORD:

			break;
		case InstrucoesArquitetura.STORE_WORD:

			break;
		default:
			System.out.println("Instrução Inválida!");
		}
	}

	public void gerarCodigo(int instrucao, Token tokenA, Token tokenB) {
		switch (instrucao) {
		case InstrucoesArquitetura.ADD:

			break;

		case InstrucoesArquitetura.SUB:

			break;
		default:
			System.out.println("Instrução Inválida!");
		}
	}
	
	private String getRegistrador(int registrador) {
		if(registrador >= RegistradoresArquitetura.S_0 || registrador <= RegistradoresArquitetura.S_7) {
			for(int i = RegistradoresArquitetura.S_0; i <= RegistradoresArquitetura.S_7; ++i) {
				if(!registradoresEmUso.containsKey(i)){
					registradoresEmUso.put(i, "S" + i);
					return registradoresEmUso.get(i);
				}
			}
		}
		return null;
	}
}
