package upb.tvtrack;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class TVSearchTask extends AsyncTask<String, Void, List<TvSeries>> {

    public interface asyncSearchResponse {

        void searchFinish(List<TvSeries> _result_list);
    }

    private ProgressDialog prgd;
    public asyncSearchResponse delegate;

    public TVSearchTask(asyncSearchResponse _delegate, Context _context) {

        delegate = _delegate;
        prgd = new ProgressDialog(_context);
    }

    @Override
    protected void onPreExecute() {

        prgd.setMessage("Loading search results...");
        prgd.show();
    }

    @Override
    protected List<TvSeries> doInBackground(String... pParams) {

        String query = pParams[0];

        TmdbSearch search = new TmdbApi("100493e87e727a4c9f510906380df77d").getSearch();
        TvResultsPage resultPage = search.searchTv(query, "en", 1);

        return resultPage.getResults();
    }

    @Override
    protected void onPostExecute(List<TvSeries> _result_list) {

        if (prgd != null && prgd.isShowing()) {

            prgd.dismiss();
        }

        delegate.searchFinish(_result_list);
    }
}
