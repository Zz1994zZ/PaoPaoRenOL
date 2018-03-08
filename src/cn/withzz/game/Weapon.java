package cn.withzz.game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 武器类
 * 实现各类武器道具的效果
 *
 */
public class Weapon {
int x,y,gx,gy;
String name;
Man owner;
int speed=2;
int type;//武器类型
int size;
BufferedImage[] face,back,left,right,img;
BufferedImage imgnow,icon;
Boolean alive=true;//存活状态
public void drawWeapon(Graphics g){	
}
public void Bomb(){};
public static Weapon getWeapon(Man owner){//通过任务的武器类型获取到武器对象
	switch(owner.weaponType){
	case 1:
		return new Daodan(owner,owner.direction);
	case 2:
		return new IceArea(owner);
	}
	return null;
}
}
//以下为继承的子类 可扩展
class IceArea extends Weapon{
	int gx,gy;
	public IceArea(Man owner){
		this.owner=owner;
		this.size=owner.myPanel.map.size;
		imgnow=owner.myPanel.RR.item[1];
		type=owner.direction;
		switch(type){
		case 1:
			x=(owner.gx)*size;
			y=gy=owner.gy*size;
			gx=owner.gx;
			gy=owner.gy-3;
			break;
		case 2:
			x=(owner.gx)*size;
			y=gy=(owner.gy)*size;
			gx=owner.gx;
			gy=owner.gy+3;
			break;
		case 3:
			x=(owner.gx)*size;
			gx=owner.gx-3;
			gy=owner.gy;
			break;
		case 4:
			x=(owner.gx)*size;
			gx=owner.gx+3;
			gy=owner.gy;
			break;
		}
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch(type){
				case 1:
					for(int i=0;i<18;i++){
						y-=10;
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case 2:
					for(int i=0;i<18;i++){
						y+=10;
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case 3:
					for(int i=0;i<18;i++){
						float a=x-(gx)*size-90;
						y=gy*size-(int) (120-0.015*a*a);
						x-=10;
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case 4:
					for(int i=0;i<18;i++){
						float a=x-(gx-3)*size-90;
						y=gy*size-(int) (120-0.015*a*a);
						x+=10;
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}

				Bomb();
			}
			
			
		}).start();
	}
	@Override
	public void drawWeapon(Graphics g){	
		 /*Graphics2D a =(Graphics2D)g ;
		 a.rotate(Math.PI/2);*/
		if(alive){

		if((gx>=0)&&(gx<=owner.myPanel.map.m2)&&(gy>=0)&&(gy<=owner.myPanel.map.m1)){
			g.drawLine(gx*size, gy*size,(gx+1)*size,(gy+1)*size);//手雷落地位置标识
			g.drawLine(gx*size, (gy+1)*size,(gx+1)*size,gy*size);
			
			g.drawImage(imgnow, x, y, size,size, owner.myPanel);//手雷飞行轨迹

		}
		}
		
	}
	@Override
	 public void Bomb(){
		if(gx<0||gy<0||gx>=owner.myPanel.map.m2||gy>=owner.myPanel.map.m1||owner.myPanel.map.map[gy][gx]==1)
		{
			owner.weapon.remove(IceArea.this);
			owner.allWeapon.remove(IceArea.this);
			return;
		}
		imgnow=owner.myPanel.RR.wface[4];
		Ball b=new Ball(gx, gy, 0, owner.allBall, owner);
		if(owner.myPanel.map.map[gy][gx]==2&&(int)(Math.random()*4)==0){//在爆炸中心位置放置道具
			if(owner.myPanel.TerminalMode==2)
			 new Item(owner.myPanel,gy,gx);
		 }
		owner.allBall.add(b);
		owner.ball.add(b); 
		List<Ball> a=b.bomb();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    owner.allBall.removeAll(a);
		owner.ball.removeAll(a);
		owner.weapon.remove(IceArea.this);
		owner.allWeapon.remove(IceArea.this);
	  }
}
class Daodan extends Weapon{
	public Daodan(Man owner,int type){
		name="导弹";
		this.type=type;
		this.owner=owner;
		init();
		switch(type){
		case 1:
			img=face;
			x=(int) ((owner.gx-0)*owner.p.size);
			y=(int) ((owner.gy-1)*owner.p.size);
			break;
		case 2:
			img=back;
			x=(int) ((owner.gx-0)*owner.p.size);
			y=(int) ((owner.gy+1)*owner.p.size);
			break;
		case 3:
			img=left;
			x=(int) ((owner.gx-1)*owner.p.size);
			y=(int) ((owner.gy-0)*owner.p.size);
			break;
		case 4:
			img=right;
			x=(int) ((owner.gx+1)*owner.p.size);
			y=(int) ((owner.gy-0)*owner.p.size);
			break;
		}
	
		
		imgnow=img[0];
        t.start();
	}
	Thread t=new Thread(new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(alive){
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Boolean touch=false;
	    		gx=(x)/owner.p.size;
	    		gy=(y)/owner.p.size;
	    		if(gx<0||gy<0||gx>owner.p.map[0].length-1||gy>owner.p.map.length-1){
	    			owner.weapon.remove(Daodan.this);
	    			owner.allWeapon.remove(Daodan.this);
	    			break;
	    		}
	    		for(Man m:owner.myPanel.people){
	    			if(!m.isDead&&m.gx==gx&&m.gy==gy)
	    				touch=true;
	    		}
	    		if(touch||owner.p.map[gy][gx]==-1||owner.p.map[gy][gx]==2/*||gx<=0||gy<=0||gx>=owner.p.map[0].length-1||gy>=owner.p.map.length-1*/){
	    			Bomb();
	    		}
	          switch(type){
	          case 1:
	        	  y--;
	        	  break;
	          case 2:
	        	  y++;
	        	  break;
	          case 3:
	        	  x--;
	        	  break;
	          case 4:
	        	  x++;
	        	  break;  
	          }
				
			}
		}
		
	});
	@Override
	public void drawWeapon(Graphics g){	
		 /*Graphics2D a =(Graphics2D)g ;
		 a.rotate(Math.PI/2);*/
		g.drawImage(imgnow, x, y, owner.p.size,owner.p.size, owner.myPanel);
		if(alive){
			switch(type){
			case 1:
				 for(int i=0;i<30;i++){
						// g.setColor(getColor((int) (Math.random()*10)));
						 g.setColor(Color.red);
						 g.drawOval((int) (x+20+13*Math.random()), (int) (y+45+15*Math.random()), 1, 3);
					 }
				break;
			case 2:
				 for(int i=0;i<30;i++){
						// g.setColor(getColor((int) (Math.random()*10)));
						 g.setColor(Color.red);
						 g.drawOval((int) (x+18+13*Math.random()), (int) (y+15-15*Math.random()), 3, 1);
					 }
				break;
			case 3:
				 for(int i=0;i<30;i++){
						// g.setColor(getColor((int) (Math.random()*10)));
						 g.setColor(Color.red);
						 g.drawOval((int) (x+45+15*Math.random()), (int) (y+25+13*Math.random()), 3, 1);
					 }
				break;
			case 4:
				 for(int i=0;i<30;i++){
						// g.setColor(getColor((int) (Math.random()*10)));
						 g.setColor(Color.red);
						 g.drawOval((int) (x-5+15*Math.random()), (int) (y+25+13*Math.random()), 3, 1);
					 }
				break;
			}
		}
	}
	private void init(){
		face=owner.myPanel.RR.wface;
		back=owner.myPanel.RR.wback;
		left=owner.myPanel.RR.wleft;
		right=owner.myPanel.RR.wright;
	}
	  public Color getColor(int i){
		  switch(i){
		  case 0:
			  return Color.black;
		  case 1:
			  return Color.blue;
		  case 2:
			  return Color.green;
		  case 3:
			  return Color.red;
		  case 4:
			  return Color.orange;
		  case 5:
			  return Color.white;
		  case 6:
			  return Color.yellow;
		  case 7:
			  return Color.GRAY;
		  case 8:
			  return Color.pink;
		  case 9:
			  return Color.cyan;

		  }
	return Color.black;
	  }
		@Override
	  public void Bomb(){
			if(alive)
			alive=false;
			else 
				return;
			new Thread(new Runnable(){

				@Override
				public void run() {
					Ball b=new Ball(gx, gy, 0, owner.allBall, owner);
					if(owner.myPanel.map.map[gy][gx]==2&&(int)(Math.random()*4)==0){//在爆炸中心位置放置道具
						if(owner.myPanel.TerminalMode==2)
						new Item(owner.myPanel,gy,gx);
					 }
					owner.allBall.add(b);
					owner.ball.add(b); 
					List<Ball> a=b.bomb();
					// TODO Auto-generated method stub
					for(int i=0;i<5;i++){
						try {
							imgnow=img[i];
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					owner.allBall.removeAll(a);
					owner.ball.removeAll(a);
	    			owner.weapon.remove(Daodan.this);
					owner.allWeapon.remove(Daodan.this);
					}
			}).start();
	  }
	
}
