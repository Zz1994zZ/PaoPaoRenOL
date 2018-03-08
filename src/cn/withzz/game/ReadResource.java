package cn.withzz.game;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * 将各类资源读入内存
 */
public class ReadResource {
	public boolean isReady=false;
	BufferedImage [] ball=new BufferedImage[9];
	BufferedImage [] mapImg=new BufferedImage[3];
	BufferedImage [] face,back,left,right,dying,dead;
	BufferedImage [] wface,wback,wleft,wright;
	BufferedImage [] item;
	BufferedImage [][][] man=new BufferedImage[2][6][6];
	BufferedImage victory,failed,cGame;
	//MusicPlayer mp;
	
	
	public   void init(){
	
				// TODO Auto-generated method stub
				  face=new BufferedImage[6];
				  back=new BufferedImage[6];
				  left=new BufferedImage[6];
				  right=new BufferedImage[6];
				  dead=new BufferedImage[6];
				  dying=new BufferedImage[6];
				    wface=new BufferedImage[5];
					wback=new BufferedImage[5];
					wleft=new BufferedImage[5];
					wright=new BufferedImage[5];
					item=new BufferedImage [5];
				  try {
					  
					  /*for(int i=0;i<2;i++){//人物
						  for(int j=0;j<6;j++){
							  for(int k=0;k<6;k++){
								  man[i][j][k]=ImageIO.read(new File("images/man"+i+""));
							  }
						  }
					  }*/
					  for(int i=0;i<6;i++){//人物
						  face[i]=ImageIO.read(new File("images/man1/face/f"+i+".png"));
						  back[i]=ImageIO.read(new File("images/man1/back/b"+i+".png"));
						  left[i]=ImageIO.read(new File("images/man1/left/l"+i+".png"));
						  right[i]=ImageIO.read(new File("images/man1/right/r"+i+".png"));
					  }
					  dead[0]=ImageIO.read(new File("images/man1/dead.png"));
					  dying[0]=ImageIO.read(new File("images/man1/dying.png"));
				      for(int i=0;i<3;i++){//地图

								mapImg[i]=ImageIO.read(new File("images/wall/wall"+i+".jpg"));
				    	}
				    	for(int i=0;i<9;i++){//泡泡
								ball[i]=ImageIO.read(new File("images/ball/"+(i+1)+".png"));
				    	}
				    	 for(int i=0;i<5;i++)//武器
						  {  
				    		 wface[i]=ImageIO.read(new File("images/daodan/f/d"+(i+1)+".png"));
						     wback[i]=ImageIO.read(new File("images/daodan/b/d"+(i+1)+".png"));
						     wleft[i]=ImageIO.read(new File("images/daodan/l/d"+(i+1)+".png"));
						     wright[i]=ImageIO.read(new File("images/daodan/r/d"+(i+1)+".png"));
						  }
				    	 for(int i=0;i<5;i++){
				    		 item[i]=ImageIO.read(new File("images/itemIcon/"+(i+1)+".png"));
				    	 }
				    	 //三条游戏结束显示
				    	 victory=ImageIO.read(new File("images/menu/victory.png"));
				    	 failed=ImageIO.read(new File("images/menu/failed.png"));
				    	 cGame=ImageIO.read(new File("images/menu/cGame.jpg"));
				    	   // mp=new MusicPlayer("34312.mid");
			
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 isReady=false;
				}

		      isReady=true;

		
	}

}
