package com.alberto.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.alberto.model.Medicamento;
import com.alberto.task.PresentacionesTask;
import com.opencsv.CSVWriter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

public class PresentacionesController {
    
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

    private PresentacionesTask presentacionesTask;

//@FXML
   // private ListView<Medicamento> medicamentosListView;
               
    @FXML
    private TextField searchField;

    private final int itemsPerPage = 10;

    @FXML
    private Pagination pagination;

    @FXML
    private Label lbStatus;

    //Agrega una nueva variable para almacenar las anotaciones
    private Map<Medicamento, String> anotacionesMap = new HashMap<>();
    
    
    
    public PresentacionesController(String requestedMedicamento){
        this.requestedMedicamento = requestedMedicamento;
        this.results = FXCollections.observableArrayList();
    }

    /*Esta parte es para intentar implementar en la vista una paginación que me permita moverme por el TableView
    
    public Node createPage(int pageIndex) {
        
        // Calcular el índice inicial y final de los elementos a mostrar en esta página
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, results.size());

        

        TableColumn<Medicamento, Integer> nroFila = new TableColumn<>("#");
        TableColumn<Medicamento, String> nregistro = new TableColumn<>("Nº Registro");
        TableColumn<Medicamento, String> nombre = new TableColumn<>("Nombre");
        TableColumn<Medicamento, String> pactivos = new TableColumn<>("P. Activos");
        TableColumn<Medicamento, String> labtitular = new TableColumn<>("Lab. Titular");

        nroFila.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(startIndex + column.getTableView().getItems().indexOf(column.getValue()) + 1));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nregistro.setCellValueFactory(new PropertyValueFactory<>("nregistro"));
        pactivos.setCellValueFactory(new PropertyValueFactory<>("pactivos"));
        labtitular.setCellValueFactory(new PropertyValueFactory<>("labtitular"));

        medicamentosTabla.getColumns().addAll(nroFila, nombre, nregistro, pactivos, labtitular /*presentaciones);

       
        // Agregar los elementos correspondientes a la página actual
        medicamentosTabla.setItems(FXCollections.observableArrayList(results.subList(startIndex, endIndex)));

        return medicamentosTabla;
    } */
    

    @FXML
    public void initialize() {
        searchField = new TextField();
        
        TableColumn<Medicamento, Integer> nroFila = new TableColumn<>("#");
        TableColumn<Medicamento, String> nregistro = new TableColumn<>("Nº Registro");
        TableColumn<Medicamento, String> nombre = new TableColumn<>("Nombre");
        TableColumn<Medicamento, String> pactivos = new TableColumn<>("P. Activos");
        TableColumn<Medicamento, String> labtitular = new TableColumn<>("Lab. Titular");
        //Agrego una columna para poder guardar mis anotaciones de los medicamentos
        TableColumn<Medicamento, String> anotaciones = new TableColumn<>("Anotaciones");
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
        //TextFieldTableCell.forTableColumn() devuelve una celda de edición de texto, que es el tipo de celda que se 
        //utilizará para la columna "Nombre". setEditable(true) permite que la columna sea editable.
        nombre.setCellFactory(TextFieldTableCell.forTableColumn()); 
        nombre.setEditable(true); //hace la celda editable
        nregistro.setCellValueFactory(new PropertyValueFactory<>("nregistro"));
        pactivos.setCellValueFactory(new PropertyValueFactory<>("pactivos"));
        labtitular.setCellValueFactory(new PropertyValueFactory<>("labtitular"));

        /* // Crear una celda personalizada para la columna "Presentaciones" que muestre el TableView de presentaciones.
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
    }); */

        /* presentaciones.setCellValueFactory(cellData -> {
        Medicamento medicamento = cellData.getValue();
        TableView<Presentaciones> presentacionesTableView = new TableView<>();
        TableColumn<Presentaciones, String> cn = new TableColumn<>("Cod. Nacional");
        cn.setCellValueFactory(new PropertyValueFactory<>("cn"));

        presentacionesTableView.getColumns().add(cn);
        presentacionesTableView.setItems(FXCollections.observableArrayList(medicamento.getPresentaciones()));

        return new SimpleObjectProperty<>(presentacionesTableView);
    }); */ 
        
        //añado a la tableView las distintas columnas que hemos creado.
        medicamentosTabla.getColumns().addAll(nroFila, nombre, nregistro, pactivos, labtitular, anotaciones /*presentaciones*/);
        this.medicamentosTabla.setItems(this.results);

        //llama a la tarea y ejecuto la misma, actualizando la barra de estado.
        this.presentacionesTask = new PresentacionesTask(requestedMedicamento, this.results); 
        this.presentacionesTask.messageProperty().addListener((observableValue, oldValue, newValue) -> this.lbStatus.setText(newValue));
        new Thread(presentacionesTask).start();

        /* // Configurar el objeto Pagination
        pagination.setPageFactory(this::createPage);
        pagination.setPageCount((int) Math.ceil((double) results.size() / itemsPerPage));

        // Actualizar la tabla cuando se cambie de página
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            medicamentosTabla.getItems().clear();
            medicamentosTabla.setItems(FXCollections.observableArrayList(results.subList(newIndex.intValue() * itemsPerPage, Math.min(results.size(), (newIndex.intValue() + 1) * itemsPerPage))));
        }); */
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

    /*con esto pretendia hacer que una columna pudiese servir para filtrar los elementos de dicha columna, PTE REVISAR E IMPLEMENTAR
     @FXML
    public void filterList(ActionEvent event) {

        FilteredList<Medicamento> filteredData = new FilteredList<Medicamento>(results, p -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(medicamento -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
            String Filter = newValue.toUpperCase();
                return medicamento.getNombre().toUpperCase().contains(Filter);
            });
        });
        
        medicamentosTabla.setItems(filteredData);
        medicamentosTabla.refresh();
    
    } */

    @FXML
    public void clearTabla(ActionEvent event) {
        results.clear();
    }
/* 
    @FXML
    public void exportCSV(ActionEvent event) {
        File outputFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator")
                + this.requestedMedicamento + "_presentaciones.csv");
        
        try {
            FileWriter writer = new FileWriter(outputFile);
            CSVWriter csvWriter = new CSVWriter(writer);
            List<String[]> data = new ArrayList<String[]>();
            for (Medicamento medicamento : this.results){
                //incluye la columna "Anotaciones" en el archivo CSV le asigno un valor por defecto y luego la incluyo para exportar.
                String anotaciones = anotacionesMap.getOrDefault(medicamento, "");
                String[] row = { medicamento.getNombre(), medicamento.getNregistro(), medicamento.getPactivos(), anotaciones};
                data.add(row);
        }
            csvWriter.writeAll(data);
            csvWriter.close();
            }catch (IOException e){
                e.printStackTrace();
        }
    } */

    @FXML
    public void exportCSV(ActionEvent event) {
        String baseFileName = requestedMedicamento + "_presentaciones";
        File outputFile = new File("src/main/exportaciones/" + baseFileName + ".csv");
    
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000); // Agregar retraso de 10 segundos
    
                // Nombre del archivo CSV
                int i = 1;
                String csvFileName = baseFileName + ".csv";
                while (new File("src/main/exportaciones/" + csvFileName).exists()) {
                    csvFileName = baseFileName + "_" + i + ".csv";
                    i++;
                }
    
                // Archivo CSV
                File csvFile = new File("src/main/exportaciones/" + csvFileName);
                FileWriter writer = new FileWriter(csvFile);
                CSVWriter csvWriter = new CSVWriter(writer);
                List<String[]> data = new ArrayList<String[]>();
                for (Medicamento medicamento : results) {
                    String anotaciones = anotacionesMap.getOrDefault(medicamento, "");
                    String[] row = {medicamento.getNombre(), medicamento.getNregistro(), medicamento.getPactivos(), anotaciones};
                    data.add(row);
                }
                csvWriter.writeAll(data);
                csvWriter.close();
    
                // Nombre del archivo ZIP
                i = 1;
                String zipFileName = baseFileName + ".zip";
                while (new File("src/main/exportaciones/" + zipFileName).exists()) {
                    zipFileName = baseFileName + "_" + i + ".zip";
                    i++;
                }
    
                // Archivo ZIP
                FileOutputStream fos = new FileOutputStream("src/main/exportaciones/" + zipFileName);
                ZipOutputStream zipOut = new ZipOutputStream(fos);
    
                // Archivo CSV
                FileInputStream fis = new FileInputStream(csvFile);
                ZipEntry zipEntry = new ZipEntry(csvFile.getName());
                zipOut.putNextEntry(zipEntry);
    
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
    
                zipOut.close();
                fis.close();
                fos.close();
    
                return csvFile;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }).thenAcceptAsync((File csvFile) -> {
    
            if(csvFile != null){
                // Mensaje de alerta
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exportación exitosa");
                    alert.setHeaderText(null);
                    alert.setContentText("El archivo se ha exportado con éxito en formato ZIP.");
                    alert.showAndWait();
                });
            }else{
                // Mensaje de alerta de error
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al exportar archivo");
                    alert.setHeaderText(null);
                    alert.setContentText("Ocurrió un error al exportar el archivo. Por favor, inténtalo de nuevo.");
                    alert.showAndWait();
                });
            }
        });
    }
}
