// HorizontalAdapter.java
package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.R;

import java.util.List;

public class ListInformationlAdapter extends RecyclerView.Adapter<ListInformationlAdapter.ViewHolder> {
    private List<String> data;

    public ListInformationlAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_information, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.information_button.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Button information_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            information_button = itemView.findViewById(R.id.information_button);
        }
    }
}
