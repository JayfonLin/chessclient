package chess.game;

/**
 * 
 * Created on 2015-02-03
 * @author jeff
 */

import static chess.game.ChessLoadUtil.InFort;
import static chess.game.Constant.*;

public abstract class SearchEngine {
	
	protected int CurPosition[] = new int[BOARD_NUMBER];
	public ChessMove m_cmBestMove;
	protected MoveGenerator m_pMG;
	protected Evaluation m_pEval;
	protected int m_nSearchDepth;
	protected int m_nMaxDepth;
	protected int m_nMoveCount;
	protected final ChessMove m_MoveList[][] = new ChessMove[10][80];
	
	public SearchEngine(){
		m_nSearchDepth = 3;
		m_pMG = new MoveGenerator();
		m_pEval = new Evaluation();
		
		initMoveList();
	}
	
	protected void initMoveList(){
		for (int i = 0; i < m_MoveList.length; i++){
			for (int j = 0; j < m_MoveList[i].length; j++){
				m_MoveList[i][j] = new ChessMove();
			}
		}
	}
	
	public abstract boolean SearchAGoodMove(int[] squares);
	
	public void SetSearchDepth(int nDepth){
		m_nSearchDepth = nDepth;
	}
	
	public void SetEvaluator(Evaluation pEval){m_pEval = pEval;};

	public void SetMoveGenerator(MoveGenerator pMG){m_pMG = pMG;};
	
	protected int MakeMove(ChessMove move){
		int nChessID;
		int sqSrc = ChessLoadUtil.Src(move.Move);
		int sqDst = ChessLoadUtil.Dst(move.Move);
		nChessID = CurPosition[sqDst];
		CurPosition[sqDst] = CurPosition[sqSrc];
		CurPosition[sqSrc] = 0;
		move.ChessID = (short)nChessID;
		return nChessID;
	}
	
	protected void UnMakeMove(ChessMove move, int nChessID){
		int sqSrc = ChessLoadUtil.Src(move.Move);
		int sqDst = ChessLoadUtil.Dst(move.Move);
		CurPosition[sqSrc] = CurPosition[sqDst];
		CurPosition[sqDst] = nChessID;
	}
	
	protected int IsGameOver(int[] squares, int nDepth){
		int i;
		boolean RedLive = false, BlackLive=false; 
		
		for (i = 0; i < Constant.BOARD_NUMBER; ++i){
			if (!InFort(i)){
				continue;
			}
			if (squares[i] == R_KING){
				RedLive = true;
			}
			if (squares[i] == B_KING){
				BlackLive = true;
			}
		}

		i = (m_nMaxDepth - nDepth + 1) % 2;
		
		if (!RedLive)
			if (i != 0)
				return 19990 + nDepth;
			else
				return -19990 - nDepth;
		if (!BlackLive)
			if (i != 0)
				return -19990 - nDepth;
			else
				return 19990 + nDepth;
		return 0;
	}
	
}
