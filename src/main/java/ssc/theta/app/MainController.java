package ssc.theta.app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import ssc.base.view.Navigator;
import ssc.base.view.WindowBarView;
import ssc.base.global.ViewGlobal;
import ssc.theta.app.screens.ProfileScreen;
import ssc.base.task.BaseTask;
import ssc.base.task.InitViewTask;
import ssc.base.ultil.ThreadUtils;

public class MainController implements Initializable {

    @FXML
    TabPane contentContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        InitViewTask task = new InitViewTask();
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                BaseTask dbt = new BaseTask() {
                    @Override
                    public boolean mainFunction() {
                        ThreadUtils.Sleep(1000);
                        return true;
                    }
                };
                dbt.start();
                new WindowBarView();
                ViewGlobal.getInst().initToolTipView();

                dbt.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ViewGlobal.getInst().getLbToolTipMessage().textProperty().bind(dbt.messageProperty());
                        ViewGlobal.getInst().getTooltipContainer().visibleProperty().bind(dbt.runningProperty());
                    }
                });

                Navigator navigator = new Navigator();
                navigator.addScreen(new ProfileScreen("Tạo Tài Khoản", 0, "GROUP", navigator));
                navigator.setTabIndex(0);
            }
        });
        task.start();

    }

}
