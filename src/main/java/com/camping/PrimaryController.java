package com.camping;

import com.camping.model.Client;
import com.camping.model.Client.MODE;
import com.camping.model.Parcel;
import com.camping.model.Reservation;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.javafx.StackedFontIcon;

public class PrimaryController {
    @FXML GridView<Parcel> gvParcel;
    
    private DataModel dataModel; 
    private ResourceBundle resourceBundle;    
    private ObservableList<Parcel> parcelList;
    private ObservableList<Client> clientList;   
    
    
    public void initDataModel(DataModel dataModel, ResourceBundle resourceBundle) {
        // ensure model is only set once:
        if (this.dataModel != null) {
            throw new IllegalStateException(resourceBundle.getString("modelIllegalStateExceptionText"));
        }
        this.dataModel = dataModel ;
         
        if (this.resourceBundle != null) {
            throw new IllegalStateException(resourceBundle.getString("resourcebundleIllegalStateExceptionText"));
        }
        this.resourceBundle = resourceBundle;
       
        this.clientList = dataModel.getClientList();
        clientList.addAll(dataModel.dbGetAllClients());
        
        setupGridView();
    }
    
    private void setupGridView(){
        parcelList = dataModel.getParcelList();        
        gvParcel.setItems(parcelList);
        gvParcel.setCellFactory(gv -> new GridCell<Parcel>()
        {
            Label lblRoomId = new Label(); 
            FontIcon iconDeleteRoom = new FontIcon("antf-close-circle");
            StackPane spTopRoot = new StackPane(lblRoomId, iconDeleteRoom);
            
            Label lblStatusTitle = new Label();
            Label lblCurrentStatus = new Label();
            FontIcon iconHome = new FontIcon("antf-home");
            FontIcon iconInfo = new FontIcon("antf-info-circle");
            StackedFontIcon stackedFontIcon = new StackedFontIcon();
            VBox vbCenterRoot = new VBox(lblStatusTitle, lblCurrentStatus, stackedFontIcon);
            
            Button btnReserveParcel = new Button();            
            BorderPane root = new BorderPane();
            
            {
                lblRoomId.setMaxWidth(Double.MAX_VALUE);
                lblRoomId.setAlignment(Pos.CENTER);
                lblRoomId.setFont(Font.font("System", FontWeight.BOLD, 35));
                
                iconDeleteRoom.setIconSize(25);
                StackPane.setAlignment(iconDeleteRoom, Pos.TOP_RIGHT);
                spTopRoot.setPadding(new Insets(10, 10, 0, 0));
                
                lblStatusTitle.setFont(Font.font("System", FontWeight.NORMAL, 16));
                lblCurrentStatus.setFont(Font.font("System", FontWeight.NORMAL, 28));
                iconInfo.setIconSize(100);
                iconHome.setIconSize(100);
                iconInfo.setIconColor(Color.GREEN);
                    
                stackedFontIcon.getChildren().addAll(iconHome, iconInfo);
                
                vbCenterRoot.setAlignment(Pos.CENTER);
                vbCenterRoot.setMaxHeight(Double.MAX_VALUE);
                
                btnReserveParcel.setFont(Font.font("System", FontWeight.NORMAL, 28));
                btnReserveParcel.setMaxWidth(Double.MAX_VALUE);
                btnReserveParcel.setPrefHeight(100);
                btnReserveParcel.setStyle("-fx-background-color: green;\n-fx-border-radius:0 0 10 10;\n-fx-background-radius: 0 0 10 10;");

                root.setTop(spTopRoot);
                root.setCenter(vbCenterRoot);
                root.setBottom(btnReserveParcel);
                root.setStyle("-fx-border-color: black;\n-fx-border-width: 5;\n-fx-border-radius: 10;");
                root.setMaxSize(400, 400);
            }            
            
            @Override
            public void updateItem(Parcel item, boolean empty)
            {
                super.updateItem(item, empty);
                if(empty || item == null)
                {
                    setText(null);
                    setGraphic(null);
                }
                else
                {
                    lblRoomId.setText(resourceBundle.getString("stringRoomId")+ item.getName());
                    btnReserveParcel.setText(item.isOccupied()? resourceBundle.getString("stringCheckout") : resourceBundle.getString("stringReserve"));
                    lblCurrentStatus.setText(item.isOccupied()? resourceBundle.getString("stringOccupied") : resourceBundle.getString("stringReserve"));
             
                    iconHome.setVisible(!item.isOccupied());
                    iconInfo.setVisible(item.isOccupied());  
                    if(iconInfo.isVisible()){
                        iconInfo.setOnMouseClicked(mouseClickedEvent ->{
                            showReservationInfo(item);
                        });
                    }
                    btnReserveParcel.setOnMouseClicked((t) -> {
                        dataModel.setCurrentParcel(item);
                        if(!item.isOccupied()){                            
                            reserveRoom();                       
                        }
                        else{                     
                            cancelOrCheckout(item);
                        }
                                           
                    });
                    iconDeleteRoom.setOnMouseClicked((t) -> {
                        deleteRoom(item);
                    });
                    setGraphic(root);
                }
            }           
        });
        
        parcelList.addAll(dataModel.dbGetAllParcels());
    }
    
    private ListView<Client> setupClientListView(MODE mode){
        ListView<Client> lvClient = new ListView();
        lvClient.setItems(clientList);        
        
        lvClient.setCellFactory((lv) -> new ListCell<Client>(){
            Label lblClientFirstName = new Label(); 
            Label lblClientLastName = new Label();
            HBox hBox = new HBox(lblClientFirstName, lblClientLastName);            
            FontIcon iconDeleteClient = new FontIcon("antf-close-circle");
            StackPane spTopRoot = new StackPane(hBox, iconDeleteClient);
            
            Label lblClientPhoneNumber = new Label();
            Label lblClientDni = new Label();
            Label lblClientVehiclePlateNumber = new Label();
            VBox vbCenterRoot = new VBox(lblClientPhoneNumber, lblClientDni, lblClientVehiclePlateNumber);           
            Button btnMode = new Button();            
            
            BorderPane bpListViewRoot = new BorderPane();
            {
                hBox.setMaxWidth(Double.MAX_VALUE);
                hBox.setAlignment(Pos.CENTER);
                lblClientFirstName.setFont(Font.font("System", FontWeight.BOLD, 17));
                lblClientLastName.setFont(Font.font("System", FontWeight.BOLD, 17));
                iconDeleteClient.setIconSize(15);
                StackPane.setAlignment(iconDeleteClient, Pos.TOP_RIGHT);
                spTopRoot.setPadding(new Insets(10, 10, 0, 0));
                
                lblClientPhoneNumber.setFont(Font.font("System", FontWeight.NORMAL, 12));
                lblClientDni.setFont(Font.font("System", FontWeight.NORMAL, 12));
                lblClientVehiclePlateNumber.setFont(Font.font("System", FontWeight.NORMAL, 12));
                vbCenterRoot.setAlignment(Pos.CENTER);
                vbCenterRoot.setMaxHeight(Double.MAX_VALUE);
                
                btnMode.setFont(Font.font("System", FontWeight.NORMAL, 12));
                btnMode.setMaxWidth(Double.MAX_VALUE);
                btnMode.setPrefHeight(35);
                switch (mode) {
                    case SELECT_CLIENT:
                        btnMode.setText(resourceBundle.getString("btnModeTextSelect"));
                        break;
                    case EDIT_CLIENT:
                        btnMode.setText(resourceBundle.getString("btnModeTextEdit"));
                        break;
                    default:
                        throw new AssertionError();
                }
                bpListViewRoot.setTop(spTopRoot);
                bpListViewRoot.setCenter(vbCenterRoot);
                bpListViewRoot.setBottom(btnMode);
                bpListViewRoot.setStyle("-fx-border-color: black;\n-fx-border-width: 5;\n-fx-border-radius: 10;");
                bpListViewRoot.setMaxSize(400, 400);
            }
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null)
                {
                    setText(null);
                    setGraphic(null);
                }
                else
                {
                    lblClientFirstName.setText(item.getFirstName() + " ");
                    lblClientLastName.setText(item.getLastName());
                    lblClientPhoneNumber.setText(item.getPhoneNumber());
                    lblClientDni.setText(item.getDni());
                    lblClientVehiclePlateNumber.setText(item.getVehiclePlateNumber());
                    switch (mode) {
                        case SELECT_CLIENT:
                            btnMode.setDisable(true);
                            break;
                        case EDIT_CLIENT:
                            btnMode.setOnAction((t) -> {
                                editClient(item);
                            });
                            break;
                        default:
                            throw new AssertionError();
                    }
                    iconDeleteClient.setOnMouseClicked((t) -> {
                        deleteClient(item);
                    });
                    setGraphic(bpListViewRoot);
                }
            }
            
        });
        lvClient.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
            dataModel.setCurrentClient(newSelection));
        
        return lvClient;
    }    
    
    private void showReservationInfo(Parcel parcel){
        Client client = dataModel.getClient(parcel);
        if(client.getId() != -1){
            BorderPane root = new BorderPane();          

            // Create the custom dialog.
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle(resourceBundle.getString("reservationInfoDialogTitle"));
            dialog.setHeaderText(resourceBundle.getString("reservationInfoDialogHeader") + " " + parcel.getName());

            // Set the button types.
            ButtonType btntDone = new ButtonType(resourceBundle.getString("reservationInfoDialogDoneBtn"), ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btntDone);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField tfRoomName = new TextField(parcel.getName());
            tfRoomName.setEditable(false);
            TextField tfRoomOccupied = new TextField(!parcel.isOccupied() ? resourceBundle.getString("reservationInfoDialogAvailable") : resourceBundle.getString("reservationInfoDialogOccupied"));
            tfRoomOccupied.setEditable(false);
            TextField tfClientName = new TextField(client.getFirstName() + " " + client.getLastName());
            tfClientName.setEditable(false);
            TextField tfClientNumber = new TextField(client.getPhoneNumber());
            tfClientNumber.setEditable(false);
            TextField tfClientDni = new TextField(client.getDni());
            tfClientDni.setEditable(false);
            TextField tfClientVpn = new TextField(client.getVehiclePlateNumber());
            tfClientVpn.setEditable(false);

            grid.add(new Label(resourceBundle.getString("reservationInfoDialogRoomName")), 0, 0);
            grid.add(tfRoomName, 1, 0);
            grid.add(new Label(resourceBundle.getString("reservationInfoDialogRoomOccupied")), 0, 1);
            grid.add(tfRoomOccupied, 1, 1);
            grid.add(new Label(resourceBundle.getString("reservationInfoDialogClientName")), 0, 2);
            grid.add(tfClientName, 1, 2);
            grid.add(new Label(resourceBundle.getString("reservationInfoDialogClientPhone")), 0, 3);
            grid.add(tfClientNumber, 1, 3);
            grid.add(new Label(resourceBundle.getString("reservationInfoDialogClientDni")), 0, 4);
            grid.add(tfClientDni, 1, 4);
            grid.add(new Label(resourceBundle.getString("reservationInfoDialogClientVpn")), 0, 5);
            grid.add(tfClientVpn, 1, 5);

            Button btnAdd = (Button)dialog.getDialogPane().lookupButton(btntDone);
            btnAdd.disableProperty().bind(
                Bindings.isEmpty(tfRoomName.textProperty())
                .or(Bindings.isEmpty(tfRoomOccupied.textProperty()))
                .or(Bindings.isEmpty(tfClientName.textProperty()))
                .or(Bindings.isEmpty(tfClientNumber.textProperty()))
            );

            root.setCenter(grid);       
            dialog.getDialogPane().setContent(root);
            dialog.showAndWait();
        }
        else{
            //Todo error alert
        }
    }
    
    private boolean reserveRoom(){
        BorderPane root = new BorderPane();          
        
        // Create the custom dialog.
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle(resourceBundle.getString("reserveRoomDialogTitle"));
        dialog.setHeaderText(resourceBundle.getString("reserveRoomDialogHeader") + " " + dataModel.currentParcelProperty().get().getName());
        
        // Set the button types.
        ButtonType btnSelect = new ButtonType(resourceBundle.getString("reserveRoomDialogSelectBtn"), ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnSelect, ButtonType.CANCEL);
       
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        
        TextField tfRoomName = new TextField(dataModel.currentParcelProperty().get().getName());
        tfRoomName.setEditable(false);
        TextField tfRoomOccupied = new TextField(!dataModel.currentParcelProperty().get().isOccupied() ? resourceBundle.getString("reserveRoomDialogAvailable") : resourceBundle.getString("reserveRoomDialogOccupied"));
        tfRoomOccupied.setEditable(false);
        TextField tfClientName = new TextField();
        tfClientName.setEditable(false);
        TextField tfClientNumber = new TextField();
        tfClientNumber.setEditable(false);
        TextField tfClientDni = new TextField();
        tfClientDni.setEditable(false);
        TextField tfClientVpn = new TextField();
        tfClientVpn.setEditable(false);
        
        grid.add(new Label(resourceBundle.getString("reserveRoomDialogRoomName")), 0, 0);
        grid.add(tfRoomName, 1, 0);
        grid.add(new Label(resourceBundle.getString("reserveRoomDialogRoomOccupied")), 0, 1);
        grid.add(tfRoomOccupied, 1, 1);
        grid.add(new Label(resourceBundle.getString("reserveRoomDialogClientName")), 0, 2);
        grid.add(tfClientName, 1, 2);
        grid.add(new Label(resourceBundle.getString("reserveRoomDialogClientPhone")), 0, 3);
        grid.add(tfClientNumber, 1, 3);
        grid.add(new Label(resourceBundle.getString("reserveRoomDialogClientDni")), 0, 4);
        grid.add(tfClientDni, 1, 4);
        grid.add(new Label(resourceBundle.getString("reserveRoomDialogClientVpn")), 0, 5);
        grid.add(tfClientVpn, 1, 5);

        Button btnAdd = (Button)dialog.getDialogPane().lookupButton(btnSelect);
        btnAdd.disableProperty().bind(
            Bindings.isEmpty(tfRoomName.textProperty())
            .or(Bindings.isEmpty(tfRoomOccupied.textProperty()))
            .or(Bindings.isEmpty(tfClientName.textProperty()))
            .or(Bindings.isEmpty(tfClientNumber.textProperty()))
        );

        root.setTop(setupClientListView(MODE.SELECT_CLIENT));
        dataModel.currentClientProperty().addListener((ov, oldClient, newClient) -> {
            if(newClient != null){
                tfClientName.setText(newClient.getFirstName() + " " + newClient.getLastName());
                tfClientNumber.setText(newClient.getPhoneNumber());
                tfClientDni.setText(newClient.getDni());
                tfClientVpn.setText(newClient.getVehiclePlateNumber());
            }
        });
        root.setBottom(grid);
       
        dialog.getDialogPane().setContent(root);

        Platform.runLater(() -> tfRoomName.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnSelect) {
                LocalDateTime checkInTime = LocalDateTime.now();
                LocalDateTime checkoutTime = checkInTime.plusDays(1).withHour(11).withMinute(0).withSecond(0);
                
                return new Reservation(dataModel.currentClientProperty().get().getId(), dataModel.currentParcelProperty().get().getId(), checkInTime, checkoutTime, Reservation.STATUS.ACTIVE);
            }
            return null;
        });

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Optional<Reservation> result = dialog.showAndWait();
        result.ifPresent((t) -> {
            if(dataModel.addNewReservation(t)){
                int lastReservationId = dataModel.getLastTableId("reservation");
                if(lastReservationId > 0){
                    t.setreservationId(lastReservationId);
                    
                    Parcel tempParcel = new Parcel(dataModel.currentParcelProperty().get().getId(), dataModel.currentParcelProperty().get().getName(), !dataModel.currentParcelProperty().get().isOccupied());
                    if(dataModel.updateParcelOccupied(tempParcel))
                    {
                        dataModel.currentParcelProperty().get().setOccupied(!dataModel.currentParcelProperty().get().isOccupied());   
                        atomicBoolean.set(true);
                    }
                    else{
                       //Todo - show alert for update error!
                    } 
                }
            }      
            else{
                //Todo - add error alert!
            }            
        });    
        
        return atomicBoolean.get();
    }
    
    private void cancelOrCheckout(Parcel parcel){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Checkout or Cancel Reservation Dialog");
        alert.setContentText("Select Checkout to check the client out. Select Cancel Reservatioin to cancel the client's reservation. Select Cancel to close this dialog without checking out or canceling the reservation.");

        ButtonType btntCheckout = new ButtonType("Checkout");
        ButtonType btntCancelReservation = new ButtonType("Cancel Reservation");
        ButtonType btntCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btntCheckout, btntCancelReservation, btntCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btntCheckout){
            Parcel tempCurrentParcel = parcel;
            Parcel tempParcel = new Parcel(tempCurrentParcel.getId(), tempCurrentParcel.getName(), false);            
            
            if(dataModel.updateParcelOccupied(tempParcel)){
                tempCurrentParcel.setOccupied(false);
                if(dataModel.updateReservationStatus(tempCurrentParcel, Reservation.STATUS.ACTIVE, Reservation.STATUS.COMPLETE))
                {
                    //Todo - alert checkout is complete
                }
                else{
                    //Todo - alert saying this failed!
                }
            }
            else{
                //Todo - aleart saying this failed!
            }
        } else if (result.get() == btntCancelReservation) {
            Parcel tempCurrentParcel = parcel;
            Parcel tempParcel = new Parcel(tempCurrentParcel.getId(), tempCurrentParcel.getName(), false);
                
            if(dataModel.updateParcelOccupied(tempParcel)){                
                tempCurrentParcel.setOccupied(false);
                if(dataModel.updateReservationStatus(tempCurrentParcel, Reservation.STATUS.ACTIVE, Reservation.STATUS.CANCELED))
                {
                    //Todo - alert checkout is complete
                }
                else{
                    //Todo - alert saying this failed!
                }
            }
            else{
                //Todo - aleart saying this failed!
            }
        } else if (result.get() == btntCancel) {
            // ... user chose "Three"
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
    
    private void deleteRoom(Parcel parcel){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("confirmtionDialogTitle"));
        alert.setHeaderText(resourceBundle.getString("deleleRoomConfirmationHeader"));
        alert.setContentText(resourceBundle.getString("deleleRoomConfirmationContentText") + " " + parcel.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if(dataModel.deleteParcel(parcel)){
                parcelList.remove(parcel);
            }
            else{
                //Todo - show did not delete dialog!
            }
        }         
    }
    
    private void editClient(Client client){
         // Create the custom dialog.
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle(resourceBundle.getString("editClientDialogTitle"));
        dialog.setHeaderText(resourceBundle.getString("editClientDialogHeader"));

        // Set the button types.
        ButtonType btntAdd = new ButtonType(resourceBundle.getString("editClientDialogAddBtn"), ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btntAdd, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField tfFirstName = new TextField(client.getFirstName());
        TextField tfLastName = new TextField(client.getLastName());
        TextField tfPhoneNumber = new TextField(client.getPhoneNumber());
        TextField tfDni = new TextField(client.getDni());
        TextField tfVehiclePlaetNumber = new TextField(client.getVehiclePlateNumber());

        grid.add(new Label(resourceBundle.getString("editClientDialogFirstNameLabel")), 0, 0);
        grid.add(tfFirstName, 1, 0);
        grid.add(new Label(resourceBundle.getString("editClientDialogLastNameLabel")), 0, 1);
        grid.add(tfLastName, 1, 1);
        grid.add(new Label(resourceBundle.getString("editClientDialogPhoneNumberLabel")), 0, 2);
        grid.add(tfPhoneNumber, 1, 2);
        grid.add(new Label(resourceBundle.getString("editClientDialogDniLabel")), 0, 3);
        grid.add(tfDni, 1, 3);
        grid.add(new Label(resourceBundle.getString("editClientDialogVpnLabel")), 0, 4);
        grid.add(tfVehiclePlaetNumber, 1, 4);

        // Enable/Disable login button depending on whether a username was entered.
        Button btnAdd = (Button)dialog.getDialogPane().lookupButton(btntAdd);
        btnAdd.disableProperty().bind(
            Bindings.isEmpty(tfFirstName.textProperty())
            .or(Bindings.isEmpty(tfLastName.textProperty()))
            .or(Bindings.isEmpty(tfPhoneNumber.textProperty()))
            .or(Bindings.isEmpty(tfDni.textProperty()))
            .or(Bindings.isEmpty(tfVehiclePlaetNumber.textProperty()))
        );
        
        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> tfFirstName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btntAdd) {
                return new Client(client.getId(), tfFirstName.getText(), tfLastName.getText(), tfPhoneNumber.getText(), tfDni.getText(), tfVehiclePlaetNumber.getText());
            }
            return null;
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent((t) -> {
            if(dataModel.updateClient(t)){                   
                client.setFirstName(t.getFirstName());
                client.setLastName(t.getLastName());
                client.setPhoneNumber(t.getPhoneNumber());
                client.setDni(t.getDni());
                client.setVehiclePlateNumber(t.getVehiclePlateNumber());
                System.out.println("Updated Client: " + client.toString());
            }
            else{
                //Todo - add error alert!
            }
        });       
    }
    
    private void deleteClient(Client client){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resourceBundle.getString("confirmtionDialogTitle"));
        alert.setHeaderText(resourceBundle.getString("deleteClientConfirmationHeader"));
        alert.setContentText(resourceBundle.getString("deleteClientContentText") + " " + client.getFirstName() + " " + client.getLastName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if(dataModel.deleteClient(client)){
                clientList.remove(client);
            }
            else{
                //Todo - show did not delete dialog!
            }
        }        
    }
    
    /*
        This also handles adding a new Client!!!
        That's teh reason you don't see the method addClient. 
        This method add new Clients and makes use of the methods editClient and deleteClient via the setupClientListView!!!
    */
    @FXML
    void handleBtnClients(ActionEvent actionEvent){
        // Create the custom dialog.
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle(resourceBundle.getString("clientDialogTitle"));
        dialog.setHeaderText(resourceBundle.getString("clientDialogHeader"));

        // Set the button types.
        ButtonType btntAdd = new ButtonType(resourceBundle.getString("clientDialogAddBtn"), ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btntAdd, ButtonType.CLOSE);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        
        TextField tfFirstName = new TextField();
        TextField tfLastName = new TextField();
        TextField tfPhoneNumber = new TextField();
        TextField tfDni = new TextField();
        TextField tfVehiclePlaetNumber = new TextField();

        grid.add(new Label(resourceBundle.getString("clientDialogFirstNameLabel")), 0, 0);
        grid.add(tfFirstName, 1, 0);
        grid.add(new Label(resourceBundle.getString("clientDialogLastNameLabel")), 0, 1);
        grid.add(tfLastName, 1, 1);
        grid.add(new Label(resourceBundle.getString("clientDialogPhoneNumberLabel")), 0, 2);
        grid.add(tfPhoneNumber, 1, 2);
        grid.add(new Label(resourceBundle.getString("clientDialogDniLabel")), 0, 3);
        grid.add(tfDni, 1, 3);
        grid.add(new Label(resourceBundle.getString("clientDialogVpnLabel")), 0, 4);
        grid.add(tfVehiclePlaetNumber, 1, 4);

        // Enable/Disable login button depending on whether a username was entered.
        Button btnAdd = (Button)dialog.getDialogPane().lookupButton(btntAdd);
        btnAdd.disableProperty().bind(
            Bindings.isEmpty(tfFirstName.textProperty())
            .or(Bindings.isEmpty(tfLastName.textProperty()))
            .or(Bindings.isEmpty(tfPhoneNumber.textProperty()))
            .or(Bindings.isEmpty(tfDni.textProperty()))
            .or(Bindings.isEmpty(tfVehiclePlaetNumber.textProperty()))
        );

        BorderPane root = new BorderPane();
        root.setTop(setupClientListView(MODE.EDIT_CLIENT));
        
        Label lblAddNewClientText = new Label(resourceBundle.getString("clientDialogAddNewClientLabel"));
        lblAddNewClientText.setPadding(new Insets(10, 0, 0, 0));
        lblAddNewClientText.setFont(Font.font("System", FontWeight.NORMAL, 16));
        root.setCenter(lblAddNewClientText);
        root.setBottom(grid);
        dialog.getDialogPane().setContent(root);

        // Request focus on the username field by default.
        Platform.runLater(() -> tfFirstName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btntAdd) {
                return new Client(tfFirstName.getText(), tfLastName.getText(), tfPhoneNumber.getText(), tfDni.getText(), tfVehiclePlaetNumber.getText());
            }
            return null;
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent((t) -> {
            if(dataModel.addNewClient(t)){
                int newClientId = dataModel.getLastTableId("client");
                if(newClientId > 0){
                    t.setId(newClientId);
                    clientList.add(t);
                }
                else{
                    //Todo - add error alert!
                }
            }
        });       
    }
    
    @FXML
    void handleBtnTwo(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.WARNING, "This feature has not been setup!");
                         alert.showAndWait();
    }
    
    @FXML
    void handleBtnThree(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.WARNING, "This feature has not been setup!");
                         alert.showAndWait();
    }
    
    @FXML
    void handleBtnFour(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.WARNING, "This feature has not been setup!");
                         alert.showAndWait();
    }
    
    @FXML
    void handleBtnAddNewRoom(ActionEvent actionEvent){
        Dialog<Parcel> dialog = new Dialog<>();
        dialog.setTitle(resourceBundle.getString("addNewRoomDialogTitle"));
        dialog.setHeaderText(resourceBundle.getString("addNewRoomDialogHeaderText"));
        ButtonType btntAdd = new ButtonType(resourceBundle.getString("addNewRoomDialogAddBtn"), ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btntAdd, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField tfRoomName = new TextField();

        grid.add(new Label(resourceBundle.getString("addNewRoomDialongLabelText")), 0, 0);
        grid.add(tfRoomName, 1, 0);

        // Enable/Disable login button depending on whether a username was entered.
        Button btnAdd = (Button)dialog.getDialogPane().lookupButton(btntAdd);
        btnAdd.disableProperty().bind(
            Bindings.isEmpty(tfRoomName.textProperty())
        );

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> tfRoomName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btntAdd) {
                return new Parcel(tfRoomName.getText());
            }
            return null;
        });

        Optional<Parcel> result = dialog.showAndWait();
        result.ifPresent((t) -> {
            if(dataModel.addNewParcel(t)){
                int newParcelId = dataModel.getLastTableId("parcel");
                if(newParcelId > 0){
                    t.setId(newParcelId);
                    parcelList.add(t);
                }
                else{
                    //Todo - add error alert!
                }
            }
        });    
    }
    
    @FXML
    void handleIconExit(MouseEvent mouseEvent){
        Platform.exit();
    }
    
    
    
    
}
