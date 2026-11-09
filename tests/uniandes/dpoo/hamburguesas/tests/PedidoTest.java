package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest{
    private Pedido pedidoVacio;
    private Pedido pedidoCompleto;
    private ProductoMenu hamburguesa;
    private Combo combo;
    private ProductoAjustado papasAjustadas;

    @TempDir
    File carpetaTemporal;

    @BeforeEach
    void setUp( ) throws Exception {
        pedidoVacio = new Pedido( "Laura Gómez", "Carrera 7 # 40-10" );

        hamburguesa = new ProductoMenu( "corral", 14000 );
        ProductoMenu papas = new ProductoMenu( "papas medianas", 5500 );
        ProductoMenu gaseosa = new ProductoMenu( "gaseosa", 5000 );

        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( hamburguesa );
        items.add( papas );
        items.add( gaseosa );
        combo = new Combo( "combo corral", 0.1, items );

        papasAjustadas = new ProductoAjustado( papas );

        pedidoCompleto = new Pedido( "Ana Torres", "Calle 10 # 5-20" );
        pedidoCompleto.agregarProducto( hamburguesa );
        pedidoCompleto.agregarProducto( combo );
        pedidoCompleto.agregarProducto( papasAjustadas );
    }

    private static final String FACTURA_COMPLETA = "Cliente: Ana Torres\n" //
            + "Dirección: Calle 10 # 5-20\n" //
            + "----------------\n" //
            + "corral\n" + "            14000\n" //
            + "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n" //
            + "papas medianas\n" + "            5500\n" + "            5500\n" //
            + "----------------\n" //
            + "Precio Neto:  41550\n" //
            + "IVA:          7894\n" //
            + "Precio Total: 49444\n";

    private static final String FACTURA_VACIA = "Cliente: Laura Gómez\n" //
            + "Dirección: Carrera 7 # 40-10\n" //
            + "----------------\n" //
            + "----------------\n" //
            + "Precio Neto:  0\n" //
            + "IVA:          0\n" //
            + "Precio Total: 0\n";

    @Test
    void testIdPedidoSeIncrementa( ) {
        Pedido primero = new Pedido( "Cliente 1", "Dirección 1" );
        Pedido segundo = new Pedido( "Cliente 2", "Dirección 2" );
        Pedido tercero = new Pedido( "Cliente 3", "Dirección 3" );

        assertEquals( primero.getIdPedido( ) + 1, segundo.getIdPedido( ), "El identificador del segundo pedido debe ser el siguiente al del primero." );
        assertEquals( segundo.getIdPedido( ) + 1, tercero.getIdPedido( ), "El identificador del tercer pedido debe ser el siguiente al del segundo." );
    }

    @Test
    void testIdPedidoEsEstable( ) {
        int id = pedidoVacio.getIdPedido( );
        new Pedido( "Otro cliente", "Otra dirección" );
        assertEquals( id, pedidoVacio.getIdPedido( ), "Crear otros pedidos no debe cambiar el identificador de un pedido existente." );
    }

    @Test
    void testGetNombreCliente( ) {
        assertEquals( "Laura Gómez", pedidoVacio.getNombreCliente( ), "El nombre del cliente no es el esperado." );
        assertEquals( "Ana Torres", pedidoCompleto.getNombreCliente( ), "El nombre del cliente no es el esperado." );
    }

    @Test
    void testPrecioTotalPedidoVacio( ) {
        assertEquals( 0, pedidoVacio.getPrecioTotalPedido( ), "Un pedido sin productos debe costar 0." );
    }

    @Test
    void testPrecioTotalPedidoCompleto( ) {
        assertEquals( 49444, pedidoCompleto.getPrecioTotalPedido( ), "El precio total debe ser el neto más el IVA del 19%." );
    }

    @Test
    void testAgregarProductoActualizaElPrecio( ) {
        pedidoVacio.agregarProducto( hamburguesa );
        assertEquals( 16660, pedidoVacio.getPrecioTotalPedido( ), "Después de agregar un producto, el precio total debe reflejarlo." );

        pedidoVacio.agregarProducto( hamburguesa );
        assertEquals( 33320, pedidoVacio.getPrecioTotalPedido( ), "Un mismo producto puede agregarse varias veces al pedido." );
    }

    @Test
    void testGenerarTextoFacturaPedidoVacio( ) {
        assertEquals( FACTURA_VACIA, pedidoVacio.generarTextoFactura( ), "La factura de un pedido vacío no es la esperada." );
    }

    @Test
    void testGenerarTextoFacturaPedidoCompleto( ) {
        assertEquals( FACTURA_COMPLETA, pedidoCompleto.generarTextoFactura( ), "La factura debe incluir cliente, dirección, cada producto, neto, IVA y total." );
    }

    @Test
    void testGenerarTextoFacturaUnSoloProducto( ) {
        pedidoVacio.agregarProducto( hamburguesa );
        String esperado = "Cliente: Laura Gómez\n" //
                + "Dirección: Carrera 7 # 40-10\n" //
                + "----------------\n" //
                + "corral\n" + "            14000\n" //
                + "----------------\n" //
                + "Precio Neto:  14000\n" //
                + "IVA:          2660\n" //
                + "Precio Total: 16660\n";
        assertEquals( esperado, pedidoVacio.generarTextoFactura( ), "La factura de un pedido con un solo producto no es la esperada." );
    }

    @Test
    void testFacturaCoherenteConLosPrecios( ) {
        String factura = pedidoCompleto.generarTextoFactura( );
        assertTrue( factura.contains( "Precio Total: " + pedidoCompleto.getPrecioTotalPedido( ) + "\n" ), "El total de la factura debe coincidir con el precio total del pedido." );
    }

    private String leerArchivo( File archivo ) throws IOException {
        return Files.readString( archivo.toPath( ), Charset.defaultCharset( ) );
    }

    @Test
    void testGuardarFactura( ) throws Exception {
        File archivo = new File( carpetaTemporal, "factura_completa.txt" );
        pedidoCompleto.guardarFactura( archivo );

        assertTrue( archivo.exists( ), "El archivo de la factura debe haberse creado." );
        assertEquals( FACTURA_COMPLETA, leerArchivo( archivo ), "El contenido del archivo debe ser igual al texto de la factura." );
    }

    @Test
    void testGuardarFacturaPedidoVacio( ) throws Exception {
        File archivo = new File( carpetaTemporal, "factura_vacia.txt" );
        pedidoVacio.guardarFactura( archivo );

        assertEquals( FACTURA_VACIA, leerArchivo( archivo ), "El archivo de la factura de un pedido vacio no tiene el contenido esperado." );
    }

    @Test
    void testGuardarFacturaSobreescribeElArchivo( ) throws Exception {
        File archivo = new File( carpetaTemporal, "factura.txt" );
        pedidoCompleto.guardarFactura( archivo );
        pedidoVacio.guardarFactura( archivo );

        assertEquals( FACTURA_VACIA, leerArchivo( archivo ), "Al guardar de nuevo en el mismo archivo, debe quedar sólo la ultima factura." );
    }

    @Test
    void testGuardarFacturaEnCarpetaInexistente( ) {
        File archivo = new File( carpetaTemporal, "carpeta_que_no_existe/factura.txt" );

        assertThrows( FileNotFoundException.class, ( ) -> pedidoCompleto.guardarFactura( archivo ), "Debe lanzarse FileNotFoundException si no se puede crear el archivo." );
        assertTrue( !archivo.exists( ), "No debe haberse creado ningún archivo." );
    }
}