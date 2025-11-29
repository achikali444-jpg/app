package com.termux.tools;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.termux.tools.R;

public class ToolDetailActivity extends AppCompatActivity {
	
	private static final String TAG = "ToolDetailActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tool_detail);
		
		Log.d(TAG, "ToolDetailActivity started");
		
		// Setup toolbar with dark theme
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		// Enable back button
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		
		try {
			// Get data from intent with null checks
			String toolName = getIntent().getStringExtra("tool");
			String description = getIntent().getStringExtra("description");
			String detailedInfo = getIntent().getStringExtra("detailed_info");
			String installCommands = getIntent().getStringExtra("install_commands");
			String usageCommands = getIntent().getStringExtra("usage_commands");
			int imageResource = getIntent().getIntExtra("image_resource", R.drawable.ic_default);
			
			Log.d(TAG, "Received data - Tool: " + toolName);
			
			// Initialize views
			ImageView imageView = findViewById(R.id.imageViewToolDetail);
			TextView textViewTitle = findViewById(R.id.textViewToolTitle);
			TextView textViewDescription = findViewById(R.id.textViewToolDescription);
			TextView textViewDetailedInfo = findViewById(R.id.textViewDetailedInfo);
			TextView textViewInstallCommands = findViewById(R.id.textViewInstallCommands);
			TextView textViewUsageCommands = findViewById(R.id.textViewUsageCommands);
			
			// Set data with fallbacks
			if (imageView != null) {
				imageView.setImageResource(imageResource);
			}
			
			if (textViewTitle != null && toolName != null) {
				textViewTitle.setText(toolName);
				} else if (textViewTitle != null) {
				textViewTitle.setText("Unknown Tool");
			}
			
			if (textViewDescription != null && description != null) {
				textViewDescription.setText(description);
				} else if (textViewDescription != null) {
				textViewDescription.setText("No description available");
			}
			
			if (textViewDetailedInfo != null && detailedInfo != null) {
				textViewDetailedInfo.setText(detailedInfo);
				} else if (textViewDetailedInfo != null) {
				textViewDetailedInfo.setText("No detailed information available");
			}
			
			if (textViewInstallCommands != null && installCommands != null) {
				textViewInstallCommands.setText(installCommands);
				} else if (textViewInstallCommands != null) {
				textViewInstallCommands.setText("No installation commands available");
			}
			
			if (textViewUsageCommands != null && usageCommands != null) {
				textViewUsageCommands.setText(usageCommands);
				} else if (textViewUsageCommands != null) {
				textViewUsageCommands.setText("No usage commands available");
			}
			
			// Set toolbar title with tool name
			if (getSupportActionBar() != null && toolName != null) {
				getSupportActionBar().setTitle(toolName);
				} else if (getSupportActionBar() != null) {
				getSupportActionBar().setTitle("Tool Details");
			}
			
			Log.d(TAG, "Data set successfully");
			
			} catch (Exception e) {
			Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
			// Set default values if something goes wrong
			setDefaultValues();
		}
	}
	
	private void setDefaultValues() {
		try {
			TextView textViewTitle = findViewById(R.id.textViewToolTitle);
			TextView textViewDescription = findViewById(R.id.textViewToolDescription);
			TextView textViewDetailedInfo = findViewById(R.id.textViewDetailedInfo);
			TextView textViewInstallCommands = findViewById(R.id.textViewInstallCommands);
			TextView textViewUsageCommands = findViewById(R.id.textViewUsageCommands);
			
			if (textViewTitle != null) textViewTitle.setText("Tool Details");
			if (textViewDescription != null) textViewDescription.setText("Information not available");
			if (textViewDetailedInfo != null) textViewDetailedInfo.setText("Detailed information could not be loaded.");
			if (textViewInstallCommands != null) textViewInstallCommands.setText("Installation commands not available");
			if (textViewUsageCommands != null) textViewUsageCommands.setText("Usage commands not available");
			
			if (getSupportActionBar() != null) {
				getSupportActionBar().setTitle("Tool Details");
			}
			} catch (Exception e) {
			Log.e(TAG, "Error in setDefaultValues: " + e.getMessage());
		}
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}