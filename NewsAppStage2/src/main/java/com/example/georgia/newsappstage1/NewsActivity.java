package com.example.georgia.newsappstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = News.class.getName ();

    /**
     * URL for news data from the Guardian dataset
     */
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?q=sports%2C%20technology%2Cworldwide%2Cpolitics&show-tags=contributor&order-by=newest&page-size=20&api-key=48204420-2fdb-45dd-b9bf-0baee3ebbf2b";


    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of earthquakes
     */
    private NewsAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.news_activity );

        // Find a reference to the {@link ListView} in the layout
        ListView NewsListView = findViewById ( R.id.news_list );

        mEmptyStateTextView = findViewById ( R.id.empty_view );
        NewsListView.setEmptyView ( mEmptyStateTextView );

        // Create a new adapter that takes an empty list of News as input
        mAdapter = new NewsAdapter ( this, new ArrayList<News> () );

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        NewsListView.setAdapter ( mAdapter );

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected article.
        NewsListView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                News currentNews = mAdapter.getItem ( position );

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse ( currentNews.getmUrl () );

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent ( Intent.ACTION_VIEW, newsUri );

                // Send the intent to launch a new activity
                startActivity ( websiteIntent );
            }
        } );

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService ( Context.CONNECTIVITY_SERVICE );

        // Get details on the currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo ();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected ()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager ();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader ( NEWS_LOADER_ID, null, this );
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById ( R.id.loading_info );
            loadingIndicator.setVisibility ( View.GONE );

            // Update empty state with no connection error message
            mEmptyStateTextView.setText ( R.string.no_internet_connection );
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences ( this );
        String resultsLimit = sharedPrefs.getString (
                getString ( R.string.settings_results_limit_key ),
                getString ( R.string.settings_results_limit_default ) );
        String orderBy = sharedPrefs.getString (
                getString ( R.string.settings_order_by_key ),
                getString ( R.string.settings_order_by_default ) );
        Uri baseUri = Uri.parse ( GUARDIAN_REQUEST_URL );
        Uri.Builder uriBuilder = baseUri.buildUpon ();

        uriBuilder.appendQueryParameter ( "format", "json" );
        uriBuilder.appendQueryParameter ( "limit", "10" );
        uriBuilder.appendQueryParameter ( "page-size", resultsLimit );
        uriBuilder.appendQueryParameter ( "orderby", orderBy );
       

        return new NewsLoader ( this, uriBuilder.toString () );
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById ( R.id.loading_info );
        loadingIndicator.setVisibility ( View.GONE );

        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText ( R.string.sorry_there_are_no_news_to_display );

        // Clear the adapter of previous article data
        mAdapter.clear ();

        // If there is a valid list of {@link New}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty ()) {
            mAdapter.addAll ( news );
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear ();
    }

    @Override
    // This method initialize the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.main, menu );
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent ( this, SettingsActivity.class );
            startActivity ( settingsIntent );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }
}

