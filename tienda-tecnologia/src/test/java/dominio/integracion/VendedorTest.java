package dominio.integracion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import dominio.Vendedor;
import dominio.GarantiaExtendida;
import dominio.Producto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.ProductoTestDataBuilder;

@RunWith(JUnit4.class)
public class VendedorTest {

	private static final String COMPUTADOR_LENOVO = "Computador Lenovo";
	private static final String NOMBRE_CLIENTE = "Jonathan Payares";

	private static final String CODIGO = "S01H4AT5";
	private static final String NOMBRE_PRODUCTO = "Procesador Intel i7";
	private static final int PRECIO = 780000;
	private static final int PRECIO_BD = 240000;

	private SistemaDePersistencia sistemaPersistencia;

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	@Before
	public void setUp() {

		sistemaPersistencia = new SistemaDePersistencia();

		repositorioProducto = sistemaPersistencia.obtenerRepositorioProductos();
		repositorioGarantia = sistemaPersistencia.obtenerRepositorioGarantia();

		sistemaPersistencia.iniciar();
	}

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void generarGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);

		// assert
		assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));

	}

	@Test
	public void productoYaTieneGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
		try {
			vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
			fail();

		} catch (GarantiaExtendidaException e) {
			// assert
			assertEquals(Vendedor.EL_PRODUCTO_TIENE_GARANTIA, e.getMessage());
		}
	}

	@Test
	public void productoNoTieneCodigoConTresVocalesTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo("FTA05I12").build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);

		// assert
		assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));
	}
	
	@Test
	public void guardarNombreDelClienteDeLaGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo("FTA05I12").build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);

		// assert
		assertEquals(NOMBRE_CLIENTE,repositorioGarantia.obtener(producto.getCodigo()).getNombreCliente());
	}
	
	@Test
	public void guardarPrecioDeLaGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo(CODIGO).conNombre(NOMBRE_PRODUCTO).conPrecio(PRECIO_BD)
				.build();
		
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);

		// assert
		assertEquals(24000,repositorioGarantia.obtener(producto.getCodigo()).getPrecioGarantia(),0);
	}

	@Test
	public void calcularYGenerarGarantiaExtendidaTest() {
		
		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo(CODIGO).conNombre(NOMBRE_PRODUCTO).conPrecio(PRECIO)
				.build();

		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
		GarantiaExtendida garantiaRepositorio = repositorioGarantia.obtener(producto.getCodigo());		
		
		// assert
		assertEquals(NOMBRE_PRODUCTO, garantiaRepositorio.getProducto().getNombre());
		assertEquals(CODIGO, garantiaRepositorio.getProducto().getCodigo());
		assertEquals(PRECIO, garantiaRepositorio.getProducto().getPrecio(), 0);
		assertEquals(NOMBRE_CLIENTE, garantiaRepositorio.getNombreCliente());
		assertEquals(156000, garantiaRepositorio.getPrecioGarantia(), 0);
		
	}

}
