package chess.ui;

import android.util.DisplayMetrics;

/**
 * Created on 2015-02-08
 * @author jeff
 */


public class ViewConstant {
	public static float sXtart=0;//棋盘的起始坐标
	public static float sYtart=0;
	
	public static boolean yingJMflag;//赢界面标志
	public static boolean shuJMflag;//输界面标志
	
	public static float screen_height;
    public static float screen_width; 
	public static float height;
	public static float width; 
	public static int huiqiBS=2;
	public static boolean isnoPlaySound=true;//是否播放声音
	public static boolean isComputerPlayChess=false;//是否为电脑下棋,初始为不是
	public static boolean isHeqi=false;//是否为和棋
	public static boolean isnoStart=false;//是否为开始或者暂停
	public static boolean isnoTCNDxuanz;//是否为弹出了难度选择
	public static int nanduXS=1;//难度系数
	
	public static int thinkDeeplyTime=1;
	
	public static int zTime=900000;
	public static int endTime=zTime;
	public static float xZoom=1F;//缩放比例
	public static float yZoom=1F;
	
	public static float xSpan=48.0f*xZoom;
	public static float ySpan=48.0f*yZoom;
	
	public static float scoreWidth = 7*xZoom*4f;//时间数字间隔
	public static float windowWidth=200*xZoom;//窗口的大小
	public static float windowHeight=400*xZoom;
	
	
	public static float chessR=30*xZoom;//棋子半径
	public static float fblRatio=0.6f*xZoom;//棋子缩放比例
	
	
	public static void initAll(DisplayMetrics dm){
		initZoom(dm);
		initChessViewFinal();
	}
	
	public static void initZoom(DisplayMetrics dm){
		
		screen_height = dm.heightPixels;
        screen_width = dm.widthPixels;
		
		int tempHeight = (int)screen_height;
		tempHeight *= 0.9f;
        int tempWidth=(int)screen_width; 
         
       
        if(tempHeight>tempWidth)
        {
        	height=tempHeight;
        	width=tempWidth;
        }
        else
        {
        	height=tempWidth;
        	width=tempHeight;
        }
        
        float zoomx=width/480;
		float zoomy=height/800;
		if(zoomx>zoomy){
			xZoom=yZoom=zoomy;
			
		}else
		{
			xZoom=yZoom=zoomx;
		}
		
	}
	
	public static void initChessViewFinal()
	{
		xSpan=48.0f*xZoom;
		ySpan=48.0f*yZoom;
		
		sXtart=(width-48*10*xZoom)/2;
		sYtart=(height-48*11*yZoom)/2;
		
		scoreWidth = 7*xZoom*4f;//时间数字间隔

		chessR=26f*xZoom;//棋子半径
		fblRatio=0.6f*xZoom;//棋子缩放比例
	}
}