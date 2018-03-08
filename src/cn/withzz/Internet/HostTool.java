package cn.withzz.Internet;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.withzz.game.Controller;
import cn.withzz.game.Man;
import cn.withzz.game.Panel;
import cn.withzz.game.Partner;

/**
 * 
 * 具体实现房主主机的网络数据的接收发送和处理消息
 * 
 */
public class HostTool implements InetController {
	public List<Partner> users = new ArrayList<Partner>();
	IManager im;

	static enum STATE {
		REDYING, REDY, GAMING, OVER
	};

	STATE state = STATE.REDYING;
	Panel gamePanel;
	IDgroup ig = new IDgroup();
	Partner myself;
	int hostPort = 4103;//服务器监听此端口
	
	Controller ctl = new Controller() {
		@Override
		public void recive(String m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			System.out.println("主机收到" + m+"  state="+state);
			switch (state) {
			case REDYING:
				switch (m) {
				case "leave":// 处理有人离开房间
					for (Partner p : users) {
						if (p.getIp().equals(dp.getAddress())) {
							users.remove(p);
							System.out.println(dp.getAddress() + "leave");
						}
					}
					break;
				case "join":// 处理有人进入房间
					boolean add = true;
					for (Partner p : users) {// 查询是否已经在房间中
						if (p.getIp().equals(dp.getAddress()))
							add = false;
					}
					if (add) {
						users.add(new Partner(dp.getAddress(), dp.getPort()));
						System.out.println(dp.getAddress()+":"+dp.getPort()+ "join in~");
					}
					break;
				}
				break;
			case REDY:
				break;
			case GAMING:// 游戏中
				String a[] = m.split(":");
				if (a[0].equals("gamestart"))
					break;
				
				shanbo(handle(m));// 处理接收到的消息后组播到所有人
				break;
			case OVER:
				break;
			}
		}

	};

	public String handle(String m) {// 游戏进行时的数据处理
		String[] mm = m.split(":");
		for (Man man : getMans()) {
			if ((man.getID() + "").equals(mm[0])) {
				m = m + ":" + man.getX() + ":" + man.getY();
				man.ctl.recive(mm[1], null);
				System.out.println("处理" + m);
				JSONObject jo=	new JSONObject();
				jo.put("op","userop");
				jo.put("id",man.getID());
				jo.put("opcode",mm[1]);
				return jo.toString();
			}
		}
		return null;
	}

	public void setState(STATE s) {
		state = s;
	}

	/**
	 * 返回主机接受的玩家网络信息列表
	 * 
	 * @return
	 */
	public List<Partner> getPartners() {
		System.out.println("伙伴列表: ");
		for (Partner p : users) {
			System.out.println(p.getIp() + "~");
		}
		return users;
	}

	public HostTool() {
		im = new IManager(ctl, hostPort);
	}

	public void startWork() {
		state = STATE.REDYING;
		im.startRecive();// 开始接受局域网中单播
	}

	public void stopWork() {
		state = STATE.OVER;
		im.stopRecive();// 停止接受单播
	}

	/**
	 * 告知所有小伙伴玩家列表
	 */

	public void fenpei() {
		String message = "";
		for (Partner p : users) {
			Man m = p.getMan();
			message += m.getID() + ":" + m.getX() + ":" + m.getY() + "/";
		}
		System.out.println("群发:" + message);
		shanbo(message);
	}

	/**
	 * 主机创建所有人物对象 并分配ID
	 */
	public void createMans() {
		for (Partner p : users) {
			int id = ig.getID();
			Man man;
//			if (p.equals(myself))
//				man = new Man(gamePanel.map.bothPlace[id - 1][0] * 60 - 20,
//						gamePanel.map.bothPlace[id - 1][1] * 60 - 30, 2,
//						gamePanel, this);
//			else
				man = new Man(gamePanel.map.bothPlace[id - 1][0] * 60 - 20,
						gamePanel.map.bothPlace[id - 1][1] * 60 - 30, 3,
						gamePanel, this);
			man.setID(id);
			p.setMan(man);
			System.out
					.println("分配ID " + p.getMan().getID() + "角色到" + p.getIp());
		}
	}

	/**
	 * 获得创建的对应室友用户的角色对象列表
	 * 
	 * @return
	 */

	public List<Man> getMans() {
		List<Man> a = new ArrayList<Man>();
		for (Partner p : users) {
			a.add(p.getMan());
		}
		return a;
	}

	/**
	 * 发送M字符串到所有小伙伴
	 * 
	 * @param m
	 */
	public void shanbo(String m) {
		for (Partner p : users) {
			System.out.println("发送:" + m+"到"+p.getIp());
			im.send(m, p);
		}
		System.out.println("广播" + m);
	}
	Thread spreadData =new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(state.equals(STATE.GAMING)){
				try {
					Thread.sleep(300);
					JSONArray ja=new JSONArray();
					for (Partner p : users) {
						JSONObject a=new JSONObject();
						a.put("x",p.getMan().getX());
						a.put("y", p.getMan().getY());
						ja.put(a);
					}
					JSONObject jo=new JSONObject();
					jo.put("op", "seat");
					jo.put("data",ja);
					shanbo(jo.toString());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	});
	/**
	 * 开始游戏
	 */
	public void startGame(Panel pl) {
		gamePanel = pl;
		createMans();
		fenpei();
		setState(STATE.GAMING);
		for (Partner p : users) {
			if (!p.equals(myself))
				im.send("gamestart:" + p.getMan().ID, p);
		}
		
		spreadData.start();
	}

	@Override
	public void sendMessage(String m) {
		// TODO Auto-generated method stub
		//im.send(m, myself);// 主机给自己发送命令
		shanbo(m);
		
	}
}

/**
 * 用于依次分配房间用户的唯一ID
 * 
 * @author Administrator
 * 
 */
class IDgroup {
	boolean[] used = { false, false, false, false };

	public int getID() {
		for (int i = 0; i < 4; i++)
			if (!used[i]) {
				used[i] = true;
				return i + 1;
			}
		return -1;
	}

	public void logoutID(int id) {
		used[id - 1] = false;
	}

}
