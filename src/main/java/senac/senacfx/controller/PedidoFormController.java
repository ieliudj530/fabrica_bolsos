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
import senac.senacfx.model.entities.Cliente;
import senac.senacfx.model.entities.Entregador;
import senac.senacfx.model.entities.Pedido;
import senac.senacfx.model.exceptions.ValidationException;
import senac.senacfx.model.services.ClienteService;
import senac.senacfx.model.services.EntregadorService;
import senac.senacfx.model.services.PedidoService;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class PedidoFormController implements Initializable {

    private Pedido entity;

    private PedidoService service;
    private ClienteService clienteService;
    private EntregadorService entregadorService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtCodigo;

    @FXML
    private ComboBox<Cliente> comboBoxCliente;

    @FXML
    private ComboBox<Entregador> comboBoxEntregador;

    @FXML
    private DatePicker dpDataPedido;

    @FXML
    private Label labelErrorCliente;

    @FXML
    private Label labelErrorEntregador;

    @FXML
    private Label labelErrorData;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Cliente> obsListClientes;
    private ObservableList<Entregador> obsListEntregadores;

    public void setPedido(Pedido entity) {
        this.entity = entity;
    }

    public void setPedidoService(PedidoService service) {
        this.service = service;
    }

    public void setClienteService(ClienteService service) {
        this.clienteService = service;
    }

    public void setEntregadorService(EntregadorService service) {
        this.entregadorService = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entidade nula");
        }
        if (service == null) {
            throw new IllegalStateException("Servico nulo");
        }

        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Erro ao salvar pedido", null, e.getMessage(), Alert.AlertType.ERROR);
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

    private Pedido getFormData() {
        Pedido obj = new Pedido();
        ValidationException exception = new ValidationException("Erro na validacao");

        obj.setCodigo(Utils.tryParseToLong(txtCodigo.getText()));

        Cliente cliente = comboBoxCliente.getValue();
        if (cliente == null) {
            exception.addError("cliente", "Cliente obrigatorio");
        } else {
            obj.setCodigoCliente(cliente.getCodigo());
        }

        Entregador entregador = comboBoxEntregador.getValue();
        if (entregador == null) {
            exception.addError("entregador", "Entregador obrigatorio");
        } else {
            obj.setCodigoEntregador(entregador.getCodigo());
        }

        if (dpDataPedido.getValue() == null) {
            exception.addError("data", "Data obrigatoria");
        } else {
            obj.setDataPedido(dpDataPedido.getValue().atStartOfDay());
        }

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
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entidade nula");
        }

        txtCodigo.setText(String.valueOf(entity.getCodigo()));
        if (entity.getDataPedido() != null) {
            dpDataPedido.setValue(entity.getDataPedido().toLocalDate());
        }
    }

    public void loadAssociatedObjects() {
        if (clienteService == null || entregadorService == null) {
            throw new IllegalStateException("Servicos associados nulos");
        }
        List<Cliente> listClientes = clienteService.findAll();
        obsListClientes = FXCollections.observableArrayList(listClientes);
        comboBoxCliente.setItems(obsListClientes);

        List<Entregador> listEntregadores = entregadorService.findAll();
        obsListEntregadores = FXCollections.observableArrayList(listEntregadores);
        comboBoxEntregador.setItems(obsListEntregadores);
    }

    private void setErrorMessages(Map<String, String> errors) {
        labelErrorCliente.setText(errors.getOrDefault("cliente", ""));
        labelErrorEntregador.setText(errors.getOrDefault("entregador", ""));
        labelErrorData.setText(errors.getOrDefault("data", ""));
    }
}
