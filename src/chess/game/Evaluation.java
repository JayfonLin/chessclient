package chess.game;

/**
 * 
 * created on 2015-02-04
 * @author jeff
 */

import static chess.game.ChessLoadUtil.*;
import static chess.game.Constant.*;

import java.util.Arrays;


public class Evaluation {
	public final static int BASEVALUE_PAWN = 100;
	public final static int BASEVALUE_BODYGUARD = 250;
	public final static int BASEVALUE_BISHOP = 250;
	public final static int BASEVALUE_ROOK = 500;
	public final static int BASEVALUE_HORSE = 350;
	public final static int BASEVALUE_CANNON = 350;
	public final static int BASEVALUE_KING = 10000;

	public final static int FLEXIBILITY_PAWN = 15;
	public final static int FLEXIBILITY_BODYGUARD = 1;
	public final static int FLEXIBILITY_BISHOP = 1;
	public final static int FLEXIBILITY_ROOK = 6;
	public final static int FLEXIBILITY_HORSE = 12;
	public final static int FLEXIBILITY_CANNON = 6;
	public final static int FLEXIBILITY_KING = 0;
	
	
	
	protected final int m_BaseValue[] = new int[PIECE_COUNT];
	protected final int m_FlexValue[] = new int[PIECE_COUNT];
	protected final short m_AttackPos[] = new short[BOARD_NUMBER];
	protected final short m_GuardPos[] = new short[BOARD_NUMBER];
	protected final short m_FlexibilityPos[] = new short[BOARD_NUMBER];
	protected final int m_chessValue[] = new int[BOARD_NUMBER];
	protected int nPosCount;
	protected final int RelatePos[] = new int[SINGLE_GEN_MOVE_SIZE];
			
	public Evaluation(){
		m_BaseValue[B_KING] = BASEVALUE_KING; 
		m_BaseValue[B_ROOK] = BASEVALUE_ROOK; 
		m_BaseValue[B_HORSE] = BASEVALUE_HORSE; 
		m_BaseValue[B_BODYGUARD] = BASEVALUE_BODYGUARD; 
		m_BaseValue[B_BISHOP] = BASEVALUE_BISHOP; 
		m_BaseValue[B_CANNON] = BASEVALUE_CANNON; 
		m_BaseValue[B_PAWN] = BASEVALUE_PAWN; 

		m_BaseValue[R_KING] = BASEVALUE_KING; 
		m_BaseValue[R_ROOK] = BASEVALUE_ROOK; 
		m_BaseValue[R_HORSE] = BASEVALUE_HORSE; 
		m_BaseValue[R_BODYGUARD] = BASEVALUE_BODYGUARD; 
		m_BaseValue[R_BISHOP] = BASEVALUE_BISHOP; 
		m_BaseValue[R_CANNON] = BASEVALUE_CANNON; 
		m_BaseValue[R_PAWN] = BASEVALUE_PAWN; 

		m_FlexValue[B_KING] = FLEXIBILITY_KING; 
		m_FlexValue[B_ROOK] = FLEXIBILITY_ROOK; 
		m_FlexValue[B_HORSE] = FLEXIBILITY_HORSE; 
		m_FlexValue[B_BODYGUARD] = FLEXIBILITY_BODYGUARD; 
		m_FlexValue[B_BISHOP] = FLEXIBILITY_BISHOP; 
		m_FlexValue[B_CANNON] = FLEXIBILITY_CANNON; 
		m_FlexValue[B_PAWN] = FLEXIBILITY_PAWN; 

		m_FlexValue[R_KING] = FLEXIBILITY_KING; 
		m_FlexValue[R_ROOK] = FLEXIBILITY_ROOK; 
		m_FlexValue[R_HORSE] = FLEXIBILITY_HORSE; 
		m_FlexValue[R_BODYGUARD] = FLEXIBILITY_BODYGUARD; 
		m_FlexValue[R_BISHOP] = FLEXIBILITY_BISHOP; 
		m_FlexValue[R_CANNON] = FLEXIBILITY_CANNON; 
		m_FlexValue[R_PAWN] = FLEXIBILITY_PAWN; 
			
		
	}
	
	protected int ProtectAttck(int[] squares, boolean bIsRedTurn){
		int nChessType, nTargetType;
		int side = bIsRedTurn ? 0 : 1; //0代表红方，1代表黑方
		int i, k;
		
		for (i = 0; i < BOARD_NUMBER; ++i){
			if (!InBoard(i)){
				continue;
			}
			
			if (squares[i] != 0){
				nChessType = squares[i];
				GetRelatePiece(squares, i, side);
				
				for (k = 0; k < nPosCount; ++k){
					nTargetType = squares[RelatePos[k]];
					if (nTargetType == 0){
						m_FlexibilityPos[i]++;	
					}
					else{
						if (SameSide(nChessType, nTargetType)){
							m_GuardPos[RelatePos[k]]++;
						}else{
							m_AttackPos[RelatePos[k]]++;
							m_FlexibilityPos[i]++;	
							switch (nTargetType){
							case R_KING:
								if (!bIsRedTurn)
									return 18888;
								
							case B_KING:
								if (bIsRedTurn)
									return 18888;
								
							default:
								m_AttackPos[RelatePos[k]] += (30 + (m_BaseValue[nTargetType] 
										- m_BaseValue[nChessType])/10)/10;
								break;
							}
						}
					}
				}
			}
		}
		
		return 0;
	}
	
	protected void GetFlexibility(int[] squares){
		int i, nChessType;
		for (i = 0; i < BOARD_NUMBER; ++i){
			if (!InBoard(i)){
				continue;
			}
			
			if (squares[i] != 0){
				nChessType = squares[i];
				m_chessValue[i]++;
				m_chessValue[i] += m_FlexValue[nChessType] * m_FlexibilityPos[i];
				
				//TODO
				m_chessValue[i] += GetPOSValue(i, nChessType);
			}
		}
	}
	
	protected int GetChessValues(int[] squares, boolean bIsRedTurn){
		int nHalfvalue, i, nChessType;
		for (i = 0; i < BOARD_NUMBER; ++i){
			if (!InBoard(i)){
				continue;
			}
			
			if (squares[i] == 0){
				continue;
			}
			
			nChessType = squares[i];
			nHalfvalue = m_BaseValue[nChessType]/16;
			
			//这个可能要去掉
			m_chessValue[i] += m_BaseValue[nChessType];
			
			if (IsRed(nChessType)){
				if (m_AttackPos[i] != 0){
					if (bIsRedTurn){
						if (nChessType == R_KING){
							m_chessValue[i] -= 20;
						}
						else{
							m_chessValue[i] -= nHalfvalue * 2;
							if (m_GuardPos[i] != 0)
								m_chessValue[i] += nHalfvalue;
						}
					}
					else{
						if (nChessType == R_KING)
							return 18888;
						m_chessValue[i] -= nHalfvalue*10;
						if (m_GuardPos[i] != 0)
							m_chessValue[i] += nHalfvalue*9;
					}
					m_chessValue[i] -= m_AttackPos[i];
				}
				else{
					if (m_GuardPos[i] != 0)
						m_chessValue[i] += 5;
				}
			}
			else{
				if (m_AttackPos[i] != 0){
					if (!bIsRedTurn){
						if (nChessType == B_KING){
							m_chessValue[i]-= 20;
						}
						else{
							m_chessValue[i] -= nHalfvalue * 2;
							if (m_GuardPos[i] != 0)
								m_chessValue[i] += nHalfvalue;
						}
					}
					else{
						if (nChessType == B_KING)
							return 18888;
						m_chessValue[i] -= nHalfvalue*10;
						if (m_GuardPos[i] != 0)
							m_chessValue[i] += nHalfvalue*9;
					}
					m_chessValue[i] -= m_AttackPos[i];
				}
				else{
					if (m_GuardPos[i] != 0)
						m_chessValue[i] += 5;
				}
			}
		}
		
		return 0;
	}
	
	protected int GetScore(int[] squares, boolean bIsRedTurn){
		int nRedValue = 0, nBlackValue = 0, nChessType, i;

		for(i = 0; i < BOARD_NUMBER; i++){
			if (!InBoard(i)){
				continue;
			}
			
			nChessType = squares[i];
			if (nChessType == 0){
				continue;
			}
				
			if (IsRed(nChessType))
			{
				nRedValue += m_chessValue[i];	
			}
			else
			{
				nBlackValue += m_chessValue[i];	
			}
		}
			
		if (bIsRedTurn){
			return nRedValue - nBlackValue;
		}
		return  nBlackValue-nRedValue ;
	}
	
	public int Evaluate(int[] squares, boolean bIsRedTurn){
		
		Arrays.fill(m_chessValue, 0);
		Arrays.fill(m_AttackPos, (short)0);
		Arrays.fill(m_GuardPos, (short)0);
		Arrays.fill(m_FlexibilityPos, (short)0);
		
		int score = ProtectAttck(squares, bIsRedTurn);
		if (score != 0){
			return score;
		}

		GetFlexibility(squares);
		score = GetChessValues(squares, bIsRedTurn);
		if (score != 0){
			return score;
		}
		
		return GetScore(squares, bIsRedTurn);
	}

	protected int GetRelatePiece(int[] squares, int sqSrc, int pSide){
		nPosCount = 0;
		int nChessID;
		boolean illeagal = true;
		int mvs[];
		
		nChessID = squares[sqSrc] & 0x7;
		switch(nChessID)
		{
		case PIECE_KING:
			mvs = MoveGenerator.GenKingMoves(squares, sqSrc, pSide, illeagal);
			AddPoints(mvs);
			
			int mv = MoveGenerator.GenKingFace(squares, sqSrc);
			if (mv != -1){
				AddPoint(mv);
			}
			
			break;
					
		case PIECE_BODYGUARD:
			mvs = MoveGenerator.GenBodyguardMoves(squares, sqSrc, pSide, illeagal);
			AddPoints(mvs);
			break;
					
		case PIECE_BISHOP:
			mvs = MoveGenerator.GenBishopMoves(squares, sqSrc, pSide, illeagal);
			AddPoints(mvs);
			break;
			
		
		case PIECE_HORSE:		
			mvs = MoveGenerator.GenHorseMoves(squares, sqSrc, pSide, illeagal);
			AddPoints(mvs);
			break;
			
		case PIECE_ROOK:
			mvs = MoveGenerator.GenRookMoves(squares, sqSrc, pSide, illeagal);
			AddPoints(mvs);
			break;
		
		case PIECE_PAWN:
			mvs = MoveGenerator.GenPawnMoves(squares, sqSrc, pSide, illeagal);
			AddPoints(mvs);
			break;
			
		case PIECE_CANNON:
			mvs = MoveGenerator.GenCannonMoves(squares, sqSrc, pSide, illeagal);
			AddPoints(mvs);
			break;
			
		default:
			break;
			
		}
		return nPosCount ;		
	}
	
	protected void AddPoint(int mv){
		int sqDst = Dst(mv);
		RelatePos[nPosCount++] = sqDst;
	}
	
	protected void AddPoints(int[] mvs){
		int sqDst;
		for (int mv : mvs){
			if (mv == -1){
				break;
			}
			sqDst = Dst(mv);
			RelatePos[nPosCount++] = sqDst;
		}
	}
	
	public static int GetPOSValue(int sq, int pc) { 
	    
	    if (IsRed(pc)) {
	      return PIECE_POS_VALUE[pc&0x7][sq];
	    } else {
	      return PIECE_POS_VALUE[pc&0x7][SquareFlip(sq)];
	    }
	}

}
