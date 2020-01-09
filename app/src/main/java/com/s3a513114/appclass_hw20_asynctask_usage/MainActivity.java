package com.s3a513114.appclass_hw20_asynctask_usage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ProgressBar mProgressBar;//進度條
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
    public void btnDownloadOnclick(View view){
        //new GetImage().execute("https://w.wallhaven.cc/full/vg/wallhaven-vg2y5p.jpg");
        new GetImage().execute("https://obs.line-scdn.net/0hobMAbPvbME1wOBsIszRPGkpuMyJDVCNOFA5hTjNWbnkIC3RJGFoseFw4an5fX3cTHgp9LlA6K3wNDCNMSFss/w644");
    }
    private class GetImage extends AsyncTask<String , Integer , Bitmap>{

        @Override
        protected void onPreExecute() {
            //執行前 設定可以在這邊設定
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //執行中 在背景做事情
            String urlStr = params[0];
            HttpURLConnection connection = null;


            try {
                URL url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int fileLength = connection.getContentLength();
                InputStream input = null;
                input = connection.getInputStream();
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                }


                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //執行中 可以在這邊告知使用者進度
            super.onProgressUpdate(values);
            mProgressBar.setMax(100);
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //執行後 完成背景任務
            super.onPostExecute(bitmap);

            imageView.setImageBitmap(bitmap);
        }
    }
}
