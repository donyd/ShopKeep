package com.donyd.jsunscripted.www.shopkeep.sqlite;

import android.provider.BaseColumns;

// adapted from https://app.pluralsight.com/library/courses/android-database-application-sqlite-building-your-first/table-of-contents
// and https://developer.android.com/training/data-storage/sqlite.html
public final class ShopKeepContract {

    public static final class Products implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "products";

        // Column Names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
    }

    public static final class Purchases implements BaseColumns {
        // Table Name
        public static final String TABLE_NAME = "purchases";

        // Column Names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_PURCHASE_DATE = "purchase_date";
        public static final String COLUMN_ESTIMATED_DURATION = "estimated_duration";
        public static final String COLUMN_ACTUAL_DURATION = "actual_duration";
    }

}
