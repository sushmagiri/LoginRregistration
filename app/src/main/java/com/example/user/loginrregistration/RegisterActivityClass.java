package com.example.user.loginrregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RegisterActivityClass extends AppCompatActivity {

    String response;
    HttpClient client;
    String url = "http://192.168.1.28/loginregister/registration.php";
    EditText et_name, et_email, et_password;
    Button btn_submit,btn_login;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_name=(EditText)findViewById(R.id.name);
        et_email=(EditText)findViewById(R.id.email);
        et_password=(EditText)findViewById(R.id.password);
        btn_submit=(Button)findViewById(R.id.btnRegister);
        btn_login=(Button)findViewById(R.id.btnLinkToLoginScreen);
        client = new DefaultHttpClient();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new LongOperation().execute(url);
            }
            });
        btn_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent j = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(j);
                finish();
            }
        });
    }
    private class LongOperation  extends AsyncTask<String, Void, Void> {
        private String error = null;
        private ProgressDialog progressDialog = new ProgressDialog(RegisterActivityClass.this);
        protected void onPreExecute() {
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();

        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            HttpGet request = new HttpGet(urls[0].toString()+"?name="+et_name.getText().toString()+"&email="+et_email.getText().toString()+"&password="+et_password.getText().toString());
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
                error=Log.getStackTraceString(e);
            } catch (IOException e) {
                error=Log.getStackTraceString(e);;
            }
            response=stringBuilder.toString();
            return null;
        }
        protected void onPostExecute(Void unused) {
            progressDialog.dismiss();
            if(error==null ){
//                Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                Toast.makeText(RegisterActivityClass.this, response, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(RegisterActivityClass.this,"No internet connection", Toast.LENGTH_SHORT).show();
                Log.d("Error", error);
            }
        }
    }

}

