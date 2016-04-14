package com.example.imonitor_collect.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.imonitor_collect.R;



public class QRCodeDialog extends Dialog {

	public QRCodeDialog(Context context) {
		super(context);
	}

	public QRCodeDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private ImageView QrcodeIV;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private String positiveButtonText;
		private String negativeButtonText;

		public Builder(Context context) {
			this.context = context;
		}

		public void setQrCode(ImageView iv) {
			QrcodeIV = iv;
		}
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public QRCodeDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final QRCodeDialog dialog = new QRCodeDialog(context,R.style.Custom_Dialog);
			View layout = inflater.inflate(R.layout.dialog_qrcode, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			((Button) layout.findViewById(R.id.positiveButton))
			.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					positiveButtonClickListener.onClick(dialog,
							DialogInterface.BUTTON_POSITIVE);
				}
			});
			((Button) layout.findViewById(R.id.negativeButton))
			.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					negativeButtonClickListener.onClick(dialog,
							DialogInterface.BUTTON_NEGATIVE);
				}
			});
			((RelativeLayout) layout.findViewById(R.id.content)).removeAllViews();
			((RelativeLayout) layout.findViewById(R.id.content)).addView(QrcodeIV);

			dialog.setContentView(layout);
			return dialog;
		}
	}
}