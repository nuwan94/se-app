package me.nuwan.seofficial.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import me.nuwan.seofficial.Model.Person;
import me.nuwan.seofficial.R;


public class PeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<Person> data;
    private Context mContext;

    public PeopleAdapter(Context context, List<Person> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_people, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        final Person current = data.get(position);

        myHolder.sno.setText(current.getSno());
        myHolder.name.setText(current.getName());
        myHolder.mobile.setText(current.getMob());
        Picasso.get().load(current.getImg()).placeholder(R.drawable.progress_animation).error(R.mipmap.user).into(myHolder.img);

        if(current.getMob().isEmpty()) {
            myHolder.callBtn.setVisibility(View.INVISIBLE);
            myHolder.smsBtn.setVisibility(View.INVISIBLE);
        }else{
            myHolder.callBtn.setVisibility(View.VISIBLE);
            myHolder.smsBtn.setVisibility(View.VISIBLE);
        }

        if(Float.parseFloat(current.getLat())==0 && Float.parseFloat(current.getLang())==0){
            myHolder.locationBtn.setVisibility(View.INVISIBLE);
        }else{
            myHolder.locationBtn.setVisibility(View.VISIBLE);
            myHolder.locationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDirection(current.getLat(),current.getLang(),current.getName());
                }
            });
        }



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

    private void showDirection(String lat, String lang,String name) {
        float latF = Float.parseFloat(lat);
        float langF = Float.parseFloat(lang);
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latF, langF, name.split(" ")[0]+"'s Location");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        mContext.startActivity(intent);
    }

    private void sendSms(String peopleMobileNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_SENDTO);
        dialIntent.setData(Uri.parse("sms:" +peopleMobileNumber));
        mContext.startActivity(dialIntent);
    }

    private void makeCall(String peopleMobileNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" +peopleMobileNumber));
        mContext.startActivity(dialIntent);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, sno, mobile;
        ImageView img, callBtn, smsBtn, locationBtn;

        MyHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.peopleName);
            sno = itemView.findViewById(R.id.peopleSNO);
            mobile = itemView.findViewById(R.id.peopleMobile);
            img = itemView.findViewById(R.id.peopleImage);
            callBtn = itemView.findViewById(R.id.peopleCallBtn);
            smsBtn = itemView.findViewById(R.id.peopleSmsBtn);
            locationBtn = itemView.findViewById(R.id.peopleLocationBtn);
        }

    }
}
