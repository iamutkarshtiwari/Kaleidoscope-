package com.github.iamutkarshtiwari.kaleidoscope.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import com.github.iamutkarshtiwari.kaleidoscope.activity.HomeActivity;
import com.github.iamutkarshtiwari.kaleidoscope.models.Movies;

public class JSONParser {

    private String dataSource;

    public JSONParser(String dataSource) {
        this.dataSource = dataSource;
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
            InputStream is = HomeActivity.getContext().getAssets().open(dataSource);
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
    public ArrayList<Movies> getResponseData() {
        ArrayList<Movies> itemList = new ArrayList<Movies>();
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

            // Generate Movies arraylist from json data
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject MoviesObject = jsonArray.getJSONObject(i);

                String id = MoviesObject.getString("id");
                String MoviesName = MoviesObject.getString("name");
                String isSoldOut = MoviesObject.getString("status");
                long likesCount = MoviesObject.getLong("num_likes");
                long commentsCount = MoviesObject.getLong("num_comments");
                long price = MoviesObject.getLong("price");
                String photoURL = MoviesObject.getString("photo");

                Movies Movies = new Movies(id, MoviesName, isSoldOut, likesCount, commentsCount, price, photoURL);
                itemList.add(Movies);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }
}
