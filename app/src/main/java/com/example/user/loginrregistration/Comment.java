package com.example.user.loginrregistration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 8/8/2016.
 */
public class Comment extends AppCompatActivity {
    Button comment, done;
    ListView lv;
    Context context;
    EditText comment1;
    String username;
    TextView user;
    HttpClient client;
    String response;
    ArrayList<Entity> entityArrayList;
    private static final String REGISTER_URL = "http://192.168.1.28/Loginregister/comment.php";
    private static final String LIST_URL = "http://192.168.1.28/Loginregister/list.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        lv = (ListView) findViewById(R.id.listView);

     //   View header = getLayoutInflater().inflate(R.layout.header_list,null);
        //lv.addHeaderView(header, null, false);

        comment = (Button) findViewById(R.id.bt_cmnt);
        done = (Button)findViewById(R.id.done);
        comment1 = (EditText)findViewById(R.id.et_comment);
        comment1.setVisibility(View.GONE);
        done.setVisibility(View.GONE);
        username=getIntent().getStringExtra("name");
        Log.d("userrr",username);
        try{
            String i=getIntent().getStringExtra("pass");
            if(i.equals("HELLO")){
                lv.setVisibility(View.VISIBLE);}
                else{
                lv.setVisibility(View.GONE);
            }

        }
        catch (Exception e){
            lv.setVisibility(View.GONE);
        }


        comment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                comment1.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);


            }
        });

        done.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Toast.makeText(Comment.this, "Comment successful", Toast.LENGTH_LONG).show();
                setcomment();

            }
        });
client=new DefaultHttpClient();
        new LongOperation().execute(LIST_URL);



    }

    private void setcomment() {
        String comment2 = comment1.getText().toString();
        doComment(username, comment2);

    }

    private void doComment(final String username, String comment2) {
        String urlSuffix = "?name=" + username + "&comment=" + comment2;

        class setcomment extends AsyncTask<String, String, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading=ProgressDialog.show(Comment.this,"Please Wait",null,true,true);
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute( s);
                loading.dismiss();

                Toast.makeText(Comment.this,s, Toast.LENGTH_LONG).show();


                finish();
                String i="HELLO";
                startActivity(getIntent().putExtra("pass",i));
            }

            @Override
            protected String doInBackground(String... strings) {
                String s = strings[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL + s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                } catch (Exception e) {
                    return null;
                }
            }




        }
        setcomment su=new setcomment();
        su.execute(urlSuffix);


    }

    private class LongOperation extends AsyncTask<String,String,Void>{

        private String error = null;
        private ProgressDialog progressDialog = new ProgressDialog(Comment.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(Comment.this,"Please wait",null,true,true);

        }



        @Override
        protected Void doInBackground(String... urls) {

            HttpGet request = new HttpGet(urls[0].toString());

            HttpResponse httpResponse;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                httpResponse = client.execute(request);
                HttpEntity entity = httpResponse.getEntity();
                InputStream stream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));


                String line=null;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (ClientProtocolException e) {
                error="Error";
            } catch (IOException e) {
                error="Error";
            }
            response=stringBuilder.toString();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(error==null){
                try {

                    if(response.contains("[") && response.contains("]")) {
                        response = response.substring(response.indexOf('['), response.indexOf(']') + 1);
                        JSONArray jsonArray = new JSONArray(response);
                        // Toast.makeText(getActivity(),""+jsonArray.length(),Toast.LENGTH_LONG).show();
                        JSONObject jsonObject = new JSONObject();

                        Log.d("json array length", String.valueOf(jsonArray.length()));

                        entityArrayList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);


                            entityArrayList.add(new Entity(jsonObject.getInt("comment_id"),jsonObject.getString("name"),jsonObject.getString("comment")));
                        }


                        Log.d("comment size", String.valueOf(entityArrayList.size()));


                        lv.setAdapter(new CustomAdapter(getApplicationContext(),entityArrayList));



                    }


                }
                catch (JSONException e){
                    Toast.makeText(Comment.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }else{
                Toast.makeText(Comment.this,error, Toast.LENGTH_SHORT).show();
            }

        }

    }


    }



