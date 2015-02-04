package chess.game;

/**
 * TranspositionTable.java 
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

import java.util.Random;
import chess.game.Define.CHESSMOVE;

public class TranspositionTable {
	enum ENTRY_TYPE{exact, lower_bound, upper_bound};
	public class HashItem {
		long checksum;
		ENTRY_TYPE entry_type;
		short depth;
		short eval;
	}
	private Random random;
	public final int m_nHashKey32[][][] = new int[15][10][9];
	public final long m_ulHashKey64[][][] = new long[15][10][9];
	public final HashItem m_pTT[][] = new HashItem[10][];
	public int m_HashKey32;
	public long m_HashKey64;
	public TranspositionTable(){
		InitializeHashKey();
	}
	
	public void InitializeHashKey(){
		int i,j,k;
		random = new Random();
		
		for (i = 0; i < 15; i++)
			for (j = 0; j < 10; j++)
				for (k = 0; k < 9; k++)
				{
					m_nHashKey32[i][j][k] = rand32();
					m_ulHashKey64[i][j][k] = rand64();
				}

		m_pTT[0] = new HashItem[1024*1024];
		m_pTT[1] = new HashItem[1024*1024];
	}
	public void CalculateInitHashKey(byte CurPosition[][])
	{
		int j,k,nChessType;
		m_HashKey32 = 0;
		m_HashKey32 = 0;
		for (j = 0; j < 10; j++)
			for (k = 0; k < 9; k++)
			{
				nChessType = CurPosition[j][k];
				if (nChessType != Define.NOCHESS)
				{
					m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nChessType][j][k]; 
					m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nChessType][j][k]; 
				}
			}
	}
	public void Hash_MakeMove(CHESSMOVE move, byte CurPosition[][])
	{
		byte nToID, nFromID;
		nFromID = CurPosition[move.From.y][move.From.x];
		nToID = CurPosition[move.To.y][move.To.x];

		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nFromID][move.From.y][move.From.x]; 
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nFromID][move.From.y][move.From.x]; 

		if (nToID != Define.NOCHESS)
		{
			m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][move.To.y][move.To.x]; 
			m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][move.To.y][move.To.x]; 
		}
		
		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nFromID][move.To.y][move.To.x]; 
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nFromID][move.To.y][move.To.x]; 
	}
	public void Hash_UnMakeMove(CHESSMOVE move, byte nChessID, byte CurPosition[][])
	{
		byte nToID;
		nToID = CurPosition[move.To.y][move.To.x];
		
		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][move.From.y][move.From.x]; 
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][move.From.y][move.From.x]; 

		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][move.To.y][move.To.x]; 
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][move.To.y][move.To.x]; 
		
		if (nChessID != 0)
		{
			m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nChessID][move.To.y][move.To.x]; 
			m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nChessID][move.To.y][move.To.x]; 
		}
	}
	public int LookUpHashTable(int alpha, int beta, int depth,int TableNo)
	{
		int x;
		HashItem pht;

		x = m_HashKey32 & 0xFFFFF;
		pht = m_pTT[TableNo][x];

	    if (pht.depth >= depth && pht.checksum == m_HashKey64)
		{
			switch (pht.entry_type) 
			{
			case exact: 
				return pht.eval;
			case lower_bound:
				if (pht.eval >= beta)
					return (pht.eval);
				else 
					break;
			case upper_bound:
				if (pht.eval <= alpha)
					return (pht.eval);
				else 
					break;
	        }
		}

		return 66666;
	}
	public void EnterHashTable(ENTRY_TYPE entry_type, short eval, short depth,int TableNo)
	{
		int x;
		HashItem pht;

		x = m_HashKey32 & 0xFFFFF;//二十位哈希地址
		pht = m_pTT[TableNo][x];

		pht.checksum = m_HashKey64;
		pht.entry_type = entry_type;
		pht.eval = eval;
		pht.depth = depth;
	}

	private long rand64(){
		 return random.nextLong();
	}
	private int rand32(){
		return random.nextInt();
	}
	
}
