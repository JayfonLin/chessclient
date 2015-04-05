package chess.game;

/**
 *
 * Created on 2015-02-03
 * @author jeff
 */

import static chess.game.ChessLoadUtil.*;
import static chess.game.Constant.*;

public class MoveGenerator {
	protected int nGenMoves;
	protected static int mMoves[] = new int[SINGLE_GEN_MOVE_SIZE]; 
	
	public MoveGenerator(){
		
	}
	
	public int CreatePossibleMove(int squares[], int mvs[], int ply,int pSide){
		int sqSrc;
		int pieceSelfSide, pieceSrc;
		boolean illeagal = false; //非伪法（合法）走法
		
		// 生成所有走法，需要经过以下几个步骤：
		nGenMoves = 0;
		pieceSelfSide = SideTag(pSide);
		for (sqSrc = 0; sqSrc < BOARD_NUMBER; ++sqSrc){
			
			if (!InBoard(sqSrc)){
				continue;
			}

			// 1. 找到一个本方棋子，再做以下判断：
			pieceSrc = squares[sqSrc];
			if ((pieceSrc & pieceSelfSide) == 0){
				continue;
			}
			
			// 2. 根据棋子确定走法
			switch (pieceSrc - pieceSelfSide){
			case PIECE_KING:
				for (int mv : GenKingMoves(squares, sqSrc, pSide, illeagal)){
					if (mv == -1){
						break;
					}
					mvs[nGenMoves++] = mv;
				}
				break;
				
			case PIECE_BODYGUARD:
				for (int mv : GenBodyguardMoves(squares, sqSrc, pSide, illeagal)){
					if (mv == -1){
						break;
					}
					mvs[nGenMoves++] = mv;
				}
				break;
				
			case PIECE_BISHOP:
				for (int mv : GenBishopMoves(squares, sqSrc, pSide, illeagal)){
					if (mv == -1){
						break;
					}
					mvs[nGenMoves++] = mv;
				}
				break;
				
			case PIECE_HORSE:
				for (int mv : GenHorseMoves(squares, sqSrc, pSide, illeagal)){
					if (mv == -1){
						break;
					}
					mvs[nGenMoves++] = mv;
				}
				break;
			case PIECE_ROOK:
				for (int mv : GenRookMoves(squares, sqSrc, pSide, illeagal)){
					if (mv == -1){
						break;
					}
					mvs[nGenMoves++] = mv;
				}
				break;
				
			case PIECE_CANNON:
				for (int mv : GenCannonMoves(squares, sqSrc, pSide, illeagal)){
					if (mv == -1){
						break;
					}
					mvs[nGenMoves++] = mv;
				}
				break;
				
			case PIECE_PAWN:
				for (int mv : GenPawnMoves(squares, sqSrc, pSide, illeagal)){
					if (mv == -1){
						break;
					}
					mvs[nGenMoves++] = mv;
				}
				break;
				
			default:
				break;
			}
		}
		return nGenMoves;
	}
	public static int[] GenKingMoves(int squares[], int sqSrc, int pSide, boolean illeagal){
		int sqDst, pieceDst, genCount = 0;
		int pieceSelfSide = SideTag(pSide);

		for (int i = 0; i < 4; ++i){
			sqDst = sqSrc + KING_DELTA[i];
			if (!InFort(sqDst)){
				continue;
			}
			pieceDst = squares[sqDst];
			if ((pieceDst & pieceSelfSide) == 0 || illeagal){
				mMoves[genCount++] = Move(sqSrc, sqDst);
			}
		}

		int mv = GenKingFace(squares, sqSrc);
		if (mv != -1){
			mMoves[genCount++] = mv;
		}
		
		mMoves[genCount] = -1;
		return mMoves;
	}
	
	public static int[] GenBodyguardMoves(int squares[], int sqSrc, int pSide, boolean illeagal){
		int sqDst, pieceDst, genCount = 0;
		int pieceSelfSide = SideTag(pSide);
		
		for (int i = 0; i < 4; ++i){
			sqDst = sqSrc + BODYGUARD_DELTA[i];
			if (!InFort(sqDst)){
				continue;
			}
			pieceDst = squares[sqDst];
			if ((pieceDst & pieceSelfSide) == 0 || illeagal){
				mMoves[genCount++] = Move(sqSrc, sqDst);
			}
		}
		
		mMoves[genCount] = -1;
		return mMoves;
	}
	
	public static int[] GenBishopMoves(int squares[], int sqSrc, int pSide, boolean illeagal){
		int sqDst, pieceDst, genCount = 0;
		int pieceSelfSide = SideTag(pSide);
		
		for (int i = 0; i < 4; ++i){
			sqDst = sqSrc + BODYGUARD_DELTA[i];
			if (!(InBoard(sqDst) && HomeHalf(sqDst, pSide) && squares[sqDst] == 0)){
				continue;
			}
			sqDst += BODYGUARD_DELTA[i];
			pieceDst = squares[sqDst];
			if ((pieceDst & pieceSelfSide) == 0 || illeagal){
				mMoves[genCount++] = Move(sqSrc, sqDst);
			}
		}
		
		mMoves[genCount] = -1;
		return mMoves;
	}
	
	public static int[] GenHorseMoves(int squares[], final int sqSrc, final int pSide, final boolean illeagal){
		int sqDst, pieceDst, genCount = 0;
		final int pieceSelfSide = SideTag(pSide);
		
		for (int i = 0; i < 4; ++i){
			sqDst = sqSrc + KING_DELTA[i];
			if (squares[sqDst] != 0){
				continue;
			} 
			for (int j = 0; j < 2; ++j){
				sqDst = sqSrc + HORSE_DELTA[i][j];
				if (!InBoard(sqDst)){
					continue;
				}
				pieceDst = squares[sqDst];
				if ((pieceDst & pieceSelfSide) == 0 || illeagal){
					mMoves[genCount++] = Move(sqSrc, sqDst);
				}
			}
		}
		
		mMoves[genCount] = -1;
		return mMoves;
	}
	
	public static int[] GenRookMoves(int squares[], final int sqSrc, final int pSide,final boolean illeagal){
		int sqDst, pieceDst, genCount = 0, nDelta;
		final int pieceOppSide = OppSideTag(pSide);
		
		for (int i = 0; i < 4; ++i){
			nDelta = KING_DELTA[i];
			sqDst = sqSrc + nDelta;
			while (InBoard(sqDst)){
				pieceDst = squares[sqDst];
				if (pieceDst == 0){
					mMoves[genCount++] = Move(sqSrc, sqDst);
				}else{
					if (((pieceDst & pieceOppSide) != 0) || illeagal){
						mMoves[genCount++] = Move(sqSrc, sqDst);
					}
					break;
				}
				sqDst += nDelta;
			}
		}
		
		mMoves[genCount] = -1;
		return mMoves;
	}
	
	public static int[] GenCannonMoves(int squares[], final int sqSrc, final int pSide, final boolean illeagal){
		int sqDst, pieceDst, genCount = 0, nDelta;
		final int pieceOppSide = OppSideTag(pSide);
		
		for (int i = 0; i < 4; ++i){
			nDelta = KING_DELTA[i];
			sqDst = sqSrc + nDelta;
			while (InBoard(sqDst)){
				pieceDst = squares[sqDst];
				if (pieceDst == 0){
					mMoves[genCount++] = Move(sqSrc, sqDst);
				}else{
					break;
				}
				sqDst += nDelta;
			}
			
			sqDst += nDelta;
			while (InBoard(sqDst)){
				pieceDst = squares[sqDst];
				if (pieceDst != 0){
					if ((pieceDst & pieceOppSide) != 0 || illeagal){
						mMoves[genCount++] = Move(sqSrc, sqDst);
					}
					break;
				}
				sqDst += nDelta;
			}
		}
		
		mMoves[genCount] = -1;
		return mMoves;
	}
	
	public static int[] GenPawnMoves(int squares[], int sqSrc, int pSide, boolean illeagal){
		int sqDst, pieceDst, genCount = 0, nDelta;
		int pieceSelfSide = SideTag(pSide);
		
		sqDst = SquareForward(sqSrc, pSide);
		if (InBoard(sqDst)){
			pieceDst = squares[sqDst];
			if ((pieceDst & pieceSelfSide) == 0 || illeagal){
				mMoves[genCount++] = Move(sqSrc, sqDst);
			}
		}
		if (AwayHalf(sqSrc, pSide)){
			for (nDelta = -1; nDelta <= 1; nDelta += 2){
				sqDst = sqSrc + nDelta;
				if (InBoard(sqDst)){
					pieceDst = squares[sqDst];
					if ((pieceDst & pieceSelfSide) == 0 || illeagal){
						mMoves[genCount++] = Move(sqSrc, sqDst);
					}
				}
			}
		}
		
		mMoves[genCount] = -1;
		return mMoves;
	}
	
	public static int GenKingFace(int squares[], final int sqSrc){

		int kingPosition;
		for (kingPosition = 0; kingPosition < BOARD_NUMBER; ++kingPosition){
			if (InFort(kingPosition) && squares[kingPosition]!= 0 && kingPosition != sqSrc){
				int piece = squares[kingPosition];
				int type = piece & 0x7; 
				if (type == PIECE_KING){
					int mv = Move(sqSrc, kingPosition);
					if (LegalKingFace(squares, mv)){
						return mv;
					}
					return -1;
				}
			}
		}
		return -1;
	}
	
	public static boolean LeagalMove(int[] squares, int mv, int pSide){
		int sqSrc, sqDst, sqPin;
		int pieceSelfSide, pieceSrc, pieceDst, nDelta;
		
		// 判断走法是否合法，需要经过以下的判断过程：
		// 1. 判断起始格是否有自己的棋子
		sqSrc = Src(mv);
		pieceSrc = squares[sqSrc];
		pieceSelfSide = SideTag(pSide);
		if ((pieceSrc & pieceSelfSide) == 0){
			return false;
		}
		
		// 2. 判断目标格是否有自己的棋子
		sqDst = Dst(mv);
		pieceDst = squares[sqDst];
		if ((pieceDst & pieceSelfSide) != 0){
			return false;
		}
		
		// 3. 根据棋子的类型检查走法是否合理
		switch (pieceSrc - pieceSelfSide){
		case PIECE_KING:
			return InFort(sqDst) && KingSpan(sqSrc, sqDst) || LegalKingFace(squares, mv);
		case PIECE_BODYGUARD:
			return InFort(sqDst) && BodyguardSpan(sqSrc, sqDst);
		case PIECE_BISHOP:
			return SameHalf(sqSrc, sqDst) && BishopSpan(sqSrc, sqDst) && squares[BishopPin(sqSrc, sqDst)] == 0;
		case PIECE_HORSE:
			sqPin = HorsePin(sqSrc, sqDst);
			return sqPin != sqSrc && squares[sqPin] == 0;
		case PIECE_ROOK:
		case PIECE_CANNON:
			if (SameRank(sqSrc, sqDst)){
				nDelta = (sqDst < sqSrc ? -1 : 1);
			}else if (SameFile(sqSrc, sqDst)){
				nDelta = (sqDst < sqSrc ? -16 : 16);
			}else{
				return false;
			}
			sqPin = sqSrc + nDelta;
			while (sqPin != sqDst && squares[sqPin] == 0){
				sqPin += nDelta;
			}
			if (sqPin == sqDst){
				return pieceDst == 0 || pieceSrc - pieceSelfSide == PIECE_ROOK;
			}else if (pieceDst != 0 && pieceSrc - pieceSelfSide == PIECE_CANNON){
				sqPin += nDelta;
				while (sqPin != sqDst && squares[sqPin] == 0){
					sqPin += nDelta;
				}
				return sqPin == sqDst;
			}else{
				return false;
			}
		case PIECE_PAWN:
			if (AwayHalf(sqDst, pSide) && (sqDst == sqSrc - 1 || sqDst == sqSrc + 1)){
				return true;
			}
			return sqDst == SquareForward(sqSrc, pSide);
		default:
			return false;
		}
	}
	
	private static boolean LegalKingFace(int[] squares, int mv){
		int sqSrc = Src(mv);
		final int sqDst = Dst(mv);
		if (!SameFile(sqSrc, sqDst)){
			return false;
		}
		int pieceSrc = squares[sqSrc];
		int pieceDst = squares[sqDst];
		if (pieceSrc == pieceDst){
			return false;
		}
		if (pieceSrc == 0 || pieceDst == 0){
			return false;
		}
		
		int pieceType = pieceSrc & 0x7;
		if (pieceType != PIECE_KING){
			return false;
		}
		pieceType = pieceDst & 0x7;
		if (pieceType != PIECE_KING){
			return false;
		}
		
		int nDelta = (sqDst < sqSrc ? -16 : 16);
		sqSrc += nDelta;
		while (InBoard(sqSrc) && sqSrc != sqDst){
			if (squares[sqSrc] != 0){
				return false;
			}
			sqSrc += nDelta;
		}
		
		System.out.println("LegalKingFace true");
		return true;
	}

	/*protected int AddMove(int nFromX,int nFromY, int nToX, int nToY,int nPly){
		m_MoveList[nPly][m_nMoveCount].From.x = (byte) nFromX;
		m_MoveList[nPly][m_nMoveCount].From.y = (byte) nFromY;
		m_MoveList[nPly][m_nMoveCount].To.x = (byte) nToX;
		m_MoveList[nPly][m_nMoveCount].To.y = (byte) nToY;
		m_nMoveCount++;
		return m_nMoveCount;
	}*/
}
