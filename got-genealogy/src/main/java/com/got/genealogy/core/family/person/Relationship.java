package com.got.genealogy.core.family.person;

/**
 * All valid genderless family relationships,
 * used directly by the InformationPool and
 * Genealogy processor.
 * <p>
 * Only CHILD, PARENT and SPOUSE are used to
 * determine an official relationship, which
 * is to be stored under label in Relation.
 * <p>
 * Every other relationship is used for family
 * relationship output strings.
 */
public enum Relationship {
    ASCENDANT_COUSIN,
    AUNT_OR_UNCLE,
    CHILD,
    COUSIN,
    DESCENDANT_COUSIN,
    GRANDAUNT_OR_UNCLE,
    GRANDCHILD,
    GRANDNIECE_OR_NEPHEW,
    GRANDPARENT,
    GREAT_GRANDAUNT_OR_UNCLE,
    GREAT_GRANDCHILD,
    GREAT_GRANDNIECE_OR_NEPHEW,
    GREAT_GRANDPARENT,
    HALF_SIBLING,
    NIECE_OR_NEPHEW,
    PARENT,
    SIBLING,
    SPOUSE,
    STEP_CHILD,
    STEP_PARENT
}

