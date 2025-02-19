package com.tpi_pais.mega_store.products.service;

import com.tpi_pais.mega_store.exception.BadRequestException;
import com.tpi_pais.mega_store.products.dto.*;
import com.tpi_pais.mega_store.products.mapper.EstadisticasProductosMasVendidosMapper;
import com.tpi_pais.mega_store.products.mapper.EstadisticasVentasMapper;
import com.tpi_pais.mega_store.products.model.FrecuenciaVentas;
import com.tpi_pais.mega_store.products.model.MontoPromedioVenta;
import com.tpi_pais.mega_store.products.model.ProductoMasVendido;
import com.tpi_pais.mega_store.products.model.VentasPorPeriodo;
import com.tpi_pais.mega_store.products.repository.EstProdMasVendidoRepository;
import com.tpi_pais.mega_store.products.repository.EstVentasRepository;
import com.tpi_pais.mega_store.products.repository.FrecuenciaVentasRepository;
import com.tpi_pais.mega_store.products.repository.MontoPromedioVentaRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EstadisticasService implements IEstadisticasService {

    private EstVentasRepository prodMasVendidosR;
    private EstProdMasVendidoRepository productoRepository;
    private FrecuenciaVentasRepository frecuenciaVentasRepository;
    private EstadisticasProductosMasVendidosMapper prodMasVendidosM;
    private EstadisticasVentasMapper ventasM;
    private MontoPromedioVentaRepository mPRepository;

    public EstadisticasService(
            EstadisticasProductosMasVendidosMapper prodMasVendidosM,
            EstadisticasVentasMapper ventasM,
            EstVentasRepository prodMasVendidosR,
            EstProdMasVendidoRepository productoRepository,
            FrecuenciaVentasRepository frecuenciaVentasRepository,
            MontoPromedioVentaRepository mPRepository) {
        this.prodMasVendidosM = prodMasVendidosM;
        this.ventasM = ventasM;
        this.prodMasVendidosR = prodMasVendidosR;
        this.productoRepository = productoRepository;
        this.frecuenciaVentasRepository = frecuenciaVentasRepository;
        this.mPRepository = mPRepository;
    }

    @Override
    public List<EstadisticasProductosMasVendidosDTO> obtenerProductosMasVendidos(String fechaDesde, String fechaHasta, Integer limite) {
        //Las fechas las recibo en formato aaaa-mm-dd
        if (limite == null || limite <= 0) {
            limite = 10;
        }
        this.verificarFechas(fechaDesde, fechaHasta);
        Timestamp fDesde = null;
        Timestamp fHasta = null;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fDesde = new Timestamp(dateFormat.parse(fechaDesde).getTime());
            fHasta = new Timestamp(dateFormat.parse(fechaHasta).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        List<ProductoMasVendido> productosMasVendidosI = productoRepository.findProductosMasVendidos(fDesde, fHasta, 10);
        List<EstadisticasProductosMasVendidosDTO> productosMasVendidosD = new ArrayList<EstadisticasProductosMasVendidosDTO>();
        for (ProductoMasVendido producto : productosMasVendidosI) {
            productosMasVendidosD.add(prodMasVendidosM.toDTO(producto));
        }
        return productosMasVendidosD;
    }

    @Override
    public void verificarFechas(String fechaDesde, String fechaHasta) {
        if (fechaDesde == null || fechaHasta == null) {
            throw new BadRequestException("Las fechas son obligatorias.");
        }
        if (fechaDesde.isEmpty() || fechaHasta.isEmpty()) {
            throw new BadRequestException("Las fechas son obligatorias.");
        }
        Date dateDesde = null;
        Date dateHasta = null;
        try {
            dateDesde = new SimpleDateFormat("yyyy-MM-dd").parse(fechaDesde);
            dateHasta = new SimpleDateFormat("yyyy-MM-dd").parse(fechaHasta);
        } catch (ParseException e) {
            throw new BadRequestException("Las fechas tienen un formato incorrecto.");
        }
        if (dateDesde.after(dateHasta)) {
            throw new BadRequestException("La fecha de inicio debe ser menor a la fecha final.");
        }
    }

    @Override
    public List<EstadisticaVentasDTO> obtenerVentas(String fechaDesde, String fechaHasta, String frecuencia) {
        this.verificarFechas(fechaDesde, fechaHasta);
        Timestamp fDesde = null;
        Timestamp fHasta = null;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fDesde = new Timestamp(dateFormat.parse(fechaDesde).getTime());
            fHasta = new Timestamp(dateFormat.parse(fechaHasta).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (frecuencia == null) {
            throw new BadRequestException("La frecuencia es obligatoria.");
        }
        if (!frecuencia.equals("A") && !frecuencia.equals("M") && !frecuencia.equals("D")) {
            throw new BadRequestException("La frecuencia debe ser A, M o D.");
        }
        List<EstadisticaVentasDTO> estadisticasVentas = new ArrayList<EstadisticaVentasDTO>();
        if (frecuencia.equals("A")) {
            List<VentasPorPeriodo> estadisticas = prodMasVendidosR.findVentasPorAnios(fDesde, fHasta);
            for (VentasPorPeriodo estadistica : estadisticas) {
                estadisticasVentas.add(ventasM.anioToDTO(estadistica));
            }
        } else if (frecuencia.equals("M")) {
            List<VentasPorPeriodo> estadisticas = prodMasVendidosR.findVentasPorMes (fDesde, fHasta);
            for (VentasPorPeriodo estadistica : estadisticas) {
                estadisticasVentas.add(ventasM.mesToDTO(estadistica));
            }
        } else {
            List<VentasPorPeriodo> estadisticas = prodMasVendidosR.findVentasPorDias(fDesde, fHasta);
            for (VentasPorPeriodo estadistica : estadisticas) {
                estadisticasVentas.add(ventasM.diaToDTO(estadistica));
            }
        }
        return estadisticasVentas;
    }

    @Override
    public Map<String, Integer> obtenerFrecuenciaVentas() {
        List<FrecuenciaVentas> promedios = frecuenciaVentasRepository.obtenerPromedioDiasEntreVentas();
        Integer min = 1000;
        Integer max = 0;
        for (FrecuenciaVentas promedio : promedios) {
            if (promedio.getDias() < min) {
                min = promedio.getDias();
            }
            if (promedio.getDias() > max) {
                max = promedio.getDias();
            }
        }
        Integer intervalos = 10;
        Integer tamañoIntervalo = (max - min) / intervalos;
        if (tamañoIntervalo  == 0) {
            tamañoIntervalo  = 1;
        }
        Map<String, Integer> histograma = new LinkedHashMap<>();

        for (int i = 0; i < intervalos; i++) {
            int inicio = min + (i * tamañoIntervalo);
            int fin = (i == intervalos - 1) ? max : (inicio + tamañoIntervalo - 1);
            String rango = inicio + " - " + fin;
            histograma.put(rango, 0);
        }
        // Contar frecuencias en cada intervalo
        for (FrecuenciaVentas promedio : promedios) {
            int dias = promedio.getDias();
            for (String rango : histograma.keySet()) {
                String[] limites = rango.split(" - ");
                int inicio = Integer.parseInt(limites[0]);
                int fin = Integer.parseInt(limites[1]);

                if (dias >= inicio && dias <= fin) {
                    histograma.put(rango, histograma.get(rango) + 1);
                    break;  // Evita contar el mismo valor más de una vez
                }
            }
        }
        return histograma;
    }

    @Override
    public Map<String, Integer> obtenerMontoPromedioVentas() {
        List<EstadisticaMontoPromedioVentaDTO> montoPromedio = mPRepository.obtenerMontoPromedioPorUsuario();

        // Inicializamos min y max con un valor extremo
        Double min = Double.MAX_VALUE; // El valor más alto posible para Double
        Double max = Double.MIN_VALUE; // El valor más bajo posible para Double

        for (EstadisticaMontoPromedioVentaDTO monto : montoPromedio) {
            if (monto.getMonto() < min) {
                min = monto.getMonto();
            }
            if (monto.getMonto() > max) {
                max = monto.getMonto();
            }
        }

        // Si el min y max no se modificaron (es decir, la lista estaba vacía), puedes manejar este caso
        if (min.equals(Double.MAX_VALUE) || max.equals(Double.MIN_VALUE)) {
            // Manejar el caso en el que no se encontraron valores válidos (por ejemplo, lanzar excepción o retornar un mapa vacío)
            return new LinkedHashMap<>();
        }

        Integer intervalos = 10;
        Integer tamIntervalo = (int) ((max - min) / intervalos);
        if (tamIntervalo == 0) {
            tamIntervalo = 1;
        }

        Map<String, Integer> histograma = new LinkedHashMap<>();
        for (int i = 0; i < intervalos; i++) {
            int inicio = (int) (min + (i * tamIntervalo));
            int fin = (i == intervalos - 1) ? Math.toIntExact(max.longValue()) : (inicio + tamIntervalo - 1);
            String rango = inicio + " - " + fin;
            histograma.put(rango, 0);
        }

        for (EstadisticaMontoPromedioVentaDTO monto : montoPromedio) {
            Double montoVenta = monto.getMonto();
            for (String rango : histograma.keySet()) {
                String[] limites = rango.split(" - ");
                int inicio = Integer.parseInt(limites[0]);
                int fin = Integer.parseInt(limites[1]);
                if (montoVenta >= inicio && montoVenta <= fin) {
                    histograma.put(rango, histograma.get(rango) + 1);
                    break;  // Evita contar el mismo valor más de una vez
                }
            }
        }

        return histograma;
    }
}
