package com.example.sloff.tokentestempty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardActivity extends AppCompatActivity {
    public class Document {
        String title;
        String docId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        @Override
        public String toString() {
            return this.title;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent activityThatCalled = getIntent();

        String userInfo = activityThatCalled.getStringExtra("user");

        ArrayList<Document> docs = new ArrayList<>();
        String[] docDisplay;


        try{
            JSONObject json = new JSONObject(userInfo);
            JSONArray ja = json.getJSONArray("documents");
//            JSONObject bocs = ja.getJSONObject(0);
//            String title = bocs.getString("title");
//            String id = bocs.getString("reference");


            for(int i = 0; i < ja.length();i++){
                JSONObject bocs = ja.getJSONObject(i);
                String title = bocs.getString("title");
                String id = bocs.getString("reference");

                Document temp = new Document();
                temp.setTitle(title);
                temp.setDocId(id);

                docs.add(temp);
            }

//            Toast.makeText(DashboardActivity.this, docs.get(0), Toast.LENGTH_LONG).show();
        }catch (JSONException e){
            Toast.makeText(DashboardActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }


//        Toast.makeText(DashboardActivity.this, userInfo, Toast.LENGTH_SHORT).show();


        ListAdapter theAdapter = new ArrayAdapter<Document>(this, android.R.layout.simple_list_item_1,docs);

        final ListView theListView = (ListView) findViewById(R.id.theListView) ;

        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Document a = (Document) theListView.getAdapter().getItem(position);
                String did = a.getDocId();
//                String tvShowPicked = "Doc ID: " + did;

                getDocContents(did);



//                Toast.makeText(DashboardActivity.this, tvShowPicked, Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent getCameraIntent = new Intent(this, CameraActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(getCameraIntent);
            }
        });
    }



    //192.168.50.126:3000
    private void getDocContents(String id) {
        final Intent getDocScreenIntent = new Intent(this,  DocumentActivity.class);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        UserClient userClient = retrofit.create(UserClient.class);

        Call<ResponseBody> call = userClient.getDocBody("http://192.168.50.126:3000/api/" + id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String res = response.body().string();
//                        Toast.makeText(DashboardActivity.this, res, Toast.LENGTH_SHORT).show();

                        getDocScreenIntent.putExtra("docContents", res);
                        startActivity(getDocScreenIntent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
//                    Toast.makeText(DashboardActivity.this, "Wrong Token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
