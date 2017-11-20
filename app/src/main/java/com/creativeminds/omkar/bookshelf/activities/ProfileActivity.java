package com.creativeminds.omkar.bookshelf.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeminds.omkar.bookshelf.Constants;
import com.creativeminds.omkar.bookshelf.R;
import com.creativeminds.omkar.bookshelf.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    TextView name_tv, email_tv, phno_tv, state_tv, city_tv;
    String name, email, phno, state, city,u_email,u_name,u_phno,message;
    FloatingActionButton edit_fab;
    ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        name_tv = (TextView) findViewById(R.id.user_name_tv_id);
        email_tv = (TextView) findViewById(R.id.user_email_tv_id);
        phno_tv = (TextView) findViewById(R.id.user_phone_tv_id);
        state_tv = (TextView) findViewById(R.id.user_state_tv_id);
        city_tv = (TextView) findViewById(R.id.user_city_tv_id);
        edit_fab = (FloatingActionButton) findViewById(R.id.edit_profile_fab_id);

        name = sharedPref.getString(Constants.TAG_USER_NAME, "null");
        email = sharedPref.getString(Constants.TAG_USER_EMAIL, "null");
        phno = sharedPref.getString(Constants.TAG_USER_PHONE, "null");
        state = sharedPref.getString(Constants.TAG_USER_STATE, "null");
        city = sharedPref.getString(Constants.TAG_USER_CITY, "null");

        name_tv.setText(name);
        email_tv.setText(email);
        phno_tv.setText(phno);
        state_tv.setText(state);
        city_tv.setText(city);

        edit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //editProfileDialog();

                Intent intent = new Intent(ProfileActivity.this,UpdateProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void editProfileDialog() {
        final EditText nameet = new EditText(this);
        nameet.setHint("Enter Name");
        nameet.setText(name);
        final EditText emailet = new EditText(this);
        emailet.setHint("Enter Email");
        emailet.setText(email);
        final EditText phoneet = new EditText(this);
        phoneet.setHint("Enter Phone No.");
        phoneet.setText(phno);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(nameet);
        linearLayout.addView(emailet);
        linearLayout.addView(phoneet);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                u_name = nameet.getText().toString();
                u_phno = phoneet.getText().toString();
                u_email = emailet.getText().toString();
                new updateUserDetails().execute();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    class updateUserDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setTitle("Updating Profile...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_UID, sharedPref.getString(Constants.TAG_UID,"null")));
            params.add(new BasicNameValuePair(Constants.TAG_USER_NAME, u_name));
            params.add(new BasicNameValuePair(Constants.TAG_USER_PHONE, u_phno));
            params.add(new BasicNameValuePair(Constants.TAG_USER_EMAIL, u_email));

            JSONObject json = jsonParser.makeHttpRequest(Constants.TAG_SERVER_MAIN_URL+Constants.TAG_SERVER_UPDATE_USER_DETAILS, "GET", params);

            Log.d("RESPONSE: ", json.toString());

            try {

                final int success = json.getInt(Constants.TAG_SUCCESS);

                if (success == 1) {

                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(ProfileActivity.this,MainActivity.class);
                            //intent.putExtra(Constants.TAG_USER_EMAIL,user_email);
                            progressDialog.dismiss();
                        }
                    });


                } else {
                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ProfileActivity.this,message,Toast.LENGTH_LONG).show();
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
                progressDialog.dismiss();
        }
    }

}
