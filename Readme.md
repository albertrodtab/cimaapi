###### Requisitos (1 pto cada uno, obligatorios) 

* La aplicación deberá utilizar técnicas de programación reactiva utilizando la librería RxJava en algún momento 
* Se mostrará un listado de datos utilizando dos operaciones diferentes de la API. 
* Se mostrará información detallada de los items de los dos listados anteriores. 
* Todas las operaciones de carga de datos se harán en segundo plano y se mostrará una barra de progreso o similar al usuario 
* Incorporar alguna operación de búsqueda o filtrado sobre los datos cargados de la API (búsqueda o filtrado que se hará desde la aplicación JavaFX, diferentes a las opciones de filtrado que permita la API) 

###### Otras funcionalidades (1 pto cada una) 

* Cargar algún tipo de contenido gráfico a partir de información dada por la API (una foto, por ejemplo).
### Implementado
* Permitir la exportación del contenido a un fichero CSV.
* Implementar una funcionalidad que permite exportar algún listado (devuelto por alguna operación de la API) a un CSV y se comprima en zip (La idea es implementarlo usando CompletableFuture). Teneis aqui un tutorial sobre cómo comprimir en ZIP con Java 
* Crea, utilizando WebFlux, un pequeño servicio web relacionado con la API seleccionada y consúmelo desde alguna zona de la aplicación JavaFX utilizando WebClient.
### Implementado
* Utiliza correctamente la clase ObservableList de JavaFX para la visualización de los contenidos en los diferentes controles de JavaFX que decidas utilizar (ComboBox, TableView, ListView, . . .).
### Implementado
* Realizar el seguimiento del proyecto utilizando la plataforma GitHub para almacenar el código y gestionando las issues (bug, mejoras, . . .) a medida que se vaya trabajando en él.


        Configurar la carpeta donde guardo mis certificados
        para ello debo acceder a la APi de CIMA, en la barra de direcciones del navegador pulsar sobre el candado
        https://cima.aemps.es/cima/rest/medicamento?nregistro=51347
        ver las propiedades del certificado y exportarlo en mi equipo.
        luego configuro un almacen de certificado "TrustStore", y agrego el certificado descargado desde CMD con el comando 
        keytool -import -alias cima -file certificado.crt -keystore miTruststore
        uso para crearlo la herramienta de java keytool.
        Finalmento configuro el truststore en mi aplicación, para ello implemento estas 2 lineas de código.
        Tambien se podría configurar esta propiedad en le archivo de propiedades del sitema "java.security", yo he decidido 
        implemntarlo mediante código como se ve a continuación.
        

        // con esto le digo donde está ubicado mi truststore y la clave para poder acceder y consultar los certificados.

        System.setProperty("javax.net.ssl.trustStore", "C:\\certf\\miTruststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "LeeHTY59.");
