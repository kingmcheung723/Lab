package hkust.comp4521.audio;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import hkust.comp4521.audio.player.MusicPlayer;

public class MusicActivity extends Activity implements Playlist.OnSongSelectedListener {
    private static final String TAG = "MusicActivity";
    private static int songIndex = 0;
    private MusicPlayer player;
    // indicates if the player is running on a small screen device (false) or tablet (true)
    private boolean dualview = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a layout for the Activity with four buttons:
        // Rewind, Pause, Play and Forward and set it to the view of this activity
        setContentView(R.layout.main);
        // The music player is implemented as a Java Singleton class so that only one
        // instance of the player is present within the application. The getMusicPlayer()
        // method returns the reference to the instance of the music player class
        // get a reference to the instance of the music player
        // set the context for the music player to be this activity
        // add this activity as an observer.
        player = MusicPlayer.getMusicPlayer();

        player.setContext(this);
        startSong(songIndex);
        // If the view being used contains the SongPlaying fragment in the layout, then
        // we are using dualview layout and the screen size is large. So both fragments
        // are on the screen. Set dualview to true
        if (findViewById(R.id.song) != null)
            dualview = true;
        if (!dualview) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
            if (findViewById(R.id.fragment_container) != null) {
                Fragment firstFragment = new SongPlaying();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.fragment_container, firstFragment, getResources().getString(R.string.NowPlaying));
                ft.commit();
                Log.i(TAG, "First Fragment: " + firstFragment.getTag() + " Res ID: " + firstFragment.getId());
            }
        }
    }

    private void startSong(int index) {
        final String[] songFile = getResources().getStringArray(R.array.filename);
        final String[] songList = getResources().getStringArray(R.array.Songs);
        player.start(getResources().getIdentifier(songFile[index], "raw", getPackageName()), songList[index]);
    }

    @Override
    public void onSongSelected(int id) {
        // This method is for the OnSongSelectedListener interface. When the user selects a song in the
        // play list, then this method is invoked
        player.reset();
        songIndex = id;
        startSong(id);
        if (!dualview) {
            Fragment firstFragment = getFragmentManager().findFragmentByTag(getResources().getString(R.string.NowPlaying));
            Log.i(TAG, "First Fragment: " + firstFragment.getTag() + " Res ID: " + firstFragment.getId());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, firstFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
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
                if (!dualview) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment secondFragment = getFragmentManager().findFragmentByTag(getResources().getString(R.string.SongList));
                    if (secondFragment == null) {
                        secondFragment = new Playlist();
                    }
                    ft.replace(R.id.fragment_container, secondFragment, getResources().getString(R.string.SongList));
                    ft.addToBackStack(null);
                    ft.commit();
                    Log.i(TAG, "Second Fragment: " + secondFragment.getTag() + " Res ID: " + secondFragment.getId());
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}