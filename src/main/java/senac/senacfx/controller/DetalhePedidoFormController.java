package senac.senacfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Constraints;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.DetalhePedido;
import senac.senacfx.model.entities.Pedido;
import senac.senacfx.model.entities.Produto;
import senac.senacfx.model.exceptions.ValidationException;
import senac.senacfx.model.services.DetalhePedidoService;
import senac.senacfx.model.services.PedidoService;
import senac.senacfx.model.services.ProdutoService;

import java.net.URL;
import java.util.*;

public class DetalhePedidoFormController implements Initializable {

    private DetalhePedido entity;

    private DetalhePedidoService service;
    private PedidoService pedidoService;
    private ProdutoService produtoService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtCodigo;

    @FXML
    private ComboBox<Pedido> comboBoxPedido;

    @FXML
    private ComboBox<Produto> comboBoxProduto;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private TextField txtPrecoUnitario;

    @FXML
    private Label labelErrorPedido;

    @FXML
    private Label labelErrorProduto;

    @FXML
    private Label labelErrorQuantidade;

    @FXML
    private Label labelErrorPreco;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Pedido> obsListPedidos;
    private ObservableList<Produto> obsListProdutos;

    public void setDetalhePedido(DetalhePedido entity) {
        this.entity = entity;
    }

    public void setDetalhePedidoService(DetalhePedidoService service) {
        this.service = service;
    }

    public void setPedidoService(PedidoService service) {
        this.pedidoService = service;
    }

    public void setProdutoService(ProdutoService service) {
        this.produtoService = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null || service == null) {
            throw new IllegalStateException("Entidade ou serviço nulo");
        }

        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Erro ao salvar detalhe do pedido", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private DetalhePedido getFormData() {
        DetalhePedido obj = new DetalhePedido();
        ValidationException exception = new ValidationException("Erro na validacao");

        obj.setCodigo(Utils.tryParseToLong(txtCodigo.getText()));

        Pedido pedido = comboBoxPedido.getValue();
        if (pedido == null) {
            exception.addError("pedido", "Pedido obrigatorio");
        } else {
            obj.setCodigoPedido(pedido.getCodigo());
        }

        Produto produto = comboBoxProduto.getValue();
        if (produto == null) {
            exception.addError("produto", "Produto obrigatorio");
        } else {
            obj.setCodigoProduto(produto.getCodigo());
        }

        obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
        obj.setPrecoUnitario(Utils.tryParseToDouble(txtPrecoUnitario.getText()));

        if (!exception.getErrors().isEmpty()) {
            throw exception;
        }

        return obj;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldLong(txtCodigo);
        Constraints.setTextFieldInteger(txtQuantidade);
        Constraints.setTextFieldDouble(txtPrecoUnitario);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entidade nula");
        }

        txtCodigo.setText(String.valueOf(entity.getCodigo()));
        txtQuantidade.setText(String.valueOf(entity.getQuantidade()));
        txtPrecoUnitario.setText(String.format("%.2f", entity.getPrecoUnitario()));
    }

    public void loadAssociatedObjects() {
        if (pedidoService == null || produtoService == null) {
            throw new IllegalStateException("Servicos associados nulos");
        }
        obsListPedidos = FXCollections.observableArrayList(pedidoService.findAll());
        comboBoxPedido.setItems(obsListPedidos);

        obsListProdutos = FXCollections.observableArrayList(produtoService.findAll());
        comboBoxProduto.setItems(obsListProdutos);
    }

    private void setErrorMessages(Map<String, String> errors) {
        labelErrorPedido.setText(errors.getOrDefault("pedido", ""));
        labelErrorProduto.setText(errors.getOrDefault("produto", ""));
        labelErrorQuantidade.setText(errors.getOrDefault("quantidade", ""));
        labelErrorPreco.setText(errors.getOrDefault("preco", ""));
    }
}
