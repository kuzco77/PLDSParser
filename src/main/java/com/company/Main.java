package com.company;

import com.HTMLFetch.PLDSFetch;
import com.config.Config;
import com.entities.VasItemNews;
import com.entities.VasItemNewsType;
import com.logger.Logger;
import com.model.FileManager;
import com.thread.ResizeImageThread;

import java.net.URL;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        transferArticleFromPLDS2SQL();
    }

    public static void resizeImageWithThread() {
        List<VasItemNewsType> listType = VasItemNewsType.getArticleType();
        int k = 0;
        for (VasItemNewsType type : listType) {
            ResizeImageThread resizeThread = new ResizeImageThread(type, 0, 100000, "Resize Thread No." + k);
            resizeThread.start();
            k++;
        }
    }

    public static void transferArticleFromPLDS2SQL() {
        List<VasItemNewsType> listType = VasItemNewsType.getArticleType();
        for (int i = 4; i < listType.size(); i++) {
            int k = 1;
            int numberOfError = 0;
            while (numberOfError < 3 && k < 15) {
                Logger.log("Page = " + k);
                Logger.log("Searching in category: " + listType.get(i).name + "(" + listType.get(i).id + ")");
                List<VasItemNews> newListNew = PLDSFetch.getListNew(listType.get(i).id, k, (urlPicture, fileName) -> {
                    return FileManager.saveFile(new URL(urlPicture), Config.IMAGE_STORE_PATH + fileName);
                });
                k++;
                Logger.log("Size of batch: " + newListNew.size());

                if (newListNew.size() == 0) {
                    numberOfError++;
                } else if (VasItemNews.checkItemExist(newListNew.get(newListNew.size() - 1))) {
                    VasItemNews.add2SQL(newListNew);
                    numberOfError = 3;
                    Logger.log("===== Duplicate!!! Skip to next category ======");
                } else {
                    VasItemNews.add2SQL(newListNew);
                    numberOfError = 0;
                    Logger.log("===== Successfully add this batch() to DB ======");
                }
            }

        }
    }

}
