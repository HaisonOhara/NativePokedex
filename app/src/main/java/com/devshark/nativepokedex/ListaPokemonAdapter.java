package com.devshark.nativepokedex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.devshark.nativepokedex.models.DetailFragment;
import com.devshark.nativepokedex.models.Pokemon;

import java.util.ArrayList;


public class ListaPokemonAdapter extends RecyclerView.Adapter<ListaPokemonAdapter.ViewHolder> {

    private ArrayList<Pokemon> dataset;
    private Context context;
    private ImageView imageView;
    private MainActivity main;

    public ListaPokemonAdapter(Context context, MainActivity ma) {
        this.context = context;
        dataset = new ArrayList<>();
        this.main = ma;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Pokemon p = dataset.get(position);
        holder.nameTextView.setText(p.getName());

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + p.getNumber() + ".png")
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.fotoImageView);

        holder.fotoImageView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "You clicked on " + dataset.get(position).getNumber(), Toast.LENGTH_LONG).show();



                FrameLayout fragmentLayout = new FrameLayout(context);
                fragmentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                fragmentLayout.setId(3);
                main.setContentView(fragmentLayout);
                main.getSupportFragmentManager()
                        .beginTransaction().addToBackStack(null)
                        .add(3, new DetailFragment(dataset.get(position).getNumber(),
                                dataset.get(position).getName(), dataset.get(position).getHeight()))
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void adicionarListaPokemon(ArrayList<Pokemon> listaPokemon) {
        dataset.addAll(listaPokemon);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView fotoImageView;
        private TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            fotoImageView = (ImageView) itemView.findViewById(R.id.fotoImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);

        }
    }

}
