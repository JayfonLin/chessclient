package chess.ui;

/**
 * GameView.java 
 * chess 1.0
 * Date 2015/02/09
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

import chess.activity.R;
import chess.game.Define;
import chess.game.Evaluation;
import chess.game.MoveGenerator;
import chess.game.NegaScout_TT_HH;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import static chess.ui.ViewConstant.*;
import static chess.game.Define.*;

public class GameView extends View{

	MainActivity father;
	Bitmap[][] chessBitmap;//象棋棋子图片
	Bitmap chessZouQiflag;//标志走棋
	Paint paint;//画笔
	Bitmap boardFrame;//棋盘边框
	Bitmap background;//背景图
	Bitmap fort;//炮台
	Bitmap fort2;//炮台边沿的时候
	Bitmap fort1;
	Bitmap selectBM;

	int[][] color=new int[20][3];

	boolean playChessflag;//下棋方标志位，false为黑方下棋
	byte ucpcSquares[][]=new byte[10][9];
	boolean isStart = false;

	boolean cmFlag=true;//触摸是否有效
	boolean isFlage;//是否为第一次下棋
	boolean seleFlag = false;//是否为选中
	boolean toFlag = false;
	int fromx, fromy, tox, toy;
	int bzcol, bzrow;
	public NegaScout_TT_HH engine;

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			seleFlag = toFlag = true;
			fromx = engine.m_cmBestMove.From.x;
			fromy = engine.m_cmBestMove.From.y;
			tox = engine.m_cmBestMove.To.x;
			toy = engine.m_cmBestMove.To.y;
			switch(msg.what){
			case 0:
				father.playSound(SOUND_MOVE2, 0);
				break;
			case 1:
				father.playSound(SOUND_CAPTURE2, 0);
				break;
			default:
				father.playSound(SOUND_MOVE2, 0);
				break;
			}
	
			postInvalidate();
			LoadUtil.ChangeSide();
			cmFlag = true;
		}
		
	};

	public GameView(Context context) {
		super(context);
		this.father=(MainActivity)context;	
		setFocusable(true);
		setKeepScreenOn(true);
		paint = new Paint();//创建画笔
		paint.setAntiAlias(true);//打开抗锯齿
		LoadUtil.Startup();//初始化棋盘
		initArrays();//初始化数组
		initBitmap(); ///初始化图片
		LoadUtil.sdPlayer=0;//下棋方为红方
		engine = new NegaScout_TT_HH();
		
		endTime=zTime;//总时间	
	}

	public void initArrays()
	{
		for(int i=0;i<10;i++)
		{
			ucpcSquares[i]=LoadUtil.ucpcSquares[i].clone();
		}
	}
	
	
	@Override
	public void onDraw(Canvas canvas)
	{		
		canvas.drawColor(Color.argb(255, 0, 0, 0));
		canvas.drawBitmap(background,0,0, null);

		onDrawWindowindow(canvas,sXtart,sYtart);
		
		switch(Define.IsGameOver(ucpcSquares)){
		case 0:
			
			break;
		case 1:
			drawWinOrLoss(true);
			break;
		case -1:
			drawWinOrLoss(false);
			break;
		}
	}
	
	protected void drawWinOrLoss(final boolean isWin){
		AlertDialog.Builder builder = new AlertDialog.Builder(father);
		builder.setTitle("中国象棋");
		if (isWin){
			builder.setMessage("你赢了！\n再来一盘？");
			father.playSound(SOUND_WIN, 0);
		}else{
			builder.setMessage("你输了！\n再接再励！\n再来一盘？");
			father.playSound(SOUND_LOSS, 0);
		}
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				LoadUtil.Startup();//初始化棋盘
				initArrays();//初始化数组
				seleFlag = false;
				toFlag = false;
				father.showWindow();
				engine = new NegaScout_TT_HH();
				postInvalidate();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				//alert.dismiss();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
			
	}
	
	public void onDrawWindowindow(Canvas canvas,float sXtart,float sYtart)
	{
		canvas.drawBitmap(boardFrame,0,0, null);
		
		//绘制红色填充矩形
		paint.setColor(Color.RED);//设置画笔颜色
		paint.setStrokeWidth(3);//设置线的粗细
		
		//画竖线
		canvas.drawLine(sXtart+xSpan+0*xSpan,sYtart+ySpan, sXtart+xSpan+xSpan*0, sYtart+ySpan*10, paint);
		canvas.drawLine(sXtart+xSpan+8*xSpan,sYtart+ySpan, sXtart+xSpan+xSpan*8, sYtart+ySpan*10, paint);
		for(int i=1;i<8;i++)//画竖线
		{
			canvas.drawLine(sXtart+xSpan+i*xSpan,sYtart+ySpan, sXtart+xSpan+xSpan*i, sYtart+ySpan*5, paint);
		}
		for(int i=1;i<8;i++)//画竖线
		{
			canvas.drawLine(sXtart+xSpan+i*xSpan,sYtart+ySpan*6, sXtart+xSpan+xSpan*i, sYtart+ySpan*10, paint);
		}
		
		canvas.drawLine(sXtart+xSpan*4,sYtart+ySpan, sXtart+xSpan*6, sYtart+ySpan*3, paint);//绘制九宫斜线
		canvas.drawLine(sXtart+xSpan*6,sYtart+ySpan, sXtart+xSpan*4, sYtart+ySpan*3, paint);
		
		canvas.drawLine(sXtart+xSpan*4,sYtart+ySpan*8, sXtart+xSpan*6, sYtart+ySpan*10, paint);
		canvas.drawLine(sXtart+xSpan*6,sYtart+ySpan*8, sXtart+xSpan*4, sYtart+ySpan*10, paint);
		
		for(int i=0;i<10;i++)//画横线
		{
			if (i == 4){paint.setStrokeWidth(4);}
			canvas.drawLine(xSpan+sXtart,ySpan+ySpan*i+sYtart, sXtart+xSpan*9, sYtart+ySpan+ySpan*i, paint);
			if (i == 5){paint.setStrokeWidth(3);}
		}
		
		//绘制边框
		paint.setStrokeWidth(5);//设置线的粗细
		canvas.drawLine(sXtart+0.8f*xSpan,sYtart+0.8f*ySpan, sXtart+9.2f*xSpan, sYtart+0.8f*ySpan, paint);
		canvas.drawLine(sXtart+0.8f*xSpan,sYtart+0.8f*ySpan, sXtart+0.8f*xSpan, sYtart+10.2f*ySpan, paint);
		canvas.drawLine(sXtart+9.2f*xSpan,sYtart+0.8f*ySpan, sXtart+9.2f*xSpan, sYtart+10.2f*ySpan, paint);
		canvas.drawLine(sXtart+0.8f*xSpan, sYtart+10.2f*ySpan,sXtart+9.2f*xSpan, sYtart+10.2f*ySpan, paint);
		
		canvas.drawBitmap(fort,sXtart+2*xSpan-chessR,sYtart+3*ySpan-chessR, null);//绘制炮台
		canvas.drawBitmap(fort,sXtart+2*xSpan-chessR,sYtart+8*ySpan-chessR, null);//绘制炮台
		canvas.drawBitmap(fort,sXtart+8*xSpan-chessR,sYtart+3*ySpan-chessR, null);//绘制炮台
		canvas.drawBitmap(fort,sXtart+8*xSpan-chessR,sYtart+8*ySpan-chessR, null);//绘制炮台
		
		canvas.drawBitmap(fort2,sXtart+1*xSpan-chessR,sYtart+4*ySpan-chessR, null);//绘制兵台
		canvas.drawBitmap(fort,sXtart+3*xSpan-chessR,sYtart+4*ySpan-chessR, null);//绘制兵炮台
		canvas.drawBitmap(fort,sXtart+5*xSpan-chessR,sYtart+4*ySpan-chessR, null);//绘制兵炮台
		canvas.drawBitmap(fort,sXtart+7*xSpan-chessR,sYtart+4*ySpan-chessR, null);//绘制兵炮台
		canvas.drawBitmap(fort1,sXtart+9*xSpan-chessR,sYtart+4*ySpan-chessR, null);//绘制兵炮台
		
		canvas.drawBitmap(fort2,sXtart+1*xSpan-chessR,sYtart+7*ySpan-chessR, null);//绘制兵台
		canvas.drawBitmap(fort,sXtart+3*xSpan-chessR,sYtart+7*ySpan-chessR, null);//绘制兵炮台
		canvas.drawBitmap(fort,sXtart+5*xSpan-chessR,sYtart+7*ySpan-chessR, null);//绘制兵炮台
		canvas.drawBitmap(fort,sXtart+7*xSpan-chessR,sYtart+7*ySpan-chessR, null);//绘制兵炮台
		canvas.drawBitmap(fort1,sXtart+9*xSpan-chessR,sYtart+7*ySpan-chessR, null);//绘制兵炮台
		
		//画棋子
		for(int i=0;i<10;i++)//绘制棋子
		{
			for(int j=0;j<9;j++)
			{
				if(ucpcSquares[i][j]!=NOCHESS)
				{
					canvas.drawBitmap(chessBitmap[ucpcSquares[i][j]/8][
					(ucpcSquares[i][j]-1)%7],sXtart+j*xSpan-chessR+xSpan,sYtart+i*ySpan-chessR+ySpan, null);
				}
			}
		}
		if (seleFlag){
			drawSelection(canvas, fromx, fromy);
			if (toFlag){
				drawSelection(canvas, tox, toy);
			}
		}
		
		
	}
	
	public void initBitmap()
	{
		float xZoom=ViewConstant.xZoom;
		int chessD = (int)(chessR*2);
		background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background), 
				(int)screen_width, (int)screen_height, false); //背景图
		boardFrame=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.floor2), 
				(int)screen_width, (int)screen_height, false); //棋盘
		selectBM = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.selected), chessD, chessD, false);
		chessZouQiflag=scaleToFit(BitmapFactory.decodeResource(getResources(), R.drawable.selected),xZoom);//标志位
	
		//炮台
		fort = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fort), chessD, chessD, false);
		fort1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fort1), chessD, chessD, false);
		fort2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fort2), chessD, chessD, false);
		
		chessBitmap=new Bitmap[][]{//棋子
				{
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bk), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ba), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bb), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bn), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.br), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bc), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bp), chessD, chessD, false),
				
				},{
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rk), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ra), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rb), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rn), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rr), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rc), chessD, chessD, false),
					Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rp), chessD, chessD, false),
				
				}
		};
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		if(e.getAction()==MotionEvent.ACTION_DOWN)
		{
			if(!cmFlag)//如果正在进行电脑下棋
			{
				return false;
			}
			
			int col = (int)((e.getX()-sXtart)/xSpan);
			int row = (int)((e.getY()-sYtart)/ySpan);	
			if(	((e.getX()-col*xSpan-sXtart)*(e.getX()-col*xSpan-sXtart)+
				(e.getY()-row*ySpan-sYtart)*(e.getY()-row*ySpan-sYtart))<xSpan/2*xSpan/2)		
			{
				bzcol=col-1;
				bzrow=row-1;//看其在哪一个格子上
			}
			else if(((e.getX()-col*xSpan-sXtart)*(e.getX()-col*xSpan-sXtart)+
				(e.getY()-(row+1)*ySpan-sYtart)*(e.getY()-(row+1)*ySpan-sYtart))<xSpan/2*xSpan/2)	
			{
				bzcol=col-1;
				bzrow=row;
			}
			else if(((e.getX()-(1+col)*xSpan-sXtart)*(e.getX()-(1+col)*xSpan-sXtart)+
					(e.getY()-(row+1)*ySpan-sYtart)*(e.getY()-(row+1)*ySpan-sYtart))<xSpan/2*xSpan/2)	
			{
				bzcol=col;
				bzrow=row;
			}
			else if(((e.getX()-(1+col)*xSpan-sXtart)*(e.getX()-(1+col)*xSpan-sXtart)+
			(e.getY()-row*ySpan-sYtart)*(e.getY()-row*ySpan-sYtart))<xSpan/2*xSpan/2)
			{
				bzcol=col;
				bzrow=row-1;
			}
			if (bzcol < 0 || bzcol > 8 || bzrow < 0 || bzrow > 9)
				return false;
			if (seleFlag && toFlag){
				seleFlag = toFlag = false;
			}
			if (seleFlag){
				//走棋
				if (MoveGenerator.IsValidMove(ucpcSquares, fromx, fromy, bzcol, bzrow)){
					tox = bzcol;toy = bzrow;
					toFlag = true;
					int sound_id;
					if (ucpcSquares[toy][tox] == NOCHESS)
						sound_id = SOUND_MOVE1;
					else sound_id = SOUND_CAPTURE1;
					ucpcSquares[toy][tox] = ucpcSquares[fromy][fromx];
					ucpcSquares[fromy][fromx] = NOCHESS;
					father.playSound(sound_id, 0);
					enermyAct(ucpcSquares);
				}else if (IsSameSide(ucpcSquares[fromy][fromx], ucpcSquares[bzrow][bzcol])){
					fromx = bzcol; fromy = bzrow;
					seleFlag = true;
				}
				
			}else{
				if (ucpcSquares[bzrow][bzcol] != NOCHESS && ucpcSquares[bzrow][bzcol] >= R_BEGIN){
					seleFlag = true; toFlag = false;
					fromx = bzcol;fromy = bzrow;
				}else{
					seleFlag = false; toFlag = false;
				}
			}
			postInvalidate();
			 
			return true;
		}

		return super.onTouchEvent(e);
	}
	
	public void drawSelection(Canvas canvas, int x, int y){
		canvas.drawBitmap(selectBM,sXtart+(x+1)*xSpan-chessR,sYtart+(y+1)*ySpan-chessR, null);
	}
	
	public void enermyAct(byte position[][]){
		cmFlag = false;
		LoadUtil.ChangeSide();
		new Thread(new EnermyRunnable(handler)).start();
	}
	
	class EnermyRunnable implements Runnable{
		Handler mHandler;
		public EnermyRunnable(Handler pHandler){
			mHandler = pHandler;
		}
		@Override
		public void run() {
			int what = 0;
			if(engine.SearchAGoodMove(ucpcSquares))
				what = 1;
			mHandler.sendEmptyMessage(what);
		}
		
	}

}
