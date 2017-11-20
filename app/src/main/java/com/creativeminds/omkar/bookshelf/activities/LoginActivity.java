package com.creativeminds.omkar.bookshelf.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeminds.omkar.bookshelf.Constants;
import com.creativeminds.omkar.bookshelf.R;
import com.creativeminds.omkar.bookshelf.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText user_email_et,user_pass_et;
    Button login_btn;
    TextView register_tv;
    String email,pass,message;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    JSONParser jParser = new JSONParser();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_email_et = (EditText)findViewById(R.id.user_email_input_id);
        user_pass_et = (EditText)findViewById(R.id.user_pass_input_id);
        login_btn = (Button) findViewById(R.id.login_btn_id);
        register_tv = (TextView) findViewById(R.id.register_tv_id);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        if(sharedPref.getBoolean(Constants.TAG_LOGGED_IN,false)){
            user_email_et.setText(sharedPref.getString(Constants.TAG_USER_EMAIL,""));
            user_pass_et.setText(sharedPref.getString(Constants.TAG_USER_PASSWORD,""));
            email = user_email_et.getText().toString();
            pass = user_pass_et.getText().toString();
            new CheckLogin().execute();
        }

        if(getIntent().getStringExtra(Constants.TAG_USER_EMAIL)!= null && getIntent().getStringExtra(Constants.TAG_USER_PASSWORD) != null){
            user_email_et.setText(getIntent().getStringExtra(Constants.TAG_USER_EMAIL));
            user_pass_et.setText(getIntent().getStringExtra(Constants.TAG_USER_PASSWORD));
        }else if(getIntent().getStringExtra(Constants.TAG_USER_EMAIL)!= null){
            user_email_et.setText(getIntent().getStringExtra(Constants.TAG_USER_EMAIL));
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = user_email_et.getText().toString();
                pass = user_pass_et.getText().toString();

                if(email.isEmpty()){
                    user_email_et.setError("Enter Email");
                }else if(pass.isEmpty()){
                    user_pass_et.setError("Enter Password");
                }else {
                   // Toast.makeText(LoginActivity.this,"Sucess",Toast.LENGTH_SHORT).show();
                    new CheckLogin().execute();
                }

            }
        });

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    class CheckLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_USER_EMAIL, email));
            params.add(new BasicNameValuePair(Constants.TAG_USER_PASSWORD, pass));
            JSONObject json = jParser.makeHttpRequest(Constants.TAG_SERVER_MAIN_URL+Constants.TAG_SERVER_CHECK_LOGIN, "POST", params);

            Log.d("RESPONSE: ", json.toString());

            try {

                final int success = json.getInt(Constants.TAG_SUCCESS);

                if (success == 1) {

                    message = json.getString(Constants.TAG_MESSAGE);
                    JSONArray users = json.getJSONArray(Constants.TAG_USERS_ARRAY);

                    JSONObject user = users.getJSONObject(0);
                    final String uid = user.getString(Constants.TAG_UID);
                    final String name = user.getString(Constants.TAG_USER_NAME);
                    final String uemail = user.getString(Constants.TAG_USER_EMAIL);
                    final String u_pass = user.getString(Constants.TAG_USER_PASSWORD);
                    final String phno = user.getString(Constants.TAG_USER_PHONE);
                    final String city = user.getString(Constants.TAG_USER_CITY);
                    final String state = user.getString(Constants.TAG_USER_STATE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();

                             editor = sharedPref.edit();
                            editor.putString(Constants.TAG_UID, uid);
                            editor.putString(Constants.TAG_USER_NAME, name);
                            editor.putString(Constants.TAG_USER_EMAIL, uemail);
                            editor.putString(Constants.TAG_USER_PASSWORD, u_pass);
                            editor.putString(Constants.TAG_USER_PHONE, phno);
                            editor.putString(Constants.TAG_USER_CITY, city);
                            editor.putString(Constants.TAG_USER_STATE, state);
                            editor.putBoolean(Constants.TAG_LOGGED_IN,true);
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            progressDialog.dismiss();
                            finish();
                        }
                    });


                } else {
                     message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                    });
                    progressDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

        }
    }
}
