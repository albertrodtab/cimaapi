package com.alberto.controller;

import java.util.ArrayList;
import java.util.List;

import com.alberto.model.Medicamento;
import com.alberto.task.MedicamentoTask;

import io.reactivex.functions.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AppController {
    @FXML
    private TextField medicamentoInput;
    @FXML
    private Button btSearch;
    @FXML
    private Button btDelete;
    @FXML
    private Button btExport;
    @FXML
    private TextField deleteInput;
    @FXML
    private TextArea medicamentosArea;

    private MedicamentoTask medicamentoTask;
    //private SynonymsTask synonymsTask;
    private List<String> medicamentos;

    private String lastSearch;


    @FXML
    public void searchMedicamento(ActionEvent event) {
        this.medicamentos = new ArrayList<String>();
        String requestedMedicamento = medicamentoInput.getText();
        this.lastSearch = requestedMedicamento;
        medicamentoInput.clear();
        medicamentoInput.requestFocus();
        medicamentosArea.setText("");

        Consumer<Medicamento> user = (medicamento) -> {
            String previousText;
            previousText = medicamentosArea.getText() + "\n";
            Thread.sleep(10);
            this.medicamentosArea.setText(previousText + medicamento.getNombre());
            this.medicamentos.add(medicamento.getNombre());
           
        };

        this.medicamentoTask = new MedicamentoTask(requestedMedicamento, user);
        new Thread(medicamentoTask).start();
    }
    
}
