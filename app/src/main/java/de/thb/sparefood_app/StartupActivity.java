package de.thb.sparefood_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import de.thb.sparefood_app.ui.login.LoginActivity;
import pl.droidsonroids.gif.GifImageView;

public class StartupActivity extends AppCompatActivity {
    GifImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.startup_layout);
        imageView = findViewById(R.id.startUpLoadingScreen);

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    Thread.sleep(2540);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean available) {
                startActivity(new Intent(StartupActivity.this, LoginActivity.class));
            }
        }.execute();
    }
}
