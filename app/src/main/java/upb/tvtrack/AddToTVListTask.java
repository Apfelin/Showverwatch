package upb.tvtrack;

import android.os.AsyncTask;
import android.util.Log;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class AddToTVListTask extends AsyncTask<Integer, Void, TvSeries> {

    public interface asyncAddResponse {

        void addFinish(TvSeries _tv_result);
    }

    public asyncAddResponse delegate;

    public AddToTVListTask(asyncAddResponse _delegate) {

        delegate = _delegate;
    }

    @Override
    protected TvSeries doInBackground(Integer... _params) {

        int id = _params[0];

        TmdbTV tv = new TmdbApi("100493e87e727a4c9f510906380df77d").getTvSeries();
        return tv.getSeries(id, "en");
    }

    @Override
    protected void onPostExecute(TvSeries _tv_result) {

        delegate.addFinish(_tv_result);
    }
}
