package sample.Game;

import sample.StartMeUp;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Zihui xu - Modified
 *  @version 1.0
 *  Setting Game Characters
 */
public class GameLogger extends Logger {

    private static Logger m_logger = Logger.getLogger("GameLogger");
    private DateFormat m_dateFormat = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss");
    private Calendar m_calendar = Calendar.getInstance();

    /**
     * Setting Game Logger
     * @throws IOException
     */
    public GameLogger() throws IOException {
        super("GameLogger", null);

        File directory = new File(System.
                getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" +
                StartMeUp.m_GAME_NAME + ".log");
        m_logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    /**
     * Create Format Message
     * @param message is String Object
     * @return
     */
    private String createFormattedMessage(String message) {
        return m_dateFormat.format(m_calendar.getTime()) + " -- " + message;
    }

    /**
     * Create Message
     * @param message is String Object
     */
    public void info(String message) {
        m_logger.info(createFormattedMessage(message));
    }

    /**
     *  Warning  Message
     * @param message is String Object
     */
    public void warning(String message) {
        m_logger.warning(createFormattedMessage(message));
    }

    /**
     * Create Message
     * @param message is String Object
     */
    public void severe(String message) {
        m_logger.severe(createFormattedMessage(message));
    }
}