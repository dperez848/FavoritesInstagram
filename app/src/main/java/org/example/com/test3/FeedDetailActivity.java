package org.example.com.test3;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.example.com.test3.models.FeedModel;
import org.example.com.test3.utils.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedDetailActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar) Toolbar mToolbar;
    @Bind(R.id.txt_tags) TextView mTags;
    @Bind(R.id.txt_author) TextView mAuthor;
    @Bind(R.id.txt_date) TextView mDate;
    @Bind(R.id.detail_pager) ViewPager mDetailPager;
    @Bind(R.id.circles) LinearLayout circles;

    private ArrayList<FeedModel> mFeedList;
    private CustomPagerAdapter mAdapter;
    private String mUrl;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);


        ButterKnife.bind(this);

        // Set Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFeedList = bundle.getParcelableArrayList(Constants.KEY_LIST);
            currentPosition = bundle.getInt(Constants.KEY_POSITION);
            if (mFeedList != null) {
                mAdapter = new CustomPagerAdapter(FeedDetailActivity.this, mFeedList);
                mDetailPager.setAdapter(mAdapter);
                setInfo(0);
                mDetailPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        setIndicator(position);
                        setInfo(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                buildCircles();
                mDetailPager.setCurrentItem(currentPosition);
            }
        }
    }

    private void setInfo(int position){
        FeedModel feed = mFeedList.get(position);
        String caption;
        if (feed.getCaption() != null)
            caption = feed.getCaption().getText();
        else
            caption = getString(R.string.no_tittle);

        if(getTags(position).equals(""))
            mTags.setText(R.string.no_tags);
        else
            mTags.setText(getTags(position));

        mDate.setText(feed.getStringDate());
        mAuthor.setText(feed.getUser().getFull_name());
        mUrl = feed.getImages().getStandard_resolution().getUrl();
        getSupportActionBar().setTitle(caption);
    }

    public String getTags(int position){
        StringBuilder bufferTags = new StringBuilder();
        int i;
        int tagSize = mFeedList.get(position).getTags().size();
        for(i = 0; i < tagSize; i++){
            bufferTags.append("#" + mFeedList.get(position).getTags().get(i) + " ");
        }
        return bufferTags.toString();
    }

    private void buildCircles(){
        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (1 * scale + 0.5f);

        for (int i = 0 ; i < mAdapter.getCount() ; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.circle);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }
        setIndicator(currentPosition);
    }

    private void setIndicator(int index){
        if (index < mAdapter.getCount()) {
            for (int i = 0 ; i < mAdapter.getCount() ; i++) {
                ImageView circle = (ImageView) circles.getChildAt(i);
                if (i == index) {
                    circle.setImageResource(R.drawable.circle_selected);
                } else {
                    circle.setImageResource(R.drawable.circle);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_share) {
            startActivity(new Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_comment) + mUrl)
                    .setType("text/plain"));
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //**************************************************
    //                  View Pager
    //**************************************************

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        private ArrayList<FeedModel> mData;

        public CustomPagerAdapter(Context context, ArrayList<FeedModel> mData) {
            mContext = context;
            this.mData = mData;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            FeedModel feed = mData.get(position);
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_image_pager, null);
            ImageView mImageView = ButterKnife.findById(view, R.id.image_pager);
            Glide.with(mContext)
                    .load(feed.getImages().getStandard_resolution().getUrl())
                    .placeholder(R.drawable.default_image)
                    .into(mImageView);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FeedDetailActivity.this, WebViewActivity.class);
                    intent.putExtra(Constants.KEY_WEBVIEW, mFeedList.get(position).getUser().getUsername());
                    startActivity(intent);
                }
            });

            collection.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
