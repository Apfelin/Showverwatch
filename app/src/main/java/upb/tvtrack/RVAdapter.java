package upb.tvtrack;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.movito.themoviedbapi.model.tv.TvSeries;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TVShowViewHolder> {

    private List<TvSeries> tvshows;
    private RecyclerViewClickListener listener;

    public static class TVShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView showName;
        TextView showDesc;
        ImageView showImage;
        RecyclerViewClickListener listener;

        TVShowViewHolder(View _itemView, RecyclerViewClickListener _listener) {

            super(_itemView);

            cv = _itemView.findViewById(R.id.cv);
            showName = _itemView.findViewById(R.id.show_name);
            showDesc = _itemView.findViewById(R.id.show_description);
            showImage = _itemView.findViewById(R.id.show_image);

            listener = _listener;
            _itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View _itemView) {

            listener.onClick(_itemView, getAdapterPosition());
        }
    }

    RVAdapter(List<TvSeries> _tvshows, RecyclerViewClickListener _listener) {

        tvshows = _tvshows;
        listener = _listener;
    }

    public void setData(List<TvSeries> _tvshows) {

        tvshows.clear();
        tvshows.addAll(_tvshows);
        notifyDataSetChanged();
    }

    public void addData(TvSeries _tvshow) {

        if (isEmpty()) {

            tvshows.clear();
        }

        tvshows.add(_tvshow);
        notifyDataSetChanged();
    }

    public int getIdByIndex(int _i) {

        return tvshows.get(_i).getId();
    }

    public TvSeries getTvSeriesByIndex(int _i) {

        return tvshows.get(_i);
    }

    public boolean isEmpty() {

        return tvshows.isEmpty();
    }

    public boolean isEmptyTVShow() {

        TvSeries emptyList = new TvSeries();
        emptyList.setName("Search for TV shows!");
        emptyList.setOverview("Use the search button above to search for your favourite TV shows.");

        if (tvshows.get(0).equals(emptyList)) {

            return true;
        }

        return false;
    }

    public void sortName() {

        TvSeries tmp;

        for (int i = 0; i < tvshows.size()-1; i++) {

            if (tvshows.get(i).getName().compareToIgnoreCase(tvshows.get(i+1).getName()) > 0) {

                tmp = tvshows.get(i);
                tvshows.set(i, tvshows.get(i+1));
                tvshows.set(i+1, tmp);
            }
        }

        notifyDataSetChanged();
    }

    public void sortVoteAvg() {

        TvSeries tmp;

        for (int i = 0; i < tvshows.size()-1; i++) {

            if (tvshows.get(i).getVoteAverage() < tvshows.get(i+1).getVoteAverage()) {

                tmp = tvshows.get(i);
                tvshows.set(i, tvshows.get(i+1));
                tvshows.set(i+1, tmp);
            }
        }

        notifyDataSetChanged();
    }

    public void sortLastAir() {

        TvSeries tmp;
        String last_air1;
        String last_air2;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < tvshows.size()-1; i++) {

            last_air1 = tvshows.get(i).getLastAirDate();
            last_air2 = tvshows.get(i+1).getLastAirDate();

            try {

                Date date1 = format.parse(last_air1);
                Date date2 = format.parse(last_air2);

                if (date1.getTime() > date2.getTime()) {

                    tmp = tvshows.get(i);
                    tvshows.set(i, tvshows.get(i+1));
                    tvshows.set(i+1, tmp);
                }
            } catch (ParseException e) {

                e.printStackTrace();
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView _recyclerView) {

        super.onAttachedToRecyclerView(_recyclerView);
    }

    @Override
    public TVShowViewHolder onCreateViewHolder(ViewGroup _viewGroup, int _i) {

        View v = LayoutInflater.from(_viewGroup.getContext()).inflate(R.layout.layout_cardview, _viewGroup, false);
        TVShowViewHolder tvsvh = new TVShowViewHolder(v, listener);
        return tvsvh;
    }

    @Override
    public void onBindViewHolder(final TVShowViewHolder _tvShowViewHolder, int _i) {

        _tvShowViewHolder.showName.setText(tvshows.get(_i).getName());
        _tvShowViewHolder.showDesc.setText(tvshows.get(_i).getOverview());
        ImageViewTask ivt = new ImageViewTask(_tvShowViewHolder.showImage);
        ivt.execute("https://image.tmdb.org/t/p/w342/" + tvshows.get(_i).getPosterPath());
    }

    @Override
    public int getItemCount() {

        return tvshows.size();
    }
}
