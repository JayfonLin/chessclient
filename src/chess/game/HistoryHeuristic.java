package chess.game;

/**
 * 
 * Created on 2015-02-05
 * @author jeff
 */

import java.util.Arrays;

public class HistoryHeuristic {
	
	protected final int m_HistoryTable[][] = new int[Constant.BOARD_NUMBER][Constant.BOARD_NUMBER];
	protected final ChessMove m_TargetBuff[] = new ChessMove[100]; 
	
	public HistoryHeuristic(){
		for (int i = 0; i < m_TargetBuff.length; ++i){
			m_TargetBuff[i] = new ChessMove();
		}
	}
		
	public void ResetHistoryTable(){
		for (int i = 0; i < m_HistoryTable.length; ++i){
			Arrays.fill(m_HistoryTable[i], 10);
		}
		
	}
	
	public int GetHistoryScore(ChessMove move){
		int nFrom, nTo;
		
		nFrom = ChessLoadUtil.Src(move.Move);
		nTo = ChessLoadUtil.Dst(move.Move);
		
		return m_HistoryTable[nFrom][nTo];
	}
	
	public void EnterHistoryScore(ChessMove move,int depth){
		int nFrom, nTo;
		nFrom = ChessLoadUtil.Src(move.Move);
		nTo = ChessLoadUtil.Dst(move.Move);

		m_HistoryTable[nFrom][nTo] += 2<<depth;
	}
	
	public void MergeSort(ChessMove source[], int n, boolean direction){
		int s = 1;
		while(s < n)
		{
			MergePass(source, m_TargetBuff, s, n, direction);
			s += s;
			MergePass(m_TargetBuff, source, s, n, direction);
			s += s;
		}
	}
	
	protected void Merge(ChessMove source[], ChessMove target[], int l,int m, int r){
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
	protected void MergePass(ChessMove source[], ChessMove target[],
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
	protected void Merge_A(ChessMove source[], ChessMove target[], int l,int m, int r){
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
