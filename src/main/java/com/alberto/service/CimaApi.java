package com.alberto.service;

import com.alberto.model.Medicamento;
import com.alberto.model.Presentaciones;
import com.alberto.model.Psuministro;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CimaApi {

    @GET("medicamento")
    public Observable<Medicamento> getInformation(@Query("nregistro") String nregistro);

    @GET("presentaciones")
    public Observable<Presentaciones> getPresentaciones(@Query("nregistro") String nregistro);

    @GET("psuministro")
    public Observable<Psuministro> getPsuministro(@Query("nregistro") String nregistro);
}