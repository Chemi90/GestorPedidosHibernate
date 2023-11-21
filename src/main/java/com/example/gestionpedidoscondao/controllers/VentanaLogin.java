package com.example.gestionpedidoscondao.controllers;

import com.example.gestionpedidoscondao.App;
import com.example.gestionpedidoscondao.Session;
import com.example.gestionpedidoscondao.domain.usuario.Usuario;
import com.example.gestionpedidoscondao.domain.usuario.UsuarioDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

/**
 * Clase de controlador para la ventana de login de la aplicación.
 * Proporciona los métodos de manejo para la interfaz de usuario de login,
 * permitiendo a los usuarios iniciar sesión o cancelar la operación.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 2023-11-21
 */
public class VentanaLogin {

    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField tfPass;
    @FXML
    private Button btnSession;
    @FXML
    private Button bntCancel;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Maneja el evento de clic en el botón de inicio de sesión.
     * Valida las credenciales del usuario y, si son correctas,
     * cambia a la ventana principal de la aplicación.
     * Muestra una alerta si las credenciales son incorrectas.
     *
     * @throws Exception Si ocurre un error al cambiar de escena.
     */
    @FXML
    private void onLoginButtonClick() throws IOException {
        String nombre = tfUser.getText();
        String password = tfPass.getText();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario user = usuarioDAO.validateUser(nombre, password);

        if (user != null) {
            Session.setUser(user);

            App.changeScene("ventanaPrincipal.fxml", "Gestor de Pedidos");
        } else {
            // Mostrar mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Login");
            alert.setHeaderText(null);
            alert.setContentText("Usuario o contraseña incorrectos.");
            alert.showAndWait();
        }
    }

    /**
     * Maneja el evento de clic en el botón de cancelar.
     * Limpia los campos de texto y cierra la aplicación.
     *
     * @param actionEvent El evento de acción que desencadenó este método.
     */
    @FXML
    public void onCancelButtonClick(ActionEvent actionEvent) {
        // Acción para el botón "Cancelar" (puedes agregar lo que sea necesario)
        tfUser.clear(); // Limpia los campos de usuario y contraseña
        tfPass.clear();
        // Cierra la aplicación
        Platform.exit();
    }
}