package com.example.kormoran.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kormoran.R;
import com.example.kormoran.data.Achivement;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {
    List<Achivement> achivementList;
    private Context context;

    public AchievementAdapter(Context context, List<Achivement> achivementList) {
        this.achivementList = achivementList;
        this.context = context;
    }

    @NonNull
    @Override
    public AchievementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_achivement_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementAdapter.ViewHolder holder, int position) {
        holder.ivTrophy.setImageResource(R.drawable.trophy);
        holder.tvTrophy.setText(achivementList.get(position).getNama());
        holder.tvDesc.setText(achivementList.get(position).getDeskripsi());
    }

    @Override
    public int getItemCount() {
        return (achivementList != null) ? achivementList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTrophy;
        TextView tvTrophy;
        TextView tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivTrophy = itemView.findViewById(R.id.iv_trophy);
            tvTrophy = itemView.findViewById(R.id.tv_trophy);
            tvDesc = itemView.findViewById(R.id.tv_desc);

        }
    }
}
