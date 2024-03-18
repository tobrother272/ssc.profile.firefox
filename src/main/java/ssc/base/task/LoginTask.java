/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import ssc.base.global.TC;
import ssc.base.ultil.ApiHelper;
import ssc.base.ultil.ThreadUtils;

/**
 *
 * @author PC
 */
public class LoginTask extends BaseTask {

    private String username;
    private String password;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean mainFunction() {
        try {
            updateMessage("Đang login ...");
            ThreadUtils.Sleep(500);
            Map<String, String> datas = new HashMap<String, String>();
            datas.put("username", username);
            datas.put("password", password);
            JSONObject res = ApiHelper.postData("/login", datas);
            if (res != null && res.get("success") != null) {
                if (res.get("success").toString().contains("true")) {
                    TC.getInts().setAccessToken(((JSONObject) res.get("data")).get("token").toString());
                    return true;
                } else {
                    updateMessage(res.get("message").toString());
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            updateMessage("Đăng nhập");
        }
        return false;
    }

}
