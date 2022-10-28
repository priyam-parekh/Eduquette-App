package com.eduquette.model;

import java.io.Serializable;

/**
 * Class to represent the Index Card
 */
public class IndexCard implements Serializable {
    // Instance variables for all attributes of Index Card
    private Integer id;
    private String term;
    private String definition;

    /**
     * This is the constructor for the Index Card
     *
     * @param id         The id of the index card
     * @param term       The term of the index card
     * @param definition The definition of the index card
     */
    public IndexCard(Integer id, String term, String definition) {
        this.id = id;
        this.term = term;
        this.definition = definition;
    }

    /**
     * Get the id of the index card
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the id of the index card
     *
     * @param id the id to be set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the term of the index card
     *
     * @return The term
     */
    public String getTerm() {
        return term;
    }

    /**
     * Set the term of the index card
     *
     * @param term the term to be set
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * Get the definition of the index card
     *
     * @return The definition
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Set the definition of the index card
     *
     * @param definition the definition to be set
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
