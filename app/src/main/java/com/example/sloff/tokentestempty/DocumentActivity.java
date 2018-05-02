package com.example.sloff.tokentestempty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        Intent activityThatCalled = getIntent();

        String doc = activityThatCalled.getStringExtra("docContents");

        TextView textBox = (TextView) findViewById(R.id.docBody);
        textBox.setMovementMethod(new ScrollingMovementMethod());

        try {
            String text = "";

            JSONObject temp = new JSONObject(doc);
            JSONArray tarr = temp.getJSONArray("ops");
//            JSONObject od = tarr.getJSONObject(0);
//            String tt = od.getString("insert");
//
//            Toast.makeText(DocumentActivity.this, tt, Toast.LENGTH_LONG).show();
            for(int i = 0; i < tarr.length(); i++){
                JSONObject od = tarr.getJSONObject(i);
                String tt = od.getString("insert");
                text += tt;
            }

            textBox.append(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
