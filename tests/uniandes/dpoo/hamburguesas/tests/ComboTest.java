package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Producto;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;


public class ComboTest{
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;
    private ArrayList<ProductoMenu> items;
    private Combo comboNormal;
    private Combo comboSinDescuento;
    private Combo comboGratis;
    private Combo comboVacio;

    @BeforeEach
    void setUp( ) throws Exception
    {
        hamburguesa = new ProductoMenu( "corral", 14000 );
        papas = new ProductoMenu( "papas medianas", 5500 );
        gaseosa = new ProductoMenu( "gaseosa", 5000 );

        items = new ArrayList<ProductoMenu>( );
        items.add( hamburguesa );
        items.add( papas );
        items.add( gaseosa );

        comboNormal = new Combo( "combo corral", 0.1, items );
        comboSinDescuento = new Combo( "combo sin descuento", 0.0, items );
        comboGratis = new Combo( "combo gratis", 1.0, items );
        comboVacio = new Combo( "combo vacio", 0.1, new ArrayList<ProductoMenu>( ) );
    }

    @Test
    void testGetNombre( ){
        assertEquals( "combo corral", comboNormal.getNombre( ), "El nombre del combo no es el esperado." );
    }

    @Test
    void testGetPrecioConDescuento( ){
        assertEquals( 22050, comboNormal.getPrecio( ), "El precio del combo debe ser el valor de los productos menos el descuento." );
    }

    @Test
    void testGetPrecioDescuentoFraccionario( ){
        Combo combo = new Combo( "combo 7%", 0.07, items );
        assertEquals( 22785, combo.getPrecio( ), "El precio del combo con descuento del 7% no es el esperado." );
    }

    @Test
    void testGetPrecioSinDescuento( ){
        assertEquals( 24500, comboSinDescuento.getPrecio( ), "Sin descuento el precio debe ser la suma de los productos." );
    }

    @Test
    void testGetPrecioDescuentoTotal( ){
        assertEquals( 0, comboGratis.getPrecio( ), "Con 100% de descuento el combo no debe costar nada." );
    }

    @Test
    void testGetPrecioComboVacio( ){
        assertEquals( 0, comboVacio.getPrecio( ), "Un combo sin productos debe costar 0." );
    }

    @Test
    void testElComboCopiaLaListaDeProductos( ){
        items.add( new ProductoMenu( "extra", 10000 ) );
        assertEquals( 22050, comboNormal.getPrecio( ), "El combo debe conservar su propia copia de la lista de productos." );
    }

    @Test
    void testGenerarTextoFactura( ){
        String esperado = "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n";
        assertEquals( esperado, comboNormal.generarTextoFactura( ), "El texto de la factura del combo no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaSinDescuento( ){
        String esperado = "Combo combo sin descuento\n" + " Descuento: 0.0\n" + "            24500\n";
        assertEquals( esperado, comboSinDescuento.generarTextoFactura( ), "El texto de la factura de un combo sin descuento no es el esperado." );
    }

    @Test
    void testEsUnProducto( ){
        assertTrue( comboNormal instanceof Producto, "Combo debe implementar la interfaz Producto." );
    }
}