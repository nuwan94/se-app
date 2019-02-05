package me.nuwan.seofficial.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import me.nuwan.seofficial.R;
import me.nuwan.seofficial.Model.Rank;


public class RankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private LayoutInflater inflater;
    private List<Rank> data;

    public RankAdapter(Context context, List<Rank> data){
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_rank, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder= (MyHolder) holder;
        Rank current = data.get(position);

        myHolder.number.setText(String.format(Locale.ENGLISH,"%d", position + 1));
        myHolder.name.setText(current.getShortName());
        myHolder.score.setText(String.format(Locale.ENGLISH,"%,.2f", Float.parseFloat(current.getScore())));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView number,name, score;
        MyHolder(final View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.rankNumber);
            name = itemView.findViewById(R.id.rankName);
            score = itemView.findViewById(R.id.rankScore);

        }

    }
}
