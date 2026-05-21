package com.example.practica5ordenamiento;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.ToLongFunction;

public class HelloApplication extends Application {

    private ArrayList<VideoJuego> listaJuegos;
    private TableView<VideoJuego> tabla;
    private ComboBox<String> comboColumna;
    private BarChart<String, Number> grafica;
    private Label etiquetaTiempos;

    private static final String[] COLUMNAS = {
            "Title", "Release Date", "Team", "Rating", "Times Listed",
            "Number of Reviews", "Summary", "Reviews",
            "Plays", "Playing", "Backlogs", "Wishlist"
    };

    @Override
    public void start(Stage stage) throws Exception {
        try {
            LectorArchivo lector = new LectorArchivo();
            listaJuegos = lector.getJuegos();
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setContentText("no cargo arch " + e.getMessage());
            alerta.showAndWait();
            return;
        }

        Label lblColumna = new Label("Nombre de la columna a ordenar:");
        comboColumna = new ComboBox<>(FXCollections.observableArrayList(COLUMNAS));
        comboColumna.setValue("Title");

        Button btnOrdenar = new Button("Ordenar");
        btnOrdenar.setOnAction(e -> ejecutarOrdenamiento());

        HBox panelSuperior = new HBox(10, lblColumna, comboColumna, btnOrdenar);
        panelSuperior.setPadding(new Insets(10));
        panelSuperior.setAlignment(Pos.CENTER_LEFT);

        //tabla 13 columnas del CSV
        tabla = new TableView<>();

        TableColumn<VideoJuego, String> colIndex = new TableColumn<>("#");
        colIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
        colIndex.setPrefWidth(50);

        TableColumn<VideoJuego, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setPrefWidth(180);

        TableColumn<VideoJuego, String> colDate = new TableColumn<>("Release Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        colDate.setPrefWidth(100);

        TableColumn<VideoJuego, String> colTeam = new TableColumn<>("Team");
        colTeam.setCellValueFactory(new PropertyValueFactory<>("team"));
        colTeam.setPrefWidth(150);

        TableColumn<VideoJuego, Double> colRating = new TableColumn<>("Rating");
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colRating.setPrefWidth(70);

        TableColumn<VideoJuego, String> colListed = new TableColumn<>("Times Listed");
        colListed.setCellValueFactory(new PropertyValueFactory<>("timesListed"));
        colListed.setPrefWidth(90);

        TableColumn<VideoJuego, String> colReviews = new TableColumn<>("Number of Reviews");
        colReviews.setCellValueFactory(new PropertyValueFactory<>("numberOfReviews"));
        colReviews.setPrefWidth(120);

        TableColumn<VideoJuego, String> colSummary = new TableColumn<>("Summary");
        colSummary.setCellValueFactory(new PropertyValueFactory<>("summary"));
        colSummary.setPrefWidth(200);

        TableColumn<VideoJuego, String> colRevText = new TableColumn<>("Reviews");
        colRevText.setCellValueFactory(new PropertyValueFactory<>("reviews"));
        colRevText.setPrefWidth(200);

        TableColumn<VideoJuego, String> colPlays = new TableColumn<>("Plays");
        colPlays.setCellValueFactory(new PropertyValueFactory<>("plays"));
        colPlays.setPrefWidth(70);

        TableColumn<VideoJuego, String> colPlaying = new TableColumn<>("Playing");
        colPlaying.setCellValueFactory(new PropertyValueFactory<>("playing"));
        colPlaying.setPrefWidth(70);

        TableColumn<VideoJuego, String> colBacklogs = new TableColumn<>("Backlogs");
        colBacklogs.setCellValueFactory(new PropertyValueFactory<>("backlogs"));
        colBacklogs.setPrefWidth(70);

        TableColumn<VideoJuego, String> colWishlist = new TableColumn<>("Wishlist");
        colWishlist.setCellValueFactory(new PropertyValueFactory<>("wishlist"));
        colWishlist.setPrefWidth(70);

        tabla.getColumns().addAll(colIndex, colTitle, colDate, colTeam, colRating,
                colListed, colReviews, colSummary, colRevText,
                colPlays, colPlaying, colBacklogs, colWishlist);
        tabla.setItems(FXCollections.observableArrayList(listaJuegos));
        tabla.setPrefHeight(150);

        etiquetaTiempos = new Label("");
        etiquetaTiempos.setPadding(new Insets(5, 10, 5, 10));

        //grafica de barras
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Algoritmos");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tiempo (Nanosegundos)");

        grafica = new BarChart<>(xAxis, yAxis);
        grafica.setTitle("Rendimiento de Algoritmos (Nanosegundos)");
        grafica.setLegendVisible(false);
        grafica.setPrefHeight(250);
        //Ly principal
        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(10));
        raiz.getChildren().addAll(
                panelSuperior,
                new Label("Datos del dataset (" + listaJuegos.size() + " videojuegos):"),
                tabla,
                etiquetaTiempos,
                grafica
        );

        Scene escena = new Scene(raiz, 900, 750);
        stage.setTitle("Lesly Lopez P5 - Métodos de Ordenamiento");
        stage.setScene(escena);
        stage.show();
    }

    private void ejecutarOrdenamiento() {
        String columna = comboColumna.getValue();
        Comparator<VideoJuego> comparador = comparadorPara(columna);
        ToLongFunction<VideoJuego> extractor = extractorPara(columna);
        VideoJuego[] arreglo = listaJuegos.toArray(new VideoJuego[0]);

        Task<String> tarea = new Task<>() {
            @Override
            protected String call() {

                long tiempoQuickSort = medir(() -> MetodoDeOrdenamiento.quickSort(arreglo.clone(), comparador));
                long tiempoMergeSort = medir(() -> MetodoDeOrdenamiento.mergeSort(arreglo.clone(), comparador));
                long tiempoShellSort = medir(() -> MetodoDeOrdenamiento.shellSort(arreglo.clone(), comparador));
                long tiempoSeleccion = medir(() -> MetodoDeOrdenamiento.seleccionDirecta(arreglo.clone(), comparador));
                long tiempoRadix = extractor != null ? medir(() -> MetodoDeOrdenamiento.radixSort(arreglo.clone(), extractor)) : -1;
                long tiempoArraysSort = medir(() -> MetodoDeOrdenamiento.ordenarJava(arreglo.clone(), comparador));
                long tiempoParallelSort = medir(() -> MetodoDeOrdenamiento.ordenarJavaParalelo(arreglo.clone(), comparador));

                //actualizar gr
                javafx.application.Platform.runLater(() -> {
                    grafica.getData().clear();
                    XYChart.Series<String, Number> serie = new XYChart.Series<>();

                    serie.getData().add(new XYChart.Data<>("QuickSort",       tiempoQuickSort));
                    serie.getData().add(new XYChart.Data<>("MergeSort",       tiempoMergeSort));
                    serie.getData().add(new XYChart.Data<>("Shell Sort",      tiempoShellSort));
                    serie.getData().add(new XYChart.Data<>("Sel. Directa",    tiempoSeleccion));
                    if (tiempoRadix >= 0)
                        serie.getData().add(new XYChart.Data<>("Radix Sort",  tiempoRadix));
                    serie.getData().add(new XYChart.Data<>("sort()",          tiempoArraysSort));
                    serie.getData().add(new XYChart.Data<>("parallelSort()",  tiempoParallelSort));

                    grafica.getData().add(serie);
                    serie.getData().forEach(d -> d.getNode().setStyle("-fx-bar-fill: #9B59B6;"));
                });

                //print tiempos
                StringBuilder sb = new StringBuilder("Tiempos (nanosegundos):\n");
                sb.append("1. QuickSort: ").append(String.format("%,d", tiempoQuickSort)).append(" ns\n");
                sb.append("2. MergeSort: ").append(String.format("%,d", tiempoMergeSort)).append(" ns\n");
                sb.append("3. Shell Sort: ").append(String.format("%,d", tiempoShellSort)).append(" ns\n");
                sb.append("4. Seleccion Directa: ").append(String.format("%,d", tiempoSeleccion)).append(" ns\n");
                if (tiempoRadix >= 0)
                    sb.append("5. Radix Sort: ").append(String.format("%,d", tiempoRadix)).append(" ns\n");
                sb.append("6. sort(): ").append(String.format("%,d", tiempoArraysSort)).append(" ns\n");
                sb.append("7. parallelSort(): ").append(String.format("%,d", tiempoParallelSort)).append(" ns\n");

                return sb.toString();
            }
        };

        tarea.setOnSucceeded(e -> {
            etiquetaTiempos.setText(tarea.getValue());
            VideoJuego[] ordenado = arreglo.clone();
            MetodoDeOrdenamiento.ordenarJava(ordenado, comparador);
            tabla.setItems(FXCollections.observableArrayList(ordenado));
        });

        new Thread(tarea, "ordenamiento").start();
    }
    //tiempo de ejecucion
    private long medir(Runnable algoritmo) {
        long inicio = System.nanoTime();
        algoritmo.run();
        return System.nanoTime() - inicio;
    }

    private Comparator<VideoJuego> comparadorPara(String columna) {
        return switch (columna) {
            case "Title" -> Comparator.comparing(j -> j.getTitle().toLowerCase());
            case "Release Date" -> Comparator.comparing(VideoJuego::getReleaseDate);
            case "Team" -> Comparator.comparing(j -> j.getTeam().toLowerCase());
            case "Summary" -> Comparator.comparing(j -> j.getSummary().toLowerCase());
            case "Reviews" -> Comparator.comparing(j -> j.getReviews().toLowerCase());
            case "Rating" -> Comparator.comparingDouble(VideoJuego::getRating);
            case "Times Listed" -> Comparator.comparingLong(j -> VideoJuego.convertirANumero(j.getTimesListed()));
            case "Number of Reviews" -> Comparator.comparingLong(j -> VideoJuego.convertirANumero(j.getNumberOfReviews()));
            case "Plays" -> Comparator.comparingLong(j -> VideoJuego.convertirANumero(j.getPlays()));
            case "Playing" -> Comparator.comparingLong(j -> VideoJuego.convertirANumero(j.getPlaying()));
            case "Backlogs" -> Comparator.comparingLong(j -> VideoJuego.convertirANumero(j.getBacklogs()));
            case "Wishlist" -> Comparator.comparingLong(j -> VideoJuego.convertirANumero(j.getWishlist()));
            default -> Comparator.comparing(j -> j.getTitle().toLowerCase());
        };
    }

    private ToLongFunction<VideoJuego> extractorPara(String columna) {
        return switch (columna) {
            case "Rating" -> j -> (long)(j.getRating() * 100);
            case "Times Listed" -> j -> VideoJuego.convertirANumero(j.getTimesListed());
            case "Number of Reviews" -> j -> VideoJuego.convertirANumero(j.getNumberOfReviews());
            case "Plays" -> j -> VideoJuego.convertirANumero(j.getPlays());
            case "Playing" -> j -> VideoJuego.convertirANumero(j.getPlaying());
            case "Backlogs" -> j -> VideoJuego.convertirANumero(j.getBacklogs());
            case "Wishlist" -> j -> VideoJuego.convertirANumero(j.getWishlist());
            default -> null;
        };
    }

}