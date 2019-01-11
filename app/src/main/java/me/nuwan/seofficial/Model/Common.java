package me.nuwan.seofficial.Model;

import android.app.Activity;
import android.content.ContextWrapper;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class Common {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");

    public static void showToast(Activity a, String msg) {
        Toast.makeText(a.getApplicationContext(), msg, Toast.LENGTH_SHORT)
                .show();
    }


}
