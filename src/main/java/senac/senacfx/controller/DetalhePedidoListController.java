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
import senac.senacfx.model.entities.DetalhePedido;
import senac.senacfx.model.services.DetalhePedidoService;
import senac.senacfx.model.services.PedidoService;
import senac.senacfx.model.services.ProdutoService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DetalhePedidoListController implements Initializable, DataChangeListener {

    private DetalhePedidoService service;

    @FXML
    private TableView<DetalhePedido> tableViewDetalhe;

    @FXML
    private TableColumn<DetalhePedido, Long> tableColumnCodigo;

    @FXML
    private TableColumn<DetalhePedido, Long> tableColumnPedido;

    @FXML
    private TableColumn<DetalhePedido, Long> tableColumnProduto;

    @FXML
    private TableColumn<DetalhePedido, Integer> tableColumnQuantidade;

    @FXML
    private TableColumn<DetalhePedido, Double> tableColumnPrecoUnitario;

    @FXML
    private TableColumn<DetalhePedido, DetalhePedido> tableColumnEDIT;

    @FXML
    private TableColumn<DetalhePedido, DetalhePedido> tableColumnREMOVE;

    @FXML
    private Button btNew;

    private ObservableList<DetalhePedido> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        DetalhePedido obj = new DetalhePedido();
        createDialogForm(obj, "/gui/DetalhePedidoForm.fxml", parentStage);
    }

    public void setDetalhePedidoService(DetalhePedidoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tableColumnPedido.setCellValueFactory(new PropertyValueFactory<>("codigoPedido"));
        tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("codigoProduto"));
        tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnPrecoUnitario.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDetalhe.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service is null!");
        }
        List<DetalhePedido> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDetalhe.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(DetalhePedido obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            DetalhePedidoFormController controller = loader.getController();
            controller.setDetalhePedido(obj);
            controller.setDetalhePedidoService(new DetalhePedidoService());
            controller.setPedidoService(new PedidoService());
            controller.setProdutoService(new ProdutoService());
            controller.subscribeDataChangeListener(this);
            controller.loadAssociatedObjects();
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Detalhe do Pedido");
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
        tableColumnEDIT.setCellFactory(param -> new TableCell<DetalhePedido, DetalhePedido>() {
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(DetalhePedido obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createDialogForm(obj, "/gui/DetalhePedidoForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<DetalhePedido, DetalhePedido>() {
            private final Button button = new Button("Remover");

            @Override
            protected void updateItem(DetalhePedido obj, boolean empty) {
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

    private void removeEntity(DetalhePedido obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Deseja realmente excluir este detalhe?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service está null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbException e) {
                Alerts.showAlert("Erro ao remover detalhe", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
