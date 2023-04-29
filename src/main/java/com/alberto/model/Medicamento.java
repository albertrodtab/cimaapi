package com.alberto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicamento {
    
    String nregistro;
    String nombre;
    String pactivos;
    String labtitular;
    String cn;
    String observ;
    //List<Presentaciones> presentaciones;
    public void setAnotaciones(String newValue) {
    }
    public String getAnotaciones() {
        return null;
    }
}
