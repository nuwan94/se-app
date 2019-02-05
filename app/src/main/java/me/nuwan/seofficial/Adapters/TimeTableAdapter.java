package me.nuwan.seofficial.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Rank;
import me.nuwan.seofficial.Model.TimeTableSubject;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class TimeTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<TimeTableSubject> data;
    private List<Integer> androidColors;

    public TimeTableAdapter(Context context, List<TimeTableSubject> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_time_table, parent, false);
        int[] x = view.getResources().getIntArray(R.array.colors);
        androidColors = new ArrayList<Integer>();
        for (int i : x)
            androidColors.add(i);

        return new TimeTableAdapter.MyHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TimeTableAdapter.MyHolder myHolder = (TimeTableAdapter.MyHolder) holder;
        TimeTableSubject current = data.get(position);
        int randomAndroidColor = androidColors.get(new Random().nextInt(androidColors.size()));

        String code = current.getCode().split(",")[0];
        final String day = current.getCode().split(",")[1];
        final String key = current.getCode().split(",")[2];

        myHolder.code.setBackgroundColor(randomAndroidColor);
        myHolder.code.setText(code);
        myHolder.name.setText(current.getName());
        myHolder.time.setText(String.format("%s - %s", current.getStart(), current.getEnd()));
        myHolder.hall.setText(current.getHall());
        myHolder.lec.setText(current.getLec());
        if ("admin".equals(User.currentUser.getType()))
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View view) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    FirebaseDB.getTimeTableReference().child(day).child(key).removeValue();

                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                    notifyDataSetChanged();
                    return true;
                }
            });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, code, time, hall, lec;

        MyHolder(final View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.ttCode);
            name = itemView.findViewById(R.id.ttName);
            time = itemView.findViewById(R.id.ttTime);
            hall = itemView.findViewById(R.id.ttLectureHall);
            lec = itemView.findViewById(R.id.ttInstructorName);
        }

    }
}
