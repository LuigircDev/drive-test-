package servicio.geolocalizacion.config;

import lombok.Data;

import java.util.List;

@Data
public class Configuracion {
    private CoordenadasAgencia coordenadasAgencia;
    private Double radioAdmitidoKm;
    private List<ZonaRestringida> zonasRestringidas;

    @Data
    public static class CoordenadasAgencia {
        private Double lat;
        private Double lon;
    }

    @Data
    public static class ZonaRestringida {
        private Coordenadas Noroeste;
        private Coordenadas Sureste;

        @Data
        public static class Coordenadas {
            private Double lat;
            private Double lon;
        }
    }
}
