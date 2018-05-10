package com.example.jose.viassecundariasnarino.viasapi;

import com.example.jose.viassecundariasnarino.modelos.ViasRespuesta;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServiceVias
{
        @GET("qvqk-dtmf.json")
        Call<ArrayList<ViasRespuesta>> obtenerListaVias();
}
