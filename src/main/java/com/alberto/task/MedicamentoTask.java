package com.alberto.task;

import com.alberto.model.Medicamento;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class MedicamentoTask extends Task<Integer>{

    private String requestedMedicamento;
    private Consumer<Medicamento> user;
    private ObservableList<Medicamento> results;

    public MedicamentoTask(String requestedMedicamento, ObservableList<Medicamento> results){
        this.requestedMedicamento = requestedMedicamento;
        this.results = results;
    }

    @Override
    protected Integer call() throws Exception {
        CimaApiService cimaApiService = new CimaApiService();
       

        Consumer<Medicamento> user = (medicamento) -> {
            Platform.runLater(() -> {
                results.add(medicamento);
            });
           
           
        };
        cimaApiService.getMedicamento(requestedMedicamento).subscribe(user);

        return null;
        
    }
    
}
