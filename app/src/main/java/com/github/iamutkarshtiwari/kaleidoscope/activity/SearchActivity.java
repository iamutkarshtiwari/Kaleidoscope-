//package com.github.iamutkarshtiwari.kaleidoscope.activity;
//
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import com.arlib.floatingsearchview.FloatingSearchView;
//import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
//import com.github.iamutkarshtiwari.kaleidoscope.models.Movies;
//
//import java.util.ArrayList;
//
//import iamutkarshtiwari.github.io.mercariapp.R;
//import iamutkarshtiwari.github.io.mercariapp.adapter.HomeRecyclerAdapter;
//import iamutkarshtiwari.github.io.mercariapp.models.Product;
//import iamutkarshtiwari.github.io.mercariapp.models.ProductSuggestion;
//import iamutkarshtiwari.github.io.mercariapp.utils.JSONParser;
//
//public class SearchActivity extends AppCompatActivity {
//
//    private static final int PRODUCT_LIST = 1;
//    private static final int SUGGESTION_LIST = 2;
//
//    private FloatingSearchView searchBarView;
//    private String mLastQuery = "";
//    private ArrayList<Movies> allItemList;
//    private RecyclerView recyclerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        searchBarView = (FloatingSearchView) findViewById(R.id.floating_search_view);
//        searchBarView.setOnHomeActionClickListener(
//                new FloatingSearchView.OnHomeActionClickListener() {
//                    @Override
//                    public void onHomeClicked() {
//                        finish();
//                    }
//                });
//
//        int numberOfColumns = 2;
//        // Change column count based on screen orientation
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            numberOfColumns = 3;
//        }
//
//        JSONParser jsonParser = new JSONParser("all.json");
//        allItemList = jsonParser.getResponseData();
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        ArrayList<Product> searchResult;
//        searchResult = new ArrayList<Product>();
//        HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(this, searchResult);
//        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), numberOfColumns);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//        setupSearchBar();
//    }
//
//    /**
//     * Sets up listeners for search bar
//     */
//    public void setupSearchBar() {
//        searchBarView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//            @Override
//            public void onSearchTextChanged(String oldQuery, final String newQuery) {
//
//                if (!oldQuery.equals("") && newQuery.equals("")) {
//                    searchBarView.clearSuggestions();
//                } else {
//                    // Will show progress bar in case of slow internet response
//                    searchBarView.showProgress();
//                    // Get search suggestions
//                    searchBarView.swapSuggestions(filteredProductSuggestions(newQuery));
//                    searchBarView.hideProgress();
//                }
//            }
//        });
//
//        searchBarView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
//            @Override
//            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
//                setSearchLabelVisibility(false);
//                ArrayList<Product> searchResult = filteredProducts(searchSuggestion.getBody());
//                updateResults(searchResult);
//            }
//
//            @Override
//            public void onSearchAction(String query) {
//                mLastQuery = query;
//                ArrayList<Product> searchResult = filteredProducts(query);
//                setSearchLabelVisibility(searchResult.size() == 0);
//                updateResults(searchResult);
//            }
//        });
//    }
//
//    /**
//     * Returns list of filtered products/suggestions
//     *
//     * @param query search keyword
//     * @return
//     */
//    public ArrayList<Product> filteredProducts(String query) {
//        //new array list that will hold the filtered data
//
//        ArrayList<Product> filteredProducts = new ArrayList<Product>();
//
//        //looping through existing elements
//        for (Product item : allItemList) {
//            //if the existing elements contains the search input
//            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
//                filteredProducts.add(item);
//            }
//        }
//        return filteredProducts;
//    }
//
//    /**
//     * Returns list of filtered suggestions
//     *
//     * @param query search keyword
//     * @return
//     */
//    public ArrayList<ProductSuggestion> filteredProductSuggestions(String query) {
//        //new array list that will hold the filtered data
//
//        ArrayList<ProductSuggestion> filteredProductSuggestions = new ArrayList<>();
//
//        //looping through existing elements
//        for (Product item : allItemList) {
//            //if the existing elements contains the search input
//            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
//                filteredProductSuggestions.add(new ProductSuggestion(item.getName()));
//            }
//        }
//        return filteredProductSuggestions;
//    }
//
//    /**
//     * Updates search results in the recycler view
//     *
//     * @param searchResult
//     */
//    public void updateResults(ArrayList<Product> searchResult) {
//        HomeRecyclerAdapter adapter = (HomeRecyclerAdapter) recyclerView.getAdapter();
//        adapter.updateAdapterData(searchResult);
//        adapter.notifyDataSetChanged();
//
//        searchBarView.clearSuggestions();
//        searchBarView.clearQuery();
//        searchBarView.clearSearchFocus();
//    }
//
//    /**
//     * Sets visibility of search label
//     *
//     * @param flag
//     */
//    public void setSearchLabelVisibility(boolean flag) {
//        RelativeLayout searchLabel = (RelativeLayout) findViewById(R.id.search_label);
//        int id = flag ? View.VISIBLE : View.INVISIBLE;
//        searchLabel.setVisibility(id);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (searchBarView.isSearchBarFocused()) {
//            searchBarView.clearSuggestions();
//            searchBarView.clearQuery();
//            searchBarView.clearSearchFocus();
//            return;
//        }
//        finish();
//    }
//}
//
