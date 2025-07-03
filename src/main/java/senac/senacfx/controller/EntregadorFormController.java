package senac.senacfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Constraints;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Entregador;
import senac.senacfx.model.exceptions.ValidationException;
import senac.senacfx.model.services.EntregadorService;

import java.net.URL;
import java.util.*;

public class EntregadorFormController implements Initializable {

    private Entregador entity;

    private EntregadorService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtTelefone;

    @FXML
    private Label labelErrorNome;

    @FXML
    private Label labelErrorTelefone;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setEntregador(Entregador entity) {
        this.entity = entity;
    }

    public void setEntregadorService(EntregadorService service) {
        this.service = service;
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
            Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Entregador getFormData() {
        Entregador obj = new Entregador();
        ValidationException exception = new ValidationException("Erro na validacao");

        obj.setCodigo(Utils.tryParseToLong(txtCodigo.getText()));

        if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
            exception.addError("nome", "campo nao pode ser vazio");
        }
        obj.setNome(txtNome.getText());

        if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
            exception.addError("telefone", "campo nao pode ser vazio");
        }
        obj.setTelefone(txtTelefone.getText());

        if (!exception.getErrors().isEmpty()) {
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldLong(txtCodigo);
        Constraints.setTextFieldMaxLength(txtNome, 200);
        Constraints.setTextFieldMaxLength(txtTelefone, 20);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entidade nula");
        }

        txtCodigo.setText(String.valueOf(entity.getCodigo()));
        txtNome.setText(entity.getNome());
        txtTelefone.setText(entity.getTelefone());
    }

    private void setErrorMessages(Map<String, String> errors) {
        labelErrorNome.setText(errors.getOrDefault("nome", ""));
        labelErrorTelefone.setText(errors.getOrDefault("telefone", ""));
    }
}
