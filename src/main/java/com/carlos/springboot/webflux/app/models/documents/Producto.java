package com.carlos.springboot.webflux.app.models.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Document(collection = "productos")
public class Producto {

    @Id
    private String id;

    private String nombre;
    private Double precio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    public Producto(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto() {

    }
}
