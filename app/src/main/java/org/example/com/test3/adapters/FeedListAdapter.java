package org.example.com.test3.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.example.com.test3.MainActivity;
import org.example.com.test3.R;
import org.example.com.test3.models.FeedModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FeedModel> mFeedList;
    private OnItemClickListener mListener;
    private Context mContext;

    // ViewHolder
    public static class ViewHolderItem extends RecyclerView.ViewHolder {

        @Bind(R.id.feed_image)
        ImageView mImage;
        @Bind(R.id.feed_tittle)
        TextView mTittle;

        public ViewHolderItem(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    // Adapter
    public FeedListAdapter(Context context, List<FeedModel> feedList, MainActivity listener) {
        this.mContext = context;
        this.mFeedList = feedList;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater vi = LayoutInflater.from(parent.getContext());
        View v = vi.inflate(R.layout.view_recycler, parent, false);
        return new ViewHolderItem(v);    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolderItem viewHolderItem = (ViewHolderItem) holder;

        if(mFeedList.get(position).getCaption() != null && !mFeedList.get(position).getCaption().getText().equals("")) {
            viewHolderItem.mTittle.setText(mFeedList.get(position).getCaption().getText());
        }else{
            viewHolderItem.mTittle.setText(R.string.no_tittle);
        }
        Glide.with(mContext).load(mFeedList.get(position).getImages().getThumbnail().getUrl())
                .placeholder(R.drawable.default_image)
                .into(viewHolderItem.mImage);

        if(mFeedList.get(position).isSelected()){
            viewHolderItem.mTittle.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolderItem.mTittle.setBackgroundColor(Color.parseColor("#000000"));
        }
        else{
            viewHolderItem.mTittle.setTextColor(Color.parseColor("#000000"));
            viewHolderItem.mTittle.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        viewHolderItem.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFeedList.size();
    }

    public void selectImage(int pos){
        int i;
        for (i = 0; i < mFeedList.size(); i++){
            if(i == pos){
                mFeedList.get(i).setSelected(true);
            }else{
                mFeedList.get(i).setSelected(false);
            }
        }
        this.notifyDataSetChanged();
    }

    public void notifyChange() {
        mFeedList.get(0).setSelected(true);
        notifyDataSetChanged();
    }


    // Interface for receiving click events from list items
    public interface OnItemClickListener {
        void onItemClick(int i);
    }
}
