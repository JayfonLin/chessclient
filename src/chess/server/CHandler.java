package chess.server;

import java.util.HashMap;
import java.util.Map;
import chess.logic.CPlayGame;
import chess.network.CBinUnpacker;

/**
 * Created on 2015-10-05
 * @author jeff
 *
 */

public class CHandler {

	
	static final Map<Integer, IPackageHandler> HANDLERS = new HashMap<Integer, IPackageHandler>();
	
	
	static {
		HANDLERS.put(CProto.CHESS_MOVE, chess.logic.CPlayGame.GetInstance().new OnEnermyChessMove());
		
		HANDLERS.put(CProto.CMD_UNMAKE_MOVE, chess.logic.CPlayGame.GetInstance().new OnUnmakeMove());
	}
	
	public static void OnPackage(CBinUnpacker pack){
		int cmd = pack.ReadDWord();
		System.out.println("OnPackage command: " + cmd);
		HANDLERS.get(cmd).DoCommand(pack);
	}
}
