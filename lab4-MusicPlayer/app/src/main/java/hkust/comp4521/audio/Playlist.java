package hkust.comp4521.audio;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Playlist extends ListFragment {

    private static final String TAG = "Playlist";

    // the host activity should register itself as a listener and implement the interface methods
    // this variable keeps track of the reference to the host activity
    OnSongSelectedListener mListener = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    // for a listfragment, we need not implement onCreateView() since this is implicit

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        // Give some text to display if there is no data.  In a real
        // application this would come from a resource.
        setEmptyText("No songs yet");

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        // create a string array and initialize it with string array resources from strings.xml
        final String[] songList = getResources().getStringArray(R.array.Songs);

        // create a list adapter and supply it to the listview so that the list of songs can
        // be displayed in the listview
        this.setListAdapter((ListAdapter) new ArrayAdapter<String>(getActivity(), R.layout.playlist_item, R.id.songlist, songList));

        // get a reference to the listview
        ListView lv = getListView();

        lv.setOnItemClickListener(
                new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long id) {

                        // position gives the index of the song selected

                        // return the information about the selected song to MusicActivity through the interface method
                        mListener.onSongSelected(position);
                    }

                }

        );

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSongSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    // the interface that must be implemented by the host activity for communicating from the
    // fragment to the activity
    public interface OnSongSelectedListener {
        public void onSongSelected(int id);
    }
}
