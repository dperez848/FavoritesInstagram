package org.example.com.test3.models;

import org.example.com.test3.network.FeedHttpService;
import org.example.com.test3.network.HttpClientGenerator;
import org.example.com.test3.utils.Constants;

import java.util.List;

import retrofit2.Callback;

public class ResponseModel {
    List<FeedModel> data;

    public List<FeedModel> getmFeedList() {
        return data;
    }

    public void setmFeedList(List<FeedModel> mFeedList) {
        this.data = mFeedList;
    }

    public static HttpClient getHttpClient() {
        return new HttpClient();
    }

    public static class HttpClient {

        private FeedHttpService mFeedHttpService;

        public HttpClient() {
            mFeedHttpService = HttpClientGenerator.createClient(FeedHttpService.class, Constants.URL_BASE);
        }

        public void getAll(Callback<ResponseModel> callback) {
            mFeedHttpService
                    .getAll(Constants.CODE)
                    .enqueue(callback);
        }

    }
}
