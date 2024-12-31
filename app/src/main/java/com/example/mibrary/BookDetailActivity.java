package com.example.mibrary;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        videoView = findViewById(R.id.videoView);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String videoUrl = getIntent().getStringExtra("videoUrl");

        if (title == null || description == null || videoUrl == null) {
            Toast.makeText(this, "Book details are missing.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        playVideo(videoUrl);
    }

    private void playVideo(String videoUrl) {
        try {
            Uri videoUri = Uri.parse(videoUrl);
            videoView.setVideoURI(videoUri);

            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);

            videoView.start();

            videoView.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(BookDetailActivity.this, "Error playing video.", Toast.LENGTH_SHORT).show();
                return true;
            });

        } catch (Exception e) {
            Toast.makeText(this, "Invalid video URL.", Toast.LENGTH_SHORT).show();
        }
    }
}
