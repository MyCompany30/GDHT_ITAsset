package com.gdht.itasset.widget;

import com.gdht.itasset.R;

import android.app.Dialog;
import android.content.Context;

public class WaitingDialog extends Dialog {
	public WaitingDialog(Context context) {
		super(context);
	}
	public WaitingDialog(Context context, int transdialog) {
		super(context,transdialog);
	}
	protected WaitingDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	@Override
	public void show() {
		this.setContentView(R.layout.waiting_view);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.setCanceledOnTouchOutside(false);
		super.show();
	}
}
