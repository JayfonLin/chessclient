package chess.engine;

import static chess.engine.ChessLoadUtil.*;
import static chess.engine.Constant.*;


/**
 * Created on 2015-02-03
 * @author jeff
 *
 */



public class Define {
	
	public final static int SOUND_CAPTURE1 = 1;
	public final static int SOUND_CAPTURE2 = 2;
	public final static int SOUND_MOVE1 = 3;
	public final static int SOUND_MOVE2 = 4;
	public final static int SOUND_WIN = 5;
	public final static int SOUND_LOSS = 6;
	
	public static int IsGameOver(int[] squares){
		boolean redLive = false, blackLive = false;
		for (int i = 0; i < BOARD_NUMBER; ++i){
			if (!InFort(i)){
				continue;
			}
			if (squares[i] == R_KING){
				redLive = true;
			}
			if (squares[i] == B_KING){
				blackLive = true;
			}
		}
		if (redLive && blackLive)
			return 0;
		if (redLive)
			return 1;
		return -1;
	}

	// 判断是否被将军
	public static boolean Checked(int[] squares, int pSide){
		int i, j, sqSrc, sqDst;
		int pieceSelfSide, pieceOppSide, pieceDst, nDelta;
		int boardNum = 256;
		pieceSelfSide = SideTag(pSide);// 获得红黑标记(红子是8，黑子是16)
		pieceOppSide = OppSideTag(pSide);// 获得红黑标记，对方的
		// 找到棋盘上的帅(将)，再做以下判断：

		for (sqSrc = 0; sqSrc < boardNum; ++sqSrc) {
		    if (squares[sqSrc] != pieceSelfSide + PIECE_KING) {
		      continue;
		    }

		    // 1. 判断是否被对方的兵(卒)将军
		    if (squares[SquareForward(sqSrc, pSide)] == pieceOppSide + PIECE_PAWN) {
		      return true;
		    }
		    for (nDelta = -1; nDelta <= 1; nDelta += 2) {
		    	if (squares[sqSrc + nDelta] == pieceOppSide + PIECE_PAWN) {
		    		return true;
		    	}
		    }

		    // 2. 判断是否被对方的马将军(以仕(士)的步长当作马腿)
		    for (i = 0; i < 4; i ++) {
		    	if (squares[sqSrc + BODYGUARD_DELTA[i]] != 0) { 
		    		continue;
		    	}
		    	for (j = 0; j < 2; j ++) {
			        int pieceDstt = squares[sqSrc + HORSE_CHECK_DELTA[i][j]];
			        if (pieceDstt == pieceOppSide + PIECE_HORSE) {
			        	return true;
			        }
		    	}
		    }

		    // 3. 判断是否被对方的车或炮将军(包括将帅对脸)
		    for (i = 0; i < 4; i ++) {
		    	nDelta = KING_DELTA[i];
		    	sqDst = sqSrc + nDelta;
		    	while (InBoard(sqDst)) {
			        pieceDst = squares[sqDst];
			        if (pieceDst != 0) {
			        	if (pieceDst == pieceOppSide + PIECE_ROOK || pieceDst == pieceOppSide + PIECE_KING) {
			        		return true;
			        	}
			        	break;
			        }
			        sqDst += nDelta;
		    	}
		    	sqDst += nDelta;
		    	while (InBoard(sqDst)) {
		    		pieceDst = squares[sqDst];  
		    		if (pieceDst != 0) {
		    			if (pieceDst == pieceOppSide + PIECE_CANNON) {
		    				return true;
		    			}
		    			break;
		    		}
		    		sqDst += nDelta;
		    	}
		    }
		    return false;
		}
		return false;
	}


}
