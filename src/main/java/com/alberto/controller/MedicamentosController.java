package com.alberto.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alberto.model.Medicamento;
import com.alberto.task.MedicamentoTask;
import com.opencsv.CSVWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MedicamentosController {

    @FXML
    private TextField deleteInput;

    @FXML
    private Button btDelete;

    @FXML
    private Button btExport;

    @FXML
    private TableView<Medicamento> medicamentosTabla = new TableView<>(null);
    
    private String requestedMedicamento;

    private ObservableList<Medicamento> results;

    private MedicamentoTask medicamentoTask;
   
  
    
    public MedicamentosController(String requestedMedicamento){
        this.requestedMedicamento = requestedMedicamento;
        this.results = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        
        TableColumn<Medicamento, Integer> nroFila = new TableColumn<>("#");
        TableColumn<Medicamento, String> nregistro = new TableColumn<>("Nº Registro");
        TableColumn<Medicamento, String> nombre = new TableColumn<>("Nombre");
        TableColumn<Medicamento, String> pactivos = new TableColumn<>("P. Activos");
        TableColumn<Medicamento, String> labtitular = new TableColumn<>("Lab. Titular");
        //TableColumn<Medicamento, TableView<Presentaciones>> presentaciones = new TableColumn<>("Presentaciones");

        // Configurar la fábrica de celdas personalizada para la columna de número de fila
        nroFila.setCellFactory(column -> {
            return new TableCell<Medicamento, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (this.getTableRow() != null && !empty) {
                        int rowIndex = this.getTableRow().getIndex() + 1;
                        setText(String.valueOf(rowIndex));
                    } else {
                        setText("");
                    }
                }
            };
        });
        

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
        


        medicamentosTabla.getColumns().addAll(nroFila, nombre, nregistro, pactivos, labtitular /*presentaciones*/);
        this.medicamentosTabla.setItems(this.results);

        this.medicamentoTask = new MedicamentoTask(requestedMedicamento, this.results);
        new Thread(medicamentoTask).start();
    }

    @FXML
    public void deleteMedicamento(ActionEvent event) {
        // Obtenemos el índice del registro seleccionado
        int index = medicamentosTabla.getSelectionModel().getFocusedIndex();

        // Si se ha seleccionado un registro, lo eliminamos
        if (index >= 0) {
            results.remove(index);
        }
    }

    @FXML
    public void clearTabla(ActionEvent event) {
        results.clear();
    }

    @FXML
    public void exportCSV(ActionEvent event) {
        File outputFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator")
                + this.requestedMedicamento + "_medicamento.csv");
        
        try {
            FileWriter writer = new FileWriter(outputFile);
            CSVWriter csvWriter = new CSVWriter(writer);
            List<String[]> data = new ArrayList<String[]>();
            for (Medicamento medicamento : this.results){
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