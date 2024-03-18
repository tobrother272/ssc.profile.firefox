/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import org.json.simple.JSONObject;
import ssc.base.global.TC;
import ssc.base.ultil.ApiHelper;

/**
 *
 * @author PC
 */
public class CheckAccessTokenTask extends BaseTask {

    @Override
    public boolean mainFunction() {
        try {
            JSONObject res = ApiHelper.getDataWithAccessToken("/userinfo");
            System.out.println(res.toJSONString());
            if (res != null && res.get("data") != null) {
                System.out.println("aaaaaaaaa");
                JSONObject data = ((JSONObject) res.get("data"));
                TC.getInts().role =data.get("role").toString();
                TC.getInts().enabled = Integer.parseInt(data.get("enabled").toString());
                return true;
            } else {
                TC.getInts().setAccessToken("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TC.getInts().setAccessToken("");
        return true;
    }

}
