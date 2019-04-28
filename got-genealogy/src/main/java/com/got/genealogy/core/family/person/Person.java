package com.got.genealogy.core.family.person;

import com.got.genealogy.core.graph.property.Vertex;

import java.util.HashMap;
import java.util.Map;

import static com.got.genealogy.core.family.person.Gender.UNSPECIFIED;

/**
 * Person used as a Vertex in
 * FamilyTree.
 * <p>
 * Their name and gender are
 * stored in label and gender,
 * respectively.
 * <p>
 * Any extra information are
 * stored in a map, in upper
 * case formatting.
 */
public class Person extends Vertex {
    private Gender gender = UNSPECIFIED;
    private Map<String, String> details = new HashMap<>();

    /**
     * Person constructor, requiring
     * a name to be passed.
     *
     * @param label person's full name.
     */
    public Person(String label) {
        super(label); // name
    }

    /**
     * Returns the Gender enum, thus
     * forcing a valid gender, rather
     * than just a String object.
     *
     * @return Gender enum.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Set the person's gender, using
     * only valid Gender enums.
     *
     * @param gender change person's gender
     *               with a Gender enum.
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Returns a map of optional
     * details about the Person.
     * <p>
     * Every key is the detail
     * title and the value is
     * the detail itself.
     *
     * @return map of extra details about
     * a person.
     */
    public Map<String, String> getDetails() {
        return details;
    }

    /**
     * Gets a specific detail,
     * based on the key, ignoring
     * the case of the input String.
     *
     * @param key used to get a detail
     *            title from the details
     *            map.
     * @return the associated detail
     * under the key.
     */
    public String getDetail(String key) {
        return details.get(key.toUpperCase());
    }

    /**
     * Replace an existing detail,
     * ignoring the case of the input
     * String.
     *
     * @param key   of which the detail
     *              is to be replaced.
     * @param value is the new detail.
     */
    public void setDetail(String key, String value) {
        details.replace(
                key.toUpperCase(),
                value.toUpperCase());
    }

    /**
     * Add a new detail and store
     * it in upper-case format, to
     * disregard case-sensitivity.
     * <p>
     * The key is the detail title
     * and the value is the detail
     * itself.
     *
     * @param key   under which a new
     *              detail is to be
     *              added.
     * @param value is the new detail.
     */
    public void addDetail(String key, String value) {
        details.put(
                key.toUpperCase(),
                value.toUpperCase());
    }

    /**
     * Remove a detail under a specific
     * key.
     *
     * @param key of the detail to be
     *            removed.
     */
    public void removeDetail(String key) {
        details.remove(key.toUpperCase());
    }
}