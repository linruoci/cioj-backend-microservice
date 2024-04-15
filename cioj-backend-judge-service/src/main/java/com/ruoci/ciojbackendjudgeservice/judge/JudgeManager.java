package com.ruoci.ciojbackendjudgeservice.judge;

import com.ruoci.ciojbackendjudgeservice.judge.strategy.DefaultJudgeStrategyImpl;
import com.ruoci.ciojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategyImpl;
import com.ruoci.ciojbackendjudgeservice.judge.strategy.JudgeContext;
import com.ruoci.ciojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.ruoci.ciojbackendmodel.codesandbox.JudgeInfo;
import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @Author: ruoci
 **/
@Service
public class JudgeManager {


    public JudgeInfo doJudge(JudgeContext judgeContext){

        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();

        JudgeStrategy judgeStrategy = new DefaultJudgeStrategyImpl();

        if ("java".equals(language)){
            judgeStrategy =  new JavaLanguageJudgeStrategyImpl();
        }

        return judgeStrategy.doJudge(judgeContext);
    }

}
