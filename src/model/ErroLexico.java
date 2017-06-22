package model;

public class ErroLexico extends Erro {
	
	public ErroLexico(int linha, int coluna) {
		this.setLinha(linha);
		this.setColuna(coluna);
		this.setMensagemErro("Erro Léxico -- Linha: " + linha + " Coluna: " + coluna);
	}
}
