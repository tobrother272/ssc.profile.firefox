/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.googleaction;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import ssc.base.run.ActionWithAccountBase;
import ssc.base.run.GoogleInteractiveChildTask;
import ssc.theta.app.model.TaskLog;

/**
 * @author simpl
 */
public class StartServerTask extends GoogleInteractiveChildTask {

    public StartServerTask(ActionWithAccountBase task) {
        super(task);
    }

    @Override
    protected Boolean call() {

        Process process;
        try {

            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("cmd", "/c", "\"", "appium", "-a", "127.0.0.1", "-p", getParentTask().getPort() + "", "\""));

            getParentTask().insertSuccessLog("Đã tạo appium ở port " + getParentTask().getPort());

            pb.redirectErrorStream(true);
            process = pb.start();

            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                //System.out.println(line);

                getParentTask().getArrRemoteLog().add(new TaskLog(System.currentTimeMillis(), line, line, "", 1));

                if (getParentTask().getArrRemoteLog().size() > 200) {
                    getParentTask().getArrRemoteLog().clear();
                }
                if (line.contains("No plugins") || line.contains("address already in use")) {
                    getParentTask().setServerReady(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }

        /*
        try {
            AppiumServiceBuilder builder = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .usingPort(Integer.parseInt(getParentTask().getPort()))
                    //.withArgument(ServerArgument.LOG_LEVEL, "info") // Set log level
                    .withLogFile(new File("appium_" + getParentTask().getPort() + ".log")); // Specify log file

            // Start the Appium server
            AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
            service.start();
            getParentTask().setServerReady(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
        return false;

    }

    @Override
    public void sendResultToParent() {

    }

    @Override
    public String setActionName() {
        return " giây | Đang start server";

    }

}
