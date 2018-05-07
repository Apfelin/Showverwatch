package upb.tvtrack;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class AddTVActivity extends AppCompatActivity implements TVSearchTask.asyncSearchResponse {

    private List<TvSeries> search_shows = new ArrayList<>();
    private RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tv);

        // add toolbar to activity
        Toolbar toolbar = findViewById(R.id.addtv_toolbar);
        setSupportActionBar(toolbar);

        // set a back button on the toolbar, to return to TVList activity
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        RecyclerView rv_search = findViewById(R.id.rv_searchresult);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_search.setLayoutManager(llm);

        adapter = new RVAdapter(search_shows, new RecyclerViewClickListener() {

            @Override
            public void onClick(View view, int i) {

                Intent returnIntent = new Intent();

                if (adapter.isEmptyTVShow()) {

                    setResult(RESULT_CANCELED);
                } else {

                    returnIntent.putExtra("tvid", adapter.getIdByIndex(i));
                    setResult(RESULT_OK, returnIntent);
                }
                finish();
            }
        });
        rv_search.setAdapter(adapter);

        handleSearchIntent(getIntent());

        if(search_shows.isEmpty()) {

            TvSeries emptyList = new TvSeries();
            emptyList.setName("Search for TV shows!");
            emptyList.setOverview("Use the search button above to search for your favourite TV shows.");

            search_shows.add(emptyList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // place the search button on the toolbar
        getMenuInflater().inflate(R.menu.addtv_menu, menu);

        // get the search view
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // change the text color for the search view
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));

        // current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

        setIntent(intent);
        handleSearchIntent(intent);
    }

    private void handleSearchIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
            TVSearchTask tvst = new TVSearchTask(this, this);
            tvst.execute(query);
        }
    }

    @Override
    public void searchFinish(List<TvSeries> _result_list) {

        if (_result_list != null) {

            adapter.setData(_result_list);
        } else {

            Toast.makeText(this,"No TV show found!", Toast.LENGTH_SHORT).show();
        }
    }
}
