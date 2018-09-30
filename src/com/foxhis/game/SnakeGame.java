package com.foxhis.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
public class SnakeGame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 500, HEIGHT = 400, SLEEPTIME = 200, L = 1,R = 2, U = 3, D = 4;
	
	BufferedImage offersetImage= new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_3BYTE_BGR);
	

	Rectangle rect = new Rectangle(20, 40, 9 * 50, 10 * 33);
	
	Snake snake;//蛇
	SNode node;//食物
	public SnakeGame() {
		snake = new Snake(this); //创建一条蛇
		createNode();  //随机创建一个食物
		this.setBounds(200, 200, WIDTH, HEIGHT); //设置窗口的大小，坐标
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					snake.dir = L;
					break;
				case KeyEvent.VK_RIGHT:
					snake.dir = R;
					break;
				case KeyEvent.VK_UP:
					snake.dir = U;
					break;
				case KeyEvent.VK_DOWN:
					snake.dir = D;
				}
			}
		});
		this.setTitle("Snake Game 0.1   By  tq");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		new Thread(new ThreadUpadte()).start();
	}
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) offersetImage.getGraphics();
		g2d.setColor(Color.green);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.black);
		g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
		if (snake.hit(node)) {
			createNode();
		}
		snake.draw(g2d);
		node.draw(g2d);
		g.drawImage(offersetImage, 0, 0, null);
	}
	
	class ThreadUpadte implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(SLEEPTIME);
					repaint(); //不停的刷面板
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void createNode() {
		int x = (int) (Math.random() * 350) + 50,y = (int) (Math.random() * 270) + 50;
		node = new SNode(x, y, Color.red);
	}
	public static void main(String args[]) {
		new SnakeGame();
	}
}


/**
 * 
 * @author tq
 *
 */
class SNode {
	int x, y, width = 15, height = 15;
	Rectangle rect = new Rectangle(x, y, width, height);
	Color color;
	public SNode(int x, int y, Color color) {
		this.x = x; this.y = y;
		this.color = color;
	}
	public void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.drawRect(x, y, width, height);
	}
	public Rectangle getRect() {
		rect.x = x; rect.y = y;
		return rect;
	}
}


/**
 * 
 * @author tq
 *
 */
class Snake {   
	public List<SNode> nodes = new ArrayList<SNode>();
	SnakeGame interFace;
	int dir=SnakeGame.R;
	public Snake(SnakeGame interFace) {
		this.interFace = interFace;
		nodes.add(new SNode(20 + 150, 40 + 150, Color.black));
		addNode();
	}
	public boolean hit(SNode node) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).getRect().intersects(node.getRect())) {
				addNode();
				return true;
			}
		}
		return false;
	}
	public void draw(Graphics2D g2d) {
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).draw(g2d);
		}
		move();
	}
	public void move() {
		nodes.remove((nodes.size() - 1));
		addNode();
	}
	
	//每次都是去掉尾部，加到前面
	public synchronized void addNode() {
		SNode nodeTempNode = nodes.get(0);
		switch (dir) {
		case SnakeGame.L:
			if (nodeTempNode.x <= 20) {
				nodeTempNode = new SNode(20 + 15 * 50, nodeTempNode.y, Color.black);
			}
			nodes.add(0, new SNode(nodeTempNode.x - nodeTempNode.width, nodeTempNode.y, Color.black ));
			break;
		case SnakeGame.R:
			if (nodeTempNode.x >= 20 + 15 * 50 - nodeTempNode.width) {
				nodeTempNode = new SNode(20 - nodeTempNode.width, nodeTempNode.y, Color.black);
			}
			nodes.add(0, new SNode(nodeTempNode.x + nodeTempNode.width, nodeTempNode.y, Color.black));
			break;
		case SnakeGame.U:
			if (nodeTempNode.y <= 40) {
				nodeTempNode = new SNode(nodeTempNode.x, 40 + 15 * 35, Color.black);
			}
			nodes.add(0, new SNode(nodeTempNode.x, nodeTempNode.y - nodeTempNode.height, Color.black));
			break;
		case SnakeGame.D:
			if (nodeTempNode.y >= 40 + 15 * 35 - nodeTempNode.height) {
				nodeTempNode = new SNode(nodeTempNode.x,40 - nodeTempNode.height, Color.black);
			}
			nodes.add(0, new SNode(nodeTempNode.x, nodeTempNode.y + nodeTempNode.height, Color.black));
			break;
		}
	}
}