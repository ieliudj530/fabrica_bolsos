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
import senac.senacfx.model.entities.Produto;
import senac.senacfx.model.exceptions.ValidationException;
import senac.senacfx.model.services.ProdutoService;

import java.net.URL;
import java.util.*;

public class ProdutoFormController implements Initializable {

    private Produto entity;

    private ProdutoService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtDescricao;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtStock;

    @FXML
    private Label labelErrorNome;

    @FXML
    private Label labelErrorDescricao;

    @FXML
    private Label labelErrorPreco;

    @FXML
    private Label labelErrorStock;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setProduto(Produto entity) {
        this.entity = entity;
    }

    public void setProdutoService(ProdutoService service) {
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

    private Produto getFormData() {
        Produto obj = new Produto();
        ValidationException exception = new ValidationException("Erro na validacao");

        obj.setCodigo(Utils.tryParseToLong(txtCodigo.getText()));

        if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
            exception.addError("nome", "campo nao pode ser vazio");
        }
        obj.setNome(txtNome.getText());

        if (txtDescricao.getText() == null || txtDescricao.getText().trim().equals("")) {
            exception.addError("descricao", "campo nao pode ser vazio");
        }
        obj.setDescricao(txtDescricao.getText());

        obj.setPreco(Utils.tryParseToDouble(txtPreco.getText()));
        obj.setStock(Utils.tryParseToInt(txtStock.getText()));

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
        Constraints.setTextFieldMaxLength(txtDescricao, 300);
        Constraints.setTextFieldDouble(txtPreco);
        Constraints.setTextFieldInteger(txtStock);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entidade nula");
        }

        txtCodigo.setText(String.valueOf(entity.getCodigo()));
        txtNome.setText(entity.getNome());
        txtDescricao.setText(entity.getDescricao());
        txtPreco.setText(String.format("%.2f", entity.getPreco()));
        txtStock.setText(String.valueOf(entity.getStock()));
    }

    private void setErrorMessages(Map<String, String> errors) {
        labelErrorNome.setText(errors.getOrDefault("nome", ""));
        labelErrorDescricao.setText(errors.getOrDefault("descricao", ""));
        labelErrorPreco.setText(errors.getOrDefault("preco", ""));
        labelErrorStock.setText(errors.getOrDefault("stock", ""));
    }
}
