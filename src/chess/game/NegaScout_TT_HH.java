package chess.game;

/**
 * NegaScout_TT_HH.java 
 * chess 1.0
 * Date 2015/02/05
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

import chess.game.TranspositionTable.ENTRY_TYPE;

public class NegaScout_TT_HH extends SearchEngine{
	protected TranspositionTable TT;
	protected HistoryHeuristic HH;
	public NegaScout_TT_HH(){
		TT = new TranspositionTable();
		HH = new HistoryHeuristic();
	}
	@Override
	public void SearchAGoodMove(byte[][] position) {
		for (int i = 0; i < position.length; i++){
			System.arraycopy(position[i], 0, CurPosition[i], 0, position[i].length);
		}
		m_nMaxDepth = m_nSearchDepth;
		TT.CalculateInitHashKey(CurPosition);
		HH.ResetHistoryTable();
//		m_nMaxDepth = 1;
//		NegaScout(m_nMaxDepth, -20000, 20000);
//		m_nMaxDepth = m_nSearchDepth;
//		for (m_nMaxDepth = 1; m_nMaxDepth <= m_nSearchDepth; m_nMaxDepth++)
		NegaScout(m_nMaxDepth, -20000, 20000);
		MakeMove(m_cmBestMove);
		for (int i = 0; i < CurPosition.length; i++){
			System.arraycopy(CurPosition[i], 0, position[i], 0, CurPosition[i].length);
		}
	}

	protected int NegaScout(int depth, int alpha, int beta){
		int Count,i;
		byte type;
		int a,b,t;
		int side;
		int score;
		
		i = IsGameOver(CurPosition, depth);
		if (i != 0)
			return i;
		
		side = (m_nMaxDepth-depth)%2;
		
		score = TT.LookUpHashTable(alpha, beta, depth, side); 
		if (score != 66666) 
			return score;
		
		if (depth <= 0)	
		{
			score = m_pEval.Evaluate(CurPosition, side != 0);
			TT.EnterHashTable(ENTRY_TYPE.exact, (short)score, (short)depth, side);
			return score;
		}
		
		Count = m_pMG.CreatePossibleMove(CurPosition, depth, side);
		
		for (i=0;i<Count;i++) 
		{
			m_pMG.m_MoveList[depth][i].Score = 
				HH.GetHistoryScore(m_pMG.m_MoveList[depth][i]);
		}
		HH.MergeSort(m_pMG.m_MoveList[depth], Count, false);
		
		int bestmove=-1;
		
	    a = alpha;
	    b = beta;
	    int eval_is_exact = 0;
	    for ( i = 0; i < Count; i++ ) 
		{
			TT.Hash_MakeMove(m_pMG.m_MoveList[depth][i], CurPosition);
			type = MakeMove(m_pMG.m_MoveList[depth][i]);
			
			t = -NegaScout(depth-1 , -b, -a );
			
			if (t > a && t < beta && i > 0) 
			{
				a = -NegaScout (depth-1, -beta, -t );     /* re-search */
				eval_is_exact = 1;
				if(depth == m_nMaxDepth)
					m_cmBestMove = m_pMG.m_MoveList[depth][i];
				bestmove = i;
			}
			TT.Hash_UnMakeMove(m_pMG.m_MoveList[depth][i],type, CurPosition); 
			UnMakeMove(m_pMG.m_MoveList[depth][i],type); 
			if (a < t)
			{
				eval_is_exact = 1;
				a=t;
				if(depth == m_nMaxDepth)
					m_cmBestMove = m_pMG.m_MoveList[depth][i];
			}
			if ( a >= beta ) 
			{
				TT.EnterHashTable(ENTRY_TYPE.lower_bound, (short)a, (short)depth,side);
				HH.EnterHistoryScore(m_pMG.m_MoveList[depth][i], depth);
				return a;
			}
			b = a + 1;                      /* set new null window */
		}
		if (bestmove != -1)
		HH.EnterHistoryScore(m_pMG.m_MoveList[depth][bestmove], depth);
		if (eval_is_exact != 0) 
			TT.EnterHashTable(ENTRY_TYPE.exact, (short)a, (short)depth,side);
		else 
			TT.EnterHashTable(ENTRY_TYPE.upper_bound, (short)a, (short)depth,side);
		return a;
	}
}
