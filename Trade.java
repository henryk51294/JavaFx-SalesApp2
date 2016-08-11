package trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Trade extends Application {

    public Stage window;

    String GbName, GbAccess;
    int GbUserId;
    double GbSum;
//    Class - scene instantiations
    Login login = new Login();
    ManageProducts manageProducts = new ManageProducts();

//    Establishing database links
    Connection conn = DbConnector.establishConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;

    @Override
    public void start(Stage stage) {
        window = stage;
        window.setTitle("Trade Application");
        window.setScene(login.login());
        window.show();
    }

    class Login {

        TextField username;
        PasswordField pass;
        Label welcome, feedback;
        Button signin;

        public Scene login() {
            BorderPane root = new BorderPane();
            Scene login = new Scene(root, 300, 250);
            login.getStylesheets().add(getClass().getResource("login.css").toExternalForm());

            HBox topHBox = new HBox(15);
            topHBox.setPadding(new Insets(10, 10, 10, 10));
            welcome = new Label("Sales Application");
            welcome.setId("welcome");
            topHBox.getChildren().addAll(welcome);
            root.setTop(topHBox);

            GridPane gridPane = new GridPane();
            gridPane.setVgap(15);
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            BorderPane.setAlignment(gridPane, Pos.CENTER);

            username = new TextField();
            username.setId("username");
            username.setPromptText("Username");
            username.setMaxWidth(300);
            username.setOnKeyPressed(e -> {
                feedback.setText("");
            });
            GridPane.setConstraints(username, 0, 0);

            pass = new PasswordField();
            pass.setId("pass");
            pass.setPromptText("Password");
            pass.setMaxWidth(300);
            GridPane.setConstraints(pass, 0, 1);
            pass.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    validateLogin();
                }
            });

            HBox bottomHBox = new HBox(15);
            signin = new Button("Login");
            signin.setMaxWidth(150);
            signin.setOnAction(e -> {
                validateLogin();
            });

            bottomHBox.getChildren().addAll(signin);
            GridPane.setConstraints(bottomHBox, 0, 2);

            feedback = new Label();
            feedback.setId("feedback");
            GridPane.setConstraints(feedback, 0, 3);

            gridPane.getChildren().addAll(username, pass, bottomHBox, feedback);
            root.setCenter(gridPane);

            return login;
        }

        public void validateLogin() {
            try {
                String qry = "SELECT * FROM user WHERE username = ? AND pass = ?; SELECT SUM(amount) AS total_Sm FROM product"; 
                String qry2 = "SELECT SUM(amount) AS total FROM product";
                pst = conn.prepareStatement(qry);
                pst.setString(1, username.getText());
                pst.setString(2, pass.getText());

                rs = pst.executeQuery();
                if (rs.next()) {
                    GbName = rs.getString("fname");
                    GbUserId = rs.getInt("id");
                    GbAccess = rs.getString("access");
//                    GbSum = rs.getDouble("amount");
                    window.setScene(manageProducts.manageProducts());
                    feedback.setText("Login successful");
                } else {
                    feedback.setText("Login failed");
                    clearLoginFields();
                }
            } catch (Exception x) {
                x.printStackTrace();
            }
        }

        public void clearLoginFields() {
            username.clear();
            pass.clear();
        }
    }

    class ManageProducts {

        public void clearInsertFields() {
            pname.clear();
            price.clear();
            units.clear();
        }

        TableView<Product> table = new TableView<>();
        final ObservableList<Product> data = FXCollections.observableArrayList();

        TextField id, pname, price, units, amount;

        public Scene manageProducts() {
            BorderPane root = new BorderPane();
            Scene manageProducts = new Scene(root, 800, 400);

            TableView<Product> table = new TableView<>();
            final ObservableList<Product> data = FXCollections.observableArrayList();

            VBox vBoxContainer = new VBox();
            vBoxContainer.setPadding(new Insets(10, 10, 10, 10));
//Top Items
            HBox hBoxTableTitle = new HBox(120);
            hBoxTableTitle.setPadding(new Insets(10, 10, 10, 10));
            Label title = new Label("Sales Application");

            Label welcome = new Label("Welcome : " + GbName);

            Button logout = new Button("Logout");
            logout.setOnAction(e -> {
                window.setScene(login.login());
            });

            hBoxTableTitle.getChildren().addAll(title, welcome, logout);
//Structuring Table
            TableColumn col1 = new TableColumn("Id");
            col1.setMinWidth(100);
            col1.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn col2 = new TableColumn("Product");
            col2.setMinWidth(100);
            col2.setCellValueFactory(new PropertyValueFactory<>("pname"));

            TableColumn col3 = new TableColumn("Price");
            col3.setMinWidth(100);
            col3.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn col4 = new TableColumn("Units");
            col4.setMinWidth(100);
            col4.setCellValueFactory(new PropertyValueFactory<>("units"));

            TableColumn col5 = new TableColumn("Amount");
            col5.setMinWidth(100);
            col5.setCellValueFactory(new PropertyValueFactory<>("amount"));
            
            TableColumn col6 = new TableColumn("SalesAgent");
            col6.setMinWidth(100);
            col6.setCellValueFactory(new PropertyValueFactory<>("fname"));

            table.getColumns().addAll(col1, col2, col3, col4, col5, col6);

            BorderPane.setMargin(table, new Insets(10, 10, 10, 10));

            vBoxContainer.getChildren().addAll(hBoxTableTitle, table);

            root.setCenter(vBoxContainer);
// Vertical box for adding new User           
            VBox newProduct = new VBox(20);
            newProduct.setPadding(new Insets(10, 10, 10, 10));

            HBox hBoxAddNewProductTitle = new HBox(80);
            hBoxAddNewProductTitle.setPadding(new Insets(10, 10, 10, 240));
            Label title2 = new Label("Add new Product Sales");
            Label sum = new Label();
            hBoxAddNewProductTitle.getChildren().addAll(title2);

            HBox hBoxAddProduct = new HBox(30);
            hBoxAddProduct.setPadding(new Insets(10, 10, 10, 10));

            pname = new TextField();
            pname.setMinWidth(150);
            pname.setPromptText("Product");

            price = new TextField();
            price.setMinWidth(150);
            price.setPromptText("Price");

            units = new TextField();
            units.setMinWidth(150);
            units.setPromptText("Units");

            Button save = new Button("Add");
            save.setPrefWidth(100);
            save.setPrefHeight(25);
            save.setOnAction(e -> {
                try {
                    String qry = "INSERT INTO product(pname, price, units, amount, userId)"
                            + "VALUES (?,?,?,?,?)";

                    pst = conn.prepareStatement(qry);
                    pst.setString(1, pname.getText());
                    pst.setString(2, price.getText());
                    pst.setString(3, units.getText());
                    pst.setDouble(4, (Double.parseDouble(price.getText()) * (double) Integer.parseInt(units.getText())));
                    pst.setInt(5, GbUserId);
                    System.out.println("Insert complete");
                    pst.execute();
                    pst.close();
                } catch (Exception x) {
                    x.printStackTrace();
                }
                clearInsertFields();
            });

            Button load = new Button("Load Records");
            load.setPrefWidth(100);
            load.setPrefHeight(25);
            load.setOnAction(e -> {
                data.clear();
                if ("admin".equals(GbAccess)) {
                    String qry4 = "SELECT product.id, pname, price, units, amount, fname "
                            + "FROM product, user "
                            + "WHERE product.userId = user.id";
                    try {                        
                        pst = conn.prepareStatement(qry4);
                        rs = pst.executeQuery();

                        while (rs.next()) {
                            data.add(new Product(
                                    rs.getInt("id"),
                                    rs.getString("pname"),                                    
                                    rs.getDouble("price"),
                                    rs.getInt("units"),                                    
                                    rs.getDouble("amount"),                                    
                                    rs.getString("fname")
                                    
                            ));
                            table.setItems(data);
                            table.setEditable(true);
                        }
                        pst.close();
                        rs.close();

                    } catch (Exception x) {
                        x.printStackTrace();
                    }
                } else {
                    String qry = "SELECT product.id, pname, price, units, amount, fname "
                            + "FROM product, user "
                            + "WHERE product.userId = user.id "
                            + "AND userId = '"+GbUserId+"'";
                    
                    try {
                        pst = conn.prepareStatement(qry);
                        rs = pst.executeQuery();

                        while (rs.next()) {
                            data.add(new Product(
                                    rs.getInt("id"),
                                    rs.getString("pname"),
                                    rs.getDouble("price"),
                                    rs.getInt("units"),
                                    rs.getDouble("amount"),
                                    rs.getString("fname")
                            ));
                            table.setItems(data);
                            table.setEditable(true);
                        }
                        pst.close();
                        rs.close();

                    } catch (Exception x) {
                        x.printStackTrace();
                    }
                }
            });

        hBoxAddProduct.getChildren ().addAll(pname, price, units, save, load);

        newProduct.getChildren ().addAll(hBoxAddNewProductTitle, hBoxAddProduct);

        root.setBottom (newProduct);

        return manageProducts ;
    }
}

public static void main(String[] args) {
        launch(args);
    }

}
