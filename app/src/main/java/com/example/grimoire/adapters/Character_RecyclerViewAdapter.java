package com.example.grimoire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grimoire.R;
import com.example.grimoire.models.CasterClassModel;
import com.example.grimoire.models.CharacterModel;
import com.example.grimoire.interfaces.RecyclerViewInterface;

import java.util.ArrayList;

public class Character_RecyclerViewAdapter extends RecyclerView.Adapter<Character_RecyclerViewAdapter.MyViewHolder> {



    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<CharacterModel> allCharacterModels;
    ArrayList<CasterClassModel> allClasses;

    public Character_RecyclerViewAdapter (Context context, ArrayList<CharacterModel> allCharacterModels,
                                      ArrayList<CasterClassModel> allClasses,
                                      RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.allCharacterModels = allCharacterModels;
        this.allClasses = allClasses;
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
        CasterClassModel casterClassModel = new CasterClassModel();
        int classId = allCharacterModels.get(position).getClassId();

        for (CasterClassModel c : allClasses)
            if (c.getId() == classId) {
                casterClassModel = c;
                break;
            }

        holder.tvClass.setText(casterClassModel.getName());
        holder.imageView.setImageResource(casterClassModel.getClassImage());
        holder.tvName.setText(allCharacterModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return allCharacterModels.size();
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
