package com.sistemaFacturacion.Mambo.entity.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_base_5d", length = 10)
    private String codigoBase5D;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private categoria categoria;

    @Column(nullable = false)
    private Double precio;

    @Column(name = "precio_compra_proveedor")
    private Double precioCompraProveedor;

    @Column(name = "porcentaje_ganancia_min")
    private Double porcentajeGananciaMin;

    @Column(name = "porcentaje_ganancia_max")
    private Double porcentajeGananciaMax;

    @Column(name = "precio_venta_min")
    private Double precioVentaMin;

    @Column(name = "precio_venta_max")
    private Double precioVentaMax;

    @Column(nullable = true)
    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "imagen_url")
    private String imagenUrl;

}
