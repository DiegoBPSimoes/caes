package botao;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Botao {
	public static int l=180,a=50;
	public boolean select=false;
	public int x, y, pos;
	public String texto;
	public Botao(int x, int y,String texto) {
		this.x=x;
		this.y=y;
		this.texto=texto;
	}
	public void render(Graphics g) {
		g.setFont(new Font("Arial", 2, 18));
		if(!select) {
			g.setColor(new Color(255, 255, 200));
			g.fillRoundRect(x, y, l, a, 50, 50);
			
			
			
		}
		else {
			g.setColor(new Color(255, 100, 50));//laranja
			g.fillRoundRect(x, y, l, a, 50, 50);
			
			
			//g.setColor(new Color(0, 0, 0));
			
		}
		g.setColor(new Color(153, 217, 234));
		
	}
}
