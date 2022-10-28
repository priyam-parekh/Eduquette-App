package com.eduquette;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.eduquette.model.IndexCard;
import com.eduquette.model.IndexDeck;
import com.eduquette.model.ReviewOrder;
import com.eduquette.model.ReviewType;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class for the IndexCard screen
 */
public class IndexCardActivity extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_NAME = "SharedPreference";

    // Instance variable to store the index deck
    private IndexDeck indexDeck;

    /**
     * Overridden method that is called when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cards);

        // Initialize all the panel components
        Toolbar toolbar = findViewById(R.id.toolbar_view_cards);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_view_cards);
        ImageView ivSubjectLogo = findViewById(R.id.iv_view_cards_subject_logo);
        TextView tvSubject = findViewById(R.id.tv_view_cards_subject);

        // Configure the menu and listeners
        toolbar.inflateMenu(R.menu.menu_index_card);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new OptionMenuClickListener());
        initCollapsingToolbar();

        // Load the current subject and initialize the components with data
        loadCurrentSubject();
        ivSubjectLogo.setImageDrawable(getDrawable(indexDeck.getThumbnail()));
        tvSubject.setText(indexDeck.getSubject());

        // Initialize the recycler view and setup the adapter
        List<IndexCard> indexCardList = new ArrayList<>();
        IndexCardAdapter adapter = new IndexCardAdapter(this, indexCardList);
        indexCardList.addAll(indexDeck.getIndexCards());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Overriden method that is called when the option menu is created
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index_card, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Loads the current subject from the shared preferences
     */
    private void loadCurrentSubject() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        // Create the index deck object from the data
        indexDeck = new Gson().fromJson(sharedPreferences.getString(IndexDeck.ID, ""), IndexDeck.class);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar_view_cards);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar_view_cards);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(indexDeck.getSubject());
                    collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Class to handle the option menu click
     */
    private class OptionMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Intent intent;
            if (menuItem.getItemId() == R.id.action_review_terms_in_order) {
                // If the review terms in order is called then start the review card activity
                intent = new Intent(getBaseContext(), ReviewIndexCardActivity.class);
                intent.putExtra("ReviewType", ReviewType.TERM);
                intent.putExtra("ReviewOrder", ReviewOrder.IN_ORDER);
                startActivity(intent);
                return true;
            } else if (menuItem.getItemId() == R.id.action_review_definitions_in_order) {
                // If the review definitions in order is called then start the review card activity
                intent = new Intent(getBaseContext(), ReviewIndexCardActivity.class);
                intent.putExtra("ReviewType", ReviewType.DEFINITION);
                intent.putExtra("ReviewOrder", ReviewOrder.IN_ORDER);
                startActivity(intent);
                return true;
            } else if (menuItem.getItemId() == R.id.action_review_terms_random_order) {
                // If the review terms in random order is called then start the review card activity
                intent = new Intent(getBaseContext(), ReviewIndexCardActivity.class);
                intent.putExtra("ReviewType", ReviewType.TERM);
                intent.putExtra("ReviewOrder", ReviewOrder.RANDOM_ORDER);
                startActivity(intent);
                return true;
            } else if (menuItem.getItemId() == R.id.action_review_definitions_random_order) {
                // If the review definitions in random order is called then start the review card activity
                intent = new Intent(getBaseContext(), ReviewIndexCardActivity.class);
                intent.putExtra("ReviewType", ReviewType.DEFINITION);
                intent.putExtra("ReviewOrder", ReviewOrder.RANDOM_ORDER);
                startActivity(intent);
                return true;
            }
            return false;
        }
    }
}
