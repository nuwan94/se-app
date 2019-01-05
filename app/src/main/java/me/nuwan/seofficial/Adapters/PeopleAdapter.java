package me.nuwan.seofficial.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.nuwan.seofficial.Model.Person;
import me.nuwan.seofficial.R;


public class PeopleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private LayoutInflater inflater;
    private List<Person> data;

    public PeopleAdapter(Context context, List<Person> data){
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_people, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder= (MyHolder) holder;
        Person current = data.get(position);

        myHolder.sno.setText(current.getPeopleSNO());
        myHolder.name.setText(current.getPeopleName());
        myHolder.mobile.setText(current.getPeopleMobileNumber());
        Picasso.get().load(current.getPeopleImage()).into(myHolder.img);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView name, sno,mobile;
        ImageView img;
        MyHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.peopleName);
            sno = itemView.findViewById(R.id.peopleSNO);
            mobile = itemView.findViewById(R.id.peopleMobile);
            img = itemView.findViewById(R.id.peopleImage);
        }

    }
}
