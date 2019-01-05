package me.nuwan.seofficial.Adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.nuwan.seofficial.Model.Feed;
import me.nuwan.seofficial.R;

import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private LayoutInflater inflater;
    private List<Feed> data;

    public FeedAdapter(Context context, List<Feed> data){
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_feed, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder= (MyHolder) holder;
        Feed current = data.get(position);

        myHolder.feedTitle.setText(current.getFeedTitle());
        myHolder.feedAuthor.setText(current.getFeedAuthor());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myHolder.feedDesc.setText(Html.fromHtml(current.getFeedDesc(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            myHolder.feedDesc.setText(Html.fromHtml(current.getFeedDesc()));
        }

        try {
            myHolder.feedDesc.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {

        }

//        myHolder.feedDesc.setText(current.getFeedDesc());
        myHolder.feedImage.setImageResource(current.getFeedImg());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView feedTitle, feedDesc,feedAuthor;
//        CircleImageView feedImage;
        ImageView feedImage;
        MyHolder(final View itemView) {
            super(itemView);
            feedTitle = itemView.findViewById(R.id.feedTitle);
            feedDesc = itemView.findViewById(R.id.feedDesc);
            feedAuthor = itemView.findViewById(R.id.feedAuthor);
            feedImage = itemView.findViewById(R.id.feedImage);
        }

    }
}
