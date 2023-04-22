package com.alberto.task;

import com.alberto.model.Medicamento;
import com.alberto.model.Psuministro;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class PSuministroTask extends Task<Integer>{

    private String requestedMedicamento;
    private Consumer<Psuministro> user;
    private ObservableList<Medicamento> results;

    public PSuministroTask(String requestedMedicamento, ObservableList<Medicamento> results){
        this.requestedMedicamento = requestedMedicamento;
        this.results = results;
    }

    @Override
    protected Integer call() throws Exception {
        CimaApiService cimaApiService = new CimaApiService();

        Consumer<Psuministro> user = (psuminsitro) -> {
            Platform.runLater(()->this.results.addAll(psuminsitro.getResultados()));
        };
        cimaApiService.getPsuministro(requestedMedicamento).subscribe(user);

        return null;
        
    }
    
}