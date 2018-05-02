package com.example.sloff.tokentestempty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Button btnCamera = (Button)findViewById(R.id.btnCamera);
        imageView = (ImageView) findViewById(R.id.testImg);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");

        File f = new File(Environment.getExternalStorageDirectory()+"/"+ UUID.randomUUID().toString()+".jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", f.getName(), reqFile);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://192.168.50.126:3000")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        UserClient userClient = retrofit.create(UserClient.class);

        Call<ResponseBody> call = userClient.uploadPhoto(body);

//                    Toast.makeText(CameraActivity.this, "Saved "+file, Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(ImageActivity.this, "success :)", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//        ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

        imageView.setImageBitmap(bitmap);
    }
}
