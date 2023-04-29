package com.alberto.task;

import java.util.concurrent.TimeUnit;

import com.alberto.model.Medicamento;
import com.alberto.model.Psuministro;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

public class PSuministroTask extends Task<Integer>{

    private String requestedMedicamento;
    private Consumer<Psuministro> user;
    private ObservableList<Medicamento> results;
    private ProgressIndicator progressIndicator;

    public PSuministroTask(String requestedMedicamento, ObservableList<Medicamento> results, ProgressIndicator progressIndicator){
        this.requestedMedicamento = requestedMedicamento;
        this.results = results;
        this.progressIndicator = progressIndicator;
    }

    @Override
    protected Integer call() throws Exception {
        CimaApiService cimaApiService = new CimaApiService();

        Consumer<Medicamento> user = (medicamento) -> {
            Platform.runLater(()->this.results.add(medicamento));
            Thread.sleep(1000);
            updateProgress(1, 1); //actualiza el valor del progreso de la tarea
            };
                
        // actualiza el valor del progress indicator durante la operación
        for (int i = 0; i < 100; i++) {
            updateProgress(i, 100);
            Thread.sleep(10); // simulando una operación larga
        }
        cimaApiService.getPsuministro(requestedMedicamento)
        .timeout(30, TimeUnit.SECONDS)
        .subscribe(user, Throwable -> {
            //manejar la excepción aquí
            Throwable.printStackTrace();
        });

        return null;
        
    }
    
}
