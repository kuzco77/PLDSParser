package com.model;

import com.logger.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileManager {
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    public static boolean downloadFile(String url, String outputPath) {
        try {

            FileUtils.copyURLToFile(
                    new URL(url),
                    new File(outputPath),
                    CONNECTION_TIMEOUT,
                    READ_TIMEOUT
            );
        } catch (IOException e) {
            Logger.error(e);
        }
        return false;
    }

    public static boolean saveFile(URL imgURL, String imgSavePath) {

        boolean isSucceed = true;

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(imgURL.toString());
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.11 Safari/537.36");
        httpGet.addHeader("Referer", "https://www.google.com");

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity imageEntity = httpResponse.getEntity();

            if (imageEntity != null) {
                FileUtils.copyInputStreamToFile(imageEntity.getContent(), new File(imgSavePath));
            }

        } catch (IOException e) {
            isSucceed = false;
        }

        httpGet.releaseConnection();

        return isSucceed;
    }
}
