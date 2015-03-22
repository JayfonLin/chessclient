package chess.game;

/**
 * created on 2015-03-21
 * @author jeff
 *
 */

import static chess.game.Constant.*;
public class ChessLoadUtil {

	// 判断棋子是否在棋盘中
	public static boolean InBoard(int piecePosition){
		return PIECE_IN_BOARD[piecePosition] != 0;
	}
	
	// 判断棋子是否在九宫中
	public static boolean InFort(int piecePosition){
		return PIECE_IN_FORT[piecePosition] != 0;
	}
	
	// 获得格子的纵坐标row
	public static int RankY(int piecePosition){
		return piecePosition >> 4;
	}
	
	// 获得格子的横坐标col
	public static int FileX(int piecePosition){
		return piecePosition & 0xf;
	}
	
	// 根据纵坐标和横坐标获得格子
	public static int CoordXY(int x, int y){
		return x + (y << 4);
	}
	
	// 翻转格子
	public static int SquareFlip(int piecePosition){
		return 0xfe - piecePosition;
	}

	// 纵坐标垂直镜像
	public static int FileFlip(int x){
		return 0xe - x;
	}
	
	// 横坐标水平镜像
	public static int RankFlip(int y){
		return 0xf - y;
	}
	
	// 格子水平镜像
	public static int MirrorSquare(int piecePosition){
		return CoordXY(FileFlip(FileX(piecePosition)), RankY(piecePosition));
	}
	
	// 兵向前一格
	public static int SquareFoward(int piecePosition, int side){
		return piecePosition - 0x10 + (side << 5);
	}
	
	// 走法是否符合帅(将)的步长
	public static boolean KingSpan(int pieceSrc, int pieceDst){
		return PIECE_LEGAL_SPAN[pieceDst - pieceSrc + 0x100] == 1;
	}
	
	// 走法是否符合仕(士)的步长
	public static boolean BishopSpan(int pieceSrc, int pieceDst){
		return PIECE_LEGAL_SPAN[pieceDst - pieceSrc + 0x100] == 2;
	}
	
	// 走法是否符合相(象)的步长
	public static boolean ElephantSpan(int pieceSrc, int pieceDst){
		return PIECE_LEGAL_SPAN[pieceDst - pieceSrc + 0x100] == 3;
	}
	
	// 相(象)眼的位置
	public static int BishopPin(int pieceSrc, int pieceDst){
		return (pieceSrc + pieceDst) >> 1;
	}
	
	// 马腿的位置
	public static int HorsePin(int pieceSrc, int pieceDst){
		return pieceSrc + HORSE_PIN[pieceDst - pieceSrc + 0x100];
	}
	
	// 是否未过河
	public static boolean HomeHalf(int pieceSrc, int side){
		return (pieceSrc & 0x80) != (side << 7);
	}
	
	// 是否已过河
	public static boolean AwayHalf(int pieceSrc, int side){
		return (pieceSrc & 0x80) == (side << 7);
	}
	
	// 是否在河的同一边
	public static boolean SameHalf(int pieceSrc, int pieceDst){
		return ((pieceSrc ^ pieceDst) & 0x80) == 0;
	}
	
	// 是否在同一行
	public static boolean SameRank(int pieceSrc, int pieceDst){
		return ((pieceSrc ^ pieceDst) & 0xf0) == 0;
	}
	
	// 是否在同一列
	public static boolean SameFile(int pieceSrc, int pieceDst){
		return ((pieceSrc ^ pieceDst) & 0x0f) == 0;
	}
	
	// 获得红黑标记(红子是8，黑子是16)
	public static int SideTag(int side){
		return 8 + (side << 3);
	}
	
	// 获得对方红黑标记
	public static int OppSideTag(int side){
		return 16 - (side << 3);
	}
	
	// 获得走法的起点
	public static int Src(int mv){
		return mv & 0xff;
	}
	
	// 获得走法的终点
	public static int Dst(int mv){
		return mv >> 8;
	}
	
	// 根据起点和终点获得走法
	public static int Move(int pieceSrc, int pieceDst){
		return pieceSrc + pieceDst * 256;
	}
	
	// 走法水平镜像
	public static int MirrorMove(int mv){
		return Move(MirrorSquare(Src(mv)), MirrorSquare(Dst(mv)));
	}
}
