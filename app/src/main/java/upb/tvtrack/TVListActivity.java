package upb.tvtrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.tv.TvSeries;

public class TVListActivity extends AppCompatActivity implements AddToTVListTask.asyncAddResponse {

    //TODO: sortare, notificare

    private static final int ADD_REQUEST_CODE = 0;

    private List<TvSeries> tvshows = new ArrayList<>();
    private RVAdapter adapter;
    JSONArray jArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvlist);

        Toolbar toolbar = findViewById(R.id.tvlist_toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = findViewById(R.id.rv_shows);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new RVAdapter(tvshows, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int i) {

                Toast.makeText(view.getContext(), "Position " + i, Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);

        SharedPreferences shPref = getPreferences(Context.MODE_PRIVATE);
        String jStr = shPref.getString("json", null);

        if (jStr == null) {

            TvSeries empty = new TvSeries();
            empty.setName("No TV shows added!");
            empty.setOverview("Add a TV show to see it here.");

            tvshows.add(empty);
            adapter.setData(tvshows);
        } else {

            try {

                jArr = new JSONArray(jStr);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {

        super.onPause();

        for (int i = 0; i < tvshows.size(); i++) {

            jArr.put(makeJSONObject(tvshows.get(i)));
        }

        SharedPreferences shPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor shPrefEdit = shPref.edit();
        shPrefEdit.putString("json", jArr.toString());
        shPrefEdit.apply();
    }

    @Override
    public void onResume() {

        super.onResume();

        SharedPreferences shPref = getPreferences(Context.MODE_PRIVATE);
        String jStr = shPref.getString("json", null);

        if (jStr == null) {

            TvSeries empty = new TvSeries();
            empty.setName("No TV shows added!");
            empty.setOverview("Add a TV show to see it here.");

            tvshows.add(empty);
            adapter.setData(tvshows);
        } else {

            try {

                jArr = new JSONArray(jStr);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    public void addPress() {

        Intent intent = new Intent(this, AddTVActivity.class);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _intent) {

        super.onActivityResult(_requestCode, _resultCode, _intent);

        if (_requestCode == ADD_REQUEST_CODE) {

            if (_resultCode == RESULT_OK) {

                int tvid = _intent.getIntExtra("tvid", -1);
                AddToTVListTask attvlt = new AddToTVListTask(this);
                attvlt.execute(tvid);
            } else if (_resultCode == RESULT_CANCELED) {

                Toast.makeText(this,"No TV show added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void addFinish(TvSeries _tv_result) {

        adapter.addData(_tv_result);
    }

    public JSONObject makeJSONObject(TvSeries _tv) {

        JSONObject tv = new JSONObject();

        try {

            tv.put("id", _tv.getId());
            tv.put("name", _tv.getName());
            tv.put("overview", _tv.getOverview());
            tv.put("poster_path", _tv.getPosterPath());
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return tv;
    }
}
