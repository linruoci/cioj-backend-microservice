package com.ruoci.ciojbackendjudgeservice.judge.strategy;

import com.ruoci.ciojbackendmodel.codesandbox.JudgeInfo;
import com.ruoci.ciojbackendmodel.dto.question.JudgeCase;
import com.ruoci.ciojbackendmodel.entity.Question;
import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @Author: ruoci
 **/
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> outputList;

    private List<String> inputList;

    private Question question;

    private QuestionSubmit questionSubmit;

    private List<JudgeCase> judgeCaseList;


}
