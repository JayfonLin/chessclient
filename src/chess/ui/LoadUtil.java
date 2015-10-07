package chess.ui;

/**
 * Created on 2015-02-10
 * @author jeff
 */

import static chess.engine.Constant.*;

public class LoadUtil {
	public static int sdPlayer = 0;                   // 轮到谁走，0=红方，1=黑方
	public static int pieceSquares[] = new int[BOARD_NUMBER];   // 棋盘上的棋子
	//public static int mvResult;                     // 电脑走的棋
	
	/*final static byte InitChessBoard[][]= 
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
	};*/
	
	// 初始化棋盘
	public static void Startup() {
		sdPlayer = 0;
		for (int i = 0; i < BOARD_NUMBER; ++i){
			pieceSquares[i] = BOARD_STARTUP[i];
		}
	}
	
	public static void ChangeSide() {         // 交换走子方
	    sdPlayer = 1 - sdPlayer;
	}
	
	public static int GetSide(){
		return sdPlayer;
	}

}
