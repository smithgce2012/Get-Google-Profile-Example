package com.smit.common;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class StaticValue {

	public static final AlphaAnimation buttonClick = new AlphaAnimation(1F,
			0.5F);
	
	public static String[] accountarrs = null;

	public static void networkAlert(Context c) {
		Builder alert5 = new AlertDialog.Builder(c);
		alert5.setTitle("Internet Connection Problem");
		alert5.setMessage("Application requires a network connection. Please try again.");
		alert5.setCancelable(false);
		alert5.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		AlertDialog dialog = alert5.show();

		// Must call show() prior to fetching text view
		TextView messageView = (TextView) dialog
				.findViewById(android.R.id.message);
		messageView.setGravity(Gravity.CENTER);

		TextView titleView = (TextView) dialog.findViewById(c.getResources()
				.getIdentifier("alertTitle", "id", "android"));
		if (titleView != null) {
			titleView.setGravity(Gravity.CENTER);
		}

	}

	public static void serverAlert(Context c) {
		Builder alert5 = new AlertDialog.Builder(c);
		// alert5.setIcon(R.drawable.alert);
		alert5.setTitle("Sorry!");
		alert5.setMessage("Our server is being updated at the moment.  We apologize for the inconvenience.  It should be up shortly.  Please try again later.");
		alert5.setCancelable(false);
		alert5.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		AlertDialog dialog = alert5.show();

		// Must call show() prior to fetching text view
		TextView messageView = (TextView) dialog
				.findViewById(android.R.id.message);
		messageView.setGravity(Gravity.CENTER);

		TextView titleView = (TextView) dialog.findViewById(c.getResources()
				.getIdentifier("alertTitle", "id", "android"));
		if (titleView != null) {
			titleView.setGravity(Gravity.CENTER);
		}

	}

}
