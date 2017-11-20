package com.creativeminds.omkar.bookshelf.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.creativeminds.omkar.bookshelf.R;

/**
 * Created by Omkar on 9/23/2017.
 */

public class Utils {
    private Context mContext;
    private Activity mActivity;


    public Utils(Activity activity){
        this.mActivity = activity;
        this.mContext = activity;
    }

    public boolean isStringEmpty(String str){
        if(str.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    public boolean isValidMobile(String str){
        if(str.length() != 10){
            return false;
        }else if(str.startsWith("7") && str.startsWith("8") && str.startsWith("9")){
            return true;
        }else {
            return false;
        }
    }

    public boolean isValidEmail(String str){
        if(str.contains("@") && str.contains(".")){
            return true;
        }else {
            return false;
        }
    }

    public void showMessage(int view_id,String message,int bg_color){
        Snackbar snackbar = Snackbar.make(mActivity.findViewById(view_id), message , Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(mActivity.getResources().getColor(bg_color));
        snackbar.show();
    }

}
