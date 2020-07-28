package com.HTMLFetch;

import com.config.Config;
import com.entities.VasItemNews;
import com.entities.VasItemNewsType;
import com.logger.Logger;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PLDSFetch {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static List<VasItemNews> getListNew2(int typeInt, int page, PLDSCompletion completion) {
        List<VasItemNews> listNew = new ArrayList<>();
        VasItemNewsType newType = VasItemNewsType.getArticleTypeById(typeInt);
        try {
            String apiLink = Config.BASE_PLDS_URL + newType.url + "/page/" + String.valueOf(page);
            return getData(typeInt, apiLink, completion);
        } catch (Exception e) {
            Logger.error(e);
        }
        return listNew;
    }

    public static List<VasItemNews> resizeImageInDetail(List<VasItemNews> listArticle) {
        Document doc = null;
        List<VasItemNews> newListArticle = new ArrayList<>();
        for (int i = 0; i < listArticle.size(); i++) {
            VasItemNews newArticle = listArticle.get(i);
            doc = Jsoup.parseBodyFragment(newArticle.detail);
            Elements imgElements = doc.select("img").attr("width", "500");
            for (Element imgElement: imgElements) {
                imgElement.attr("width", "100%");
            }
            newArticle.detail = doc.toString();
            newListArticle.add(newArticle);
        }
        return newListArticle;
    }

    private static List<VasItemNews> getData(int typeInt, String apiURL, PLDSCompletion completion) throws Exception {
        Document doc = Jsoup.connect(apiURL).get();

        List<VasItemNews> listNews = new ArrayList<>();

        Elements a = doc.getElementsByClass("module module-cate pkg delta fl-right");
        if (a.size() > 0) {
            Elements listTitle = a.get(0).getElementsByClass("title");
            Elements listInfoPkg = a.get(0).getElementsByClass("info pkg");
            for (int i = 0; i < listTitle.size() || i < listInfoPkg.size(); i++) {
                VasItemNews news = new VasItemNews();
                news.type = typeInt;

                String title = listTitle.get(i).text();
                news.title = title;

                String logo = listInfoPkg.get(i).select("div.info.pkg > a.img > img").attr("src");
                String logoDownloadLink = logo.replace("thumb_x190x125", "");
                news.logo = "/" + logoDownloadLink.replace(Config.BASE_IMAGE_PLDS_URL, "");


                String shortDesc = listInfoPkg.get(i).select("div.info.pkg > p.desc").text();
                news.shortDesc = shortDesc;

                String timeString = listInfoPkg.get(i).select("div.info.pkg > p.meta").text();
                DateFormat dateFormat = new SimpleDateFormat(Config.PLDS_DATE_FORMAT);
                Date publishDate = dateFormat.parse(timeString);
                news.create_date = publishDate;

                String href = listTitle.get(i).select("h4.title > a").attr("href");
                news.url = href;
                doc = Jsoup.connect(href).get();

                Elements detailEs = doc.select("article.detail");
                if (detailEs.size() == 0) continue;
                detailEs.first().select("nav.breakcrumb.pkg").remove();
                Element detailE = detailEs.first().getElementById("main-detail");
                String detail = detailE.getElementsByAttributeValue("itemprop", "articleBody").toString();
                news.detail = detail;

//                System.out.println(logoDownloadLink);
//                System.out.println(news.logo);

                completion.downloadFunc(logoDownloadLink, news.logo);

                listNews.add(news);
            }
        }

        return listNews;
    }


    public interface PLDSCompletion {
        boolean downloadFunc(String urlPicture, String fileName) throws MalformedURLException;
    }
}
