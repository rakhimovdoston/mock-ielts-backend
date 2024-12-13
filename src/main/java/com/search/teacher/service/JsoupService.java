package com.search.teacher.service;

import com.search.teacher.dto.modules.RMultipleChoiceDto;
import com.search.teacher.model.entities.modules.reading.ReadingQuestion;
import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JsoupService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String replaceInstruction(String instruction, String count) {

        Document document = Jsoup.parse(instruction);
        Elements elements = document.select("b");
        Element element = elements.last();

        if (element != null) {
            element.replaceWith(new Element("b").text(count));
            return document.body().html();
        }
        return instruction;
    }

    public static String replaceInstruction(String instructor, List<RMultipleChoiceDto> choices) {
        Document document = Jsoup.parse(instructor);
        var countOption = choices.get(0).getAnswers().size();
        Elements elements = document.select("b");
        Element element = elements.first();
        if (element != null) {
            element.replaceWith(new Element("b").text(getAlphabetByCount(countOption)));
            return document.body().html();
        }
        return instructor;
    }

    private static String getAlphabetByCount(int countOption) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < countOption; i++) {
            if (i == countOption - 1) {
                text.append(" or ").append((char) ('A' + i));
                continue;
            }
            text.append((char) ('A' + i));
            if (i != countOption - 2) {
                text.append(", ");
            }
        }
        return text.toString();
    }

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
