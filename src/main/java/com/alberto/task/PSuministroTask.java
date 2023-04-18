package com.alberto.task;

import com.alberto.model.Psuministro;
import com.alberto.service.CimaApiService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class PSuministroTask extends Task<Integer>{

    private String requestedMedicamento;
    private Consumer<Psuministro> user;

    public PSuministroTask(String requestedMedicamento, Consumer<Psuministro> user){
        this.requestedMedicamento = requestedMedicamento;
        this.user = user;
    }

    @Override
    protected Integer call() throws Exception {
        CimaApiService cimaApiService = new CimaApiService();
        cimaApiService.getPsuministro(requestedMedicamento).subscribe(user);

        return null;
        
    }
    
}
