/*
 * Copyright (c) 2016. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.sdm.quotationshake.activities;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import labs.sdm.quotationshake.R;

public class QuotationActivity extends AppCompatActivity {

    // Whether the user can ask for a new quotation, so the option can appear in the ActionBar
    boolean newQuotation = true;
    // Whether this quotation could be added to the favourites list,
    // so the option can appear in the ActionBar
    boolean addQuotation = false;

    // Hold references to View objects
    ProgressBar progressBar = null;
    TextView tvQuote;
    TextView tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        // Keep a reference to:
        //  the ProgressBar showing that the HTTP request is still in progress
        //  the TextView displaying the text of the quotation
        //  the TextView displaying the author of the quotation
        progressBar = (ProgressBar) findViewById(R.id.pbGettingQuotation);
        tvQuote = ((TextView) findViewById(R.id.tvQuotation));
        tvAuthor = ((TextView) findViewById(R.id.tvAuthor));

        // As there is no quotation to show when the activity is created,
        // display a greetings message that includes the user's name
        tvQuote.setText(
                String.format(getResources().getString(R.string.greetings),
                        getResources().getString(R.string.nameless)));
    }

    /*
        This method is executed when the activity is created to populate the ActionBar with actions
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Generate the Menu object from the XML resource file
        getMenuInflater().inflate(R.menu.menu_quotation, menu);
        // Make the two actions visible according to current state of the activity
        // The user cannot ask for a new quotation until the previous one is served
        menu.findItem(R.id.action_refresh).setVisible(newQuotation);
        // The quotation cannot be added to the database if it is already there or there is an ongoing request
        menu.findItem(R.id.action_add).setVisible(newQuotation && addQuotation);

        return super.onCreateOptionsMenu(menu);
    }

    /*
        This method is executed when any action from the ActionBar is selected
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Determine the action to take place according to the Id of the action selected
        switch (item.getItemId()) {

            // Get a new quotation
            case R.id.action_refresh:

                // There is no Internet connection available, so inform the user about that
                Toast.makeText(this, R.string.connection_not_available, Toast.LENGTH_SHORT).show();

                // The action was successfully resolved
                return true;

            // Add this quotation to the favourites list
            case R.id.action_add:

                // The action was successfully resolved
                return true;
        }
        // There was no custom behaviour for that action, so let the system take care of it
        return super.onOptionsItemSelected(item);
    }

}