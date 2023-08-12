package com.rishi.iprangesearch.factory;

import com.rishi.iprangesearch.adapter.CsvDataAdapter;
import com.rishi.iprangesearch.adapter.DataAdapter;
import com.rishi.iprangesearch.adapter.JsonDataAdapter;

public class DataAdapterFactory {
    public static DataAdapter getAdapter(String dataType) {
        switch (dataType.toLowerCase()) {
            case "json":
                return new JsonDataAdapter();
            case "csv":
                return new CsvDataAdapter();
            default:
                return null;
        }
    }
}