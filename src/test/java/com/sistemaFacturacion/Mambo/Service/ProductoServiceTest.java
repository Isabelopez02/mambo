package com.sistemaFacturacion.Mambo.Service;

import com.sistemaFacturacion.Mambo.entity.Repository.CategoriaRepository;
import com.sistemaFacturacion.Mambo.entity.Repository.ProductoRepository;
import com.sistemaFacturacion.Mambo.entity.model.Producto;
import com.sistemaFacturacion.Mambo.entity.model.categoria;
import com.sistemaFacturacion.Mambo.mape.dto.ProductoDTO;
import com.sistemaFacturacion.Mambo.mape.mapeo.ProductoMape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoMape productoMape;

    @Mock
    private GuardadoImgService guardadoImgService;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoEntity;
    private ProductoDTO productoDTO;
    private categoria cat;

    @BeforeEach
    void setUp() {
        cat = new categoria();
        cat.setNombre("Maquillaje");

        productoEntity = new Producto();
        productoEntity.setId(1L);
        productoEntity.setNombre("Kit Maquillaje Glow");
        productoEntity.setCodigoBase5D("12343");
        productoEntity.setCategoria(cat);
        productoEntity.setPrecio(35.00);
        productoEntity.setPrecioCompraProveedor(22.50);
        productoEntity.setPorcentajeGananciaMin(30.0);
        productoEntity.setPorcentajeGananciaMax(50.0);
        productoEntity.setPrecioVentaMin(29.25);
        productoEntity.setPrecioVentaMax(33.75);

        productoDTO = new ProductoDTO();
        productoDTO.setId(1L);
        productoDTO.setNombre("Kit Maquillaje Glow");
        productoDTO.setCodigoBase5D("12343");
        productoDTO.setCategoriaNombre("Maquillaje");
        productoDTO.setPrecio(35.00);
        productoDTO.setPrecioCompraProveedor(22.50);
        productoDTO.setPorcentajeGananciaMin(30.0);
        productoDTO.setPorcentajeGananciaMax(50.0);
        productoDTO.setPrecioVentaMin(29.25);
        productoDTO.setPrecioVentaMax(33.75);
    }

    @Test
    @DisplayName("CP-01: Guardar Producto con cálculo de % Ganancia e Imagen")
    void testSaveProducto_Success() {
        MockMultipartFile mockFile = new MockMultipartFile("imagenUrl", "test.jpg", "image/jpeg", "fake image".getBytes());
        productoDTO.setImagenUrl(mockFile);

        when(productoMape.toEntity(any(ProductoDTO.class))).thenReturn(productoEntity);
        when(categoriaRepository.findByNombre("Maquillaje")).thenReturn(Optional.of(cat));
        when(guardadoImgService.guardarImagen(mockFile)).thenReturn("/uploads/test.jpg");
        when(productoRepository.save(any(Producto.class))).thenReturn(productoEntity);
        when(productoMape.toDto(any(Producto.class))).thenReturn(productoDTO);

        ProductoDTO result = productoService.save(productoDTO);

        assertNotNull(result);
        assertEquals("Kit Maquillaje Glow", result.getNombre());
        assertEquals("12343", result.getCodigoBase5D());
        assertEquals(22.50, result.getPrecioCompraProveedor());
        assertEquals(30.0, result.getPorcentajeGananciaMin());
        assertEquals(50.0, result.getPorcentajeGananciaMax());

        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(guardadoImgService, times(1)).guardarImagen(mockFile);
    }

    @Test
    @DisplayName("CP-02: Actualizar Producto existente exitosamente")
    void testActualizarProducto_Success() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoEntity));
        when(categoriaRepository.findByNombre("Maquillaje")).thenReturn(Optional.of(cat));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoEntity);
        when(productoMape.toDto(any(Producto.class))).thenReturn(productoDTO);

        ProductoDTO result = productoService.actualizar(1L, productoDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("CP-03: Listar todos los productos")
    void testFindAllProductos_Success() {
        when(productoRepository.findAll()).thenReturn(List.of(productoEntity));
        when(productoMape.toDto(productoEntity)).thenReturn(productoDTO);

        List<ProductoDTO> result = productoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Kit Maquillaje Glow", result.get(0).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("CP-04: Eliminar producto por ID")
    void testDeleteById_Success() {
        doNothing().when(productoRepository).deleteById(1L);

        assertDoesNotThrow(() -> productoService.deleteById(1L));

        verify(productoRepository, times(1)).deleteById(1L);
    }
}
