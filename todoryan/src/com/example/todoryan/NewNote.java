package com.example.todoryan;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NewNote extends Activity {
	
	boolean isNewNote = false;
	
	private ToDoTable ToDoTable;
	
	String oldText;
	String oldId;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ToDoTable = new ToDoActivity.MyApp();
        // get the passed in text from original note
        Bundle extras = getIntent().getExtras();
        oldText = "Blank note...";
        if (extras != null) {
            oldText = extras.getString("old_text");
            oldId = extras.getString("old_id");
            isNewNote = false;
        }
        else
        {
        	isNewNote = true;
        }
        
        setContentView(R.layout.note_editor);
        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);
        
        EditText editText = (EditText)findViewById(R.id.editNoteText);
		editText.setText(oldText);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Create a new item
		ToDoItem item = new ToDoItem();			
		EditText mTextNewToDo = (EditText) findViewById(R.id.editNoteText);
		// hardcoded at the moment 
		item.setNoteBook("NOTEBOOK TITLE");
		// set the text of the note equal to the text of the EditText in the activity
		item.setText(mTextNewToDo .getText().toString());
		
		// truncate the title to a substring of the actual note
		if(item.getText().length() < 15)
			item.setTitle(mTextNewToDo.getText().toString().substring(0,item.getText().length()));
		else
			item.setTitle(mTextNewToDo.getText().toString().substring(0,15));
		
		// new note
		if(isNewNote == true)
		{
			// insert operation
			if(oldText == null)
				return;
			// Insert the new item
			ToDoTable.myToDoTable.insert(item, new TableOperationCallback<ToDoItem>() {
				public void onCompleted(ToDoItem entity, Exception exception, ServiceFilterResponse response) {
					if (exception == null) {
						// on success
					}
					else {
						// on failure
						// Add some kind of exception handling here, or atleast a message to the user
					}
					// regardless of what happens update the list
					ToDoActivity.refreshItemsFromTable();
				}
			});
		}	
		if(isNewNote == false)
		{
			// set the id of our local item to the same id as the existing note
			item.setId(oldId);
			// Update the item
			ToDoTable.myToDoTable.update(item, new TableOperationCallback<ToDoItem>() {
			public void onCompleted(ToDoItem entity, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {
					// success
				}
				else {
					// failure
				}
				//refresh items displayed in adapter
				ToDoActivity.refreshItemsFromTable();
				}
			});
		}
	}
}