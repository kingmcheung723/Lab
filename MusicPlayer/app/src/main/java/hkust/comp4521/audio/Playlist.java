package hkust.comp4521.audio;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class Playlist extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //	create	a	string	array	and	initialize	it	with string	array resources	from	strings.xml
        final String[] songList =
                getResources().getStringArray(R.array.Songs);
        //	create	a	list	adapter	and	supply	it	to	the	listview	so	that	the list	of	songs	can
        //	be	displayed	in	the	listview
        this.setListAdapter((ListAdapter) new ArrayAdapter<String>(this,
                R.layout.playlist_item, R.id.songlist, songList));
        //	get	a	reference	to	the	listview
        ListView lv = getListView();
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long id) {
                        //	position	gives	the	index	of	the	song	selected	by the	user
                        //	return	the	information	about	the	selected	song to	MusicActivity
                        Intent in = new Intent(getApplicationContext(),
                                MusicActivity.class);
                        in.putExtra("songIndex", position);
                        //	return	the	same	return	code	100	that MusicActivity	used	to	start	this	activity
                        setResult(100, in);
                        //	exit	from	this	activity
                        finish();
                    }
                }
        );
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
