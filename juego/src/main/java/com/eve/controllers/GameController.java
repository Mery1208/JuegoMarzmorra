package com.eve.controllers;

import java.util.ArrayList;

import com.eve.SceneID;
import com.eve.SceneManager;
import com.eve.model.GestorJuego;
import com.eve.model.Personaje;
import com.eve.model.Protagonista;
import com.eve.model.Proveedor;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import com.eve.interfaces.Observer;

public class GameController implements Observer {
    /** Se establecen las etiquetas FXML con los componentes de las vistas */
    @FXML
    AnchorPane anchorPane;
    @FXML
    GridPane gridPane;
    @FXML
    GridPane gridPane2;
    @FXML
    VBox vBox;
    @FXML
    Label LPerder;
    @FXML
    Button buttonMenu;

    @FXML
    public void initialize() {
        AnchorPane.setTopAnchor(gridPane, 0.0);
        AnchorPane.setBottomAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 0.0);

        AnchorPane.setTopAnchor(gridPane2, 0.0);
        AnchorPane.setBottomAnchor(gridPane2, 0.0);
        AnchorPane.setLeftAnchor(gridPane2, 0.0);
        AnchorPane.setRightAnchor(gridPane2, 0.0);

        GestorJuego gestor = Proveedor.getInstance().getGestorJuego();
        gestor.subscribe(this);
        contruirGrid();
        gestor.getEscenario().generarPosiciones();
        play();

        Platform.runLater(() -> {
            anchorPane.requestFocus();
            // Para recuperar el foco si se hace clic en cualquier parte de la escena
            anchorPane.getScene().addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                anchorPane.requestFocus();
            });
        });
    }

    /** Método para construir el grid con las imagenes del escenario */
    public void contruirGrid() {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.setPadding(new Insets(0));
        gridPane2.getChildren().clear();
        gridPane2.getColumnConstraints().clear();
        gridPane2.getRowConstraints().clear();
        gridPane2.setPadding(new Insets(0));

        double anchoDisponible = 600;
        double altoDisponible = 600;

        int numColumnas = Proveedor.getInstance().getGestorJuego().getEscenario().getEscenario().length;
        int numFilas = Proveedor.getInstance().getGestorJuego().getEscenario().getEscenario()[0].length;

        double tamcoluma = anchoDisponible / numColumnas;
        double tamfila = altoDisponible / numFilas;

        for (int i = 0; i < numColumnas; i++) {
            ColumnConstraints col = new ColumnConstraints(tamcoluma);
            col.setHalignment(HPos.CENTER);
            gridPane.getColumnConstraints().add(col);
            gridPane2.getColumnConstraints().add(col);
        }

        for (int i = 0; i < numFilas; i++) {
            RowConstraints row = new RowConstraints(tamfila);
            row.setValignment(VPos.CENTER);
            gridPane.getRowConstraints().add(row);
            gridPane2.getRowConstraints().add(row);
        }
    }

    /**
     * Método para rellenar el escenario con las imágenes.
     * 
     * @param gestor
     */
    public void pintarEscenario() {
        GestorJuego gestor = Proveedor.getInstance().getGestorJuego();
        Image suelo = new Image(getClass().getResourceAsStream(gestor.getEscenario().getSuelo()));
        Image pared = new Image(getClass().getResourceAsStream(gestor.getEscenario().getPared()));
        Image trampa = new Image(getClass().getResourceAsStream(gestor.getEscenario().getTrampa())); 
        String[][] escenario = gestor.getEscenario().getEscenario();
        gridPane.getChildren().clear();
        gridPane.setHgap(0);
        gridPane.setVgap(0);
        gridPane.setPadding(Insets.EMPTY);
        int numFilas = escenario.length;
        int numColumnas = escenario[0].length;

        double anchoDisponible = 600;
        double altoDisponible = 600;

        double tamcoluma = anchoDisponible / numColumnas;
        double tamfila = altoDisponible / numFilas;

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                ImageView imageView;
                if (escenario[i][j].equals("p")) {
                    imageView = new ImageView(pared);
                } else if (escenario[i][j].equals("t")) { 
                    imageView = new ImageView(trampa);
                } else {
                    imageView = new ImageView(suelo);
                }
                imageView.setFitWidth(tamcoluma);
                imageView.setFitHeight(tamfila);
                imageView.setPreserveRatio(false);
                gridPane.add(imageView, j, i);
            }
        }

    }

    public void pintarPersonajes() {
        GestorJuego gestor = Proveedor.getInstance().getGestorJuego();
        int numFilas = gestor.getEscenario().getEscenario().length;
        int numColumnas = gestor.getEscenario().getEscenario()[0].length;
        ArrayList<Personaje> personajes = gestor.getPersonajes();
        double anchoDisponible = 600;
        double altoDisponible = 600;
        double tamcoluma = anchoDisponible / numColumnas;
        double tamfila = altoDisponible / numFilas;

        gridPane2.getChildren().clear();

        for (int i = 0; i < personajes.size(); i++) {
            ImageView imagen = new ImageView(new Image(getClass().getResourceAsStream(personajes.get(i).getImagen())));
            imagen.setFitHeight(tamfila);
            imagen.setFitWidth(tamcoluma);
            imagen.setPreserveRatio(true);
            gridPane2.add(imagen, personajes.get(i).getPosicion()[1], personajes.get(i).getPosicion()[0]);
        }
    }

    public void play() {
        GestorJuego gestor = Proveedor.getInstance().getGestorJuego();
        vBox.setVisible(false);
        // Escucha de teclado
        anchorPane.setOnKeyPressed(event -> {

            switch (event.getCode()) {
                case W:
                case UP:
                    gestor.turno("W");
                    break;
                case A:
                case LEFT:
                    gestor.turno("A");
                    break;
                case S:
                case DOWN:
                    gestor.turno("S");
                    break;
                case D:
                case RIGHT:
                    gestor.turno("D");
                    break;
                default:
                    break;
            }

        });
        anchorPane.setFocusTraversable(true);
        anchorPane.requestFocus();

    }

    /**
     * Método para establecer el final o no del juego. Si se llega al final del
     * juego, aparece un VBox con un botón para reiniciar el juego.
     * 
     * @return finaljuego en true o false.
     */
    public boolean finalJuego() {
        boolean finalJuego = false;
        Protagonista prota = Proveedor.getInstance().getGestorJuego().buscarProta();
        int vidaProta = prota.getPuntosvida();

        if (vidaProta <= 0) {
            LPerder.setText("HAS PERDIDO");
            finalJuego = true;
        } else if (Proveedor.getInstance().getGestorJuego().getPersonajes().size() <= 1
                && Proveedor.getInstance().getGestorJuego().buscarProta().getNivel() >= 5) {
            LPerder.setText("HAS GANADO");
            finalJuego = true;
        }

        if (finalJuego) {
            vBox.setVisible(true);
            buttonMenu.setOnAction(e -> {
                SceneManager sm = SceneManager.getInstance();
                sm.setScene(SceneID.MAIN, "main");
                sm.loadScene(SceneID.MAIN);
            });
            return true;
        }

        return false;
    }

    @Override
    public void onChange() {
        if (!finalJuego()) {
            contruirGrid();
            pintarEscenario();
            pintarPersonajes();
        }
    }
}