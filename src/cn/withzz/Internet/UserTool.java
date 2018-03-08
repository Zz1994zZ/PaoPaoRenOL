package cn.withzz.Internet;
import java.awt.Color;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.withzz.game.Controller;
import cn.withzz.game.Item;
import cn.withzz.game.Man;
import cn.withzz.game.Map;
import cn.withzz.game.Panel;
import cn.withzz.game.Partner;
import cn.withzz.game.ReadResource;

/**
 * 
 * 普通用户（区别于主机用户）发送网络消息专用类
 * 
 */
public class UserTool implements InetController {
	List<Man> users = new ArrayList<Man>();
	public List<Host> hostGroup = new ArrayList<Host>();
	IManager im;

	static enum STATE {
		REDYING, REDY, GAMING, OVER
	};

	STATE state = STATE.REDYING;
	Panel gamePanel;
	String msg;
	Host host;
	int myID;
	public int  userPort =4003,hostPort = 4103;
	String MulticastIp = "224.0.2.4";
	public void setHost(Host host){
		this.host=host;
	}
	/**
	 * 接收消息
	 */
	Controller ctl = new Controller() {

		@Override
		public void recive(String m, DatagramPacket dp) {
			// TODO Auto-generated method stub
			System.out.println("收到" + m);

			switch (state) {
			case REDYING:
				String a[] = m.split(":");
				if (a[0].equals("gamestart")) {
					myID = Integer.valueOf(a[1]);
					setState(STATE.GAMING);
					System.out.println("收到游戏开始!获取ID" + myID);
					startGame();
				} else {
					msg = m;
				}
				break;
			case REDY:
				break;
			case GAMING:
				
				handle(m);
				
				break;
			case OVER:
				break;
			}
		}

	};

	public UserTool() {
		im = new IManager(ctl, userPort);//用户端口未知  填写0让IManger自行选择
	}

	/*
	 * 处理服务器设置相关数据
	 */
	private void set(String m){
		String[] s=m.split("-");
		String data=s[1];
		switch(s[0]){
		case "map":
			System.out.println("map"+data);
			gamePanel.map.setMapBy(data);
			break;
		case "item":
			System.out.println("item"+data);
			String[] seat=data.split("/");
			int x=Integer.valueOf(seat[0]);
			int y=Integer.valueOf(seat[1]);
			int type=Integer.valueOf(seat[2]);
			System.out.println("放道具!"+x+","+y);
			new Item(gamePanel,x,y,type);
			break;
		case "seat":
			System.out.println("seat"+data);
			String[] seat2=data.split("+");
			for (int i=0;i<seat2.length;i++) {
				String[] ss=seat2[i].split("/");
				users.get(i).setSeat(Integer.valueOf(ss[0]),Integer.valueOf(ss[1]));
			}
			break;
		
		}
	}
	/**
	 * 开始工作
	 */
	public void startWork() {
		im.startRecive();// 开始接受单薄消息
		this.setState(STATE.REDYING);// 设置状态为在准备中
	}

	/**
	 * 加入房间
	 */
	public void joinHouse() {

	}

	/**
	 * 停止工作
	 */
	public void stopWork() {
		setState(STATE.OVER);
		im.stopRecive();// 停止一切接收，关闭端口
	}

	/**
	 * new出所需的Man对象放到user列表
	 */
	public void createMan() {
		String[] a = msg.split("/");
		String[][] b = new String[a.length][];
		for (int i = 0; i < a.length; i++) {
			b[i] = a[i].split(":");
		}
		for (int i = 0; i < b.length; i++) {
			int id = Integer.valueOf(b[i][0]);
			Man m;
			if (id == myID)// 设置本人Man type为2即网络1p
				m = new Man(Integer.valueOf(b[i][1]), Integer.valueOf(b[i][2]),
						2, gamePanel, this);
			else
				// 其他网络断type为3
				m = new Man(Integer.valueOf(b[i][1]), Integer.valueOf(b[i][2]),
						3, gamePanel, this);
			m.setID(id);
			users.add(m);
		}
	}

	/**
	 * 获取玩家
	 * 
	 * @return
	 */
	public List<Man> getMans() {
		return users;
	}

	/**
	 * 向主机发送字符串消息M
	 * 
	 * @param m
	 */
	public void sendM(String m) {
		System.out.println("主机在：" + host.getIp() + ":" + host.getPort());
		im.send(m, new Partner(host.getIp(), hostPort));
		System.out.println("向主机发送" + m);
	}

	/**
	 * 处理主机传送的数据
	 * 
	 * @param m
	 */
//	public void handle(String m) {// 游戏进行时的玩家操作数据处理
//		String[] mm = m.split(":");
//		for (Man man : users) {
//			if ((man.getID() + "").equals(mm[0])) {
//				man.setSeat(Integer.valueOf(mm[2]), Integer.valueOf(mm[3]));
//				man.ctl.recive(mm[1], null);
//				break;
//			}
//		}
//	}
	public void handle(String m) {// 游戏进行时的玩家操作数据处理
		JSONObject ja=new JSONObject(m);
		switch(ja.getString("op"))
		{
		case "map":
			gamePanel.map.setMapBy(ja.getString("map"));
			break;
		case "item":
			int x=ja.getInt("x");
			int y=ja.getInt("y");
			int type=ja.getInt("id");
			System.out.println("放道具!"+x+","+y);
			new Item(gamePanel,x,y,type);
			break;
		case "seat":
			JSONArray jarray=ja.getJSONArray("data");
			JSONObject jo;
			for (int i=0;i<jarray.length();i++) {
				jo=jarray.getJSONObject(i);
				users.get(i).setSeat(jo.getInt("x"),jo.getInt("y"));
			}
			break;
		case "userop":
			int id=ja.getInt("id");
			String opcode=ja.getString("opcode");
			users.get(id-1).ctl.recive(opcode, null);
			break;
		}
	
	}
	

	/**
	 * 开始进行游戏
	 */
	public void startGame() {
		System.out.println("开始！");
		ReadResource r = new ReadResource();
		r.init();
		gamePanel = new Panel(r, Map.map2, Map.bothPlace2,1);
		createMan();
		JFrame F = new JFrame();
		gamePanel.setPeople(getMans());
		F.setSize(gamePanel.map.m2 * 60 + 17, gamePanel.map.m1 * 60 + 40);
		F.add(gamePanel);
		F.setBackground(Color.white);
		F.setLocationRelativeTo(null);
		F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		F.setVisible(true);
		setState(STATE.GAMING);
	}

	public void setState(STATE s) {
		state = s;
	}

	/**
	 * 重写InetController的发送消息方法在键盘操作时调用发送消息到主机
	 */
	@Override
	public void sendMessage(String m) {
		// TODO Auto-generated method stub
		sendM(m);// 客户端发送数据到主机
	}
}
