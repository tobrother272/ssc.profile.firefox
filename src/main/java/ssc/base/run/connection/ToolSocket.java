/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run.connection;

//import FormComponent.View.SSCMessage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.Scene;
import ssc.base.task.BaseTask;
import ssc.base.ultil.CMDUtils;
import ssc.base.view.ProcessTaskView;

/**
 *
 * @author PC
 */
public class ToolSocket {

    private ProcessTaskView ptv;

    public ProcessTaskView getPtv() {
        return ptv;
    }

    public void setPtv(ProcessTaskView ptv) {
        this.ptv = ptv;
    }

    private ServerSocket serverSocket;
    private List<TaskClientConnection> arrClients;
    private Scene scene;
    private boolean isConnect = false;
    public boolean showLog = true;

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public List<String> startServer(List<String> cmds) {
        long startTime = System.currentTimeMillis();
        List<String> outPut = new ArrayList<>();

        Process process;
        try {
            ProcessBuilder pb = new ProcessBuilder(cmds);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                outPut.add(line);
                if (showLog) {
                    System.out.println(line);
                }

                if (line.contains("No plugins")) {
                    isConnect = true;
                    ptv.startTask();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }

        System.out.println((System.currentTimeMillis()) - startTime);

        return outPut;
    }

    public void start() {
        if (serverSocket != null) {
            return;
        }
        try {
            serverSocket = new ServerSocket(8001);

            BaseTask task = new BaseTask() {
                @Override
                public boolean mainFunction() {
                    try {
                        CMDUtils.checkAndKillProcess("node.exe");
                        startServer(Arrays.asList("cmd", "/c", "\"", "appium", "-a", "127.0.0.1", "-p", " 4723", "\""));

                    } catch (Exception e) {
                    }
                    return true;
                }
            };
            task.start();

            //SSCMessage.showSuccess(scene,"Đã tạo được server socket");
            new ServerConnect().start();
        } catch (Exception e) {
            //SSCMessage.showError(scene,"Không tạo được socket");
        }
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public List<TaskClientConnection> getArrClients() {
        return arrClients;
    }

    public void setArrClients(List<TaskClientConnection> arrClients) {
        this.arrClients = arrClients;
    }

    public void showNotifi(String name) {
        //SSCMessage.showSuccessInThread(scene, "Đã connect trình duyệt " + name);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ToolSocket() {
        arrClients = new ArrayList<>();
    }
    public static ToolSocket instance;

    public static ToolSocket getInstance() {
        if (instance == null) {
            instance = new ToolSocket();
        }
        return instance;
    }

}
