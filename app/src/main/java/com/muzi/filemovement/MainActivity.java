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
import java.io.IOException;

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
//        String oldPath = Environment.getExternalStorageDirectory() + "/ekwing/student";
//        String newPath = getExternalFilesDir(null).getAbsolutePath();
//        FileMoveUtils.getInstance().move(oldPath,newPath);


        new Thread(new Runnable() {
            @Override
            public void run() {
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ekwing/student/sound/55cf0aeb58d41.mp3";
                File file = new File(filePath);
                String parent = file.getParent();

                File parentFile = file.getParentFile();

                Log.e("MainActivity", "parentFile-->" + parentFile.getName());

                Log.e("MainActivity", "file.exists():" + file.exists());
                Log.e("MainActivity", "parent-->" + parent);
                String newPath = getExternalFilesDir(null) + "/sound";
                File newFile = new File(newPath);
                if (!newFile.exists()) {
                    newFile.mkdir();
                }
                try {
                    FileMoveUtils.getInstance().realMove(filePath, newPath + "/" + file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
