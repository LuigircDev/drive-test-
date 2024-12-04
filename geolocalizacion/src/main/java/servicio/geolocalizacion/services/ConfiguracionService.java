package servicio.geolocalizacion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import servicio.geolocalizacion.config.Configuracion;

@Service
public class ConfiguracionService {

    @Value("${configuracion.url}")
    private String urlConfiguracion;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    public Configuracion getConfiguracion() {
        Configuracion configuracion = restTemplate.getForObject(urlConfiguracion, Configuracion.class);
        return configuracion;
    }
}
