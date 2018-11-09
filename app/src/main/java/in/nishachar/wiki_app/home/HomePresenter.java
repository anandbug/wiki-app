package in.nishachar.wiki_app.home;

import androidx.annotation.NonNull;
import in.nishachar.wiki_app.repository.WikiSearchRepo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.UserActionsListener {
    private final HomeContract.View mHomeView;
    private final WikiSearchRepo mWikiSearchRepo;
    private final CompositeDisposable compositeDisposable;

    /**
     * constructor to initialize our view of MVP architecture.
     *
     * @param mHomeView       the view of MVP
     * @param mWikiSearchRepo repository of hash tags
     */
    HomePresenter(@NonNull HomeContract.View mHomeView, @NonNull WikiSearchRepo mWikiSearchRepo) {
        this.mHomeView = mHomeView;
        this.mWikiSearchRepo = mWikiSearchRepo;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void queryWiki(String query) {
        mHomeView.showLoader();

        compositeDisposable.add(mWikiSearchRepo.searchWiki("query", "json", "pageimages%7Cpageterms", "prefixsearch", 1, 2, "thumbnail", 100, 10, "description", query, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
                .doOnNext(response -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getQuery() != null) {
                        mHomeView.showRecycler();
                        mHomeView.showSearchResults(response.body().getQuery().getPages());
                    } else {
                        mHomeView.showNoResultsPlaceHolder();
                    }
                })
                .subscribe());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeOldRequest() {
        compositeDisposable.clear();
    }
}
