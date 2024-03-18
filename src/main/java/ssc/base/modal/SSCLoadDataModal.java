/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import ssc.base.task.BaseTask;

/**
 *
 * @author PC
 */
public abstract class SSCLoadDataModal extends SSCBaseModal {

    public abstract void initTable(TableView tv);

    public abstract void loadList(ObservableList data);
    private ObservableList data;
    private TableView tvData ;
    public SSCLoadDataModal(int widthRate, int height, String title, ObservableList data) {
        super(widthRate, height, title, "");
        this.tvData = new TableView();
        this.data = data;
        tvData.getStyleClass().addAll("table-view-material");
        tvData.setPrefSize(content.getPrefWidth(), content.getPrefHeight());
        content.getChildren().add(tvData);
        tvData.setItems(data);
    }

    public void loadDataTask() {

        BaseTask task = new BaseTask() {
            @Override
            public boolean mainFunction() {
                try {
                    loadList(data);
                } catch (Exception ex) {

                }
                return true;
            }
        ;
        };
        task.start();
        /*
        LoadModelTask lmt = new LoadModelTask("") {
            @Override
            public ObservableList<BaseModel> getData(String query) {
                 loadList(data);
            }
        };
        lmt.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                data.setAll(lmt.getValue());
            }
        });
        lmt.start();
         */
    }

    @Override
    public void show() {
        initTable(tvData);
        showModal();
        loadDataTask();
    }
}
