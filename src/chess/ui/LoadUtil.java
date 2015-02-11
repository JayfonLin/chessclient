package chess.ui;

/**
 * LoadUtil.java 
 * chess 1.0
 * Date 2015/02/10
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

import static chess.game.Define.*;

public class LoadUtil {
	public static int sdPlayer=1;                   // 轮到谁走，0=红方，1=黑方
	public static byte ucpcSquares[][]=new byte[10][9];   // 棋盘上的棋子
	public static int mvResult;                     // 电脑走的棋
	
	final static byte InitChessBoard[][]= 
	{
		{B_CAR,   B_HORSE, B_ELEPHANT, B_BISHOP, B_KING,  B_BISHOP, B_ELEPHANT, B_HORSE, B_CAR},
		{NOCHESS, NOCHESS, NOCHESS,    NOCHESS,  NOCHESS, NOCHESS,  NOCHESS,    NOCHESS, NOCHESS},
		{NOCHESS, B_CANON, NOCHESS,    NOCHESS,  NOCHESS, NOCHESS,  NOCHESS,    B_CANON, NOCHESS},
		{B_PAWN,  NOCHESS, B_PAWN,     NOCHESS,  B_PAWN,  NOCHESS,  B_PAWN,     NOCHESS, B_PAWN},
		{NOCHESS, NOCHESS, NOCHESS,    NOCHESS,  NOCHESS, NOCHESS,  NOCHESS,    NOCHESS, NOCHESS},
		
		{NOCHESS, NOCHESS, NOCHESS,    NOCHESS,  NOCHESS, NOCHESS,  NOCHESS,    NOCHESS, NOCHESS},
		{R_PAWN,  NOCHESS, R_PAWN,     NOCHESS,  R_PAWN,  NOCHESS,  R_PAWN,     NOCHESS, R_PAWN},
		{NOCHESS, R_CANON, NOCHESS,    NOCHESS,  NOCHESS, NOCHESS,  NOCHESS,    R_CANON, NOCHESS},
		{NOCHESS, NOCHESS, NOCHESS,    NOCHESS,  NOCHESS, NOCHESS,  NOCHESS,    NOCHESS, NOCHESS},
		{R_CAR,   R_HORSE, R_ELEPHANT, R_BISHOP, R_KING,  R_BISHOP, R_ELEPHANT, R_HORSE, R_CAR}
	};
	// 初始化棋盘
	public static void Startup() {
	  sdPlayer = 0;
	  for (int i = 0; i < 10; i++){ //初始化为零
		  for (int j = 0; j < 9; j++){
			  ucpcSquares[i][j] = NOCHESS;
		  }
	  }
	  ucpcSquares = InitChessBoard.clone();
	  for (int i = 0; i < 10; i++){ 
		  ucpcSquares[i] = InitChessBoard[i].clone();
	  }
	  
	}
	
	public static void ChangeSide() {         // 交换走子方
	    sdPlayer = 1 - sdPlayer;
	}
	public static void AddPiece(int x, int y, int pc) { // 在棋盘上放一枚棋子
		ucpcSquares[y][x] = (byte) pc;
	}
	public static void DelPiece(int x, int y) { // 从棋盘上拿走一枚棋子
	    ucpcSquares[y][x] = NOCHESS;
	    
	}
}
