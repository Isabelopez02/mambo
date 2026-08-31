package com.sistemaFacturacion.Mambo.mape.mapeo;

import org.springframework.stereotype.Component;
import com.sistemaFacturacion.Mambo.entity.model.Producto;
import com.sistemaFacturacion.Mambo.mape.dto.ProductoDTO;

@Component
public class ProductoMape {
    public ProductoDTO toDto(Producto producto){
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setCodigoBase5D(producto.getCodigoBase5D());
        if (producto.getCategoria() != null) {
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setPrecioCompraProveedor(producto.getPrecioCompraProveedor());
        dto.setPorcentajeGananciaMin(producto.getPorcentajeGananciaMin());
        dto.setPorcentajeGananciaMax(producto.getPorcentajeGananciaMax());
        dto.setPrecioVentaMin(producto.getPrecioVentaMin());
        dto.setPrecioVentaMax(producto.getPrecioVentaMax());
        dto.setStock(producto.getStock());
        dto.setEstadoStock(producto.getStock() == null || producto.getStock() == 0 ? "Sin stock"
                : (producto.getStock() <= 5 ? "Stock bajo" : "En stock"));
        dto.setImg(producto.getImagenUrl());
        return dto;
    }

    public Producto toEntity(ProductoDTO dto){
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCodigoBase5D(dto.getCodigoBase5D());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setPrecioCompraProveedor(dto.getPrecioCompraProveedor());
        producto.setPorcentajeGananciaMin(dto.getPorcentajeGananciaMin());
        producto.setPorcentajeGananciaMax(dto.getPorcentajeGananciaMax());
        producto.setPrecioVentaMin(dto.getPrecioVentaMin());
        producto.setPrecioVentaMax(dto.getPrecioVentaMax());
        producto.setStock(dto.getStock());
        return producto;
    }
}
