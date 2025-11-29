package com.termux.tools.dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.termux.tools.R;

public class TermsOfServiceDialog extends Dialog {

    private Context context;

    public TermsOfServiceDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_terms_of_service);

        // Initialize views
        ImageView imageView = findViewById(R.id.imageViewTermsDialog);
        TextView textTitle = findViewById(R.id.textViewTermsTitle);
        TextView textContent = findViewById(R.id.textViewTermsContent);
        TextView textEmail = findViewById(R.id.textViewTermsEmail);
        ImageView btnCopyEmail = findViewById(R.id.btnCopyTermsEmail);
        TextView btnClose = findViewById(R.id.btnCloseTerms);

        // Set data
        if (imageView != null) {
            imageView.setImageResource(R.drawable.home_img);
        }

        if (textTitle != null) {
            textTitle.setText("Terms of Service");
        }

        if (textContent != null) {
            String termsText = "Last updated: November 2025\n\n" +
                    "Welcome to Termux Tools and Commands!\n" +
                    "By using this app, you agree to follow these simple terms and rules. Please read them carefully before using the app.\n\n" +
                    "🔹 1. About the App\n\n" +
                    "Termux Tools and Commands is a free Android app that helps users learn and use Termux commands and tools easily. The app gives basic information, usage, and examples to help users understand Termux better.\n\n" +
                    "This app is made and managed by Achik Ahmed.\n\n" +
                    "🔹 2. Using the App\n\n" +
                    "You can use this app for learning and personal use only. You are not allowed to:\n\n" +
                    "    • Copy, sell, or upload the app anywhere else\n" +
                    "    • Use the app for hacking or any illegal work\n" +
                    "    • Change or edit the app without permission\n\n" +
                    "The app is made only for educational and learning purposes.\n\n" +
                    "🔹 3. Ads and Third-Party Services\n\n" +
                    "We use Google AdMob to show ads in this app. AdMob may collect some non-personal data like your device info or advertising ID to show better ads.\n\n" +
                    "You can read Google's privacy policy here: 🔗 https://policies.google.com/privacy\n\n" +
                    "We do not control or take responsibility for the content shown in ads.\n\n" +
                    "🔹 4. No Guarantee\n\n" +
                    "We try to keep all Termux commands and tools working correctly, but sometimes tools or commands may not work on some devices. We are not responsible if something doesn't work as expected.\n\n" +
                    "This app is provided \"as is\", without any promises or guarantees.\n\n" +
                    "🔹 5. Limitation of Liability\n\n" +
                    "We are not responsible for:\n\n" +
                    "    • Any loss of data.\n" +
                    "    • Any problem caused by using the tools wrongly.\n" +
                    "    • Any misuse of Termux for hacking or illegal work.\n\n" +
                    "Use this app at your own risk.\n\n" +
                    "🔹 6. Updates and Changes\n\n" +
                    "We may update or change this app at any time to fix bugs, add new tools, or improve features.\n" +
                    "We may also update these Terms when needed. If we do, the new version will replace the old one inside the app and on our website.\n\n" +
                    "🔹 7. Contact Us\n\n" +
                    "If you have any questions or problems about these Terms, you can contact us anytime:\n\n";
            textContent.setText(termsText);
        }

        if (textEmail != null) {
            textEmail.setText("📧 achikahmed.info@gmail.com\n👤 Developer: Achik Ahmed");
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

        // Make links clickable
        setupClickableLinks();
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

    private void setupClickableLinks() {
        TextView textContent = findViewById(R.id.textViewTermsContent);
        if (textContent != null) {
            String fullText = textContent.getText().toString();
            SpannableString spannableString = new SpannableString(fullText);
            
            // Make Google Privacy Policy link clickable
            int googlePrivacyStart = fullText.indexOf("https://policies.google.com/privacy");
            if (googlePrivacyStart != -1) {
                int googlePrivacyEnd = googlePrivacyStart + "https://policies.google.com/privacy".length();
                ClickableSpan googlePrivacySpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        openUrl("https://policies.google.com/privacy");
                    }
                };
                spannableString.setSpan(googlePrivacySpan, googlePrivacyStart, googlePrivacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            
            textContent.setText(spannableString);
            textContent.setMovementMethod(LinkMovementMethod.getInstance());
            textContent.setTextIsSelectable(true);
        }
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Cannot open link", Toast.LENGTH_SHORT).show();
        }
    }
}