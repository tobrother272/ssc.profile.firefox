/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import org.json.simple.JSONObject;

/**
 *
 * @author PC
 */
public class MyFileUtils {

    public static void printLogToDesktop() {
        try {
            if (new File("log.txt").exists()) {
                PrintStream printStream = new PrintStream(new FileOutputStream("log.txt"));
                System.setOut(printStream);
                System.setErr(printStream);
            }
        } catch (Exception e) {
        }
    }

    public static boolean copyFile(File source, File target) {
        try (
                 InputStream in = new FileInputStream(source);  OutputStream out = new FileOutputStream(target)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getFileExtension(String file) {
        try {
            return file.substring(file.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static void copy(File sourceLocation, File targetLocation) throws IOException {
        //&&!sourceLocation.getName().contains("firefox") khong bit de lam gi
        if (sourceLocation.isDirectory() && !sourceLocation.getName().contains("cache2")) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    public static void copyDirectory(File source, File target) {
        try {
            if (!target.exists()) {
                //System.out.println("tạo "+target.getAbsolutePath());
                target.mkdir();
            }
            for (String f : source.list()) {
                copy(new File(source, f), new File(target, f));
            }
        } catch (Exception e) {
        }
    }

    public static void removeLine(File inputFile, String LineDelete) {
        try {
            // Create a File object for the temporary file. 
            File tempFile = new File("myTempFile.txt");

            // Create a BufferedReader object to read the original file. 
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            // Create a BufferedWriter object to write to the temporary file. 
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            // Read each line from the original file. 
            String line;
            while ((line = reader.readLine()) != null) {

                // If the line does not match the line you want to delete, write it to the temporary file. 
                if (!line.equals(LineDelete)) {
                    writer.write(line + System.getProperty("line.separator"));
                }
            }

            // Close the readers and writers. 
            reader.close();
            writer.close();

            // Delete the original file. 
            inputFile.delete();

            // Rename the temporary file to the original file. 
            tempFile.renameTo(inputFile);
        } catch (Exception e) {
        }

    }

    public static Boolean extractZip(String source, String destination) {
        List<String> arr = new ArrayList<>();
        arr.add(System.getProperty("user.dir") + File.separator + "tool" + File.separator + "7-Zip" + File.separator + "7z");
        arr.add("x");
        arr.add(source);
        arr.add("-o" + destination);
        String[] array = new String[arr.size()];
        arr.toArray(array);
        CMDUtils.cmd(array);
        return true;
    }

    public static void unzipFolder(Path source, Path target) throws IOException {

        try ( ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {

            // list files in zip
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {

                boolean isDirectory = false;
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, target);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);

                    // copy files, classic
                    /*try (FileOutputStream fos = new FileOutputStream(newPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }*/
                }

                zipEntry = zis.getNextEntry();

            }
            zis.closeEntry();

        }

    }

    public static Path zipSlipProtect(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }

    public static Boolean checkZip(String source) {
        try {
            ZipFile zipFile = new ZipFile(source);
            for (FileHeader hd : zipFile.getFileHeaders()) {
                // System.out.println(hd.getFileName());
            }
            return true;
        } catch (ZipException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<String> getListStringFromFile(String filePath) {
        ArrayList<String> result = new ArrayList<>();
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"));      
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();

        } catch (IOException e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static void openUrl(String url) {
        if (System.getProperty("os.name").contains("Linux")) {
            try {
                Runtime rt = Runtime.getRuntime();
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape", "opera", "links", "lynx"};

                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++) {
                    if (i == 0) {
                        cmd.append(String.format("%s \"%s\"", browsers[i], url));
                    } else {
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                    }
                }
                // If the first didn't work, try the next browser and so on

                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            } catch (Exception e) {
            }
        } else {
            try {
                Desktop desktop = java.awt.Desktop.getDesktop();
                URI oURL = new URI(url);
                desktop.browse(oURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteFolder(File file) {
        try {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteFolder(f);
                }
            }
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void openFile(String url) {
        if (url.length() == 0) {
            return;
        }
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                File myFile = new File(url);
                desktop.open(myFile);
            } catch (Exception ex) {

            }
        }
    }

    public static String getStringFromFile(String filePath) {
        String result = "";
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"));      
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result = result + line + "\n";
            }
            br.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return result.trim();
    }

    public static JSONObject getFileInfo(String path) {
        try {
            JSONObject object = new JSONObject();
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }

            object.put("dateTime", file.lastModified());
            object.put("dateTimeString", StringUtils.convertLongToDataTime("dd/MM HH:mm", file.lastModified()));
            object.put("size", file.length() / 1024 / 1024);

            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createFolder(String uri) {
        File theDir = new File(uri);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                se.printStackTrace();
            }
            if (result) {
                //System.out.println("DIR created");  
            }
        }
    }

    public static boolean writeStringToFileUTF8(String content, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "UTF-8"));
            writer.write(content);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean writeStringToFile(String content, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.write(content);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean deleteFile(String fileurl) {
        try {
            File file = new File(fileurl);
            if (!file.exists()) {
                return true;
            }
            if (file.delete()) {
                return true;
                //System.out.println(file.getName() + " is deleted!");
            } else {
                return false;
                //System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            System.err.println("delete fail :" + e.getMessage());
            return false;
        }
    }

    public static boolean writeJsonToFile(String jsonString, String pathFile) {
        FileWriter file = null;
        try {
            //System.out.println("---------------------");
            //System.out.println(jsonString);

            file = new FileWriter(pathFile);
            file.write(jsonString);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (Exception e) {
            }

        }
        return false;
    }

    public static boolean zipFolderToFile(File fileToBeCreated, File directory) {
        try {
            ZipFile zipFile = new ZipFile(fileToBeCreated);
            ZipParameters params = new ZipParameters();
            params.setOverrideExistingFilesInZip(false);
            params.setIncludeRootFolder(false);
            zipFile.addFolder(directory, params);
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return false;
    }
}
