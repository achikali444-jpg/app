package com.termux.tools.dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.termux.tools.R;

public class ContactUsDialog extends Dialog {

    private Context context;

    public ContactUsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_contact_us);

        // Initialize views
        ImageView imageView = findViewById(R.id.imageViewContactDialog);
        TextView textTitle = findViewById(R.id.textViewContactTitle);
        TextView textContent = findViewById(R.id.textViewContactContent);
        TextView textEmail = findViewById(R.id.textViewContactEmail);
        ImageView btnCopyEmail = findViewById(R.id.btnCopyContactEmail);
        TextView btnEmail = findViewById(R.id.btnSendEmail);
        TextView btnClose = findViewById(R.id.btnCloseContact);

        // Set data
        if (imageView != null) {
            imageView.setImageResource(R.drawable.home_img);
        }

        if (textTitle != null) {
            textTitle.setText("Contact Us");
        }

        if (textContent != null) {
            String contactText = "We'd love to hear from you!\n\n" +
                    "If you have any questions, suggestions, or need help with the app, please don't hesitate to reach out to us. Your feedback helps us improve the app and add new features.\n\n" +
                    "You can contact us through email or by using the direct email button below. We typically respond within 24-48 hours.";
            textContent.setText(contactText);
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

        // Send Email button click listener
        if (btnEmail != null) {
            btnEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmail();
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

    private void sendEmail() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:achikahmed.info@gmail.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Termux Tools & Commands App Feedback");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Achik,\n\nI would like to share some feedback about the app:");
            
            context.startActivity(Intent.createChooser(emailIntent, "Send email via:"));
        } catch (Exception e) {
            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}