package com.alberto.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Psuministro {

    int totalFilas;
    int pagina;
    int tamanioPagina;
    List<Medicamento> resultados;
    
}
