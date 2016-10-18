package mobi.storedot.booklistingapp;

/**
 * An {@link Book} object contains information related to a single Book.
 */

public class Book {

    /**
     * Category/Topic of the book
     */
    private String mTopic;

    /**
     * Author of the book
     */
    private String mAuthor;

    /**
     * Title of the Book
     */
    private String mTitle;

    /**
     * Website URL of the book
     */
    private String mUrl;

    /**
     * Constructs a new {@link Book} object.
     *
     * @param topic  is the category in which the book/volume can be found
     * @param author is the author who wrote the book
     * @param title  is the given title of the book
     * @param url    is the website URL to find more details about the book
     */
    public Book(String topic, String author, String title, String url) {
        mTopic = topic;
        mAuthor = author;
        mTitle = title;
        mUrl = url;
    }

    /**
     * Returns the Topic/Category of the Book.
     */
    public String getTopic() {
        return mTopic;
    }

    /**
     * Returns the author of the Book.
     */
    public String getAuthor() {
        return mAuthor;
    }

    /**
     * Returns the title of the Book.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the website URL to find more information about the Book.
     */
    public String getUrl() {
        return mUrl;
    }
}
