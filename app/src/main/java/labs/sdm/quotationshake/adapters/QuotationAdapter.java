/*
 * Copyright (c) 2017. David de Andrés and Juan Carlos Ruiz, DISCA - UPV, Development of apps for mobile devices.
 */

package labs.sdm.quotationshake.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import labs.sdm.quotationshake.R;
import labs.sdm.quotationshake.pojo.Quotation;

/**
 * Custom adapter to associate the source data with Views within the ListView.
 */
public class QuotationAdapter extends ArrayAdapter {

    // Hold reference to the source data, context, and layout
    private List<Quotation> data;
    private Context context;
    private int layout;

    // Hold references to View elements
    private class ViewHolder {
        TextView tvQuotationText;
        TextView tvQuotationAuthor;
    }

    public QuotationAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);

        this.data = objects;
        this.context = context;
        this.layout = resource;
    }

    /*
        Creates and populates a View with the information from the required position of the data source.
    */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;
        ViewHolder holder;

        // Reuse the View if it already exists
        if (convertView == null) {
            // Inflate the View to create it for the first time
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inflater.inflate(this.layout, null);

            // Keep references for View elements in the layout
            holder = new ViewHolder();
            holder.tvQuotationText = (TextView) result.findViewById(R.id.tvQuote);
            holder.tvQuotationAuthor = (TextView) result.findViewById(R.id.tvAuthor);
            // Associate the ViewHolder to the View
            result.setTag(holder);
        }

        // Retrieve the ViewHolder from the View
        holder = (ViewHolder) result.getTag();
        // Populate the View with information from the required position of the data source
        holder.tvQuotationText.setText(data.get(position).getQuoteText());
        holder.tvQuotationAuthor.setText(data.get(position).getQuoteAuthor());

        // Return the View
        return result;
    }
}
