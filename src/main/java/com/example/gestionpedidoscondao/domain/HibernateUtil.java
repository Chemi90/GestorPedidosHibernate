package com.example.gestionpedidoscondao.domain;

import lombok.extern.java.Log;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Clase HibernateUtil.
 * Utilizada para configurar y proporcionar una instancia de SessionFactory de Hibernate.
 * Incluye un bloque estático para inicializar la SessionFactory al cargar la clase.
 * Utiliza anotaciones para registrar eventos importantes en el proceso de configuración.
 *
 * @author Author Name
 * @version 1.0
 * @since 2023-11-21
 */
@Log
public class HibernateUtil {

    /**
     * Instancia única de SessionFactory.
     * Inicializada durante el proceso de carga de la clase.
     */
    private static SessionFactory sf = null;

    /**
     * Bloque estático para inicializar la instancia de SessionFactory.
     * Configura la SessionFactory a partir de un objeto de configuración de Hibernate.
     * Registra en el log el éxito o los errores durante la inicialización.
     */
    static{
        try {
            Configuration cfg = new Configuration();
            cfg.configure();
            sf = cfg.buildSessionFactory();
            log.info("SessionFactory creada con exito!");
        } catch(Exception ex) {
            log.severe("Error al crear SessionFactory: " + ex.getMessage());
            ex.printStackTrace(); // Esto imprimirá la traza completa de la excepción
        }
    }

    /**
     * Devuelve la instancia de SessionFactory.
     *
     * @return SessionFactory La instancia única de SessionFactory.
     */
    public static SessionFactory getSessionFactory(){
        return sf;
    }
}