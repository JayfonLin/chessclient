package chess.game;

/**
 * MoveGenerator.java 
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


public abstract class SearchEngine {
	
	protected final byte CurPosition[][] = new byte[10][9];
	public ChessMove m_cmBestMove;
	protected MoveGenerator m_pMG;
	protected Evaluation m_pEval;
	protected int m_nSearchDepth;
	protected int m_nMaxDepth;
	
	public abstract boolean SearchAGoodMove(byte position[][]);
	
	public void SetSearchDepth(int nDepth){
		m_nSearchDepth = nDepth;
	}
	
	public void SetEvaluator(Evaluation pEval){m_pEval = pEval;};

	public void SetMoveGenerator(MoveGenerator pMG){m_pMG = pMG;};
	
	protected byte MakeMove(ChessMove move){
		byte nChessID;
		nChessID = CurPosition[move.To.y][move.To.x];
		CurPosition[move.To.y][move.To.x] = CurPosition[move.From.y][move.From.x];
		CurPosition[move.From.y][move.From.x] = Define.NOCHESS;
		return nChessID;
	}
	protected void UnMakeMove(ChessMove move, byte nChessID){
		CurPosition[move.From.y][move.From.x] = CurPosition[move.To.y][move.To.x];
		CurPosition[move.To.y][move.To.x] = nChessID;
	}
	protected int IsGameOver(byte position[][], int nDepth){
		int i,j;
		boolean RedLive = false, BlackLive=false; 
		
		for (i = 7; i < 10; i++)
			for (j = 3; j < 6; j++)
			{
				if (position[i][j] == Define.B_KING)
					BlackLive = true;
				if (position[i][j] == Define.R_KING)
					RedLive  = true;
			}

		for (i = 0; i < 3; i++)
			for (j = 3; j < 6; j++)
			{
				if (position[i][j] == Define.B_KING)
					BlackLive = true;
				if (position[i][j] == Define.R_KING)
					RedLive  = true;
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
