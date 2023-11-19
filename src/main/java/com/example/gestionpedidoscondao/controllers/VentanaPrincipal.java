package com.example.gestionpedidoscondao.controllers;

import com.example.gestionpedidoscondao.App;
import com.example.gestionpedidoscondao.Session;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import com.example.gestionpedidoscondao.domain.carrito.Carrito;
import com.example.gestionpedidoscondao.domain.itemPedido.ItemPedido;
import com.example.gestionpedidoscondao.domain.pedido.Pedido;
import com.example.gestionpedidoscondao.domain.pedido.PedidoDAO;
import com.example.gestionpedidoscondao.domain.producto.Producto;
import com.example.gestionpedidoscondao.domain.producto.ProductoDAO;
import com.example.gestionpedidoscondao.domain.usuario.Usuario;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.hibernate.Transaction;
import org.hibernate.mapping.Set;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Clase controladora de la interfaz de usuario principal de la aplicación.
 * Gestiona la interacción con el usuario y realiza la navegación entre diferentes vistas.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 */
public class VentanaPrincipal extends Application implements Initializable {

    @javafx.fxml.FXML
    private TableView tbPedidos;
    @javafx.fxml.FXML
    private TableColumn cCodigo;
    @javafx.fxml.FXML
    private TableColumn cFecha;
    @javafx.fxml.FXML
    private TableColumn cTotal;
    @javafx.fxml.FXML
    private TableView tbCarrito;
    @javafx.fxml.FXML
    private TableColumn cNombre;
    @javafx.fxml.FXML
    private TableColumn cCantidadCarrito;
    @javafx.fxml.FXML
    private TableColumn cPrecioTotal;
    @javafx.fxml.FXML
    private Button btnAceptar;
    @javafx.fxml.FXML
    private Button btnCancelar;
    @javafx.fxml.FXML
    private Label lbNombreUsuario;
    @javafx.fxml.FXML
    private ComboBox cbItem;
    @javafx.fxml.FXML
    private Label lbPrecio;
    @javafx.fxml.FXML
    private ComboBox cbCantidad;
    @javafx.fxml.FXML
    private Button bntAñadir;
    @javafx.fxml.FXML
    private MenuItem mbLogout;
    @javafx.fxml.FXML
    private MenuItem mbClose;
    private ProductoDAO productoDAO = new ProductoDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();


    /**
     * Punto de entrada principal para la aplicación de JavaFX.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicializa la ventana principal de la aplicación con el escenario proporcionado.
     *
     * @param primaryStage El escenario principal para esta aplicación, sobre el cual
     *                     la aplicación se desarrolla.
     */
    @Override
    public void start(Stage primaryStage) {
    }

    @javafx.fxml.FXML
    protected void onComprarClick(ActionEvent event) {
        ObservableList<Carrito> itemsCarrito = tbCarrito.getItems();

        if (itemsCarrito.isEmpty()) {
            // Mensaje de error si el carrito está vacío
            // ... (tu código existente)
            return;
        }

        double totalCompra = calcularTotalCompra(itemsCarrito);
        String codigoPedido = generateUniqueCode();
        Date fechaPedido = new Date(System.currentTimeMillis());
        Usuario usuario = Session.getUser(); // Asumiendo que Session.getUser() devuelve una instancia de Usuario

        Pedido pedido = new Pedido();
        pedido.setCodigo(codigoPedido);
        pedido.setFecha(fechaPedido);
        pedido.setUsuario(usuario);
        pedido.setTotal(totalCompra);

        try {
            // Iniciar transacción con Hibernate
            try(org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction tx = session.beginTransaction();

                // Guardar el pedido
                session.save(pedido);

                // Guardar cada item del carrito como ItemPedido
                for (Carrito item : itemsCarrito) {
                    Producto producto = productoDAO.findByName(item.getNombre()); // Método para buscar Producto por nombre
                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setPedido(pedido);
                    itemPedido.setProducto(producto);
                    itemPedido.setCantidad(item.getCantidad());

                    session.save(itemPedido);
                }

                // Confirmar la transacción
                tx.commit();

                // Limpiar el carrito y actualizar la interfaz
                itemsCarrito.clear();
                tbCarrito.getItems().clear();

                // Actualizar la tabla de pedidos
                actualizarTablaPedidos();

                // Mostrar mensaje de éxito
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Compra Exitosa");
                alert.setHeaderText(null);
                alert.setContentText("Tu pedido ha sido registrado exitosamente.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarTablaPedidos() {
        // Suponiendo que tienes un método en PedidoDAO para obtener pedidos del usuario
        List<Pedido> pedidosActualizados = pedidoDAO.findByUsuarioId(Math.toIntExact(Session.getUser().getId()));
        tbPedidos.setItems(FXCollections.observableArrayList(pedidosActualizados));
    }


    private double calcularTotalCompra(ObservableList<Carrito> itemsCarrito) {
        double total = 0;
        for (Carrito item : itemsCarrito) {
            total += item.getPrecioTotal();
        }
        return total;
    }


    @javafx.fxml.FXML
    public void onCancelarClick(ActionEvent actionEvent) {
        tbCarrito.getItems().clear();
    }

    @javafx.fxml.FXML
    public void onAñadirClick(ActionEvent actionEvent) {
        String productoSeleccionado = cbItem.getValue() != null ? cbItem.getValue().toString() : null;
        Integer cantidadSeleccionada = cbCantidad.getValue() != null ? (int) cbCantidad.getValue() : null;

        if (productoSeleccionado == null || cantidadSeleccionada == null || cantidadSeleccionada <= 0) {
            // Mostrar alerta si no se ha seleccionado el producto o la cantidad es incorrecta
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en selección");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un producto y una cantidad válida.");
            alert.showAndWait();
            return; // Salir del método
        }

        Double precioUnitario = Double.parseDouble(lbPrecio.getText().replace("Precio: ", ""));
        Double total = cantidadSeleccionada * precioUnitario; // Calcular el total

        // Crear un nuevo elemento para el carrito
        Carrito carritoItem = new Carrito(productoSeleccionado, cantidadSeleccionada, total);

        // Agregar el elemento al TableView tbCarrito
        tbCarrito.getItems().add(carritoItem);

        // Llena las columnas de la tabla tbCarrito (si no se han llenado previamente)
        if (cNombre.getCellData(0) == null) {
            cNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            cCantidadCarrito.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
            cPrecioTotal.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
            tbCarrito.getColumns().setAll(cNombre, cCantidadCarrito, cPrecioTotal);
        }
    }

    public String generateUniqueCode() {
        long timestamp = System.currentTimeMillis();
        int randomValue = (int) (Math.random() * 1000);
        String uniqueCode = "PEDIDO_" + timestamp + "_" + randomValue;
        return uniqueCode;
    }

    @javafx.fxml.FXML
    public void onLogoutClick(ActionEvent actionEvent) {
        try {
            App.changeScene("ventanaLogin.fxml", "Login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Manejador del evento de clic para el elemento de menú 'Cerrar'.
     * Cierra la ventana actual y, como resultado, la aplicación si esta es la única ventana abierta.
     *
     * @param actionEvent El evento de acción que desencadenó este método.
     */
    @javafx.fxml.FXML
    public void onCloseClick(ActionEvent actionEvent) {
        // Cerrar la aplicación
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    /**
     * Este método se invoca automáticamente para inicializar el controlador después de que su
     * elemento raíz ha sido completamente procesado.
     *
     * @param url            La ubicación utilizada para resolver rutas relativas para el objeto raíz,
     *                       o {@code null} si la ubicación no es conocida.
     * @param resourceBundle El recurso que se utilizó para localizar el objeto raíz, o {@code null}
     *                       si el objeto raíz no fue localizado.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbNombreUsuario.setText(Session.getUser().getNombre());
        loadNombresProductosIntoComboBox();
        loadPedidosUsuario();
        chageSceneToItemsPedidos();
    }

    private void chageSceneToItemsPedidos() {
        tbPedidos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Pedido pedidoSeleccionado = (Pedido) newValue;
                Pedido codigoPedido = pedidoSeleccionado;
                Session.setPedido(codigoPedido); // Guardar el código del pedido en la sesión
                try {
                    App.changeScene("ventanaItemPedido.fxml", "Items del Pedido " + codigoPedido);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Carga los pedidos del usuario y actualiza la interfaz de usuario en consecuencia.
     */
    private void loadPedidosUsuario() {
        List<Pedido> pedidos = pedidoDAO.findByUsuarioId(Math.toIntExact(Session.getUser().getId()));

        cCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        cFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        cTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Asegúrate de que la lista de pedidos no esté vacía
        if (!pedidos.isEmpty()) {
            tbPedidos.setItems(FXCollections.observableArrayList(pedidos));
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error pedidos");
            alert.setHeaderText(null);
            alert.setContentText("Tabla vacia.");
            alert.showAndWait();
        }
    }


    /**
     * Actualiza la etiqueta de precio basándose en el producto seleccionado.
     * Si el producto es {@code null}, se establece el texto de la etiqueta a "$0.00".
     *
     * @param producto El producto cuyo precio se debe mostrar.
     */
    private void loadPrecioProductosIntoLabel(Producto producto){
        if (producto != null) {
            lbPrecio.setText(String.valueOf(producto.getPrecio()));
        } else {
            lbPrecio.setText("$0.00");
        }
    }

    /**
     * Carga los nombres de todos los productos en el ComboBox para la selección del usuario.
     * Además, establece un oyente para cuando se selecciona un nuevo producto,
     * para actualizar la interfaz de usuario correspondientemente.
     */
    private void loadNombresProductosIntoComboBox() {
        List<Producto> productos = productoDAO.getAll();
        List<String> nombreProductos = new ArrayList<>();
        for (Producto producto : productos) {
            nombreProductos.add(producto.getNombre());
        }
        cbItem.getItems().addAll(nombreProductos);
        listenerProductoSeleccionado(productos);
    }

    /**
     * Añade un oyente al ComboBox de productos que actualiza la cantidad disponible y
     * el precio en la interfaz de usuario cuando se selecciona un producto.
     *
     * @param productos Lista de productos para los que se establecerá el oyente.
     */
    private void listenerProductoSeleccionado(List<Producto> productos) {
        cbItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Producto selectedProducto = productos.stream()
                    .filter(producto -> producto.getNombre().equals(newValue))
                    .findFirst()
                    .orElse(null);
            if (selectedProducto != null) {
                updateCantidadComboBox(selectedProducto);
                loadPrecioProductosIntoLabel(selectedProducto);
            }
        });
    }

    /**
     * Actualiza el ComboBox de cantidad basándose en la cantidad disponible del producto seleccionado.
     * Limpia el ComboBox actual y añade la cantidad de elementos según la disponibilidad del producto.
     *
     * @param producto El producto seleccionado cuya cantidad disponible determinará las opciones de cantidad.
     */
    private void updateCantidadComboBox(Producto producto) {
        cbCantidad.getItems().clear();
        if (producto != null) {
            for (int i = 1; i <= producto.getCantidadDisponible(); i++) {
                cbCantidad.getItems().add(i);
            }
        }
    }
}
