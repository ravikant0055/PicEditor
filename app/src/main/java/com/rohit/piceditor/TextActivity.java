package com.rohit.piceditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;

public class TextActivity extends AppCompatActivity {

    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    TextView edtText;
    Button btnSave,btnDone;
    Button btnSetText;
    String ImageName;
    int color = Color.WHITE;
    Uri photo;
    Typeface mTypeface;
    int font = R.font.muli_black;
    public static View root;
    LinearLayout FontLinear;
    ImageView btnAddMore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        //Github Library Link to Add More Features
        //https://github.com/burhanrashid52/PhotoEditor

        mPhotoEditorView = findViewById(R.id.photoViewText);
        edtText = findViewById(R.id.edtText);
        btnSave = findViewById(R.id.btnSave);
        btnSetText = findViewById(R.id.btnSetText);
        btnDone = findViewById(R.id.btnDone);
        FontLinear = findViewById(R.id.FontLinear);
        btnAddMore = findViewById(R.id.btnAddMore);

        //get Photo Uri And RandomName By Intent
        photo = getIntent().getParcelableExtra("uri");
        ImageName = getIntent().getStringExtra("ImageName");
        Bitmap photoBitmap = HomeActivity.constan.photoMap;

        //set Image on Custom ImageView(Library View)
        mPhotoEditorView.getSource().setImageBitmap(photoBitmap);

        //Initialising Custom ImageView Check Github Library Page for more Options
        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();



        btnDone.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.VISIBLE);


        //This Function Will Activate on LongPress On Text, so we Store View id of Long Pressed Text in root, and use it to modify
        mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(View rootView, String text, int colorCode) {
                Toast.makeText(TextActivity.this, "Click To Edit", Toast.LENGTH_SHORT).show();
                root = rootView;
                btnDone.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
                FontLinear.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
            }
            @Override
            public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
            }
            @Override
            public void onStartViewChangeListener(ViewType viewType) {
            }

            @Override
            public void onStopViewChangeListener(ViewType viewType) {

            }
        });

        //When Keyboard Open this Will Activate ,so that View in EditText Will Not Cut
        KeyboardVisibilityEvent.setEventListener(
                TextActivity.this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        // some code depending on keyboard visiblity status
                        if(isOpen){
                            btnSave.setVisibility(View.GONE);
                            btnDone.setVisibility(View.GONE);
                            FontLinear.setVisibility(View.GONE);
                        }else{
                            btnSave.setVisibility(View.VISIBLE);
                            btnDone.setVisibility(View.VISIBLE);
                            FontLinear.setVisibility(View.VISIBLE);
                        }

                    }
                });




    }

    //Function TO Save Image In internal storage
    private void saveToInternalStorageForText(String Name){
        File storageDir = new File(Environment.getExternalStorageDirectory()
                + "/PicEditor");
        File cacheDir = new File(getCacheDir(),"test.jpg");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File mypath=new File(storageDir,Name + "txt.jpg");

        String filePath = cacheDir.getPath();
        if (ActivityCompat.checkSelfPermission(TextActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mPhotoEditor.saveAsFile(filePath, new PhotoEditor.OnSaveListener() {
            @Override
            public void onSuccess(@NonNull String imagePath) {
             //   Toast.makeText(TextActivity.this, "Image Successfully saved", Toast.LENGTH_SHORT).show();

                File mypath = new File(imagePath);
                Bitmap bitmap = null;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                try {
                   bitmap = BitmapFactory.decodeStream(new FileInputStream(mypath), null, options);
                    HomeActivity.constan.photoMap = bitmap;
                    String Path = mypath.getPath();
                    File file = new File(Path);
                    file.delete();

                    finish();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("PhotoEditor", "Failed to save Image");
            }
        });
    }

    public void ChangeText(View rootView,String Text,int Color){
        mPhotoEditor.editText(rootView, Text, color);
    }

    public void btnClicked(View view){
        switch (view.getId()){

            //Fonts Clicks
            case R.id.font1:
                font = R.font.black_ops_one;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                break;
            case R.id.font2:
                font = R.font.lato;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                break;
            case R.id.font3:
                font = R.font.lora;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                break;
            case R.id.font4:
                font = R.font.muli;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                break;
            case R.id.font5:
                font = R.font.pacifico;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                break;
            case R.id.font6:
                font = R.font.muli_extrabold;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                break;

                //Color Clicks
            case R.id.black:
                color = Color.BLACK;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);

                if(root != null){
                    ChangeText(root,edtText.getText().toString(),color);
                }
                break;
            case R.id.white:
                color = Color.WHITE;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                if(root != null){
                    ChangeText(root,edtText.getText().toString(),color);
                }
                break;
            case R.id.blue:
                color = Color.BLUE;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                if(root != null){
                    ChangeText(root,edtText.getText().toString(),color);
                }
                break;
            case R.id.red:
                color = Color.RED;
                mPhotoEditor.undo();
                mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                if(root != null){
                    ChangeText(root,edtText.getText().toString(),color);
                }
                break;

            case R.id.btnSetText:
                if (edtText.getText().toString().equals("")) {
                    Toast.makeText(TextActivity.this, "Write Something", Toast.LENGTH_SHORT).show();
                } else {
                    //Describing Font
                    if(root != null){
                        ChangeText(root,edtText.getText().toString(),color);
                    }else{
                        if(btnSetText.getText().toString().equals("Add")){
                            btnSetText.setText("Edit");
                            mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                            mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                        }else{
                            btnSetText.setText("Edit");
                            mPhotoEditor.undo();
                            mTypeface = ResourcesCompat.getFont(TextActivity.this, font);
                            mPhotoEditor.addText(mTypeface,edtText.getText().toString(), color);
                        }

                    }
                }
                break;
            case R.id.btnAddMore:
                btnSetText.setText("Add");
                break;

            case R.id.btnSave:
                saveToInternalStorageForText(ImageName);
                break;

            case R.id.btnDone:
                root = null;
                btnSave.setVisibility(View.VISIBLE);;
                btnDone.setVisibility(View.INVISIBLE);
                FontLinear.setVisibility(View.VISIBLE);
                break;
        }
    }


    //Font Guide
    /*
    * Guide To Add More Fonts -
    * To add More Fonts, add a Font XML file in res/fonts folder and describe it
    * in res/values/preloaded_fonts,
    * To add A new Fonts , copy the Whole Code of any Font and just replace
    * name of NEw Font, for ex- app:fontProviderQuery="Muli" change Muli to NEw Font
    * and set a new Button for this new Font
    * */
}