package chess.ui;

/**
 * MainActivity.java 
 * chess 1.0
 * Date 2015/02/08
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

import static chess.ui.ViewConstant.*;
import static chess.game.Define.*;

import java.util.HashMap;

import chess.activity.R;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	SoundPool soundPool;//声音池
	HashMap<Integer, Integer> soundPoolMap; //声音池中声音ID与自定义声音ID的Map
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
      //设置横屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);//游戏过程中只允许多媒体音量,而不允许通化音量
        initPm();//调整屏幕分辨率
        initSound();//初始化声音资源
		setContentView(new GameView(this));
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
    	if(!isnoPlaySound)
    	{
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
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int tempHeight=(int) (height=dm.heightPixels);
        int tempWidth=(int) (width=dm.widthPixels); 
        screen_height = (int) (height=dm.heightPixels);
        screen_width = (int) (width=dm.widthPixels); 
//        
        if(tempHeight>tempWidth)
        {
        	height=tempHeight;
        	width=tempWidth;
        }
        else
        {
        	height=tempWidth;
        	width=tempHeight;
        }
        float zoomx=width/480;
		float zoomy=height/800;
		if(zoomx>zoomy){
			xZoom=yZoom=zoomy;
			
		}else
		{
			xZoom=yZoom=zoomx;
		}
		sXtart=(width-48*10*xZoom)/2;
		sYtart=(height-48*11*yZoom)/2;

		initChessViewFinal();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
