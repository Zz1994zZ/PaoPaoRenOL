package cn.withzz.game;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import cn.withzz.Internet.InetController;
import cn.withzz.Tool.MiGong;

/**
 * 
 * 游戏内角色
 * x,y绘图所使用的坐标
 * gx,gy碰撞判定所使用的坐标
 * size人物大小（适应屏幕）
 * 
 */
public class Man {
	private int x, y, max = 2, speed = 8, size = 100, power = 1;
	private String name = "Blanker";
	public int rx = 1000;// 用于逃离身上的泡泡~
	public int ry = 1000;
	List<Ball> ball = new ArrayList<Ball>();
	List<Ball> allBall;
	List<Weapon> weapon = new ArrayList<Weapon>();
	List<Weapon> allWeapon;
	List<int[]> stuckList = new ArrayList<int[]>();
	BufferedImage[] face, back, left, right, state, dying, dead;
	BufferedImage imgnow;
	Ball newset = null;
	boolean isDead = false, isDying = false, isOver = false;
	boolean setBall = false, beStuck = false;
	boolean ddd = false;
	Map p;
	MiGong mg;
	Panel myPanel;
	int direction = 2, step = 0;
	public int gx = 0;
	public int gy = 0;
	int ix = 0;
	int iy = 0;
	int type;
	boolean stand = true;
	KeyAdapter ka;
	int weaponType = 0;
	int weaponNum = 0;
	int deadNum = 0;
	// IManager im;
	public int ID = -1;
   //设置角色ID 主要用于网络对战区分
	public void setID(int i) {
		ID = i;
	}

	public int getID() {
		return ID;
	}
    //设置名称 可在对战时显示在游戏界面
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
    //设置坐标
	public void setSeat(int x, int y) {
		this.x = x;
		this.y = y;
		System.out.println("设置坐标");
	}

	public int getSpeed() {
		return speed;
	}
    //设置移动速度
	public void setSpeed(int x) {
		if (x < 4)
			return;
		this.speed = x;
	}
    
	public int getMax() {
		return max;
	}
    //设置泡泡数量上限 （最大10）
	public void setMax(int x) {
		if (x > 10)
			return;
		this.max = x;
	}

	public int getSize() {
		return size;
	}
   
	public int getPower() {
		return power;
	}
	 //设置泡泡威力
	public void setPower(int x) {
		if (x > 10)
			return;
		this.power = x;
	}

	// ********************************人物行为*********************************************
	private void move(String d) {// 运动
		if (isDead || isDying)
			return;
		switch (d) {
		case "1":
			state = back;
			direction = 1;
			stand = false;
			// System.out.println("up");
			break;
		case "2":
			state = face;
			direction = 2;
			stand = false;
			// System.out.println("down");
			break;
		case "3":
			state = left;
			direction = 3;
			stand = false;
			// System.out.println("left");
			break;
		case "4":
			state = right;
			direction = 4;
			stand = false;
			// System.out.println("right");
			break;
		}
	}

	private void stop(String d) {// 停止运动
		BufferedImage[] rs = null;
		switch (d) {
		case "10":
			rs = back;
			break;
		case "20":
			rs = face;
			break;
		case "30":
			rs = left;
			break;
		case "40":
			rs = right;
			break;
		}
		if (rs == state && !isDead && !isDying) {
			imgnow = state[5];
			stand = true;
		}
	}

	// ******************************************************************************************************
	public Controller ctl = new Controller() {// 接受控制

		@Override
		public void recive(String m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			switch (m) {
			case "1":
			case "2":
			case "3":
			case "4":
				move(m);
				break;
			case "11":
				skill();
				break;
			case "10":
			case "20":
			case "30":
			case "40":
				stop(m);
				break;
			}
		}

	};

	public void setSkill(int x) {// 设置技能
		if (weaponType == x)
			weaponNum += 2;
		else {
			weaponType = x;
			weaponNum = 2;
		}
	}

	private void skill() {// 施放技能
		if (isDead || isDying)
			return;
		if (weaponNum <= 0) {
			weaponType = 0;
			weaponNum = 0;
		}
		switch (weaponType) {
		case 0:
			if (ball.size() < max) {
				newset = Ball.setBall(Man.this);
				if (newset != null) {
					ball.add(newset);
					allBall.add(newset);
				}
			}
			break;
		case 1:
			Weapon ww = Weapon.getWeapon(Man.this);
			weapon.add(ww);
			allWeapon.add(ww);
			break;
		case 2:
			Weapon www = Weapon.getWeapon(Man.this);
			weapon.add(www);
			allWeapon.add(www);
			System.out.println("二阶段导弹");
			break;
		case 3:
			System.out.println("三阶段导弹");
			break;
		}
		if (weaponType != 0)
			weaponNum--;
	}

	/**
	 * 控制***********************************************************************
	 * ************************
	 */
	KeyAdapter ka0 = new KeyAdapter() {// ********************************************1P操作
		@Override
		public void keyReleased(KeyEvent e) {
			BufferedImage[] rs = null;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				rs = back;
				break;
			case KeyEvent.VK_DOWN:
				rs = face;
				break;
			case KeyEvent.VK_LEFT:
				rs = left;
				break;
			case KeyEvent.VK_RIGHT:
				rs = right;
				break;
			case KeyEvent.VK_B:

				if (myPanel.shwoInfo)
					myPanel.shwoInfo = false;
				else
					myPanel.shwoInfo = true;
				System.out.println("showInfo");
				break;
			}
			if (rs == state && !isDead && !isDying) {
				imgnow = state[5];
				stand = true;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			/*
			 * if(isDead) refresh();
			 */
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (isDead || isDying)
				return;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				state = back;
				direction = 1;
				stand = false;
				// System.out.println("up");
				break;
			case KeyEvent.VK_DOWN:
				state = face;
				direction = 2;
				stand = false;
				// System.out.println("down");
				break;
			case KeyEvent.VK_LEFT:
				state = left;
				direction = 3;
				stand = false;
				// System.out.println("left");
				break;
			case KeyEvent.VK_RIGHT:
				state = right;
				direction = 4;
				stand = false;
				// System.out.println("right");
				break;
			case KeyEvent.VK_M:
				skill();
				mg.safeBox(gx, gy);
				System.out.println("attack");
				break;
			case KeyEvent.VK_SPACE:
				skill();
				break;
			case KeyEvent.VK_1:
				if (speed < 10)
					speed++;
				break;
			case KeyEvent.VK_2:
				if (speed > 4)
					speed--;
				break;

			}
		}
	};

	KeyAdapter ka1 = new KeyAdapter() {// ************************************2P操作
		@Override
		public void keyReleased(KeyEvent e) {
			BufferedImage[] rs = null;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				rs = back;
				break;
			case KeyEvent.VK_S:
				rs = face;
				break;
			case KeyEvent.VK_A:
				rs = left;
				break;
			case KeyEvent.VK_D:
				rs = right;
				break;
			}
			if (rs == state && !isDead && !isDying) {
				imgnow = state[5];
				stand = true;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			/*
			 * if(isDead) refresh();
			 */

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (isDead || isDying)
				return;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				state = back;
				direction = 1;
				stand = false;
				// System.out.println("up");
				break;
			case KeyEvent.VK_S:
				state = face;
				direction = 2;
				stand = false;
				// System.out.println("down");
				break;
			case KeyEvent.VK_A:
				state = left;
				direction = 3;
				stand = false;
				// System.out.println("left");
				break;
			case KeyEvent.VK_D:
				state = right;
				direction = 4;
				stand = false;
				// System.out.println("right");
				break;
			case KeyEvent.VK_H:
				skill();
				// System.out.println("attack");
				break;
			case KeyEvent.VK_CONTROL:
				skill();
				break;
			case KeyEvent.VK_1:
				speed++;
				break;
			case KeyEvent.VK_2:
				if (speed >= 2)
					speed--;
				break;

			}
		}
	};

	KeyAdapter ka2 = new KeyAdapter() {// *************************************网络发送到主机
		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				ic.sendMessage(ID + ":" + "10");
				break;
			case KeyEvent.VK_DOWN:
				ic.sendMessage(ID + ":" + "20");
				break;
			case KeyEvent.VK_LEFT:
				ic.sendMessage(ID + ":" + "30");
				break;
			case KeyEvent.VK_RIGHT:
				ic.sendMessage(ID + ":" + "40");
				break;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			if (isDead)
				refresh();

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (isDead || isDying)
				return;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				ic.sendMessage(ID + ":" + "1");
				// System.out.println("up");
				break;
			case KeyEvent.VK_DOWN:
				ic.sendMessage(ID + ":" + "2");
				// System.out.println("down");
				break;
			case KeyEvent.VK_LEFT:
				ic.sendMessage(ID + ":" + "3");
				// System.out.println("left");
				break;
			case KeyEvent.VK_RIGHT:
				ic.sendMessage(ID + ":" + "4");
				// System.out.println("right");
				break;
			case KeyEvent.VK_A:
				System.out.println("attack");
				break;
			case KeyEvent.VK_SPACE:
				// System.out.println("jump");
				ic.sendMessage(ID + ":" + "11");
				break;
			case KeyEvent.VK_1:
				ic.sendMessage(ID + ":" + "12");
				break;
			case KeyEvent.VK_2:
				ic.sendMessage(ID + ":" + "13");
				break;

			}
		}
	};

	// *******************************************************AI相关****************************************
	/**
	 * 移动到指定格子
	 * 
	 * @param x
	 * @param y
	 */
	public Boolean moveTo(final int t) {
		int x = 0, y = 0;
		switch (t) {
		case 4:
			x = gx;
			y = gy - 1;
			System.out.println("向上走到" + x + ":" + y);
			break;
		case 2:
			x = gx;
			y = gy + 1;
			System.out.println("向下走到" + x + ":" + y);
			break;
		case 1:
			y = gy;
			x = gx - 1;
			System.out.println("向左走到" + x + ":" + y);
			break;
		case 3:
			y = gy;
			x = gx + 1;
			System.out.println("向右走到" + x + ":" + y);
			break;
		}
		int time = 0;
		boolean finish = false;
		while (!finish) {
			if ((gx != rx && gy != ry && myPanel.map.map[y][x] != 0) || isDead
					|| isDying || time >= 100)
				return false;
			try {
				Thread.sleep(10);
				time++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (t) {
			case 4:
				move("1");
				finish = (this.y + 30) <= y * p.size;
				break;
			case 2:
				move("2");
				finish = (this.y + 30) >= y * p.size;
				break;
			case 1:
				move("3");
				finish = (this.x + 20) <= x * p.size;
				break;
			case 3:
				move("4");
				finish = (this.x + 20) >= x * p.size;
				break;
			}
		}
		switch (t) {
		case 4:
			stop("10");
			break;
		case 2:
			stop("20");
			break;
		case 1:
			stop("30");
			break;
		case 3:
			stop("40");
			break;
		}
		System.out.println("完成一步");
		return true;

	}

	Thread ai = new Thread(new Runnable() { // AI行动线程*******************************************************

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int order = 1;
					while (true) {

						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (isDead || isDying)
							continue;
						//

						Man em;
						if (!Man.this.myPanel.people.isEmpty())
							em = Man.this.myPanel.people.get(0);
						else
							em = Man.this;
						// 找到主角
						// System.out.println(em.gx+":"+em.gy);//主角坐标输出
						int a[] = mg.aa(gy, gx, em.gy, em.gx);// 获取路径
						int l = (a == null ? 0 : a.length);// 路径长度

						int sp[] = mg.safeBox(gy, gx);// 最近安全路径
						int sl = (sp == null ? 0 : sp.length);// 路径长度

						if (em.isDead || em.isDying || l == 0)// 敌人已死亡
						{
							order = 2;
						}

						if (sl != 0 || l == 0)// 不安全或者无法找到地方路径时逃跑
							order = 2;

						//System.out.println("命令(" + order + ")");
						Boolean shot = true;
						for (int i = 0; i < l; i++) {
							if (i != 0 && a[i] != a[i - 1])
								shot = false;
						}
						if (mg.isSafe(em))
							order = 1;
						//
						switch (order) {// 判断指令*******************************
						case 1:// 前往玩家所在位置
							if (l == 0)
								break;
							if (shot)
								skill();
							for (int i = 0; i < l && i == 0; i++) {
								//System.out.println("攻击");

								if (!moveTo(a[i])) {
									//System.out.println("被挡住");
								}

							}
							if (l != 0) {
								{
									// System.out.println("到达指定地点");
									//System.out.println("可能被挡住或已经到达目标");
								}
							}
							order = 2;// 每走一步都判断是否安全
							break;
						case 2:// 寻找安全位置
							for (int i = 0; i < sl; i++) {

								//System.out.println("逃跑");
								if (!moveTo(sp[i])) {
									// System.out.println("被挡住");
									// break;
									order = 2;

								}

							}
							if (sl == 0) {
								//System.out.println("没有安全位置");
								order = 1;
							}

							break;
						case 3:// 攻击
							skill();
							break;
						case 4:// 逃跑远离玩家
							break;
						case 5:// 自由开采道具
							break;
						}

					}
				}

			});
	private InetController ic;

	// /**********************************************************************************************************
	public Man(int x, int y, int type, Panel panel, InetController ic) {
		panel.ic=ic;
		this.ic = ic;
		this.myPanel = panel;
		this.allBall = panel.allBall;
		this.allWeapon = panel.allWeapon;
		this.p = panel.map;
		this.type = type;
		this.x = ix = x;
		this.y = iy = y;
		this.mg = new MiGong(myPanel, Man.this);
		init();
		gx = (x + 50) / 60;
		gy = (y + 60) / 60;
		switch (type) {
		case 0:// 单机1P
			ka = ka0;
			name="Tom";
			break;
		case 1:// 单机2P
			ka = ka1;
			name="Jerry";
			break;
		case 2:// 网络1P
			ka = ka2;
			name="LonelyYou";
			break;
		case 3:// 网络其他玩家控制
			ka = null;
			name="Player"+ID;
			break;
		case 4:// AI
			ka = null;
			ai.start();
			break;
		}

	}
    //重生
	public void refresh() {
		// System.out.println("重生");
		x = ix;
		y = iy;
		gx = (x + 50) / 60;
		gy = (y + 60) / 60;
		imgnow = face[5];
		direction = 2;
		stand = true;
		isDying = false;
		isDead = false;
	}
   //被攻击调用
	public void beAttack() {
		if (isDead || isDying)
			return;
		isDying = true;
		System.out.println("被打倒了~");
		imgnow = dying[0];
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println("死了~");
				isDying = false;
				isDead = true;
				deadNum++;
				if (deadNum >= 3) {// 死亡三次游戏结束
					isOver = true;
					switch (type) {
					case 0:// 单机1P
						myPanel.gameOver(0);
						return;
					case 1:// 单机2P
						myPanel.gameOver(1);
						return;
					case 2:// 网络1P
						myPanel.gameOver(0);
						return;
					case 3:// 网络其他玩家控制
						myPanel.gameOver(2);
						return;
					case 4:// AI
						myPanel.gameOver(3);
						return;
					}
				}
				imgnow = dead[0];
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Man.this.refresh();// 复活
			}
		}).start();

	}
    
	public void beTouch(Man m) {
		if (!isDying)
			return;
		// if(m是队友)
		// isDying=false;
		// else
		// {isDying=false;
		// isDead=true; }
	}
   /*
    * 地图碰撞检测 （实现难度之一）
    * 
    */
	private boolean isTouch2(int x, int y) {
		x += 20;
		y += 30;
		int[][] mg;
		if (ddd)// 如果角色在泡泡上
		{
			mg = new int[p.map.length][p.map[0].length];// 复制一份地图
			for (int i = 0; i < p.map.length; i++) {
				for (int j = 0; j < p.map[0].length; j++) {
					mg[i][j] = p.map[i][j];
				}
			}
			/*
			 * for(int [] a:stuckList){ mg[a[1]][a[0]]=0;//取消角色下的泡泡碰撞 }
			 */
			mg[ry][rx] = 0;// 取消角色下的泡泡碰撞

		} else {
			mg = p.map;
			rx = 1000;
			ry = 1000;
		}
		if (x < 0 || y < 0 || x > p.m2 * p.size - p.size
				|| y > p.m1 * p.size - p.size) {
			// System.out.println("走出界外！");
			return true;
		}

		if (x % p.size == 0 && y % p.size == 0) {
			if (mg[y / p.size][x / p.size] != 0) { // System.out.println("不可能情况啊！");
				return true;
			}
		} else if (x % p.size == 0) {
			if (mg[y / p.size][x / p.size] != 0
					|| mg[y / p.size + 1][x / p.size] != 0) {// System.out.println("垂直有障碍！");
				return true;
			}
		} else if (y % p.size == 0) {
			if (mg[y / p.size][x / p.size] != 0
					|| mg[y / p.size][x / p.size + 1] != 0) {// System.out.println("水平有障碍！");
				return true;
			}
		} else if (mg[y / p.size][x / p.size] != 0
				|| mg[y / p.size + 1][x / p.size] != 0
				|| mg[y / p.size][x / p.size + 1] != 0
				|| mg[y / p.size + 1][x / p.size + 1] != 0) {
			switch (direction) {
			case 1:
				if (mg[y / p.size][x / p.size] == 0 && x % p.size <= p.size / 2) {
					this.x--;
				} else if (mg[y / p.size][x / p.size + 1] == 0
						&& x % p.size >= p.size / 2) {
					this.x++;
				}
				break;
			case 2:
				if (mg[y / p.size + 1][x / p.size] == 0
						&& x % p.size <= p.size / 2) {
					this.x--;
				} else if (mg[y / p.size + 1][x / p.size + 1] == 0
						&& x % p.size >= p.size / 2) {
					this.x++;
				}
				break;
			case 3:
				if (mg[y / p.size][x / p.size] == 0 && y % p.size <= p.size / 2) {
					this.y--;
				} else if (mg[y / p.size + 1][x / p.size] == 0
						&& y % p.size >= p.size / 2) {
					this.y++;
				}
				break;
			case 4:
				if (mg[y / p.size][x / p.size + 1] == 0
						&& y % p.size <= p.size / 2) {
					this.y--;
				} else if (mg[y / p.size + 1][x / p.size + 1] == 0
						&& y % p.size >= p.size / 2) {
					this.y++;
				}
				break;
			}
			beStuck = true;
			return true;
		}
		return false;
	}

	private void init() {
		face = myPanel.RR.face;
		back = myPanel.RR.back;
		left = myPanel.RR.left;
		right = myPanel.RR.right;
		dead = myPanel.RR.dead;
		dying = myPanel.RR.dying;
		//mp = myPanel.RR.mp;
		state = face;
		imgnow = state[5];
		stand = true;
		t.start();
	}
/*
 * 人物线程
 * 实现移动和各种相关判定
 * 实现移动方向水平纠正 （使操作顺畅）
 */
	Thread t = new Thread(new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			int i = 6;

			while (true) {
				gx = (x + 50) / 60;//玩家所在方块的x坐标
				gy = (y + 60) / 60;//玩家所在方块的y坐标
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (p.map[gy][gx] == -1)// 判定是否在水泡中
				{
					rx = gx;
					ry = gy;
				}
				ddd = (Math.abs(rx * p.size - (x + 20)) < p.size)
						&& (Math.abs(ry * p.size - (y + 30)) < p.size);// 是否在水泡中
				if (stand || isDying || isDead)
					continue;
				//if(myPanel.TerminalMode==2)
				switch (direction) {
				case 1:
					if (!isTouch2(x, y - 1)
							|| (isTouch2(x, y) && gy >= 1 && p.map[gy - 1][gx] == 0))// 如果正处于障碍中卡住则可以往空地移动
																						// 下面同理
						y -= 1;
					break;
				case 2:
					if (!isTouch2(x, y + 1)
							|| (isTouch2(x, y) && gy + 1 < p.m1 && p.map[gy + 1][gx] == 0))
						y += 1;
					break;
				case 3:
					if (!isTouch2(x - 1, y)
							|| (isTouch2(x, y) && gx >= 1 && p.map[gy][gx - 1] == 0))
						x -= 1;
					break;
				case 4:
					if (!isTouch2(x + 1, y)
							|| (isTouch2(x, y) && gx + 1 < p.m2 && p.map[gy][gx + 1] == 0))
						x += 1;
					break;
				}
				gx = (x + 50) / 60;
				gy = (y + 60) / 60;
				i++;
				if (!isDying && !isDead)
					imgnow = state[i / speed / 2 % 6];
			}
		}
	});
}
