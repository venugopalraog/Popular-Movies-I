package udacity.com.popularmovies.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.popularmovies.R;
import udacity.com.popularmovies.common.Constants;
import udacity.com.popularmovies.model.TrailerInfo;

/**
 * Created by gubbave on 10/5/2016.
 */
public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    private List<TrailerInfo> trailerInfoList;
    private OnItemClickListener mListener;
    private Context mContext;

    public TrailerListAdapter(Activity activity) {
        mContext = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerListAdapter.ViewHolder holder, int position) {
        holder.bindData(getItem(position), position, mListener);
    }

    @Override
    public int getItemCount() {
        if (trailerInfoList != null)
            return trailerInfoList.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.trailer_list_item;
    }

    public TrailerInfo getItem(int position) {
        return trailerInfoList.get(position);
    }

    public void setTrailerInfoList(List<TrailerInfo> trailerInfoList) {
        this.trailerInfoList = trailerInfoList;
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.trailer_list_item_title) TextView trailerTitle;
        @BindView(R.id.trailer_list_item_trailerPoster) ImageView trailerPoster;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(TrailerInfo trailerInfo, final int position, final OnItemClickListener listener) {

            Picasso.with(mContext).load(Constants.YOUTUBE_IMAGE_URL + trailerInfo.getKey() + Constants.YOUTUBE_THUMB)
                    .into(trailerPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onItemClick(view, position);
                }
            });
        }
    }


}
