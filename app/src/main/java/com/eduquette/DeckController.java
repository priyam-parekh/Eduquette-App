package com.eduquette;

import com.eduquette.model.IndexCard;
import com.eduquette.model.IndexDeck;
import com.eduquette.model.ReviewOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used to control the deck of cards during review
 */
public class DeckController {
    // List of index cards to be used
    private List<IndexCard> indexCards;

    // Working copy of list of index cards
    private List<IndexCard> workingCards;

    // Variable to hold the current review order
    private ReviewOrder reviewOrder;

    // Variable to hold the current index in the review process
    private int currentIndex = -1;

    /**
     * This is the constructor for the deck controller
     *
     * @param indexDeck   The index deck to use
     * @param reviewOrder The review order type to use
     */
    public DeckController(IndexDeck indexDeck, ReviewOrder reviewOrder) {
        // Make a copy of all the given index cards
        this.indexCards = new ArrayList<>();
        this.indexCards.addAll(indexDeck.getIndexCards());

        // Make a copy of all the give index cards to use during review
        this.workingCards = new ArrayList<>();
        this.workingCards.addAll(indexDeck.getIndexCards());

        this.reviewOrder = reviewOrder;
        resetDeck();
    }

    /**
     * Reset the controller to the default state
     */
    public void resetDeck() {
        currentIndex = -1;
        workingCards.clear();
        workingCards.addAll(indexCards);
    }

    /**
     * Retrieve the next card based on the review order
     *
     * @return the next card to be reviewed
     */
    public IndexCard getNextCard() {
        IndexCard nextCard = null;
        if (reviewOrder == ReviewOrder.IN_ORDER) {
            // If the review order is "IN_ORDER"
            currentIndex++;
            if (currentIndex < workingCards.size()) {
                nextCard = workingCards.get(currentIndex);
            }
        } else {
            // If the review order is "RANDOM"
            if (workingCards.size() > 0) {
                // Get a random card from the working deck
                Random random = new Random();
                currentIndex = random.nextInt(workingCards.size());

                // Remove the chosen card from the working deck
                nextCard = workingCards.remove(currentIndex);
            }
        }

        return nextCard;
    }
}
