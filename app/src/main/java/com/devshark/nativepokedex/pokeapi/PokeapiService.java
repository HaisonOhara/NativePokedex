package com.devshark.nativepokedex.pokeapi;

import com.devshark.nativepokedex.models.PokemonResposta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonResposta> obterListaPokemon(@Query("limit") int limit,@Query("offset") int offset);

}
