package com.termux.tools.dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.termux.tools.R;

public class AboutUsDialog extends Dialog {
	
	private Context context;
	
	public AboutUsDialog(@NonNull Context context) {
		super(context);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_about_us);
		
		// Initialize views
		ImageView imageView = findViewById(R.id.imageViewAboutDialog);
		TextView textTitle = findViewById(R.id.textViewAboutTitle);
		TextView textContent = findViewById(R.id.textViewAboutContent);
		TextView textEmail = findViewById(R.id.textViewAboutEmail);
		ImageView btnCopyEmail = findViewById(R.id.btnCopyEmail);
		TextView btnClose = findViewById(R.id.btnCloseAbout);
		
		// Set data
		if (imageView != null) {
			imageView.setImageResource(R.drawable.home_img);
		}
		
		if (textTitle != null) {
			textTitle.setText("About Us");
		}
		
		if (textContent != null) {
			String aboutText = "Welcome to Termux Tools and Commands!\n\n" +
			"This app helps you learn and use Termux in an easy way. You can find many Termux tools and their commands here. Each tool has a short note that tells you what it does and how to use it.\n\n" +
			"Our goal is to make learning Termux simple and fun. Whether you are new or already know a little about Termux, this app will help you grow your skills. You can use it to learn commands, try tools, and understand how Termux works on your phone.\n\n" +
			"This app is made and cared for by Achik Ahmed. The aim of this app is to help everyone understand Termux step by step.\n\n" +
			"We keep updating the app to add more tools and new commands. Your feedback helps us make the app better every day.\n\n" +
			"If you like this app, please give it 5 stars and tell your friends about it.";
			textContent.setText(aboutText);
		}
		
		if (textEmail != null) {
			textEmail.setText("📧 achikahmed.info@gmail.com");
		}
		
		// Copy Email button click listener
		if (btnCopyEmail != null) {
			btnCopyEmail.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					copyEmailToClipboard();
				}
			});
		}
		
		// Close button click listener
		if (btnClose != null) {
			btnClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}
	
	private void copyEmailToClipboard() {
		try {
			String email = "achikahmed.info@gmail.com";
			ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("Email", email);
			clipboard.setPrimaryClip(clip);
			Toast.makeText(context, "Email copied to clipboard", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
			Toast.makeText(context, "Failed to copy email", Toast.LENGTH_SHORT).show();
		}
	}
}