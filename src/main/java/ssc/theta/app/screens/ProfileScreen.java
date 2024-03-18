/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.screens;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ssc.base.ui.components.ListSelectItem;
import ssc.base.view.Navigator;
import ssc.base.view.ScreenBase;
import ssc.base.database.BaseModel;
import ssc.base.database.BaseTable;
import ssc.base.global.TC;
import ssc.base.modal.TaskModal;
import ssc.base.task.BaseTask;
import ssc.base.ui.components.ChildMenuButton;
import ssc.base.ultil.MyFileUtils;
import ssc.base.view.SSCMessage;
import ssc.theta.app.model.FirefoxUltis;
import ssc.theta.app.model.GoogleAccount;
import ssc.theta.app.model.sqlQuery.GoogleAccountQuery;

/**
 * @author PC
 */
public class ProfileScreen extends ScreenBase {

    public ProfileScreen(String title, int tabIndex, String menuIcon, Navigator navigator) {
        super(title, tabIndex, menuIcon, navigator);
    }

    ObservableList<GoogleAccount> arrData;
    private BaseTable profilesTable;

    @Override
    public void initView() {
        arrData = FXCollections.observableArrayList();
        List<ListSelectItem> selectItems = new ArrayList<>();
        selectItems.add(new ListSelectItem("Thêm tài khoản", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                GoogleAccount ga = new GoogleAccount(arrData.size() + 1);
                ga.showAddViewFromDB(arrData, "Điền thông tin tài khoản");
            }
        }));
        selectItems.add(new ListSelectItem("Thêm nhiều tài khoản", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                BaseModel.AddDataFromFileTxt(arrData);
            }
        }));
        profilesTable = new BaseTable("profilesContainer", arrData, "Import", selectItems) {
            @Override
            public ObservableList initGetData(String query) {
                return GoogleAccountQuery.getListGoogleAccount(query);
            }

            @Override
            public List<TableColumn> initArrCol() {
                return GoogleAccount.getArrCol(arrData);
            }

            @Override
            public void removeEvt(ObservableList listRemove) {
                ObservableList<GoogleAccount> arrDataS = FXCollections.observableArrayList();
                arrDataS.addAll(listRemove);
                for (GoogleAccount ga : arrDataS) {
                    //System.out.println("a "+ga.getGoogleAccountname());
                    if (ga.deleteData().length() == 0) {
                        arrData.remove(ga);
                    }
                }
            }
        };

        profilesTable.getTvData().setRowFactory(tv -> {
            TableRow<GoogleAccount> row = new TableRow<>();
            row.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    GoogleAccount account = (GoogleAccount) row.getItem();
                    if (account == null) {
                        return;
                    }
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("https://www.tiktok.com/"+account.getNickname()));
                    } catch (Exception ex) {
                        // TODO Auto-generated catch block
                        //ex.printStackTrace();
                    }

                }
            });
            return row;
        });


    }

    @Override
    public void reloadView() {
        profilesTable.reloadData("");
        if(!TC.getInts().browser_path.isEmpty() && !TC.getInts().profile_folder.isEmpty()){
            arrData.clear();
            BaseTask reloadFolder = new BaseTask() {
                @Override
                public boolean mainFunction() {
                    try {
                        File file[] = new File(TC.getInts().profile_folder).listFiles();
                        int stt= 1 ;
                        for (File profileF:file ){
                            GoogleAccount ga=new GoogleAccount(stt);
                            ga.setUsername(profileF.getName());
                            arrData.add(ga);
                        }
                        System.out.println("reload data");
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    return true;
                }
            };
            reloadFolder.start();
        }




    }

    private TaskModal expoCTaskModal;
    public static ObservableList<String> GRAPHIC_CARD
            = FXCollections.observableArrayList(
            "ANGLE (NVIDIA GeForce GTX 1060 3GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (NVIDIA GeForce GTX 1060 6GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (NVIDIA GeForce GTX 1650 4GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (Microsoft Basic Render Driver Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (AMD Radeon RX 560 Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (NVIDIA GeForce GTX 660 2GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (NVIDIA GeForce GTX 460 2GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (NVIDIA GeForce GTX 760 2GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (NVIDIA GeForce GTX 970 2GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (NVIDIA GeForce GTX 1650 Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (Radeon RX 470 4GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (Radeon RX 470 8GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (Radeon RX 570 4GB Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (Radeon RX 580 4GB Driver Direct3D11 vs_5_0 ps_5_0)",
            "ANGLE (Radeon RX 590 4GB Driver Direct3D11 vs_5_0 ps_5_0)"
    );
    public static ObservableList<String> userAgents
            = FXCollections.observableArrayList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36"
    );

    public static ObservableList<String> gl_versions
            = FXCollections.observableArrayList(
            "2.0",
            "2.1",
            "3.0",
            "3.1",
            "3.2",
            "3.3",
            "4.0"
    );
    public static ObservableList<String> gl_shading_language
            = FXCollections.observableArrayList(
            "1.10",
            "1.20",
            "1.30",
            "1.40",
            "1.50",
            "3.30",
            "4.00"
    );
    public static ObservableList<String> gl_renderer
            = FXCollections.observableArrayList(
            "NVIDIA GeForce GTX 1060",
            "NVIDIA GeForce GTX 1650",
            "Microsoft Basic Render Driver",
            "AMD Radeon RX 560",
            "NVIDIA GeForce GTX 660",
            "NVIDIA GeForce GTX 460",
            "NVIDIA GeForce GTX 760",
            "NVIDIA GeForce GTX 970",
            "NVIDIA GeForce GTX 1650)",
            "Radeon RX 470",
            "Radeon RX 470",
            "Radeon RX 570",
            "Radeon RX 580",
            "Radeon RX 590"
    );
    public static ObservableList<String> gl_vendor
            = FXCollections.observableArrayList(
            "NVIDIA Corporation",
            "Intel Open Source Technology Center",
            "Intel Inc.",
            "Intel Corporation",
            "Intel",
            "ATI Technologies Inc."
    );
    public static ObservableList<String> media_devices
            = FXCollections.observableArrayList(
            "NVIDIA Corporation",
            "Intel Open Source Technology Center",
            "Intel Inc.",
            "Intel Corporation",
            "Intel",
            "ATI Technologies Inc."
    );

    @Override
    public void initArrBtn(List<ChildMenuButton> arrChildMenuBtn) {

        arrChildMenuBtn.add(new ChildMenuButton("Thêm profile", "Thêm 1 profile mới", "PLUS_CIRCLE", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Chọn 1 thư mục lưu profile");
                chooser.setInitialDirectory(new File(TC.getInts().profile_folder.isEmpty()?System.getProperty("user.dir"):TC.getInts().profile_folder));
                File selectedDirectory = chooser.showDialog(new Stage());
                if (selectedDirectory != null) {
                    BaseTask exportTask = new BaseTask() {
                        @Override
                        public boolean mainFunction() {


                            String jsonOject = "{"
                                    + "\"localIP\":\"192.168.1.9\","
                                    + "\"localIP_RP\":\"192.168.1.9\","
                                    + "\"card\":\"" + GRAPHIC_CARD.get(new Random().nextInt(GRAPHIC_CARD.size())) + "\","
                                    + "\"canvasNoiseR\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                                    + "\"canvasNoiseG\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                                    + "\"canvasNoiseB\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                                    + "\"canvasNoiseA\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                                    + "\"webglNoise\":" + (Math.ceil(new Random().nextFloat() * 10) / 10) + ","
                                    + "\"webgl_35661\":\"[128, 192, 256]\","
                                    + "\"webgl_else\":" + (new Random().nextInt(4) + 1) + ","
                                    + "\"webgl_36349\":" + ((new Random().nextInt(2) + 1) * 4096) + ","
                                    + "\"webgl_34930\":" + ((new Random().nextInt(3) + 1) * 16) + ","
                                    + "\"webgl_34076\":" + ((new Random().nextInt(2) + 1) * 16384) + ","
                                    + "\"webgl_3413\":" + (new Random().nextInt(4) + 1) + ","
                                    + "\"webgl_3386\":" + ((new Random().nextInt(3) + 1) * 8192) + ","
                                    + "\"gl_version\":\"" + gl_versions.get(new Random().nextInt(gl_versions.size())) + "\","
                                    + "\"gl_vendor\":\"" +  gl_vendor.get(new Random().nextInt(gl_vendor.size()))+ "\","
                                    + "\"gl_renderer\":\"" + gl_renderer.get(new Random().nextInt(gl_renderer.size())) + "\","
                                    + "\"gl_shading_language\":\"" +gl_shading_language.get(new Random().nextInt(gl_shading_language.size())) + "\","
                                    + "\"gl_unmasked_vendor\":\"" +gl_vendor.get(new Random().nextInt(gl_vendor.size())) + "\","
                                    + "\"mediaDevice1\":" + (new Random().nextInt(4) + 1) + ","
                                    + "\"mediaDevice2\":" + (new Random().nextInt(4) + 1) + ","
                                    + "\"hardwareConcurrency\":" + ((new Random().nextInt(4) + 1) * 2) + ","
                                    + "\"userAgent\":\"" +userAgents.get(new Random().nextInt(userAgents.size()))+ "\","
                                    + "\"deviceMemory\":" + ((new Random().nextInt(4) + 1)) + ","
                                    + "\"font_offset\":\"[" + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "," + ((new Random().nextInt(4)) - 2) + "]\","
                                    + "\"audioContext_1\":" + (Math.ceil(new Random().nextFloat() * 10000) / 10000) + ","
                                    + "\"audioContext_2\":" + (Math.ceil(new Random().nextFloat() * 10000) / 10000) + ","
                                    + "\"audioContext_3\":" + (Math.ceil(new Random().nextFloat() * 10000) / 10000) + ","
                                    + "\"audioContext_4\":" + (Math.ceil(new Random().nextFloat() * 10000) / 10000) + "}";
                            String documentString
                                    = "<!doctype html>\n"
                                    + "<html>\n"
                                    + "<head>\n"
                                    + "<head>\n"
                                    + "<body>\n"
                                    + "	<localIp id=\"fingerprint\">" + jsonOject + "</localIp>\n"
                                    + "</body>	\n"
                                    + "</html>";
                            MyFileUtils.writeStringToFileUTF8(documentString, selectedDirectory.getAbsolutePath() + "\\fingerprintConfig.html");
                            FirefoxUltis.makeProfile(selectedDirectory.getName(),selectedDirectory.getAbsolutePath());
                            FirefoxUltis.openFireFox(selectedDirectory.getAbsolutePath(),"file:///"+selectedDirectory.getAbsolutePath() + "\\fingerprintConfig.html");
                            return true;
                        }
                    };
                    exportTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent t) {

                        }
                    });
                    exportTask.start();
                }


            }
        }));


    }

}
