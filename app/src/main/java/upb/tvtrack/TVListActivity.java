package upb.tvtrack;

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

public class TVListActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<TvSeries> tvshows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvlist);

        Toolbar toolbar = findViewById(R.id.tvlist_toolbar);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.rv_shows);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(tvshows, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int i) {

                Toast.makeText(view.getContext(), "Position " + i, Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);

        if(tvshows.isEmpty()) {

            initializeIfNoShow();
        }
    }

    public void addPress(View view) {

        Intent intent = new Intent(this, AddTVActivity.class);
        startActivity(intent);
    }

    private void initializeIfNoShow(){

        TvSeries emptyList = new TvSeries();
        emptyList.setName("No TV shows added!");
        emptyList.setOverview("Add a TV show to see it here.");

        tvshows.add(emptyList);
    }
}
