package com.vocabtrainer.project.vocabtrainer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;
import com.vocabtrainer.project.vocabtrainer.adapter.ScanWordAdapter;
import com.vocabtrainer.project.vocabtrainer.camera.CameraSourcePreview;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.CAMERA;

public class ScanActivity extends AppCompatActivity implements WordDetector.Callback {
    private static final String TAG = ScanActivity.class.getSimpleName();

    private static final int CAMERA_PERMISSION = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.camera_preview)
    CameraSourcePreview cameraPreview;

    @BindView(R.id.rv_words)
    RecyclerView recyclerView;

    @BindView(R.id.camera_stop)
    View stopCamera;

    @BindView(R.id.camera_start)
    View startCamera;

    private CameraSource cameraSource;
    private HashSet<String> detectedWords;
    private ScanWordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkAndRequestPermission();
        createCameraSource();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScanWordAdapter(this);
        recyclerView.setAdapter(adapter);
        detectedWords = new HashSet<>();
    }

    private void createCameraSource() {
        Context context = getApplicationContext();

        // Create the TextRecognizer
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        // TODO: Set the TextRecognizer's Processor.

        // Check if the TextRecognizer is operational.
        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");

        } else {
            textRecognizer.setProcessor(new WordDetector(this));
        }
        // Create the mCameraSource using the TextRecognizer.
        cameraSource =
                new CameraSource.Builder(context, textRecognizer)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(1280, 1024)
                        .setRequestedFps(15.0f)
                        .setAutoFocusEnabled(true)
                        .build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "permissions for camera not granted");
                return;
            }
            cameraPreview.start(cameraSource);
            startCamera.setVisibility(View.VISIBLE);
            stopCamera.setVisibility(View.GONE);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "could not start camera");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraPreview.stop();
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{CAMERA},
                        CAMERA_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    finish();
                }
                return;
            }

        }
    }

    @Override
    public void reportDetectedWords(List<String> words) {
        if(isFinishing() || isDestroyed()) return;
        int count = detectedWords.size();
        detectedWords.addAll(words);

        if(detectedWords.size() > count){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.swapData(new ArrayList<String>(detectedWords));
                }
            });
        }
    }

    public void clearScannedWords(View view){
        synchronized (detectedWords) {
            detectedWords.clear();
            adapter.swapData(new ArrayList<String>(detectedWords));
        }
    }

    public void stopCamera(View view){
        cameraPreview.stop();
        startCamera.setVisibility(View.VISIBLE);
        stopCamera.setVisibility(View.GONE);
    }

    @SuppressLint("MissingPermission")
    public void startCamera(View view){
        try {
            cameraPreview.start(cameraSource);
        } catch (IOException e) {
        }
        startCamera.setVisibility(View.GONE);
        stopCamera.setVisibility(View.VISIBLE);
    }
}
