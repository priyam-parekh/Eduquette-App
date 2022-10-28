package com.eduquette;

import com.eduquette.model.IndexDeck;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to hold all the index cards that are grouped by subject name
 */
public class DataController {
    // This variable holds all the index cards grouped by subject
    private static Map<String, IndexDeck> indexDecks = new HashMap<>();

    /**
     * Get the master list of index cards grouped by subjects
     *
     * @return the map of index cards
     */
    static Map<String, IndexDeck> getIndexDecks() {
        return indexDecks;
    }

    /**
     * Set the master list of index cards grouped by subjects
     *
     * @param indexDecks Map of indexDecks
     */
    public static void setIndexDecks(Map<String, IndexDeck> indexDecks) {
        DataController.indexDecks = indexDecks;
    }

    /**
     * Get the list of cards for the given subject
     *
     * @param subject The subject name
     * @return the index cards for the given subject
     */
    public static IndexDeck getIndexDeck(String subject) {
        return indexDecks.get(subject);
    }

}
