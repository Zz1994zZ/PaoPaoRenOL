package cn.withzz.game;


import java.net.InetAddress;
/**
 * 小伙伴 网络玩家网络信息相关
 * @author Administrator
 *
 */
public class Partner {
private String name="blank";
private InetAddress ip;
private int port;
private Man m;
public Partner(InetAddress ip,int port){
	this.ip=ip;
	this.port=port;
}
/**
 * 设置玩家对应的Man对象
 * @param m
 */
public void setMan(Man m){
	this.m=m;
}
/**
 * 获得玩家对应的Man对象
 * @return
 */
public Man getMan(){
	return m;
}
public void setName(String name){
	this.name=name;
}
public String getName(){
	return name;
}
public void setIp(InetAddress ip){
	this.ip=ip;
}
public InetAddress getIp(){
	return ip;
}
public void setPotr(int port){
	this.port=port;
}
public int getPort(){
	return port;
}
}
