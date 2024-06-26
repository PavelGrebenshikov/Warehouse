package com.example.warehouse;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainWindow extends Application {
    @Override
    public void start(Stage stage) {
        Panel panel = new Panel("Warehouse");
        panel.getStyleClass().add("panel-success");
        panel.getStyleClass().setAll("h4", "panel-success");
        panel.setMinSize(1200, 800);
        BorderPane content = new BorderPane();


//        buttons CRUD
        Button createButton = new Button("Добавить запись");
        Button readButton = new Button("Вывести информацию");
        Button updateButton = new Button("Обновить информацию");
        Button deleteButton = new Button("Удалить запись");


//       layouts
        HBox hBox = new HBox();
        VBox vBox = new VBox(5);

        vBox.getChildren().setAll(createButton, readButton, updateButton, deleteButton);
        hBox.getChildren().setAll(vBox);

        content.setCenter(hBox);

        /* Обновление информации */

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                BorderPane addPane = new BorderPane();

                ArrayList<String> listId = new ArrayList<>();

                BaseLogic getId = new BaseLogic();
                ResultSet getData = getId.read();

                HBox hBoxComboBox = new HBox();
                HBox hBoxDeleteButton = new HBox();

                while (true) {
                    try {
                        if (!getData.next()) break;
                        listId.add(getData.getString(1));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }

                ComboBox<String> deleteComboBox = new ComboBox<>(FXCollections.observableList(listId));
                hBoxComboBox.getChildren().setAll(deleteComboBox);
                hBoxComboBox.setAlignment(Pos.CENTER);
                deleteComboBox.getSelectionModel().selectFirst();

                Button deleteButton = new Button("Удалить запись");
                hBoxDeleteButton.getChildren().setAll(deleteButton);
                hBoxDeleteButton.setAlignment(Pos.CENTER);
                deleteButton.getStyleClass().setAll("btn-lg", "btn-danger");

                addPane.setTop(deleteComboBox);
                addPane.setCenter(deleteButton);

                deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        Integer id = Integer.parseInt(deleteComboBox.getValue());
                        BaseLogic deleteData = new BaseLogic(id);
                        deleteData.delete();

                    }
                });

                Stage addStage = new Stage();
                Scene addScene = new Scene(addPane, 800, 600);
                addScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                addStage.setTitle("Удаление информации");
                addStage.setScene(addScene);
                addStage.show();

            }
        });

        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                BorderPane addPane = new BorderPane();

                HBox inputHBox = new HBox();
                HBox listViewBox = new HBox();
                HBox updateButtonView = new HBox();

                inputHBox.setPadding(new Insets(20, 0, 20, 0));

                ListView listViewId = new ListView();
                ListView listViewWarehouse = new ListView();
                ListView listViewNameMaterials = new ListView();
                ListView countViewMaterials = new ListView();
                ListView createViewDate = new ListView();

                BaseLogic data = new BaseLogic();
                ResultSet getResult = data.read();

                ArrayList<String> listId = new ArrayList<>();

                while (true) {
                    try {
                        if (!getResult.next()) break;
                        listId.add(getResult.getString(1));
                        listViewId.getItems().add(getResult.getString(1));
                        listViewWarehouse.getItems().add(getResult.getString(2));
                        listViewNameMaterials.getItems().add(getResult.getString(3));
                        countViewMaterials.getItems().add(getResult.getString(4));
                        createViewDate.getItems().add(getResult.getString(5));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }

                ComboBox<String> listWarehouses = new ComboBox<>(FXCollections.observableList(listId));
                listWarehouses.getSelectionModel().selectFirst();
                TextField inputWarehouse = new TextField();
                TextField inputNameMaterials = new TextField();
                TextField inputCountMaterials = new TextField();
                DatePicker datePicker = new DatePicker();

                Button updateButton = new Button("Обновить");
                updateButton.getStyleClass().setAll("btn-lg", "btn-danger");

                inputHBox.getChildren().setAll(listWarehouses, inputWarehouse, inputNameMaterials, inputCountMaterials, datePicker);
                listViewBox.getChildren().setAll(listViewId, listViewWarehouse, listViewNameMaterials, countViewMaterials, createViewDate);
                updateButtonView.getChildren().setAll(updateButton);
                updateButtonView.setAlignment(Pos.CENTER);
                updateButtonView.setPadding(new Insets(20));

                addPane.setTop(inputHBox);
                addPane.setCenter(listViewBox);
                addPane.setBottom(updateButtonView);

                updateButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        try {
                            String updateWarehouse = inputWarehouse.getText();
                            String material = inputNameMaterials.getText();
                            Integer count = Integer.valueOf(inputCountMaterials.getText());
                            Integer id = Integer.parseInt(listWarehouses.getValue());
                            LocalDate date = datePicker.getValue();
                            BaseLogic updateData = new BaseLogic(id, updateWarehouse, material, count, date);
                            updateData.update();
                            Alert succesUpdate = new Alert(Alert.AlertType.INFORMATION, "Данные успешно обновлены");
                            succesUpdate.setHeaderText(null);
                            succesUpdate.show();
                        } catch (NullPointerException | NumberFormatException e) {
                            Alert warning = new Alert(Alert.AlertType.ERROR, String.valueOf(e));
                            warning.show();
                        }

                    }
                });

                Stage addStage = new Stage();
                Scene addScene = new Scene(addPane, 800, 600);
                addScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                addStage.setTitle("Обновление информации");
                addStage.setScene(addScene);
                addStage.show();

            }
        });

        readButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                BorderPane addPane = new BorderPane();
                BaseLogic readData = new BaseLogic();
                ResultSet result = readData.read();
                HBox readHBOX = new HBox();

                ListView listId = new ListView();
                ListView listWarehouse = new ListView();
                ListView listNameMaterials = new ListView();
                ListView countMaterials = new ListView();
                ListView createDate = new ListView();

                while (true) {
                    try {
                        if (!result.next()) break;
                        listId.getItems().add(result.getString(1));
                        listWarehouse.getItems().add(result.getString(2));
                        listNameMaterials.getItems().add(result.getString(3));
                        countMaterials.getItems().add(result.getString(4));
                        createDate.getItems().add(result.getString(5));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }
                readHBOX.getChildren().setAll(listId, listWarehouse, listNameMaterials, countMaterials, createDate);
                addPane.setTop(readHBOX);

                Stage addStage = new Stage();
                Scene addScene = new Scene(addPane, 800, 600);
                addScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                addStage.setTitle("Чтение информации");
                addStage.setScene(addScene);
                addStage.show();
            }
        });


        /* Создание окно добавления записи */

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                BorderPane addPane = new BorderPane();

                /* Поля ввода  */
                List<String> myList = Arrays.asList("Склад легких материалов", "Склад металла", "Склад древесины");
                ComboBox<String> listWarehouses = new ComboBox<>(FXCollections.observableList(myList));
                listWarehouses.getSelectionModel().selectFirst();
                TextField inputMaterials = new TextField();
                TextField addCounterMaterials = new TextField();
                DatePicker datePicker = new DatePicker();

                // Size UI
                listWarehouses.setMinSize(320, 40);
                inputMaterials.setMinSize(320, 40);
                addCounterMaterials.setMinSize(320, 40);
                datePicker.setMinSize(320, 40);

                // Font
                Font font = new Font("Arial", 18);
                inputMaterials.setFont(font);
                addCounterMaterials.setFont(font);

                // Placeholders
                inputMaterials.setPromptText("Введите название матерала");
                addCounterMaterials.setPromptText("Введите количество материала");

                /* Метки */
                Label choiceWarehouse = new Label("Выбор склада", listWarehouses);
                Label writeMaterialsToWarehouse = new Label("Запись материала", inputMaterials);
                Label counterMaterials = new Label("Количество материала", addCounterMaterials);
                Label dateAddingMaterials = new Label("Дата добавления", datePicker);

                /* Paddings */
                choiceWarehouse.setPadding(new Insets(30));
                writeMaterialsToWarehouse.setPadding(new Insets(30));
                counterMaterials.setPadding(new Insets(30));
                dateAddingMaterials.setPadding(new Insets(30));

                /* Кнопка записи */
                Button writeButton = new Button("Записать данные");
                writeButton.getStyleClass().setAll("btn", "btn-danger");
                writeButton.setMinSize(520, 50);

                VBox hBoxSecondWindow = new VBox();
                VBox vBoxSecondWindow1 = new VBox();
                HBox hBoxSecondWindowTextFields = new HBox();
                HBox hBoxSecondWindowButton = new HBox();

                hBoxSecondWindow.getChildren().setAll(vBoxSecondWindow1, hBoxSecondWindowTextFields);
                vBoxSecondWindow1.getChildren().setAll(choiceWarehouse, writeMaterialsToWarehouse, counterMaterials, dateAddingMaterials, datePicker);

                hBoxSecondWindowTextFields.getChildren().setAll(listWarehouses, inputMaterials, addCounterMaterials);
                hBoxSecondWindowTextFields.setAlignment(Pos.CENTER);

                hBoxSecondWindowButton.getChildren().add(writeButton);
                hBoxSecondWindowButton.setPadding(new Insets(20));
                hBoxSecondWindowButton.setAlignment(Pos.CENTER);

                addPane.setTop(hBoxSecondWindow);
                addPane.setBottom(hBoxSecondWindowButton);

                writeButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        try {
                            String nameWarehouse = listWarehouses.getValue();
                            String nameMaterials = inputMaterials.getText();
                            Integer countMaterials = Integer.parseInt(addCounterMaterials.getText());
                            LocalDate date = datePicker.getValue();

                            if (nameMaterials == null || nameMaterials.isEmpty()) {
                                Alert warningEmptyOrNull = new Alert(Alert.AlertType.ERROR, "Строчка 'Название материала' пуста");
                                warningEmptyOrNull.show();
                            }

                            BaseLogic logic = new BaseLogic(nameWarehouse, nameMaterials, countMaterials, date);
                            logic.create();
                            Alert success = new Alert(Alert.AlertType.INFORMATION, "Запись создана");
                            success.setHeaderText(null);
                            success.show();
                        } catch (NullPointerException | NumberFormatException e) {
                            Alert warning = new Alert(Alert.AlertType.ERROR, String.valueOf(e));
                            warning.show();
                        }


                    }
                });


                Stage addStage = new Stage();
                Scene addScene = new Scene(addPane, 800, 600);
                addScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                addStage.setTitle("Сохранить");
                addStage.setScene(addScene);
                addStage.show();

            }
        });




        panel.setBody(content);

        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Склад материалов");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}