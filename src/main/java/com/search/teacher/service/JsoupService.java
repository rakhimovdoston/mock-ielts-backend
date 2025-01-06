package com.search.teacher.service;

import com.search.teacher.dto.modules.RMultipleChoiceDto;
import com.search.teacher.dto.modules.listening.MultipleQuestionSecondDto;
import com.search.teacher.exception.BadRequestException;
import com.search.teacher.model.entities.modules.reading.Form;
import com.search.teacher.model.entities.modules.reading.ReadingPassage;
import com.search.teacher.model.entities.modules.reading.ReadingQuestionTypes;
import com.search.teacher.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

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
                el.appendChild(spanElement(el));
                answerStart++;
                el.appendChild(selectElementWithKeyValue(map, answerStart));
            }
        } else {
            for (var el : olElement.children()) {
                el.appendChild(spanElement(el));
                answerStart++;
                el.appendChild(defaultInputElement(answerStart));
            }
        }
        return document.body().html();
    }

    public static boolean checkListeningQuestion(String content, int startQuestion) {
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByClass("reading-question-inputs");
        return elements.size() + startQuestion > 10;
    }

    private static String substringFromString(String str) {
        String text = str.substring(0, 50);
        char[] chars = text.toCharArray();
        return chars[chars.length - 1] == ' ' ? text.substring(0, 49) : text + "...";
    }

    public static String getTitleFromCondition(String condition) {
        Document document = Jsoup.parse(condition);
        return substringFromString(document.body().text());
    }

    public static MultipleQuestionSecondDto replaceMultipleChoice(String content, int startQuestion) {
        Document document = Jsoup.parse(content);
        Elements elements = document.select("ul");
        Element element = elements.first();
        if (element == null)
            throw new BadRequestException("Please enter correct format for Multiple choices questions");

        MultipleQuestionSecondDto multipleQuestionSecondDto = new MultipleQuestionSecondDto();
        int questionCount = questionCountFromContent(document.body());
        multipleQuestionSecondDto.setQuestionCount((startQuestion + 1) + "-" + (questionCount + startQuestion));

        Elements liElements = element.select("li");
        int order = 1;
        List<Form> forms = new ArrayList<>();
        for (Element liElement : liElements) {
            forms.add(new Form(liElement.text(), order));
            order++;
        }
        element.empty();
        multipleQuestionSecondDto.setForms(forms);
        multipleQuestionSecondDto.setConditions(document.body().html());

        return multipleQuestionSecondDto;
    }

    private static int questionCountFromContent(Element body) {
        Map<String, Integer> maps = new HashMap<>();
        maps.put("ONE", 1);
        maps.put("TWO", 2);
        maps.put("THREE", 3);
        maps.put("FOUR", 4);
        maps.put("FIVE", 5);
        for (var element : body.getAllElements()) {
            for (var entry : maps.keySet()) {
                if (element.text().equalsIgnoreCase(entry)) {
                    return maps.get(entry);
                }
            }
        }
        return 2;
    }

    public static MultipleQuestionSecondDto replaceContent(String content, int startQuestion, ReadingQuestionTypes type, ReadingPassage passage) {
        Document doc = Jsoup.parse(content);
        int countPassage = countHeadingFromPassage(passage);
        return switch (type) {
            case LOCATING_INFORMATION -> locationInformation(doc, countPassage, startQuestion);
            case YES_NO_NOT_GIVEN, TRUE_FALSE_NOT_GIVEN -> trueFalseYesNo(doc, type, startQuestion);
            case MULTIPLE_CHOICES_QUESTION_SECONDS -> replaceMultipleChoice(content, startQuestion);
            case MATCHING_FEATURES, MATCHING_SENTENCE_ENDINGS -> matchingForReading(doc, startQuestion);
            default -> replaceContent(doc, startQuestion);
        };
    }

    private static MultipleQuestionSecondDto trueFalseYesNo(Document doc, ReadingQuestionTypes type, int listStartQuestion) {
        MultipleQuestionSecondDto question = new MultipleQuestionSecondDto();
        Elements elements = doc.select("ol");
        Element element = elements.first();
        int start = listStartQuestion;
        if (element == null)
            throw new BadRequestException("Please correct enter " + (ReadingQuestionTypes.isYesNo(type) ? "True or False" : "Yes or No") + " or Not Given");
        element.attr("style", "list-style-type: decimal;list-style-position: inside;");
        element.attr("start", String.valueOf(listStartQuestion + 1));
        for (var el : element.getElementsByTag("li")) {
            el.appendChild(spanElement(el));
            listStartQuestion++;
            List<String> options = new ArrayList<>();
            if (ReadingQuestionTypes.isYesNo(type)) {
                options.add("Yes");
                options.add("No");
                options.add("Not Given");
            } else {
                options.add("True");
                options.add("False");
                options.add("Not Given");
            }
            el.appendChild(selectElement(options, listStartQuestion));
        }
        question.setConditions(doc.body().html());
        question.setQuestionCount((start + 1) + "-" + (listStartQuestion));
        return question;
    }

    private static MultipleQuestionSecondDto locationInformation(Document doc, int countPassage, int listStartQuestion) {
        if (countPassage == 0)
            throw new BadRequestException("Passage content is not list so you don't use this question type");
        MultipleQuestionSecondDto question = new MultipleQuestionSecondDto();
        final int start = listStartQuestion;
        Elements elements = doc.select("ol");
        Element element = elements.first();
        if (element == null)
            throw new BadRequestException("Please enter questions");

        element.attr("style", "list-style-type: decimal; list-style-position: inside;");
        element.attr("start", String.valueOf(listStartQuestion + 1));
        for (var el : element.getElementsByTag("li")) {
            el.appendChild(spanElement(el));
            listStartQuestion++;
            List<String> options = new ArrayList<>();
            for (int i = 0; i < countPassage; i++) {
                char value = (char) ('A' + i);
                options.add(String.valueOf(value));
            }
            el.appendChild(selectElement(options, listStartQuestion));
        }
        question.setConditions(doc.body().html());
        question.setQuestionCount((start + 1) + "-" + (listStartQuestion));
        return question;
    }

    private static MultipleQuestionSecondDto matchingForReading(Document doc, int listStartQuestion) {
        Elements olElements = doc.select("ol");
        Elements ulElements = doc.select("ul");
        Element olElement = olElements.first();
        Element ulElement = ulElements.first();
        int start = listStartQuestion;
        if (ulElement == null) {
            throw new BadRequestException("Please enter for second statement, For example A. something, ...");
        }
        if (olElement == null) {
            throw new BadRequestException("Please enter the questions");
        }
        Element newOlElement = replaceUlToOlStyleWithAlphabet(ulElement);
        ulElement.replaceWith(newOlElement);
        Map<String, String> maps = new HashMap<>();
        int i = 0;
        for (Element element : newOlElement.getElementsByTag("li")) {
            char value = (char) ('A' + i);
            maps.put(String.valueOf(value), element.text());
            i++;
        }

        olElement.attr("style", "list-style-type: decimal;list-style-position: inside;");
        olElement.attr("start", String.valueOf(listStartQuestion + 1));
        for (var el : olElement.getElementsByTag("li")) {
            el.appendChild(spanElement(el));
            listStartQuestion++;
            el.appendChild(selectElementWithKeyValue(maps, listStartQuestion));
        }
        MultipleQuestionSecondDto question = new MultipleQuestionSecondDto();
        question.setConditions(doc.body().html());
        question.setQuestionCount((start + 1) + "-" + listStartQuestion);
        return question;
    }

    private static Element replaceUlToOlStyleWithAlphabet(Element ulElement) {
        Element newOlElement = new Element("ol");
        newOlElement.attr("type", "A").attr("start", "1");
        newOlElement.attr("style", "list-style-type: upper-alpha;list-style-position: inside;");
        newOlElement.insertChildren(0, ulElement.children());
        return newOlElement;
    }

    private static Element spanElement(Element liElement) {
        Element span = new Element("span");
        span.text(liElement.text());
        liElement.empty();
        return span;
    }

    private static Element selectElementWithKeyValue(Map<String, String> optionValues, int listStartQuestion) {
        Element select = defaultSelectElement(listStartQuestion);
        for (String key : optionValues.keySet()) {
            select.appendChild(new Element("option").attr("value", optionValues.get(key)).text(key));
        }
        return select;
    }

    private static Element selectElement(List<String> optionValues, int listStartQuestion) {
        Element select = defaultSelectElement(listStartQuestion);
        for (String value : optionValues) {
            select.appendChild(new Element("option").attr("value", String.valueOf(value)).text(String.valueOf(value)));
        }
        return select;
    }

    private static Element defaultInputElement(int answerStart) {
        Element input = new Element("input");
        input.attr("type", "text");
        input.attr("value", "");
        input.attr("class", "reading-question-inputs");
        input.attr("tabIndex", String.valueOf(answerStart));
        input.attr("id", "ques-" + answerStart);
        input.attr("placeholder", String.valueOf(answerStart));

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
        return input;
    }

    private static Element defaultSelectElement(int listStartQuestion) {
        Element select = new Element("select");
        select.attr("id", "ques-" + listStartQuestion);
        select.attr("class", "question-type-select");
        select.attr("style", """
            width: 100px;
            border:1px solid gray;
            border-radius:20px;
            padding:4px 8px;
            outline:none;
            margin-left: 14px;
            """);
        select.appendChild(new Element("option").attr("value", ""));
        return select;
    }

    private static int countHeadingFromPassage(ReadingPassage passage) {
        Document document = Jsoup.parse(passage.getContent());
        Elements elements = document.select("ol");
        Element element = elements.first();
        if (element == null)
            return 0;
        return element.getElementsByTag("li").size();
    }

    public static MultipleQuestionSecondDto replaceContent(Document document, int startQuestion) {
        Elements elements = document.getElementsByClass("reading-question-inputs");
        MultipleQuestionSecondDto multipleQuestionSecondDto = new MultipleQuestionSecondDto();
        multipleQuestionSecondDto.setConditions(document.body().html());
        multipleQuestionSecondDto.setQuestionCount((startQuestion + 1) + "-" + (startQuestion + elements.size()));
        return multipleQuestionSecondDto;
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
