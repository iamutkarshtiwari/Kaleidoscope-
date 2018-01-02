package com.github.iamutkarshtiwari.kaleidoscope.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import com.github.iamutkarshtiwari.kaleidoscope.activity.HomeActivity;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movie;

public class JSONParser {

    private String dataSource;
    private Context context;

    public JSONParser(String dataSource, Context context) {
        this.dataSource = dataSource;
        this.context = context;
    }

    /**
     * Loads JSON from the asset file
     *
     * @param dataSource name of the asset file
     * @return JSON string
     */
    public String loadJSONFromAsset(String dataSource) {
        String json = null;
        try {
            InputStream is =  context.getAssets().open(dataSource);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Parses the JSON string and generates list of Moviess
     *
     * @return ArrayList of Moviess
     */
    public ArrayList<Movie> getResponseData() {
        ArrayList<Movie> itemList = new ArrayList<Movie>();
        String responseJSON = loadJSONFromAsset(this.dataSource);

        // Return empty list if null response
        if (responseJSON == null) {
            return itemList;
        }

        try {
            JSONObject jsonObject = new JSONObject(responseJSON);

            // Return empty list if response result is not "ok"
            if (!jsonObject.getString("result").equalsIgnoreCase("ok")) {
                return itemList;
            }

            JSONArray jsonArray = jsonObject.getJSONArray("data");

            // Generate Movie arraylist from json data
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject MoviesObject = jsonArray.getJSONObject(i);

                String id = MoviesObject.getString("id");
                String MoviesName = MoviesObject.getString("name");
                String isSoldOut = MoviesObject.getString("status");
                long likesCount = MoviesObject.getLong("num_likes");
                long commentsCount = MoviesObject.getLong("num_comments");
                long price = MoviesObject.getLong("price");
                String photoURL = MoviesObject.getString("photo");

//                Movie Movies = new Movie(id, MoviesName, isSoldOut, likesCount, commentsCount, price, photoURL);
//                itemList.add(Movies);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }
}
