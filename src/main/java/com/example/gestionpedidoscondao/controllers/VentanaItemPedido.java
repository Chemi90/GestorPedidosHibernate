package com.example.gestionpedidoscondao.controllers;

import com.example.gestionpedidoscondao.App;
import com.example.gestionpedidoscondao.Session;
import com.example.gestionpedidoscondao.domain.itemPedido.ItemPedido;
import com.example.gestionpedidoscondao.domain.itemPedido.ItemPedidoDAO;
import com.example.gestionpedidoscondao.domain.pedido.Pedido;
import com.example.gestionpedidoscondao.domain.pedido.PedidoDAO;
import com.example.gestionpedidoscondao.domain.producto.Producto;
import com.example.gestionpedidoscondao.domain.producto.ProductoDAO;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clase VentanaItemPedido que extiende de Application e implementa Initializable.
 * Esta clase se encarga de gestionar la interfaz de usuario para el manejo de ítems de pedidos
 * en una aplicación JavaFX.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 2023-11-21
 */
public class VentanaItemPedido extends Application implements Initializable {

    // Definiciones de componentes de la interfaz de usuario
    @javafx.fxml.FXML
    private TableView<ItemPedido> tbItemsPedidos;
    @javafx.fxml.FXML
    private TableColumn<ItemPedido, String> cnomProducto;
    @javafx.fxml.FXML
    private TableColumn<ItemPedido, Double> cprecioProducto;
    @javafx.fxml.FXML
    private TableColumn<ItemPedido, Integer> cCantidad;
    @javafx.fxml.FXML
    private Button btnVolver;
    private ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO();
    @javafx.fxml.FXML
    private ComboBox cbItem;
    @javafx.fxml.FXML
    private Label lbPrecio;
    @javafx.fxml.FXML
    private ComboBox cbCantidad;
    @javafx.fxml.FXML
    private Button btnBorrar;
    private PedidoDAO pedidoDAO;
    private ProductoDAO productoDAO = new ProductoDAO();
    @javafx.fxml.FXML
    private Button btnAñadir;
    private ItemPedido itemActual = null;
    private Pedido pedidoActual = null;
    @javafx.fxml.FXML
    private Button btnBorrarItem;

    /**
     * {@inheritDoc}
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método inicializador de JavaFX.
     *
     * @param primaryStage El escenario principal proporcionado por JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {    }

    /**
     * Carga y muestra los ítems de un pedido en la tabla de la interfaz gráfica.
     * Este método se llama al inicializar la ventana y se encarga de llenar la tabla
     * con los ítems correspondientes al pedido actual.
     */
    public void loadItemsPedido() {
        System.out.println("Cargando items del pedido: " + Session.getPedido());

        if (itemPedidoDAO == null) {
            System.out.println("Error: itemPedidoDAO no ha sido inicializado");
            return;
        }

        List<ItemPedido> items = itemPedidoDAO.findItemsByPedidoCodigo(Session.getPedido().getCodigo());

        cnomProducto.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProducto().getNombre()));
        cprecioProducto.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrecioTotal()));
        cCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tbItemsPedidos.setItems(FXCollections.observableArrayList(items));
    }

    /**
     * Vuelve a la ventana principal de la aplicación.
     *
     * @param actionEvent El evento de acción que desencadena este método.
     */
    @javafx.fxml.FXML
    public void volver(ActionEvent actionEvent) {
        try {
            App.changeScene("ventanaPrincipal.fxml", "Gestor de Pedidos");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadItemsPedido();
        loadNombresProductosIntoComboBox();
        agregarListenerTabla();
    }

    /**
     * Agrega un listener a la tabla de ítems de pedidos.
     * Este listener actualiza la interfaz de usuario cuando se selecciona un ítem en la tabla.
     */
    private void agregarListenerTabla() {
        tbItemsPedidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ItemPedido itemSeleccionado = tbItemsPedidos.getSelectionModel().getSelectedItem();
                actualizarInterfazUsuarioConItemSeleccionado(itemSeleccionado);
            }
        });
    }

    /**
     * Actualiza los elementos de la interfaz de usuario con los detalles del ítem seleccionado.
     *
     * @param itemSeleccionado Ítem de pedido seleccionado en la tabla.
     */
    private void actualizarInterfazUsuarioConItemSeleccionado(ItemPedido itemSeleccionado) {

        cbItem.getSelectionModel().select(itemSeleccionado.getProducto().getNombre());

        lbPrecio.setText(String.format("%.2f", itemSeleccionado.getPrecioTotal()));

        cbCantidad.getSelectionModel().select(Integer.valueOf(itemSeleccionado.getCantidad()));
    }

    /**
     * Carga los nombres de los productos en el ComboBox de la interfaz de usuario.
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
     * Agrega un listener al ComboBox de productos.
     * Este listener actualiza la interfaz de usuario según el producto seleccionado.
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
     * Actualiza el ComboBox de cantidades con los valores disponibles para el producto seleccionado.
     *
     * @param producto Producto seleccionado en el ComboBox.
     */
    private void updateCantidadComboBox(Producto producto) {
        cbCantidad.getItems().clear();
        if (producto != null) {
            for (int i = 1; i <= producto.getCantidadDisponible(); i++) {
                cbCantidad.getItems().add(i);
            }
        }
    }

    /**
     * Actualiza la etiqueta de precio en la interfaz de usuario con el precio del producto seleccionado.
     *
     * @param producto Producto seleccionado en el ComboBox.
     */
    private void loadPrecioProductosIntoLabel(Producto producto){
        if (producto != null) {
            lbPrecio.setText(String.valueOf(producto.getPrecio()));
        } else {
            lbPrecio.setText("$0.00");
        }
    }

    /**
     * Recarga y refresca la tabla de ítems de pedidos con la información actualizada.
     */
    private void recargarYRefrescarTablaItems() {

        String codigoPedido = Session.getPedido().getCodigo();
        List<ItemPedido> itemsActualizados = itemPedidoDAO.findItemsByPedidoCodigo(codigoPedido);

        tbItemsPedidos.setItems(FXCollections.observableArrayList(itemsActualizados));
        tbItemsPedidos.refresh();
    }

    /**
     * Maneja la acción de borrar un pedido completo.
     * Muestra un diálogo de confirmación antes de proceder con la eliminación.
     *
     * @param actionEvent Evento que desencadena esta acción.
     */
    @javafx.fxml.FXML
    public void onBorrarClick(ActionEvent actionEvent) {
        String codigoPedido = Session.getPedido().getCodigo();
        PedidoDAO pedidoDAO = new PedidoDAO();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("¿Deseas borrar el pedido " + codigoPedido + " del listado?");
        var result = alert.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            pedidoDAO.deleteByCodigo(codigoPedido);
            volver(null);
        }
    }

    /**
     * Maneja la acción de borrar un ítem de pedido individual.
     * Muestra un diálogo de confirmación antes de proceder con la eliminación.
     *
     * @param actionEvent Evento que desencadena esta acción.
     */
    @javafx.fxml.FXML
    public void borrar(ActionEvent actionEvent) {
        ItemPedido itemSeleccionado = getSelectedItemPedido();

        if (itemSeleccionado != null) {
            // Crear y mostrar el diálogo de confirmación
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("¿Deseas borrar el artículo " + itemSeleccionado.getProducto().getNombre() + " del listado?");
            var result = alert.showAndWait();

            // Comprobar si el usuario confirmó la acción
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                // Solo borrar el ítem si el usuario confirma la acción
                itemPedidoDAO.remove(itemSeleccionado);
                recargarYRefrescarTablaItems();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Por favor, selecciona un artículo para borrar.");
            alert.showAndWait();
        }
    }

    /**
     * Obtiene el ítem de pedido seleccionado actualmente en la tabla.
     *
     * @return ItemPedido seleccionado, o null si no hay ninguno seleccionado.
     */
    public ItemPedido getSelectedItemPedido() {
        return tbItemsPedidos.getSelectionModel().getSelectedItem();
    }

    /**
     * Maneja la acción de añadir un nuevo ítem al pedido.
     * Verifica la validez de la entrada y actualiza la tabla de ítems del pedido.
     *
     * @param actionEvent Evento que desencadena esta acción.
     */
    @javafx.fxml.FXML
    public void añadir(ActionEvent actionEvent) {

        if (pedidoDAO == null) {
            pedidoDAO = new PedidoDAO();
        }
        if (productoDAO == null) {
            productoDAO = new ProductoDAO();
        }

        String codigoPedido = Session.getPedido().getCodigo();
        pedidoActual = pedidoDAO.findByCodigo(codigoPedido);

        String nombreProducto = cbItem.getValue() != null ? cbItem.getValue().toString() : null;
        Integer cantidad = cbCantidad.getValue() != null ? (int) cbCantidad.getValue() : null;

        if (nombreProducto == null || cantidad == null || cantidad <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Entrada");
            alert.setHeaderText("Datos de Producto Inválidos");
            alert.setContentText("Por favor, selecciona un producto y especifica una cantidad válida.");
            alert.showAndWait();
            return;
        }

        Producto productoSeleccionado = productoDAO.findByName(nombreProducto);
        if (productoSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Producto");
            alert.setHeaderText("Producto No Encontrado");
            alert.setContentText("El producto seleccionado no existe. Por favor, selecciona otro producto.");
            alert.showAndWait();
            return;
        }

        ItemPedido nuevoItem = new ItemPedido();
        nuevoItem.setProducto(productoSeleccionado);
        nuevoItem.setCantidad(cantidad);
        nuevoItem.setPedido(pedidoActual);

        itemPedidoDAO.save(nuevoItem);

        recargarYRefrescarTablaItems();
    }
}