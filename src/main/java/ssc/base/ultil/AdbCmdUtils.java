/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ssc.base.ultil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class AdbCmdUtils {

    public static List<String> getListDeviceAsString() {
        List<String> outPut = new ArrayList<>();
        Process process;
        try {
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("adb", "devices", "-l"));
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                if (line.contains("device product") || line.contains("recovery product")) {
                    System.out.println(line);
                    outPut.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }
        return outPut;
    }

    public static List<String> runOriginCommand(List<String> cmds) {
        long startTime = System.currentTimeMillis();
        List<String> outPut = new ArrayList<>();

        System.out.println(cmds);

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
                // System.out.println(line);
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

    public static void runCommandNonDevice(List<String> cmds) {
        for (String cmd : cmds) {
            System.out.print(" " + cmd);
        }
        System.out.println("");
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
                //System.out.println(line);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }
    }

    private static List<String> runCommand(List<String> cmds) {
        long startTime = System.currentTimeMillis();
        for (String cmd : cmds) {
            System.out.print(" " + cmd);
        }
       System.out.println("");
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
                //System.out.println(line);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }

        // System.out.println((System.currentTimeMillis()) - startTime);
        return outPut;
    }

    private static void runCommandAsBat(List<String> cmds) {
        String arr = "";
        for (String cmd : cmds) {
            arr = arr + " " + cmd;
        }
        //System.out.println(arr);
        String fileBat = System.getProperty("user.dir") + File.separator + "temps" + File.separator + System.currentTimeMillis() + ".bat";

        MyFileUtils.writeStringToFile(arr, fileBat);
        Process process;
        try {
            ProcessBuilder pb = new ProcessBuilder(fileBat);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                //System.out.println(line);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }

    }

    public static List<String> runAdbCommand(String deviceId, List<String> arr) {
        List<String> cmd = new ArrayList<>();
        cmd.add("adb");
        cmd.add("-s");
        cmd.add(deviceId);
        cmd.addAll(arr);
        return runCommand(cmd);
    }

    public static void runAdbCommandAsBat(String deviceId, List<String> arr) {
        List<String> cmd = new ArrayList<>();
        cmd.add("adb");
        cmd.add("-s");
        cmd.add(deviceId);
        cmd.addAll(arr);
        runCommandAsBat(cmd);
    }

    public static List<String> runAdbShellCommand(String deviceId, List<String> arr) {
        List<String> cmd = new ArrayList<>();
        try {
            cmd.add("adb");
            cmd.add("-s");
            cmd.add(deviceId);
            cmd.add("shell");
            cmd.addAll(arr);
        } catch (Exception e) {
        }
        return runCommand(cmd);
    }


}
