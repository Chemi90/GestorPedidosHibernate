package com.example.gestionpedidoscondao.domain.producto;

import com.example.gestionpedidoscondao.domain.itemPedido.ItemPedido;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Productos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_productos")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio")
    private double precio;

    @Column(name = "cantidad_disponible")
    private int cantidadDisponible;

    // Aquí podrías agregar relaciones con otras entidades si las hay, por ejemplo, con ItemPedido
    @OneToMany(mappedBy = "producto")
    private Set<ItemPedido> itemsPedidos = new HashSet<>();
}
