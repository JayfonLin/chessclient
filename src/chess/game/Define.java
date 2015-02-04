package chess.game;

/**
 * Define.java 
 * chess 1.0
 * Date 2015/02/03
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

public class Define {

	public final static byte NOCHESS = 0;    //没有棋子
	
	public final static byte B_KING = 1;     //黑帅
	public final static byte B_CAR = 2;      //黑车
	public final static byte B_HORSE = 3;    //黑马
	public final static byte B_CANON = 4;    //黑炮
	public final static byte B_BISHOP = 5;   //黑士
	public final static byte B_ELEPHANT = 6; //黑象
	public final static byte B_PAWN = 7;     //黑卒
	public final static byte B_BEGIN = B_KING;
	public final static byte B_END = B_PAWN;

	public final static byte R_KING = 8;     //红帅
	public final static byte R_CAR = 9;      //红车
	public final static byte R_HORSE = 10;   //红马
	public final static byte R_CANON = 11;   //红炮
	public final static byte R_BISHOP = 12;  //红士
	public final static byte R_ELEPHANT = 13;//红象
	public final static byte R_PAWN = 14;    //红兵
	public final static byte R_BEGIN = R_KING;
	public final static byte R_END = R_PAWN;

	final static public boolean IsBlack(int x){
		return x>=B_BEGIN&&x<=B_END;
	}
	final static public boolean IsRed(int x){
		return x>=R_BEGIN&&x<=R_END;
	}
	final static public boolean IsSameSide(int x, int y){
		return (IsBlack(x)&&IsBlack(y))||(IsRed(x)&&IsRed(y));
	}


	/*
	 * 定义一个棋子位置的结构
	 */
	public class CHESSMANPOS
	{
		public byte x;
		public byte y;
	}

	/*
	 * 一个走法的结构
	 */
	public class CHESSMOVE
	{
		public short ChessID;	//标明是什么棋子
		public CHESSMANPOS	From;
		public CHESSMANPOS	To;			
		public int Score;		
	}

}
