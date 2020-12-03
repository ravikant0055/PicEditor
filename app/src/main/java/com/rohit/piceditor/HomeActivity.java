package com.rohit.piceditor;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.yalantis.ucrop.UCrop;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class HomeActivity extends AppCompatActivity {
    Button btnAddImage;
    ImageView btnText, btnShadow,btnDivider,btnCanvas,btnCrop;
    PhotoEditor mPhotoEditor;
    PhotoEditorView mPhotoEditorView;
    int PICK_PHOTO_FOR_AVATAR = 1;
    FrameLayout linear;
    Uri fullPhotoUri;
    String PicName;
    Button btnReset,btnSave;
    Bitmap bitmap;
    private final int PHOTO_EDITOR_REQUEST_CODE = 231;
    ImageView btnEdit;
    Bitmap OrignalBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnAddImage = findViewById(R.id.btnAddImage);
        btnText = findViewById(R.id.btnText);
        btnShadow = findViewById(R.id.btnShadow);
        btnDivider = findViewById(R.id.btnDivider);
        btnCanvas = findViewById(R.id.btnCanvas);
        linear =findViewById(R.id.linear);
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        btnEdit = findViewById(R.id.btnExp);
        btnReset = findViewById(R.id.btnReset);
        btnSave = findViewById(R.id.btnSave);


        Dexter.withContext(HomeActivity.this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(HomeActivity.this, "Allow Permission To Use This App", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    }
                })
                .check();

        //Initialising Custom ImageView from a GithubLibrary
        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();

        //Button to Open Gallery To Select Image
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
            }
        });

        //To Open Contrast Activity
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullPhotoUri == null){
                    Toast.makeText(HomeActivity.this, "Choose an Image First", Toast.LENGTH_SHORT).show();
                }else{
                  //To Open UCropActivity
                    Ucropactivity();
                }
            }
        });

        btnDivider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullPhotoUri == null){
                    Toast.makeText(HomeActivity.this, "Choose an Image First", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(HomeActivity.this, DividerActivity.class);
                    intent.putExtra("uri",fullPhotoUri);
                    intent.putExtra("ImageName",PicName);

                    startActivity(intent);
                }
            }
        });

        btnShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullPhotoUri == null){
                    Toast.makeText(HomeActivity.this, "Choose an Image First", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(HomeActivity.this, ShadowActivity.class);
                    intent.putExtra("uri",fullPhotoUri);
                    intent.putExtra("ImageName",PicName);
                    startActivity(intent);
                }
            }
        });

        btnCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullPhotoUri == null){
                    Toast.makeText(HomeActivity.this, "Choose an Image First", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(HomeActivity.this, CanvasActivity.class);
                    intent.putExtra("uri",fullPhotoUri);
                    intent.putExtra("ImageName",PicName);
                    startActivity(intent);
                }
            }
        });

        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullPhotoUri == null){
                    Toast.makeText(HomeActivity.this, "Choose an Image First", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(HomeActivity.this, TextActivity.class);
                    intent.putExtra("uri",fullPhotoUri);
                    intent.putExtra("ImageName",PicName);
                    startActivity(intent);
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constan.photoMap = OrignalBitmap;
                mPhotoEditorView.getSource().setImageBitmap(OrignalBitmap);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(constan.photoMap != null){
                    saveToInternalStorage(constan.photoMap,PicName);
                    Toast.makeText(HomeActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        //here it Checks if any Image is Selected or Not
        if(linear.getVisibility() == View.VISIBLE){
            fullPhotoUri = null;
            linear.setVisibility(View.INVISIBLE);
            btnAddImage.setVisibility(View.VISIBLE);
        }else{
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPhotoEditorView.getSource().setImageBitmap(constan.photoMap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //When We Select Pic From GAllery
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            try {
                linear.setVisibility(View.VISIBLE);
                btnAddImage.setVisibility(View.INVISIBLE);
                InputStream inputStream = HomeActivity.this.getContentResolver().openInputStream(data.getData());

                fullPhotoUri = data.getData();


                Uri uri = data.getData();
                //TO genrate Random ImageName
                PicName = "PicEd"+uri.toString().substring(uri.toString().indexOf("%")+1);
                mPhotoEditorView.getSource().setImageURI(fullPhotoUri);
                //Convert Uri into Bitmap
                try {
                   bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                   constan.photoMap = bitmap;
                   OrignalBitmap = bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        //When UCrop Activity Return Image
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                constan.photoMap = bitmap;
                mPhotoEditorView.getSource().setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File mypath = new File(getCacheDir(), "test.jpg");
            String Path = mypath.getPath();
            File file = new File(Path);
            file.delete();


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("tag",UCrop.getError(data).toString());
        }
    }

    //This is Static Class , So that we can Change Bitmap From Anywhere just CAll(HomeService.constan.photomap)
    public static class constan {
        public static Bitmap photoMap = null;
    }

    //Dexter Library To get Permission , See Github For more Info
    public void getPermission(){
        Dexter.withContext(HomeActivity.this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(HomeActivity.this, "Allow Permission To Use This App", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    }
                })
                .check();
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

    public void Ucropactivity(){

        //Here We Have to pass ImagePath , So First We will Save Bitmap in our App Cache Folder
        //and then Get path from cache Folder and Convert Path into Uri
        //And pass the Uri to UCrop Activity, Now When App Returns Image , We Will Delete that Temporary Saved image From Cache

        String Path;
        File mypath = new File(getCacheDir(), "test.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            constan.photoMap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path = mypath.getPath();
        File storageDir = new File(Path);
        Uri uri = Uri.fromFile(mypath);

        UCrop.of(uri, uri).start(HomeActivity.this);
    }

}