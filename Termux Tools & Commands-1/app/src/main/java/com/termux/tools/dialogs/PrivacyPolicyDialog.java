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

public class PrivacyPolicyDialog extends Dialog {

    private Context context;

    public PrivacyPolicyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_privacy_policy);

        // Initialize views
        ImageView imageView = findViewById(R.id.imageViewPrivacyDialog);
        TextView textTitle = findViewById(R.id.textViewPrivacyTitle);
        TextView textContent = findViewById(R.id.textViewPrivacyContent);
        TextView textEmail = findViewById(R.id.textViewPrivacyEmail);
        ImageView btnCopyEmail = findViewById(R.id.btnCopyPrivacyEmail);
        TextView btnClose = findViewById(R.id.btnClosePrivacy);

        // Set data
        if (imageView != null) {
            imageView.setImageResource(R.drawable.home_img);
        }

        if (textTitle != null) {
            textTitle.setText("Privacy Policy");
        }

        if (textContent != null) {
            String privacyText = "Last updated: November 2025\n\n" +
                    "Welcome to Termux Tools and Commands! This app is made and managed by Achik Ahmed. Your privacy is very important to us, and we want you to understand how we use your information.\n\n" +
                    "🔹 Information We Collect\n\n" +
                    "Our app does not ask for your name, phone number, or personal data. However, we use Google AdMob to show ads. AdMob may collect some non-personal information like:\n\n" +
                    "   • Your device type (Android version, model, etc.)\n" +
                    "   • Advertising ID\n" +
                    "   • App usage data (to show better ads)\n\n" +
                    "This information is collected by Google, not by us. You can learn more about how Google uses this data here: 🔗https://policies.google.com/privacy.\n\n" +
                    "🔹 How We Use Information\n\n" +
                    "We use AdMob ads to earn money and keep this app free for everyone. We never sell, share, or misuse your personal data. The small amount of data collected by Google AdMob helps to:\n\n" +
                    "    • Show safe and relevant ads\n\n" +
                    "    • Improve ad performance\n\n" +
                    "🔹 Third-Party Services\n\n" +
                    "This app uses Google AdMob, which follows its own privacy rules. AdMob may use cookies or device IDs to serve better ads.\n\n" +
                    "Read AdMob's privacy policy here: 🔗https://support.google.com/admob/answer/6128543\n\n" +
                    "🔹 Children's Privacy\n\n" +
                    "This app is made for general users and does not target children under 13. We do not collect any information from kids knowingly.\n\n" +
                    "🔹 Security\n\n" +
                    "We try our best to keep your data safe. Since we do not collect personal data, there is very little risk of misuse.\n\n" +
                    "🔹 Changes to This Policy\n\n" +
                    "We may update this Privacy Policy from time to time. When we do, the new version will replace the old one inside the app and on our website.\n\n" +
                    "🔹 Contact Us\n\n" +
                    "If you have any questions about this Privacy Policy, feel free to contact us:\n\n";
            textContent.setText(privacyText);
        }

        if (textEmail != null) {
            textEmail.setText("📧 achikahmed.info@gmail.com 👤Developer: Achik Ahmed");
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
        TextView textContent = findViewById(R.id.textViewPrivacyContent);
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
            
            // Make AdMob Privacy Policy link clickable
            int admobPrivacyStart = fullText.indexOf("https://support.google.com/admob/answer/6128543");
            if (admobPrivacyStart != -1) {
                int admobPrivacyEnd = admobPrivacyStart + "https://support.google.com/admob/answer/6128543".length();
                ClickableSpan admobPrivacySpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        openUrl("https://support.google.com/admob/answer/6128543");
                    }
                };
                spannableString.setSpan(admobPrivacySpan, admobPrivacyStart, admobPrivacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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