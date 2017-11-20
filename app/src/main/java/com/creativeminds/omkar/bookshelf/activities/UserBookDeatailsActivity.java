package com.creativeminds.omkar.bookshelf.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class UserBookDeatailsActivity extends AppCompatActivity {

    TextView name_tv, author_tv, mrp_tv, sp_tv, edition_tv, cat_tv, cond_tv, desc_tv;
    FloatingActionButton edit_fab, delete_fab;
    String name, author, mrp, sp, edition, cat, cond, desc, phno, email, img_url, u_id, b_id, message,uname,ucity,isbn;
    ImageView img;
    JSONParser jParser = new JSONParser();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_details);
        declareElements();
        getDataFromIntent();
        setData();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        edit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserBookDeatailsActivity.this, UpdateBookActivity.class);
                intent.putExtra(Constants.TAG_BOOK_NAME,name);
                intent.putExtra(Constants.TAG_UID,author);
                intent.putExtra(Constants.TAG_BID,b_id);
                intent.putExtra(Constants.TAG_BOOK_AUTHOR,author);
                intent.putExtra(Constants.TAG_ISBN,isbn);
                intent.putExtra(Constants.TAG_BOOK_MRP,mrp);
                intent.putExtra(Constants.TAG_BOOK_SELLING_PRICE,sp);
                intent.putExtra(Constants.TAG_BOOK_CONDITION,cond);
                intent.putExtra(Constants.TAG_BOOK_CATEGORY,cat);
                intent.putExtra(Constants.TAG_BOOK_EDITION,edition);
                intent.putExtra(Constants.TAG_BOOK_DESCRIPTION,desc);
                intent.putExtra(Constants.TAG_USER_PHONE,phno);
                intent.putExtra(Constants.TAG_USER_EMAIL,email);
                intent.putExtra(Constants.TAG_BOOK_IMAGE_URL,img_url);
                startActivity(intent);
                finish();
            }
        });

        delete_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteBook().execute();

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

        edit_fab = (FloatingActionButton) findViewById(R.id.fab_edit_id);
        delete_fab = (FloatingActionButton) findViewById(R.id.fab_delete_id);

        img = (ImageView) findViewById(R.id.product_image);

    }

    public void getDataFromIntent() {
        Intent intent = getIntent();
        name = intent.getStringExtra(Constants.TAG_BOOK_NAME);
        author = intent.getStringExtra(Constants.TAG_BOOK_AUTHOR);
        isbn = intent.getStringExtra(Constants.TAG_ISBN);
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



        Glide.with(UserBookDeatailsActivity.this).load(img_url)
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(img);
    }

    class deleteBook extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UserBookDeatailsActivity.this);
            progressDialog.setTitle("Deleting Book..");
            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(true);
            progressDialog.show();


        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_BID, b_id));
            JSONObject json = jParser.makeHttpRequest(Constants.TAG_SERVER_MAIN_URL + Constants.TAG_SERVER_DELETE_BOOK, "GET", params);

            Log.d("RESPONSE: ", json.toString());

            try {

                final int success = json.getInt(Constants.TAG_SUCCESS);

                if (success == 1) {

                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserBookDeatailsActivity.this,message,Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });


                } else {
                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UserBookDeatailsActivity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    });

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
