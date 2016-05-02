package org.example.com.test3;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.example.com.test3.adapters.FeedListAdapter;
import org.example.com.test3.app.App;
import org.example.com.test3.models.FeedModel;
import org.example.com.test3.models.ResponseModel;
import org.example.com.test3.utils.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FeedListAdapter.OnItemClickListener{

    private static final String TAG = "Main";
    @State  ArrayList<FeedModel> mFeedModel = new ArrayList<FeedModel>();
    private FeedListAdapter mRecyclerAdapter;
    private CustomPagerAdapter mPagerAdapter;
    private GridLayoutManager mLayout;
    // Butterknife
    @Bind(R.id.feed_pager)  ViewPager mFeedPager;
    @Bind(R.id.feed_recycler) RecyclerView mRecycler;
    @Bind(R.id.tool_bar) Toolbar mToolbar;
    @Bind(R.id.progress) ProgressBar progressBar;
    @Bind(R.id.swipe_container) SwipeRefreshLayout mSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Butterknife
        ButterKnife.bind(this);

        // Set Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        // Recycler Adapter
        mRecyclerAdapter = new FeedListAdapter(MainActivity.this, mFeedModel, MainActivity.this);
        mLayout = new GridLayoutManager(MainActivity.this, 3);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mLayout);
        mRecycler.setAdapter(mRecyclerAdapter);

        // Pager Adapter
        mPagerAdapter = new CustomPagerAdapter(MainActivity.this, mFeedModel);
        mFeedPager.setAdapter(mPagerAdapter);

        mFeedPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mRecyclerAdapter.selectImage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if(savedInstanceState == null) {
            getFeed();
        }else{
            progressBar.setVisibility(View.INVISIBLE);
        }
        mSwipe.setDistanceToTriggerSync(120);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeed();
            }
        });

    }

    @Override
    public void onItemClick(int i) {
        mFeedPager.setCurrentItem(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            progressBar.setVisibility(View.VISIBLE);
            getFeed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getFeed() {

        if(App.isNetworkAvailable()){
            ResponseModel.getHttpClient().getAll(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccess()) {
                        ResponseModel mResponseModel = response.body();
                        mFeedModel.clear();

                        mFeedModel.addAll(mResponseModel.getmFeedList());

                        mRecyclerAdapter.notifyChange();
                        mPagerAdapter.notifyDataSetChanged();
                        mFeedPager.setCurrentItem(0);
                        progressBar.setVisibility(View.INVISIBLE);
                        mSwipe.setRefreshing(false);
                    } else {
                        Log.d(TAG, "Codigo: " + response.code());
                        Log.d(TAG, "Response: " + response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    mSwipe.setRefreshing(false);
                }
            });
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_image_pager, null);
            ImageView mImageView = ButterKnife.findById(view, R.id.image_pager);

            Glide.with(mContext)
                    .load(mData.get(position).getImages().getLow_resolution().getUrl())
                    .placeholder(R.drawable.default_image)
                    .into(mImageView);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, FeedDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.KEY_LIST, mData);
                    bundle.putInt(Constants.KEY_POSITION,position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            collection.addView(view);
            return view;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
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
