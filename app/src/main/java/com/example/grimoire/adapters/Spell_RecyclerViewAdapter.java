package com.example.grimoire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grimoire.Helpers.DatabaseHelper;
import com.example.grimoire.R;
import com.example.grimoire.classes.ChosenSpell;
import com.example.grimoire.classes.Spell;
import com.example.grimoire.interfaces.RecyclerViewInterface;

import java.util.ArrayList;

public class Spell_RecyclerViewAdapter extends RecyclerView.Adapter<Spell_RecyclerViewAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<Spell> spells;
    int classImage;

    public Spell_RecyclerViewAdapter (Context context, ArrayList<Spell> spells, int classImage,
                                      RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.spells = spells;
        this.recyclerViewInterface = recyclerViewInterface;
        this.classImage = classImage;
    }

    @NonNull
    @Override
    public Spell_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.spell_recycler_view_row, parent, false);

        return new Spell_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Spell_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(spells.get(position).getName());
        holder.tvLevelAndSchool.setText(spells.get(position).getLevelAndSchool());
        holder.imageView.setImageResource(classImage);
    }

    @Override
    public int getItemCount() {
        return spells.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName, tvLevelAndSchool;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.classSymbol);
            tvName = itemView.findViewById(R.id.spellName);
            tvLevelAndSchool = itemView.findViewById(R.id.levelAndSchool);

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
