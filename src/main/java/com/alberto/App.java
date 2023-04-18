package com.alberto;

import com.alberto.controller.AppController;
import com.alberto.model.Medicamento;
//import com.alberto.model.Medicamento;
//import com.alberto.model.Psuministro;
//import com.alberto.service.CimaApiService;
import com.alberto.util.R;

//import io.reactivex.functions.Consumer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class App extends Application{

/*      public static void main( String[] args ){ 

        /* tras configurar la carpeta donde guardo mis certificados
        * para ello debo acceder a la APi de CIMA, en la barra de direcciones pulsar sobre el candado
        * ver las propiedades del certificado y exportarlo en mi equipo.
        * luego configuro un almacen de certificado "TrustStore", y agrego el certificado descargado desde CMD con el comando 
        * keytool -import -alias cima -file certificado.crt -keystore miTruststore
        * uso para crearlo la herramienta de java keytool.
        * Finalmento configuro el truststore en mi aplicación, para ello implmento estas 2 lineas de código.
        * Tambien se podría configurar esta propiedad en le archivo de propiedades del sitema "java.security", yo he decidido 
        * implemntarlo mediante código como se ve a continuación.
        * 

        // con esto le digo donde está ubicado mi truststore y la clave para poder acceder y consultar los certificados.
        System.setProperty("javax.net.ssl.trustStore", "C:\\certf\\miTruststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "LeeHTY59.");


        CimaApiService cimaApiService = new CimaApiService();
                Consumer<Medicamento> medicamento = (medicine-> {
                        System.out.println("Vamos a ver los datos de un medicamento.");              
                        System.out.println("N Registro: " + medicine.getNregistro());
                        System.out.println("Nombre:" + medicine.getNombre());
                        System.out.println("Pactivos:" + medicine.getPactivos());
                        System.out.println("labtitular:" + medicine.getLabtitular());
                });

                Consumer<Psuministro> psuministro = (allpsuministro ->{
                        System.out.println("Vamos a ver los datos de problemas de suministro actuales en España.");
                        System.out.println("Resultados fármacos con problemas de suministro: "); 
                        System.out.println("Fármacos" + allpsuministro.getResultados());
                });
                
                Consumer<Psuministro> psuministroNregistro = (farmaco ->{
                        System.out.println("Resultados fármacos con problemas de suministro: ");
                        System.out.println("Fármaco" + farmaco.getResultados());

                });


                cimaApiService.getMedicamento("51347").subscribe(medicamento);
                cimaApiService.getPsuministro("").subscribe(psuministro);
                cimaApiService.getPsuministro("42991").subscribe(psuministroNregistro);

        }  */

        @Override
        public void start(Stage primaryStage) throws Exception {
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI("main-app.fxml"));
            loader.setController(new AppController());
            ScrollPane mainPane = loader.load();
            Scene scene = new Scene(mainPane);
            primaryStage.setScene(scene);
            primaryStage.setTitle("CimaAPI PSP");
            primaryStage.show();       
        }

        @Override
        public void stop() throws Exception{
            super.stop();
        };

        public static void main(String[] args) {

        /* tras configurar la carpeta donde guardo mis certificados
        * para ello debo acceder a la APi de CIMA, en la barra de direcciones pulsar sobre el candado
        * ver las propiedades del certificado y exportarlo en mi equipo.
        * luego configuro un almacen de certificado "TrustStore", y agrego el certificado descargado desde CMD con el comando 
        * keytool -import -alias cima -file certificado.crt -keystore miTruststore
        * uso para crearlo la herramienta de java keytool.
        * Finalmento configuro el truststore en mi aplicación, para ello implmento estas 2 lineas de código.
        * Tambien se podría configurar esta propiedad en le archivo de propiedades del sitema "java.security", yo he decidido 
        * implemntarlo mediante código como se ve a continuación.
        */

        // con esto le digo donde está ubicado mi truststore y la clave para poder acceder y consultar los certificados.
        System.setProperty("javax.net.ssl.trustStore", "C:\\certf\\miTruststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "LeeHTY59.");

                launch();
        }
      
} 

  

   
    
    



