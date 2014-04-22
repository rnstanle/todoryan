package com.example.todoryan;

/**
 * Represents an item in a ToDo list
 */
public class NoteBook {

	/**
	 * Book title
	 */
	@com.google.gson.annotations.SerializedName("title")
	private String mTitle;
	
	/**
	 * Item Id
	 */
	@com.google.gson.annotations.SerializedName("id")
	private String mId;

	/**
	 * ToDoItem constructor
	 */
	public NoteBook() {

	}

	@Override
	public String toString() {
		return getTitle();
	}

	/**
	 * Initializes a new NoteBook
	 * 
	 * @param title
	 *            The item title
	 * @param id
	 *            The item id
	 */
	public NoteBook(String title, String id) {
		this.setId(id);
		this.setTitle(title);
	}

	/**
	 * Returns the item id
	 */
	public String getId() {
		return mId;
	}

	/**
	 * Sets the item id
	 * 
	 * @param id
	 *            id to set
	 */
	public final void setId(String id) {
		mId = id;
	}

	/**
	 * Returns the item title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Sets the item title
	 * 
	 * @param title
	 *            title to set
	 */
	public final void setTitle(String title) {
		mTitle = title;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NoteBook && ((NoteBook) o).mId == mId;
	}
}
