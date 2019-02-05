package me.nuwan.seofficial.Model;

import android.content.Context;
import android.widget.Toast;
import java.text.SimpleDateFormat;

public class Common {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat birthdayFormat = new SimpleDateFormat("dd MMMM yyyy");
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
    public static String sortPeopleType;

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
