package chess.server;

import chess.network.CBinUnpacker;

public interface IPackageHandler {
	
	public void DoCommand(CBinUnpacker pack);
	
}
