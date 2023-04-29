package com.alberto.service;

import java.util.List;

import com.alberto.model.Medicamento;
import com.alberto.model.Presentaciones;
import com.alberto.model.Psuministro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CimaApiService {

    static final String BASE_URL = "https://cima.aemps.es/cima/rest/";
    private CimaApi cimaApi;

   
    public CimaApiService(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gsonParser = new GsonBuilder()
        .setLenient()
        .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gsonParser))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

        this.cimaApi = retrofit.create(CimaApi.class);

    }

    public Observable<Medicamento> getMedicamento(String nregistro){
         return this.cimaApi.getInformation(nregistro).map(medicamento -> {
            return medicamento;
        });
    }

    public Observable<Medicamento> getPsuministro(String nregistro){
        return this.cimaApi.getPsuministro(nregistro).map(psuministro -> psuministro.getResultados())
        .flatMapIterable(resultados -> resultados);
               
            
        };

    public Observable<Medicamento> getPresentaciones(String nregistro){
        return this.cimaApi.getPresentaciones(nregistro).map(presentaciones -> presentaciones.getResultados())
        .flatMapIterable(resultados -> resultados);
    }

    
}

