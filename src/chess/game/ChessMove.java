package chess.game;


/*
 * 一个走法的结构
 */
public class ChessMove
{
	public short ChessID;	//标明是什么棋子
	public ChessManPos From = new ChessManPos();
	public ChessManPos To = new ChessManPos();			
	public int Score;
	public ChessMove(){}
}