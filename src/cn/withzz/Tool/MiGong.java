package cn.withzz.Tool;
import java.util.ArrayList;
import java.util.List;

import cn.withzz.game.Ball;
import cn.withzz.game.Man;
import cn.withzz.game.Panel;



/**
 * 迷宫求解算法实现
 * 
 *
 */
public class MiGong {


Panel p;
Man man;
/* 上向左右返回值
 *    4
 * 1     3
 *    2
*/

public MiGong(Panel pl,Man m){
	this.p=pl;
	this.man=m;
}
/**
 * 	            {0,1,1,0,1},
				{0,0,0,0,0},
				{1,1,0,1,1},
				{1,0,0,1,0},
				{1,0,0,0,0},	
 */
/**
 * 判断是否安全
 * @param m
 * @return
 */
public boolean isSafe(Man m){
	int ymg[][]=p.map.map;
	int [][]mg=new int [ymg.length][ymg[0].length];
	for(int i=0;i<ymg.length;i++){
		for(int j=0;j<ymg[0].length;j++){
			mg[i][j]=ymg[i][j];
		}
	}
   List <Ball>bl=new ArrayList<Ball>();
   bl.addAll(p.allBall);
	for(Ball b:bl){//标记地图中危险方块 
		int x=b.x,y=b.y,power=b.power,m1=b.m1,m2=b.m2;
		 for(int i=x+1;i<=x+power;i++){
			  if(i>=0&&i<m2&&y>=0&&y<m1&&(mg[y][i]==0||mg[y][i]==3))
			  {
				  mg[y][i]=3;
			  }
			  else
				  break;
	
		 }
		 for(int i=x-1;i>=x-power;i--){
			  if(i>=0&&i<m2&&y>=0&&y<m1&&(mg[y][i]==0||mg[y][i]==3))
			  {
				  mg[y][i]=3;
			  }
			  else
				  break;
		 }
		 for(int i=y+1;i<=y+power;i++){
			  if(x>=0&&i>=0&&x<m2&&i<m1&&(mg[i][x]==0||mg[i][x]==3))
			  {
				  mg[i][x]=3;
			  }

			 else 
			 break;
		 }
		 for(int i=y-1;i>=y-power;i--){
			  if(x>=0&&i>=0&&x<m2&&i<m1&&(mg[i][x]==0||mg[i][x]==3))
			  {
				  mg[i][x]=3;
			  }

			 else 
			 break;
		 }
		
	}
	for(Ball b:bl){//标记地图中危险方块 	
		mg[b.y][b.x]=3;
	}
	/*System.out.print("爆炸设置后地图");
	
	for(int i=0;i<mg.length;i++){
	System.out.println();
	for(int j=0;j<mg[0].length;j++){
		System.out.print(mg[i][j]);
	}
}*/
	if(mg[m.gy][m.gx]==0)
	{
		//System.out.print("敌对安全");
		return true;
	}
	return false;
}
/**
 * 获得最近的安全行进方向序列
 * @param x1 所在位置x坐标
 * @param y1 所在位置y坐标
 * @return
 */
public int[] safeBox(int x1,int y1){//寻找最近的安全行进方向
	int [] result=null;
	int safeDirection=-1;
	int ls=1000;
	int ymg[][]=p.map.map;
	int [][]mg=new int [ymg.length][ymg[0].length];
	for(int i=0;i<ymg.length;i++){
		for(int j=0;j<ymg[0].length;j++){
			mg[i][j]=ymg[i][j];
		}
	}
   List <Ball>bl=new ArrayList<Ball>();
   bl.addAll(p.allBall);
	for(Ball b:bl){//标记地图中危险方块 
		if(b==null)
			continue;
		int x=b.x,y=b.y,power=b.power,m1=b.m1,m2=b.m2;
		 for(int i=x+1;i<=x+power;i++){
			  if(i>=0&&i<m2&&y>=0&&y<m1&&(mg[y][i]==0||mg[y][i]==3))
			  {
				  mg[y][i]=3;
			  }
			  else
				  break;
	
		 }
		 for(int i=x-1;i>=x-power;i--){
			  if(i>=0&&i<m2&&y>=0&&y<m1&&(mg[y][i]==0||mg[y][i]==3))
			  {
				  mg[y][i]=3;
			  }
			  else
				  break;
		 }
		 for(int i=y+1;i<=y+power;i++){
			  if(x>=0&&i>=0&&x<m2&&i<m1&&(mg[i][x]==0||mg[i][x]==3))
			  {
				  mg[i][x]=3;
			  }

			 else 
			 break;
		 }
		 for(int i=y-1;i>=y-power;i--){
			  if(x>=0&&i>=0&&x<m2&&i<m1&&(mg[i][x]==0||mg[i][x]==3))
			  {
				  mg[i][x]=3;
			  }

			 else 
			 break;
		 }
		
	}
	for(Ball b:bl){//标记地图中危险方块 	
		mg[b.y][b.x]=3;
	}
	/*System.out.print("爆炸设置后地图");
	
	for(int i=0;i<mg.length;i++){
	System.out.println();
	for(int j=0;j<mg[0].length;j++){
		System.out.print(mg[i][j]);
	}
}*/
	if(mg[x1][y1]==0)
	{
		//System.out.println("本地安全");
		return null;

	}
	for(int i=0;i<mg.length;i++){
		System.out.println("");
		for(int j=0;j<mg[0].length;j++){
		
			if(mg[i][j]==0)
				{
				int [] g=aa(x1,y1,i,j);
				if(g==null)
					continue;
				int sl=g.length;
				if(sl<ls)
					{ls=sl;
					safeDirection=g[0];
					result=g;
					}
			
				System.out.print("0");
				}
			else
				System.out.print("1");
			
		}
	}
	System.out.println("安全方向"+safeDirection+"序列为:");
	for(int i=0;result!=null&&i<result.length;i++){
		System.out.print(result[i]+"->");
	}
	return result;
}
/**
 * 获得寻路行进序列
 * @param x 源x坐标
 * @param y 源y坐标
 * @param gx 目的x坐标
 * @param gy 目的y坐标
 * @return
 */
public int [] aa(int x,int y,int gx,int gy){//获得寻路行进序列
	int r[]=null;
	stack dz=new stack();
    r=null;
    int ymg[][]=p.map.map;
	int [][] mg=new int [ymg.length][ymg[0].length];
	for(int i=0;i<ymg.length;i++){
		for(int j=0;j<ymg[0].length;j++){
			mg[i][j]=ymg[i][j];
		}
	}
	if(man.rx!=1000)
	mg[man.ry][man.rx]=0;
	mg[x][y]=3;
	int p=-1;
	/*for(int i=0;i<ymg.length;i++){
		System.out.println();
		for(int j=0;j<ymg[0].length;j++){
			System.out.print(mg[i][j]);
		}
	}*/
	while(true){
		if(x==gx&&y==gy)
		{
			if(r==null||dz.len<r.length)
			{
				r=new int[dz.len];
				for(int i=0;i<dz.len;i++){
					r[i]=dz.mg[i];
					//System.out.print(dz.mg[i]+"->");
				}
			}

		}
		int m=mg[x][y];
		     if(y-1>=0&&(mg[x][y-1]==0||mg[x][y-1]>m+1)&&p!=1)
		{dz.push(1);y--;p=3;mg[x][y]=m+1;}
		else if(x+1<mg.length&&(mg[x+1][y]==0||mg[x+1][y]>m+1)&&p!=2)
		{dz.push(2);x++;p=4;mg[x][y]=m+1;}
		else if(y+1<mg[0].length&&(mg[x][y+1]==0||mg[x][y+1]>m+1)&&p!=3)
		{dz.push(3);y++;p=1;mg[x][y]=m+1;}
		else if(x-1>=0&&(mg[x-1][y]==0||mg[x-1][y]>m+1)&&p!=4)
		{dz.push(4);x--;p=2;mg[x][y]=m+1;}
		else{
			if(dz.len==0)
				break;
			switch(dz.pop()){
			case 1:
				p=1;	
				y++;
				break;
			case 2:
				p=2;
				x--;
				break;
			case 3:
				y--;
				p=3;
				break;
			case 4:
				x++;
				p=4;
				break;
			}	
		}
	}
	/*for(int i=0;r!=null&&i<r.length;i++){
		System.out.print(r[i]+"->");
	}*/
	return r;
}
}
//栈
class stack{
	int mg []=new int[1000];
	int len=0;
	public void push(int i){
		mg[len]=i;
		len++;
	} 
	public int  pop(){
		len--;
		return mg[len];
		
	}
}
