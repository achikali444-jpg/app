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

public class ToolInfoDialog extends Dialog {

    private Context context;
    private String toolName;
    private String description;
    private String detailedInfo;
    private String installCommands;
    private String usageCommands;
    private int imageResource;

    public ToolInfoDialog(@NonNull Context context, String toolName, String description, 
                         String detailedInfo, String installCommands, String usageCommands, 
                         int imageResource) {
        super(context);
        this.context = context;
        this.toolName = toolName;
        this.description = description;
        this.detailedInfo = detailedInfo;
        this.installCommands = installCommands;
        this.usageCommands = usageCommands;
        this.imageResource = imageResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_tool_info);

        // Initialize views
        ImageView imageView = findViewById(R.id.imageViewToolDialog);
        TextView textTitle = findViewById(R.id.textViewToolTitleDialog);
        TextView textDescription = findViewById(R.id.textViewToolDescriptionDialog);
        TextView textDetailedInfo = findViewById(R.id.textViewDetailedInfoDialog);
        TextView textInstallCommands = findViewById(R.id.textViewInstallCommandsDialog);
        TextView textUsageCommands = findViewById(R.id.textViewUsageCommandsDialog);
        ImageView btnCopyInstall = findViewById(R.id.btnCopyInstall);
        ImageView btnCopyUsage = findViewById(R.id.btnCopyUsage);
        TextView btnClose = findViewById(R.id.btnCloseDialog);

        // Set data
        if (imageView != null) imageView.setImageResource(imageResource);
        if (textTitle != null) textTitle.setText(toolName);
        if (textDescription != null) textDescription.setText(description);
        if (textDetailedInfo != null) textDetailedInfo.setText(detailedInfo);
        if (textInstallCommands != null) textInstallCommands.setText(installCommands);
        if (textUsageCommands != null) textUsageCommands.setText(usageCommands);

        // Make command text selectable and copyable
        if (textInstallCommands != null) {
            textInstallCommands.setTextIsSelectable(true);
        }
        if (textUsageCommands != null) {
            textUsageCommands.setTextIsSelectable(true);
        }

        // Copy Installation Commands button
        if (btnCopyInstall != null) {
            btnCopyInstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyToClipboard(installCommands, "Installation commands copied to clipboard");
                }
            });
        }

        // Copy Usage Commands button
        if (btnCopyUsage != null) {
            btnCopyUsage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyToClipboard(usageCommands, "Usage commands copied to clipboard");
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

    private void copyToClipboard(String text, String message) {
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Termux Commands", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to copy to clipboard", Toast.LENGTH_SHORT).show();
        }
    }
}