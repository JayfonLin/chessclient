package chess.logic;


/**
 * Created on 2015-10-07
 * @author jeff
 *
 */

import android.os.Handler;

public class HandlerManager {

	private Handler game_view_handler;
	
	private static HandlerManager handler_manager;
	
	private HandlerManager(){}
	
	public static HandlerManager GetInstance(){
		if (handler_manager == null){
			synchronized(HandlerManager.class){
				handler_manager = new HandlerManager();
			}
		}
		
		return handler_manager;
	}
	
	public void SetGameViewHandler(Handler handler){
		game_view_handler = handler;
	}
	
	public Handler GetGameViewHandler(){
		return game_view_handler;
	}
}
