package chess.ui;

/**
 * Created on 2015-02-09
 * @author jeff
 */

import chess.activity.R;
import chess.engine.ChessLoadUtil;
import chess.engine.Define;
import chess.engine.MoveGenerator;
import chess.engine.NegaScout_TT_HH;
import chess.logic.CPlayGame;
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
import static chess.engine.ChessLoadUtil.*;
import static chess.engine.Constant.*;
import static chess.engine.Define.*;

import chess.logic.HandlerManager;

public class GameView extends View{
	
	public static final int MSG_WHAT_CHESS_MOVE = 1; 

	MainActivity father;
	Bitmap[][] chessBitmap;//象棋棋子图片
	//Bitmap chessZouQiflag;//标志走棋
	Paint paint;//画笔
	Bitmap boardFrame;//棋盘边框
	Bitmap background;//背景图
	Bitmap fort;//炮台
	Bitmap fort2;//炮台边沿的时候
	Bitmap fort1;
	Bitmap selectBM;

	boolean playChessflag;//下棋方标志位，false为黑方下棋
	int ucpcSquares[] = new int[BOARD_NUMBER];
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
			switch(msg.what){
			case MSG_WHAT_CHESS_MOVE:
				int move = msg.arg1;
				EnermyChessMove(move);
				break;
				
			default:
				break;
			}
			
			/*
			seleFlag = toFlag = true;
			int sqSrc, sqDst;
			
			sqSrc = Src(engine.m_cmBestMove.Move);
			sqDst = Dst(engine.m_cmBestMove.Move);
			fromx = FileX(sqSrc)-FILE_LEFT;
			fromy = RankY(sqSrc)-RANK_TOP;
			tox = FileX(sqDst)-FILE_LEFT;
			toy = RankY(sqDst)-RANK_TOP;
			switch(msg.what){
			case 0:
				father.playSound(SOUND_MOVE2, 0);
				break;
				
			case 1:
				father.playSound(SOUND_CAPTURE2, 0);
				break;
				
			case 2:
				LoadUtil.ChangeSide();
				cmFlag = true;
				return;
				
			default:
				father.playSound(SOUND_MOVE2, 0);
				break;
			}
	
			postInvalidate();
			LoadUtil.ChangeSide();
			cmFlag = true;
			*/
		}
		
	};

	public GameView(Context context) {
		super(context);
		this.father=(MainActivity)context;	
		initGame();
	}
	
	public void initGame(){
		setFocusable(true);
		setKeepScreenOn(true);
		paint = new Paint();//创建画笔
		paint.setAntiAlias(true);//打开抗锯齿
		
		LoadUtil.Startup();//初始化棋盘
		ucpcSquares = LoadUtil.pieceSquares.clone();
		initBitmap(); ///初始化图片
		engine = new NegaScout_TT_HH();
		
		endTime=zTime;//总时间	
		
		HandlerManager.GetInstance().SetGameViewHandler(handler);
	}
	
	
	@Override
	public void onDraw(Canvas canvas){		
		canvas.drawColor(Color.argb(255, 0, 0, 0));
		canvas.drawBitmap(background, 0, 0, null);

		onDrawWindowindow(canvas, sXtart, sYtart);
		
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
		
		if (!CPlayGame.GetInstance().IsStart()){
			father.showWindow();
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
				ucpcSquares = LoadUtil.pieceSquares.clone();
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
	
	public void onDrawWindowindow(Canvas canvas, float sXtart, float sYtart){
		canvas.drawBitmap(boardFrame, 0, 0, null);
		
		//绘制红色填充矩形
		paint.setColor(Color.RED);//设置画笔颜色
		paint.setStrokeWidth(3);//设置线的粗细
		
		//画竖线
		canvas.drawLine(sXtart+xSpan+0*xSpan, sYtart+ySpan, sXtart+xSpan+xSpan*0, sYtart+ySpan*10, paint);
		canvas.drawLine(sXtart+xSpan+8*xSpan, sYtart+ySpan, sXtart+xSpan+xSpan*8, sYtart+ySpan*10, paint);
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
		Bitmap ToBedrawn = null;
		for (int i = 0; i < BOARD_NUMBER; ++i){
			if (!InBoard(i)){
				continue;
			}
			
			if (ucpcSquares[i] == 0){
				continue;
			}
			ToBedrawn = chessBitmap[ucpcSquares[i] >> 4][ucpcSquares[i]%8];
			
			int x = FileX(i)-FILE_LEFT, y = RankY(i)-RANK_TOP;
			canvas.drawBitmap(ToBedrawn, sXtart+x*xSpan-chessR+xSpan, sYtart+y*ySpan-chessR+ySpan, null);
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
		int chessD = (int)(chessR*2);
		background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.background), 
				(int)screen_width, (int)screen_height, false); //背景图
		boardFrame=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.floor2), 
				(int)screen_width, (int)screen_height, false); //棋盘
		selectBM = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.selected), chessD, chessD, false);
	
		//炮台
		fort = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fort), chessD, chessD, false);
		fort1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fort1), chessD, chessD, false);
		fort2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.fort2), chessD, chessD, false);
		
		chessBitmap=new Bitmap[][]{//棋子
		{
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rk), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ra), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rb), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rn), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rr), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rc), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rp), chessD, chessD, false),
		},{
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bk), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ba), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bb), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bn), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.br), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bc), chessD, chessD, false),
			Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bp), chessD, chessD, false),
		}};
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
			
			int sqSrc = CoordXY(fromx+FILE_LEFT, fromy+RANK_TOP);
			int sqDst = CoordXY(bzcol+FILE_LEFT, bzrow+RANK_TOP);
			int mv = Move(sqSrc, sqDst);
			
			if (seleFlag){
				
				//走棋
				if (MoveGenerator.LeagalMove(ucpcSquares, mv, LoadUtil.GetSide())){
					int sound_id;
					if (ucpcSquares[sqDst] == 0)
						sound_id = SOUND_MOVE1;
					else sound_id = SOUND_CAPTURE1;
					ucpcSquares[sqDst] = ucpcSquares[sqSrc];
					ucpcSquares[sqSrc] = 0;
					father.playSound(sound_id, 0);
					//enermyAct(ucpcSquares);
					
					tox = FileX(sqDst)-FILE_LEFT;
					toy = RankY(sqDst)-RANK_TOP;
					toFlag = true;
					
					
					cmFlag = false;
					LoadUtil.ChangeSide();
					int move = ChessLoadUtil.Move(sqSrc, sqDst);
					CPlayGame.GetInstance().ChessMove(move);
					
				}else if (SameSide(ucpcSquares[sqSrc], ucpcSquares[sqDst])){
					fromx = bzcol; fromy = bzrow;
					seleFlag = true;
				}
				
			}else{
				if (IsRed(ucpcSquares[sqDst])){
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
	
	public void EnermyChessMove(int move){
		
		seleFlag = toFlag = true;
		int sqSrc, sqDst;
		
		sqSrc = Src(move);
		sqDst = Dst(move);
		
		ucpcSquares[sqDst] = ucpcSquares[sqSrc];
		ucpcSquares[sqSrc] = 0;
		
		fromx = FileX(sqSrc)-FILE_LEFT;
		fromy = RankY(sqSrc)-RANK_TOP;
		tox = FileX(sqDst)-FILE_LEFT;
		toy = RankY(sqDst)-RANK_TOP;

		postInvalidate();
		LoadUtil.ChangeSide();
		cmFlag = true;
	}
	
	public void enermyAct(int squares[]){
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
			if (Define.IsGameOver(ucpcSquares) != 0){
				what = 2;
			}
			else if(engine.SearchAGoodMove(ucpcSquares)){
				what = 1;
			}
			mHandler.sendEmptyMessage(what);
		}
		
	}

}
