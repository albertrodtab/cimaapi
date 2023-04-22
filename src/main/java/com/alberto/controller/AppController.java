package com.alberto.controller;

import java.io.IOException;
import com.alberto.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.VBox;

public class AppController {
    @FXML
    private TextField medicamentoInput;
    @FXML
    private Button btSearch;
    @FXML
    private Button btPsuministro;
    @FXML
    private Button btPresentaciones;
    @FXML
    private TabPane tpMedicamento;

    private String lastSearch;


    @FXML
    public void searchMedicamento(ActionEvent event) {
        //medicamentos.clear();
        String requestedMedicamento = medicamentoInput.getText();
        this.lastSearch = requestedMedicamento;
        medicamentoInput.clear();
        medicamentoInput.requestFocus();
        //medicamentosArea.setText("");        

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("results.fxml"));
        MedicamentosController medicamentosController = new MedicamentosController(requestedMedicamento);
        loader.setController(medicamentosController);
        try{
            VBox vBox = loader.load();
            tpMedicamento.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
            tpMedicamento.getTabs().add(new Tab(requestedMedicamento, vBox));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void searchPsuministro(ActionEvent event) {

        //medicamentos.clear();

        String requestedMedicamento = medicamentoInput.getText();
        this.lastSearch = requestedMedicamento;
        medicamentoInput.clear();
        medicamentoInput.requestFocus();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("results.fxml"));
        PSuministroController PSuministroController = new PSuministroController(requestedMedicamento);
        loader.setController(PSuministroController);
        try{
            VBox vBox = loader.load();
            tpMedicamento.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
            tpMedicamento.getTabs().add(new Tab(requestedMedicamento, vBox));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void searchPresentaciones(ActionEvent event) {

        //medicamentos.clear();

        String requestedMedicamento = medicamentoInput.getText();
        this.lastSearch = requestedMedicamento;
        medicamentoInput.clear();
        medicamentoInput.requestFocus();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("results.fxml"));
        PresentacionesController PresentacionesController = new PresentacionesController(requestedMedicamento);
        loader.setController(PresentacionesController);
        try{
            VBox vBox = loader.load();
            tpMedicamento.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
            tpMedicamento.getTabs().add(new Tab(requestedMedicamento, vBox));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}

       