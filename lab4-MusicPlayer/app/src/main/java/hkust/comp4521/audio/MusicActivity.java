package hkust.comp4521.audio;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import hkust.comp4521.audio.player.MusicPlayer;

public class MusicActivity extends Activity implements Playlist.OnSongSelectedListener {


    private static final String TAG = "MusicActivity";
    MusicPlayer player = null;
    private static int songIndex = 0;

    private GestureDetector mDetector;


    // indicates if the player is running on a small screen device (false) or tablet (true)
    private boolean dualview = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a layout for the Activity with four buttons:
        // Rewind, Pause, Play and Forward and set it to the view of this activity
        setContentView(R.layout.main);
        startService(new Intent(this, MusicController.class));
        // The music player is implemented as a Java Singleton class so that only one
        // instance of the player is present within the application. The getMusicPlayer()
        // method returns the reference to the instance of the music player class
        // get a reference to the instance of the music player
        // set the context for the music player to be this activity
        // add this activity as an observer.

        mDetector = new GestureDetector(this, new MyGestureListener());

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void startSong(int index) {
        Log.i(TAG, "Service:	startSong()");
        final String[] songFile = getResources().getStringArray(R.array.filename);
        final String[] songList = getResources().getStringArray(R.array.Songs);
        if (player != null) {
            songIndex = index;
            player.start(getResources().getIdentifier(songFile[index], "raw",
                    getPackageName()), songList[index]);
        } else
            Log.i(TAG, "Service:	startSong	Null	Player");
    }


    @Override
    public void onSongSelected(int id) {
        //	This	method	is	for	the	OnSongSelectedListener	interface.	When	the	user	selects	a	song	in
        //	the	play	list,	then	this	method	is	invoked
        songIndex = id;
        //	create	an	intent to	send	to	MusicController	service
        Intent intent = new Intent(getApplicationContext(), MusicController.class);
        //	Add	the	action	to	the	intent.	Here	we	are	trying	to	start	the	song
        intent.setAction(Constants.ACTION_SONG);
        //	add	the	song	ID	to	the	intent
        intent.putExtra("Song", id);
        //	call	startService	to	deliver	the	intent	to	onStartCommand()	in	the	service
        //	where	it	will	be	handled.
        startService(intent);
        if (!dualview) {
            Fragment firstFragment =
                    getFragmentManager().findFragmentByTag(getResources().getString(R.string.NowPlaying));
            Log.i(TAG, "First	Fragment:	" + firstFragment.getTag() + "	Res	ID:	" +
                    firstFragment.getId());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, firstFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onDestroy() {
        //	Pop	up	a	message	on	the	screen to	show	that	the	service	is	started
        Toast.makeText(this, "MusicController	Service	Stopped", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service:	onDestroy()");
        player = null;
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

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean
        onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean
        onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            //  gesture is  from left to right
            if (velocityX > 0) {
                // bring  the  playlist fragment to the  front and once the user selects a song
                // from the list, return  the information about  the selected song to MusicActivity
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
            }
            return true;
        }
    }
}