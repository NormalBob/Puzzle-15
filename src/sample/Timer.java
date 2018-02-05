package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by DIMA on 18.12.2017.
 */
public class Timer
{
    private Timeline timeline = new Timeline();
    private long timeStart = 0;
    private long timePauseStart = 0;

    private String time = "";

    @FXML
    Text txt_time;

    public void timeStart()
    {
        timeStart = System.currentTimeMillis();

        DateFormat timeFormat = new SimpleDateFormat( "mm:ss" );
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis( 500 ),
                        event -> {
                            final long diff = System.currentTimeMillis() - timeStart;
                            if ( diff < 0 )
                            {
                                //  timeLabel.setText( "00:00" );
                                txt_time.setText( timeFormat.format( 0 ) );
                            }
                            else
                            {
                                txt_time.setText( timeFormat.format( diff ) );
                                time = timeFormat.format( diff );
                            }
                        }
                )
        );

        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();

        this.timeline = timeline;
    }

    public void timeStop()
    {
        this.timeline.stop();
        DateFormat timeFormat = new SimpleDateFormat( "mm:ss" );
        txt_time.setText( timeFormat.format( 0 ) );
    }

    public void timePause()
    {
        this.timeline.pause();
        timePauseStart = System.currentTimeMillis();
    }

    public void timePlay()
    {
        this.timeline.play();
        timeStart = System.currentTimeMillis() - (timePauseStart - timeStart);
    }

    public void setTxtTime(Text txt_time)
    {
        this.txt_time = txt_time;
    }
}
