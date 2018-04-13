package upb.tvtrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TVListActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<Show> tvshows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvlist);

        Toolbar toolbar = findViewById(R.id.tvlist_toolbar);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.rv_shows);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(tvshows);
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

        tvshows.add(new Show("No TV show added", "Add a show!", R.drawable.ic_add_to_queue_black_48dp));
    }
}
