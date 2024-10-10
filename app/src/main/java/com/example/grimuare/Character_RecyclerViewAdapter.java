package com.example.grimuare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Character_RecyclerViewAdapter extends RecyclerView.Adapter<Character_RecyclerViewAdapter.MyViewHolder> {



    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<Character> allCharacters;

    public Character_RecyclerViewAdapter (Context context, ArrayList<Character> allCharacters,
                                      RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.allCharacters = allCharacters;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public Character_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.character_recycler_view_row, parent, false);

        return new Character_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Character_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(allCharacters.get(position).getName());
        holder.tvClass.setText(allCharacters.get(position).getMainClass());
        holder.imageView.setImageResource(allCharacters.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return allCharacters.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName, tvClass;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.characterClassSymbol);
            tvName = itemView.findViewById(R.id.characterName);
            tvClass = itemView.findViewById(R.id.characterClass);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION)
                        recyclerViewInterface.onItemClick(pos);
                }
            });

            itemView.setOnLongClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION)
                        recyclerViewInterface.onItemLongClick(pos);
                }
                return true;
            });
        }
    }
}
