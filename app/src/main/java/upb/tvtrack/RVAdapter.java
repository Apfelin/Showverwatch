package upb.tvtrack;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TVShowViewHolder> {

    List<Show> tvshows;

    public static class TVShowViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView showName;
        TextView showDesc;
        ImageView showImage;

        TVShowViewHolder(View itemView) {

            super(itemView);

            cv = itemView.findViewById(R.id.cv);
            showName = itemView.findViewById(R.id.show_name);
            showDesc = itemView.findViewById(R.id.show_description);
            showImage = itemView.findViewById(R.id.show_image);
        }
    }

    RVAdapter(List<Show> tvshows) {

        this.tvshows = tvshows;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TVShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cardview, viewGroup, false);
        TVShowViewHolder tvsvh = new TVShowViewHolder(v);
        return tvsvh;
    }

    @Override
    public void onBindViewHolder(TVShowViewHolder tvShowViewHolder, int i) {

        tvShowViewHolder.showName.setText(tvshows.get(i).title);
        tvShowViewHolder.showDesc.setText(tvshows.get(i).description);
        tvShowViewHolder.showImage.setImageResource(tvshows.get(i).photoID);
    }

    @Override
    public int getItemCount() {

        return tvshows.size();
    }
}
