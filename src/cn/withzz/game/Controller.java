package cn.withzz.game;
import java.net.DatagramPacket;
/*
 * 控制器接口 便于在JPanel中实现后接受网络控制数据
 */
public abstract class Controller {
	public abstract void recive(String m, DatagramPacket dp);
}
