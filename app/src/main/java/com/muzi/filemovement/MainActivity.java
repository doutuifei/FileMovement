package com.muzi.filemovement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    private void requestPermission() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void moveFile(View view) {
        final long startMillis = System.currentTimeMillis();
        String oldPath = Environment.getExternalStorageDirectory() + "/aaa";
        String newDesc = getExternalFilesDir(null).getAbsolutePath();
        FileMoveManager.getInstance().move(oldPath, newDesc, new Callback() {
            @Override
            public int getInterval() {
                return 200;
            }

            @Override
            public void onStart() {
                Log.e("MainActivity", "start");
            }

            @Override
            public void onScan(File file) {
                Log.e("MainActivity", "scan-->" + file.getAbsolutePath());
            }

            @Override
            public void onProgress(int progress) {
                Log.e("MainActivity", "progress-->" + progress);
            }

            @Override
            public void onFinish() {
                Log.e("MainActivity", "onFinish");
                long finishMillis = System.currentTimeMillis() - startMillis;
                Log.e("MainActivity", "finishMillis-->" + finishMillis);
            }
        });
    }

}
