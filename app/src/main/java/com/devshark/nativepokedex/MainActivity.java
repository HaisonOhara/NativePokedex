package com.devshark.nativepokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.devshark.nativepokedex.models.DetailFragment;
import com.devshark.nativepokedex.models.Pokemon;
import com.devshark.nativepokedex.models.PokemonResposta;
import com.devshark.nativepokedex.pokeapi.PokeapiService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";

    private Retrofit retrofit;
    private int offset;
    private RecyclerView recyclerView;
    private ListaPokemonAdapter listaPokemonAdapter;
    private boolean aptoParaCarregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listaPokemonAdapter = new ListaPokemonAdapter(this, this);
        recyclerView.setAdapter(listaPokemonAdapter);

        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (aptoParaCarregar) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            Log.i(TAG, "Final da Varredura de Pokemons");
                            aptoParaCarregar = false;
                            offset += 20;
                            obterDados(offset);
                        }
                    }
                }
            }
        });

//Adicionando http client  para log de request na API
        OkHttpClient.Builder okhttpClientBuilder=new OkHttpClient.Builder();

        HttpLoggingInterceptor pokeResponse = new HttpLoggingInterceptor();

        okhttpClientBuilder.addInterceptor(pokeResponse);

        pokeResponse.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build())
                .build();
        aptoParaCarregar = true;
        offset = 0;
        obterDados(offset);
    }


    private void obterDados(int offset) {
        PokeapiService service = retrofit.create((PokeapiService.class));
        Call<PokemonResposta> pokemonRespostaCall = service.obterListaPokemon(20, offset);

        pokemonRespostaCall.enqueue(new Callback<PokemonResposta>() {
            @Override
            public void onResponse(Call<PokemonResposta> call, Response<PokemonResposta> response) {
                aptoParaCarregar = true;
                if (response.isSuccessful()) {

                    PokemonResposta pokemonResposta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonResposta.getResults();

                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);

                } else {
                    Log.e(TAG, "onResponse:" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonResposta> call, Throwable t) {
                aptoParaCarregar = true;
                Log.e(TAG, "onFailure:" + t.getMessage());
            }
        });
    }

    public void ClickOnBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}