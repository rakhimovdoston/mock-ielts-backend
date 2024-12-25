package com.search.teacher.service;

import com.search.teacher.dto.modules.RMultipleChoiceDto;
import com.search.teacher.exception.BadRequestException;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public static String replaceQuestionList(String content, int answerStart) {
        Document document = Jsoup.parse(content);
        Element imageElement = document.select("img").first();
        if (imageElement != null) {
            imageElement.attr("width", "600px").attr("height", "450px").attr("alt", "Listening Images");
        }
        Elements olElements = document.select("ol");
        Element olElement = olElements.last();
        if (olElement == null) return content;
        if (olElement.childNodeSize() + answerStart > 10) {
            throw new BadRequestException("Each Listening section should not have more than 10 questions. Please reduce your questions.");
        }
        olElement.attr("start", String.valueOf(answerStart + 1));
        olElement.attr("style", "list-style-type: decimal; list-style-position: inside;");

        Elements elements = document.select("ul");
        Map<String, String> map = new HashMap<>();
        Element element = elements.first();
        if (element != null) {
            Element newOlElement = new Element("ol");
            newOlElement.attr("type", "A").attr("start", "1");
            newOlElement.attr("style", "list-style-type: upper-alpha;list-style-position: inside;");
            newOlElement.insertChildren(0, element.children());
            int i = 0;
            for (var el : newOlElement.getElementsByTag("li")) {
                char value = (char) ('A' + i);
                map.put(String.valueOf(value), el.text());
                i++;
            }
            element.replaceWith(newOlElement);
        }

        if (!map.isEmpty()) {
            for (var el : olElement.children()) {
                Element span = new Element("span");
                span.text(el.text());
                el.empty();
                Element select = new Element("select");
                answerStart++;
                select.attr("id", "ques-" + answerStart).attr("class", "question-type-select");
                select.attr("style", """
                    width: 100px;
                    border:1px solid gray;
                    border-radius:20px;
                    padding:4px 8px;
                    outline:none;
                    margin-left: 14px;
                    """);
                select.appendChild(new Element("option").attr("value", ""));
                for (var mapEntry : map.keySet()) {
                    select.appendChild(new Element("option").attr("value", map.get(mapEntry)).text(String.valueOf(mapEntry)));
                }
                el.appendChild(span);
                el.appendChild(select);
            }
        } else {
            for (var el : olElement.children()) {
                answerStart++;
                Element span = new Element("span");
                span.text(el.text());
                el.empty();
                Element input = new Element("input");
                input.attr("type", "text").attr("value", "").attr("class", "reading-question-inputs").attr("tabIndex", String.valueOf(answerStart)).attr("id", "ques-" + answerStart).attr("placeholder", String.valueOf(answerStart));

                input.attr("style", """
                    display: inline-block;
                    width:150px;
                    text-align:center;
                    border:1px solid gray;
                    border-radius:20px;
                    padding:4px 8px;
                    outline:none;
                    margin-left: 14px;
                    vertical-align: center
                    """);
                el.appendChild(span);
                el.appendChild(input);
            }
        }
        return document.body().html();
    }

    public static boolean checkListeningQuestion(String content, int startQuestion) {
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByClass("reading-question-inputs");
        return elements.size() + startQuestion > 10;
    }

    public String setOrderForHtmlContent(int startQuestion, String content) {
        Document doc = Jsoup.parse(content);
        final int listStart = startQuestion;
        Elements olElements = doc.select("ol");
        for (var el : olElements) {
            if (!el.attr("type").equals("A")) {
                Elements select = el.getElementsByClass("question-type-select");
                Elements inputs = el.getElementsByClass("reading-question-inputs");
                if (!select.isEmpty()) {
                    el.attr("start", listStart + "");
                    Elements selectElements = el.getElementsByClass("question-type-select");
                    for (var selectElement : selectElements) {
                        selectElement.attr("id", "ques-" + startQuestion);
                        startQuestion++;
                    }
                    break;
                }
                if (!inputs.isEmpty()) {
                    el.attr("start", listStart + "");
                    Elements elements = el.getElementsByClass("reading-question-inputs");
                    for (Element element : elements) {
                        element.attr("placeholder", startQuestion + "");
                        element.attr("id", "ques-" + startQuestion);
                        element.attr("tabindex", startQuestion + "");
                        startQuestion++;
                    }
                }
            }
        }
        startQuestion = listStart;
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

        Elements selectElements = document.getElementsByClass("question-type-select");
        for (Element element : selectElements) {
            int id = Integer.parseInt(element.attr("id").substring(5));
            max = Math.max(max, id);
        }
        return max;
    }

    public static String questionCountString(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByClass("reading-question-inputs");
        int max = Utils.MIN_VALUE, min = Utils.MAX_VALUE;
        if (elements.isEmpty()) {
            elements = document.getElementsByClass("question-type-select");
            for (Element element : elements) {
                String id = element.attr("id");
                int idValue = Integer.parseInt(id.substring(5));
                max = Math.max(max, idValue);
                min = Math.min(min, idValue);
            }
            return min + "-" + max;
        }
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
        if (elements.isEmpty()) {
            elements = document.getElementsByClass("question-type-select");
            for (Element element : elements) {
                String id = element.attr("id");
                int idValue = Integer.parseInt(id.substring(5));
                min = Math.min(min, idValue);
            }
            return min;
        }
        for (Element element : elements) {
            var placeHolder = Integer.parseInt(element.attr("placeholder"));
            min = Math.min(min, placeHolder);
        }
        return min;

    }

}
