
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.xml.transform.Templates;

import botao.Botao;
import imagens.SpriteSheet;
import perguntas.Pergunta;

public class Game extends Canvas implements Runnable, MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;
	public static JFrame frame;// gera a plataforma que o jogo ira rodar
	private Thread thread;// cria o timer do jogo ou tick porem nao instancia, apenas reserva a memoria
	private BufferedImage image; // para uso geral
	private boolean isRunning = true;// flag de jogo rodando
	public static final int WIDTH = 800;// largura da plataforma
	public static final int HIGHT = 600;// altura da plataforma
	static final int SCALE = 1;// escala da plataforma, forma mais eficar de redimencionar o tamanho da tela

	static Botao bot1;
	static boolean bm = true, perguntando = true;
	static ArrayList<Botao> botoes;
	static ArrayList<Pergunta> perguntas;
	static ArrayList<String> respostas;
	static int posPergunta = 0, resposta;
	static SpriteSheet sheet;
	static BufferedImage foto;
	static String textoFinal;

	public Game() {
		addMouseListener(this);// para que eventos de mouse funcionem
		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HIGHT * SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HIGHT, BufferedImage.TYPE_INT_RGB);

	}

	public static void main(String[] args) {
		Game game = new Game();// instancia o jogo pelo contrutor
		botoes = new ArrayList<Botao>();
		bot1 = new Botao(WIDTH - Botao.l - 30, HIGHT - 4 * Botao.a, "Proximo");
		bot1.select = true;
		perguntas = new ArrayList<Pergunta>();
		respostas = new ArrayList<String>();
		criandoPerguntas();
		sheet = new SpriteSheet("/drCao.png");
		foto = sheet.getSprite(0, 0, 300, 300);

		for (int i = 0; i < perguntas.get(posPergunta).respostas.size(); i++) {
			botoes.add(new Botao(10 + (Botao.l + 10) * i, 300, perguntas.get(posPergunta).respostas.get(i)));
		}

		game.start();// inicia o jogo

	}

	public void tick() {

	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();// conjunto de buffer para otimizar a renderização
		if (bs == null) {// caso esteja vazio
			this.createBufferStrategy(3);// criamos um com 3 espaços podem ser 2 ou 3
			return;
		}
		Graphics g = image.getGraphics();

		g = bs.getDrawGraphics();
		// tela de limpeza, foi criado um retângulo com o tamanho da tela em preto.
		g.setColor(new Color(153, 217, 234));
		g.fillRect(0, 0, WIDTH, HIGHT);
		g.setColor(new Color(255, 100, 50));
		g.fillOval(60, 60, 200, 200);
		g.setColor(new Color(0, 50, 200));
		g.fillOval(70, 70, 180, 180);
		g.drawImage(foto, 66, 70, 184, 184, null);
		g.setFont(new Font("Arial", 3, 30));
		g.setColor(new Color(255, 255, 200));
		g.fillOval(250, 90, 50, 50);
		g.setColor(new Color(153, 217, 234));
		g.fillOval(245, 70, 40, 40);
		if (perguntando) {
			g.setColor(new Color(255, 255, 200));
			g.fillRoundRect(280, 70, 500, 180, 80, 80);
			g.setColor(new Color(153, 217, 234));
			g.setFont(new Font("Arial", 3, 30));
			g.drawString(perguntas.get(posPergunta).pergunta, 290, 150);

			for (int i = 0; i < botoes.size(); i++) {
				botoes.get(i).render(g);
			}
			for (int i = 0; i < botoes.size(); i++) {
				if(i>=3) {
					g.drawString(perguntas.get(posPergunta).respostas.get(i), botoes.get(i).x + 10, botoes.get(i).y + 30);
				}
				else {
					g.drawString(perguntas.get(posPergunta).respostas.get(i), botoes.get(i).x + 10, botoes.get(i).y + 30);
				}
			}
			bot1.render(g);
			g.drawString("próximo", bot1.x + 50, bot1.y + 30);
		} else {

			g.setColor(new Color(255, 255, 200));
			g.fillRoundRect(280, 70, 500, 300, 80, 80);
			g.setColor(new Color(153, 217, 234));
			g.drawString("Diagnosticamos que seu cão está:", 280, 150);
			g.drawString(textoFinal, 350, 250);
			
			bot1.render(g);
			g.drawString("Voltar", bot1.x + 50, bot1.y + 30);
		}
		bs.show();
	}

	public static void atualizaBotoes() {
		botoes.clear();
		for (int i = 0; i < perguntas.get(posPergunta).respostas.size(); i++) {
			if (i >= 3) {
				botoes.add(new Botao(10 + (Botao.l + 10) * (i-3), 370, perguntas.get(posPergunta).respostas.get(i)));
			} else {
				botoes.add(new Botao(10 + (Botao.l + 10) * i, 300, perguntas.get(posPergunta).respostas.get(i)));
			}
		}

	}

	public static void criandoPerguntas() {
		respostas.add("Para cima");
		respostas.add("Caidas");
		respostas.add("Normal");
		perguntas.add(new Pergunta("A orelha do seu cachorro está:", respostas));
		respostas.clear();

		respostas.add("Rígido");
		respostas.add("Inclinado para frente");
		respostas.add("Inclinado para trás");
		respostas.add("Agachado");
		respostas.add("Normal");
		perguntas.add(new Pergunta("O corpo de seu cachorro está:", respostas));
		respostas.clear();

		respostas.add("Abanando");
		respostas.add("Entre as pernas");
		respostas.add("Rígido para cima");
		perguntas.add(new Pergunta("O rabo do seu cachorro está:", respostas));
		respostas.clear();

		respostas.add("Sim");
		respostas.add("Não");
		perguntas.add(new Pergunta("O pet está com a lingua para fora?", respostas));
		respostas.clear();

	}

	public synchronized void start() {

		thread = new Thread(this);// estancia o time do game
		isRunning = true;// liga a flag para que o jogo possa fluir
		thread.start();
	}

	public synchronized void stop() {
		isRunning = false;// abaixa a flag de jogo rodando
		try {
			thread.join();// nao sei
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void initFrame() {// aqui se constroe a plataforma com as preferencias
		frame = new JFrame("Pets Especialista ");// cria o game... "game #1 "é o titulo do game
		frame.add(this);// nao entendi esta linha de comando
		frame.setResizable(false);// impede o redirecionamento da janela
		frame.pack();// mostrar a janela
		frame.setLocationRelativeTo(null);// coloca a janela no centro da tela
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// garante que ao clicar em fechar ele encerre o aplicativo
		frame.setVisible(true);// ao inicializar fica visivel imediatamente
	}

	@Override
	public void run() {// verifica a flag de jogo rodando
		long lastTime = System.nanoTime();// pega a hora do sisitema em nanosegundos
		double amountOfTicks = 30.0;// ainda nao sei
		double ns = 1000000000 / amountOfTicks;// nao sei
		double delta = 0;// flag de estouro de timer
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {// condicional para verificação do game rodando
			long now = System.nanoTime();// pega a hora do sisitema em nano time
			delta += (now - lastTime) / ns;// soma as diferenças de tempo entre now w lastTime e divide por nano
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				delta--;// baixa o flag do delta porem mantem o erro acumulando e corrigindo o
						// syncronismo
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				// System.out.println("fps: "+frames);
				timer += 1000;
			}
		}
		stop();// caso ocorra algum problema e o programa saia do loop necessario desligar a
				// threds
	}

	public void criarResposta() {
		if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 4 && perguntas.get(2).resposta == 2
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Alerta");
			textoFinal="Alerta";

		} else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 1 && perguntas.get(2).resposta == 2
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Possível Ameaça");
			textoFinal="Possível Ameaça";

		}

		else if (perguntas.get(0).resposta == 1 && perguntas.get(1).resposta == 3 && perguntas.get(2).resposta == 1
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Medo");
			textoFinal="Medo";
			

		}

		else if (perguntas.get(0).resposta == 1 && perguntas.get(1).resposta == 2 && perguntas.get(2).resposta == 1
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Ameaçado");
			textoFinal="Ameaçado";

		}

		else if (perguntas.get(0).resposta == 2 && perguntas.get(1).resposta == 4 && perguntas.get(2).resposta == 2
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Sem Problemas");
			textoFinal="Sem Problemas";

		}

		else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 3 && perguntas.get(2).resposta == 2
				&& perguntas.get(3).resposta == 1) {
			System.out.println("À espreita");
			textoFinal="À espreita";

		}

		else if (perguntas.get(0).resposta == 1 && perguntas.get(1).resposta == 1 && perguntas.get(2).resposta == 2
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Muito irritado");
			textoFinal="Muito irritado";

		}

		else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 4 && (perguntas.get(2).resposta == 2||perguntas.get(2).resposta == 1)
				&& perguntas.get(3).resposta == 0) {
			System.out.println("Estressado");
			textoFinal="Estressado";

		}

		else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 3 && perguntas.get(2).resposta == 1
				&& perguntas.get(3).resposta == 1) {
			System.out.println("precisa de tempo (lambendo o nariz)");
			textoFinal="Precisa de tempo";

		}

		else if (perguntas.get(0).resposta == 2 && perguntas.get(1).resposta == 3 && perguntas.get(2).resposta == 2
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Ancioso");
			textoFinal="Ancioso";

		}

		else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 0 && perguntas.get(2).resposta == 2
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Incomodado");
			textoFinal="Incomodado";

		} else if (perguntas.get(0).resposta == 2 && perguntas.get(1).resposta == 4 && perguntas.get(2).resposta == 2
				&&( perguntas.get(3).resposta == 0 || perguntas.get(3).resposta == 1)) {
			System.out.println("Tranquilo");
			textoFinal="Tranquilo";

		}

		else if (perguntas.get(0).resposta == 1 && perguntas.get(1).resposta == 2 && perguntas.get(2).resposta == 0
				&& perguntas.get(3).resposta == 0) {
			System.out.println("Amigável e educado");
			textoFinal="Amigável e educado";

		}

		else if (perguntas.get(0).resposta == 1 && perguntas.get(1).resposta == 4 && perguntas.get(2).resposta == 1
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Pedindo");
			textoFinal="Pedindo algo";

		}

		else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 1 && perguntas.get(2).resposta == 0
				&& perguntas.get(3).resposta == 0) {
			System.out.println("Querendo brincar");
			textoFinal="Querendo brincar";

		}

		else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 4 && perguntas.get(2).resposta == 1
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Curioso");
			textoFinal="Curioso";

		}

		else if (perguntas.get(0).resposta == 1 && perguntas.get(1).resposta == 4 && perguntas.get(2).resposta == 0
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Amigável");
			textoFinal="Amigável";

		}

		else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 4 && perguntas.get(2).resposta == 0
				&& perguntas.get(3).resposta == 0) {
			System.out.println("Feliz e contente");
			textoFinal="Feliz e contente";

		}

		else if (perguntas.get(0).resposta == 2 && perguntas.get(1).resposta == 4 && perguntas.get(2).resposta == 0
				&& perguntas.get(3).resposta == 0) {
			System.out.println("Super contente");
			textoFinal="Super contente";

		} else if (perguntas.get(0).resposta == 0 && perguntas.get(1).resposta == 3 && perguntas.get(2).resposta == 0
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Preparado para agir");
			textoFinal="Preparado para agir";

		}

		else if (perguntas.get(0).resposta == 2 && perguntas.get(1).resposta == 1 && perguntas.get(2).resposta == 0
				&& perguntas.get(3).resposta == 1) {
			System.out.println("Demonstrando Amor");
			textoFinal="Demonstrando Amor";

		}

		else if (perguntas.get(0).resposta == 1 && perguntas.get(1).resposta == 2 && perguntas.get(2).resposta == 0
				&& perguntas.get(3).resposta == 0) {
			System.out.println("Cumprimentando");
			textoFinal="Cumprimentando";

		}
		else {
			System.out.println("Cumprimentando");
			textoFinal="Sem definição";
			
		}

	}

	public void verificaClique(int x, int y) {

		Rectangle rec1 = new Rectangle(x, y, 2, 2);
		if (perguntando) {
			for (int i = 0; i < botoes.size(); i++) {
				if (rec1.intersects(new Rectangle(botoes.get(i).x , botoes.get(i).y, Botao.l, Botao.a))) {
					for (int j = 0; j < botoes.size(); j++) {
						botoes.get(j).select = false;
					}
					botoes.get(i).select = true;
					resposta = i;

				}

			}
			if (rec1.intersects(new Rectangle(bot1.x, bot1.y, Botao.l, Botao.a)) & bm) {
				perguntas.get(posPergunta).resposta = resposta;
				posPergunta++;
				if (posPergunta <= perguntas.size() - 1) {
					atualizaBotoes();
				} else {
					perguntando = false;
					criarResposta();
				}

			}
		} else {
			if (rec1.intersects(new Rectangle(bot1.x, bot1.y, Botao.l, Botao.a)) & bm) {
				posPergunta = 0;
				atualizaBotoes();
				perguntando = true;

			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

		verificaClique(e.getX(), e.getY());
		bm = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		bm = true;

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
