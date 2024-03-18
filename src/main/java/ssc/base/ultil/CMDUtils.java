/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import static ssc.base.ultil.ThreadUtils.Sleep;
import ssc.theta.app.model.TaskLog;

/**
 *
 * @author PC
 */
public class CMDUtils {

    public static Boolean checkAndKillProcess(String processName) {
        try {
            String cmd = "cmd /c Taskkill /IM " + processName + " /F ";
            String cmd_array[] = cmd.split(" ");
            for (int i = 0; i < cmd_array.length; i++) {
                if (cmd_array[i].equals("processName")) {
                    cmd_array[i] = processName;
                }
            }
            for (String string : cmd_array) {
                System.out.print(" " + string);
            }
            ProcessBuilder pb = new ProcessBuilder(cmd_array);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();
            Sleep(2000);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            try {
                while (input.readLine() != null && !(line = input.readLine()).equals("")) {
                    System.out.println("line " + line);
                    if (line.contains("has been terminated")) {
                        process.destroy();
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return true;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    public static boolean cmdStartWithNewLine(List<String> queries, ObservableList<TaskLog> historyList) {
        Thread thread = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    String[] itemsArray = new String[queries.size()];
                    itemsArray = queries.toArray(itemsArray);
                    cmdWithNewLine(itemsArray, historyList);
                } catch (Exception e) {
                }
                return true;
            }
        });
        thread.setDaemon(false);
        thread.start();
        return true;
    }

    public static int getPortFree(List<Integer> arrPort) {
        ServerSocket serverSocket = null;
        for (int i : arrPort) {
            try {
                serverSocket = new ServerSocket(i);
                return i;
            } catch (IOException e) {

            } finally {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                }

            }
        }
        return -1;
    }

    public static boolean openAnotherApp(String appPath) {
        try {
            File file = new File(appPath);
            Desktop.getDesktop().open(file);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static List<String> cmdWithNewLine(String cmd_array[], ObservableList<TaskLog> historyList) {
        List<String> outPut = new ArrayList<>();
        String s;
        Process process;
        // System.out.println("\n-----\n");
        for (String string : cmd_array) {
            //System.out.print(" "+string);
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd_array);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                try {
                    historyList.add(0, new TaskLog(System.currentTimeMillis(), line, "", "", 0));
                } catch (Exception e) {
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

    public static List<String> cmdStart(List<String> queries) {
        String[] itemsArray = new String[queries.size()];
        itemsArray = queries.toArray(itemsArray);
        List<String> outPut = new ArrayList<>();
        String s;
        Process process;
        System.out.println(queries);

        try {
            ProcessBuilder pb = new ProcessBuilder(itemsArray);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {

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

    public static List<String> cmdStartTF(List<String> queries, TextArea tf) {
        String[] itemsArray = new String[queries.size()];
        itemsArray = queries.toArray(itemsArray);
        List<String> outPut = new ArrayList<>();
        String s;
        Process process;
        System.out.println(queries);

        try {
            ProcessBuilder pb = new ProcessBuilder(itemsArray);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                tf.appendText(line + "\n");

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

    public static List<String> cmdStartTFWait(List<String> queries, TextArea tf, int time) {
        String[] itemsArray = new String[queries.size()];
        itemsArray = queries.toArray(itemsArray);
        List<String> outPut = new ArrayList<>();
        String s;
        Process process;
        System.out.println(queries);

        try {
            ProcessBuilder pb = new ProcessBuilder(itemsArray);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            int i = 0;
            while (i < time) {
                i++;
                line = input.readLine();
                if (line != null) {
                    tf.appendText(line + "\n");
                } else {
                    tf.appendText("wait " + i + "/" + time + " " + "\n");
                }
                Sleep(1000);

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

    public static List<String> cmd(String cmd_array[]) {
        List<String> outPut = new ArrayList<>();
        String s;
        Process process;
        // System.out.println("\n-----\n");
        for (String string : cmd_array) {
            //System.out.print(" "+string);
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd_array);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                if (line.contains("device product")) {
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
}
