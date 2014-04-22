package com.example.todoryan;

import static com.microsoft.windowsazure.mobileservices.MobileServiceQueryOperations.*;

import java.net.MalformedURLException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

public class ToDoActivity extends Activity {

	/**
	 * Mobile Service Client reference
	 */
	private MobileServiceClient mClient;

	/**
	 * Mobile Service Table used to access data
	 */
	//private MobileServiceTable<ToDoItem> mToDoTable;
	//private MyApp ToDoTable;
	
	/**
	 * Adapter to sync the items list with the view
	 */
	private static ToDoItemAdapter mAdapter;

	private static NoteBookAdapter mBookAdapter;
	/**
	 * EditText containing the "New ToDo" text
	 */
	private EditText mTextNewToDo;

	/**
	 * EditText containing the note's text
	 */
	private EditText mNoteText;
	/**
	 * Progress spinner to use for table operations
	 */
	private ProgressBar mProgressBar;

	/**
	 * Initializes the activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);
		
		/**************** create a notebook */
		/********************/
		
		final Button button;
		
		mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

		// Initialize the progress bar
		mProgressBar.setVisibility(ProgressBar.GONE);
		
		try {
			// Create the Mobile Service Client instance, using the provided
			// Mobile Service URL and key
			mClient = new MobileServiceClient(
					"https://todoryan.azure-mobile.net/",
					"viZsdOaRlZiWqPkyppKkKudUTXkEwk41",
					this).withFilter(new ProgressFilter());
			
/*			// this code is unimplemented, it was only used to create an additional table
			NoteBook item = new NoteBook();
			NoteBookTable.myBookTable = mClient.getTable(NoteBook.class);
			
			item.setTitle("NOTEBOOK TITLE");
			
			NoteBookTable.myBookTable.insert(item, new TableOperationCallback<NoteBook>() {
			      public void onCompleted(NoteBook entity, Exception exception, ServiceFilterResponse response) {
			            if (exception == null) {
			                  // Insert succeeded
			            } else {
			                  // Insert failed
			            	Toast.makeText(getApplicationContext(), exception.getCause().toString(), Toast.LENGTH_SHORT).show();
			            }
			      }
			});
	*/		
			// Get the Mobile Service Table instance to use
			ToDoTable.myToDoTable = mClient.getTable("TodoItem",ToDoItem.class);
			// Bind this to the EditText 
			mTextNewToDo = (EditText) findViewById(R.id.textNewToDo);
			// Create an adapter to bind the items with the view
			mAdapter = new ToDoItemAdapter(this, R.layout.row_layout);
			ListView listViewToDo = (ListView) findViewById(R.id.listViewToDo);
			listViewToDo.setAdapter(mAdapter);
			// Load the items from the Mobile Service
			refreshItemsFromTable();
		} catch (MalformedURLException e) {
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
	}
	
	/**
	 * Initializes the activity menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/**
	 * Select an option from the menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_refresh) {
			refreshItemsFromTable();
		}
		
		if (item.getItemId() == R.id.menu_add){
			// start activity used to create/edit a note
			Intent editNote = new Intent(this, NewNote.class);
			startActivity(editNote);
		}
		return true;
	}
	
	/**
	 * Refresh the list with the items in the Mobile Service Table
	 */
	public static void refreshItemsFromTable() {

		// Get the items that weren't marked as completed and add them in the adapter
		ToDoTable.myToDoTable.execute(new TableQueryCallback<ToDoItem>() {
		//mToDoTable.where().field("complete").eq(val(false)).execute(new TableQueryCallback<ToDoItem>() {
			public void onCompleted(List<ToDoItem> result, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {
					// success
					mAdapter.clear();
					for (ToDoItem item : result) {
						mAdapter.add(item);
					}
				} else {
					// error
					createAndShowDialog(exception, "Error");
				}
			}
		});
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param exception
	 *            The exception to show in the dialog
	 * @param title
	 *            The dialog title
	 */
	private static void createAndShowDialog(Exception exception, String title) {
		Throwable ex = exception;
		if(exception.getCause() != null){
			ex = exception.getCause();
		}
		createAndShowDialog(ex.getMessage(), title);
	}

	/**
	 * Creates a dialog and shows it
	 * 
	 * @param message
	 *            The dialog message
	 * @param title
	 *            The dialog title
	 */
	private static void createAndShowDialog(String message, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ToDoItemAdapter.mContext);

		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
	/**
	 * Animates progressBar while updating data
	 */
	private class ProgressFilter implements ServiceFilter {
		@Override
		public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
				final ServiceFilterResponseCallback responseCallback) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
				}
			});
			
			nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {
				@Override
				public void onResponse(ServiceFilterResponse response, Exception exception) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.GONE);
						}
					});
					
					if (responseCallback != null)  responseCallback.onResponse(response, exception);
				}
			});
		}
	}
}