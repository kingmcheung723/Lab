package hkust.comp4521.audio;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import hkust.comp4521.audio.player.MusicPlayer;
import hkust.comp4521.audio.player.PlayerState;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link  SongPlaying . OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongPlaying# newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongPlaying extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Observer {


    private static final String TAG = "SongPlaying";
    private static ImageButton playerButton, rewindButton, forwardButton;
    public static Handler handler;
    private TextView songTitleText;
    private static int songIndex = 0;
    private SeekBar songProgressBar;
    private TextView complTime, remTime;

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
        Log.i(TAG, "onCreate");

        // The music player is implemented as a Java Singleton class so that only one
        // instance of the player is present within the application. The getMusicPlayer()
        // method returns the reference to the instance of the music player class
        // get a reference to the instance of the music player
        // set the context for the music player to be this fragment
        // add this fragment as an observer.
        player = MusicPlayer.getMusicPlayer();
        player.addObserver(this);

        // setRetainInstance(true);
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Create a layout for the fragment with the buttons:
        // and set it to the view of this fragment
        View view = inflater.inflate(R.layout.songplaying, container, false);
        Log.i(TAG, "onCreateView()");

        // Get the references to the buttons from the layout of the activity
        playerButton = (ImageButton) view.findViewById(R.id.play);
        playerButton.setOnClickListener(this);
        rewindButton = (ImageButton) view.findViewById(R.id.rewind);
        rewindButton.setOnClickListener(this);
        forwardButton = (ImageButton) view.findViewById(R.id.forward);
        forwardButton.setOnClickListener(this);

        // get a reference to the song title TextView in the UI
        songTitleText = (TextView) view.findViewById(R.id.songTitle);
        // get reference to the seekbar, completion time and remaining time textviews
        songProgressBar = (SeekBar) view.findViewById(R.id.songProgressBar);
        songProgressBar.setMax(100);
        songProgressBar.setOnSeekBarChangeListener(this);

        complTime = (TextView) view.findViewById(R.id.songCurrentDurationLabel);
        remTime = (TextView) view.findViewById(R.id.songRemainingDurationLabel);

        songProgressBar.setProgress(player.progress());
        complTime.setText(player.completedTime());
        remTime.setText("-" + player.remainingTime());
        songTitleText.setText(player.getSongTitle());

        if (player.isPlaying()) {
            updateSongProgress();
            playerButton.setImageResource(R.drawable.img_btn_pause);
        }
        return view;
    }

    public void updateSongProgress() {
        handler.postDelayed(songProgressUpdate, 500);
    }

    private Runnable songProgressUpdate = new Runnable() {
        @Override
        public void run() {
            // Initialize the progress bar and the status TextViews
            // We want to modify the progress bar. But we can do it only from
            // the UI thread. To do this we make use of the handler.
            songProgressBar.setProgress(player.progress());
            complTime.setText(player.completedTime());
            remTime.setText("-" + player.remainingTime());
            // schedule another update 500 ms later
            handler.postDelayed(songProgressUpdate, 500);
        }
    };

    public void cancelUpdateSongProgress() {
        // cancel all callbacks that are already in the handler queue
        handler.removeCallbacks(songProgressUpdate);
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
    public void update(Observable arg0, Object arg1) {
        // The update method is called whenever the Music Player experiences change of state
        // arg1 returns the current state of the player in the form of PlayerState enum variable
        // Use the switch to recognize which state the player just entered and take appropriate
        // action to handle the change of state. Here we update the play/pause button accordingly
        switch ((PlayerState) arg1) {
            case Ready:
                Log.i(TAG, "Player State Changed to Ready");
                playerButton.setImageResource(R.drawable.img_btn_play);
                     songTitleText.setText(player.getSongTitle());
                songProgressBar.setProgress(player.progress());
                complTime.setText(player.completedTime());
                remTime.setText("-" + player.remainingTime());
                break;
            case Paused:
                Log.i(TAG, "Player State Changed to Paused");
                playerButton.setImageResource(R.drawable.img_btn_play);
                cancelUpdateSongProgress();
                songProgressBar.setProgress(player.progress());
                complTime.setText(player.completedTime());
                remTime.setText("-" + player.remainingTime());
                break;
            case Stopped:
                Log.i(TAG, "Player State Changed to Stopped");
                playerButton.setImageResource(R.drawable.img_btn_play);
                cancelUpdateSongProgress();
                break;
            case Playing:
                playerButton.setImageResource(R.drawable.img_btn_pause);
                Log.i(TAG, "Player State Changed to Playing");
                updateSongProgress();
                break;
            case Reset:
                Log.i(TAG, "Player State Changed to Reset");
                playerButton.setImageResource(R.drawable.img_btn_play);
                cancelUpdateSongProgress();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
        cancelUpdateSongProgress();
        if (fromUser && player.isPlaying())
            player.reposition(progress);
        updateSongProgress();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
