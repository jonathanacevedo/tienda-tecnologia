package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import dominio.Vendedor;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.Producto;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	private static final String NOMBRE_CLIENTE = "Jonathan Payares";

	@Test
	public void productoYaTieneGarantiaTest() {

		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();

		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());

		// assert
		assertTrue(existeProducto);
	}

	@Test
	public void productoNoTieneGarantiaTest() {

		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();

		Producto producto = productoestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());

		// assert
		assertFalse(existeProducto);
	}

	@Test
	public void productoTieneCodigoConTresVocalesTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo("FTA05I12U").build();
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		try {

			vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
			fail();

		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_NO_CUENTA_CON_GARANTIA, e.getMessage());
		}
	}
	
	@Test
	public void productoTieneCodigoConTresVocalesMinusculasTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conCodigo("FTa05i12e").build();
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		try {

			vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
			fail();

		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_NO_CUENTA_CON_GARANTIA, e.getMessage());
		}
	}

	@Test
	public void precioDeGarantiaConPrecioDe650000() {

		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		double precioProducto = 650000;
		double precioGarantia = vendedor.calcularPrecioGarantia(precioProducto);

		// assert
		Assert.assertEquals(precioGarantia, 130000, 0);

	}

	@Test
	public void precioDeGarantiaConPrecioDe420000() {

		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		// act
		double precioProducto = 420000;
		double precioGarantia = vendedor.calcularPrecioGarantia(precioProducto);

		// assert
		Assert.assertEquals(precioGarantia, 42000, 0);

	}

	@Test
	public void fechaFinGarantiaPara16Agosto2018() {

		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		Calendar calendar = Calendar.getInstance();

		// act
		calendar.set(Calendar.YEAR, 2018);
		calendar.set(Calendar.MONTH, 7);
		calendar.set(Calendar.DAY_OF_MONTH, 16);
		Date fechaSolicitudGarantia = calendar.getTime();

		calendar.set(Calendar.YEAR, 2019);
		calendar.set(Calendar.MONTH, 3);
		calendar.set(Calendar.DAY_OF_MONTH, 6);
		Date fechaEsperadaGarantia = calendar.getTime();

		Date fechaFinGarantia = vendedor.obtenerFechaFinGarantia(600000, fechaSolicitudGarantia);

		// assert
		Assert.assertTrue(fechaFinGarantia.equals(fechaEsperadaGarantia));

	}
	
	@Test
	public void fechaFinGarantiaQueTerminaDomingo() {

		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		Calendar calendar = Calendar.getInstance();

		// act
		calendar.set(Calendar.YEAR, 2019);
		calendar.set(Calendar.MONTH, 2);
		calendar.set(Calendar.DAY_OF_MONTH, 8);
		Date fechaSolicitudGarantia = calendar.getTime();

		calendar.set(Calendar.YEAR, 2019);
		calendar.set(Calendar.MONTH, 9);
		calendar.set(Calendar.DAY_OF_MONTH, 28);
		Date fechaEsperadaGarantia = calendar.getTime();

		Date fechaFinGarantia = vendedor.obtenerFechaFinGarantia(600000, fechaSolicitudGarantia);
		
		// assert
		Assert.assertTrue(fechaFinGarantia.equals(fechaEsperadaGarantia));

	}

	@Test
	public void validarDiaHabilDeFechaDomingo() {
		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		Calendar calendar = Calendar.getInstance();

		// act
		calendar.set(Calendar.YEAR, 2019);
		calendar.set(Calendar.MONTH, 5);
		calendar.set(Calendar.DAY_OF_MONTH, 9);
		Date fechaFinGarantiaDomingo = calendar.getTime();
		
		calendar.set(Calendar.YEAR, 2019);
		calendar.set(Calendar.MONTH, 5);
		calendar.set(Calendar.DAY_OF_MONTH, 10);
		Date fechaEsperadaGarantia = calendar.getTime();
		
		Date fechaFinGarantia = vendedor.validarDiaHabil(fechaFinGarantiaDomingo);
		
		// assert
		Assert.assertTrue(fechaFinGarantia.equals(fechaEsperadaGarantia));
	}
	
	@Test
	public void validarDiaHabilDeFechaHabil() {
		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);

		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		Calendar calendar = Calendar.getInstance();

		// act
		calendar.set(Calendar.YEAR, 2019);
		calendar.set(Calendar.MONTH, 5);
		calendar.set(Calendar.DAY_OF_MONTH, 12);
		Date fechaFinGarantiaDomingo = calendar.getTime();
		
		Date fechaFinGarantia = vendedor.validarDiaHabil(fechaFinGarantiaDomingo);
		
		// assert
		Assert.assertTrue(fechaFinGarantia.equals(fechaFinGarantiaDomingo));
	}

}
