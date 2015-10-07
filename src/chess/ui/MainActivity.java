package chess.ui;

/**
 * Created on 2015-02-08
 * @author jeff
 */

import static chess.ui.ViewConstant.*;
import static chess.engine.Define.*;

import java.util.HashMap;

import chess.activity.R;
import chess.network.CTCPClient;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import chess.logic.CPlayGame;

public class MainActivity extends Activity {

	SoundPool soundPool;//声音池
	HashMap<Integer, Integer> soundPoolMap; //声音池中声音ID与自定义声音ID的Map
	PopupWindow mPopupWindow;
	private static final String[] depths = {"1层","2层","3层","4层","5层","6层","7层"};
	private ArrayAdapter<String> adapter;
	int searchDepth = 3;
	GameView gameView;
	TextView depth_tv;
	RelativeLayout select_depth_rl;
	PopupWindow depth_window;
	RadioGroup radioGroup;
	
	public CPlayGame play_game;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	public void init(){
		initApp();
		initPm();//调整屏幕分辨率
		initSound();//初始化声音资源
		ConnectServer();
		
		gameView = new GameView(this);
		setContentView(gameView);
		
		initViews();
		
		initGame();
	}
	
	public void initGame(){
		play_game = CPlayGame.GetInstance();
	}

	public void initApp(){
		//设置全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //设置横屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);//游戏过程中只允许多媒体音量,而不允许通化音量
	}
	
	public void ConnectServer(){
		CTCPClient client = CTCPClient.GetInstance();
	}

	public void initViews(){
		View popupView = getLayoutInflater().inflate(R.layout.select_depth_popup, null);
		mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		depth_tv = (TextView) popupView.findViewById(R.id.textView2);
		
		View depth_select_popup = getLayoutInflater().inflate(R.layout.level_select, null);
	    depth_window = new PopupWindow(depth_select_popup, LayoutParams.MATCH_PARENT, 
	    		LayoutParams.MATCH_PARENT, true);
		depth_window.setTouchable(true);
		depth_window.setOutsideTouchable(true);
		
		select_depth_rl = (RelativeLayout) popupView.findViewById(R.id.select_relativelayout);
		select_depth_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				depth_window.showAtLocation(gameView, Gravity.CENTER, 0, 0);
			}
		});
		
		radioGroup = (RadioGroup) depth_select_popup.findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				//searchDepth = arg0.getCheckedRadioButtonId()+1;
				switch(arg1){
				case R.id.radioButton1:
					searchDepth = 1;
					break;
				case R.id.radioButton2:
					searchDepth = 2; 
					break;
				case R.id.radioButton3:
					searchDepth = 3;
					break;
				case R.id.radioButton4:
					searchDepth = 4;
					break;
				case R.id.radioButton5:
					searchDepth = 5;
					break;
				case R.id.radioButton6:
					searchDepth = 6; 
					break;
				case R.id.radioButton7:
					searchDepth = 7;
					break;
				case R.id.radioButton8:
					searchDepth = 8;
					break;
				}
				System.out.println("search depth: "+searchDepth);
				depth_tv.setText(searchDepth+"层");
				depth_window.dismiss();
			}
		});
		
		
		Button startButton = (Button) popupView.findViewById(R.id.button1);
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mPopupWindow.dismiss();
				gameView.engine.SetSearchDepth(searchDepth);
				play_game.StartupChessGame((byte)searchDepth);
			};
		});

	}
	
	public void showWindow(){
		mPopupWindow.showAtLocation(gameView, Gravity.CENTER, 0, 0);
		mPopupWindow.update();
	}
	
	public void initSound()
    {
		//声音池
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	    soundPoolMap = new HashMap<Integer, Integer>();   
	    
	    soundPoolMap.put(SOUND_CAPTURE1, soundPool.load(this, R.raw.capture, 1));  //玩家吃子
	    soundPoolMap.put(SOUND_CAPTURE2, soundPool.load(this, R.raw.capture2, 1)); //电脑吃子	    
	    soundPoolMap.put(SOUND_MOVE1, soundPool.load(this, R.raw.move, 1));        //玩家移动
	    soundPoolMap.put(SOUND_MOVE2, soundPool.load(this, R.raw.move2, 1));       //电脑移动
	    soundPoolMap.put(SOUND_WIN, soundPool.load(this, R.raw.win, 1));           //玩家赢
	    soundPoolMap.put(SOUND_LOSS, soundPool.load(this, R.raw.loss, 1));         //玩家输
    }
	
    //播放声音
    public void playSound(int sound, int loop) 
    {
    	if(!isnoPlaySound){
    		return;
    	}
    	
	    AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
	
	public void initPm()
    {
    	//获取屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
		ViewConstant.initAll(dm);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
