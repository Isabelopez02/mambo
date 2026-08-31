package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.entity.model.Producto;
import com.sistemaFacturacion.Mambo.entity.model.categoria;
import com.sistemaFacturacion.Mambo.mape.dto.ProductoDTO;
import com.sistemaFacturacion.Mambo.mape.mapeo.ProductoMape;
import com.sistemaFacturacion.Mambo.entity.Repository.CategoriaRepository;
import com.sistemaFacturacion.Mambo.entity.Repository.ProductoRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMape productoMape;
    private final GuardadoImgService guardadoImgservice;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository,
            ProductoMape productoMape, GuardadoImgService guardadoImgservice) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoMape = productoMape;
        this.guardadoImgservice = guardadoImgservice;
    }

    // Crear producto en la Base de Datos
    public ProductoDTO save(ProductoDTO dto) {
        Producto producto = productoMape.toEntity(dto);

        if (dto.getCategoriaNombre() != null && !dto.getCategoriaNombre().isEmpty()) {
            categoria categoria = categoriaRepository.findByNombre(dto.getCategoriaNombre())
                    .orElseGet(() -> {
                        categoria nuevaCat = new categoria();
                        nuevaCat.setNombre(dto.getCategoriaNombre());
                        return categoriaRepository.save(nuevaCat);
                    });
            producto.setCategoria(categoria);
        }

        if (dto.getImagenUrl() != null && !dto.getImagenUrl().isEmpty()) {
            String urlImagen = guardadoImgservice.guardarImagen(dto.getImagenUrl());
            producto.setImagenUrl(urlImagen);
        }

        Producto nuevo = productoRepository.save(producto);
        return productoMape.toDto(nuevo);
    }

    // Actualizar producto en la Base de Datos
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(dto.getNombre());
        if (dto.getCodigoBase5D() != null) producto.setCodigoBase5D(dto.getCodigoBase5D());
        producto.setDescripcion(dto.getDescripcion());
        if (dto.getPrecio() != null) producto.setPrecio(dto.getPrecio());
        if (dto.getPrecioCompraProveedor() != null) producto.setPrecioCompraProveedor(dto.getPrecioCompraProveedor());
        if (dto.getPorcentajeGananciaMin() != null) producto.setPorcentajeGananciaMin(dto.getPorcentajeGananciaMin());
        if (dto.getPorcentajeGananciaMax() != null) producto.setPorcentajeGananciaMax(dto.getPorcentajeGananciaMax());
        if (dto.getPrecioVentaMin() != null) producto.setPrecioVentaMin(dto.getPrecioVentaMin());
        if (dto.getPrecioVentaMax() != null) producto.setPrecioVentaMax(dto.getPrecioVentaMax());
        if (dto.getStock() != null) producto.setStock(dto.getStock());

        if (dto.getCategoriaNombre() != null && !dto.getCategoriaNombre().isEmpty()) {
            categoria categoria = categoriaRepository.findByNombre(dto.getCategoriaNombre())
                    .orElseGet(() -> {
                        categoria nuevaCat = new categoria();
                        nuevaCat.setNombre(dto.getCategoriaNombre());
                        return categoriaRepository.save(nuevaCat);
                    });
            producto.setCategoria(categoria);
        }

        if (dto.getImagenUrl() != null && !dto.getImagenUrl().isEmpty()) {
            String urlImagen = guardadoImgservice.guardarImagen(dto.getImagenUrl());
            producto.setImagenUrl(urlImagen);
        }

        Producto nuevo = productoRepository.save(producto);
        return productoMape.toDto(nuevo);
    }

    public List<ProductoDTO> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(productoMape::toDto)
                .toList();
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }
}
