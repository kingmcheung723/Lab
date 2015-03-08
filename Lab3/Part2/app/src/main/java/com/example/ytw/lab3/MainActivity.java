package com.example.ytw.lab3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private ProgressBar progressBar;
    private TextView statusText;
    private int completed;
    private WorkerTask worker;

   /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get handles for the progress bar and status text TextView
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusText = (TextView) findViewById(R.id.status_text);
        // Set the maximum value that the progress bar will display
        progressBar.setMax(100);
        // Declare the listeners for the two buttons
        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
    }

    public void onClick(View source) {
        // Start button is clicked
        if (source.getId() == R.id.start_button) {
            // Declare a new background thread
            // Start the background thread

            // Declare a new Async Task of the WorkerTask class
            worker = new WorkerTask();
            // Execute the Async Task
            worker.execute();
        } else if (source.getId() == R.id.reset_button) {
            // Reset button is clicked
            if (!worker.isCancelled()) {
                worker.cancel(true);
            }
            completed = 0;
            progressBar.setProgress(completed);
            statusText.setText(String.format("Click the button"));
        }
    }


    private class WorkerTask extends AsyncTask<Object, String, Boolean> {
        // Initialize the progress bar and the status TextView
        @Override
        protected void onPreExecute() {
            completed = 0;
            // This will result in a call to onProgressUpdate()
            publishProgress(Integer.toString(completed),  String.format("Completed %d", completed));
        }

        @Override
        // This method updates the main UI, refreshing the progress bar and TextView.
        // Finish this method by yourself.
        protected void onProgressUpdate(String... values) {
            progressBar.setProgress(Integer.parseInt(values[0]));
            statusText.setText(values[1]);
        }

        // Do the main computation in the background and update the UI using publishProgress()
        // Finish this method by yourself.
        @Override
        protected Boolean doInBackground(Object... params) {
            // Do a large amount of computation
            int l = 0;
            for (int i = 0; i< 100; ++i) {
                for (int j = 0; j < 5000; ++j) {
                    for (int k = 0; k < 5000; ++k) {
                        l = i*j*k;
                    }
                }
                // Periodically update the UI to reflect the progress of the computation
                completed += 1;
                publishProgress(Integer.toString(completed),  String.format("Completed %d", completed));
            }
            return true;
        }
    }
}
