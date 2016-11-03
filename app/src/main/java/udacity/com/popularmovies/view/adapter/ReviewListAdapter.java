package udacity.com.popularmovies.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.popularmovies.R;
import udacity.com.popularmovies.model.Review;

/**
 * Created by venugopalraog on 10/30/16.
 */
public class ReviewListAdapter  extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private List<Review> reviewInfoList;
    private OnItemClickListener mListener;

    public ReviewListAdapter(Context context) {

    }


    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ReviewListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewListAdapter.ViewHolder holder, int position) {
        holder.bindData(getItem(position), position, mListener);
    }

    @Override
    public int getItemCount() {
        if (reviewInfoList == null) return 0;
        return reviewInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.review_list_item;
    }

    private Review getItem(int position) {
        return reviewInfoList.get(position);
    }

    public void setmListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setReviewInfoList(List<Review> reviewInfoList) {
        this.reviewInfoList = reviewInfoList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_list_item_authorName) TextView authorTxt;
        @BindView(R.id.review_list_item_content)    TextView contentTxt;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Review review, final int position, final OnItemClickListener listener) {
            authorTxt.setText(review.getAuthor());
            contentTxt.setText(review.getContent());

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
