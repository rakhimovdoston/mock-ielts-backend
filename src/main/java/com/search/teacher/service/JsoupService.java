package com.search.teacher.service;

import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JsoupService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String setOrderForHtmlContent(int startQuestion, String content) {
        Document doc = Jsoup.parse(content);
        Elements elements = doc.getElementsByClass("reading-question-inputs");
        for (Element element : elements) {
            element.attr("placeholder", startQuestion + "");
            element.attr("id", "ques-" + startQuestion);
            element.attr("tabindex", startQuestion + "");
            startQuestion++;
        }
        return doc.body().html();
    }

    public static int getLastQuestionNumberFromHtml(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByClass("reading-question-inputs");
        int max = Utils.MIN_VALUE;
        for (Element element : elements) {
            var placeHolder = Integer.parseInt(element.attr("placeholder"));
            max = Math.max(max, placeHolder);
        }
        return max;
    }

    public static String questionCountString(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByClass("reading-question-inputs");
        int max = Utils.MIN_VALUE, min = Utils.MAX_VALUE;
        for (Element element : elements) {
            var placeHolder = Integer.parseInt(element.attr("placeholder"));
            max = Math.max(max, placeHolder);
            min = Math.min(min, placeHolder);
        }
        return min + "-" + max;
    }


    public static int getStartQuestionNumber(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByClass("reading-question-inputs");
        int min = Utils.MAX_VALUE;
        for (Element element : elements) {
            var placeHolder = Integer.parseInt(element.attr("placeholder"));
            min = Math.min(min, placeHolder);
        }
        return min;

    }

}
