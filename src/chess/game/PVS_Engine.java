package chess.game;

import java.util.Arrays;

/**
 * PVS_Engine.java 
 * chess 1.0
 * Date 2015/02/04
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

public class PVS_Engine extends SearchEngine{

	@Override
	public boolean SearchAGoodMove(byte[][] position) {
		
		for (int i = 0; i < position.length; i++){
			System.arraycopy(position[i], 0, CurPosition[i], 0, position[i].length);
		}
		m_nMaxDepth = m_nSearchDepth;

//		for (m_nMaxDepth = 1; m_nMaxDepth <= m_nSearchDepth; m_nMaxDepth++)
		
		   PrincipalVariation(m_nMaxDepth, -20000, 20000);
		
		
		MakeMove(m_cmBestMove);
		for (int i = 0; i < CurPosition.length; i++){
			System.arraycopy(CurPosition[i], 0, position[i], 0, CurPosition[i].length);
		}
		return false;
	}
	
	protected int PrincipalVariation(int depth, int alpha, int beta)
	{
		int score;
		int Count,i;
		byte type;
		int best;

		i = IsGameOver(CurPosition, depth);
		if (i != 0)
			return i;

		if (depth <= 0)	//叶子节点取估值
			return m_pEval.Evaluate(CurPosition, (m_nMaxDepth-depth)%2 != 0);
		
		Count = m_pMG.CreatePossibleMove(CurPosition, depth, (m_nMaxDepth-depth)%2);
		

		type = MakeMove(m_pMG.m_MoveList[depth][0]);
		best = -PrincipalVariation( depth-1, -beta, -alpha);
		UnMakeMove(m_pMG.m_MoveList[depth][0],type); 
		if(depth == m_nMaxDepth)
			m_cmBestMove = m_pMG.m_MoveList[depth][0];

		for (i=1;i<Count;i++) 
		{
			
			if(best < beta) 
			{
				if (best > alpha) 
					alpha = best;
				type = MakeMove(m_pMG.m_MoveList[depth][i]);
				score = -PrincipalVariation(depth-1, -alpha-1, -alpha);
				if (score > alpha && score < beta) 
				{
					best = -PrincipalVariation(depth-1, -beta, -score);
					if(depth == m_nMaxDepth)
						m_cmBestMove = m_pMG.m_MoveList[depth][i];
				}
				else if (score > best)
				{
					best = score;
					if(depth == m_nMaxDepth)
						m_cmBestMove = m_pMG.m_MoveList[depth][i];
				}
				UnMakeMove(m_pMG.m_MoveList[depth][i],type); 
			}
		}

		return best;
	}

}
