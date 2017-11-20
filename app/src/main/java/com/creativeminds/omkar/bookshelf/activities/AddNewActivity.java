package com.creativeminds.omkar.bookshelf.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.creativeminds.omkar.bookshelf.Constants;
import com.creativeminds.omkar.bookshelf.R;
import com.creativeminds.omkar.bookshelf.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class AddNewActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText et_book_name,et_book_author,et_book_isbn,et_book_mrp,et_selling_price,et_book_desc,et_image_url;//,et_book_edition,et_book_category,et_book_condition
    Button add_btn,upload_img_btn;
    String book_name,image_url,book_author,book_isbn,book_edition,book_category,book_mrp,selling_price,book_condition,book_desc,date_created;
    JSONParser jParser = new JSONParser();
    String message;
    SharedPreferences sharedPref;
    AwesomeValidation mAwesomeValidation = new AwesomeValidation(BASIC);
    Spinner spinner_edition,spinner_category,spinner_condition;
    Activity activity= AddNewActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
         sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        declareElements();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidInputs()){
                    new AddNewBook().execute();
                }
            }
        });

        upload_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUploader(AddNewActivity.this,"net.moosen.imgur");
            }
        });

    }

    public void declareElements(){
        et_book_name = (EditText)findViewById(R.id.book_name_input_id);
        et_book_author = (EditText)findViewById(R.id.book_author_input_id);
        et_book_isbn = (EditText)findViewById(R.id.book_isbn_input_id);
        //et_book_edition = (EditText)findViewById(R.id.book_edition_input_id);
        //et_book_category = (EditText)findViewById(R.id.book_catagory_input_id);
        et_book_mrp = (EditText)findViewById(R.id.book_mrp_input_id);
        et_selling_price = (EditText)findViewById(R.id.book_sell_price_input_id);
        //et_book_condition = (EditText)findViewById(R.id.book_condition_input_id);
        et_book_desc = (EditText)findViewById(R.id.book_description_input_id);
        et_image_url = (EditText)findViewById(R.id.image_url_input_id);
        add_btn = (Button) findViewById(R.id.add_btn_id);
        upload_img_btn = (Button) findViewById(R.id.upload_btn_id);
        spinner_category = (Spinner) findViewById(R.id.book_category_spinner);
        spinner_condition = (Spinner) findViewById(R.id.book_condition_spinner);
        spinner_edition = (Spinner) findViewById(R.id.book_edition_spinner);
        setValidation();
    }

    public void setValidation(){

        //UserName Validation
        mAwesomeValidation.addValidation(activity, R.id.book_name_input_id, "^(?=\\s*\\S).*$" , R.string.err_bname);
        mAwesomeValidation.addValidation(activity, R.id.book_author_input_id, "^(?=\\s*\\S).*$" , R.string.err_bauthor);
        //Phone Validation
        mAwesomeValidation.addValidation(activity, R.id.book_isbn_input_id, "^[9]\\d{12}$", R.string.err_bisbn);

        mAwesomeValidation.addValidation(activity, R.id.book_mrp_input_id, "^[0-9]+$", R.string.err_bprice);
        mAwesomeValidation.addValidation(activity, R.id.book_sell_price_input_id, "^[0-9]+$", R.string.err_bprice);

        //mAwesomeValidation.addValidation(activity, R.id.book_description_input_id, "^(?=\\s*\\S).*$", R.string.err_bdesc);
        mAwesomeValidation.addValidation(activity, R.id.image_url_input_id, "^(?=\\s*\\S).*$", R.string.err_bimageurl);


    }

    public boolean isValidInputs(){
        book_name = et_book_name.getText().toString();
        book_author = et_book_author.getText().toString();
        book_isbn = et_book_isbn.getText().toString();
        book_edition = spinner_edition.getSelectedItem().toString();
        book_category = spinner_category.getSelectedItem().toString();
        book_mrp = et_book_mrp.getText().toString();
        selling_price = et_selling_price.getText().toString();
        book_condition = spinner_condition.getSelectedItem().toString();
        book_desc = et_book_desc.getText().toString();
        image_url = et_image_url.getText().toString();
        date_created = getCurrentDate();

        return  mAwesomeValidation.validate();
    }

    public String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    class AddNewBook extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddNewActivity.this);
            progressDialog.setTitle("Please wait...");
            progressDialog.setMessage("Adding New Book...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_UID, sharedPref.getString(Constants.TAG_UID,"null")));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_NAME, book_name));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_AUTHOR, book_author));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_EDITION, book_edition));
            params.add(new BasicNameValuePair(Constants.TAG_ISBN, book_isbn));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_CATEGORY, book_category));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_MRP, book_mrp));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_SELLING_PRICE, selling_price));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_CONDITION, book_condition));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_DESCRIPTION, book_desc));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_DATE_CREATED,date_created ));
            params.add(new BasicNameValuePair(Constants.TAG_USER_CITY,sharedPref.getString(Constants.TAG_USER_CITY,"null") ));
            params.add(new BasicNameValuePair(Constants.TAG_BOOK_IMAGE_URL,image_url ));


            JSONObject json = jParser.makeHttpRequest(Constants.TAG_SERVER_MAIN_URL+Constants.TAG_SERVER_ADD_NEW_BOOK, "GET", params);

            Log.d("RESPONSE: ", json.toString());

            try {

                final int success = json.getInt(Constants.TAG_SUCCESS);

                if (success == 1) {

                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddNewActivity.this,message,Toast.LENGTH_LONG).show();
                            Intent intent =new Intent(AddNewActivity.this,MainActivity.class);
                            //intent.putExtra(Constants.TAG_USER_EMAIL,user_email);
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    });


                } else {
                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddNewActivity.this,message,Toast.LENGTH_LONG).show();
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

    @Override
    public void onResume(){
        super.onResume();
        try {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            String url = clipboard.getText().toString();

            if (!url.isEmpty() && url.startsWith("https://i.imgur.com")) {
                et_image_url.setText(url);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean openUploader(Context context,String packageName){


        try {
            PackageManager manager = context.getPackageManager();
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                Toast.makeText(context,"As Uploader is Not Installed , Install Now From Playstore",Toast.LENGTH_LONG).show();
                return false;
                //throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            Toast.makeText(context,"As Uploader is Not Installed , Install Now From Playstore",Toast.LENGTH_LONG).show();
            return false;
        }

    }
}
