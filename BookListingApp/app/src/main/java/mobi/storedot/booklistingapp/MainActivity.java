package mobi.storedot.booklistingapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * MainActivity implements the LoaderCallbacks interface,
 * along with a generic parameter specifying what the loader will return (in this case an Book).
 */

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /**
     * URL for book data from the Google Books data.
     * benefit of ending url with a q is that we can append search term at the end and then feed this url to AsyncTask.
     */

    private static final String GG_BASE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?maxResults:40&key=ADD HERE API KEY=";
    /**
     * Constant value for the book loader ID. We can choose any integer.
     */
    private static final int BOOK_LOADER_ID = 1;
    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "onCreate");

        final EditText searchEditText = (EditText) findViewById(R.id.edit_search);

        // Button reference
        Button submitSearch = (Button) findViewById(R.id.search_button);

        // now when click of button - have edit text value with it ...
        submitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // add edit text value to url query for final query value
                String searchQuery = searchEditText.getText().toString();
                String searchUrl = null;
                try {
                    searchUrl = GG_BASE_REQUEST_URL + URLEncoder.encode(searchQuery, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.i(LOG_TAG, "Edit Search button");

        /**
         * Get a reference to the ConnectivityManager to check state of network connectivity
         */

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        /**
         * Get details on the currently active default data
         network
          */

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected book.

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    /**
     * We need onCreateLoader(), for when the LoaderManager has determined that the loader
     * with our specified ID isn't running, so we should create a new one.
     */

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookLoader(this, GG_BASE_REQUEST_URL);
    }

    /**
     * We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(),
     * and use the book data to update our UI - by updating the dataset in the adapter.
     */

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    /**
     * And we need onLoaderReset(), we're being informed that the data from our loader is no longer valid.
     * This isn't actually a case that's going to come up with our simple loader,
     * but the correct thing to do is to remove all the book data from our UI
     * by clearing out the adapter’s data set.
     */

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}




