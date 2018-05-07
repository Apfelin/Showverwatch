package upb.tvtrack;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbAuthentication;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.model.config.TokenSession;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class TVDetails extends DialogFragment implements AddToTVListTask.asyncAddResponse {

    private TvSeries tv;

    // use new instance to send arguments, not the constructor
    static TVDetails newInstance(int _id) {

        TVDetails f = new TVDetails();

        // supply input args to instance
        Bundle args = new Bundle();
        args.putInt("id", _id);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        int id = getArguments().getInt("id");
        AddToTVListTask attvlt = new AddToTVListTask(this);
        attvlt.execute(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {

        // inflate the dialog view
        View v = inflater.inflate(R.layout.activity_tv_details, container, false);

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {

        super.onStart();

        Dialog dialog = getDialog();

        if (dialog != null) {

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    // delegate for the add to tv list task
    @Override
    public void addFinish(TvSeries _tv_result) {

        // get the result of the add to tv list async task
        tv = _tv_result;

        // get and set the backdrop image
        ImageView backdrop = getView().findViewById(R.id.details_backdrop);
        ImageViewTask ivt = new ImageViewTask(backdrop);
        ivt.execute("https://image.tmdb.org/t/p/w1280/" + tv.getBackdropPath());

        // get and set the name of the show
        TextView name = getView().findViewById(R.id.details_name);
        name.setText(tv.getName());

        // show the current season
        TextView seasons = getView().findViewById(R.id.details_seasons);
        seasons.setText("Season " + tv.getSeasons().size());

        // show last air date
        TextView lastaired = getView().findViewById(R.id.details_lastaired);
        Log.d("mytag", "last air is - " + tv.getLastAirDate());
        lastaired.setText("Last aired on " + tv.getLastAirDate());

        // show genres the show can be classified as (two is enough)
        TextView genres = getView().findViewById(R.id.details_genre);
        genres.setText(tv.getGenres().get(0) + ", " + tv.getGenres().get(1));

        // show user rating and number of votes
        TextView voteavg = getView().findViewById(R.id.details_voteavg);
        voteavg.setText(tv.getVoteAverage() + " out of 10");

        // show the tv show's overview/description
        TextView overview = getView().findViewById(R.id.details_overview);
        overview.setText(tv.getOverview());

        // add a listener for the close button
        ImageButton closeBtn = getView().findViewById(R.id.details_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });

        // add a listener for the favorite button - send a notification
        ImageButton favBtn = getView().findViewById(R.id.details_favorite);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // build the notification
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                        .setContentTitle("Showverwatch")
                        .setContentText(tv.getName() + " has been added to your favourites!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(tv.getName() + " has been added to your favourites!\n" +
                                        "You will be notified when a new episode comes out."))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                // show the notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                notificationManager.notify(1, mBuilder.build());

            }
        });

        // add listener to rating bar
        RatingBar rbar = getView().findViewById(R.id.details_ratingBar);
        rbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // multiply by 2, since with 5 stars, rating bar only goes up to 5 max
                final float rated = rating * 2;

                // network ops must be run on separate thread/in async task
                Log.d("mytag", "sending rating...");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // check if a guest id session token already exists
                        TokenSession tksesh = null;
                        SharedPreferences shPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        String tmdbToken = shPref.getString("tmdb_token", null);

                        if (tmdbToken == null) {

                            // if a token doesn't exist, create one and save it as json with relevant data to shared prefs
                            Log.d("mytag", "tmdb_toke null");
                            TmdbAuthentication auth = new TmdbApi("100493e87e727a4c9f510906380df77d").getAuthentication();
                            tksesh = auth.getGuestSessionToken();
                            JSONObject jsonToken = makeJSONObject(tksesh);
                            SharedPreferences.Editor shPrefEdit = shPref.edit();
                            shPrefEdit.putString("tmdb_token", jsonToken.toString());
                            shPrefEdit.apply();

                            // create a session json object, which will be sent to the tmdb server
                            JSONObject jsonSesh = new JSONObject();
                            try {

                                jsonSesh.put("id", tksesh.getGuestSessionId());
                                jsonSesh.put("expire_date", tksesh.getExpiresAt());
                                jsonSesh.put("rating", rated);
                                sendPostRating(jsonSesh);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        } else {

                            // if it already exists, just get it from shared prefs
                            Log.d("mytag", "tmdb_token not null");

                            try {

                                // create the json session token
                                JSONObject jsonSesh = new JSONObject(tmdbToken);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String expire_date = jsonSesh.getString("expire_date");
                                expire_date = expire_date.substring(0, expire_date.length()-4);
                                Date date = format.parse(expire_date);

                                // check if the token has expired, and if so, create another
                                if (System.currentTimeMillis() > date.getTime()) {

                                    Log.d("mytag", "tmdb_token expired");

                                    TmdbAuthentication auth = new TmdbApi("100493e87e727a4c9f510906380df77d").getAuthentication();
                                    tksesh = auth.getGuestSessionToken();
                                    JSONObject jsonToken = makeJSONObject(tksesh);
                                    SharedPreferences.Editor shPrefEdit = shPref.edit();
                                    shPrefEdit.putString("tmdb_token", jsonToken.toString());
                                    shPrefEdit.apply();
                                }

                                Log.d("mytag", "tmdb_token not expired");
                                jsonSesh.put("rating", rated);
                                sendPostRating(jsonSesh);
                            } catch (JSONException e) {

                                e.printStackTrace();
                            } catch (ParseException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                });

                // create the thread and start it, but only display the toast AFTER it is finished
                thread.start();
                try {

                    thread.join();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Rating sent!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // send the rating to the tmdb server thru POST
    public void sendPostRating(final JSONObject _jsonSesh) {

        Log.d("mytag", "in sendpostrating");

        try {

            // create url and set method and properties
            URL url = new URL("https://api.themoviedb.org/3/tv/" + tv.getId() +
                "/rating?api_key=100493e87e727a4c9f510906380df77d&guest_session_id=" +
                _jsonSesh.getString("id"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // get the rating from the json session object and create a request object
            float value = Float.valueOf(_jsonSesh.getString("rating"));
            JSONObject reqBody = new JSONObject();
            reqBody.put("value", value);

            Log.d("mytag", "value - " + value);

            // create an output stream and send the request
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(reqBody.toString());

            // close the stream and disconnect
            os.flush();
            os.close();
            conn.disconnect();
        } catch (Exception e) {

            e.printStackTrace();
        }

        Log.d("mytag", "finished thread in sendpostrating");
    }

    // helper function for creating a json object
    public JSONObject makeJSONObject(TokenSession _tksesh) {

        JSONObject jsonSesh = new JSONObject();

        try {

            jsonSesh.put("id", _tksesh.getGuestSessionId());
            jsonSesh.put("expire_date", _tksesh.getExpiresAt());
        } catch (JSONException e) {

            e.printStackTrace();
        }

        return jsonSesh;
    }
}
