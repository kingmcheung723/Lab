package com.example.ytw.lab3;

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
    private Runnable worker = new Runnable() {
        public void run() {
            int l;
            // Initialize the progress bar and the status TextView
            completed = 0;
            // we want to modify the progress bar so we need to do it from the UI thread
            // how can we make sure the code runs in the UI thread? use the handler!

            // Do a large amount of computation
            for (int i = 0; i < 100; ++i) {
                for (int j = 0; j < 5000; ++j) {
                    for (int k = 0; k < 5000; ++k) {
                        l = i * j * k;
                    }
                }
                completed += 1;
                handler.post(new Runnable() {
                    public void run() {
                        // Here the worker thread uses the post method to initialize the progress bar.
                        // Finish this method by yourself.
                        // Periodically update the UI to reflect the progress of the computation
                        progressBar.setProgress(completed);
                        statusText.setText(String.format("Completed %d", completed));
                    }
                });
                // Here the worker thread do the loop and update the progress bar periodically
                // Similarly, it uses the handler.post method to update the progress bar
                // Finish this part by yourself.
            }
        }
    };
    private Handler handler;

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

        handler = new Handler();
    }

    public void onClick(View source) {
        // Start button is clicked
        if (source.getId() == R.id.start_button) {
            // Declare a new background thread
            // Start the background thread
            Thread workerThread = new Thread(worker);
            workerThread.start();
        } else if (source.getId() == R.id.reset_button) {
            // Reset button is clicked
            completed = 0;
            progressBar.setProgress(completed);
            statusText.setText(String.format("Click the button"));
        }
    }
}
