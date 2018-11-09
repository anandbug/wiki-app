package in.nishachar.wiki_app.page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ViewSwitcher;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.nishachar.wiki_app.R;
import in.nishachar.wiki_app.repository.WikiApiClient;
import in.nishachar.wiki_app.repository.WikiSearchRepo;
import in.nishachar.wiki_app.shared.Constants;
import in.nishachar.wiki_app.shared.GlideApp;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PageActivity extends AppCompatActivity {
    private String url = null;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewSwitcher)
    ViewSwitcher viewSwitcher;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.circleCover)
    AppCompatImageView circleCover;

    @OnClick(R.id.fab)
    void onFabClicked() {
        if(url != null) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                viewSwitcher.showNext();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                viewSwitcher.showNext();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            setTitle(extra.getString(Constants.PAGE_TITLE));

            String image = extra.getString(Constants.THUMB_PAGE_IMAGE);
            GlideApp.with(this)
                    .load(image)
                    .placeholder(R.drawable.wiki_logo)
                    .transform(new CircleCrop())
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .into(circleCover);

            Integer pageId = extra.getInt(Constants.PAGE_ID);
            WikiApiClient.getClient(this)
                    .create(WikiSearchRepo.class)
                    .getPage("query", "json", "info", pageId, "url")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(Throwable::printStackTrace)
                    .doOnNext(response -> {
                        if (response.isSuccessful() && response.body() != null) {
                            JsonObject responseObject = response.body();
                            JsonObject query = responseObject.getAsJsonObject("query");
                            JsonObject pages = query.getAsJsonObject("pages");
                            JsonObject pageID = pages.getAsJsonObject(String.valueOf(pageId));
                            JsonElement fullUrl = pageID.get("fullurl");
                            url = fullUrl.getAsString();
                            webView.loadUrl(url);
                        }
                    })
                    .subscribe();
        }

    }
}
