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
 * Controlador de la UI para la ventana que muestra los items de un pedido.
 * Esta clase gestiona las interacciones del usuario y la presentación de
 * los datos de los items del pedido en la tabla correspondiente.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 1.0
 */
public class VentanaItemPedido extends Application implements Initializable {

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
    private Button bntModificar;
    @javafx.fxml.FXML
    private Button btnBorrar;
    private PedidoDAO pedidoDAO;
    private ProductoDAO productoDAO = new ProductoDAO();

    /**
     * Punto de entrada principal para la aplicación JavaFX.
     *
     * @param args argumentos pasados a la aplicación.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Este método es llamado por el runtime de JavaFX al iniciar la aplicación.
     * Se usa para inicializar el estado inicial de la aplicación.
     *
     * @param primaryStage el escenario principal para esta aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        // Implementación del método start
    }

    /**
     * Carga los items del pedido en la tabla de la interfaz de usuario.
     * Hace uso de la sesión actual para obtener los datos relevantes.
     */
    public void loadItemsPedido() {
        System.out.println("Cargando items del pedido: " + Session.getPedido());

        if (itemPedidoDAO == null) {
            System.out.println("Error: itemPedidoDAO no ha sido inicializado");
            return;
        }

        List<ItemPedido> items = itemPedidoDAO.findItemsByPedidoCodigo(Session.getPedido().getCodigo());

        // Utiliza CellValueFactory para acceder al nombre y precio del Producto asociado
        cnomProducto.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProducto().getNombre()));
        cprecioProducto.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPrecioTotal()));
        cCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tbItemsPedidos.setItems(FXCollections.observableArrayList(items));
    }


    /**
     * Maneja el evento de clic en el botón de volver.
     * Cambia la vista a la ventana principal de la aplicación.
     *
     * @param actionEvent El evento que desencadenó el método.
     * @throws IOException Si ocurre un error durante el cambio de escena.
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
     * Inicializa el controlador después de que su elemento raíz ha sido
     * completamente procesado.
     *
     * @param url El location utilizado para resolver rutas relativas para el objeto raíz, o null si la location no es conocida.
     * @param resourceBundle El recurso usado para localizar el objeto raíz, o null si el recurso no fue encontrado.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadItemsPedido();
        loadNombresProductosIntoComboBox();
        agregarListenerTabla();
    }

    private void agregarListenerTabla() {
        tbItemsPedidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ItemPedido itemSeleccionado = tbItemsPedidos.getSelectionModel().getSelectedItem();
                actualizarInterfazUsuarioConItemSeleccionado(itemSeleccionado);
            }
        });
    }

    private void actualizarInterfazUsuarioConItemSeleccionado(ItemPedido itemSeleccionado) {
        // Actualizar cbItem con el producto seleccionado
        cbItem.getSelectionModel().select(itemSeleccionado.getProducto().getNombre());

        // Actualizar lbPrecio con el precio total
        lbPrecio.setText(String.format("%.2f", itemSeleccionado.getPrecioTotal()));

        // Actualizar cbCantidad con la cantidad seleccionada
        cbCantidad.getSelectionModel().select(Integer.valueOf(itemSeleccionado.getCantidad()));
    }

    private void loadNombresProductosIntoComboBox() {
        List<Producto> productos = productoDAO.getAll();
        List<String> nombreProductos = new ArrayList<>();
        for (Producto producto : productos) {
            nombreProductos.add(producto.getNombre());
        }
        cbItem.getItems().addAll(nombreProductos);
        listenerProductoSeleccionado(productos);
    }

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

    private void updateCantidadComboBox(Producto producto) {
        cbCantidad.getItems().clear();
        if (producto != null) {
            for (int i = 1; i <= producto.getCantidadDisponible(); i++) {
                cbCantidad.getItems().add(i);
            }
        }
    }

    private void loadPrecioProductosIntoLabel(Producto producto){
        if (producto != null) {
            lbPrecio.setText(String.valueOf(producto.getPrecio()));
        } else {
            lbPrecio.setText("$0.00");
        }
    }

    @javafx.fxml.FXML
    public void onModificarClick(ActionEvent actionEvent) {
        ItemPedido itemSeleccionado = tbItemsPedidos.getSelectionModel().getSelectedItem();
        if (itemSeleccionado == null) {
            return;
        }

        try {
            int nuevaCantidad = Integer.parseInt(cbCantidad.getValue().toString());
            itemSeleccionado.setCantidad(nuevaCantidad);

            // Recalcular el precio total del pedido
            double totalPedido = 0.0;
            for (ItemPedido item : itemSeleccionado.getPedido().getItemsPedidos()) {
                totalPedido += item.getPrecioTotal();
            }
            Pedido pedido = itemSeleccionado.getPedido();
            pedido.setTotal(totalPedido);

            // Actualizar en base de datos
            itemPedidoDAO.update(itemSeleccionado);
            pedidoDAO.update(pedido);

            // Actualizar la UI si es necesario
            recargarYRefrescarTablaItems();

        } catch (Exception e) {
            // Manejar la excepción, por ejemplo, mostrando un mensaje de error.
        }
    }

    private void recargarYRefrescarTablaItems() {
        // Suponiendo que tienes un método para obtener el código del pedido actual
        String codigoPedido = Session.getPedido().getCodigo();
        List<ItemPedido> itemsActualizados = itemPedidoDAO.findItemsByPedidoCodigo(codigoPedido);

        // Actualizar la tabla
        tbItemsPedidos.setItems(FXCollections.observableArrayList(itemsActualizados));
        tbItemsPedidos.refresh();
    }


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
}