package in.nishachar.wiki_app.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.nishachar.wiki_app.R;
import in.nishachar.wiki_app.model.Page;
import in.nishachar.wiki_app.repository.WikiApiClient;
import in.nishachar.wiki_app.repository.WikiSearchRepo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * A placeholder fragment containing a simple view of Home-MVP.
 */
public class HomeActivityFragment extends Fragment implements HomeContract.View {
    private HomeContract.UserActionsListener mActionsListener;
    private SearchAdapter searchAdapter;

    @BindView(R.id.searchRecycler)
    RecyclerView recyclerView;

    @BindView(R.id.viewFlipper)
    ViewFlipper viewFlipper;

    public HomeActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);

        searchAdapter = new SearchAdapter(getActivity());

        recyclerView.setAdapter(searchAdapter);

        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        if(getActivity() == null)
            return;

        mActionsListener = new HomePresenter(this, WikiApiClient.getClient(getActivity()).create(WikiSearchRepo.class));
    }

    void handleSearchQuery(String query){
        if(mActionsListener != null)
            mActionsListener.queryWiki(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSearchResults(List<Page> pages) {
        searchAdapter.addPages(pages);
    }

    @Override
    public void showLoader() {
        viewFlipper.setDisplayedChild(1);
    }

    @Override
    public void showRecycler() {
        viewFlipper.setDisplayedChild(2);
    }

    @Override
    public void showNoResultsPlaceHolder() {
        viewFlipper.setDisplayedChild(3);
    }

    void showPlaceHolder() {
        viewFlipper.setDisplayedChild(0);
    }

    void clearOldRequest() {
        mActionsListener.disposeOldRequest();
    }
}
