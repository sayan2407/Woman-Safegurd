package com.shalaka.womansafegurd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton cam;
    TextInputEditText email;
    Button send;
    ImageView imageView;
    String mail;
    Uri file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       cam=findViewById(R.id.camera);
        email=findViewById(R.id.mail);
        imageView=findViewById(R.id.image1);
        send=findViewById(R.id.send);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cam.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //        file = Uri.fromFile(getOutputMediaFile());
                //        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                startActivityForResult(intent, 100);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail=email.getText().toString().trim();
                if (mail.isEmpty() || file==null)
                {
                    Toast.makeText(CameraActivity.this, "Please Enter Email or take picture", Toast.LENGTH_LONG).show();
                }
                else
                {
                    sendMail(file,mail);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap capture =(Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(capture);
            file=getImageUri(CameraActivity.this,capture);
        }
    }
    public void sendMail(Uri uri,String mail)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mail});
        i.putExtra(Intent.EXTRA_SUBJECT, "HELP ME");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        i.putExtra(Intent.EXTRA_TEXT   , "Hi,I am in trouble,please help me by reaching to this palce. I send you my location also in message");
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setType("image/jpeg");
        //  i.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://"+getPackageName()+"/"+imageURI));


        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CameraActivity.this, "Email not snet", Toast.LENGTH_SHORT).show();
        }

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(CameraActivity.this.getContentResolver(), inImage, UUID.randomUUID().toString() + ".png", "drawing");
        return Uri.parse(path);
    }
}