package com.eduquette;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.eduquette.model.IndexCard;
import com.eduquette.model.IndexDeck;
import com.eduquette.model.ReviewOrder;
import com.eduquette.model.ReviewType;

/**
 * Activity class for reviewing the IndexCard screen
 */
public class ReviewIndexCardActivity extends AppCompatActivity {
    private static final String SHARED_PREFERENCES_NAME = "SharedPreference";

    // Instance variables for all the activity components
    private IndexDeck indexDeck;
    private ReviewType reviewType;
    private TextView tvReviewIndexCardTextLabel, tvReviewIndexCardText;
    private DeckController deckController;
    private IndexCard currentIndexCard;
    private ReviewType currentReviewType;
    private Button bReviewCardFlipCard, bReviewCardNextCard, bReviewCardReset;

    /**
     * Overridden method that is called when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_index_card);

        // Initialize all the panel components
        ImageView ivSubjectLogo = findViewById(R.id.iv_review_cards_subject_logo);
        TextView tvSubject = findViewById(R.id.tv_review_cards_subject);
        bReviewCardFlipCard = findViewById(R.id.b_review_card_flip_card);
        bReviewCardNextCard = findViewById(R.id.b_review_card_next_card);
        bReviewCardReset = findViewById(R.id.b_review_card_reset);
        tvReviewIndexCardTextLabel = findViewById(R.id.tv_review_index_card_text_label);
        tvReviewIndexCardText = findViewById(R.id.tv_review_index_card_text);

        // Load the current subject, shared data and initialize the components with the data
        ReviewOrder reviewOrder = (ReviewOrder) getIntent().getSerializableExtra("ReviewOrder");
        reviewType = (ReviewType) getIntent().getSerializableExtra("ReviewType");
        currentReviewType = reviewType;
        loadCurrentSubject();
        ivSubjectLogo.setImageDrawable(getDrawable(indexDeck.getThumbnail()));
        tvSubject.setText(indexDeck.getSubject());

        // Configure the listeners for the buttons
        bReviewCardFlipCard.setOnClickListener(new CardFlipClickListener());
        bReviewCardNextCard.setOnClickListener(new NextCardClickListener());
        bReviewCardReset.setOnClickListener(new ResetCardClickListener());

        // Initialize the deck controller with the current subject
        deckController = new DeckController(indexDeck, reviewOrder);
        currentIndexCard = deckController.getNextCard();
        currentCardUpdated();
    }

    /**
     * Method to update the text fields based on the current card
     */
    private void currentCardUpdated() {
        if (currentReviewType == ReviewType.TERM) {
            tvReviewIndexCardTextLabel.setText(ReviewType.TERM.name());
            tvReviewIndexCardText.setText(Html.fromHtml(currentIndexCard.getTerm(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvReviewIndexCardTextLabel.setText(ReviewType.DEFINITION.name());
            tvReviewIndexCardText.setText(Html.fromHtml(currentIndexCard.getDefinition(), Html.FROM_HTML_MODE_COMPACT));
        }
    }

    /**
     * Method that is called when the screen comes back in focus
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        currentCardUpdated();
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
     * Class to handle the card flip click
     */
    private class CardFlipClickListener implements View.OnClickListener {

        /**
         * Overriden method that is called when the user clicks on the flip button
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (currentReviewType == ReviewType.TERM) {
                currentReviewType = ReviewType.DEFINITION;
            } else {
                currentReviewType = ReviewType.TERM;
            }

            currentCardUpdated();
        }
    }

    /**
     * Class to handle the next card click
     */
    private class NextCardClickListener implements View.OnClickListener {
        /**
         * Overriden method that is called when the user clicks on the next card button
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            // Get the next card
            IndexCard nextCard = deckController.getNextCard();
            if (nextCard != null) {
                // If we have a next card then update the fields
                currentIndexCard = nextCard;
                currentReviewType = reviewType;
                currentCardUpdated();
            } else {
                // If we have reached  the end of the deck
                // then show the message
                Toast toast = Toast.makeText(getBaseContext(), "All cards reviewed", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                // Show the reset button and hide the next card button
                bReviewCardNextCard.setVisibility(View.GONE);
                bReviewCardReset.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Class to handle the reset card click
     */
    private class ResetCardClickListener implements View.OnClickListener {

        /**
         * Overriden method that is called when the user clicks on the reset card button
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            // Reset the deck
            deckController.resetDeck();
            // Hide the reset button
            bReviewCardReset.setVisibility(View.GONE);
            // Show the next card button
            bReviewCardNextCard.setVisibility(View.VISIBLE);

            // Get the next card and update all the fields with the data
            IndexCard nextCard = deckController.getNextCard();
            currentIndexCard = nextCard;
            currentReviewType = reviewType;
            currentCardUpdated();
        }

    }
}
