package com.example.grimoire.adapters;

import android.annotation.SuppressLint;
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
import com.example.grimoire.models.SpellModel;
import com.example.grimoire.interfaces.RecyclerViewInterface;

import java.util.ArrayList;

public class Spell_RecyclerViewAdapter extends RecyclerView.Adapter<Spell_RecyclerViewAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    private final Context context;
    private final ArrayList<SpellModel> spellModels;
    private final ArrayList<SpellModel> spellModelsFiltered;
    private final ArrayList<SpellModel> spellModelsFull;
    private final int classImage;
    private final DatabaseHelper dbHelper;

    public Spell_RecyclerViewAdapter (Context context, DatabaseHelper dbHelper,
                                      ArrayList<SpellModel> spellModels, int classImage,
                                      RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.spellModels = spellModels;
        this.spellModelsFiltered = new ArrayList<>(spellModels);
        this.spellModelsFull = new ArrayList<>(spellModels);
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
        holder.tvName.setText(spellModels.get(position).getName());
        holder.tvLevelAndSchool.setText(spellModels.get(position).getLevelAndSchool(dbHelper));
        holder.imageView.setImageResource(classImage);
    }

    @Override
    public int getItemCount() {
        return spellModels.size();
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

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String searchedText) {
        spellModels.clear();
        if (searchedText.isEmpty()) {
            spellModels.addAll(spellModelsFiltered);
        } else {
            searchedText = searchedText.toLowerCase();
            for (SpellModel item : spellModelsFiltered) {
                if (item.getName().toLowerCase().contains(searchedText)) {
                    spellModels.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void applyFilters(int ritualFilter, int concentrationFilter, boolean[] levels,
                             int vFilter, int sFilter, int mFilter, int[] schoolIds) {
        ArrayList<SpellModel> filteredList = new ArrayList<>();
        for (SpellModel item : spellModelsFull) {

            boolean matchesRitual = (ritualFilter == R.id.radioAnyRitual)
                    || (ritualFilter == R.id.radioYesRitual && item.isRitual())
                    || (ritualFilter == R.id.radioNoRitual && !item.isRitual());

            boolean matchesConcentration = (concentrationFilter == R.id.radioAnyConcentration)
                    || (concentrationFilter == R.id.radioYesConcentration && item.isConcentration())
                    || (concentrationFilter == R.id.radioNoConcentration && !item.isConcentration());

            boolean matchesV = (vFilter == R.id.radioAnyV)
                    || (vFilter == R.id.radioYesV && item.isV())
                    || (vFilter == R.id.radioNoV && !item.isV());

            boolean matchesS = (sFilter == R.id.radioAnyS)
                    || (sFilter == R.id.radioYesS && item.isS())
                    || (sFilter == R.id.radioNoS && !item.isS());

            boolean matchesM = (mFilter == R.id.radioAnyM)
                    || (mFilter == R.id.radioYesM && item.isM())
                    || (mFilter == R.id.radioNoM && !item.isM());

            boolean matchesLevel = levels[item.getLevel()];

            boolean matchesSchool = false;

            for (int id : schoolIds)
                if (item.getSchoolId() == id) {
                    matchesSchool = true;
                    break;
                }

            if (matchesRitual && matchesConcentration && matchesLevel && matchesSchool
                    && matchesV && matchesS && matchesM) {
                filteredList.add(item);
            }
        }
        updateList(filteredList);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<SpellModel> newList) {
        spellModels.clear();
        spellModels.addAll(newList);
        spellModelsFiltered.clear();
        spellModelsFiltered.addAll(newList);
        notifyDataSetChanged();
    }

}
