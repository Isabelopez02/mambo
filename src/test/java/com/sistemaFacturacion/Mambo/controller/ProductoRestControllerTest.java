package com.sistemaFacturacion.Mambo.controller;

import com.sistemaFacturacion.Mambo.Service.ProductoService;
import com.sistemaFacturacion.Mambo.mape.dto.ProductoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoRestControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoRestController productoRestController;

    private ProductoDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ProductoDTO();
        dto.setId(1L);
        dto.setNombre("Cartera Chic Luxe");
        dto.setCodigoBase5D("77014");
        dto.setCategoriaNombre("Carteras");
        dto.setPrecio(59.00);
        dto.setPrecioCompraProveedor(38.00);
        dto.setPorcentajeGananciaMin(35.0);
        dto.setPorcentajeGananciaMax(60.0);
        dto.setPrecioVentaMin(51.30);
        dto.setPrecioVentaMax(60.80);
    }

    @Test
    @DisplayName("CP-05: GET /lista/productos - Retornar lista completa de productos")
    void testGetAll_Success() {
        when(productoService.findAll()).thenReturn(List.of(dto));

        ResponseEntity<List<ProductoDTO>> response = productoRestController.getAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("77014", response.getBody().get(0).getCodigoBase5D());
    }

    @Test
    @DisplayName("CP-06: POST /lista/productos - Crear nuevo producto multipart/form-data")
    void testCreate_Success() {
        when(productoService.save(any(ProductoDTO.class))).thenReturn(dto);

        ResponseEntity<ProductoDTO> response = productoRestController.create(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cartera Chic Luxe", response.getBody().getNombre());
        verify(productoService, times(1)).save(any(ProductoDTO.class));
    }

    @Test
    @DisplayName("CP-07: PUT /lista/productos/{id} - Actualizar producto")
    void testUpdate_Success() {
        when(productoService.actualizar(eq(1L), any(ProductoDTO.class))).thenReturn(dto);

        ResponseEntity<ProductoDTO> response = productoRestController.update(1L, dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(productoService, times(1)).actualizar(eq(1L), any(ProductoDTO.class));
    }

    @Test
    @DisplayName("CP-08: DELETE /lista/productos/{id} - Eliminar producto de la base de datos")
    void testDelete_Success() {
        doNothing().when(productoService).deleteById(1L);

        ResponseEntity<Void> response = productoRestController.delete(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productoService, times(1)).deleteById(1L);
    }
}
