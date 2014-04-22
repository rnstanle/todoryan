package com.example.todoryan;

import android.app.Application;

import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

class ToDoTable extends Application {
		/**
		 * Mobile Service Table used to access data
		 */
		static MobileServiceTable<ToDoItem> myToDoTable;
	}
	