package model;

public class ErroSemantico extends Erro {
	public ErroSemantico(String mensagem) {
		this.setMensagemErro("Erro Semântico: " + mensagem);
	}

}
