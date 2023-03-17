package com.example.playvideoasynctask;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadVideo extends AsyncTask<String, Integer, String> {
    private TextView tvStatus;

    public DownloadVideo(TextView tvStatus) {
        this.tvStatus = tvStatus;
    }

    @Override
    protected void onPreExecute() {
        // Thực thi tác vụ trước khi download file từ doInBackground()
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        // Tải và ghi file
        BufferedInputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int size = connection.getContentLength();

            inputStream = new BufferedInputStream(url.openStream());

            File directory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC + "/" + "video_khanh"
            );
            if (!directory.exists()) {
                directory.mkdir();
            }

            String destinationFilePath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MUSIC + "/" + "video_khanh" + "/" + "video_mp4_khanh.mp4"
            ).getAbsolutePath();

            File file = new File(destinationFilePath);
            file.createNewFile();

            outputStream = new FileOutputStream(destinationFilePath);

            byte[] data = new byte[1024];
            int count;
            double sumCount = 0.0;
            int progress = 0;

            while ((count = inputStream.read(data, 0, 1024)) != -1) {
                outputStream.write(data, 0, count);
                sumCount += count;
                if (size > 0) {
                    progress = (int) (sumCount / size * 100.0);
                    publishProgress(progress);
                }
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        return "Download mp4 file success !";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // Cập nhật con số % tải về khi đang tải file về
        super.onProgressUpdate(values);

        tvStatus.setText(values[0] + "%"); // values[0] là con số từ hàm publishProgress(progress);
    }

    @Override
    protected void onPostExecute(String s) {
        // Gửi 1 chuỗi cho UI Thread biết là tải về thành công
        super.onPostExecute(s);
        tvStatus.setText(s); // s được trả về từ return type của doInBackground()
    }
}
