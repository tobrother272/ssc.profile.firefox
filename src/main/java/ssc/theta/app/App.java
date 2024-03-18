package ssc.theta.app;


import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ssc.base.global.TC;
import ssc.base.global.ViewGlobal;
import ssc.base.task.CheckAccessTokenTask;
import ssc.base.ultil.MyFileUtils;

/**
 *
 * @author PC
 */
public class App extends Application {

    private static Scene scene;

    public static Scene getScene() {
        return scene;
    }

    /**
     * @param args the command line arguments
     */
    static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //AdbCmdUtils.runAdbCommandAsBat("ce0616064c6d492f04", Arrays.asList("exec-out", "screencap", "-p", ">", "D:\\screen.jpg"));
        
        MyFileUtils.printLogToDesktop();

        scene = new Scene(loadFXML("login"), 400, 420);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        
        ViewGlobal.getInst().bindScreen(scene);
        ViewGlobal.getInst().stage = stage;
        stage.show();

        TC.getInts().setAccessToken("");
        TC.getInts().accessTokenProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (t1.length() == 0) {
                    TC.getInts().role = "";
                    try {
                        setRoot("login");
                        ViewGlobal.getInst().stage.setWidth(400);
                        ViewGlobal.getInst().stage.setHeight(420);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CheckAccessTokenTask checkAccessTokenTask = new CheckAccessTokenTask();
                    checkAccessTokenTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {
                            if (TC.getInts().role.length() != 0) {
                                try {
                                    ViewGlobal.getInst().stage.setWidth(1366);
                                    ViewGlobal.getInst().stage.setHeight(768);
                                    setRoot("main");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    checkAccessTokenTask.start();
                }
            }
        });

        //TC.getInts().accessTokenProperty().set("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsZWJpbmhAZ21haWwuY29tIiwiZXhwIjoxNjQ5NDcxMzcwLCJpYXQiOjE2NDg5NzA3MTZ9.nW0TlgQ3UQvs21pE3AvmsilNRZh4XCqyZ-uD9Z7EEYGhwuwCFcUsjPDkz6aKgQLL4XcFg6Dwcnr_IxAiEkg9fA");
    }

    static void setRoot(String fxml) {
        try {
            scene.setRoot(loadFXML(fxml));
            ViewGlobal.getInst().bindScreen(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Parent loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            return fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
