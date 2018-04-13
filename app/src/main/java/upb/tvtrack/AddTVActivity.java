package upb.tvtrack;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class AddTVActivity extends AppCompatActivity {

    // https://developer.android.com/guide/topics/ui/dialogs.html#ProgressDialog
    // https://developer.android.com/guide/topics/search/search-dialog.html

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // place the search button on the toolbar
        getMenuInflater().inflate(R.menu.addtv_menu, menu);

        // get the search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //change the text color for the search view
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));

        //

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
}
