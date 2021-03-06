/**
 * Title: GroceryFragment.java
 * Created by Alton Webber on 4/20/15.
 * Description:
 *
 * Purpose:
 *
 *
 *
 * Usage:
 *
 *
 **/
package com.example.awebber.grocery.fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.awebber.grocery.R;
import com.example.awebber.grocery.activites.DetailActivity;
import com.example.awebber.grocery.adapter.GroceryCursorAdapter;
import com.example.awebber.grocery.data.GroceryContract;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor>{
    public static final String TAG =SearchFragment.class.getSimpleName();
    private static Context mContext ;

     // If non-null, this is the current filter the user has provided.
    String mCurFilter;
    private static final int GROCERY_LOADER = 0;

    GroceryCursorAdapter mGroceryCursorAdapter;
    public SearchFragment() {
            }
   // implemention of SearchView.OnQueryTextListener
    public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(GROCERY_LOADER, null, this);
        return true;
    }
    @Override public boolean onQueryTextSubmit(String query) {
        // Don't care about this.
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        mContext =  getActivity();
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mGroceryCursorAdapter = new GroceryCursorAdapter(getActivity(),null,0);
        mGroceryCursorAdapter.setDisplayStyle(SearchFragment.class.getSimpleName());
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        SearchView grocerySearch = ( SearchView) rootView.findViewById(R.id.search_products);
        grocerySearch.setOnQueryTextListener(this);

        ListView groceryListView = (ListView) rootView.findViewById(R.id.list_view_grocery);
        View empty = rootView.findViewById(R.id.emptyListElement);
        groceryListView.setEmptyView(empty);
        groceryListView.setAdapter(mGroceryCursorAdapter);


        // We'll call our SearchActivity
        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long  id) {
            // CursorAdapter returns a cursor at the correct position for getItem(), or null
            // if it cannot seek to that position.
            Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
            if (cursor != null) {
                Log.e( TAG, "Column Value : "+ cursor.getString(0) +
                        " Column name :"+ cursor.getColumnName(0));
                  Intent intent = new Intent(getActivity(), DetailActivity.class);
                Log.e( TAG, " A List Item was pressed");
                if(cursor.getString(1) != null){
                    Log.e( TAG, " Space 1 was not null");
                    intent.putExtra("Table", cursor.getString(0)) ;
                    intent.putExtra("Value", cursor.getString(1));
                }
                    else{
                    Log.e( TAG, " Space 1 was null");
                    intent.putExtra("Table",GroceryContract.CategoryEntry.TABLE_NAME) ;
                    intent.putExtra("Value",getArguments().getString("theCategory"));
                }
                startActivity(intent);
            }
        }
    });
                 return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(GROCERY_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
//todo MOVE CATEOGYR CODE TO OWN FRAGMENT CALLED SINGLE CATEROGRY FRAGMENT

        String sortOrder;

        Uri groceriesUri;
        String[] projections = new String[3];
        if (getArguments().getBoolean("isCategory"))
        {
            Log.i(TAG, " This is the Category" + getArguments().getString("theCategory"));
            groceriesUri = GroceryContract.GroceryEntry.
                    buildGroceriesCategory(getArguments().getString("theCategory"));
            projections[0] = GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME;
            projections[1] = GroceryContract.GroceryEntry.TABLE_NAME +"."+ GroceryContract.CategoryEntry._ID;
            projections[2] =null;
            sortOrder = GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME +" COLLATE NOCASE";
          }
         else {
             sortOrder =null;
             projections[0] = "'" + GroceryContract.GroceryEntry.TABLE_NAME + "'" + " AS table_name";
             projections[1] = GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME;
             projections[2] = GroceryContract.GroceryEntry._ID;
            if (mCurFilter == null) {
                //A Value that will never be used
                groceriesUri =  GroceryContract.GroceryEntry.buildCategoryWBrand("-1x208");
            } else {
                groceriesUri = GroceryContract.GroceryEntry.buildCategoryWBrand(mCurFilter);
                Log.i(TAG, "This is the Search Find " + projections[0] + " " + projections[1]);
            }
        }
        return new CursorLoader(getActivity(),
                groceriesUri,
                projections,
                null,
                null,
                sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mGroceryCursorAdapter.swapCursor(cursor);

    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mGroceryCursorAdapter.swapCursor(null);
    }

//TODO IMplemete correctly
    long addGrocery(String product_name) {
        long locationId;

        // First, check if the grocery with this city name exists in the db
        Cursor locationCursor = mContext.getContentResolver().query(
                GroceryContract.GroceryEntry.CONTENT_URI,
                new String[]{GroceryContract.GroceryEntry._ID},
                GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME + " = ?",
                new String[]{product_name},
                null);

        if (locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(GroceryContract.GroceryEntry._ID);
            locationId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_PRODUCT_NAME, product_name);
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_BRAND_LOC_KEY,0);
            locationValues.put(GroceryContract.GroceryEntry.COLUMN_CATEGORY_LOC_KEY,0);
            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    GroceryContract.GroceryEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    }

    /**
     * @return a new instance of {@link SearchFragment}
     *String theCategory allows the Category to be passed for the query
     *USED in  {@link MainFragment} CreateFragment Method
     **/
    public static SearchFragment newInstance(boolean isCategory,CharSequence theCategory) {
        SearchFragment fragment = new SearchFragment();


        Bundle args = new Bundle();

        args.putBoolean("isCategory", isCategory);
        args.putCharSequence("theCategory", theCategory);
        fragment.setArguments(args);

        return fragment;
    }


}
