package upb.tvtrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

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

                Toast.makeText(view.getContext(), "Position " + i, Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);

        if(tvshows.isEmpty()) {

            TvSeries empty = new TvSeries();
            empty.setName("No TV shows added!");
            empty.setOverview("Add a TV show to see it here.");

            tvshows.add(empty);
        }
    }

    public void addPress(View view) {

        Intent intent = new Intent(this, AddTVActivity.class);
        startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _intent) {

        super.onActivityResult(_requestCode, _resultCode, _intent);

        if (_requestCode == ADD_REQUEST_CODE && _resultCode == RESULT_OK) {

            int tvid = _intent.getIntExtra("tvid", -1);
            AddToTVListTask attvlt = new AddToTVListTask(this);
            attvlt.execute(tvid);
        }
    }

    @Override
    public void addFinish(TvSeries _tv_result) {

        adapter.addData(_tv_result);
    }
}
