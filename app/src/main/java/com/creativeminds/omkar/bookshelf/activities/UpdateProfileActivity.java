package com.creativeminds.omkar.bookshelf.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.creativeminds.omkar.bookshelf.Constants;
import com.creativeminds.omkar.bookshelf.R;
import com.creativeminds.omkar.bookshelf.utils.JSONParser;
import com.creativeminds.omkar.bookshelf.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText user_name_et,user_pass_et,user_pass2_et,user_email_et,user_phone_et;//user_city_et,user_state_et;
    String user_name,user_pass,user_pass2,user_email,user_phone,message,user_city,user_state;
    Button register_btn;
    SharedPreferences sharedPref;
    Utils utils;
    ProgressDialog progressDialog;
    JSONParser jParser = new JSONParser();
    Activity activity;
    Spinner city_spinner,state_spinner;
    AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = UpdateProfileActivity.this;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(UpdateProfileActivity.this);
        utils = new Utils(UpdateProfileActivity.this);
        declareElements();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isvalidFields()){
                    new RegisterNewUser().execute();
                }

            }
        });



    }

    public void declareElements(){
        user_name_et = (EditText) findViewById(R.id.user_name_input_id);
        user_pass_et = (EditText) findViewById(R.id.user_pass_input_id);
        user_pass2_et = (EditText) findViewById(R.id.user_pass2_input_id);
        user_email_et = (EditText) findViewById(R.id.user_email_input_id);
        user_phone_et = (EditText) findViewById(R.id.user_phone_input_id);
        register_btn = (Button) findViewById(R.id.register_btn_id);
        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        city_spinner = (Spinner) findViewById(R.id.city_spinner);

        register_btn.setText("Update Profile");


        user_name = sharedPref.getString(Constants.TAG_USER_NAME, "null");
        user_pass = sharedPref.getString(Constants.TAG_USER_PASSWORD, "null");
        user_pass2 = sharedPref.getString(Constants.TAG_USER_PASSWORD, "null");
        user_email = sharedPref.getString(Constants.TAG_USER_EMAIL, "null");
        user_phone =sharedPref.getString(Constants.TAG_USER_PHONE, "null");
        user_city = sharedPref.getString(Constants.TAG_USER_CITY, "null");
        user_state = sharedPref.getString(Constants.TAG_USER_STATE, "null");

        user_name_et.setText(user_name);
        user_pass_et.setText(user_pass);
        user_pass2_et.setText(user_pass2);
        user_email_et.setText(user_email);
        user_phone_et.setText(user_phone);

        ArrayAdapter<CharSequence> state_adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(state_adapter);
        if (!user_state.isEmpty()) {
            int spinnerPosition = state_adapter.getPosition(user_state);
            state_spinner.setSelection(spinnerPosition);
        }

        ArrayAdapter<CharSequence> city_adapter = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_item);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(city_adapter);
        if (!user_city.isEmpty()) {
            int spinnerPosition = city_adapter.getPosition(user_city);
            city_spinner.setSelection(spinnerPosition);
        }


        setValidation();

    }

    public void setValidation(){

        //Email Validation
        mAwesomeValidation.addValidation(activity, R.id.user_email_input_id, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);

        //UserName Validation
        mAwesomeValidation.addValidation(activity, R.id.user_name_input_id, "[a-zA-Z\\s]+" , R.string.err_name);

        //Phone Validation
        mAwesomeValidation.addValidation(activity, R.id.user_phone_input_id, "^[789]\\d{9}$", R.string.err_phone);

        //Password Validation
        String regexPassword = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}$";
        mAwesomeValidation.addValidation(activity, R.id.user_pass_input_id, regexPassword, R.string.err_pass);
        mAwesomeValidation.addValidation(activity, R.id.user_pass2_input_id, R.id.user_pass_input_id, R.string.err_pass_mis);

    }

    public boolean isvalidFields(){
        user_name = user_name_et.getText().toString();
        user_pass = user_pass_et.getText().toString();
        user_pass2 = user_pass2_et.getText().toString();
        user_email = user_email_et.getText().toString();
        user_phone = user_phone_et.getText().toString();
        user_city = city_spinner.getSelectedItem().toString();
        user_state = state_spinner.getSelectedItem().toString();

       if(!mAwesomeValidation.validate()){
            return false;
        }else{
            return true;
        }


    }

    class RegisterNewUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UpdateProfileActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Updating User Details...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_UID,sharedPref.getString(Constants.TAG_UID,"null")));
            params.add(new BasicNameValuePair(Constants.TAG_USER_NAME, user_name));
            params.add(new BasicNameValuePair(Constants.TAG_USER_PASSWORD, user_pass));
            params.add(new BasicNameValuePair(Constants.TAG_USER_EMAIL, user_email));
            params.add(new BasicNameValuePair(Constants.TAG_USER_PHONE, user_phone));
            params.add(new BasicNameValuePair(Constants.TAG_USER_CITY, user_city));
            params.add(new BasicNameValuePair(Constants.TAG_USER_STATE, user_state));

            JSONObject json = jParser.makeHttpRequest(Constants.TAG_SERVER_MAIN_URL+Constants.TAG_SERVER_UPDATE_USER_DETAILS, "POST", params);

            Log.d("RESPONSE: ", json.toString());

            try {

                final int success = json.getInt(Constants.TAG_SUCCESS);

                if (success == 1) {

                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateProfileActivity.this,message,Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(UpdateProfileActivity.this,LoginActivity.class);
                            intent.putExtra(Constants.TAG_USER_EMAIL,user_email);
                            intent.putExtra(Constants.TAG_USER_PASSWORD,user_pass);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    });


                } else {
                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateProfileActivity.this,message,Toast.LENGTH_LONG).show();
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
