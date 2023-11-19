module com.example.gestionpedidoscondao {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens com.example.gestionpedidoscondao to javafx.fxml;
    exports com.example.gestionpedidoscondao;
    exports com.example.gestionpedidoscondao.controllers;
    opens com.example.gestionpedidoscondao.controllers to javafx.fxml;

    // Abre los paquetes de entidad tanto a Hibernate como a JavaFX
    opens com.example.gestionpedidoscondao.domain.usuario to org.hibernate.orm.core, javafx.base;
    opens com.example.gestionpedidoscondao.domain.pedido to org.hibernate.orm.core, javafx.base;
    opens com.example.gestionpedidoscondao.domain.itemPedido to org.hibernate.orm.core, javafx.base;
    opens com.example.gestionpedidoscondao.domain.producto to org.hibernate.orm.core, javafx.base;
    opens com.example.gestionpedidoscondao.domain.carrito to javafx.base;
}
