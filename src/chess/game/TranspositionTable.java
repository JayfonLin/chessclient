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

import static chess.game.Constant.*;
import static chess.game.ChessLoadUtil.*;

public class TranspositionTable {
	
	public enum ENTRY_TYPE{exact, lower_bound, upper_bound};
	protected static final int HASH_SIZE = 1024 * (2 << 6);
	
	/**
	 * 哈希表中元素的结构定义
	 */
	public class HashItem {
		long checksum; //64位校验码
		ENTRY_TYPE entry_type; //数据类型
		short depth; //取得此值时的层次
		short eval; //节点的值
	}
	
	private Random random;
	public final int m_nHashKey32[][] = new int[PIECE_COUNT][BOARD_NUMBER];
	public final long m_ulHashKey64[][] = new long[PIECE_COUNT][BOARD_NUMBER];
	public final HashItem m_pTT[][] = new HashItem[10][];
	public int m_HashKey32;
	public long m_HashKey64;
		
	public TranspositionTable(){
		InitializeHashKey();
	}
	
	public void InitializeHashKey(){
		int i, j;
		random = new Random();
		
		for (i = 0; i < PIECE_COUNT; ++i)
			for (j = 0; j < BOARD_NUMBER; ++j){
				if (!InBoard(j)){
					continue;
				}
				m_nHashKey32[i][j] = rand32();
				m_ulHashKey64[i][j] = rand64();
			}
	
		m_pTT[0] = new HashItem[HASH_SIZE]; //用于存放极大值的节点数据
 		m_pTT[1] = new HashItem[HASH_SIZE]; //用于存放极小值的节点数据
 		
 		for (i = 0; i < HASH_SIZE; ++i){
 			m_pTT[0][i] = new HashItem();
 			m_pTT[1][i] = new HashItem();
 		}
	}
	
	public void CalculateInitHashKey(int CurPosition[])
	{
		int j, nChessType;
		m_HashKey32 = 0;
		m_HashKey64 = 0;
		
		for (j = 0; j < BOARD_NUMBER; ++j){
			if (!InBoard(j)){
				continue;
			}
			
			nChessType = CurPosition[j];
			if (nChessType != 0)
			{
				m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nChessType][j]; 
				m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nChessType][j]; 
			}
		}
	}
	public void Hash_MakeMove(ChessMove move, int CurPosition[]){
		int nToID, nFromID;
		
		int sqSrc = Src(move.Move);
		int sqDst = Dst(move.Move);
		nFromID = CurPosition[sqSrc];
		nToID = CurPosition[sqDst];

		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nFromID][sqSrc]; 
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nFromID][sqSrc]; 

		if (nToID != 0){
			m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][sqDst]; 
			m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][sqDst]; 
		}
		
		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nFromID][sqDst]; 
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nFromID][sqDst]; 
	}
	
	public void Hash_UnMakeMove(ChessMove move, int nChessID, int CurPosition[]){
		int nToID;
		
		int sqSrc = Src(move.Move);
		int sqDst = Dst(move.Move);
		nToID = CurPosition[sqDst];
		
		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][sqSrc]; 
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][sqSrc]; 

		m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nToID][sqDst]; 
		m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nToID][sqDst]; 
		
		if (nChessID != 0){
			m_HashKey32 = m_HashKey32 ^ m_nHashKey32[nChessID][sqDst]; 
			m_HashKey64 = m_HashKey64 ^ m_ulHashKey64[nChessID][sqDst]; 
		}
	}
	
	public int LookUpHashTable(int alpha, int beta, int depth,int TableNo){
		int x;
		HashItem pht;

		x = m_HashKey32 & 0xFFFF;
		pht = m_pTT[TableNo][x];
	
		/*if (pht == null){
			return Constant.INVALID_SCORE;
		}*/
		
	    if (pht.depth >= depth && pht.checksum == m_HashKey64){
			switch (pht.entry_type) {
			case exact: 
				return pht.eval;
			case lower_bound:
				if (pht.eval >= beta) //新的beta更小，可直接返回之前的值
					return (pht.eval);
				else 
					break;
			case upper_bound:
				if (pht.eval <= alpha) //新的窗口更小，可直接返回之前的值
					return (pht.eval);
				else 
					break;
	        }
		}

		return Constant.INVALID_SCORE;
	}
	
	public void EnterHashTable(ENTRY_TYPE entry_type, short eval, short depth, int TableNo){
		/*System.out.printf("evaluate [entry_type:%d, eval:%d, depth:%d, table_no:%d]\n",
				0, eval, depth, TableNo);*/
		
		int x = m_HashKey32 & 0xFFFF;//二十位哈希地址
		HashItem pht = m_pTT[TableNo][x];

		pht.checksum = m_HashKey64;
		pht.entry_type = entry_type;
		pht.eval = eval;
		pht.depth = depth;
		
		//m_pTT[TableNo][x] = pht;
	}

	private long rand64(){
		 return random.nextLong();
	}
	private int rand32(){
		return random.nextInt();
	}
	
}
