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

import chess.game.Define;
import chess.ui.ChessView;

public class MoveGenerator {
	protected int m_nMoveCount;
	public final ChessMove m_MoveList[][] = new ChessMove[10][80];
	
	public MoveGenerator(){
		for (int i = 0; i < m_MoveList.length; i++){
			for (int j = 0; j < m_MoveList[i].length; j++){
				m_MoveList[i][j] = new ChessMove();
			}
		}
	}
	
	public int CreatePossibleMove(byte position[][], int nPly,int nSide){
		int nChessID;
		int i,j;
		m_nMoveCount = 0;
		for (i = 0; i < 10; i++)
			for (j = 0; j < 9; j++){
				if (position[i][j] != Define.NOCHESS){
					nChessID = position[i][j];
					if (nSide == 0 && Define.IsRed(nChessID))
						continue;
					if (nSide == 1 && Define.IsBlack(nChessID))
						continue;
					switch(nChessID)
					{
					case Define.R_KING:
					case Define.B_KING:
						Gen_KingMove(position, i, j, nPly);
						break;
						
					case Define.R_BISHOP:
						Gen_RBishopMove(position, i, j, nPly);
						break;
						
					case Define.B_BISHOP:
						Gen_BBishopMove(position, i, j, nPly);
						break;
						
					case Define.R_ELEPHANT:
					case Define.B_ELEPHANT:
						Gen_ElephantMove(position, i, j, nPly);
						break;
						
					case Define.R_HORSE:		
					case Define.B_HORSE:		
						Gen_HorseMove(position, i, j, nPly);
						break;
						
					case Define.R_CAR:
					case Define.B_CAR:
						Gen_CarMove(position, i, j, nPly);
						break;
						
					case Define.R_PAWN:
						Gen_RPawnMove(position, i, j, nPly);
						break;
						
					case Define.B_PAWN:
						Gen_BPawnMove(position, i, j, nPly);
						break;
						
					case Define.B_CANON:
					case Define.R_CANON:
						Gen_CanonMove(position, i, j, nPly);
						break;
						
					default:
						break;
						
					}
				}
			}
			
		return m_nMoveCount;
	}
	
	protected void Gen_KingMove(byte position[][], int i, int j, int nPly){
		int x, y;
		for (y = 0; y < 3; y++)
			for (x = 3; x < 6; x++)
				if (IsValidMove(position, j, i, x, y))
					AddMove(j, i, x, y, nPly);
		for (y = 7; y < 10; y++)
			for (x = 3; x < 6; x++)
				if (IsValidMove(position, j, i, x, y))
					AddMove(j, i, x, y, nPly);
	}
	
	protected void Gen_RBishopMove(byte position[][], int i, int j, int nPly){
		int x,  y;
		for (y = 7; y < 10; y++)
			for (x = 3; x < 6; x++)
				if (IsValidMove(position, j, i, x, y))
					AddMove(j, i, x, y, nPly);
	}

	protected void Gen_BBishopMove(byte position[][], int i, int j, int nPly){
		int x,  y;
		for (y = 0; y < 3; y++)
			for (x = 3; x < 6; x++)
				if (IsValidMove(position, j, i, x, y))
					AddMove(j, i, x, y, nPly);
	}
	
	protected void Gen_ElephantMove(byte position[][], int i, int j, int nPly)
	{
		int x,  y;

		x=j+2;
		y=i+2;
		if(x < 9 && y < 10  && IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
		x=j+2;
		y=i-2;
		if(x < 9 && y>=0  &&  IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
		x=j-2;
		y=i+2;
		if(x>=0 && y < 10  && IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
		x=j-2;
		y=i-2;
		if(x>=0 && y>=0  && IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
	}
	

	protected void Gen_HorseMove(byte position[][], int i, int j, int nPly)
	{
		int x,  y;
	
		x=j+2;
		y=i+1;
		if((x < 9 && y < 10) &&IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
		x=j+2;
		y=i-1;
		if((x < 9 && y >= 0) &&IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
		x=j-2;
		y=i+1;
		if((x >= 0 && y < 10) &&IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
		x=j-2;
		y=i-1;
		if((x >= 0 && y >= 0) &&IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
		x=j+1;
		y=i+2;
		if((x < 9 && y < 10) &&IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		x=j-1;
		y=i+2;
		if((x >= 0 && y < 10) &&IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		x=j+1;
		y=i-2;
		if((x < 9 && y >= 0) &&IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		x=j-1;
		y=i-2;
		if((x >= 0 && y >= 0) &&IsValidMove(position, j, i, x, y))
			AddMove(j, i, x, y, nPly);
		
	}
	
	protected void Gen_RPawnMove(byte position[][], int i, int j, int nPly)
	{
		int x, y;
		int nChessID;

		nChessID = position[i][j];

		y = i - 1;
		x = j;
		
		if(y > 0 && !Define.IsSameSide(nChessID, position[y][x]))
			AddMove(j, i, x, y, nPly);
		
		if(i < 5)
		{
			y=i;
			x=j+1;
			if(x < 9 && !Define.IsSameSide(nChessID, position[y][x]))
				AddMove(j, i, x, y, nPly);
			x=j-1;
			if(x >= 0 && !Define.IsSameSide(nChessID, position[y][x]))
				AddMove(j, i, x, y, nPly);
		}
	}


	protected void Gen_BPawnMove(byte position[][], int i, int j, int nPly)
	{
		int x, y;
		int nChessID;

		nChessID = position[i][j];

		y = i + 1;
		x = j;
		
		if(y < 10 && !Define.IsSameSide(nChessID, position[y][x]))
			AddMove(j, i, x, y, nPly);
		
		if(i > 4)
		{
			y=i;
			x=j+1;
			if(x < 9 && !Define.IsSameSide(nChessID, position[y][x]))
				AddMove(j, i, x, y, nPly);
			x=j-1;
			if(x >= 0 && !Define.IsSameSide(nChessID, position[y][x]))
				AddMove(j, i, x, y, nPly);
		}
		
	}

	protected void Gen_CarMove(byte position[][], int i, int j, int nPly)
	{
		int x,  y;
		int nChessID;

		nChessID = position[i][j];
		
		x=j+1;
		y=i;
		while(x < 9)
		{
			if( Define.NOCHESS == position[y][x] )
				AddMove(j, i, x, y, nPly);
			else
			{
				if(!Define.IsSameSide(nChessID, position[y][x]))
					AddMove(j, i, x, y, nPly);
				break;
			}
			x++;
		}
		
		x = j-1;
		y = i;
		while(x >= 0)
		{
			if( Define.NOCHESS == position[y][x] )
				AddMove(j, i, x, y, nPly);
			else
			{
				if(!Define.IsSameSide(nChessID, position[y][x]))
					AddMove(j, i, x, y, nPly);
				break;
			}
			x--;
		}
		
		x=j;
		y=i+1;
		while(y < 10)
		{
			if( Define.NOCHESS == position[y][x])
				AddMove(j, i, x, y, nPly);
			else
			{
				if(!Define.IsSameSide(nChessID, position[y][x]))
					AddMove(j, i, x, y, nPly);
				break;
			}
			y++;
		}
		
		x = j;
		y = i-1;
		while(y>=0)
		{
			if( Define.NOCHESS == position[y][x])
				AddMove(j, i, x, y, nPly);
			else
			{
				if(!Define.IsSameSide(nChessID, position[y][x]))
					AddMove(j, i, x, y, nPly);
				break;
			}
			y--;
		}
	}

	protected void Gen_CanonMove(byte position[][], int i, int j, int nPly)
	{
		int x, y;
		boolean flag;
		int nChessID;

		nChessID = position[i][j];
		
		x=j+1;		
		y=i;
		flag=false;
		while(x < 9)		
		{
			if( Define.NOCHESS == position[y][x] )
			{
				if(!flag)
					AddMove(j, i, x, y, nPly);
			}
			else
			{
				if(!flag)
					flag=true;
				else 
				{
					if(!Define.IsSameSide(nChessID, position[y][x]))
						AddMove(j, i, x, y, nPly);
					break;
				}
			}
			x++;
		}
		
		x=j-1;
		flag=false;	
		while(x>=0)
		{
			if( Define.NOCHESS == position[y][x] )
			{
				if(!flag)
					AddMove(j, i, x, y, nPly);
			}
			else
			{
				if(!flag)
					flag=true;
				else 
				{
					if(!Define.IsSameSide(nChessID, position[y][x]))
						AddMove(j, i, x, y, nPly);
					break;
				}
			}
			x--;
		}
		x=j;	
		y=i+1;
		flag=false;
		while(y < 10)
		{
			if( Define.NOCHESS == position[y][x] )
			{
				if(!flag)
					AddMove(j, i, x, y, nPly);
			}
			else
			{
				if(!flag)
					flag=true;
				else 
				{
					if(!Define.IsSameSide(nChessID, position[y][x]))
						AddMove(j, i, x, y, nPly);
					break;
				}
			}
			y++;
		}
		
		y=i-1;	
		flag=false;	
		while(y>=0)
		{
			if( Define.NOCHESS == position[y][x] )
			{
				if(!flag)
					AddMove(j, i, x, y, nPly);
			}
			else
			{
				if(!flag)
					flag=true;
				else 
				{
					if(!Define.IsSameSide(nChessID, position[y][x]))
						AddMove(j, i, x, y, nPly);
					break;
				}
			}
			y--;
		}
		
	}

	protected int AddMove(int nFromX,int nFromY, int nToX, int nToY,int nPly){
		//System.out.println("nPly: "+nPly+", m_nMoveCount: "+m_nMoveCount);
		//if (m_nMoveCount >= 80) return m_nMoveCount;
		m_MoveList[nPly][m_nMoveCount].From.x = (byte) nFromX;
		m_MoveList[nPly][m_nMoveCount].From.y = (byte) nFromY;
		m_MoveList[nPly][m_nMoveCount].To.x = (byte) nToX;
		m_MoveList[nPly][m_nMoveCount].To.y = (byte) nToY;
		m_nMoveCount++;
		return m_nMoveCount;
	}
	
	public static boolean IsValidMove(byte position[][], int nFromX, int nFromY, int nToX, int nToY){
		int i = 0, j = 0;
		int nMoveChessID, nTargetID;
		
		if (nFromY ==  nToY && nFromX == nToX)
			return false;//目的与源相同
		
		nMoveChessID = position[nFromY][nFromX];
		nTargetID = position[nToY][nToX];
		
		if (Define.IsSameSide(nMoveChessID, nTargetID))
			return false;//不能吃自己的棋
		
		switch(nMoveChessID)
		{
		case Define.B_KING:     
			if (nTargetID == Define.R_KING)//老将见面?
			{
				if (nFromX != nToX)
					return false;
				for (i = nFromY + 1; i < nToY; i++)
					if (position[i][nFromX] != Define.NOCHESS)
						return false;
			}
			else
			{
				if (nToY > 2 || nToX > 5 || nToX < 3)
					return false;//目标点在九宫之外
				if(Math.abs(nFromY - nToY) + Math.abs(nToX - nFromX) > 1) 
					return false;//将帅只走一步直线:
			}
			break;
		case Define.R_BISHOP:   
			
			if (nToY < 7 || nToX > 5 || nToX < 3)
				return false;//士出九宫	
			
			if (Math.abs(nFromY - nToY) != 1 || Math.abs(nToX - nFromX) != 1)
				return false;	//士走斜线
			
			break;
			
		case Define.B_BISHOP:   //黑士
			
			if (nToY > 2 || nToX > 5 || nToX < 3)
				return false;//士出九宫	
			
			if (Math.abs(nFromY - nToY) != 1 || Math.abs(nToX - nFromX) != 1)
				return false;	//士走斜线
			
			break;
			
		case Define.R_ELEPHANT://红象
			
			if(nToY < 5)
				return false;//相不能过河
			
			if(Math.abs(nFromX-nToX) != 2 || Math.abs(nFromY-nToY) != 2)
				return false;//相走田字
			
			if(position[(nFromY + nToY) / 2][(nFromX + nToX) / 2] != Define.NOCHESS)
				return false;//相眼被塞住了
			
			break;
			
		case Define.B_ELEPHANT://黑象 
			
			if(nToY > 4)
				return false;//相不能过河
			
			if(Math.abs(nFromX-nToX) != 2 || Math.abs(nFromY-nToY) != 2)
				return false;//相走田字
			
			if(position[(nFromY + nToY) / 2][(nFromX + nToX) / 2] != Define.NOCHESS)
				return false;//相眼被塞住了
			
			break;
			
		case Define.B_PAWN:     //黑兵
			
			if(nToY < nFromY)
				return false;//兵不回头
			
			if( nFromY < 5 && nFromY == nToY)
				return false;//兵过河前只能直走
			
			if(nToY - nFromY + Math.abs(nToX - nFromX) > 1)
				return false;//兵只走一步直线:
			
			break;
			
		case Define.R_PAWN:    //红兵
			
			if(nToY > nFromY)
				return false;//兵不回头
			
			if( nFromY > 4 && nFromY == nToY)
				return false;//兵过河前只能直走
			
			if(nFromY - nToY + Math.abs(nToX - nFromX) > 1)
				return false;//兵只走一步直线:
			
			break;
			
		case Define.R_KING:     
			if (nTargetID == Define.B_KING)//老将见面?
			{
				if (nFromX != nToX)
					return false;//两个将不在同一列
				for (i = nFromY - 1; i > nToY; i--)
					if (position[i][nFromX] != Define.NOCHESS)
						return false;//中间有别的子
			}
			else
			{
				if (nToY < 7 || nToX > 5 || nToX < 3)
					return false;//目标点在九宫之外
				if(Math.abs(nFromY - nToY) + Math.abs(nToX - nFromX) > 1) 
					return false;//将帅只走一步直线:
			}
			break;
			
		case Define.B_CAR:      
		case Define.R_CAR:      
			
			if(nFromY != nToY && nFromX != nToX)
				return false;	//车走直线:
			
			if(nFromY == nToY)
			{
				if(nFromX < nToX)
				{
					for(i = nFromX + 1; i < nToX; i++)
						if(position[nFromY][i] != Define.NOCHESS)
							return false;
				}
				else
				{
					for(i = nToX + 1; i < nFromX; i++)
						if(position[nFromY][i] != Define.NOCHESS)
							return false;
				}
			}
			else
			{
				if(nFromY < nToY)
				{
					for(j = nFromY + 1; j < nToY; j++)
						if(position[j][nFromX] != Define.NOCHESS)
							return false;
				}
				else
				{
					for(j= nToY + 1; j < nFromY; j++)
						if(position[j][nFromX] != Define.NOCHESS)
							return false;
				}
			}
			
			break;
			
		case Define.B_HORSE:    
		case Define.R_HORSE:    
			
			if(!((Math.abs(nToX-nFromX)==1 && Math.abs(nToY-nFromY)==2)
				||(Math.abs(nToX-nFromX)==2&&Math.abs(nToY-nFromY)==1)))
				return false;//马走日字
			
			if	(nToX-nFromX==2)
			{
				i=nFromX+1;
				j=nFromY;
			}
			else if	(nFromX-nToX==2)
			{
				i=nFromX-1;
				j=nFromY;
			}
			else if	(nToY-nFromY==2)
			{
				i=nFromX;
				j=nFromY+1;
			}
			else if	(nFromY-nToY==2)
			{
				i=nFromX;
				j=nFromY-1;
			}
			
			if(position[j][i] != Define.NOCHESS)
				return false;//绊马腿
			
			break;
		
		case Define.B_CANON:    
		case Define.R_CANON:    
			
			if(nFromY!=nToY && nFromX!=nToX)
				return false;	//炮走直线:
			
			//炮不吃子时经过的路线中不能有棋子
			
			if(position[nToY][nToX] == Define.NOCHESS)
			{
				if(nFromY == nToY)
				{
					if(nFromX < nToX)
					{
						for(i = nFromX + 1; i < nToX; i++)
							if(position[nFromY][i] != Define.NOCHESS)
								return false;
					}
					else
					{
						for(i = nToX + 1; i < nFromX; i++)
							if(position[nFromY][i]!=Define.NOCHESS)
								return false;
					}
				}
				else
				{
					if(nFromY < nToY)
					{
						for(j = nFromY + 1; j < nToY; j++)
							if(position[j][nFromX] != Define.NOCHESS)
								return false;
					}
					else
					{
						for(j = nToY + 1; j < nFromY; j++)
							if(position[j][nFromX] != Define.NOCHESS)
								return false;
					}
				}
			}
			//炮吃子时
			else	
			{
				int count=0;
				if(nFromY == nToY)
				{
					if(nFromX < nToX)
					{
						for(i=nFromX+1;i<nToX;i++)
							if(position[nFromY][i]!=Define.NOCHESS)
								count++;
						if(count != 1)
							return false;
					}
					else
					{
						for(i=nToX+1;i<nFromX;i++)
							if(position[nFromY][i] != Define.NOCHESS)
								count++;
						if(count!=1)
							return false;
					}
				}
				else
				{
					if(nFromY<nToY)
					{
						for(j=nFromY+1;j<nToY;j++)
							if(position[j][nFromX]!=Define.NOCHESS)
								count++;
						if(count!=1)
							return false;
					}
					else
					{
						for(j=nToY+1;j<nFromY;j++)
							if(position[j][nFromX] != Define.NOCHESS)
								count++;
						if(count!=1)
							return false;
					}
				}
			}
			break;
		default:
			return false;
		}
		
		return true;
	}
}
