package chess.server;

/**
 * Created on 2015-10-05
 * @author jeff
 *
 */

public class CServerManager {
	private static CServerManager server_manager;
	
	private CServerManager(){}
	
	public static CServerManager GetInstance(){
		if (server_manager == null){
			synchronized (CServerManager.class) {
				if (server_manager == null){
					server_manager = new CServerManager();
				}
			}
		}
		
		return server_manager;
	}
	
	

}
