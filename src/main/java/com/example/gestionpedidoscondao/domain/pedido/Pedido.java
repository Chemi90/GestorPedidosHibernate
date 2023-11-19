package com.example.gestionpedidoscondao.domain.pedido;

import com.example.gestionpedidoscondao.domain.itemPedido.ItemPedido;
import com.example.gestionpedidoscondao.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

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
