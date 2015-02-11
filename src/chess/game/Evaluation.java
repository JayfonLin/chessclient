package chess.game;

/**
 * Evaluation.java 
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


import java.util.Arrays;


public class Evaluation {
	public final static int BASEVALUE_PAWN = 100;
	public final static int BASEVALUE_BISHOP = 250;
	public final static int BASEVALUE_ELEPHANT = 250;
	public final static int BASEVALUE_CAR = 500;
	public final static int BASEVALUE_HORSE = 350;
	public final static int BASEVALUE_CANON = 350;
	public final static int BASEVALUE_KING = 10000;

	public final static int FLEXIBILITY_PAWN = 15;
	public final static int FLEXIBILITY_BISHOP = 1;
	public final static int FLEXIBILITY_ELEPHANT = 1;
	public final static int FLEXIBILITY_CAR = 6;
	public final static int FLEXIBILITY_HORSE = 12;
	public final static int FLEXIBILITY_CANON = 6;
	public final static int FLEXIBILITY_KING = 0;
	
	public final static int BA0[][]=
	{
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{90,90,110,120,120,120,110,90,90},
		{90,90,110,120,120,120,110,90,90},
		{70,90,110,110,110,110,110,90,70},
		{70,70,70, 70, 70,  70, 70,70,70},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
	};

	public final static int BA1[][]=
	{
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{0,  0,  0,  0,  0,  0,  0,  0,  0},
		{70,70,70, 70, 70,70, 70,70, 70},
		{70,90,110,110,110,110,110,90,70},
		{90,90,110,120,120,120,110,90,90},
		{90,90,110,120,120,120,110,90,90},
		{0,  0, 0,  0,  0,  0,  0,  0,  0},
	};
	
	protected final int m_BaseValue[] = new int[15];
	protected final int m_FlexValue[] = new int[15];
	protected final short m_AttackPos[][] = new short[10][9];
	protected final byte m_GuardPos[][] = new byte[10][9];
	protected final byte m_FlexibilityPos[][] = new byte[10][9];
	protected final int m_chessValue[][] = new int[10][9];
	protected int nPosCount;
	protected final ChessManPos RelatePos[] = new ChessManPos[20];

	public Evaluation(){
		m_BaseValue[Define.B_KING] = BASEVALUE_KING; 
		m_BaseValue[Define.B_CAR] = BASEVALUE_CAR; 
		m_BaseValue[Define.B_HORSE] = BASEVALUE_HORSE; 
		m_BaseValue[Define.B_BISHOP] = BASEVALUE_BISHOP; 
		m_BaseValue[Define.B_ELEPHANT] = BASEVALUE_ELEPHANT; 
		m_BaseValue[Define.B_CANON] = BASEVALUE_CANON; 
		m_BaseValue[Define.B_PAWN] = BASEVALUE_PAWN; 

		m_BaseValue[Define.R_KING] = BASEVALUE_KING; 
		m_BaseValue[Define.R_CAR] = BASEVALUE_CAR; 
		m_BaseValue[Define.R_HORSE] = BASEVALUE_HORSE; 
		m_BaseValue[Define.R_BISHOP] = BASEVALUE_BISHOP; 
		m_BaseValue[Define.R_ELEPHANT] = BASEVALUE_ELEPHANT; 
		m_BaseValue[Define.R_CANON] = BASEVALUE_CANON; 
		m_BaseValue[Define.R_PAWN] = BASEVALUE_PAWN; 

		m_FlexValue[Define.B_KING] = FLEXIBILITY_KING; 
		m_FlexValue[Define.B_CAR] = FLEXIBILITY_CAR; 
		m_FlexValue[Define.B_HORSE] = FLEXIBILITY_HORSE; 
		m_FlexValue[Define.B_BISHOP] = FLEXIBILITY_BISHOP; 
		m_FlexValue[Define.B_ELEPHANT] = FLEXIBILITY_ELEPHANT; 
		m_FlexValue[Define.B_CANON] = FLEXIBILITY_CANON; 
		m_FlexValue[Define.B_PAWN] = FLEXIBILITY_PAWN; 

		m_FlexValue[Define.R_KING] = FLEXIBILITY_KING; 
		m_FlexValue[Define.R_CAR] = FLEXIBILITY_CAR; 
		m_FlexValue[Define.R_HORSE] = FLEXIBILITY_HORSE; 
		m_FlexValue[Define.R_BISHOP] = FLEXIBILITY_BISHOP; 
		m_FlexValue[Define.R_ELEPHANT] = FLEXIBILITY_ELEPHANT; 
		m_FlexValue[Define.R_CANON] = FLEXIBILITY_CANON; 
		m_FlexValue[Define.R_PAWN] = FLEXIBILITY_PAWN; 
		
		for (int i = 0; i < RelatePos.length; i++){
			RelatePos[i] = new ChessManPos();
		}
	}
	
    private int count = 0;
	public int Evaluate(byte position[][], boolean bIsRedTurn){
		int i, j, k;
		int nChessType, nTargetType;
		count++;

		for (i = 0; i < m_chessValue.length; i++)
			Arrays.fill(m_chessValue[i], 0);
		for (i = 0; i < m_AttackPos.length; i++)
			Arrays.fill(m_AttackPos[i], (short)0);
		for (i = 0; i < m_GuardPos.length; i++)
			Arrays.fill(m_GuardPos[i], (byte)0);
		for (i = 0; i < m_FlexibilityPos.length; i++)
			Arrays.fill(m_FlexibilityPos[i], (byte)0);
		

		for(i = 0; i < 10; i++)
			for(j = 0; j < 9; j++)
			{
				if(position[i][j] != Define.NOCHESS)
				{
					nChessType = position[i][j];
					GetRelatePiece(position, j, i);
					for (k = 0; k < nPosCount; k++)
					{
						nTargetType = position[RelatePos[k].y][RelatePos[k].x];
						if (nTargetType == Define.NOCHESS)
						{
							m_FlexibilityPos[i][j]++;	
						}
						else
						{
							if (Define.IsSameSide(nChessType, nTargetType))
							{
								m_GuardPos[RelatePos[k].y][RelatePos[k].x]++;
							}else
							{
								m_AttackPos[RelatePos[k].y][RelatePos[k].x]++;
								m_FlexibilityPos[i][j]++;	
								switch (nTargetType)
								{
								case Define.R_KING:
									if (!bIsRedTurn)
										return 18888;
									break;
								case Define.B_KING:
									if (bIsRedTurn)
										return 18888;
									break;
								default:
									m_AttackPos[RelatePos[k].y][RelatePos[k].x] 
											+= (30 + (m_BaseValue[nTargetType] - m_BaseValue[nChessType])/10)/10;
									break;
								}
							}
						}
					}
				}
			}

		for(i = 0; i < 10; i++)
			for(j = 0; j < 9; j++)
			{
				if(position[i][j] != Define.NOCHESS)
				{
					nChessType = position[i][j];
					m_chessValue[i][j]++;
					m_chessValue[i][j] += m_FlexValue[nChessType] * m_FlexibilityPos[i][j];
					m_chessValue[i][j] += GetBingValue(j, i, position);
				}
			}
		int nHalfvalue;
		for(i = 0; i < 10; i++)
			for(j = 0; j < 9; j++)
			{
				if(position[i][j] != Define.NOCHESS)
				{
					nChessType = position[i][j];
					nHalfvalue = m_BaseValue[nChessType]/16;
					m_chessValue[i][j] += m_BaseValue[nChessType];
					
					if (Define.IsRed(nChessType))
					{
						if (m_AttackPos[i][j] != 0)
						{
							if (bIsRedTurn)
							{
								if (nChessType == Define.R_KING)
								{
									m_chessValue[i][j]-= 20;
								}
								else
								{
									m_chessValue[i][j] -= nHalfvalue * 2;
									if (m_GuardPos[i][j] != 0)
										m_chessValue[i][j] += nHalfvalue;
								}
							}
							else
							{
								if (nChessType == Define.R_KING)
									return 18888;
								m_chessValue[i][j] -= nHalfvalue*10;
								if (m_GuardPos[i][j] != 0)
									m_chessValue[i][j] += nHalfvalue*9;
							}
							m_chessValue[i][j] -= m_AttackPos[i][j];
						}
						else
						{
							if (m_GuardPos[i][j] != 0)
								m_chessValue[i][j] += 5;
						}
					}
					else
					{
						if (m_AttackPos[i][j] != 0)
						{
							if (!bIsRedTurn)
							{
								if (nChessType == Define.B_KING)									
								{
									m_chessValue[i][j]-= 20;
								}
								else
								{
									m_chessValue[i][j] -= nHalfvalue * 2;
									if (m_GuardPos[i][j] != 0)
										m_chessValue[i][j] += nHalfvalue;
								}
							}
							else
							{
								if (nChessType == Define.B_KING)
									return 18888;
								m_chessValue[i][j] -= nHalfvalue*10;
								if (m_GuardPos[i][j] != 0)
									m_chessValue[i][j] += nHalfvalue*9;
							}
							m_chessValue[i][j] -= m_AttackPos[i][j];
						}
						else
						{
							if (m_GuardPos[i][j] != 0)
								m_chessValue[i][j] += 5;
						}
					}
				}
			}

		int nRedValue = 0; 
		int	nBlackValue = 0;

		for(i = 0; i < 10; i++)
			for(j = 0; j < 9; j++)
			{
				nChessType = position[i][j];
//				if (nChessType == R_KING || nChessType == B_KING)
//					m_chessValue[i][j] = 10000;	
				if (nChessType != Define.NOCHESS)
				{
					if (Define.IsRed(nChessType))
					{
						nRedValue += m_chessValue[i][j];	
					}
					else
					{
						nBlackValue += m_chessValue[i][j];	
					}
				}
			}
			if (bIsRedTurn)
			{
				return nRedValue - nBlackValue;
			}
			else
			{
				return  nBlackValue-nRedValue ;
			}
	}
	protected int GetRelatePiece(byte position[][], int j, int i){
		nPosCount = 0;
		byte nChessID;
		boolean flag;
		int x,y;
		
		nChessID = position[i][j];
		switch(nChessID)
		{
		case Define.R_KING:
		case Define.B_KING:
			
			for (y = 0; y < 3; y++)
				for (x = 3; x < 6; x++)
					if (CanTouch(position, j, i, x, y))
						AddPoint(x, y);
			for (y = 7; y < 10; y++)
				for (x = 3; x < 6; x++)
					if (CanTouch(position, j, i, x, y))
						AddPoint(x, y);
			break;
							
		case Define.R_BISHOP:
			
			for (y = 7; y < 10; y++)
				for (x = 3; x < 6; x++)
					if (CanTouch(position, j, i, x, y))
						AddPoint(x, y);
			break;
					
		case Define.B_BISHOP:
			
			for (y = 0; y < 3; y++)
				for (x = 3; x < 6; x++)
					if (CanTouch(position, j, i, x, y))
						AddPoint(x, y);
			break;
					
		case Define.R_ELEPHANT:
		case Define.B_ELEPHANT:
			
			x=j+2;
			y=i+2;
			if(x < 9 && y < 10  && CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			
			x=j+2;
			y=i-2;
			if(x < 9 && y>=0  &&  CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			
			x=j-2;
			y=i+2;
			if(x>=0 && y < 10  && CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			
			x=j-2;
			y=i-2;
			if(x>=0 && y>=0  && CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			break;
			
		case Define.R_HORSE:		
		case Define.B_HORSE:		
			x=j+2;
			y=i+1;
			if((x < 9 && y < 10) &&CanTouch(position, j, i, x, y))
				AddPoint(x, y);
					
			x=j+2;
			y=i-1;
			if((x < 9 && y >= 0) &&CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			
			x=j-2;
			y=i+1;
			if((x >= 0 && y < 10) &&CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			
			x=j-2;
			y=i-1;
			if((x >= 0 && y >= 0) &&CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			
			x=j+1;
			y=i+2;
			if((x < 9 && y < 10) &&CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			x=j-1;
			y=i+2;
			if((x >= 0 && y < 10) &&CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			x=j+1;
			y=i-2;
			if((x < 9 && y >= 0) &&CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			x=j-1;
			y=i-2;
			if((x >= 0 && y >= 0) &&CanTouch(position, j, i, x, y))
				AddPoint(x, y);
			break;
			
		case Define.R_CAR:
		case Define.B_CAR:
			x=j+1;
			y=i;
			while(x < 9)
			{
				if( Define.NOCHESS == position[y][x] )
					AddPoint(x, y);
				else
				{
					AddPoint(x, y);
					break;
				}
				x++;
			}
			
			x = j-1;
			y = i;
			while(x >= 0)
			{
				if( Define.NOCHESS == position[y][x] )
					AddPoint(x, y);
				else
				{
					AddPoint(x, y);
					break;
				}
				x--;
			}
			
			x=j;
			y=i+1;
			while(y < 10)
			{
				if( Define.NOCHESS == position[y][x])
					AddPoint(x, y);
				else
				{
					AddPoint(x, y);
					break;
				}
				y++;
			}
			
			x = j;
			y = i-1;
			while(y>=0)
			{
				if( Define.NOCHESS == position[y][x])
					AddPoint(x, y);
				else
				{
					AddPoint(x, y);
					break;
				}
				y--;
			}
			break;
			
		case Define.R_PAWN:
			y = i - 1;
			x = j;
			
			if(y >= 0)
				AddPoint(x, y);
			
			if(i < 5)
			{
				y=i;
				x=j+1;
				if(x < 9 )
					AddPoint(x, y);
				x=j-1;
				if(x >= 0 )
					AddPoint(x, y);
			}
			break;
			
		case Define.B_PAWN:
			y = i + 1;
			x = j;
			
			if(y < 10 )
				AddPoint(x, y);
			
			if(i > 4)
			{
				y=i;
				x=j+1;
				if(x < 9)
					AddPoint(x, y);
				x=j-1;
				if(x >= 0)
					AddPoint(x, y);
			}
			break;
			
		case Define.B_CANON:
		case Define.R_CANON:
			
			x=j+1;		
			y=i;
			flag=false;
			while(x < 9)		
			{
				if( Define.NOCHESS == position[y][x] )
				{
					if(!flag)
						AddPoint(x, y);
				}
				else
				{
					if(!flag)
						flag=true;
					else 
					{
						AddPoint(x, y);
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
						AddPoint(x, y);
				}
				else
				{
					if(!flag)
						flag=true;
					else 
					{
						AddPoint(x, y);
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
						AddPoint(x, y);
				}
				else
				{
					if(!flag)
						flag=true;
					else 
					{
						AddPoint(x, y);
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
						AddPoint(x, y);
				}
				else
				{
					if(!flag)
						flag=true;
					else 
					{
						AddPoint(x, y);
						break;
					}
				}
				y--;
			}
			break;
			
		default:
			break;
			
		}
		return nPosCount ;		
	}
	protected boolean CanTouch(byte position[][], int nFromX, int nFromY, int nToX, int nToY){
		int i = 0, j = 0;
		int nMoveChessID, nTargetID;
		
		if (nFromY ==  nToY && nFromX == nToX)
			return false;//目的与源相同
		
		nMoveChessID = position[nFromY][nFromX];
		nTargetID = position[nToY][nToX];
		
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
			
			//炮不吃子时经过的路线中不能有棋子:------------------
			
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
			//以上是炮不吃子-------------------------------------
			//吃子时:=======================================
			else	
			{
				int nCount=0;
				if(nFromY == nToY)
				{
					if(nFromX < nToX)
					{
						for(i=nFromX+1;i<nToX;i++)
							if(position[nFromY][i]!=Define.NOCHESS)
								nCount++;
							if(nCount != 1)
								return false;
					}
					else
					{
						for(i=nToX+1;i<nFromX;i++)
							if(position[nFromY][i] != Define.NOCHESS)
								nCount++;
							if(nCount!=1)
								return false;
					}
				}
				else
				{
					if(nFromY<nToY)
					{
						for(j=nFromY+1;j<nToY;j++)
							if(position[j][nFromX]!=Define.NOCHESS)
								nCount++;
							if(nCount!=1)
								return false;
					}
					else
					{
						for(j=nToY+1;j<nFromY;j++)
							if(position[j][nFromX] != Define.NOCHESS)
								nCount++;
							if(nCount!=1)
								return false;
					}
				}
			}
			//以上是炮吃子时================================
			break;
		default:
			return false;
		}
		
		return true;
	}
	protected void AddPoint(int x, int y){
		RelatePos[nPosCount].x = (byte) x;
		RelatePos[nPosCount].y = (byte) y;
		nPosCount++;
	}
	protected int GetBingValue(int x, int y, byte CurSituation[][]){
		if (CurSituation[y][x] == Define.R_PAWN)
			return BA0[y][x];
		
		if (CurSituation[y][x] == Define.B_PAWN)
			return BA1[y][x];

		return 0;
	}

}
