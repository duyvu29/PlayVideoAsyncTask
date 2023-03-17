package com.example.playvideoasynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private Button btnPlayVideo, btnDownload;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        btnPlayVideo.setOnClickListener(v -> {
            playViewFromURL(videoView, "https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        });

        btnDownload.setOnClickListener(v -> {
            askForPermissions();
        });
    }

    private void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
//                Toast.makeText(this, "You need permission !", Toast.LENGTH_SHORT).show();
                return;
            }
            new DownloadVideo(tvStatus).execute("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        } else {
            new DownloadVideo(tvStatus).execute("https://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        }
    }

    private void playViewFromURL(VideoView videoView, String url) {
        videoView.setVideoPath(url); // Run trong UI Thread
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                videoView.start();
            }
        });
        t.start();

    }
    private void initView() {
        videoView = findViewById(R.id.videoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnDownload = findViewById(R.id.btnDownload);
        tvStatus = findViewById(R.id.tvStatus);
    }
}