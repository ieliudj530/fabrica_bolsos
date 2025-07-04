package senac.senacfx.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import senac.senacfx.application.Main;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Pedido;
import senac.senacfx.model.services.PedidoService;
import senac.senacfx.model.services.ClienteService;
import senac.senacfx.model.services.EntregadorService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PedidoListController implements Initializable, DataChangeListener {

    private PedidoService service;

    @FXML
    private TableView<Pedido> tableViewPedido;

    @FXML
    private TableColumn<Pedido, Long> tableColumnCodigo;

    @FXML
    private TableColumn<Pedido, Long> tableColumnCliente;

    @FXML
    private TableColumn<Pedido, Long> tableColumnEntregador;

    @FXML
    private TableColumn<Pedido, String> tableColumnData;

    @FXML
    private TableColumn<Pedido, Pedido> tableColumnEDIT;

    @FXML
    private TableColumn<Pedido, Pedido> tableColumnREMOVE;

    @FXML
    private Button btNew;

    private ObservableList<Pedido> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Pedido obj = new Pedido();
        createDialogForm(obj, "/gui/PedidoForm.fxml", parentStage);
    }

    public void setPedidoService(PedidoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        tableColumnEntregador.setCellValueFactory(new PropertyValueFactory<>("codigoEntregador"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<>("dataPedidoString")); // Asegúrate de tener este método en Pedido

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewPedido.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service is null!");
        }
        List<Pedido> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewPedido.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Pedido obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            PedidoFormController controller = loader.getController();
            controller.setPedido(obj);
            controller.setPedidoService(new PedidoService());
            controller.setClienteService(new ClienteService());
            controller.setEntregadorService(new EntregadorService());
            controller.subscribeDataChangeListener(this);
            controller.loadAssociatedObjects();
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastro de Pedido");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Erro ao carregar a tela", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Pedido, Pedido>() {
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(Pedido obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createDialogForm(obj, "/gui/PedidoForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Pedido, Pedido>() {
            private final Button button = new Button("Remover");

            @Override
            protected void updateItem(Pedido obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Pedido obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Deseja realmente excluir este pedido?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service está null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbException e) {
                Alerts.showAlert("Erro ao remover pedido", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
