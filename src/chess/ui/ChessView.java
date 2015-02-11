package chess.ui;
import android.content.Context;
import android.view.View;
import chess.game.Define;

import static chess.game.Define.*;

/**
 * ChessDlg.java 
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

public class ChessView extends View{

	public ChessView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	

	/*final static int BORDERWIDTH = 15; //棋盘(左右)边缘的宽度
	final static int BORDERHEIGHT = 14; //棋盘(上下)边缘的高度
	final static int GRILLEWIDTH = 39;  //棋盘上每个格子的高度
	final static int GRILLEHEIGHT = 39; //棋盘上每个格子的宽度
	final byte InitChessBoard[][]= 
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
	
	private boolean m_bGameOver;
	private final byte m_ChessBoard[][] = new byte[10][9];
	private final byte m_BackupChessBoard[][] = new byte[10][9];
	private MOVECHESS m_MoveChess;
	//private POINT m_ptMoveChess;
	private int m_nBoardWidth;
	private int m_nBoardHeight;
	
	public ChessView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public boolean IsGameOver(byte position[][]){
		int i, j;
		boolean RedLive = false, BlackLive = false;
		for (i = 7; i < 10; i++)
			for (j = 3; j < 6; j++){
				if (position[i][j] == B_KING) 
					BlackLive = true;
				if (position[i][j] == R_KING)
					RedLive = true;
			}
		
		for (i = 0; i < 3; i++)
			for (j = 3; j < 6; j++){
				if (position[i][j] == B_KING)
					BlackLive = true;
				if (position[i][j] == R_KING)
					RedLive = true;
			}
		
		if (RedLive && BlackLive)
			return false;
		else 
			return true;
	}
	
	public class MOVECHESS
	{
		byte nChessID;
		//POINT ptMovePoint;
	}*/
	
	
}



