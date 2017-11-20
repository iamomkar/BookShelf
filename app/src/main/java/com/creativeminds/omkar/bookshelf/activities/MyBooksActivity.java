package com.creativeminds.omkar.bookshelf.activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.creativeminds.omkar.bookshelf.Constants;
import com.creativeminds.omkar.bookshelf.R;
import com.creativeminds.omkar.bookshelf.adapters.BooksAdapter;
import com.creativeminds.omkar.bookshelf.adapters.UserBooksAdapter;
import com.creativeminds.omkar.bookshelf.models.Book;
import com.creativeminds.omkar.bookshelf.utils.JSONParser;
import com.creativeminds.omkar.bookshelf.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyBooksActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private UserBooksAdapter adapter;
    private List<Book> bookList;
    public ArrayList<Book> bookArray = new ArrayList<Book>();
    String message;
    JSONParser jParser = new JSONParser();
    Utils utils = new Utils(MyBooksActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.books_recycler_view_home_id);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipetorefresh_id);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getAllProducts().execute();
            }
        });

        sharedPref = PreferenceManager.getDefaultSharedPreferences(MyBooksActivity.this);
        bookList = new ArrayList<>();
        adapter = new UserBooksAdapter(MyBooksActivity.this, bookList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MyBooksActivity.this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        new getAllProducts().execute();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    class getAllProducts extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            bookList.clear();

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(Constants.TAG_UID, sharedPref.getString(Constants.TAG_UID,"null")));
            JSONObject json = jParser.makeHttpRequest(Constants.TAG_SERVER_MAIN_URL+Constants.TAG_SERVER_GET_BOOKS_BY_UID, "GET", params);

            Log.d("RESPONSE: ", json.toString());

            try {

                final int success = json.getInt(Constants.TAG_SUCCESS);

                if (success == 1) {

                    message = json.getString(Constants.TAG_MESSAGE);
                    JSONArray books = json.getJSONArray(Constants.TAG_BOOKS_ARRAY);

                    for(int i = 0; i<books.length();i++) {
                        JSONObject book = books.getJSONObject(i);
                        final String uid = book.getString(Constants.TAG_UID);
                        final String bid = book.getString(Constants.TAG_BID);
                        final String bname = book.getString(Constants.TAG_BOOK_NAME);
                        final String bauthor = book.getString(Constants.TAG_BOOK_AUTHOR);
                        final String bedition = book.getString(Constants.TAG_BOOK_EDITION);
                        final String city = book.getString(Constants.TAG_USER_CITY);
                        final String isbn = book.getString(Constants.TAG_ISBN);
                        final String bcategory = book.getString(Constants.TAG_BOOK_CATEGORY);
                        final String bmrp = book.getString(Constants.TAG_BOOK_MRP);
                        final String bsp = book.getString(Constants.TAG_BOOK_SELLING_PRICE);
                        final String condition = book.getString(Constants.TAG_BOOK_CONDITION);
                        final String bdesc = book.getString(Constants.TAG_BOOK_DESCRIPTION);
                        final String bdatecreated = book.getString(Constants.TAG_BOOK_DATE_CREATED);
                        final String imageurl = book.getString(Constants.TAG_BOOK_IMAGE_URL);

                        Book b1 = new Book(uid,bid,bname,bauthor,bedition,isbn,bcategory,bmrp,bsp,condition,bdesc,bdatecreated,city,imageurl);

                        bookList.add(b1);

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(MyBooksActivity.this,message,Toast.LENGTH_LONG).show();
                            utils.showMessage(R.id.mybooks_main_layout_id,message, R.color.whatsapp);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });


                } else {
                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            utils.showMessage(R.id.mybooks_main_layout_id,message, R.color.jabong);
                            //Toast.makeText(MyBooksActivity.this,message,Toast.LENGTH_LONG).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(swipeRefreshLayout.isRefreshing()){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

}
