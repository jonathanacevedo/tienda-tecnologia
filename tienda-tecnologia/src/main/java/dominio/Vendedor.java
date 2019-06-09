package dominio;

import dominio.repositorio.RepositorioProducto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
	public static final String EL_PRODUCTO_NO_CUENTA_CON_GARANTIA = "Este producto no cuenta con garantía extendida";
	public static final int MAX_VOCALES_PERMITIDAS = 3;

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;

	}

	public void generarGarantia(String codigo, String nombreCliente) {

		if (!validarSiPermiteGarantia(codigo)) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_NO_CUENTA_CON_GARANTIA);
		}

		if (tieneGarantia(codigo)) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
		}
		
		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);

		double precioProducto = producto.getPrecio();

		Date fechaFinGarantia = obtenerFechaFinGarantia(precioProducto, new Date());
		
		double precioGarantia = calcularPrecioGarantia(precioProducto);

		GarantiaExtendida garantia = new GarantiaExtendida(producto, new Date(),
				fechaFinGarantia, precioGarantia, nombreCliente);

		repositorioGarantia.agregar(garantia);
	}

	public boolean tieneGarantia(String codigo) {

		return repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo) != null
				&& repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo).getCodigo().equals(codigo);

	}

	public boolean validarSiPermiteGarantia(String codigo) {
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("A");
		list.add("E");
		list.add("I");
		list.add("O");
		list.add("U");
		
		String codigoFormat = codigo.toUpperCase();
		
		long count = Arrays.stream(codigoFormat.split("")).
			    filter(s -> list.contains(s)).
			    count();

		return count != MAX_VOCALES_PERMITIDAS;
	}

	public double calcularPrecioGarantia(double precioProducto) {
		double precioGarantia;

		if (precioProducto > 500000) {
			precioGarantia = precioProducto * 0.2;
		} else {
			precioGarantia = precioProducto * 0.1;
		}

		return precioGarantia;
	}

	public Date obtenerFechaFinGarantia(double precioProducto, Date fechaSolicitudGarantia) {

		Date fechaFinGarantia;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaSolicitudGarantia);

		if (precioProducto > 500000) {
			calendar.add(Calendar.DATE, contarDiasDeGarantia(fechaSolicitudGarantia));
		} else {
			calendar.add(Calendar.DATE, 100);
		}

		fechaFinGarantia = calendar.getTime();

		return validarDiaHabil(fechaFinGarantia);
	}

	public int contarDiasDeGarantia(Date fechaSolicitudGarantia) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaSolicitudGarantia);

		int contadorDiasGarantia = 0;
		int contadorDiasCalendario = 0;

		do {
			if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
				++contadorDiasGarantia;
			}
			++contadorDiasCalendario;
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		} while (contadorDiasGarantia < 200);

		return contadorDiasCalendario;
	}

	public Date validarDiaHabil(Date fechaFinGarantia) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fechaFinGarantia);

		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		return calendar.getTime();
	}

}
