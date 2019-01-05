package me.nuwan.seofficial.UI.MainFragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.nuwan.seofficial.Adapters.PeopleAdapter;
import me.nuwan.seofficial.Model.Person;
import me.nuwan.seofficial.R;
import me.nuwan.seofficial.UI.SettingsActivity;

public class RankFragment extends Fragment {

    RecyclerView rv;
    public List<Person> data;

    public RankFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rank, container, false);

        rv = v.findViewById(R.id.peopleList);
        data = new ArrayList<>();

        return v;
    }

    @Override
    public void onStart() {
        fetchRanks();
        super.onStart();
    }

    private void fetchRanks() {
        String userIds = "3125964;7469625;7992142;9274132;8540694;8030256;8520448;8538605;8559510;8235644";
        int n = userIds.split("").length;
        String url = "https://api.stackexchange.com/2.2/users/"+ userIds + "?order=desc&sort=reputation&site=stackoverflow";

        new JsonTask().execute(url);

//        PeopleAdapter adapter = new PeopleAdapter(getActivity(), data);
//        rv.setAdapter(adapter);
//        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);
                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
        }
    }

}


