package com.example.awebber.grocery.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Title: GroceryContract.java
 * Created by Alton Webber on 4/15/15.
 * Description: Tables have plural names.  If the name requires two words
 * separate words with an underscore.
 *
 * Purpose: Define the data storage of Sqlite database.  Agreement between
 * data model and view describing how information is stored.  I contains
 *
 *
 *
 * Usage:
 *
 *
 */
public  class GroceryContract  {
    public static final String TAG =" GroceryContract ";
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.awebber.grocery";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider. content:// is the scheme that says we are accessing a content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.awebber.grocery/groceries/ is a valid path for
    // looking at groceries data.
    public static final String PATH_GROCERIES  = GroceryEntry.TABLE_NAME;// "groceries";
    public static final String PATH_BRANDS     = BrandEntry.TABLE_NAME;// "brands";
    public static final String PATH_CATEGORIES = CategoryEntry.TABLE_NAME;//"categories";
    public static final String PATH_INVENTORY  = InventoryEntry.TABLE_NAME;//
    // Inner class that defines the table contents of the groceries table
    public static final class GroceryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
             BASE_CONTENT_URI.buildUpon().appendPath(PATH_GROCERIES).build();

        public static final String CONTENT_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROCERIES;
        public static final String CONTENT_ITEM_TYPE =
             ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROCERIES;

        public static final String TABLE_NAME = "groceries";

        // Column with the foreign key into the basic_description table.
        public static final String COLUMN_CATEGORY_LOC_KEY = "category_id";

        // Column with the foreign key into the brands table.
        public static final String COLUMN_BRAND_LOC_KEY = "brand_id";

        // The name of the number
        public static final String COLUMN_QUANTITY = "quantity";

        // The name of the product Coconut oil,Pure Sport,Procell
        public static final String COLUMN_PRODUCT_NAME = "product_name";


        //Build a uri with an id number @ the end used for queries with id

        public static String getIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
         //For queries use buildGroceriesBasicDescWBran
        public static Uri buildGroceriesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //For queries use buildGroceriesBasicDescWBran
        public static Uri buildGroceriesBrand(String brandName) {
                        return CONTENT_URI.buildUpon().
                                appendPath(PATH_BRANDS).
                                appendPath(brandName).
                                build();
                   }

        //For queries use buildGroceriesBasicDescWBrand
        public static Uri buildGroceriesCategory(String productCategory) {
            return CONTENT_URI.buildUpon().
                    appendPath(PATH_CATEGORIES).
                    appendPath(productCategory).
                    build();
        }
        public static Uri buildGroceriesInventory() {
            return CONTENT_URI.buildUpon().
                    appendPath(PATH_INVENTORY).
                    build();
        }
        //User for Search S
        public static Uri buildCategoryWBrand(String SearchString ) {

            return CONTENT_URI.
                    buildUpon().
                    appendPath(PATH_CATEGORIES).
                    appendPath(PATH_BRANDS).
                    appendPath(SearchString).
                    build();
        }
        public static String getSearchStringFromUri(Uri uri) {

            return uri.getPathSegments().get(3);
        }
        public static String getBrandNameFromUri(Uri uri) {

            return uri.getPathSegments().get(2);
      }

        public static String getCategoryFromUri(Uri uri) {

            return  uri.getPathSegments().get(2);
        }
    }

    //    Inner class that defines the table contents of the brands table
    public static final class BrandEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BRANDS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRANDS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRANDS;

        public static final String TABLE_NAME = "brands";

        // The of the name items Brand Item e.g DURACELL,LUBRIDERM,GATORADE
        public static final String COLUMN_PRODUCT_BRAND_NAME = "brand_name";

        public static Uri buildBrandsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    //   Inner class that defines the table contents of the basic_descriptions table
    public static final class CategoryEntry implements BaseColumns {
           public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;

        public static final String TABLE_NAME = "categories";

        //A basic description of what the items is e.g cookie ,cereal ,Fruit ,tortilla chip
        //name should be in singular form
        public static final String COLUMN_CATEGORY_NAME = "category_name";

        public static Uri buildCategoriesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class InventoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INVENTORY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROCERIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GROCERIES;

        public static final String TABLE_NAME = "inventory";

        // Column with the foreign key into the basic_description table.
        public static final String COLUMN_GROCERY_LOC_KEY = "grocery_id";

        // The amound of the product that exist
        public static final String COLUMN_QUANTITY = "quantity";



        //For queries use buildGroceriesBasicDescWBran
        public static Uri buildInventoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getBrandNameFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getBasicDescFromUri(Uri uri) {
            return  uri.getPathSegments().get(2);
        }
    }

}
