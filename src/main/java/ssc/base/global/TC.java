/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.global;

import FormComponent.SSCTextArea;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.FileChooser;
import ssc.base.annotation.SettingField;
import static ssc.base.gologin.GologinDriver.BROWSER_PATH;
import ssc.base.ui.components.SSCTextField;
import static ssc.base.ultil.CMDUtils.getPortFree;
import ssc.base.view.SSCForm;

/**
 *
 * @author PC
 */
public class TC {

    public static interface SETTING_TYPE {

        public static String INTEGER = "INTEGER";
        public static String Text = "Text";
        public static String FILE = "FILE";
        public static String TextArea = "TextArea";
        public static String FOLDER = "FOLDER";
        public static String VARCHAR = "VARCHAR";
        public static String BIGINT = "BIGINT";
    }
    public List<String> arrOpens;
    public List<Integer> arrPort;
    public List<String> arrHotEmail;

    public int getPort() {
        int port = getPortFree(arrPort);
        arrPort.remove(Integer.valueOf(port));
        //return 4723;
        return port;
    }

    public static void log(String log, String _class, String title) {
        System.out.println("[" + _class + "]------------------" + title + "--------------------");
        System.out.println(log);
        System.out.println("[" + _class + "]---------------------" + title + "--------------------");

    }

    public static boolean isWin() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return true;
        }
        return false;
    }

    public static TC ints;

    private Preferences pre;

    public Preferences getPre() {
        return pre;
    }

    public void setPre(Preferences pre) {
        this.pre = pre;
    }

    public TC() {
        pre = Preferences.userRoot().node("premium." + this.getClass().getCanonicalName().replace(".glo", ""));
        accessToken = new SimpleStringProperty(pre.get("accessToken", ""));
        arrOpens = new ArrayList<>();
        arrPort = new ArrayList<>();
        arrHotEmail=new ArrayList<>();
        for (int i = 4723; i < 5000; i++) {
            arrPort.add(i);
        }
    }

    public static TC getInts() {
        if (ints == null) {
            ints = new TC();
            ints.initValueSetting();
        }
        return ints;
    }

    public void initValueSetting() {
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SettingField.class) != null) {
                try {
                    field.setAccessible(true);
                    SettingField ano = field.getAnnotation(SettingField.class);
                    if (ano.type().equals(SETTING_TYPE.INTEGER)) {
                        field.set(this, pre.getInt(ano.pre_name(), -1));
                    } else {
                        if (ano.pre_name().equals("browser_path")) {
                            field.set(this, pre.get(ano.pre_name(), BROWSER_PATH));
                        } else {
                            field.set(this, pre.get(ano.pre_name(), ""));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showSettingView(SSCForm formContainer) {
        for (Field field : getClass().getDeclaredFields()) {
            try {
                if (field.getAnnotation(SettingField.class) != null) {
                    field.setAccessible(true);
                    SettingField ano = field.getAnnotation(SettingField.class);
                    if (ano.has_view()) {
                        if (ano.type().equals(SETTING_TYPE.Text)) {
                            formContainer.addChild(new SSCTextField(formContainer, ano.pre_name(), ano.label(), "", ano.place_holder(), Arrays.asList("")));
                        } else if (ano.type().equals(SETTING_TYPE.TextArea)) {
                            formContainer.addChild(new SSCTextArea(formContainer, ano.pre_name(), ano.label(), "", ano.place_holder(), Arrays.asList("")));
                        } else if (ano.type().equals(SETTING_TYPE.FOLDER)) {
                            SSCTextField text = new SSCTextField(formContainer, ano.pre_name(), ano.label(), "", ano.place_holder(), Arrays.asList(""));
                            text.enableSelectFolder("Chọn thư mục lưu gologin profile", "");
                            formContainer.addChild(text);
                        } else if (ano.type().equals(SETTING_TYPE.FILE)) {
                            SSCTextField text = new SSCTextField(formContainer, ano.pre_name(), ano.label(), "", ano.place_holder(), Arrays.asList(""));
                            text.enableSelectFile("Chọn file trình duyệt", new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("Exe", "*.exe")});
                            formContainer.addChild(text);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void saveSetting(SSCForm formContainer) {
        formContainer.saveStorageValue();
        int index = 0;
        for (Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(SettingField.class) != null) {
                try {
                    field.setAccessible(true);
                    SettingField ano = field.getAnnotation(SettingField.class);
                    if (!ano.has_view()) {
                        continue;
                    }
                    if (ano.type().equals(SETTING_TYPE.INTEGER)) {
                        field.set(this, Integer.parseInt(formContainer.getValues().get(index)));
                    } else {
                        field.set(this, formContainer.getValues().get(index));
                    }
                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private SimpleStringProperty accessToken;

    public SimpleStringProperty accessTokenProperty() {
        return accessToken;
    }

    public String getAccessToken() {
        return "Bearer " + accessToken.get();
    }

    public void setAccessToken(String accessToken) {
        this.accessToken.set(accessToken);
        pre.put("accessToken", accessToken);

    }

    @SettingField(pre_name = "user_id", type = SETTING_TYPE.INTEGER, label = "")
    public int user_id;
    public String api_url = "http://user.sscapi.co/api/v2";

    @SettingField(pre_name = "profile_folder", type = SETTING_TYPE.FOLDER, has_view = true, label = "Thư mục lưu profile", place_holder = "Nhấp đúp chọn thư mục ...")
    public String profile_folder;
    @SettingField(pre_name = "browser_path", type = SETTING_TYPE.FILE, has_view = true, label = "Firefox exe file .", place_holder = "Nhấp đúp chọn file exe ...")
    public String browser_path;
    @SettingField(pre_name = "role", type = SETTING_TYPE.Text, label = "")
    public String role;
    @SettingField(pre_name = "enabled", type = SETTING_TYPE.INTEGER, has_view = false)
    public int enabled;
    @SettingField(pre_name = "file_error", type = SETTING_TYPE.Text, has_view = false)
    public String file_error;

}
