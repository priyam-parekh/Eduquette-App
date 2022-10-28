package com.eduquette.model;

import java.io.Serializable;
import java.util.List;

/**
 * Class to represent the Index Deck
 */
public class IndexDeck implements Serializable {
    public static String ID = "INDEX_DECK";

    // Subject of the index deck
    private String subject;

    // The string image of the deck
    private String image;

    // The resource index for the image of the deck
    private Integer thumbnail;

    // The list of index cards in the deck
    private List<IndexCard> indexCards;

    // The count of cards in the deck
    private Integer count;

    // The display string for the count of cards in the deck
    private String countDisplayString;

    /**
     * The constructor for the index deck
     *
     * @param subject   The subject of the deck
     * @param thumbnail The resource index for the image of the deck
     */
    public IndexDeck(String subject, int thumbnail) {
        this.subject = subject;
        this.thumbnail = thumbnail;
        this.count = 0;
        this.countDisplayString = String.format("(%d cards)", count);
    }

    /**
     * Get the subject of the deck
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the subject for the deck
     *
     * @param subject The subject to be set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get the resource index for the image of the deck
     *
     * @return the resource index for the image of the deck
     */
    public int getThumbnail() {
        return thumbnail;
    }

    /**
     * Set the resource index for the image of the deck
     *
     * @param thumbnail the resource index for the image of the deck
     */
    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Get the count of the index cards in the deck
     *
     * @return the count of index cards
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Set the count of the index cards in the deck
     *
     * @param count the count of index cards
     */
    public void setCount(Integer count) {
        this.count = count;
        countDisplayString = String.format("(%d %s)", count, count == 1 ? "card" : "cards");
    }

    /**
     * Get the display string for the count
     *
     * @return the count display string
     */
    public String getCountDisplayString() {
        return countDisplayString;
    }

    /**
     * Get the image for the deck
     *
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Set the image for the deck
     *
     * @param image the image to be set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Get the list of index cards in the deck
     *
     * @return the list of index cards
     */
    public List<IndexCard> getIndexCards() {
        return indexCards;
    }

    /**
     * Set the list of index cards in the deck
     *
     * @param indexCards the list of index cards to be set
     */
    public void setIndexCards(List<IndexCard> indexCards) {
        this.indexCards = indexCards;
        setCount(indexCards.size());
    }

    /**
     * Update the count based on the list of cards in the deck
     */
    public void refreshCount() {
        setCount(indexCards.size());
    }
}
