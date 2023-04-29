package com.alberto.task;

import com.alberto.model.Medicamento;
import com.alberto.model.Presentaciones;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

public class PresentacionesTask extends Task<Integer>{

    private String requestedMedicamento;
    private Consumer<Presentaciones> user;
    private ObservableList<Medicamento> results;
    private ProgressIndicator progressIndicator;

    public PresentacionesTask(String requestedMedicamento, ObservableList<Medicamento> results, ProgressIndicator progressIndicator){
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
            ProgressIndicator progressIndicator = new ProgressIndicator();
            DoubleProperty progressProperty = progressIndicator.progressProperty();
            progressProperty.set(0.5);
            
        };
        cimaApiService.getPresentaciones(requestedMedicamento).subscribe(user, throwable -> {
            // Maneja la excepción aquí
            throwable.printStackTrace();
        });
        // actualiza el valor del progress indicator durante la operación
        for (int i = 0; i < 100; i++) {
            updateProgress(i, 100);
            Thread.sleep(10); // simulando una operación larga
        }
       /*  cimaApiService.getPresentaciones(requestedMedicamento).subscribe(user, throwable -> {
            // Maneja la excepción aquí
            throwable.printStackTrace();
        }); */

        return null;
        
    }
    
}
