package com.example.gestionpedidoscondao.domain.pedido;

import com.example.gestionpedidoscondao.domain.itemPedido.ItemPedido;
import com.example.gestionpedidoscondao.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

/**
 * Clase Pedido.
 * Representa un pedido en la base de datos.
 * Esta clase está mapeada a la tabla "Pedidos" en la base de datos.
 * Contiene información sobre el pedido, incluyendo un identificador único, código, fecha, usuario asociado, total del pedido,
 * y un conjunto de ítems asociados al pedido.
 *
 * @author Author Name
 * @version 1.0
 * @since 2023-11-21
 */
@Data
@Entity
@Table(name = "Pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedidos")
    private Long id;

    @Column(name = "código")
    private String codigo;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "usuario", referencedColumnName = "id_usuarios")
    private Usuario usuario;

    @Column(name = "total")
    private double total;

    @OneToMany(mappedBy = "pedido")
    private Set<ItemPedido> itemsPedidos = new HashSet<>();

    @Override
    public String toString() {
        // Incluir solo los campos primitivos o Strings, excluir referencias a otras entidades
        return "Pedido{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", fecha=" + fecha +
                // ... otros campos
                '}';
    }
}
