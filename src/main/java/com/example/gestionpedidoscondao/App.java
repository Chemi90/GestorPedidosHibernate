package com.example.gestionpedidoscondao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación que extiende de {@link Application}.
 * Inicializa la interfaz de usuario y maneja la navegación entre las vistas.
 *
 * <p>Esta clase se encarga de configurar el escenario principal ({@link Stage})
 * y cambiar las escenas ({@link Scene}) de la aplicación.</p>
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 2023-11-21
 * @see Application
 */
public class App extends Application {
    private static Stage stage; // El escenario principal de la aplicación.

    /**
     * Inicia la aplicación JavaFX configurando y mostrando la ventana de inicio de sesión.
     *
     * @param stage El escenario principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controllers/ventanaLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 907, 497);
        stage.setResizable(false);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Cambia la escena actual del escenario principal a la especificada por el archivo FXML y establece un nuevo título.
     *
     * @param fxml  El nombre del archivo FXML que define la nueva escena.
     * @param title El título de la nueva escena.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    public static void changeScene(String fxml, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controllers/" + fxml));
        Scene scene = new Scene(fxmlLoader.load(), 907, 497);
        stage.setResizable(false);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Punto de entrada principal de la aplicación.
     *
     * @param args Argumentos pasados a la aplicación.
     */
    public static void main(String[] args) {
        launch(args);
    }
}