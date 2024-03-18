/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ssc.base.proxy.tmproxy.TMKeyAndProxy;
import ssc.theta.app.googleaction.StartServerTask;
import ssc.theta.app.model.GoogleAccount;
import ssc.theta.app.model.TaskLog;

/**
 * @author PC
 */
public abstract class ActionWithAccountBase extends ActionBase {
    
    private GoogleAccount account;
    private int driverPort;
    private SimpleStringProperty errorImage;
    private ObservableList<TaskLog> arrRemoteLog;
    private boolean timeout = false;
    
    private SimpleStringProperty deviceId;
    private SimpleStringProperty proxy;
    
    public SimpleStringProperty deviceIdProperty() {
        return deviceId;
    }

    public String getDeviceId() {
        return deviceId.get();
    }

    public void setDeviceId(String deviceId) {
        this.deviceId.set(deviceId);
    }
    
    public SimpleStringProperty proxyProperty() {
        return proxy;
    }

    public String getProxy() {
        return proxy.get();
    }

    public void setProxy(String proxy) {
        this.proxy.set(proxy);
    }
    
    private SimpleStringProperty port;
    
    public SimpleStringProperty portProperty() {
        return port;
    }
    
    public String getPort() {
        return port.get();
    }
    
    public void setPort(String port) {
        this.port.set(port);
    }

    private TMKeyAndProxy tMKeyAndProxy;
    
    public TMKeyAndProxy gettMKeyAndProxy() {
        return tMKeyAndProxy;
    }
    
    public void settMKeyAndProxy(TMKeyAndProxy tMKeyAndProxy) {
        this.tMKeyAndProxy = tMKeyAndProxy;
    }
    
    private boolean serverReady;
    
    public boolean isServerReady() {
        return serverReady;
    }
    
    public void setServerReady(boolean serverReady) {
        this.serverReady = serverReady;
    }
    
    Thread appium_server;
    StartServerTask st;
    
    public void stopServer() {
        //TC.getInts().arrPort.add(Integer.parseInt(getPort()));
        //appium_server.stop();
        //appium_server.suspend();
        //st.cancel();
        //killByPort(getPort());
    }
    
    @Override
    public boolean initAutomationAction() {

            st = new StartServerTask(this);
            appium_server = new Thread(st);
            appium_server.setDaemon(true);
            appium_server.start();
            while (!serverReady) {
                updateMyMessage("Chờ kết nối server");
            }
            
            waitChildTask(240, new OpenAndConnectDeviceTask(this));
            


        
        return true;
    }
    
    public boolean isTimeout() {
        return timeout;
    }
    
    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
    private int browserX = 0;
    private int browserY = 0;
    
    public int getBrowserX() {
        return browserX;
    }
    
    public void setBrowserX(int browserX) {
        this.browserX = browserX;
    }
    
    public int getBrowserY() {
        return browserY;
    }
    
    public void setBrowserY(int browserY) {
        this.browserY = browserY;
    }
    
    public ObservableList<TaskLog> getArrRemoteLog() {
        return arrRemoteLog;
    }
    
    public void setArrRemoteLog(ObservableList<TaskLog> arrRemoteLog) {
        this.arrRemoteLog = arrRemoteLog;
    }
    
    public void insertRemoteLog(TaskLog log) {
        arrRemoteLog.add(log);
    }
    
    public SimpleStringProperty errorImageProperty() {
        if (errorImage == null) {
            errorImage = new SimpleStringProperty("");
        }
        return errorImage;
    }
    
    public String getErrorImage() {
        return errorImage.get();
    }
    
    public void setErrorImage(String _errorImage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                errorImage.set(_errorImage);
            }
        });
        
    }
    
    public int getDockerPort() {
        return driverPort;
    }
    
    public void setDockerPort(int driverPort) {
        this.driverPort = driverPort;
    }
    
    public GoogleAccount getAccount() {
        return account;
    }
    
    public void setAccount(GoogleAccount account) {
        this.account = account;
    }
    
    public ActionWithAccountBase(int stt, GoogleAccount account) {
        super(stt);
        this.arrRemoteLog = FXCollections.observableArrayList();
        this.proxy = new SimpleStringProperty(account.getProxy());
        this.deviceId = new SimpleStringProperty(account.getDevice_id());
        this.account = account;
        setTaskOwner(account.getConnectionName());
        errorImage = new SimpleStringProperty("");
        port = new SimpleStringProperty("...");
    }
    
    @Override
    public void afterFail() {
        
    }
    
    @Override
    public void afterSuccess() {
        
    }
    
    @Override
    public String initFunction() {
        
        return "";
    }
    
    @Override
    public String objectHistory() {
        return "";
    }
    
    @Override
    public void checkAndStopTask() {
        // disconnect device ne
    }
    
    @Override
    public void updateAccountInfo() {
        account.updateData();
        // disconnect device ne 
    }
    
}
