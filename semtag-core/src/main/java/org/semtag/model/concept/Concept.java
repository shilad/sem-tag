package org.semtag.model.concept;

/**
 * @author Ari Weiland
 * @author Yulun Li
 */
public abstract class Concept<I> {
    protected final int conceptId;
    protected final String type;
    protected final I conceptObj;

    /**
     * Constructs a concept with given ID, type, and concept object.
     * @param conceptId
     * @param type
     * @param conceptObj
     */
    public Concept(int conceptId, String type, I conceptObj) {
        this.conceptId = conceptId;
        this.type = type;
        this.conceptObj = conceptObj;
    }

    /**
     * Constructs a concept with given ID, type, and concept object
     * extracted by the {@code bytesToConceptObj} method.
     * @param conceptId
     * @param type
     * @param objBytes
     */
    public Concept(int conceptId, String type, byte[] objBytes) {
        this.conceptId = conceptId;
        this.type = type;
        this.conceptObj = bytesToConceptObj(objBytes);
    }

    public int getConceptId() {
        return conceptId;
    }

    public String getType() {
        return type;
    }

    public I getConceptObj() {
        return conceptObj;
    }

    /**
     * Describes how to convert a conceptObj to a String.
     * Necessary for proper implementation of conceptObjToBytes().
     *
     * @return
     */
    protected abstract String conceptObjToString();

    /**
     * Describes how to convert a String back into a conceptObj.
     * Should reverse the conversion used by conceptObjToString().
     * Necessary for proper implementation of bytesToConceptObj().
     *
     * @param s
     * @return
     */
    protected abstract I stringToConceptObj(String s);

    /**
     * Converts the conceptObj to an array of bytes.
     *
     * @return
     */
    public byte[] conceptObjToBytes() {
        return conceptObjToString().getBytes();
    }

    /**
     * Converts an array of bytes back to a conceptObj.
     *
     * @param bytes
     * @return
     */
    public I bytesToConceptObj(byte[] bytes) {
        return stringToConceptObj(new String(bytes));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Concept)) return false;

        Concept concept = (Concept) o;

        return conceptId == concept.conceptId && type.equals(concept.type);

    }

    @Override
    public String toString() {
        return "Concept{" +
                "conceptId=" + conceptId +
                ", type=\'" + type + '\'' +
                ", conceptObj=" + conceptObj +
                '}';
    }
}
