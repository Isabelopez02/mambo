package com.sistemaFacturacion.Mambo.mape.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String codigoBase5D;
    private String categoriaNombre;
    private String icono;
    private Double precio;
    private Double precioCompraProveedor;
    private Double porcentajeGananciaMin;
    private Double porcentajeGananciaMax;
    private Double precioVentaMin;
    private Double precioVentaMax;
    private Integer stock;
    private String descripcion; 
    private String img; // ruta q muestra
    private MultipartFile imagenUrl; // ruta q guarda
    private String estadoStock;
}
