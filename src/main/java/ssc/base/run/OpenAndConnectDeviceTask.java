/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run;

import org.openqa.selenium.By;
import org.json.simple.JSONObject;
import ssc.base.global.TC;
import ssc.base.ultil.ApiHelper;

/**
 * @author simpl
 */
public class OpenAndConnectDeviceTask extends GoogleInteractiveChildTask {

    public OpenAndConnectDeviceTask(ActionWithAccountBase task) {
        super(task);
    }

    @Override
    protected Boolean call() {
        try {



            return true;
        } catch (Exception e) {
            insertErrorLog("Thiết bị mất kết nối " + e.getMessage(), "");
            insertErrorLog("Thiết bị mất kết nối", "");
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void sendResultToParent() {

    }

    @Override
    public String setActionName() {
        return " phút | Chờ kết nối thiết bị ";

    }

}
