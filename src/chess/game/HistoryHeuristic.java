package chess.game;

/**
 * HistoryHeuristic.java 
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

import java.util.Arrays;
import chess.game.Define.CHESSMOVE;

public class HistoryHeuristic {
	protected final int m_HistoryTable[][] = new int[90][90];
	protected final CHESSMOVE m_TargetBuff[] = new CHESSMOVE[100]; 
	public HistoryHeuristic(){}
		
	public void ResetHistoryTable(){
		Arrays.fill(m_HistoryTable, 10);
	}
	public int GetHistoryScore(CHESSMOVE move){
		int nFrom, nTo;
		
		nFrom = move.From.y*9+move.From.x;
		nTo = move.To.y*9+move.To.x;
		
		return m_HistoryTable[nFrom][nTo];
	}
	public void EnterHistoryScore(CHESSMOVE move,int depth){
		int nFrom, nTo;
		nFrom = move.From.y*9+move.From.x;
		nTo = move.To.y*9+move.To.x;

		m_HistoryTable[nFrom][nTo] += 2<<depth;
	}
	public void MergeSort(CHESSMOVE source[], int n, boolean direction){
		int s = 1;
		while(s < n)
		{
			MergePass(source, m_TargetBuff, s, n, direction);
			s += s;
			MergePass(m_TargetBuff, source, s, n, direction);
			s += s;
		}
	}
	
	protected void Merge(CHESSMOVE source[], CHESSMOVE target[], int l,int m, int r){
		int i = l;
		int j = m + 1;
		int k = l;
		
		while((i <= m) && (j <= r))
			if (source[i].Score <= source[j].Score)
				target[k++] = source[i++];
			else
				target[k++] = source[j++];
			
			if(i > m)
				for (int q = j; q <= r; q++)
					target[k++] = source[q];
				else
					for(int q = i; q <= m; q++)
						target[k++] = source[q];
	}
	protected void MergePass(CHESSMOVE source[], CHESSMOVE target[],
			final  int s, final  int n, final boolean direction){
		int i = 0;
		
		while(i <= n - 2 * s)
		{
			if (direction)
				Merge(source, target, i, i + s - 1, i + 2 * s - 1);
			else
				Merge_A(source, target, i, i + s - 1, i + 2 * s - 1);
			i=i+2*s;
		}
		
		if (i + s < n) 
		{
			if (direction)
				Merge(source, target, i, i + s - 1, n - 1);
			else
				Merge_A(source, target, i, i + s - 1, n - 1);
		}
		else
			for (int j = i; j <= n - 1; j++)
				target[j] = source[j];
	}
	protected void Merge_A(CHESSMOVE source[], CHESSMOVE target[], int l,int m, int r){
		int i = l;
		int j = m + 1;
		int k = l;
		
		while((i <= m) && (j <= r))
			if (source[i].Score >= source[j].Score)
				target[k++] = source[i++];
			else
				target[k++] = source[j++];
			
		if(i > m)
			for (int q = j; q <= r; q++)
				target[k++] = source[q];
		else
			for(int q = i; q <= m; q++)
				target[k++] = source[q];
	}
	
}
