package in.nishachar.wiki_app.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import in.nishachar.wiki_app.R;
import in.nishachar.wiki_app.model.Page;
import in.nishachar.wiki_app.page.PageActivity;
import in.nishachar.wiki_app.shared.Constants;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, IPageCaller {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar homeToolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(homeToolbar);

        handleIntent(getIntent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager != null ? searchManager.getSearchableInfo(getComponentName()) : null);
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);

        return true;
    }

    private void showStartScreen() {
        HomeActivityFragment homeActivityFragment = (HomeActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if(homeActivityFragment != null) {
            homeActivityFragment.showPlaceHolder();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            item.expandActionView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.length() > 1)
            performSearch(newText);
        else
            showStartScreen();

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openPage(Page page) {
        Intent intent = new Intent(this, PageActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putInt(Constants.PAGE_ID, page.getPageid());
        mBundle.putString(Constants.PAGE_TITLE, page.getTitle());
        mBundle.putString(Constants.THUMB_PAGE_IMAGE, page.getThumbnail() != null ? page.getThumbnail().getSource() : null);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            performSearch(query);
        }
    }

    private void performSearch(String query){
        HomeActivityFragment homeActivityFragment = (HomeActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if(homeActivityFragment != null) {
            homeActivityFragment.clearOldRequest();
            homeActivityFragment.handleSearchQuery(query);
        }
    }
}
