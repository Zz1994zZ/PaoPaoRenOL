package cn.withzz.Internet;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import cn.withzz.game.Controller;
import cn.withzz.game.Partner;

/**
 * 
 * 管理UDP通讯的对象 并实现接收发送 将消息传递给Controller处理
 * 
 */
public class IManager {
	List<Partner> partner = new ArrayList<Partner>();
	Controller ctr;
	DC d;

	public IManager(Controller ctr, int dcPort) {
		this.ctr = ctr;
		d = new DC(this, dcPort);
	}

	public void send(String word, Partner p) {
		d.sendMessage(word, p);
	}


	public void startRecive() {
		d.startRecive();
	}
	public void stopRecive() {
		d.stopWoking();
	}
}

/**
 * 控制连接
 * 
 * @author Administrator
 * 
 */
/*
 * class CC { ServerSocket s; Socket socket; int port=8888; IManager im; public
 * CC(IManager im,int port){ this.im=im; this.port=port; init(); } void init(){
 * try{ s = new ServerSocket(port); } catch(IOException e){
 * System.out.println("端口申请失败！"); e.printStackTrace(); } } public void recive(){
 * Socket ss=null; try { ss=s.accept(); InputStream op=ss.getInputStream();
 * BufferedReader is=new BufferedReader(new InputStreamReader(op));
 * is.readLine(); } catch (IOException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } } }
 */
/**
 * 数据连接
 * 
 * @author Administrator
 * 
 */
class DC {
	int myPort;
	IManager im;
	Boolean isWoking = true;
	private DatagramSocket dss;
	public DC(IManager im, int port) {
		this.im = im;
		myPort = port;
		try {
			System.out.println("正在申请端口" +myPort);
			dss = new DatagramSocket(myPort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("单播接收套接字创建失败！");
		}
	}

	public void stopWoking() {
		isWoking = false;
	}


	public void sendMessage(final String word, final Partner p) {// 发送UDP单播
		try {
			byte[] buf = word.getBytes();
			InetAddress address = p.getIp();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, address,
					p.getPort());
			
			/*
			 * System.out.println("数据包目标地址："+dp.getAddress()+"\n");
			 * System.out.println("数据包目标端口号："+dp.getPort()+"\n");
			 * System.out.println("数据包长度："+dp.getLength()+"\n");
			 */
			System.out.println("发送数据包长度："+dp.getLength()+"\n");
			dss.send(dp);
			System.out.println("本地端口："+dss.getLocalPort());
		} catch (Exception e) {
			System.out.println("发送失败");

		}

	}

	public void startRecive() {// 开始接收单播
		if(myPort==0)
			return;
		isWoking = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				byte[] buf = new byte[1024*10];
				DatagramPacket dp = new DatagramPacket(buf, buf.length);	
				System.out.println("开始监听端口" +myPort);
				while (isWoking) {
					try {
						// synchronized (isWoking) {
						dss.receive(dp);
						int length = dp.getLength();
						String message = new String(dp.getData(), 0, length);
						System.out.println("\n收到数据是：" + message + "  大小："+length);
						im.ctr.recive(message, dp);
					} catch (Exception e) {
						System.out.println("单播接收失败！");
						e.printStackTrace();
						//isWoking = false;
					}
				}
				while (isWoking && !dss.isClosed())
					dss.close();// 关闭套接字
				System.out.println("成功关闭套接字");
			}

		}).start();

	}
}
