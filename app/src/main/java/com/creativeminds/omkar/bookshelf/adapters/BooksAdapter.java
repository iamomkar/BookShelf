package com.creativeminds.omkar.bookshelf.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.creativeminds.omkar.bookshelf.Constants;
import com.creativeminds.omkar.bookshelf.activities.DetailsActivity;
import com.creativeminds.omkar.bookshelf.activities.UserBookDeatailsActivity;
import com.creativeminds.omkar.bookshelf.models.Book;

import java.util.List;
import com.creativeminds.omkar.bookshelf.R;


public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> productList;
    private int posi;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, deal_price,orignal_price,author,condition,category;
        public ImageView thumbnail;
        CardView card;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.book_title_id);
            orignal_price =(TextView)view.findViewById(R.id.mrp_tv_id);
            deal_price = (TextView) view.findViewById(R.id.sell_price_tv_id);
            author = (TextView) view.findViewById(R.id.author_tv_id);
            condition =(TextView)view.findViewById(R.id.cond_tv_id);
            category = (TextView) view.findViewById(R.id.cat_tv_id);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            card = (CardView) view.findViewById(R.id.card_view_books_adapter_id);
        }
    }


    public BooksAdapter(Context mContext, List<Book> albumList) {
        this.mContext = mContext;
        this.productList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card_big, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Book book = productList.get(position);
        holder.title.setText(book.getName());
        holder.deal_price.setText(mContext.getString(R.string.rupee_symbol)+book.getSellingPrice());
        holder.orignal_price.setText(mContext.getString(R.string.rupee_symbol)+book.getMRP());
        holder.orignal_price.setPaintFlags(holder.orignal_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.author.setText(book.getAuthor());
        holder.condition.setText(book.getCondition());
        holder.category.setText(book.getCategory());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                Intent intent;
                if(book.getUID().equals(sharedPref.getString(Constants.TAG_UID,null))){
                     intent = new Intent(mContext, UserBookDeatailsActivity.class);
                }else {
                     intent = new Intent(mContext, DetailsActivity.class);
                }
                intent.putExtra(Constants.TAG_BOOK_NAME,book.getName());
                intent.putExtra(Constants.TAG_UID,book.getUID());
                intent.putExtra(Constants.TAG_BID,book.getBID());
                intent.putExtra(Constants.TAG_BOOK_AUTHOR,book.getAuthor());
                intent.putExtra(Constants.TAG_ISBN,book.getIsbn());
                intent.putExtra(Constants.TAG_BOOK_MRP,book.getMRP());
                intent.putExtra(Constants.TAG_BOOK_SELLING_PRICE,book.getSellingPrice());
                intent.putExtra(Constants.TAG_BOOK_CONDITION,book.getCondition());
                intent.putExtra(Constants.TAG_BOOK_CATEGORY,book.getCategory());
                intent.putExtra(Constants.TAG_BOOK_EDITION,book.getEdition());
                intent.putExtra(Constants.TAG_BOOK_DESCRIPTION,book.getDescription());
                intent.putExtra(Constants.TAG_USER_PHONE,"8149371749");
                intent.putExtra(Constants.TAG_USER_EMAIL,"prathamesh@gmail.com");
                intent.putExtra(Constants.TAG_BOOK_IMAGE_URL,book.getImageUrl());
                mContext.startActivity(intent);

            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                Intent intent;
                if(book.getUID().equals(sharedPref.getString(Constants.TAG_UID,null))){
                    intent = new Intent(mContext, UserBookDeatailsActivity.class);
                }else {
                    intent = new Intent(mContext, DetailsActivity.class);
                }
                intent.putExtra(Constants.TAG_BOOK_NAME,book.getName());
                intent.putExtra(Constants.TAG_UID,book.getUID());
                intent.putExtra(Constants.TAG_BID,book.getBID());
                intent.putExtra(Constants.TAG_BOOK_AUTHOR,book.getAuthor());
                intent.putExtra(Constants.TAG_BOOK_MRP,book.getMRP());
                intent.putExtra(Constants.TAG_BOOK_SELLING_PRICE,book.getSellingPrice());
                intent.putExtra(Constants.TAG_BOOK_CONDITION,book.getCondition());
                intent.putExtra(Constants.TAG_BOOK_CATEGORY,book.getCategory());
                intent.putExtra(Constants.TAG_BOOK_EDITION,book.getEdition());
                intent.putExtra(Constants.TAG_BOOK_DESCRIPTION,book.getDescription());
                intent.putExtra(Constants.TAG_USER_PHONE,"8149371749");
                intent.putExtra(Constants.TAG_USER_EMAIL,"prathamesh@gmail.com");
                intent.putExtra(Constants.TAG_BOOK_IMAGE_URL,book.getImageUrl());
                mContext.startActivity(intent);


            }
        });


        Glide.with(mContext).load(book.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .fitCenter()
                .into(holder.thumbnail);


    }


    public void setPosi(int i){
        posi = i;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}