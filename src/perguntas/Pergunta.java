package perguntas;

import java.util.ArrayList;

public class Pergunta {
	public String pergunta;
	public ArrayList<String> respostas;
	public int resposta;
	public Pergunta(String pergunta, ArrayList<String> respostas) {
		this.pergunta = pergunta;
		this.respostas = (ArrayList<String>) respostas.clone();
	}
	
}
