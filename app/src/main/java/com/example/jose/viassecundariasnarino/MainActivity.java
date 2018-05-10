package com.example.jose.viassecundariasnarino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jose.viassecundariasnarino.modelos.ViasRespuesta;
import com.example.jose.viassecundariasnarino.viasapi.ApiServiceVias;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;


    private RecyclerView recyclerView;
    private ListaViasAdapter listaViasAdapter;
    private boolean aptoParaCargar;
    public static final String TAG = "VIAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listaViasAdapter = new ListaViasAdapter(this);
        recyclerView.setAdapter(listaViasAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (aptoParaCargar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, " Llegamos al final.");

                            aptoParaCargar = false;
                            //offset += 20;
                            obtenerDatos();
                        }
                    }
                }
            }
        });

        //nuevo
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.datos.gov.co/resource/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        aptoParaCargar = true;
        obtenerDatos();
    }
    private void obtenerDatos() {

        ApiServiceVias service = retrofit.create(ApiServiceVias.class);
        Call<ArrayList<ViasRespuesta>> viasRespuestaCall=service.obtenerListaVias();

        viasRespuestaCall.enqueue(new Callback<ArrayList<ViasRespuesta>>() {
            public static final String TAG = "VIAS";

            @Override
            public void onResponse(Call<ArrayList<ViasRespuesta>> call, Response<ArrayList<ViasRespuesta>> response) {
                if(response.isSuccessful())
                {
                    ArrayList<ViasRespuesta> listaVias = response.body();
                    listaViasAdapter.adicionarListaVias(listaVias);
                    for(int i=0; i<listaVias.size(); i++)
                    {
                        ViasRespuesta p = listaVias.get(i);

                        Log.i(TAG, "Codigo: "+p.getCodigo());
                        Log.i(TAG, "Identificador: "+p.getIdentificador());
                        Log.i(TAG, "Longitud firmado: "+p.getLongitudafirmado());
                        Log.i(TAG, "Longitud pavimento: "+p.getLongitudpavimento());
                        Log.i(TAG, "Municipio: "+p.getMuncipio());
                        Log.i(TAG, "Nombre via: "+p.getNombrevia());
                    }
                }
                else
                {
                    Log.e(TAG, "onResponse: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ViasRespuesta>> call, Throwable t) {

                Log.e(TAG," onFailure: "+t.getMessage() );
            }
        });
    }
}
