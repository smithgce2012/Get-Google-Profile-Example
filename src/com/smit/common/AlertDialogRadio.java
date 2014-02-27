package com.smit.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class AlertDialogRadio extends DialogFragment {

	public AlertPositiveListener alertPositiveListener;

	public interface AlertPositiveListener {
		public void onPositiveClick(int position);
	}

	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		try {
			alertPositiveListener = (AlertPositiveListener) activity;
		} catch (ClassCastException e) {
			// The hosting activity does not implemented the interface
			// AlertPositiveListener
			throw new ClassCastException(activity.toString()
					+ " must implement AlertPositiveListener");
		}
	}

	OnClickListener positiveListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			AlertDialog alert = (AlertDialog) dialog;
			int position = alert.getListView().getCheckedItemPosition();
			alertPositiveListener.onPositiveClick(position);
		}
	};

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Bundle bundle = getArguments();
		int position = bundle.getInt("position");

		AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

		b.setTitle("Choose your Google Account");

		b.setSingleChoiceItems(StaticValue.accountarrs, position, null);

		b.setPositiveButton("OK", positiveListener);

		b.setNegativeButton("Cancel", null);

		AlertDialog d = b.show();

		

		TextView titleView = (TextView) d.findViewById(getResources()
				.getIdentifier("alertTitle", "id", "android"));
		if (titleView != null) {
			titleView.setGravity(Gravity.CENTER);
		}

		return d;
	}
}
