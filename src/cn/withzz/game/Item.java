package cn.withzz.game;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.json.JSONObject;

/**
 * 
 * 道具类
 * 炸开游戏内可破坏砖块可以爆出道具
 * 玩家触碰可以拾取获得能力或者属性
 */
public class Item {
Panel p;
int ID=1;
Boolean working=true;
int gx,gy;
BufferedImage img;
int size;
/*
 * 道具被拾取调用
 */
private void BePick(Man m){
	System.out.println(m.ID+"碰到"+ID+"号道具");
	working=false;
	switch(ID){
	case 1:
	case 2:
		m.setSkill(this.ID);
		break;
	case 3:
		m.setMax(m.getMax()+1);
		break;
	case 4:
		m.setSpeed(m.getSpeed()-1);
		break;
	case 5:
		m.setPower(m.getPower()+1);
		break;
	}
	p.allItem.remove(this);
}
/*
 * 道具类构造函数
 * 设置坐标和显示的Panel
 */
public Item(Panel p,int x,int y){
	if(p.TerminalMode==1)
		return;
	ID=(int) (Math.random()*5+1);
	this.img=p.RR.item[ID-1];
	this.p=p;
	this.gx=x;
	this.gy=y;
	this.size=p.map.size;
	p.allItem.add(this);
	t.start();
	sendToClient();
}
public void sendToClient(){
	JSONObject jo=new JSONObject();
	jo.put("op","item");
	jo.put("x",gx);
	jo.put("y",gy);
	jo.put("id",ID);
	//p.ic.sendMessage("set:item-"+x+"/"+y+"/"+ID);
	p.ic.sendMessage(jo.toString());
}
public Item(Panel p,int x,int y,int type){
	ID=type;
	this.img=p.RR.item[ID-1];
	this.p=p;
	this.gx=x;
	this.gy=y;
	this.size=p.map.size;
	p.allItem.add(this);
	t.start();
}
/*
 * 实现绘制
 */
public void drawItem(Graphics g){
	if(working)
	g.drawImage(img, gy*size, gx*size, size, size,p);
}
/*
 * 监听是否被拾取（与玩家触碰即被拾取）
 */
Thread t=new Thread(new Runnable(){
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(working){
			try {
    			Thread.sleep(100);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
			for(Man m:p.people){
				if(!m.isDead&&m.gx==gy&&m.gy==gx){
					BePick(m);
				}
			}
		}
	}
});	

}
