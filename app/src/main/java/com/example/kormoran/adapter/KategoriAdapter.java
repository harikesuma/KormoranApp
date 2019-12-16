package com.example.kormoran.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kormoran.KategoriQuestionActivity;
import com.example.kormoran.R;
import com.example.kormoran.data.Kategori;
import com.squareup.picasso.Picasso;

import java.util.List;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ViewHolder> {

    private List<Kategori> kategoriList;
    private Context mContext;

    public KategoriAdapter(Context mContext,List<Kategori> kategoriList) {
        this.kategoriList = kategoriList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public KategoriAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_kategori, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriAdapter.ViewHolder holder, int position) {
        final int kategoriId = kategoriList.get(position).getId();
        final String kategoriName = kategoriList.get(position).getKategori();

        String url = "https://kormoran.000webhostapp.com/storage/kategori/"+kategoriList.get(position).getPict();

        Picasso.get()
                .load(url)
                .resize(200, 200)
                .into(holder.ivPict, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(mContext, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(mContext, "Failed to load", Toast.LENGTH_LONG).show();
                    }
                });

        holder.tvKategori.setText(kategoriList.get(position).getKategori());
        holder.cvKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, KategoriQuestionActivity.class);
                i.putExtra("ID_KATEGORI",kategoriId);
                i.putExtra("NAMA_KATEGORI",kategoriName);
                Toast.makeText(mContext, "ID = "+kategoriId, Toast.LENGTH_LONG).show();
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (kategoriList != null) ? kategoriList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPict;
        TextView tvKategori;
        CardView cvKategori;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPict = itemView.findViewById(R.id.iv_pict);
            tvKategori = itemView.findViewById(R.id.tv_kategori);
            cvKategori = itemView.findViewById(R.id.cv_kategori);
        }
    }
}
