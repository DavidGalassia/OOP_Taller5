package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Producto;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;


public class ProductoMenuTest{
    private ProductoMenu productoCaro;
    private ProductoMenu productoGratis;

    @BeforeEach
    void setUp( ) throws Exception{
        productoCaro = new ProductoMenu( "corral", 14000 );
        productoGratis = new ProductoMenu( "agua de la casa", 0 );
    }

    @Test
    void testGetNombre( ){
        assertEquals( "corral", productoCaro.getNombre( ), "El nombre del producto no es el esperado." );
        assertEquals( "agua de la casa", productoGratis.getNombre( ), "El nombre del producto no es el esperado." );
    }

    @Test
    void testGetPrecio( ){
        assertEquals( 14000, productoCaro.getPrecio( ), "El precio del producto no es el esperado." );
        assertEquals( 0, productoGratis.getPrecio( ), "El precio de un producto gratis deberia ser 0." );
    }

    @Test
    void testGenerarTextoFactura( ){
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, productoCaro.generarTextoFactura( ), "El texto de la factura del producto no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaPrecioCero( ){
        String esperado = "agua de la casa\n" + "            0\n";
        assertEquals( esperado, productoGratis.generarTextoFactura( ), "El texto de la factura de un producto gratis no es el esperado." );
    }

    @Test
    void testEsUnProducto( ){
        assertTrue( productoCaro instanceof Producto, "ProductoMenu debe implementar la interfaz Producto." );
    }
}