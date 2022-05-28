/*     */ package org.slf4j.impl;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MarkerIgnoringBase;
/*     */ import org.slf4j.helpers.MessageFormatter;
/*     */ import org.slf4j.helpers.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleLogger
/*     */   extends MarkerIgnoringBase
/*     */ {
/*     */   private static final long serialVersionUID = -632788891211436180L;
/*     */   private static final String CONFIGURATION_FILE = "simplelogger.properties";
/* 124 */   private static long START_TIME = System.currentTimeMillis();
/* 125 */   private static final Properties SIMPLE_LOGGER_PROPS = new Properties();
/*     */   
/*     */   private static final int LOG_LEVEL_TRACE = 0;
/*     */   
/*     */   private static final int LOG_LEVEL_DEBUG = 10;
/*     */   
/*     */   private static final int LOG_LEVEL_INFO = 20;
/*     */   
/*     */   private static final int LOG_LEVEL_WARN = 30;
/*     */   
/*     */   private static final int LOG_LEVEL_ERROR = 40;
/*     */   private static final int LOG_LEVEL_OFF = 50;
/*     */   private static boolean INITIALIZED = false;
/* 138 */   private static int DEFAULT_LOG_LEVEL = 20;
/*     */   private static boolean SHOW_DATE_TIME = false;
/* 140 */   private static String DATE_TIME_FORMAT_STR = null;
/* 141 */   private static DateFormat DATE_FORMATTER = null;
/*     */   private static boolean SHOW_THREAD_NAME = true;
/*     */   private static boolean SHOW_LOG_NAME = true;
/*     */   private static boolean SHOW_SHORT_LOG_NAME = false;
/* 145 */   private static String LOG_FILE = "System.err";
/* 146 */   private static PrintStream TARGET_STREAM = null;
/*     */   private static boolean LEVEL_IN_BRACKETS = false;
/* 148 */   private static String WARN_LEVEL_STRING = "WARN";
/*     */   
/*     */   public static final String SYSTEM_PREFIX = "org.slf4j.simpleLogger.";
/*     */   
/*     */   public static final String DEFAULT_LOG_LEVEL_KEY = "org.slf4j.simpleLogger.defaultLogLevel";
/*     */   
/*     */   public static final String SHOW_DATE_TIME_KEY = "org.slf4j.simpleLogger.showDateTime";
/*     */   
/*     */   public static final String DATE_TIME_FORMAT_KEY = "org.slf4j.simpleLogger.dateTimeFormat";
/*     */   public static final String SHOW_THREAD_NAME_KEY = "org.slf4j.simpleLogger.showThreadName";
/*     */   public static final String SHOW_LOG_NAME_KEY = "org.slf4j.simpleLogger.showLogName";
/*     */   public static final String SHOW_SHORT_LOG_NAME_KEY = "org.slf4j.simpleLogger.showShortLogName";
/*     */   public static final String LOG_FILE_KEY = "org.slf4j.simpleLogger.logFile";
/*     */   public static final String LEVEL_IN_BRACKETS_KEY = "org.slf4j.simpleLogger.levelInBrackets";
/*     */   public static final String WARN_LEVEL_STRING_KEY = "org.slf4j.simpleLogger.warnLevelString";
/*     */   public static final String LOG_KEY_PREFIX = "org.slf4j.simpleLogger.log.";
/*     */   
/*     */   private static String getStringProperty(String name) {
/* 166 */     String prop = null;
/*     */     try {
/* 168 */       prop = System.getProperty(name);
/* 169 */     } catch (SecurityException e) {}
/*     */ 
/*     */     
/* 172 */     return (prop == null) ? SIMPLE_LOGGER_PROPS.getProperty(name) : prop;
/*     */   }
/*     */   
/*     */   private static String getStringProperty(String name, String defaultValue) {
/* 176 */     String prop = getStringProperty(name);
/* 177 */     return (prop == null) ? defaultValue : prop;
/*     */   }
/*     */   
/*     */   private static boolean getBooleanProperty(String name, boolean defaultValue) {
/* 181 */     String prop = getStringProperty(name);
/* 182 */     return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
/*     */   }
/*     */   
/*     */   static void lazyInit() {
/* 186 */     if (INITIALIZED) {
/*     */       return;
/*     */     }
/* 189 */     INITIALIZED = true;
/* 190 */     init();
/*     */   }
/*     */   
/*     */   static void init() {
/* 194 */     loadProperties();
/*     */     
/* 196 */     String defaultLogLevelString = getStringProperty("org.slf4j.simpleLogger.defaultLogLevel", null);
/* 197 */     if (defaultLogLevelString != null) {
/* 198 */       DEFAULT_LOG_LEVEL = stringToLevel(defaultLogLevelString);
/*     */     }
/* 200 */     SHOW_LOG_NAME = getBooleanProperty("org.slf4j.simpleLogger.showLogName", SHOW_LOG_NAME);
/* 201 */     SHOW_SHORT_LOG_NAME = getBooleanProperty("org.slf4j.simpleLogger.showShortLogName", SHOW_SHORT_LOG_NAME);
/* 202 */     SHOW_DATE_TIME = getBooleanProperty("org.slf4j.simpleLogger.showDateTime", SHOW_DATE_TIME);
/* 203 */     SHOW_THREAD_NAME = getBooleanProperty("org.slf4j.simpleLogger.showThreadName", SHOW_THREAD_NAME);
/* 204 */     DATE_TIME_FORMAT_STR = getStringProperty("org.slf4j.simpleLogger.dateTimeFormat", DATE_TIME_FORMAT_STR);
/* 205 */     LEVEL_IN_BRACKETS = getBooleanProperty("org.slf4j.simpleLogger.levelInBrackets", LEVEL_IN_BRACKETS);
/* 206 */     WARN_LEVEL_STRING = getStringProperty("org.slf4j.simpleLogger.warnLevelString", WARN_LEVEL_STRING);
/*     */     
/* 208 */     LOG_FILE = getStringProperty("org.slf4j.simpleLogger.logFile", LOG_FILE);
/* 209 */     TARGET_STREAM = computeTargetStream(LOG_FILE);
/*     */     
/* 211 */     if (DATE_TIME_FORMAT_STR != null) {
/*     */       try {
/* 213 */         DATE_FORMATTER = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
/* 214 */       } catch (IllegalArgumentException e) {
/* 215 */         Util.report("Bad date format in simplelogger.properties; will output relative time", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static PrintStream computeTargetStream(String logFile) {
/* 221 */     if ("System.err".equalsIgnoreCase(logFile))
/* 222 */       return System.err; 
/* 223 */     if ("System.out".equalsIgnoreCase(logFile)) {
/* 224 */       return System.out;
/*     */     }
/*     */     try {
/* 227 */       FileOutputStream fos = new FileOutputStream(logFile);
/* 228 */       PrintStream printStream = new PrintStream(fos);
/* 229 */       return printStream;
/* 230 */     } catch (FileNotFoundException e) {
/* 231 */       Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e);
/* 232 */       return System.err;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadProperties() {
/* 239 */     InputStream in = AccessController.<InputStream>doPrivileged(new PrivilegedAction<InputStream>() {
/*     */           public InputStream run() {
/* 241 */             ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
/* 242 */             if (threadCL != null) {
/* 243 */               return threadCL.getResourceAsStream("simplelogger.properties");
/*     */             }
/* 245 */             return ClassLoader.getSystemResourceAsStream("simplelogger.properties");
/*     */           }
/*     */         });
/*     */     
/* 249 */     if (null != in) {
/*     */       
/* 251 */       try { SIMPLE_LOGGER_PROPS.load(in); }
/* 252 */       catch (IOException e)
/*     */       
/*     */       { 
/*     */         try {
/* 256 */           in.close();
/* 257 */         } catch (IOException iOException) {} } finally { try { in.close(); } catch (IOException e) {} }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 265 */   protected int currentLogLevel = 20;
/*     */   
/* 267 */   private transient String shortLogName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SimpleLogger(String name) {
/* 274 */     this.name = name;
/*     */     
/* 276 */     String levelString = recursivelyComputeLevelString();
/* 277 */     if (levelString != null) {
/* 278 */       this.currentLogLevel = stringToLevel(levelString);
/*     */     } else {
/* 280 */       this.currentLogLevel = DEFAULT_LOG_LEVEL;
/*     */     } 
/*     */   }
/*     */   
/*     */   String recursivelyComputeLevelString() {
/* 285 */     String tempName = this.name;
/* 286 */     String levelString = null;
/* 287 */     int indexOfLastDot = tempName.length();
/* 288 */     while (levelString == null && indexOfLastDot > -1) {
/* 289 */       tempName = tempName.substring(0, indexOfLastDot);
/* 290 */       levelString = getStringProperty("org.slf4j.simpleLogger.log." + tempName, null);
/* 291 */       indexOfLastDot = String.valueOf(tempName).lastIndexOf(".");
/*     */     } 
/* 293 */     return levelString;
/*     */   }
/*     */   
/*     */   private static int stringToLevel(String levelStr) {
/* 297 */     if ("trace".equalsIgnoreCase(levelStr))
/* 298 */       return 0; 
/* 299 */     if ("debug".equalsIgnoreCase(levelStr))
/* 300 */       return 10; 
/* 301 */     if ("info".equalsIgnoreCase(levelStr))
/* 302 */       return 20; 
/* 303 */     if ("warn".equalsIgnoreCase(levelStr))
/* 304 */       return 30; 
/* 305 */     if ("error".equalsIgnoreCase(levelStr))
/* 306 */       return 40; 
/* 307 */     if ("off".equalsIgnoreCase(levelStr)) {
/* 308 */       return 50;
/*     */     }
/*     */     
/* 311 */     return 20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void log(int level, String message, Throwable t) {
/* 323 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/*     */     
/* 327 */     StringBuilder buf = new StringBuilder(32);
/*     */ 
/*     */     
/* 330 */     if (SHOW_DATE_TIME) {
/* 331 */       if (DATE_FORMATTER != null) {
/* 332 */         buf.append(getFormattedDate());
/* 333 */         buf.append(' ');
/*     */       } else {
/* 335 */         buf.append(System.currentTimeMillis() - START_TIME);
/* 336 */         buf.append(' ');
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 341 */     if (SHOW_THREAD_NAME) {
/* 342 */       buf.append('[');
/* 343 */       buf.append(Thread.currentThread().getName());
/* 344 */       buf.append("] ");
/*     */     } 
/*     */     
/* 347 */     if (LEVEL_IN_BRACKETS) {
/* 348 */       buf.append('[');
/*     */     }
/*     */     
/* 351 */     switch (level) {
/*     */       case 0:
/* 353 */         buf.append("TRACE");
/*     */         break;
/*     */       case 10:
/* 356 */         buf.append("DEBUG");
/*     */         break;
/*     */       case 20:
/* 359 */         buf.append("INFO");
/*     */         break;
/*     */       case 30:
/* 362 */         buf.append(WARN_LEVEL_STRING);
/*     */         break;
/*     */       case 40:
/* 365 */         buf.append("ERROR");
/*     */         break;
/*     */     } 
/* 368 */     if (LEVEL_IN_BRACKETS)
/* 369 */       buf.append(']'); 
/* 370 */     buf.append(' ');
/*     */ 
/*     */     
/* 373 */     if (SHOW_SHORT_LOG_NAME) {
/* 374 */       if (this.shortLogName == null)
/* 375 */         this.shortLogName = computeShortName(); 
/* 376 */       buf.append(String.valueOf(this.shortLogName)).append(" - ");
/* 377 */     } else if (SHOW_LOG_NAME) {
/* 378 */       buf.append(String.valueOf(this.name)).append(" - ");
/*     */     } 
/*     */ 
/*     */     
/* 382 */     buf.append(message);
/*     */     
/* 384 */     write(buf, t);
/*     */   }
/*     */ 
/*     */   
/*     */   void write(StringBuilder buf, Throwable t) {
/* 389 */     TARGET_STREAM.println(buf.toString());
/* 390 */     if (t != null) {
/* 391 */       t.printStackTrace(TARGET_STREAM);
/*     */     }
/* 393 */     TARGET_STREAM.flush();
/*     */   }
/*     */   private String getFormattedDate() {
/*     */     String dateText;
/* 397 */     Date now = new Date();
/*     */     
/* 399 */     synchronized (DATE_FORMATTER) {
/* 400 */       dateText = DATE_FORMATTER.format(now);
/*     */     } 
/* 402 */     return dateText;
/*     */   }
/*     */   
/*     */   private String computeShortName() {
/* 406 */     return this.name.substring(this.name.lastIndexOf(".") + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void formatAndLog(int level, String format, Object arg1, Object arg2) {
/* 418 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/* 421 */     FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
/* 422 */     log(level, tp.getMessage(), tp.getThrowable());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void formatAndLog(int level, String format, Object... arguments) {
/* 433 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/* 436 */     FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
/* 437 */     log(level, tp.getMessage(), tp.getThrowable());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isLevelEnabled(int logLevel) {
/* 448 */     return (logLevel >= this.currentLogLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/* 453 */     return isLevelEnabled(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String msg) {
/* 461 */     log(0, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object param1) {
/* 469 */     formatAndLog(0, format, param1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object param1, Object param2) {
/* 477 */     formatAndLog(0, format, param1, param2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object... argArray) {
/* 485 */     formatAndLog(0, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void trace(String msg, Throwable t) {
/* 490 */     log(0, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 495 */     return isLevelEnabled(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String msg) {
/* 503 */     log(10, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object param1) {
/* 511 */     formatAndLog(10, format, param1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object param1, Object param2) {
/* 519 */     formatAndLog(10, format, param1, param2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object... argArray) {
/* 527 */     formatAndLog(10, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void debug(String msg, Throwable t) {
/* 532 */     log(10, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 537 */     return isLevelEnabled(20);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String msg) {
/* 545 */     log(20, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object arg) {
/* 553 */     formatAndLog(20, format, arg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 561 */     formatAndLog(20, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object... argArray) {
/* 569 */     formatAndLog(20, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void info(String msg, Throwable t) {
/* 574 */     log(20, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 579 */     return isLevelEnabled(30);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String msg) {
/* 587 */     log(30, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object arg) {
/* 595 */     formatAndLog(30, format, arg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 603 */     formatAndLog(30, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object... argArray) {
/* 611 */     formatAndLog(30, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void warn(String msg, Throwable t) {
/* 616 */     log(30, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 621 */     return isLevelEnabled(40);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/* 629 */     log(40, msg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object arg) {
/* 637 */     formatAndLog(40, format, arg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 645 */     formatAndLog(40, format, arg1, arg2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object... argArray) {
/* 653 */     formatAndLog(40, format, argArray);
/*     */   }
/*     */ 
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 658 */     log(40, msg, t);
/*     */   }
/*     */   
/*     */   public void log(LoggingEvent event) {
/* 662 */     int levelInt = event.getLevel().toInt();
/*     */     
/* 664 */     if (!isLevelEnabled(levelInt)) {
/*     */       return;
/*     */     }
/* 667 */     FormattingTuple tp = MessageFormatter.arrayFormat(event.getMessage(), event.getArgumentArray(), event.getThrowable());
/* 668 */     log(levelInt, tp.getMessage(), event.getThrowable());
/*     */   }
/*     */ }


/* Location:              /Users/vladislav/VimeNetwork.jar!/org/slf4j/impl/SimpleLogger.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */