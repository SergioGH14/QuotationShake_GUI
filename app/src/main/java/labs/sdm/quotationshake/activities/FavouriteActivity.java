/*
 * Copyright (c) 2016. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.sdm.quotationshake.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import labs.sdm.quotationshake.R;
import labs.sdm.quotationshake.adapters.QuotationAdapter;
import labs.sdm.quotationshake.pojo.Quotation;

public class FavouriteActivity extends AppCompatActivity {

    // Data source for favourite quotations
    List<Quotation> quotationList;
    // Adapter object linking the data source and the ListView
    QuotationAdapter adapter;
    // ListView object to display favourite quotations
    ListView favouriteListView;

    // Whether there is any favourite quotation,
    // so the action to remove them from the database can appear on the ActionBar
    boolean clearAllQuotations;

    // Position of the currently selected quotation
    int selectedItem;

    // Execution context
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // Reference to the current execution context
        context = this;

        // Keep a reference to the ListView object displaying the favourite quotations
        favouriteListView = (ListView) findViewById(R.id.lvFavourite);

        // Add all the quotations to the data source
        quotationList = new ArrayList<>();
        quotationList.addAll(getMockQuotations());

        // Create the adapter linking the data source to the ListView:
        adapter = new QuotationAdapter(this, R.layout.quotation_list_row, quotationList);

        // Set the data behind this ListView
        favouriteListView.setAdapter(adapter);

        // When an item in the list is clicked
        // access Wikipedia to look for information about the quotation author
        favouriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String author;
                try {
                    // Get the quotation author from the data source and
                    // encode it using UTF-8 to be used as part of an URL
                    author = URLEncoder.encode(quotationList.get(position).getQuoteAuthor(), "UTF-8");
                    // If the quotation is not anonymous, then access Wikipedia
                    if (!author.isEmpty()) {
                        // Create an implicit Intent
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        // Specify the URI required to search for the author on Wikipedia
                        intent.setData(
                                Uri.parse("http://en.wikipedia.org/wiki/Special:Search?search="
                                        + author));
                        // Check whether there is an application able to handle that Intent
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // Launch the activity
                            startActivity(intent);
                        }
                    }
                    // If the quotation is anonymous then display a message
                    else {
                        Toast.makeText(context, R.string.anonymous_author, Toast.LENGTH_SHORT).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        // When an item in the list is long clicked then ask for confirmation before deleting it
        favouriteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Keep a reference to the currently selected item
                selectedItem = position;

                // Build an AlertDialog to ask for confirmation before deleting the item
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Set the massage to display in the Dialog
                builder.setMessage(R.string.confirmation_delete);
                // Include a Button for handling positive confirmation: delete quotation
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the quotation from the data source
                        quotationList.remove(selectedItem);
                        // Notify the adapter to update the ListView since the data source has changed
                        adapter.notifyDataSetChanged();

                        // If there are no quotations in the favourite list then set to false the flag
                        // that controls whether to display the action for deleting all the quotations
                        if (quotationList.size() == 0) {
                            clearAllQuotations = false;
                            // Ask the system to rebuild the options of the ActionBar
                            supportInvalidateOptionsMenu();
                        }

                    }
                });
                // Include a Button for handling negative confirmation: do not delete quotation
                // No need for an onClickListener() here, as no action will take place
                builder.setNegativeButton(android.R.string.no, null);
                // Create and show the Dialog
                builder.create().show();
                // Return true as we handled the event
                return true;
            }
        });

        // If there are no quotations in the favourite list then set to false the flag
        // that controls whether to display the action for deleting all the quotations
        if (quotationList.size() > 0) {
            clearAllQuotations = true;
        }

    }

    /*
        This method is executed when the activity is created to populate the ActionBar with actions
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Generate the Menu object from the XML resource file
        getMenuInflater().inflate(R.menu.menu_favourite, menu);
        // Make the delete action visible depending on whether there is any favourite quotation
        menu.findItem(R.id.action_clear).setVisible(clearAllQuotations);

        return super.onCreateOptionsMenu(menu);
    }

    /*
        This method is executed when any action from the ActionBar is selected
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Determine the action to take place according to the Id of the action selected
        switch (item.getItemId()) {

            // Delete all favourite quotations
            case R.id.action_clear:

                // Build an AlertDialog to ask for confirmation before deleting all quotations
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Set the massage to display in the Dialog
                builder.setMessage(R.string.confirmation_clear);
                // Include a Button for handling positive confirmation: delete all quotations
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clear the data source
                        quotationList.clear();
                        // Notify the adapter to update the ListView since the data source has changed
                        adapter.notifyDataSetChanged();
                        // set to false the flag that controls whether to display
                        // the action for deleting all the quotations
                        clearAllQuotations = false;
                        // Ask the system to rebuild the options of the ActionBar
                        supportInvalidateOptionsMenu();
                    }
                });
                // Include a Button for handling negative confirmation: do not delete all quotations
                // No need for an onClickListener() here, as no action will take place
                builder.setNegativeButton(android.R.string.no, null);
                // Create and show the Dialog
                builder.create().show();

                // Return true as we handled the event
                return true;
        }
        // There was no custom behaviour for that action, so let the system take care of it
        return super.onOptionsItemSelected(item);
    }

    /*
        This method provides a list of quotations for test purposes.
    */
    private List<Quotation> getMockQuotations() {
        List<Quotation> result = new ArrayList<>();
        Quotation item;

        item = new Quotation();
        item.setQuoteText("Think Big");
        item.setQuoteAuthor("IMAX");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Push button publishing");
        item.setQuoteAuthor("Blogger");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Beauty outside. Beast inside");
        item.setQuoteAuthor("Mac Pro");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("American by birth. Rebel by choice");
        item.setQuoteAuthor("Harley Davidson");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Don't be evil");
        item.setQuoteAuthor("Google");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("If you want to impress someone, put him on your Black list");
        item.setQuoteAuthor("Johnnie Walker");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Live in your world. Play in ours");
        item.setQuoteAuthor("Playstation");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Impossible is nothing");
        item.setQuoteAuthor("Adidas");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Solutions for a small planet");
        item.setQuoteAuthor("IBM");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("I'm lovin it");
        item.setQuoteAuthor("McDonalds");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Just do it");
        item.setQuoteAuthor("Nike");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Melts in your mouth, not in your hands");
        item.setQuoteAuthor("M&M");
        result.add(item);

        item = new Quotation();
        item.setQuoteText("Because you're worth it");
        item.setQuoteAuthor("Loreal");
        result.add(item);

        return result;
    }

}
