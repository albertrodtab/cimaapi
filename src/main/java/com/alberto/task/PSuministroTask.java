package com.alberto.task;

import java.util.concurrent.TimeUnit;

import com.alberto.model.Medicamento;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;


public class PSuministroTask extends Task<Integer>{

    private String requestedMedicamento;
    //private Consumer<Psuministro> user;
    private ObservableList<Medicamento> results;
    private int counter;
    

    public PSuministroTask(String requestedMedicamento, ObservableList<Medicamento> results){
        this.requestedMedicamento = requestedMedicamento;
        this.results = results;
        this.counter = 0;
        
    }

    @Override
    protected Integer call() throws Exception {
        CimaApiService cimaApiService = new CimaApiService();

        Consumer<Medicamento> user = (medicamento) -> {
            this.counter++;
            Thread.sleep(250);
            updateMessage(String.valueOf(this.counter) + " medicamentos descargados");
            Platform.runLater(()->this.results.add(medicamento));
            };

        cimaApiService.getPsuministro(requestedMedicamento)
        .timeout(30, TimeUnit.SECONDS)
        .subscribe(user, Throwable -> {
            //manejar la excepción aquí
            Throwable.printStackTrace();
        });

        return null;
        
    }
    
}
