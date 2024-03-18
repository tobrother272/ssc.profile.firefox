package ssc.theta.app.model;

import ssc.base.global.TC;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FirefoxUltis {
    public static String makeProfile(String username, String profilePath) {
        try {
            String[] cmd_array = {TC.getInts().browser_path, "-CreateProfile", "\"" + username + " " + profilePath + "\""};
            runProgress(cmd_array);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Send text lỗi exception";
        }
        return "";
    }
    public static void runProgressOpen(String[] cmd_array) {
        try {
            List<String> arr = new ArrayList<>();


            System.out.println("======");

            for(String a : cmd_array){
                System.out.print(" "+a);
            }

            System.out.println("======");

            arr.addAll(Arrays.asList(cmd_array));
            ProcessBuilder pb = new ProcessBuilder(arr);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            if(!process.waitFor(5, TimeUnit.SECONDS)) {
                //timeout - kill the process.
                // process.destroy(); // consider using destroyForcibly instead
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void runProgress(String[] cmd_array) {
        try {
            List<String> arr = new ArrayList<>();
            //arr.add("cmd");
            //arr.add("/c");
            arr.addAll(Arrays.asList(cmd_array));
            ProcessBuilder pb = new ProcessBuilder(arr);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println("runProgress " + line);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String openFireFox(String profilePath, String startPage) {
        try {
            if (startPage.isEmpty()) {
                String[] cmd_array = {TC.getInts().browser_path, "-profile", "\"" + profilePath + "\""};
                runProgressOpen(cmd_array);
            } else {
                String[] cmd_array = {TC.getInts().browser_path, "-profile", "\"" + profilePath + "\"", "-url", "\"" + startPage + "\""};
                runProgressOpen(cmd_array);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Send text lỗi exception";
        }
        System.out.println("xxxxxxxx");
        return "";
    }

    public static String openFireFoxNewTab(String profilePath, String startPage) {
        try {
            if (startPage.length() == 0) {
                String[] cmd_array = {TC.getInts().browser_path, "-profile", "\"" + profilePath + "\""};
                runProgress(cmd_array);
            } else {
                String[] cmd_array = {TC.getInts().browser_path, "-profile", "\"" + profilePath + "\"", "-new-tab", "\"" + startPage + "\""};
                runProgress(cmd_array);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Send text lỗi exception";
        }
        return "";
    }

    public static String openFireFox() {
        try {

            String[] cmd_array = {TC.getInts().browser_path, "\"https://youtube.com/\""};
            runProgress(cmd_array);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Send text lỗi exception";
        }
        return "";
    }

    public static String openFireFoxWithSearch(String profilePath, String searchQuery, String useragent) {
        try {
            String prefs = profilePath + "\\prefs.js";
            if (searchQuery.length() == 0) {
                String[] cmd_array = {TC.getInts().browser_path, "-profile", "\"" + profilePath + "\""};
                runProgress(cmd_array);
            } else {
                String[] cmd_array = {TC.getInts().browser_path, "-profile", "\"" + profilePath + "\"", "-search", "\"" + searchQuery + "\""};
                runProgress(cmd_array);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Send text lỗi exception";
        }
        return "";
    }
}
