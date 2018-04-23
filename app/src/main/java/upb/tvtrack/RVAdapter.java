package upb.tvtrack;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;
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
        ivt.execute("http://image.tmdb.org/t/p/w342" + tvshows.get(_i).getPosterPath());
    }

    @Override
    public int getItemCount() {

        return tvshows.size();
    }
}
