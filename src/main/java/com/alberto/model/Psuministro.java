package com.alberto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Psuministro {

    String totalFilas;
    String pagina;
    String tamanioPagina;
    List<Medicamento> resultados;
    
}
