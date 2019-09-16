package iomatix.spigot.rpgleveledmobs.logging;

import java.io.PrintWriter;
import java.io.File;
import java.util.Date;
import java.util.logging.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import iomatix.spigot.rpgleveledmobs.Main;

public class LogsModule {

	private static Logger log;
	private static File logFile;
	private static PrintWriter writer;
	private static DateFormat dateFormat;
	private static boolean loggingEnabled;
	private static boolean debugEnabled;
	private static String INFO;
	private static String DEBUG;
	private static String SEVERE;
	private static String WARNING;
	public static String PLUGIN_TITLE;

	public static void saveLog() {
		LogsModule.writer.flush();
		LogsModule.writer.close();
	}

	public static void log(final LogLevel level, final String log) {
		switch (level) {
		case Debug: {
			debug(log);
			break;
		}
		case Info: {
			info(log);
			break;
		}
		case Error: {
			error(log);
			break;
		}
		case Warning: {
			warning(log);
			break;
		}
		}
	}

	public static void info(final String logMsg) {
		if (Bukkit.getConsoleSender() != null) {
			Bukkit.getConsoleSender().sendMessage(LogsModule.PLUGIN_TITLE+ " "  + LogsModule.INFO+ " " + logMsg);
		} else {
			LogsModule.log.info(logMsg);
		}
		physicalLog("[Info]" + logMsg);
	}

	public static void debug(final String logMessage) {
		if (Bukkit.getConsoleSender() != null && LogsModule.debugEnabled) {
			Bukkit.getConsoleSender().sendMessage(LogsModule.PLUGIN_TITLE+ " " + LogsModule.DEBUG+ " " + logMessage);
		}
		physicalLog("[Debug]" + logMessage);
	}

	public static void error(final String logMessage) {
		if (Bukkit.getConsoleSender() != null) {
			Bukkit.getConsoleSender().sendMessage(LogsModule.PLUGIN_TITLE+ " "  + LogsModule.SEVERE+ " " + logMessage);
		} else {
			LogsModule.log.severe(logMessage);
		}
		physicalLog("[Error]" + logMessage);
	}

	public static void warning(final String logMessage) {
		if (Bukkit.getConsoleSender() != null) {
			Bukkit.getConsoleSender().sendMessage(LogsModule.PLUGIN_TITLE+ " "  + LogsModule.WARNING+ " " + logMessage);
		} else {
			LogsModule.log.warning(logMessage);
		}
		physicalLog("[Warning]" + logMessage);
	}

	public static void setLoggingEnabled(final boolean enabled) {
		LogsModule.loggingEnabled = enabled;
	}

	public static void setDebugEnabled(final boolean enabled) {
		LogsModule.debugEnabled = enabled;
	}

	private static void physicalLog(final String message) {
		if (LogsModule.loggingEnabled) {
			final Date date = new Date();
			LogsModule.writer.println("[" + LogsModule.dateFormat.format(date) + "]" + message);
			LogsModule.writer.flush();
		}
	}

	static {
		LogsModule.log = Main.RPGMobs.getLogger();
		LogsModule.logFile = new File(Main.RPGMobs.getDataFolder().toString() + File.separator + "Log.txt");
		LogsModule.dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			if (!LogsModule.logFile.exists()) {
				LogsModule.logFile.createNewFile();
				LogsModule.writer = new PrintWriter(LogsModule.logFile);
			} else {
				LogsModule.writer = new PrintWriter(LogsModule.logFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogsModule.loggingEnabled = true;
		LogsModule.debugEnabled = true;
		INFO = ChatColor.GRAY + "[" + ChatColor.GREEN + "Info" + ChatColor.GRAY + "]";
		DEBUG = ChatColor.GRAY + "[" + ChatColor.YELLOW + "Debug" + ChatColor.GRAY + "]";
		SEVERE = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "Error" + ChatColor.GRAY + "]";
		WARNING = ChatColor.GRAY + "[" + ChatColor.RED + "Warning" + ChatColor.GRAY + "]";
		PLUGIN_TITLE = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "RPGLeveledMobs" + ChatColor.DARK_GRAY + "]";
	}

	public enum LogLevel {
		Debug, Info, Error, Warning;
	}

}
