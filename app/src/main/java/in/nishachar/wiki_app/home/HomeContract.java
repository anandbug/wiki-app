package in.nishachar.wiki_app.home;

import java.util.List;

import in.nishachar.wiki_app.model.Page;

interface HomeContract {
    /**
     * interface to make UI calls
     */
    interface View {
        void showSearchResults(List<Page> pages);
        void showLoader();
        void showRecycler();
        void showNoResultsPlaceHolder();
    }

    /**
     * interface to make user actions.
     */
    interface UserActionsListener {
        void queryWiki(String query);
        void disposeOldRequest();
    }
}
