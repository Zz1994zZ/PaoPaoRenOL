package cn.withzz.game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 泡泡
 * 实现泡泡的初始化 爆炸效果  引爆延时  
 * 
 */
public class Ball {
	public final int x;
	public final int y;
	public final int power;
	BufferedImage imgnow;
	int state = 0;
	int ul, dl, ll, rl;// 上下左右爆炸范围，m1垂直地图格数，m2水平地图格数
	public int m1;
	public int m2;
	List<Ball> b;
	Graphics g;
	Man owner;
	BufferedImage[] ball;
	Map m;
/*
 * 泡泡类构造函数
 * 需指定泡泡的施放者 以此确定泡泡的位置和威力等属性
 */
	public Ball(Man m) {
		this.x = m.gx;
		this.y = m.gy;
		this.power = m.getPower();
		this.b = m.allBall;
		this.m = m.p;
		this.owner = m;
		m1 = this.m.m1;
		m2 = this.m.m2;
		ball = owner.myPanel.RR.ball;
		for (Man mm : m.myPanel.people) {// 检测是否有角色在该泡泡上
			if ((Math.abs(x * this.m.size - (mm.getX() + 20)) < this.m.size)
					&& (Math.abs(y * this.m.size - (mm.getY() + 30)) < this.m.size)) {
				mm.rx = x;
				mm.ry = y;
				// System.out.println("在球里m~");
				mm.stuckList.add(new int[] { x, y });
			}// 设置该角色可逃离的坐标
		}
		this.m.map[y][x] = -1;// 设置地形类型为-1
		t.start();
	}
/*
 * 泡泡类第二种构造函数
 * 用于实现各种伤害类道具效果
 */
	public Ball(int x, int y, int p, List<Ball> b, Man m) {
		this.x = x;
		this.y = y;
		this.power = p;
		this.b = b;
		this.m = m.p;
		this.owner = m;
		m1 = this.m.m1;
		m2 = this.m.m2;
		;
		ball = owner.myPanel.RR.ball;
	}

	static Ball setBall(Man m) {
		if (m.myPanel.map.map[m.gy][m.gx] == -1)// 扫描放置位置上是否已有泡泡
			return null;
		return new Ball(m);
	}
/*
 * 爆炸
 * 扫描爆炸范围内是否有其他泡泡 或者可被引爆单位 扫描到后加入到引爆列表递归返回后全部引爆
 */
	public List<Ball> bomb() {
		if (state == 1)// 若正处于爆炸状态则直接返回不重复爆炸
			return null;
		// new MusicPlayer("voice/34312.mid");//水泡爆炸音效
		// owner.mp.play();
		m.map[y][x] = 0;
		state = 1;// 设置状态为正在爆炸
		int destoryBox[][] = new int[4][2];
		int destoryNum = 0;
		for (int i = x + 1; i <= x + power; i++) {// 向右扫描爆炸范围
			if (i >= 0 && i < m2 && y >= 0 && y < m1 && m.map[y][i] == 2) {// m.map[y][i]=0;
				destoryBox[destoryNum][0] = y;
				destoryBox[destoryNum][1] = i;
				destoryNum++;
				rl++;
				break;
			} else if (i >= 0 && i < m2 && y >= 0 && y < m1
					&& (m.map[y][i] == 0 || m.map[y][i] == -1)) {
				rl++;
			} else
				break;
		}
		for (int i = x - 1; i >= x - power; i--) {// 向左扫描爆炸范围
			if (i >= 0 && i < m2 && y >= 0 && y < m1 && m.map[y][i] == 2) {// m.map[y][i]=0;
				destoryBox[destoryNum][0] = y;
				destoryBox[destoryNum][1] = i;
				destoryNum++;
				ll++;
				break;
			} else if (i >= 0 && i < m2 && y >= 0 && y < m1
					&& (m.map[y][i] == 0 || m.map[y][i] == -1)) {
				ll++;
			} else
				break;
		}
		for (int i = y + 1; i <= y + power; i++) {// 向下扫描爆炸范围
			if (x >= 0 && i >= 0 && x < m2 && i < m1 && m.map[i][x] == 2) {// m.map[i][x]=0;
				destoryBox[destoryNum][0] = i;
				destoryBox[destoryNum][1] = x;
				destoryNum++;
				dl++;
				break;
			} else if (x >= 0 && i >= 0 && x < m2 && i < m1
					&& (m.map[i][x] == 0 || m.map[i][x] == -1)) {
				dl++;
			} else
				break;
		}
		for (int i = y - 1; i >= y - power; i--) {// 向上扫描爆炸范围
			if (x >= 0 && i >= 0 && x < m2 && i < m1 && m.map[i][x] == 2) {// m.map[i][x]=0;
				destoryBox[destoryNum][0] = i;
				destoryBox[destoryNum][1] = x;
				destoryNum++;
				ul++;
				break;
			} else if (x >= 0 && i >= 0 && x < m2 && i < m1
					&& (m.map[i][x] == 0 || m.map[i][x] == -1)) {
				ul++;
			} else
				break;
		}
		final List<Ball> delList = new ArrayList<Ball>();// 建立引爆的泡泡列表
		for (Man mmm : m.people) {// 扫描角色是否被炸到
			if (!mmm.isDead
					&& ((mmm.gx >= x - ll && mmm.gx <= x + rl && mmm.gy == y) || (mmm.gy >= y
							- ul
							&& mmm.gy <= y + dl && mmm.gx == x)))
				mmm.beAttack();
		}
		List<Ball> cb = new ArrayList<Ball>();
		cb.addAll(b);
		for (Ball bbb : cb) {// 扫描被引爆的泡泡并引爆
			if (bbb != this
					&& ((bbb.x >= x - ll && bbb.x <= x + rl && bbb.y == y) || (bbb.y >= y
							- ul
							&& bbb.y <= y + dl && bbb.x == x))
					&& bbb.state != 1) {
				delList.addAll(bbb.bomb());
				delList.add(bbb);
			}
		}
		for (int i = 0; i < destoryNum; i++) {// 将爆炸波及的砖墙设置为0状态
			m.beBomb(destoryBox[i][0], destoryBox[i][1]);
		}
		delList.add(this);// 将本泡泡加入删除列表
		owner.ball.remove(Ball.this);// 删除角色泡泡列表中本泡泡
		return delList;
	}

	Thread t = new Thread(new Runnable() {// 泡泡自主爆炸线程
				@Override
				public void run() {
					// TODO Auto-generated method stub

					for (int j = 0; j < 3 && state == 0; j++) {
						for (int i = 0; i < 4 && state == 0; i++) {
							try {
								Thread.sleep(250);
								imgnow = ball[i];
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					List<Ball> a = bomb();// 爆炸
					// System.out.println("返回序列"+a);
					if (state == 1)// 显示爆炸效果状态
					{
						try {
							for (int i = 0; i < 5; i++) {
								for (Man mmm : m.people) {// 扫描角色是否被炸到
									if (!mmm.isDead
											&& ((mmm.gx >= x - ll
													&& mmm.gx <= x + rl && mmm.gy == y) || (mmm.gy >= y
													- ul
													&& mmm.gy <= y + dl && mmm.gx == x)))
										mmm.beAttack();
								}
								Thread.sleep(100);
							}

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (a != null && !a.isEmpty()) {
							m.map[y][x] = 0;
							owner.ball.removeAll(a);// 在角色跑泡泡列表中删除爆炸的泡泡
							b.removeAll(a); // 在总泡泡表中删除爆炸的泡泡
						}
					}

				}
			});

}
