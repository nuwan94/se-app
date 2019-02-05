package me.nuwan.seofficial.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.nuwan.seofficial.Model.Person;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;
import me.nuwan.seofficial.UI.People.ProfileActivity;


public class PeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<User> data, list;
    private Context mContext;
//    private int lastPosition = -1;

    public PeopleAdapter(Context context, List<User> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        list = new ArrayList<>();
        list.addAll(data);
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_people, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder = (MyHolder) holder;
        final User current = data.get(position);
        myHolder.itemView.setTag(current.getName().split(" ")[0]);

        myHolder.name.setText(current.ShortName());
        myHolder.mobile.setText(current.getMob());
        Picasso.get().load(current.getImg()).placeholder(R.color.silver).error(R.mipmap.user).into(myHolder.img);

        View.OnClickListener profileView = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("user", current);
                mContext.startActivity(intent);
            }
        };

        myHolder.itemView.setOnClickListener(profileView);
        myHolder.name.setOnClickListener(profileView);
        myHolder.mobile.setOnClickListener(profileView);
        myHolder.img.setOnClickListener(profileView);

        if (current.getMob().isEmpty()) {
            myHolder.callBtn.setVisibility(View.INVISIBLE);
            myHolder.smsBtn.setVisibility(View.INVISIBLE);
        } else {
            myHolder.callBtn.setVisibility(View.VISIBLE);
            myHolder.smsBtn.setVisibility(View.VISIBLE);
            myHolder.smsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendSms(current.getMob());
                }
            });
            myHolder.callBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeCall(current.getMob());
                }
            });
        }

    }

    private void sendSms(String peopleMobileNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_SENDTO);
        dialIntent.setData(Uri.parse("sms:" + peopleMobileNumber));
        mContext.startActivity(dialIntent);
    }

    private void makeCall(String peopleMobileNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + peopleMobileNumber));
        mContext.startActivity(dialIntent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void filter(String text) {
        data.clear();
        if (text.isEmpty()) {
            data.addAll(list);
        } else {
            text = text.toLowerCase();
            for (User item : list) {
                if (item.getName().toLowerCase().contains(text)) {
                    data.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, mobile;
        ImageView img, callBtn, smsBtn;

        MyHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.peopleName);
            mobile = itemView.findViewById(R.id.peopleMobile);
            img = itemView.findViewById(R.id.peopleImage);
            callBtn = itemView.findViewById(R.id.peopleCallBtn);
            smsBtn = itemView.findViewById(R.id.peopleSmsBtn);
        }

    }
}
