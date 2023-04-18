package com.alberto.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.alberto.model.Medicamento;
import com.alberto.model.Presentaciones;
import com.alberto.model.Psuministro;
import com.alberto.task.MedicamentoTask;
import com.alberto.task.PSuministroTask;
import com.opencsv.CSVWriter;

import io.reactivex.functions.Consumer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class AppController  implements Initializable{
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
    //@FXML
    //private TextArea medicamentosArea;

    @FXML
    private TableView<Medicamento> medicamentosTabla = new TableView<>(null);

    private MedicamentoTask medicamentoTask;
    private PSuministroTask psuministroTask;
    private ObservableList<Medicamento> medicamentos;

    private String lastSearch;


    @FXML
    public void searchMedicamento(ActionEvent event) {
        //medicamentos.clear();
        String requestedMedicamento = medicamentoInput.getText();
        this.lastSearch = requestedMedicamento;
        medicamentoInput.clear();
        medicamentoInput.requestFocus();
        //medicamentosArea.setText("");        

        Consumer<Medicamento> user = (medicamento) -> {
            medicamentos.addAll(medicamento);
           
        };

        this.medicamentoTask = new MedicamentoTask(requestedMedicamento, user);
        new Thread(medicamentoTask).start();
    }

    @FXML
    public void searchPsuministro(ActionEvent event) {

        //medicamentos.clear();

        String requestedMedicamento = medicamentoInput.getText();
        this.lastSearch = requestedMedicamento;
        medicamentoInput.clear();
        medicamentoInput.requestFocus();

        Consumer<Psuministro> psuministroConsumer = (psuminsitro) -> {
            medicamentos.addAll(psuminsitro.getResultados());
        };

        this.psuministroTask = new PSuministroTask(requestedMedicamento, psuministroConsumer);
        new Thread(psuministroTask).start();
    }

    @FXML
    public void deleteMedicamento(ActionEvent event) {
        // Obtenemos el índice del registro seleccionado
        int index = medicamentosTabla.getSelectionModel().getFocusedIndex();

        // Si se ha seleccionado un registro, lo eliminamos
        if (index >= 0) {
            medicamentos.remove(index);
            deleteInput.clear();
            deleteInput.requestFocus();
        }
    }

    @FXML
    public void clearTabla(ActionEvent event) {
        medicamentos.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        medicamentosTabla.setPrefWidth(600);
        medicamentosTabla.setPrefHeight(400);

        

        TableColumn<Medicamento, String> nregistro = new TableColumn<>("Nº Registro");
        TableColumn<Medicamento, String> nombre = new TableColumn<>("Nombre");
        TableColumn<Medicamento, String> pactivos = new TableColumn<>("P. Activos");
        TableColumn<Medicamento, String> labtitular = new TableColumn<>("Lab. Titular");
        //TableColumn<Medicamento, TableView<Presentaciones>> presentaciones = new TableColumn<>("Presentaciones");
        

        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nregistro.setCellValueFactory(new PropertyValueFactory<>("nregistro"));
        pactivos.setCellValueFactory(new PropertyValueFactory<>("pactivos"));
        labtitular.setCellValueFactory(new PropertyValueFactory<>("labtitular"));

        /*// Crear una celda personalizada para la columna "Presentaciones" que muestre el TableView de presentaciones.
        presentaciones.setCellFactory(column -> new TableCell<>() {
        @Override
        protected void updateItem(TableView<Presentaciones> item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
            } else {
                setGraphic(item);
            }
        }
    });

    presentaciones.setCellValueFactory(cellData -> {
        Medicamento medicamento = cellData.getValue();
        TableView<Presentaciones> presentacionesTableView = new TableView<>();
        TableColumn<Presentaciones, String> cn = new TableColumn<>("Cod. Nacional");
        cn.setCellValueFactory(new PropertyValueFactory<>("cn"));

        presentacionesTableView.getColumns().add(cn);
        presentacionesTableView.setItems(FXCollections.observableArrayList(medicamento.getPresentaciones()));

        return new SimpleObjectProperty<>(presentacionesTableView);
    }); */
        


        medicamentosTabla.getColumns().addAll(nombre, nregistro, pactivos, labtitular /*presentaciones*/);
        this.medicamentos = FXCollections.observableArrayList();
        medicamentosTabla.setItems(medicamentos);

    }

    @FXML
    public void exportCSV(ActionEvent event) {
        File outputFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator")
                + this.lastSearch + "_medicamentos.csv");
        
        try {
            FileWriter writer = new FileWriter(outputFile);
            CSVWriter csvWriter = new CSVWriter(writer);
            List<String[]> data = new ArrayList<String[]>();
            for (Medicamento medicamento : this.medicamentos){
                String[] row = { medicamento.getNombre(), medicamento.getNregistro(), medicamento.getPactivos() };
                data.add(row);
        }
            csvWriter.writeAll(data);
            csvWriter.close();
            }catch (IOException e){
                e.printStackTrace();
        }
    }
    
}
