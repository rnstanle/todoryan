package com.example.todoryan;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter to bind a ToDoItem List to a view
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem>{

	/**
	 * Adapter context
	 */
	static Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;

	public ToDoItemAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);

		mContext = context;
		mLayoutResourceId = layoutResourceId;
	}

	/**
	 * Returns the view for a specific item on the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		final ToDoItem currentItem = getItem(position);
		final String old_text = currentItem.getText();
		final String old_id = currentItem.getId();
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}

		row.setTag(currentItem);
		//currentItem.setText(currentItem.getText());
		final TextView textView = (TextView) row.findViewById(R.id.textViewItem);
		textView.setText(currentItem.getTitle());
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Do you want to delete this note?");
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// user clicked OK
				ToDoItem item = new ToDoItem();
				item.setId(old_id);
				ToDoTable.myToDoTable.delete(item,  new TableDeleteCallback(){
					@Override
					public void onCompleted(Exception arg0,
							ServiceFilterResponse arg1) {
						// TODO Auto-generated method stub
						ToDoActivity.refreshItemsFromTable();
					}});
			}	
		});
		builder.setNegativeButton(R.string.cancel,  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// user cancelled the dialog
				
			}
		});
		textView.setEnabled(true);
		textView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				AlertDialog dialog = builder.create();
				dialog.show();
				return false;
			}
		});
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getContext(), textView.getText(), Toast.LENGTH_SHORT).show();
				
				Intent editNote = new Intent(mContext, NewNote.class);
				editNote.putExtra("old_text", old_text);
				editNote.putExtra("old_id", old_id);
				mContext.startActivity(editNote);
				
			/*	ToDoActivity activity = (ToDoActivity) mContext;
				
				activity.setContentView(R.layout.note_editor);
				EditText editText = (EditText) activity.findViewById(R.id.editNoteText);
				editText.setText(textView.getText());
				*/
			}
		});
		/*final CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkToDoItem);
		
		checkBox.setText(currentItem.getText());
		checkBox.setChecked(false);
		checkBox.setEnabled(true);

		checkBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkBox.isChecked()) {
					checkBox.setEnabled(false);
					if (mContext instanceof ToDoActivity) {
						ToDoActivity activity = (ToDoActivity) mContext;
						activity.checkItem(currentItem);
					}
				}
			}
		});*/

		return row;
	}
}
