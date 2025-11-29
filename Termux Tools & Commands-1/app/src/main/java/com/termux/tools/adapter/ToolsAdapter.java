package com.termux.tools.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.termux.tools.R;
import com.termux.tools.model.Tool;
import java.util.List;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ToolViewHolder> {
	
	private List<Tool> toolList;
	private OnItemClickListener listener;
	
	public interface OnItemClickListener {
		void onItemClick(Tool tool);
	}
	
	public ToolsAdapter(List<Tool> toolList, OnItemClickListener listener) {
		this.toolList = toolList;
		this.listener = listener;
	}
	
	@NonNull
	@Override
	public ToolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tool_card, parent, false);
		return new ToolViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ToolViewHolder holder, int position) {
		Tool tool = toolList.get(position);
		holder.bind(tool, listener);
	}
	
	@Override
	public int getItemCount() {
		return toolList.size();
	}
	
	static class ToolViewHolder extends RecyclerView.ViewHolder {
		private CardView cardView;
		private ImageView imageView;
		private TextView textTitle;
		private TextView textDescription;
		
		public ToolViewHolder(@NonNull View itemView) {
			super(itemView);
			cardView = itemView.findViewById(R.id.cardView);
			imageView = itemView.findViewById(R.id.imageViewTool);
			textTitle = itemView.findViewById(R.id.textViewTitle);
			textDescription = itemView.findViewById(R.id.textViewDescription);
		}
		
		public void bind(final Tool tool, final OnItemClickListener listener) {
			if (imageView != null) {
				imageView.setImageResource(tool.getImageResource());
			}
			if (textTitle != null) {
				textTitle.setText(tool.getName());
			}
			if (textDescription != null) {
				textDescription.setText(tool.getDescription());
			}
			
			if (cardView != null && listener != null) {
				cardView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						listener.onItemClick(tool);
					}
				});
			}
		}
	}
}