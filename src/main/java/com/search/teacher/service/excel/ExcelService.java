package com.search.teacher.service.excel;

import com.search.teacher.model.entities.Answer;
import com.search.teacher.model.entities.Question;
import com.search.teacher.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private static final int BATCH_SIZE = 1000;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<Question> getAllQuestions(MultipartFile questionFile, User currentUser) {
        List<Question> questions = new ArrayList<>();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(questionFile.getInputStream());
            XSSFSheet workSheet = workbook.getSheetAt(0);
            for (int i=1; i < workSheet.getPhysicalNumberOfRows(); i++) {
                Question question = new Question();
                XSSFRow row = workSheet.getRow(1);
                question.setName(row.getCell(0).getStringCellValue());
                question.setUserId(currentUser.getId());
                List<Answer> answers = getAnswerFromRow(row, question);
                question.setAnswers(answers);
                questions.add(question);
            }
        } catch (IOException e) {
            logger.error("Reading Excel file error: ");
            e.printStackTrace();
        }
        return questions;
    }

    private List<Answer> getAnswerFromRow(XSSFRow row, Question question) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 1; i < row.getPhysicalNumberOfCells(); i++) {
            Answer answer = new Answer();
            answer.setName(row.getCell(i).getStringCellValue());
            answer.setCorrect(i == 1);
            answer.setQuestion(question);
            answers.add(answer);
        }
        Collections.shuffle(answers);
        return answers;
    }
}
