package com.example.gestionpedidoscondao;

import com.example.gestionpedidoscondao.domain.pedido.Pedido;
import com.example.gestionpedidoscondao.domain.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase <code>Session</code> que maneja la información de la sesión del usuario.
 * Mantiene los detalles del usuario logueado y del pedido actual en el contexto de la aplicación.
 * Utiliza anotaciones de Lombok para generar automáticamente los métodos 'getters'.
 * Es importante notar que al ser una clase con miembros estáticos, se comporta de manera global,
 * manteniendo una única instancia de los datos para toda la aplicación.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 2023-11-21
 */
public class Session {

    @Setter
    @Getter
    private static Usuario User; // Usuario actualmente logueado en la sesión
    @Setter
    @Getter
    private static Pedido pedido; // Pedido actual en la sesión
}

