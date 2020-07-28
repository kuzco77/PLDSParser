package com.config;

import com.entities.VasItemNewsType;
import com.facade.DBConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Config {

    public static boolean isDevelopOnServer = true;

    public static String BASE_PLDS_URL = "https://www.doisongphapluat.com/";
    public static String BASE_IMAGE_PLDS_URL = "https://media.doisongphapluat.com/";
    /*VNMedia specific format*/
    public static String PLDS_DATE_FORMAT = "HH:mmaa ,dd/MM/yyyy";
    public static String VNMEDIA_API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S HH:mm";

    /*file config*/
    public static String LOCAL_IMAGE_STORE_PATH = "/Users/ghddev/Downloads/logo";
    public static String SERVER_IMAGE_STORE_PATH = "/app/website/hayxem.icp/public/images";
    public static String IMAGE_STORE_PATH = "/app/website/hayxem.icp/public/images";

    /*app config*/
    public static String LOCAL_LOG_PATH = "/Users/ghddev/Downloads/logs";
    public static String SERVER_LOG_PATH = "/app/crawl/doi_song_phap_luat/logs";
    public static String LOG_PATH = "/app/crawl/doi_song_phap_luat/logs";

    /*mysql config*/
    public static String DB_IP = "125.212.226.145";
    public static String DB_PORT = "3306";
    public static String DB_NAME = "phap_luat_doi_song";
    public static String DB_USER = "ghdc";
    public static String DB_PWD = "ghdc@123";
    public static int DB_MAX_CONNECTION = 100;
    public static int DB_MIN_CONNECTION = 2;
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String CONNECTION_URL = "jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;


    static {
        if (isDevelopOnServer) {
            IMAGE_STORE_PATH = SERVER_IMAGE_STORE_PATH;
            LOG_PATH = SERVER_LOG_PATH;
        } else {
            IMAGE_STORE_PATH = LOCAL_IMAGE_STORE_PATH;
            LOG_PATH = LOCAL_LOG_PATH;
        }
    }

}
