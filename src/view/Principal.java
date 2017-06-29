package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import controller.AnalisadorLexico;
import controller.AnalisadorSintatico;
import controller.GeradorDeCodigo;
import model.Erro;
import model.Token;

public class Principal {
	public static void main(String [] args) {
		final String nomeArquivoEntrada, nomeArquivoSaida;
		final Scanner sc = new Scanner(System.in);
		final InputStream is;
		final OutputStream os;
		final InputStreamReader isr;
		final OutputStreamWriter osw;
		final BufferedReader br;
		final BufferedWriter bw;
		
		AnalisadorLexico lexico = new AnalisadorLexico();
		GeradorDeCodigo gerador = new GeradorDeCodigo();
		AnalisadorSintatico sintatico = new AnalisadorSintatico(gerador);
		
		
		System.out.println("Digite o nome do Arquivo de Entrada");
		nomeArquivoEntrada = sc.nextLine();
		nomeArquivoSaida = "arquivoSaida.txt";
		
		try {
			is = new FileInputStream(nomeArquivoEntrada);
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			os = new FileOutputStream(nomeArquivoSaida);
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			
			lexico.scanear(br);
			
			System.out.println("\n--- Lista de Tokens: --- \n");
			bw.write("--- Lista de Tokens: ---");
			bw.newLine();
			bw.newLine();

			for(Token t : lexico.getTokens()) {
				System.out.print("Token: ");
				System.out.println( t.getToken());
				System.out.println("Posição: [" + t.getLexema().getLinha() + " , " + t.getLexema().getColuna() + "]");
				System.out.println(" ");
				
				bw.write("Token: ");
				bw.write( t.getToken());
				bw.newLine();
				bw.write("Posição: [" + t.getLexema().getLinha() + " , " + t.getLexema().getColuna() + "]");
				bw.newLine();
				bw.newLine();				
			}
			
			System.out.println("\n--- Lista de Erros: --- \n");
			bw.newLine();
			bw.write("--- Lista de Erros: ---");
			bw.newLine();
			bw.newLine();
			
			for(Erro e: lexico.getErros()) {
				System.out.println(e.getMensagemErro());
				bw.write(e.getMensagemErro());
				bw.newLine();
			}
			
			
			sintatico.programa(lexico.getTokens());
			
			gerador.imprimirCodigo();
		
			for(Erro e: sintatico.getErros()) {
				System.out.println(e.getMensagemErro());
				bw.write(e.getMensagemErro());
				bw.newLine();
			}
			
			bw.flush();
			
			is.close();
			isr.close();
			br.close();
			os.close();
			osw.close();
			bw.close();
		} catch(IOException e) {
			System.out.println("Problema ao ler o arquivo de entrada!");
		} finally {
			sc.close();			
		}
	}
}
