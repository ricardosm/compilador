package model;

public class ErroSintatico extends Erro {
	public ErroSintatico(int linha, int coluna) {
		this.setLinha(linha);
		this.setColuna(coluna);
		this.setMensagemErro("Erro Sintático -- Linha: " + linha + " Coluna: " + coluna);
	}
}
