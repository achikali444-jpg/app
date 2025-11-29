package com.termux.tools.model;

public class Tool {
	private String name;
	private String description;
	private String detailedInfo;
	private String installCommands;
	private String usageCommands;
	private int imageResource;
	
	public Tool(String name, String description, String detailedInfo, String installCommands, String usageCommands, int imageResource) {
		this.name = name;
		this.description = description;
		this.detailedInfo = detailedInfo;
		this.installCommands = installCommands;
		this.usageCommands = usageCommands;
		this.imageResource = imageResource;
	}
	
	// Getters
	public String getName() { return name; }
	public String getDescription() { return description; }
	public String getDetailedInfo() { return detailedInfo; }
	public String getInstallCommands() { return installCommands; }
	public String getUsageCommands() { return usageCommands; }
	public int getImageResource() { return imageResource; }
}