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
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class AddTVActivity extends AppCompatActivity implements TVSearchTask.asyncSearchResponse {

    // https://developer.android.com/guide/topics/ui/dialogs.html#ProgressDialog
    private RecyclerView rv_search;
    private List<TvSeries> search_shows = new ArrayList<>();


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

        rv_search = findViewById(R.id.rv_searchresult);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_search.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(search_shows);
        rv_search.setAdapter(adapter);

        handleSearchIntent(getIntent());

        if(search_shows.isEmpty()) {

            initializeIfNoShow();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // place the search button on the toolbar
        getMenuInflater().inflate(R.menu.addtv_menu, menu);

        // get the search view
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        //change the text color for the search view
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);

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
            TVSearchTask tvst = new TVSearchTask(this);
            tvst.execute(query);
        }
    }

    @Override
    public void searchFinish(List<TvSeries> _result_list) {

        search_shows.clear();
        search_shows.addAll(_result_list);
    }

    private void initializeIfNoShow() {

        TvSeries emptyList = new TvSeries();
        emptyList.setName("Search for TV shows!");
        emptyList.setOverview("Use the search button above to search for your favourite TV shows.");

        search_shows.add(emptyList);
    }
}
