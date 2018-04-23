package upb.tvtrack;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

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

    private List<TvSeries> result_list = new ArrayList<>();
    private ProgressDialog prgd;
    public asyncSearchResponse delegate;

    public TVSearchTask(asyncSearchResponse _delegate, Context _context) {

        this.delegate = _delegate;
        this.prgd = new ProgressDialog(_context);
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

        for (int i = 0; i < resultPage.getTotalResults(); i++) {

            result_list.add(resultPage.getResults().get(i));
        }

        return result_list;
    }

    @Override
    protected void onPostExecute(List<TvSeries> _result_list) {

        if (prgd.isShowing()) {

            prgd.dismiss();
        }

        delegate.searchFinish(_result_list);
    }
}
