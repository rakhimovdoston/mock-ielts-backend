package com.search.teacher.Techlearner.service.question;

import com.search.teacher.Techlearner.dto.question.AnswerDto;
import com.search.teacher.Techlearner.dto.question.QuestionDto;
import com.search.teacher.Techlearner.dto.question.QuestionSearchFilter;
import com.search.teacher.Techlearner.model.enums.Difficulty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionDBService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<QuestionDto> getQuestionsByFilter(QuestionSearchFilter questionSearchFilter, boolean isPageable) {
        String query = """
               select 
                   q.id as question_id, 
                   q.name as question_name, 
                   q.difficulty as question_difficulty,
                   ans.name as answer_name, 
                   ans.id as answer_id from questions q 
               left join answers ans on q.id = ans.question_id
               where q.active is true 
               """ + filter(questionSearchFilter, isPageable) + ";";
        return jdbcTemplate.query(query, new ResultSetExtractor<List<QuestionDto>>() {

            @Override
            public List<QuestionDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Long, QuestionDto> questionList = new HashMap<>();
                while (rs.next()) {
                    Long questionId = rs.getLong("question_id");
                    QuestionDto question = questionList.get(questionId);
                    if (question == null) {
                        question = new QuestionDto();
                        question.setId(questionId);
                        question.setDifficulty(Difficulty.getValue(rs.getString("question_difficulty")));
                        question.setName(rs.getString("question_name"));
                        question.setAnswers(new ArrayList<>());
                        questionList.put(questionId, question);
                    }

                    Long answerId = rs.getLong("answer_id");
                    if (answerId != 0) {
                        AnswerDto answer = new AnswerDto();
                        answer.setId(answerId);
                        answer.setName(rs.getString("answer_name"));
                        question.getAnswers().add(answer);
                    }
                }
                return new ArrayList<>(questionList.values());
            }
        });
    }

    private String filter(QuestionSearchFilter questionSearchFilter, boolean isPageable) {
        StringBuilder builder = new StringBuilder();
        if (questionSearchFilter.getType() != null) {
            builder.append(" and ").append(" q.type=").append(questionSearchFilter.getType());
        }

        if (isPageable) {
            builder
                    .append(" OFFSET ")
                    .append(questionSearchFilter.getPage())
                    .append(" ROW FETCH FIRST ")
                    .append(4 * questionSearchFilter.getSize())
                    .append(" ROW ONLY ");
        }
        return builder.toString();
    }

    public Integer getCountByFilter(QuestionSearchFilter questionSearchFilter, boolean isPageable) {
        String query = """
                select count(id) from questions where active is true
                """ + filter(questionSearchFilter, isPageable);
        return jdbcTemplate.queryForObject(query, Integer.class);
    }
}
