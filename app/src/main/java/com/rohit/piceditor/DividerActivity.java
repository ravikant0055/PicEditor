package com.rohit.piceditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yashoid.instacropper.InstaCropperView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class DividerActivity extends AppCompatActivity {
    InstaCropperView crop_view;
    Button btnDivider,btnSave,btnDivider2;
    Uri initialUri;
    Bitmap bitmap;
    ImageView img0,img1,img2,img3,img4,img5,img6,img7,img8;
    PhotoEditorView imageView3;
    LinearLayout dividerLinear;
    String ImageName = "result";
    ArrayList<Bitmap> SplittedIMages;
    LinearLayout linear1,linear2,linear3;

    int ImageNumber = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divider);

        //No Library Used

        crop_view = findViewById(R.id.instacropper);

        btnDivider = findViewById(R.id.btnDivider);
        btnDivider2 = findViewById(R.id.btnDivider2);
        btnSave  = findViewById(R.id.btnSave);
        dividerLinear = findViewById(R.id.dividerLinear);
        linear1 = findViewById(R.id.Linear1);
        linear2 = findViewById(R.id.Linear2);
        linear3 = findViewById(R.id.Linear3);
        img0 = findViewById(R.id.img0);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);

        crop_view.setVisibility(View.VISIBLE);
        dividerLinear.setVisibility(View.GONE);


        //Getting Images in Form of Uri as an Intent
        Uri photo = getIntent().getParcelableExtra("uri");
        ImageName = getIntent().getStringExtra("ImageName");

        //getting Bitmap From Static Class of HomeActivity
        Bitmap photoBitmap = HomeActivity.constan.photoMap;

        Uri photoUri = getImageUri(DividerActivity.this,photoBitmap);

        crop_view.setImageUri(photoUri);
        crop_view.setRatios(1, 1, 1);



        //Convert Uri into Bitmap
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SplittedIMages = new ArrayList<>();







        //button to Divide Images into 9Parts
        btnDivider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crop_view.setRatios(1, 1, 1);
                ImageNumber = 9;

                crop_view.setVisibility(View.VISIBLE);
                dividerLinear.setVisibility(View.GONE);

            }
        });

        //button to Divide Images into 3Parts
        btnDivider2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageNumber = 3;
                crop_view.setRatios(3, 3, 3);

                crop_view.setVisibility(View.VISIBLE);
                dividerLinear.setVisibility(View.GONE);

            }
        });

        //Button to save Resulted image
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave.setEnabled(false);

                if(ImageNumber == 9){
                    crop_view.crop(
                        View.MeasureSpec.makeMeasureSpec(1024, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        new InstaCropperView.BitmapCallback() {
                            @Override
                            public void onBitmapReady(Bitmap bitmap) {
                                SplittedIMages = splitImage(9,3,3,bitmap);

                                linear1.setVisibility(View.VISIBLE);
                                linear2.setVisibility(View.VISIBLE);
                                linear3.setVisibility(View.VISIBLE);

                                Log.e("size",SplittedIMages.size()+"");

                                img0.setImageBitmap(SplittedIMages.get(0));
                                img1.setImageBitmap(SplittedIMages.get(1));
                                img2.setImageBitmap(SplittedIMages.get(2));
                                img3.setImageBitmap(SplittedIMages.get(3));
                                img4.setImageBitmap(SplittedIMages.get(4));
                                img5.setImageBitmap(SplittedIMages.get(5));
                                img6.setImageBitmap(SplittedIMages.get(6));
                                img7.setImageBitmap(SplittedIMages.get(7));
                                img8.setImageBitmap(SplittedIMages.get(8));

                                crop_view.setVisibility(View.GONE);
                                dividerLinear.setVisibility(View.VISIBLE);

                                if(SplittedIMages.size() == 0){
                                    Bitmap finalImage = bitmap;
                                    //Pass the Bitmap and Name Of IMage You Want to Save
                                    saveToInternalStorage(finalImage,ImageName+"Divided");
                                }else{
                                    //Use  A For Loop To Save All Splitted Images in Internal Memory
                                    int a = 1;
                                    for(Bitmap bitmap1:SplittedIMages){
                                        saveToInternalStorage(bitmap1,ImageName+"Div"+a);
                                        a++;
                                    }
                                }
                                Toast.makeText(DividerActivity.this, "Image Successfully saved", Toast.LENGTH_SHORT).show();
                                btnSave.setEnabled(true);
                            }
                        }
                    );

                }else{
                    crop_view.crop(
                        View.MeasureSpec.makeMeasureSpec(1024, View.MeasureSpec.AT_MOST),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        new InstaCropperView.BitmapCallback() {
                            @Override
                            public void onBitmapReady(Bitmap bitmap) {
                                SplittedIMages = splitImage(3,1,3,bitmap);

                                crop_view.setVisibility(View.GONE);
                                dividerLinear.setVisibility(View.VISIBLE);

                                linear1.setVisibility(View.INVISIBLE);
                                linear2.setVisibility(View.VISIBLE);
                                linear3.setVisibility(View.INVISIBLE);

                                img3.setImageBitmap(SplittedIMages.get(0));
                                img4.setImageBitmap(SplittedIMages.get(1));
                                img5.setImageBitmap(SplittedIMages.get(2));

                                if(SplittedIMages.size() == 0){
                                    Bitmap finalImage = bitmap;
                                    //Pass the Bitmap and Name Of IMage You Want to Save
                                    saveToInternalStorage(finalImage,ImageName+"Divided");
                                }else{
                                    //Use  A For Loop To Save All Splitted Images in Internal Memory
                                    int a = 1;
                                    for(Bitmap bitmap1:SplittedIMages){
                                        saveToInternalStorage(bitmap1,ImageName+"Div"+a);
                                        a++;
                                    }
                                }
                                Toast.makeText(DividerActivity.this, "Image Successfully saved", Toast.LENGTH_SHORT).show();
                                btnSave.setEnabled(true);
                            }

                        }
                    );
                }

                //chech if user Click on Seprate Button or not
                //if Clicked on Seprate Button means SpliitedImages Array Filled With Image Parts

            }
        });
    }

    //Function to save all Splitted Images into Parts ,Pass the Integer value to spllit Number of images
    private ArrayList splitImage(int chunkNumbers,int row,int column,Bitmap bitmap) {
        int rows,cols;
        int chunkHeight,chunkWidth;
        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

      //  rows = cols = (int) Math.sqrt(chunkNumbers);
        rows = row;
        cols = column;
        chunkHeight = bitmap.getHeight()/rows;
        chunkWidth = bitmap.getWidth()/cols;

        int yCoord = 0;
        for(int x=0; x<rows; x++){
            int xCoord = 0;
            for(int y=0; y<cols; y++){
                chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }

        return chunkedImages;
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
        }
//        } finally {
//            try {
//               // fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}