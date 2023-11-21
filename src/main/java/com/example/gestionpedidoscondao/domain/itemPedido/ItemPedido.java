package com.example.gestionpedidoscondao.domain.itemPedido;

import com.example.gestionpedidoscondao.domain.pedido.Pedido;
import com.example.gestionpedidoscondao.domain.producto.Producto;
import jakarta.persistence.*;
import lombok.*;

/**
 * Clase ItemPedido.
 * Representa un ítem de un pedido en la base de datos.
 * Esta clase está mapeada a la tabla "ItemsPedidos" en la base de datos.
 * Incluye información sobre el pedido, el producto asociado, y la cantidad del producto.
 *
 * @author Author Name
 * @version 1.0
 * @since 2023-11-21
 */
@Data
@Entity
@Table(name = "ItemsPedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_items")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "codPedido", referencedColumnName = "código")
    private Pedido pedido;

    @Column(name = "cantidad")
    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto", referencedColumnName = "id_productos")
    private Producto producto;

    @Transient
    public Double getPrecioTotal() {
        if (producto != null) {
            return cantidad * producto.getPrecio();
        }
        return 0.0;
    }
}
