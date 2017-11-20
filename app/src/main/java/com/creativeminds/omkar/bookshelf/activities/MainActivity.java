package com.creativeminds.omkar.bookshelf.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.creativeminds.omkar.bookshelf.Constants;
import com.creativeminds.omkar.bookshelf.R;
import com.creativeminds.omkar.bookshelf.adapters.BooksAdapter;
import com.creativeminds.omkar.bookshelf.models.Book;
import com.creativeminds.omkar.bookshelf.utils.JSONParser;
import com.creativeminds.omkar.bookshelf.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPref;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private BooksAdapter adapter;
    private List<Book> bookList;
    SearchView searchView;
    SearchManager searchManager;
    public ArrayList<Book> bookArray = new ArrayList<Book>();
    String message;
    JSONParser jParser = new JSONParser();
    Utils utils = new Utils(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.books_recycler_view_home_id);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipetorefresh_id);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getAllProducts().execute();
            }
        });

        sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        bookList = new ArrayList<>();
        adapter = new BooksAdapter(MainActivity.this, bookList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        new getAllProducts().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddNewActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        //getMenuInflater().inflate(R.menu.main, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_btn).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Books..");
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(MainActivity.this,SearchResultsActivity.class);
                searchIntent.putExtra(Constants.TAG_BOOK_SEARCH_QUERY,searchView.getQuery().toString());
                startActivity(searchIntent);
                //searchView.setIconified(true);
                //menu.findItem(R.id.search_btn).collapseActionView();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_btn) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile_id) {
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        } else if(id == R.id.add_new_id){
            startActivity(new Intent(MainActivity.this,AddNewActivity.class));
        } else if (id == R.id.my_books_id) {
            startActivity(new Intent(MainActivity.this,MyBooksActivity.class));
        } else if (id == R.id.logout_id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirm ?");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sharedPref.edit().putBoolean(Constants.TAG_LOGGED_IN,false).apply();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("NO",null);
            AlertDialog alert = builder.create();
            alert.show();


        } else if (id == R.id.share_id) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Download BookShelf now For Buying and Selling Books Online.";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Book Shelf :");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.about_id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("About");
            builder.setMessage("Developed by:- \n\nOmkar Shinde\nMalhar Shinde\nShubham Gaikwad");
            builder.setPositiveButton("OK",null);
            builder.create();
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SortBasedOnDate implements Comparator {
        public int compare(Object o1, Object o2)
        {

            Book dd1 = (Book) o1;// where FBFriends_Obj is your object class
            Book dd2 = (Book) o2;
            String i = dd1.getCreatedDate().replace("/","");
            String j = dd2.getCreatedDate().replace("/","");
            return j.compareToIgnoreCase(i);//where uname is field name
        }

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
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
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
            params.add(new BasicNameValuePair(Constants.TAG_USER_CITY, sharedPref.getString(Constants.TAG_USER_CITY,"null")));
            JSONObject json = jParser.makeHttpRequest(Constants.TAG_SERVER_MAIN_URL+Constants.TAG_SERVER_GET_ALL_BOOKS_BY_CITY, "GET", params);

            Log.d("RESPONSE: ", json.toString());

            try {

                final int success = json.getInt(Constants.TAG_SUCCESS);

                if (success == 1) {

                    message = json.getString(Constants.TAG_MESSAGE);
                    JSONArray books = json.getJSONArray(Constants.TAG_BOOKS_ARRAY);

                    for(int i = 0; i<books.length();i++) {
                        JSONObject book = books.getJSONObject(i);
                        final String uid = book.getString(Constants.TAG_UID);
                        final String bid = book.getString(Constants.TAG_BOOK_NAME);
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
                            utils.showMessage(R.id.main_relative_id,message, R.color.whatsapp);
                           // Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });


                } else {
                    message = json.getString(Constants.TAG_MESSAGE);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            utils.showMessage(R.id.main_relative_id,message,  R.color.jabong);
                            //Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
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
