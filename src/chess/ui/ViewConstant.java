package chess.ui;

/**
 * ViewConstant.java 
 * chess 1.0
 * Date 2015/02/08
 * 
 * COPYRIGHT NOTES
 * ---------------
 * This source code is a part of chess which is designed for bachelor thesis.
 * You may use, compile or redistribute it as part of your application for free. 
 * You cannot redistribute sources without the official agreement of the author. 
 * If distribution of your application which contents code below was occurred, place
 * e-mail <linjiafang33@163.com> on it is to be appreciated.
 * This code can be used WITHOUT ANY WARRANTIES on your own risk.
 * 
 * Jayfon Lin <linjiafang33@163.com>
 * 
 * ---------------
 * 版权声明
 * ---------------
 * 本文件所含之代码是学士论文设计中国象棋的一部分
 * 您可以免费的使用, 编译 或者作为您应用程序的一部分。 
 * 但，您不能在未经作者书面许可的情况下分发此源代码。 
 * 如果您的应用程序使用了这些代码，在您的应用程序界面上 
 * 放入 e-mail <linjiafang33@163.com>是令人欣赏的做法。
 * 此代码并不含有任何保证，使用者当自承风险。
 * 
 * 林家访 <linjiafang33@163.com>
 *
 */

import android.graphics.Bitmap;
import android.graphics.Matrix;

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
	public static float sXtartCk;
	public static float sYtartCk;//第二窗口的起始坐标
	
	public static float windowWidth=200*xZoom;//窗口的大小
	public static float windowHeight=400*xZoom;
	
	public static float windowXstartLeft=sXtart+(5*xSpan-windowWidth)/2*xZoom;//小窗口的起始坐标
	public static float windowXstartRight=sYtart+5*xSpan+(5*xSpan-windowWidth)/2*xZoom;
	public static float windowYstart=sYtart+ySpan*4*xZoom;
	
	
	public static float chessR=30*xZoom;//棋子半径
	public static float fblRatio=0.6f*xZoom;//棋子缩放比例
	
	public static Bitmap scaleToFit(Bitmap bm,float fblRatio)//缩放图片的方法
    {
    	int width = bm.getWidth(); //图片宽度
    	int height = bm.getHeight();//图片高度
    	Matrix matrix = new Matrix(); 
    	matrix.postScale((float)fblRatio, (float)fblRatio);//图片等比例缩小为原来的fblRatio倍
    	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);//声明位图        	
    	return bmResult;
    }
	public static void initChessViewFinal()
	{
		xSpan=48.0f*xZoom;
		ySpan=48.0f*yZoom;
		
		scoreWidth = 7*xZoom*4f;//时间数字间隔
		
		
		windowWidth=200*xZoom;//窗口的大小
		windowHeight=250*xZoom;
		
		windowXstartLeft=sXtart+(5*xSpan-windowWidth)/2*xZoom;//小窗口的起始坐标
		windowXstartRight=sYtart+5*xSpan+(5*xSpan-windowWidth)/2*xZoom;
		windowYstart=sYtart+ySpan*1*xZoom;

		chessR=24f*xZoom;//棋子半径
		fblRatio=0.6f*xZoom;//棋子缩放比例
	}
}