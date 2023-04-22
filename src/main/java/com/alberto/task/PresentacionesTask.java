package com.alberto.task;

import com.alberto.model.Medicamento;
import com.alberto.model.Presentaciones;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class PresentacionesTask extends Task<Integer>{

    private String requestedMedicamento;
    private Consumer<Presentaciones> user;
    private ObservableList<Medicamento> results;

    public PresentacionesTask(String requestedMedicamento, ObservableList<Medicamento> results){
        this.requestedMedicamento = requestedMedicamento;
        this.results = results;
    }

    @Override
    protected Integer call() throws Exception {
        CimaApiService cimaApiService = new CimaApiService();

        Consumer<Presentaciones> user = (presentaciones) -> {
            Platform.runLater(()->this.results.addAll(presentaciones.getResultados()));
        };
        cimaApiService.getPresentaciones(requestedMedicamento).subscribe(user);

        return null;
        
    }
    
}
