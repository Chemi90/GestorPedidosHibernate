package com.example.gestionpedidoscondao.domain.usuario;

import com.example.gestionpedidoscondao.domain.pedido.Pedido;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Clase Usuario.
 * Representa un usuario en la base de datos.
 * Esta clase está mapeada a la tabla "usuarios" en la base de datos y contiene información sobre el usuario,
 * incluyendo su identificador único, nombre, contraseña, email y una lista de pedidos asociados al usuario.
 *
 * @author Author Name
 * @version 1.0
 * @since 2023-11-21
 */
@Data
@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuarios")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "contraseña")
    private String contraseña;

    @Column(name = "email")
    private String email;


    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    private List<Pedido> pedidos;

    @Override
    public String toString() {
        // Incluir solo los campos primitivos o Strings, excluir referencias a otras entidades
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                // ... otros campos
                '}';
    }
}