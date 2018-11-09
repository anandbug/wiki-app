package in.nishachar.wiki_app.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WikiApiClient {
    private static final String baseUrl = "https://en.wikipedia.org/";

    public static Retrofit getClient(Context context) {
        Long cacheSize = (long) (5 * 1024 * 1024);
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    if (hasNetwork(context))
                        request = request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build();
                    else
                        request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();

                    return chain.proceed(request);
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    private static Boolean hasNetwork(Context context) {
        Boolean isConnected = false; // Initial Value
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected())
                isConnected = true;
        }

        return isConnected;
    }
}
