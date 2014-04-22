package com.example.todoryan;

import android.app.Application;

import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

class NoteBookTable extends Application {
		/**
		 * Mobile Service Table used to access data
		 */
		static MobileServiceTable<NoteBook> myBookTable;
		// Get the Mobile Service Table instance to use
		//myToDoTable = mClient.getTable(ToDoItem.class);
		public MobileServiceTable<NoteBook> getTable()
		{
			return myBookTable;
		}
		public void setTable(MobileServiceTable<NoteBook> tbl)
		{
			myBookTable = tbl;
		}
	}
	