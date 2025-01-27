package com.example.paintapp;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Color;
import android.provider.MediaStore;
import android.widget.SeekBar;
import android.widget.Toast;
import android.Manifest;

import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    private static final int STORAGE_PERMISSION_CODE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paintView);

        findViewById(R.id.buttonRed).setOnClickListener(v -> paintView.setBrushColor(Color.RED));
        findViewById(R.id.buttonGreen).setOnClickListener(v -> paintView.setBrushColor(Color.GREEN));
        findViewById(R.id.buttonBlue).setOnClickListener(v -> paintView.setBrushColor(Color.BLUE));
        findViewById(R.id.buttonBlack).setOnClickListener(v -> paintView.setBrushColor(Color.BLACK));

        SeekBar seekBar = findViewById(R.id.seekBarBrushSize);
        seekBar.setProgress(10);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                paintView.setBrushSize(i);  // Use 'i' instead of 'progress'
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Add logic here for when the user starts touching the seek bar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Add logic here for when the user stops touching the seek bar
            }
        });


        findViewById(R.id.buttonEraser).setOnClickListener(v -> paintView.setEraserMode(true));
        findViewById(R.id.buttonReset).setOnClickListener(v -> paintView.clearCanvas());
        findViewById(R.id.buttonSave).setOnClickListener(v -> {
            if(checkPermission()){
                saveDrawingToGallery();
            }
            else {
                requestPermission();
            }
        });
    }

    private boolean checkPermission() {
        // No permission required for Android 10 (API level 29) or higher
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // Request WRITE_EXTERNAL_STORAGE only for Android 9 (API level 28) or lower
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }


    private void saveDrawingToGallery(){
        Bitmap bitmap = paintView.getBitmap();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + System.currentTimeMillis()+ ".png");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyPaintApp");

        Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if(imageUri != null){
            try {
                OutputStream outputStream = getContentResolver().openOutputStream(imageUri, "w");
                if(outputStream != null){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                    Toast.makeText(this, "Saved to Gallery", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}