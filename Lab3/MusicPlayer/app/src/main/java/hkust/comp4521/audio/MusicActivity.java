package hkust.comp4521.audio;

import hkust.comp4521.audio.player.MusicPlayer;
import hkust.comp4521.audio.player.PlayerState;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class MusicActivity extends Activity implements OnClickListener, Observer {


    private static final String TAG = "MusicPlayer";
    private static ImageButton playerButton, rewindButton, forwardButton;
    public static Handler handler;
    private TextView songTitleText;
    private static int songIndex = 0;

    /*
	 * Class Name: MusicPlayer
	 * 
	 *    This class implements support for playing a music file using the MediaPlayer class in Android.
	 *    It supports the following methods:
	 *    
	 *    play_pause(): toggles the player between playing and paused states
	 *    resume(): resume playing the current song
	 *    pause(): pause the currently playing song
	 *    rewind(): rewind the currently playing song by one step
	 *    forward(): forward the currently playing song by one step
	 *    stop(): stop the currently playing song
	 *    reset(): reset the music player and release the MediaPlayer associated with it
	 *    reposition(value): repositions the playing position of the song to value% and resumes playing
	 *
	 *    progress(): returns the percentage of the playback completed. useful to update the progress bar
	 *    completedTime(): Amount of the song time completed playing
	 *    remainingTime(): Remaining time of the song being played
	 *
	 *    You should use these methods to manage the playing of the song.
	 *
	 */
    private MusicPlayer player;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a layout for the Activity with four buttons:
        // Rewind, Pause, Play and Forward and set it to the view of this activity
        setContentView(R.layout.main);

        Log.i(TAG, "Activity: onCreate()");

        // Get the references to the buttons from the layout of the activity
        playerButton = (ImageButton) findViewById(R.id.play);
        playerButton.setOnClickListener(this);

        rewindButton = (ImageButton) findViewById(R.id.rewind);
        rewindButton.setOnClickListener(this);

        forwardButton = (ImageButton) findViewById(R.id.forward);
        forwardButton.setOnClickListener(this);

        //	get	a	reference	to	the	song	title	TextView	in	the	UI
        songTitleText = (TextView) findViewById(R.id.songTitle);


        // The music player is implemented as a Java Singleton class so that only one
        // instance of the player is present within the application. The getMusicPlayer()
        // method returns the reference to the instance of the music player class
        // get a reference to the instance of the music player
        // set the context for the music player to be this activity
        // add this activity as an observer.
        player = MusicPlayer.getMusicPlayer();
        player.setContext(this);
        player.addObserver(this);

        startSong(songIndex);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }

    private	void	startSong(int	index){
        final	String[]	songFile	=	getResources().getStringArray(R.array.filename);
        final String[] songList = getResources().getStringArray(R.array.Songs);

        player.start(getResources().getIdentifier(songFile[index],	"raw",	getPackageName()), songList[index]);

    }


    @Override
    protected void onDestroy() {

        Log.i(TAG, "Activity: onDestroy()");

        // reset the music player and release the media player
        player.reset();

        super.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.i(TAG, "Activity: onPause()");
    }

    @Override
    protected void onRestart() {

        super.onRestart();
        Log.i(TAG, "Activity: onRestart()");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.i(TAG, "Activity: onResume()");
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.i(TAG, "Activity: onStart()");
    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.i(TAG, "Activity: onStop()");
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.play:
                player.play_pause();
                break;

            case R.id.forward:
                player.forward();
                break;

            case R.id.rewind:
                player.rewind();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Handle the presses of the action bar items
        switch (item.getItemId()) {

            case R.id.action_playlist:
                Intent i = new Intent(getApplicationContext(), Playlist.class);

                // start the playlist activity and once the user selects a song
                // from the list, return the information about the selected song
                // to MusicActivity
                startActivityForResult(i, 100);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            songIndex = data.getExtras().getInt("songIndex");

            player.reset();

            startSong(songIndex);
        }
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        // The update method is called whenever the Music Player experiences change of state
        // arg1 returns the current state of the player in the form of PlayerState enum variable
        // Use the switch to recognize which state the player just entered and take appropriate
        // action to handle the change of state. Here we update the play/pause button accordingly

        switch ((PlayerState) arg1) {

            case Ready:
                Log.i(TAG, "Activity: Player State Changed to Ready");
                songTitleText.setText(player.getSongTitle());
                playerButton.setImageResource(R.drawable.img_btn_play);
                break;

            case Paused:
                Log.i(TAG, "Activity: Player State Changed to Paused");
                playerButton.setImageResource(R.drawable.img_btn_play);
                break;

            case Stopped:
                Log.i(TAG, "Activity: Player State Changed to Stopped");
                playerButton.setImageResource(R.drawable.img_btn_play);
                break;

            case Playing:
                Log.i(TAG, "Activity: Player State Changed to Playing");
                playerButton.setImageResource(R.drawable.img_btn_pause);
                break;

            case Reset:
                Log.i(TAG, "Activity: Player State Changed to Reset");
                playerButton.setImageResource(R.drawable.img_btn_play);
                break;

            default:
                break;

        }

    }
}