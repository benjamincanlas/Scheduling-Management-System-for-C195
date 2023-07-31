package model;

public class Contacts {
    private int contactID;
    private String contactName;
    private String email;

    /**
     * Constructor for contacts
     * @param contactID
     * @param contactName
     * @param email
     */
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     *
     * @return contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     *
     * @param contactID contact ID setter
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     *
     * @return contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     *
     * @param contactName contact name setter
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email email setter
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return contact name with ID
     */
    @Override
    public String toString() {
        return contactName ;
    }


}
