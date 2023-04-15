package com.alberto.task;

import com.alberto.model.Medicamento;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class MedicamentoTask extends Task<Integer>{

    private String requestedMedicamento;
    private Consumer<Medicamento> user;

    public MedicamentoTask(String requestedMedicamento, Consumer<Medicamento> user){
        this.requestedMedicamento = requestedMedicamento;
        this.user = user;
    }

    @Override
    protected Integer call() throws Exception {
        CimaApiService cimaApiService = new CimaApiService();
        cimaApiService.getMedicamento(requestedMedicamento).subscribe(user);

        return null;
        
    }
    
}
