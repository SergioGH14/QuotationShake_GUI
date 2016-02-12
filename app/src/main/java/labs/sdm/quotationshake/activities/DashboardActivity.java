/*
 * Copyright (c) 2016. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.sdm.quotationshake.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import labs.sdm.quotationshake.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    /*
        This method is executed when any button in the Dashboard is clicked
    */
    public void dashboardButtonClicked(View v) {

        // Intent object to launch the related activity
        Intent intent = null;

        // Determine the activity to launch according to the Id of the Button clicked
        switch (v.getId()) {

            // Activity for getting a new quotation and adding it to favourites
            case R.id.bGetQuotations:
                intent = new Intent(this, QuotationActivity.class);
                break;

            // Activity for displaying and managing the favourite quotations
            case R.id.bFavourites:
                intent = new Intent(this, FavouriteActivity.class);
                break;

            // Activity for configuring the application
            case R.id.bSettings:
                intent = new Intent(this, SettingsActivity.class);
                break;

            // Activity to display the application credits
            case R.id.bAbout:
                intent = new Intent(this, AboutActivity.class);
                break;
        }

        // Launch the required activity
        if (intent != null) {
            startActivity(intent);
        }
    }

}
