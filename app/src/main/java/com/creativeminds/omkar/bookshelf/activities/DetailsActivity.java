package com.creativeminds.omkar.bookshelf.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.creativeminds.omkar.bookshelf.Constants;
import com.creativeminds.omkar.bookshelf.R;
import com.creativeminds.omkar.bookshelf.models.Book;
import com.creativeminds.omkar.bookshelf.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    TextView name_tv, author_tv, mrp_tv, sp_tv, edition_tv, cat_tv, cond_tv, desc_tv,posted_by;
    FloatingActionButton wa_fab, msg_fab, call_fab, email_fab;
    String name, author, mrp, sp, edition, cat, cond, desc, phno, email, img_url, u_id, b_id, message,uname,ucity;
    ImageView img;
    JSONParser jParser = new JSONParser();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        declareElements();
        getDataFromIntent();
        setData();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        wa_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String smsNumber = phno;
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.setData(Uri.parse("https://api.whatsapp.com/send?phone=91" + phno + "&text=Hello%20*"+uname+"*, I%20am%20Interested%20in%20Buying%20your%20Book%20*"+name+"*%0AWhich%20You%20Posted%20on%20BookShelf"));
                    in.setPackage("com.whatsapp");
                    startActivity(in);
                } catch (Exception e) {
                    Toast.makeText(DetailsActivity.this, "Whatsapp Not Installed", Toast.LENGTH_LONG).show();
                }
            }
        });

        msg_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + phno);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("Book Shelf", "Hello "+uname+", I am Interested in Buying Your " + name + " Book on Book Shelf App");
                startActivity(it);
            }
        });


        call_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phno));
                startActivity(intent);
            }
        });

        email_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Byuing Your Book "+name);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello "+uname+", I am Interested in Byuing Your Book.Which You Posted on BookShelf App");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, email); // String[] addresses
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });


    }

    public void declareElements() {
        name_tv = (TextView) findViewById(R.id.book_name_tv_id);
        author_tv = (TextView) findViewById(R.id.book_author_tv_id);
        mrp_tv = (TextView) findViewById(R.id.book_mrp_tv_id);
        sp_tv = (TextView) findViewById(R.id.book_sp_tv_id);
        edition_tv = (TextView) findViewById(R.id.book_edition_tv_id);
        cat_tv = (TextView) findViewById(R.id.book_category_tv_id);
        cond_tv = (TextView) findViewById(R.id.book_condition_tv_id);
        desc_tv = (TextView) findViewById(R.id.book_desc_tv_id);
        posted_by = (TextView) findViewById(R.id.posted_by_tv);

        wa_fab = (FloatingActionButton) findViewById(R.id.fab_whatspp_id);
        msg_fab = (FloatingActionButton) findViewById(R.id.fab_message_id);
        call_fab = (FloatingActionButton) findViewById(R.id.fab_call_id);
        email_fab = (FloatingActionButton) findViewById(R.id.fab_email_id);

        img = (ImageView) findViewById(R.id.product_image);

    }

    public void getDataFromIntent() {
        Intent intent = getIntent();
        name = intent.getStringExtra(Constants.TAG_BOOK_NAME);
        author = intent.getStringExtra(Constants.TAG_BOOK_AUTHOR);
        mrp = intent.getStringExtra(Constants.TAG_BOOK_MRP);
        sp = intent.getStringExtra(Constants.TAG_BOOK_SELLING_PRICE);
        cond = intent.getStringExtra(Constants.TAG_BOOK_CONDITION);
        cat = intent.getStringExtra(Constants.TAG_BOOK_CATEGORY);
        edition = intent.getStringExtra(Constants.TAG_BOOK_EDITION);
        desc = intent.getStringExtra(Constants.TAG_BOOK_DESCRIPTION);
        img_url = intent.getStringExtra(Constants.TAG_BOOK_IMAGE_URL);
        u_id = intent.getStringExtra(Constants.TAG_UID);
        b_id = intent.getStringExtra(Constants.TAG_BID);
    }

    public void setData() {
        name_tv.setText(name);
        author_tv.setText(author);
        mrp_tv.setText(getString(R.string.rupee_symbol)+mrp);
        sp_tv.setText(getString(R.string.rupee_symbol)+sp);
        edition_tv.setText(edition);
        cat_tv.setText(cat);
        cond_tv.setText(cond);
        desc_tv.setText(desc);

        new getUserDetails().execute();

        Glide.with(DetailsActivity.this).load(img_url)
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(img);
    }

    class getUserDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailsActivity.this);
            progressDialog.setTitle("Loading Details..");
            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(true);
            progressDialog.show();


        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_UID, u_id));
            JSONObject json = jParser.makeHttpRequest(Constants.TAG_SERVER_MAIN_URL + Constants.TAG_SERVER_USER_DETAILS, "GET", params);

            Log.d("RESPONSE: ", json.toString());

            try {

                final int success = json.getInt(Constants.TAG_SUCCESS);

                if (success == 1) {

                    message = json.getString(Constants.TAG_MESSAGE);
                    JSONArray users = json.getJSONArray(Constants.TAG_USERS_ARRAY);


                    JSONObject user = users.getJSONObject(0);
                    u_id = user.getString(Constants.TAG_UID);
                    uname = user.getString(Constants.TAG_USER_NAME);
                    phno = user.getString(Constants.TAG_USER_PHONE);
                    email = user.getString(Constants.TAG_USER_EMAIL);
                    ucity = user.getString(Constants.TAG_USER_CITY);


                } else {
                    message = json.getString(Constants.TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            if(uname.isEmpty()){
                uname = "Error in Fetching Details";
                ucity = "404";
            }
            posted_by.setText(uname+"("+ucity+")");
            progressDialog.dismiss();

        }
    }

}
