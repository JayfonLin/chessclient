package chess.game;

/**
 * Created 2015-02-05
 * @author jeff
 */

import chess.game.TranspositionTable.ENTRY_TYPE;
import static chess.game.ChessLoadUtil.*;
import static chess.game.Constant.*;

public class NegaScout_TT_HH extends SearchEngine{
	protected TranspositionTable TT;
	protected HistoryHeuristic HH;
	protected final int moves[] = new int[80];
	
	public NegaScout_TT_HH(){
		TT = new TranspositionTable();
		HH = new HistoryHeuristic();
	}
	
	@Override
	public boolean SearchAGoodMove(int[] squares) {
		
		System.arraycopy(squares, 0, CurPosition, 0, squares.length);
		m_nMaxDepth = m_nSearchDepth;
		TT.CalculateInitHashKey(CurPosition);
		HH.ResetHistoryTable();
//		m_nMaxDepth = 1;
//		NegaScout(m_nMaxDepth, -Constant.INFINITY, Constant.INFINITY);
//		m_nMaxDepth = m_nSearchDepth;
//		for (m_nMaxDepth = 1; m_nMaxDepth <= m_nSearchDepth; m_nMaxDepth++)
		NegaScout(m_nMaxDepth, -Constant.INFINITY, Constant.INFINITY);
		
		boolean isKill = false;
		int sqDst = ChessLoadUtil.Dst(m_cmBestMove.Move);
		if (CurPosition[sqDst] != 0)
			isKill = true;
		
		/*int bestscore = TT.LookUpHashTable(-Constant.INFINITY, Constant.INFINITY, m_nMaxDepth, 1); 
		int mmm = m_cmBestMove.Move;
		System.out.printf("best_move [eval:%d, from<%d, %d>, to<%d, %d>]\n",
				bestscore, (RankY(Src(mmm))-FILE_LEFT+1), (FileX(Src(mmm))-RANK_TOP+1),
				(RankY(Dst(mmm))-FILE_LEFT+1), (FileX(Dst(mmm))-RANK_TOP+1));*/
		
		MakeMove(m_cmBestMove);
		
		System.arraycopy(CurPosition, 0, squares, 0, CurPosition.length);
		return isKill;
	}

	/**
	 * 负极大值搜索函数
	 * @param depth 表示节点离叶子节点的层数
	 * @param alpha
	 * @param beta
	 * @return
	 */
	protected int NegaScout(int depth, int alpha, int beta){
		int Count,i;
		int type;
		int a,b,t;
		int side;
		int score;
		
		i = IsGameOver(CurPosition, depth);
		if (i != 0)
			return i;
		
		side = 1-(m_nMaxDepth-depth)%2;
		
		score = TT.LookUpHashTable(alpha, beta, depth, side); 
		if (score != Constant.INVALID_SCORE) 
			return score;
		
		if (depth <= 0)	//叶子节点取估值
		{
			score = m_pEval.Evaluate(CurPosition, side == 0);
			TT.EnterHashTable(ENTRY_TYPE.exact, (short)score, (short)depth, side);
			return score;
		}
		
		//m_nMoveCount = 0;
		Count = m_pMG.CreatePossibleMove(CurPosition, moves, depth, side);
		AddMoves(Count, depth);
		
		for (i = 0; i < Count; ++i) 
		{
			m_MoveList[depth][i].Score = HH.GetHistoryScore(m_MoveList[depth][i]);
		}
		HH.MergeSort(m_MoveList[depth], Count, false);
		
		int bestmove = -1;
		
	    a = alpha;
	    b = beta;
	    int eval_is_exact = 0;
	    for (i = 0; i < Count; ++i){
			TT.Hash_MakeMove(m_MoveList[depth][i], CurPosition);
			type = MakeMove(m_MoveList[depth][i]);
			
			//递归搜索子节点，对第一个节点是全窗口，其后是空窗探测
			t = -NegaScout(depth-1 , -b, -a);
			
			//对于第一个后的节点，如果上面的搜索fail high
			if (t > a && t < beta && i > 0){
				a = -NegaScout (depth-1, -beta, -t);     /* re-search */
				eval_is_exact = 1; //设置数据类型为精确值
				if(depth == m_nMaxDepth)
					m_cmBestMove = m_MoveList[depth][i];
				bestmove = i; //记住最佳走法的位置
			}
			
			TT.Hash_UnMakeMove(m_MoveList[depth][i], type, CurPosition); 
			UnMakeMove(m_MoveList[depth][i],type); 
			if (a < t){
				eval_is_exact = 1;
				a = t;
				if(depth == m_nMaxDepth)
					m_cmBestMove = m_MoveList[depth][i];
			}
			if (a >= beta) 
			{
				TT.EnterHashTable(ENTRY_TYPE.lower_bound, (short)a, (short)depth, side);
				HH.EnterHistoryScore(m_MoveList[depth][i], depth);
				return a;
			}
			b = a + 1;                      /* set new null window */
		}
		if (bestmove != -1)
			HH.EnterHistoryScore(m_MoveList[depth][bestmove], depth);
		if (eval_is_exact != 0) 
			TT.EnterHashTable(ENTRY_TYPE.exact, (short)a, (short)depth, side);
		else 
			TT.EnterHashTable(ENTRY_TYPE.upper_bound, (short)a, (short)depth, side);
		return a;
	}
	
	protected void AddMoves(int count, int nPly){
		for (int i = 0; i < count; ++i){
			m_MoveList[nPly][i].Move = moves[i];
		}
	}
}
