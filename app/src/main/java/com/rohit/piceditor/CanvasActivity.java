package com.rohit.piceditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CanvasActivity extends AppCompatActivity {
    FrameLayout frameLayoutCanvas;
    ConstraintLayout constraint;
    Button btnCanvas,btn11,btn34,btnSave;
    ImageView imgCanvas;
    String ImageName = "result";
    int color;
    SeekBar seekBar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);

        //No Library Used

        constraint = findViewById(R.id.constraint);
        frameLayoutCanvas= findViewById(R.id.frameLayoutCanvas);
        btnCanvas = findViewById(R.id.btnCanvas);
        imgCanvas = findViewById(R.id.imgCanvas);
        btn11 = findViewById(R.id.btn11);
        btn34 = findViewById(R.id.btn34);
        btnSave = findViewById(R.id.btnSave);
        seekBar4=  findViewById(R.id.seekBar4);



        //Getting Selected Image in Uri by Intent
        Uri photo = getIntent().getParcelableExtra("uri");
        ImageName = getIntent().getStringExtra("ImageName");
        Bitmap photoBitmap = HomeActivity.constan.photoMap;


        //Set Image
        imgCanvas.setImageBitmap(photoBitmap);

        //these All Button OnClick To Set Ratio, Just Pass The Ratio You Want To Set
        btnCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintSet set = new ConstraintSet();
                set.clone(constraint);
                set.setDimensionRatio(frameLayoutCanvas.getId(), "16:9");
                set.applyTo(constraint);
            }
        });
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintSet set = new ConstraintSet();
                set.clone(constraint);
                set.setDimensionRatio(frameLayoutCanvas.getId(), "1:1");
                set.applyTo(constraint);
            }
        });
        btn34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintSet set = new ConstraintSet();
                set.clone(constraint);
                set.setDimensionRatio(frameLayoutCanvas.getId(), "3:4");
                set.applyTo(constraint);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap finalImage = viewToBitmap(frameLayoutCanvas);

                HomeActivity.constan.photoMap = finalImage;
                finish();
               // startActivity(new Intent(CanvasActivity.this,HomeActivity.class));

                //Pass the Bitmap and Name Of IMage You Want to Save
              //  saveToInternalStorage(finalImage,ImageName+"Canvs");
              //  Toast.makeText(CanvasActivity.this, "Image Successfully saved", Toast.LENGTH_SHORT).show();
            }
        });

        seekBar4.setMax(200);
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setMargins(imgCanvas,seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //Function To Save FrameLayout as an Image
    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    //Function TO Save Image In internal storage
    private void saveToInternalStorage(Bitmap bitmapImage,String Name){
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

    public void color(View view){
        switch (view.getId()){
            case R.id.black:
                color = Color.BLACK;
                frameLayoutCanvas.setBackgroundColor(color);
                break;
            case R.id.white:
                color = Color.WHITE;
                frameLayoutCanvas.setBackgroundColor(color);
                break;
            case R.id.blue:
                color = Color.BLUE;
                frameLayoutCanvas.setBackgroundColor(color);
                break;
            case R.id.red:
                color = Color.RED;
                frameLayoutCanvas.setBackgroundColor(color);
                break;
        }

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