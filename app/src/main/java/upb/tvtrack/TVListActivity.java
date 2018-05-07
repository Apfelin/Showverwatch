package upb.tvtrack;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.tv.TvSeries;

public class TVListActivity extends AppCompatActivity implements AddToTVListTask.asyncAddResponse {

    private static final int ADD_REQUEST_CODE = 0;

    private List<TvSeries> tvshows = new ArrayList<>();
    private RVAdapter adapter;

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

                FragmentManager fm = getFragmentManager();
                TVDetails tvDeets = TVDetails.newInstance(adapter.getIdByIndex(i));
                tvDeets.show(fm, "tv_details");
            }
        });
        rv.setAdapter(adapter);

        SharedPreferences shPref = getPreferences(Context.MODE_PRIVATE);
        String jStr = shPref.getString("json_shows_id", null);

        if (jStr != null) {

            try {

                JSONArray jArr = new JSONArray(jStr);
                JSONObject json = null;
                for (int i = 0; i < jArr.length(); i++) {

                    json = (JSONObject) jArr.get(i);
                    AddToTVListTask attvlt = new AddToTVListTask(this);
                    attvlt.execute(json.getInt("id"));
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tvlist_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_sortname:

                adapter.sortName();
                return true;

            case R.id.action_sortvoteavg:

                adapter.sortVoteAvg();
                return true;

            case R.id.action_sortlatest:

                adapter.sortLastAir();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPause() {

        super.onPause();

        if (!adapter.isEmpty()) {

            JSONArray jArr = new JSONArray();

            for (int i = 0; i < adapter.getItemCount(); i++) {

                JSONObject val = makeJSONObject(adapter.getTvSeriesByIndex(i));
                jArr.put(val);
            }

            SharedPreferences shPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor shPrefEdit = shPref.edit();
            shPrefEdit.putString("json_shows_id", jArr.toString());
            shPrefEdit.apply();

            // remove all shared preferences - DEBUG OPTION
//            shPrefEdit.remove("json_shows_id").apply();
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    public void addPress(View view) {

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
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return tv;
    }
}
