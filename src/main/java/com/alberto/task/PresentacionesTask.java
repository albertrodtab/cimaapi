package com.alberto.task;

import com.alberto.model.Medicamento;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;


public class PresentacionesTask extends Task<Integer>{

    private String requestedMedicamento;
    //private Consumer<Presentaciones> user;
    private ObservableList<Medicamento> results;
    private int counter;

    public PresentacionesTask(String requestedMedicamento, ObservableList<Medicamento> results){
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
            //Thread.sleep(1000);
        };
        cimaApiService.getPresentaciones(requestedMedicamento).subscribe(user, throwable -> {
            // Maneja la excepción aquí
            throwable.printStackTrace();
        });
       /*  cimaApiService.getPresentaciones(requestedMedicamento).subscribe(user, throwable -> {
            // Maneja la excepción aquí
            throwable.printStackTrace();
        }); */

        return null;
        
    }
    
}
