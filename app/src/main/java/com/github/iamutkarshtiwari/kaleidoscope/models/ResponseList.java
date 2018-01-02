package com.github.iamutkarshtiwari.kaleidoscope.models;

import java.util.ArrayList;

/**
 * Created by utkarshtiwari on 01/01/18.
 */

public class ResponseList {
    private ArrayList<Movie> results;
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
