/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ssc.base.ultil;


import org.json.simple.JSONObject;


/**
 *
 * @author ASUS
 */
public class GologinHelper {

    

    public static JSONObject makeProfile(String ver, String os, String name) {
        try {
            JSONObject finger = ApiHelper.getDataWithAccessToken("/browser?name="+name+"&os="+os+"&ver"+ver);
            if(finger.get("data")==null){
                return null;
            }
           return (JSONObject)finger.get("data");
        } catch (Exception e) {
        }
        return null;
    }

    
}
