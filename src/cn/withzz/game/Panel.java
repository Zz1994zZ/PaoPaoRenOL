package cn.withzz.game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cn.withzz.Internet.InetController;
/*
 * 将各类数据传入并绘制游戏界面
 * 
 */
public class Panel extends JPanel {
	/**
	 * 
	 */
	public InetController ic;
	private static final long serialVersionUID = 1L;
	Game myGame;
	public int TerminalMode= 1;//默认客户端
	ReadResource RR = null;
	public Map map;
	boolean shwoInfo = false;
	BufferedImage screen;
	public List<Ball> allBall = new ArrayList<Ball>();
	List<Man> people = new ArrayList<Man>();
	List<Weapon> allWeapon = new ArrayList<Weapon>();
	List<Item> allItem = new ArrayList<Item>();
	Boolean drawing = false, gameOver = false;
	BufferedImage overImage;
	Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	});

	public void setPeople(List<Man> l) {
		people.addAll(l);
		map.people = people;
		map.pl = this;
		this.setBackground(Color.white);
		for (Man mmm : people) {
			System.out.println("安装人物" + mmm.ID);
			this.addKeyListener(mmm.ka);// 设置监听
		}
	}

	public Panel(ReadResource r, int[][] m, int[][] bp,int mode) {
		this.TerminalMode=mode;
		RR = r;
		map = new Map(m, bp);
		setVisible(true);
		setFocusable(true);
		thread.start();
	}

	@Override
	public void paint(Graphics g) {
		if (RR == null || !RR.isReady)
			return;
		screen = new BufferedImage(map.m2 * 60, map.m1 * 60, 2);// 绘制地图
		Graphics graphics = screen.getGraphics();
		map.drawMap(graphics);
		List<Ball> copyAllBall = new ArrayList<Ball>();
		copyAllBall.addAll(allBall);
		for (Ball bb : copyAllBall) {// 绘制水泡
			drawing = true;
			drawBall(graphics, bb);
		}

		drawing = false;
		List<Item> copyAllItem = new ArrayList<Item>();
		copyAllItem.addAll(allItem);
		for (Item t : copyAllItem) {// 绘制物品
			t.drawItem(graphics);
		}
		for (Man m : people) {// 绘制人物
			if (m.isDead)
				continue;

			String name = m.getName();
			if (name.equals("zz"))
				graphics.setColor(Color.yellow);
			else if (m.type == 0 || m.type == 2)
				graphics.setColor(Color.blue);
			else
				graphics.setColor(Color.RED);
			graphics.drawString(name, m.getX() + 30, m.getY());

			drawMan(graphics, m);
			if (shwoInfo) {
				graphics.drawString("x=" + m.getX() + "y=" + m.getY() + "s="
						+ m.getSpeed() + "num=" + m.weaponNum + "wt="
						+ m.weaponType + "b=" + m.ball.size(), m.getX(),
						m.getY() - 10);
				graphics.drawLine(m.getX() + 20, m.getY() + 30, m.getX() + 80,
						m.getY() + 30);
				graphics.drawLine(m.getX() + 20, m.getY() + 30, m.getX() + 20,
						m.getY() + 90);
				graphics.drawLine(m.getX() + 80, m.getY() + 30, m.getX() + 80,
						m.getY() + 90);
				graphics.drawLine(m.getX() + 20, m.getY() + 90, m.getX() + 80,
						m.getY() + 90);
				graphics.setColor(Color.red);
				graphics.drawLine(m.gx * 60, m.gy * 60, m.gx * 60 + 60,
						m.gy * 60 + 60);
			}

		}
		List<Weapon> copyAllWeapon = new ArrayList<Weapon>();
		copyAllWeapon.addAll(allWeapon);
		for (Weapon w : copyAllWeapon) {// 绘制武器
			w.drawWeapon(graphics);
		}
		if (gameOver) {
			graphics.drawImage(overImage, map.m2 * 60 / 2 - 250,
					map.m1 * 60 / 2 - 120, 500, 243, this);
			// graphics.drawImage(overImage,map.m2*60/2-682,map.m1*60/2-384,1364,768,this);
			graphics.drawImage(RR.cGame, map.m2 * 60 / 2 - 82, map.m1 * 60 / 2
					- 24 + 200, 165, 48, this);
		}
		g.drawImage(screen, 0, 0, this);
		g.dispose();
	}

	public void drawMan(Graphics g, Man m) {// 人物
		Graphics graphics = screen.getGraphics();
		graphics.drawImage(m.imgnow, m.getX(), m.getY(), m.getSize(),
				m.getSize(), this);
	}

	public void drawBall(Graphics g, Ball b) {// 水泡
		if (b.state == 1) {
			Graphics graphics = screen.getGraphics();
			int x = b.x;
			int y = b.y;
			graphics.drawImage(b.ball[8], x * map.size - 5, y * map.size - 5,
					map.size + 10, map.size + 10, this);
			for (int i = x + 1; i <= x + b.rl; i++) {
				graphics.drawImage(b.ball[5], i * map.size, y * map.size,
						map.size + 20, map.size, this);
			}
			for (int i = x - 1; i >= x - b.ll; i--) {
				graphics.drawImage(b.ball[7], i * map.size - 20, y * map.size,
						map.size + 20, map.size, this);
			}
			for (int i = y - 1; i >= y - b.ul; i--) {
				graphics.drawImage(b.ball[4], x * map.size, i * map.size - 20,
						map.size, map.size + 20, this);
			}
			for (int i = y + 1; i <= y + b.dl; i++) {
				graphics.drawImage(b.ball[6], x * map.size, i * map.size,
						map.size, map.size + 20, this);
			}

			return;
		}
		Graphics graphics = screen.getGraphics();
		graphics.drawImage(b.imgnow, b.x * 60, b.y * 60, map.size, map.size,
				this);
	}

	public void gameOver(int type) {
		switch (type) {
		case 1:
			System.out.println("你赢了~");
			overImage = RR.victory;
			gameOver = true;
			break;
		case 0:
			System.out.println("你跪了~");
			overImage = RR.failed;
			gameOver = true;
			break;
		case 2:
		case 3:
			boolean isWin = true;
			for (Man m : people) {
				if (m.type != 0 && m.type != 2 && !m.isOver)
					isWin = false;
			}
			if (isWin) {
				System.out.println("你赢了~");
				overImage = RR.victory;
				gameOver = true;
			}
			break;
		}
		if(gameOver)
		this.addMouseListener(ml);

	}
boolean clicked=false;
	MouseListener ml = new MouseListener() {// 鼠标动作监听

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if(clicked)
				return;
			clicked=true;
			int x, y;
			x = e.getX();
			y = e.getY();
			// graphics.drawImage(RR.cGame,map.m2*60/2-82,map.m1*60/2-24+200,165,48,this);
			if (x > map.m2 * 60 / 2 - 82 && x < map.m2 * 60 / 2 - 82 + 165
					&& y > map.m1 * 60 / 2 - 24 + 200
					&& y < map.m1 * 60 / 2 - 24 + 200 + 48) {
				System.out.println("继续游戏");
				JFrame j = (JFrame) Panel.this.getRootPane().getParent();
				j.dispose();
				new Game();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	};

}
