package com.rohit.piceditor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.gigamole.library.ShadowLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class ShadowActivity extends AppCompatActivity {

    PhotoEditorView img;
    ShadowLayout shadowLayout;
    SeekBar SeekAngle,seekBar3,SeekShadow;
    FrameLayout frameLayout;
    int color;
    int color2;
    Button btnSave;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);

        Uri photo = getIntent().getParcelableExtra("uri");
        String ImageName = getIntent().getStringExtra("ImageName");

        shadowLayout = (ShadowLayout) findViewById(R.id.sl);
        img = findViewById(R.id.img);
        SeekAngle = findViewById(R.id.SeekAngle);
        SeekShadow = findViewById(R.id.SeekDistance);
        seekBar3 = findViewById(R.id.seekBar3);
        frameLayout = findViewById(R.id.frameLayout);
        btnSave = findViewById(R.id.btnSave);

        shadowLayout.setIsShadowed(true);
        shadowLayout.setShadowAngle(45);
        shadowLayout.setShadowRadius(20);
        shadowLayout.setShadowDistance(50);
        shadowLayout.setShadowColor(Color.DKGRAY);

      //  findViewById(R.id.white2).setBackgroundColor(Color.DKGRAY);
        findViewById(R.id.white).setBackgroundColor(Color.DKGRAY);


       // img.getSource().setImageURI(photo);

        img.getSource().setImageBitmap(HomeActivity.constan.photoMap);

        SeekAngle.setMin(45);
        SeekAngle.setMax(360);

        //This Function use Minimum Api OREO,So to work on Older Version Remove Minimum Value from Seekbar
        SeekShadow.setMin(50);
        SeekShadow.setMax(150);
        seekBar3.setMax(200);

        SeekAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                shadowLayout.setShadowAngle(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekShadow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                shadowLayout.setShadowDistance(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //For Changing Image Size
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setMargins(shadowLayout,seekBar.getProgress());
               // imgShadow.setImageURI(photo);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //finalImage is the Resulted Image , Do Whatevre you Want to do With this image
                Bitmap finalImage = viewToBitmap(frameLayout);
                //Now Set This Final Bitmap in HomeActivity
                HomeActivity.constan.photoMap = finalImage;
                finish();
            }
        });
    }

    public void color(View view){
        switch (view.getId()){
            case R.id.black:
                color = Color.BLACK;
                shadowLayout.setShadowColor(color);
                break;
            case R.id.white:
                color = Color.DKGRAY;
                shadowLayout.setShadowColor(color);
                break;
            case R.id.blue:
                color = Color.BLUE;
                shadowLayout.setShadowColor(color);

                break;
            case R.id.red:
                color = Color.RED;
                shadowLayout.setShadowColor(color);

                break;

            case R.id.black2:
                color2 = Color.BLACK;
                frameLayout.setBackgroundColor(color2);
                break;
            case R.id.white2:
                color2 = Color.WHITE;
                frameLayout.setBackgroundColor(color2);
                break;
            case R.id.blue2:
                color2 = Color.BLUE;
                frameLayout.setBackgroundColor(color2);
                break;
            case R.id.red2:
                color2 = Color.RED;
                frameLayout.setBackgroundColor(color2);
                break;
        }

    }

    //Function TO Save Image In internal storage
    private void saveToInternalStorage(Bitmap bitmapImage, String Name){
        //set image saved path
        //Set FolderNAmee
        String AppName = "PicEditor";
        File storageDir = new File(Environment.getExternalStorageDirectory()
                + "/"+AppName);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File mypath=new File(storageDir,Name+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Method to Save FrameLayout as An Image
    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    //this Method For Changing Size of Image
    private void setMargins (View view,int margin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(margin, margin, margin, margin);
            view.requestLayout();
        }
    }
}