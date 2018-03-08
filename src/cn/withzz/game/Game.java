package cn.withzz.game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cn.withzz.Internet.Host;
import cn.withzz.Internet.HostTool;
import cn.withzz.Internet.UserTool;
/*
 * 游戏入口类
 * 定义JFrame  绘制游戏入口菜单  监听鼠标操作实现菜单功能
 * 将必要图片资源加载进内存
 */
public class Game {
	BufferedImage background;
	BufferedImage chose1[] = new BufferedImage[2],
			chose2[] = new BufferedImage[2], chose3[] = new BufferedImage[2];
	BufferedImage chose11[] = new BufferedImage[2],
			chose12[] = new BufferedImage[2], chose13[] = new BufferedImage[2];
	BufferedImage chose21[] = new BufferedImage[2],
			chose22[] = new BufferedImage[2];
	BufferedImage gameList;
	//UserTool ut;
	HostTool ht;
	JFrame f = new JFrame();
	int chose = 0;
	int mode = 1;
	final JPanel j = new JPanel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
/*
 * (non-Javadoc)
 * @see javax.swing.JComponent#paint(java.awt.Graphics)
 * 绘制界面内容
 */
		@Override
		public void paint(Graphics g) {
			g.drawImage(background, 0, -10, 800, 500, this);
			switch (mode) {
			case 1:// 主菜单
				int[] order = { 0, 0, 0 };
				if (chose != 0)
					order[chose - 1] = 1;
				g.drawImage(chose1[order[0]], 0, 100, 300, 100, this);
				g.drawImage(chose2[order[1]], 0, 200, 300, 100, this);
				g.drawImage(chose3[order[2]], 0, 300, 300, 100, this);
				break;
			case 2:// 选择主机菜单
				int[] order1 = { 0, 0, 0 };
				if (chose != 0)
					order1[chose - 1] = 1;
				g.drawImage(chose11[order1[0]], 0, 100, 300, 100, this);
				g.drawImage(chose12[order1[1]], 0, 200, 300, 100, this);
				g.drawImage(chose13[order1[2]], 0, 300, 300, 100, this);
				g.drawImage(gameList, 280, 100, 500, 300, this);
				int i = 0;
//				for (Host p : ut.hostGroup) {
//					g.drawString(
//							p.getIp().toString() + "              "
//									+ p.getNum() + "/4", 300, 120 + (i * 10));
//					i++;
//				}
				break;
			case 3:// 主机菜单
				int[] order11 = { 0, 0, 0 };
				if (chose != 0)
					order11[chose - 1] = 1;
				g.drawImage(chose21[order11[0]], 0, 100, 300, 100, this);
				g.drawImage(chose22[order11[1]], 0, 200, 300, 100, this);
				g.drawImage(chose13[order11[2]], 0, 300, 300, 100, this);
				g.drawImage(gameList, 280, 100, 500, 300, this);
				int i1 = 0;
				for (Partner p : ht.users) {
					g.drawString(p.getIp().toString(), 300, 120 + (i1 * 10));
					i1++;
				}
				break;
			/*
			 * case 4://用户房间菜单 break;
			 */
			}
			g.dispose();//释放资源
		}
	};

	Game() {

		MouseMotionListener mml = new MouseMotionListener() {// 鼠标位置监听

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				// String string = "鼠标拖动到位置：（" + e.getX() + "，" + e.getY() +
				// "）";
				// System.out.println(string);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				// String string = "鼠标yi动到位置：（" + e.getX() + "，" + e.getY() +
				// "）";
				// System.out.println(string);
				// int pc = chose;
				chose = getBox(e.getX(), e.getY());
				/*
				 * if (pc != chose) j.repaint();
				 */
			}

		};
		
	/*
	 * 主要监听鼠标点击事件 
	 */
		MouseListener ml = new MouseListener() {// 鼠标动作监听

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int x, y;
				x = e.getX();
				y = e.getY();
				// String string = "点击：（" + x + "，" + y + "）";
				// System.out.println(string);
				if (mode == 1)
					menuWork(getBox(x, y));
				else if (mode == 2)
					netWork(getBox(x, y));
				else if (mode == 3)
					hostWork(getBox(x, y));
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
		// new MusicPlayer("RanMa.mid");//水泡爆炸音效

		// new MainMenu();
		/*
		 * 加载图片资源
		 * 背景图片 人物帧图片 道具图片 砖块地板植被图片 菜单标签图片
		 */
		try {

			background = ImageIO.read(new File("images/menu/background.jpg"));
			chose1[0] = ImageIO.read(new File("images/menu/1.png"));
			chose2[0] = ImageIO.read(new File("images/menu/2.png"));
			chose3[0] = ImageIO.read(new File("images/menu/3.png"));
			chose1[1] = ImageIO.read(new File("images/menu/10.png"));
			chose2[1] = ImageIO.read(new File("images/menu/20.png"));
			chose3[1] = ImageIO.read(new File("images/menu/30.png"));

			chose11[0] = ImageIO.read(new File("images/menu/joinHouse.png"));
			chose12[0] = ImageIO.read(new File("images/menu/newHouse.png"));
			chose13[0] = ImageIO.read(new File("images/menu/backMenu.png"));
			chose11[1] = ImageIO.read(new File("images/menu/joinHouse1.png"));
			chose12[1] = ImageIO.read(new File("images/menu/newHouse1.png"));
			chose13[1] = ImageIO.read(new File("images/menu/backMenu1.png"));

			chose21[0] = ImageIO.read(new File("images/menu/startGame.png"));
			chose21[1] = ImageIO.read(new File("images/menu/startGame1.png"));
			chose22[0] = ImageIO.read(new File("images/menu/kickOut.png"));
			chose22[1] = ImageIO.read(new File("images/menu/kickOut1.png"));
			gameList = ImageIO.read(new File("images/menu/gameList.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //添加各类监听和设置JFrame各项属性
		j.addMouseListener(ml);
		j.addMouseMotionListener(mml);
		j.setSize(400, 400);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		f.setLocation(width / 2 - 400, height / 2 - 250);
		f.setTitle("泡泡堂");
		f.setSize(800, 500);
		f.add(j);
		f.setResizable(false);
		f.setVisible(true);
		f.setFocusable(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thread.start();
	}
 //通过一个线程不断刷新页面内容 设置sleep30秒使画面连贯
	Thread thread = new Thread(new Runnable() {// 刷新界面
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						j.repaint();
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

	private int getBox(int x, int y) {// 判定鼠标位置属于哪个方块
		if (x > 40 && x < 260 && y > 130 && y < 370) {
			if ((y - 130) % 100 < 40)
				return (y - 130) / 100 + 1;
		}
		return 0;
	}

	private void netWork(int type) {// 联网对战点击调用
		switch (type) {
		case 1:// 加入房间
			//ut.sendM("join");
			// mode= 4;
			break;
		case 2:// 建立房间
			ht = new HostTool();
			System.out.println("\n创建主机");
			ht.startWork();
			//ut.stopWork();
			mode = 3;
			break;
		case 3:// 回到菜单
//			if (ut != null)
//				ut.stopWork();
			if (ht != null)
				ht.stopWork();
			mode = 1;
			break;
		}
	}

	private void hostWork(int type) {//主机操作调用
		switch (type) {
		case 1:// 开始游戏
			startGame();
			break;
		case 2:// 踢出玩家
			System.out.println("踢出玩家");
			break;
		case 3:// 回到菜单
		//	ut.startWork();
			if (ht != null)
				ht.stopWork();
			mode = 2;
			break;
		}
	}

	private void startGame() {//启动游戏
		ReadResource r = new ReadResource();
		r.init();
		Panel p = new Panel(r, Map.map2, Map.bothPlace2,2);
		ht.startGame(p);
		this.f.dispose();
		JFrame F = new JFrame();
		p.setPeople(ht.getMans());
		F.setSize(p.map.m2 * 60 + 5, p.map.m1 * 60 + 20);
		F.add(p);
		F.setBackground(Color.white);
		F.setLocationRelativeTo(null);
		F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		F.setVisible(true);
	}

	private void menuWork(int type) {// 菜单点击调用type传递操作值
		System.out.println("get :" + type);
		switch (type) {
		case 2://单人游戏
			f.dispose();
			ReadResource r = new ReadResource();
			r.init();
			List<Man> m = new ArrayList<Man>();
			Panel p = new Panel(r, Map.map1, Map.bothPlace1,2);
			m.add(new Man(p.map.bothPlace[0][0] * 60 - 20,
					p.map.bothPlace[0][1] * 60 - 30, 0, p, null));
			/*
			 * m.add(new
			 * Man(p.map.bothPlace[1][0]*60-20,p.map.bothPlace[1][1]*60
			 * -30,4,p,null)); m.add(new
			 * Man(p.map.bothPlace[2][0]*60-20,p.map.bothPlace
			 * [2][1]*60-30,4,p,null)); m.add(new
			 * Man(p.map.bothPlace[3][0]*60-20
			 * ,p.map.bothPlace[3][1]*60-30,4,p,null));
			 */
			// m.add(AIFactory.getAI(1,p.map.bothPlace[2][0]*60-20,p.map.bothPlace[2][1]*60-30,
			// p));
			// m.add(AIFactory.getAI(4,p.map.bothPlace[1][0]*60-20,p.map.bothPlace[1][1]*60-30,
			// p));
			for (int i = 0; i < 3; i++) {//随机生成三个AI
				m.add(AIFactory.getAI((int)(Math.random()*4)+1,
						p.map.bothPlace[i + 1][0] * 60 - 20,
						p.map.bothPlace[i + 1][1] * 60 - 30, p));
			}
			JFrame F = new JFrame();
			p.setPeople(m);
			F.setSize(p.map.m2 * 60 + 5, p.map.m1 * 60 + 20);
			F.add(p);
			F.setBackground(Color.white);
			F.setLocationRelativeTo(null);
			F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			F.setVisible(true);
			F.setResizable(false);

			break;
		case 1://双人对战
			f.dispose();
			ReadResource r1 = new ReadResource();
			r1.init();
			while (!r1.isReady)
				;
			List<Man> m1 = new ArrayList<Man>();
			Panel p1 = new Panel(r1, Map.map1, Map.bothPlace1,2);
			m1.add(new Man(p1.map.bothPlace[0][0] * 60 - 20,
					p1.map.bothPlace[0][1] * 60 - 30, 0, p1, null));
			// 添加P2//
			m1.add(new Man(p1.map.bothPlace[1][0] * 60 - 20,
					p1.map.bothPlace[1][1] * 60 - 30, 1, p1, null));
			JFrame F1 = new JFrame();
			p1.setPeople(m1);
			F1.setSize(p1.map.m2 * 60 + 5, p1.map.m1 * 60 + 20);
			F1.add(p1);
			F1.setBackground(Color.white);
			F1.setLocationRelativeTo(null);
			F1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			F1.setVisible(true);
			F1.setResizable(false);
			break;
		case 3:// 联网游戏
//			ut = new UserTool();
//			ut.startWork();
			mode = 2;
			break;
		}
	}
/*
 * 入口
 */
	public static void main(String[] args) {
		new Game();

	}

}
