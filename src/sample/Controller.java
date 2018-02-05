package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    Button box_1, box_2, box_3, box_4, box_5, box_6, box_7, box_8,
            box_9, box_10, box_11, box_12, box_13, box_14, box_15, box_0;

    @FXML
    GridPane gridPane_area;

    @FXML
    Text txt_malaca, txt_count, txt_time, txt_count_win, txt_time_win;

    @FXML
    AnchorPane ap_settings, ap_game, ap_win;

    @FXML
    Button btn_unmute, btn_mute, btn_setting_orange, btn_setting_blue, btn_setting_dark;


    private ArrayList<Button> boxs = new ArrayList<>(); // Лист со всеми кнопками от 1 - 16
    private int[] nums = new int[16];
    private Button[][] btnBox = new Button[4][4];

    private final Random random = new Random();

    private Timeline timeline = new Timeline();
    private long timeStart = 0;
    private long timePauseStart = 0;

    private int count = 0; // Счетчик ходов
    private String time = "";

    private static final String moveSFXLink = "src/sample/sounds/axe-wood-chop-01.wav";
    private static final String shakeAreaSFXLink = "src/sample/sounds/shaker-1.wav";

    private static Sound moveSound = new Sound(new File(moveSFXLink));
    private static Sound shakeSound = new Sound(new File(shakeAreaSFXLink));

    private FileOutputStream out;
    private Properties prop;
    private static final String PATH_TO_PROPERTIES = "src/sample/config.properties";

    private Timer timer;

    @FXML
    public void muteSound()
    {
        moveSound.setVolume(0);
        shakeSound.setVolume(0);
        btn_mute.setId("choosed-button-setting");
        btn_unmute.setId("unchoosed-button-setting");
        prop.setProperty("song", "0");
        if(out != null)
        {
            try {
                prop.store(out, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void unMuteSound()
    {
        moveSound.setVolume(1);
        shakeSound.setVolume(1);
        btn_mute.setId("unchoosed-button-setting");
        btn_unmute.setId("choosed-button-setting");
        prop.setProperty("song", "1");
        if(out != null)
        {
            try {
                prop.store(out, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void changeBackgroundBlue()
    {
        btn_setting_orange.setId("unchoosed-button-setting");
        btn_setting_blue.setId("choosed-button-setting");
        btn_setting_dark.setId("unchoosed-button-setting");
        ap_settings.setId("background-blue");
        ap_game.setId("background-blue");
        ap_win.setId("background-blue");
        prop.setProperty("color", "blue");
        if(out != null)
        {
            try {
                prop.store(out, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void changeBackgroundOrange()
    {
        btn_setting_orange.setId("choosed-button-setting");
        btn_setting_blue.setId("unchoosed-button-setting");
        btn_setting_dark.setId("unchoosed-button-setting");
        ap_settings.setId("background-orange");
        ap_game.setId("background-orange");
        ap_win.setId("background-orange");
        prop.setProperty("color", "orange");
        if(out != null)
        {
            try {
                prop.store(out, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void changeBackgroundDark()
    {
        btn_setting_orange.setId("unchoosed-button-setting");
        btn_setting_blue.setId("unchoosed-button-setting");
        btn_setting_dark.setId("choosed-button-setting");
        ap_settings.setId("background-dark");
        ap_game.setId("background-dark");
        ap_win.setId("background-dark");
        prop.setProperty("color", "dark");
        if(out != null)
        {
            try {
                prop.store(out, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void openSettings()
    {
        timer.timePause();
        ap_settings.setVisible(true);
    }

    @FXML
    public void backToGame()
    {
        ap_settings.setVisible(false);
        timer.timePlay();

    }

    @FXML
    public void moveBox(ActionEvent event)
    {
        Button btn = (Button) event.getSource();
        int x = 0, y = 0;

        for (int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++ )
            {
                if(btn == btnBox[i][j])
                {
                    x = i;
                    y = j;
                }
            }
        }

        if(x > 0 && btnBox[x - 1][y] == box_0)
        {
            gridPane_area.getChildren().remove(btn);
            gridPane_area.getChildren().remove(box_0);

            gridPane_area.add(btn, y, x - 1);
            gridPane_area.add(box_0, y, x);

            btnBox[x - 1][y] = btn;
            btnBox[x][y] = box_0;

            firstStepStartTime();
            moveSound.play();
            count++;
        }

        if(x < 3 && btnBox[x + 1][y] == box_0)
        {
            gridPane_area.getChildren().remove(btn);
            gridPane_area.getChildren().remove(box_0);

            gridPane_area.add(btn, y, x + 1);
            gridPane_area.add(box_0, y, x);

            btnBox[x + 1][y] = btn;
            btnBox[x][y] = box_0;

            firstStepStartTime();
            moveSound.play();
            count++;
        }

        if(y > 0 && btnBox[x][y - 1] == box_0)
        {
            gridPane_area.getChildren().remove(btn);
            gridPane_area.getChildren().remove(box_0);

            gridPane_area.add(btn, y - 1, x);
            gridPane_area.add(box_0, y, x);

            btnBox[x][y - 1] = btn;
            btnBox[x][y] = box_0;

            firstStepStartTime();
            moveSound.play();
            count++;
        }

        if(y < 3 && btnBox[x][y + 1] == box_0)
        {
            gridPane_area.getChildren().remove(btn);
            gridPane_area.getChildren().remove(box_0);

            gridPane_area.add(btn, y + 1, x);
            gridPane_area.add(box_0, y, x);

            btnBox[x][y + 1] = btn;
            btnBox[x][y] = box_0;

            firstStepStartTime();
            moveSound.play();
            count++;
        }

        txt_count.setText(String.valueOf(count));

        if(checkWin())
        {
            timer.timeStop();
            ap_win.setVisible(true);
            if(ap_win.isVisible())
            {
                txt_count_win.setText(String.valueOf(count));
                txt_time_win.setText(time);
            }
        }
    }

    private void firstStepStartTime()
    {
        if(count == 0)
        {
            timer.timeStart();
        }
    }


    private boolean checkWin()
    {
        boolean flag = true;

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                String boxNum = btnBox[i][j].getId();
                boxNum = boxNum.replace("box_", "");


                // Если ячейка равна 0, то 16, если нет, то номер кнопки
                int n = boxNum.equals("0") ? 16 : Integer.parseInt(boxNum);

                if(n != i * 4 + j + 1)
                {
                    flag = false;
                }
            }
        }

        return flag;
    }

    @FXML
    public void generateArea()
    {
        count = 0;
        time = "";
        txt_count.setText(String.valueOf(count));

        timer.timeStop();

        fillList();

        gridPane_area.getChildren().clear();
        try
        {
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    int num = random.nextInt(boxs.size());
                    gridPane_area.add(boxs.get(num), j, i);
                    boxs.get(num).setVisible(true);

                    String boxNum = boxs.get(num).getId();
                    boxNum = boxNum.replace("box_", "");

                    btnBox[i][j] = boxs.get(num);
                    nums[i * 4 + j] = boxNum.equals("0") ? 16 :
                            Integer.parseInt(boxNum);

                    boxs.remove(num);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(" ERROR: " + e);
        }

        if(!convergenceTest())
        {
            generateArea();
        }
    }

    @FXML
    public void resetWin()
    {
        generateArea();
        shakeSound.play();
        if(ap_win.isVisible())
            ap_win.setVisible(false);
    }

    /**
     * Проверка на сходимость
     */
    private boolean convergenceTest()
    {
        boolean even = false;
        int empty = 0;
        int sum = 0;
        int nowNum;

        for(int i = 0; i < nums.length; i++)
        {
            nowNum = nums[i];

            if(nowNum == 16)
            {
                empty = i / 4 + 1;
                continue;
            }

            for(int k = i + 1; k < nums.length; k++)
            {
                if(nowNum > nums[k])
                {
                    sum++;
                }
            }
        }
        sum += empty;

        if(sum % 2 == 0)
        {
            even = true;
        }

        return even;
    }

    private void fillList()
    {
        boxs.add(box_1);
        boxs.add(box_2);
        boxs.add(box_3);
        boxs.add(box_4);
        boxs.add(box_5);
        boxs.add(box_6);
        boxs.add(box_7);
        boxs.add(box_8);
        boxs.add(box_9);
        boxs.add(box_10);
        boxs.add(box_11);
        boxs.add(box_12);
        boxs.add(box_13);
        boxs.add(box_14);
        boxs.add(box_15);
        boxs.add(box_0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        timer = new Timer();
        timer.setTxtTime(txt_time);

        String color = "";
        String song = "";

        generateArea();

        FileInputStream fileInputStream = null;

        //инициализируем специальный объект Properties
        //типа Hashtable для удобной работы с данными
        prop = new Properties();

        try
        {
            //обращаемся к файлу и получаем данные
            fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);

            prop.load(fileInputStream);

            color = prop.getProperty("color");
            song = prop.getProperty("song");
        }
        catch (IOException e)
        {
            System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES + " не обнаружено");
            e.printStackTrace();
        }

        switch (color)
        {
            case "dark":
            default:
                changeBackgroundDark();
                break;

            case "orange":
                changeBackgroundOrange();
                break;

            case "blue":
                changeBackgroundBlue();
                break;
        }

        switch (song)
        {
            case "1":
            default:
                unMuteSound();
                break;
            case "0":
                muteSound();
                break;
        }

        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out = new FileOutputStream(PATH_TO_PROPERTIES);
            prop.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
