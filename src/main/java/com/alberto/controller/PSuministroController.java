package com.alberto.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alberto.model.Medicamento;

import com.alberto.task.PSuministroTask;
import com.opencsv.CSVWriter;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class PSuministroController {
    
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

    private PSuministroTask psuministroTask;

    @FXML
    private Label lbStatus;

    //Agrega una nueva variable para almacenar las anotaciones
    private Map<Medicamento, String> anotacionesMap = new HashMap<>();
    
    //@FXML
    // ListView<Medicamento> medicamentosListView;
   
    
    
    public PSuministroController(String requestedMedicamento){
        this.requestedMedicamento = requestedMedicamento;
        this.results = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
       
        TableColumn<Medicamento, Integer> nroFila = new TableColumn<>("#");
        TableColumn<Medicamento, String> cn = new TableColumn<>("Cod Nacional");
        TableColumn<Medicamento, String> nombre = new TableColumn<>("Nombre");
        TableColumn<Medicamento, String> observ = new TableColumn<>("Observaciones");
        //Agrego una columna para poder guardar mis anotaciones de los medicamentos
        TableColumn<Medicamento, String> anotaciones = new TableColumn<>("Anotaciones");
        //TableColumn<Medicamento, String> labtitular = new TableColumn<>("Lab. Titular");
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
        
        /* configuro la celda para que sea editable y me deje guardar mis anotaciones
         * es necesario añadir dos metodos en la clase medicamento, para establer el texto y obtenerlo.
         */
        anotaciones.setCellFactory(column -> new TableCell<Medicamento, String>() {
            private final TextArea textArea = new TextArea();
        
            {
                textArea.setWrapText(true);
                textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                    Medicamento medicamento = getTableView().getItems().get(getIndex());
                    medicamento.setAnotaciones(newValue);
                });
            }
        
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Medicamento medicamento = getTableView().getItems().get(getIndex());
                    textArea.setText(medicamento.getAnotaciones());
                    setGraphic(textArea);
                }
            }
        });

        /*  agrega un listener que se encargua de actualizar el mapa anotacionesMap cada vez que el usuario 
        edita la celda de la columna "Anotaciones" */
        anotaciones.setCellFactory(TextFieldTableCell.forTableColumn());
        anotaciones.setOnEditCommit(event -> {
        Medicamento medicamento = event.getRowValue();
        anotacionesMap.put(medicamento, event.getNewValue());
        });

        //con esto configuro la tabla de medicamentos y sus columnas en la interfaz de usuario. Cada columna se vincula a una propiedad del objeto Medicamento
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombre.setEditable(true); //hace la celda editable
        cn.setCellValueFactory(new PropertyValueFactory<>("cn"));
        observ.setCellValueFactory(new PropertyValueFactory<>("Observ"));
        //labtitular.setCellValueFactory(new PropertyValueFactory<>("labtitular"));

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

        //añado a la tableView las distintas columnas que hemos creado.
        medicamentosTabla.getColumns().addAll(nroFila, nombre, cn, observ, anotaciones /*presentaciones*/);
        this.medicamentosTabla.setItems(this.results);


        this.psuministroTask = new PSuministroTask(requestedMedicamento, this.results);
        this.psuministroTask.messageProperty().addListener((observableValue, oldValue, newValue) -> this.lbStatus.setText(newValue));
        new Thread(psuministroTask).start();
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
                + this.requestedMedicamento + "_psumistro.csv");
        
        try {
            FileWriter writer = new FileWriter(outputFile);
            CSVWriter csvWriter = new CSVWriter(writer);
            List<String[]> data = new ArrayList<String[]>();
            for (Medicamento medicamento : this.results){
            //incluye la columna "Anotaciones" en el archivo CSV le asigno un valor por defecto y luego la incluyo para exportar.
            String anotaciones = anotacionesMap.getOrDefault(medicamento, "");
            String[] row = { medicamento.getNombre(), medicamento.getNregistro(), medicamento.getPactivos(), anotaciones };
            data.add(row);
        }
            csvWriter.writeAll(data);
            csvWriter.close();
            }catch (IOException e){
                e.printStackTrace();
        }
    }
}
