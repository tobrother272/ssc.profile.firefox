/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.gologin;

import java.io.File;
import org.json.simple.JSONObject;
import ssc.base.global.TC;
import ssc.base.task.BaseLogTask;
import ssc.base.ultil.GologinHelper;
import ssc.base.ultil.MyFileUtils;
import ssc.theta.app.model.GoogleAccount;

/**
 *
 * @author PC
 */
public class OpenFirefoxBrowserTask extends BaseLogTask {

    private GologinDriver driver;
    private GoogleAccount account;

    public OpenFirefoxBrowserTask(GoogleAccount account) {
        super();
        this.account = account;
    }

    @Override
    public boolean mainFunction() {
        try {

            String profileF = TC.getInts().profile_folder + File.separator + account.getConnectionName();
            insertSuccessLog("Profile path " + profileF);



            updateMessage("Đã mở trình duyệt");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
