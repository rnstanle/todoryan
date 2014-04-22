package com.example.todoryan;

/**
 * Represents an item in a ToDo list
 */
public class ToDoItem {

	/**
	 * Item title
	 */
	@com.google.gson.annotations.SerializedName("title")
	private String mTitle;
	
	/**
	 * Item text
	 */
	@com.google.gson.annotations.SerializedName("text")
	private String mText;

	/**
	 * Item Id
	 */
	@com.google.gson.annotations.SerializedName("id")
	private String mId;

	/**
	 * Indicates if the item is completed
	 */
	@com.google.gson.annotations.SerializedName("complete")
	private boolean mComplete;

	/**
	 * Indicate the book this note belongs to
	 */
	@com.google.gson.annotations.SerializedName("notebook")
	private String mNoteBook;
	
	/**
	 * ToDoItem constructor
	 */
	public ToDoItem() {

	}
	
	@Override
	public String toString() {
		return getText();
	}

	/**
	 * Initializes a new ToDoItem
	 * 
	 * @param text
	 *            The item text
	 * @param id
	 *            The item id
	 */
	public ToDoItem(String title, String text, String id, String notebook) {
		this.setText(text);
		this.setId(id);
		this.setTitle(title);
		this.setNoteBook(notebook);
	}

	/**
	 * Sets the item text
	 * 
	 * @param text
	 *            text to set
	 */
	public final void setText(String text) {
		mText = text;
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
	/**
	 * Indicates if the item is marked as completed
	 */
	public boolean isComplete() {
		return mComplete;
	}

	/**
	 * Marks the item as completed or in-completed
	 */
	public void setComplete(boolean complete) {
		mComplete = complete;
	}

	/**
	 * Returns the item text
	 */
	public String getText() {
		return mText;
	}

	/**
	 * Sets the item notebook
	 * 
	 * @param notebook
	 *            notebook this note belongs to
	 */
	public final void setNoteBook(String notebook) {
		mNoteBook = notebook;
	}

	/**
	 * Returns the item notebook name
	 */
	public String getNoteBook() {
		return mNoteBook;
	}
	@Override
	public boolean equals(Object o) {
		return o instanceof ToDoItem && ((ToDoItem) o).mId == mId;
	}
}
