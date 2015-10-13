package chess.logic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import chess.network.CBinPacker;
import chess.network.CBinUnpacker;
import chess.network.CTCPClient;
import chess.server.CProto;
import chess.server.IPackageHandler;
import chess.ui.GameView;

/**
 * Created on 2015-10-05
 * @author jeff
 *
 */
public class CPlayGame {
	
	boolean is_start = false;
	
	private static CPlayGame instance;
	
	private CPlayGame(){
		
	}
	
	public static CPlayGame GetInstance(){
		
		if (instance == null){
			synchronized (CPlayGame.class) {
				if (instance == null){
					instance = new CPlayGame();
				}
			}
		}
		
		return instance;
	}

	public boolean IsStart(){
		return is_start;
	}
	
	public void StartupChessGame(byte search_depth){
		CTCPClient client = CTCPClient.GetInstance();
		CBinPacker packet = new CBinPacker();
		packet.PackDWord(CProto.CHESS_START_UP);
		packet.PackByte(search_depth);
		client.Send(packet);
		
		is_start = true;
	}
	
	public void ChessMove(int move){
		
		CTCPClient client = CTCPClient.GetInstance();
		CBinPacker packet = new CBinPacker();
		packet.PackDWord(CProto.CHESS_MOVE);
		packet.PackDWord(move);
		client.Send(packet);
		
	}
	
	public void UnmakeMove(){
		
		CTCPClient client = CTCPClient.GetInstance();
		CBinPacker packet = new CBinPacker();
		packet.PackDWord(CProto.CMD_UNMAKE_MOVE);
		client.Send(packet);
		
	}
	
	public class OnEnermyChessMove implements IPackageHandler
	{

		@Override
		public void DoCommand(CBinUnpacker pack) {
			int move = pack.ReadDWord();
			byte is_kill = pack.ReadByte();
			Handler handler = HandlerManager.GetInstance().GetGameViewHandler();
			Message msg = new Message();
			msg.what = GameView.MSG_WHAT_CHESS_MOVE;
			msg.arg1 = move;
			msg.arg2 = is_kill;
			
			handler.sendMessage(msg);
		}
		
	}
	
	public class OnUnmakeMove implements IPackageHandler
	{

		@Override
		public void DoCommand(CBinUnpacker pack) {
			System.out.println("OnUnmakeMove");
			int move = pack.ReadDWord();
			byte chess_id = pack.ReadByte();
			
			Message msg = new Message();
			msg.what = GameView.MSG_WHAT_CHESS_WITHDRAW;
			msg.arg1 = move;
			msg.arg2 = chess_id;
			
			Handler handler = HandlerManager.GetInstance().GetGameViewHandler();
			handler.sendMessage(msg);
		}
		
	}
}
