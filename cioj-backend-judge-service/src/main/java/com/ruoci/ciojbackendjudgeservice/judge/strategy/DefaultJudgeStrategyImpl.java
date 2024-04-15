package com.ruoci.ciojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.ruoci.ciojbackendmodel.codesandbox.JudgeInfo;
import com.ruoci.ciojbackendmodel.dto.question.JudgeCase;
import com.ruoci.ciojbackendmodel.dto.question.JudgeConfig;
import com.ruoci.ciojbackendmodel.entity.Question;
import com.ruoci.ciojbackendmodel.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * @Author: ruoci
 **/
public class DefaultJudgeStrategyImpl implements JudgeStrategy {

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> outputList = judgeContext.getOutputList();
        List<String> inputList = judgeContext.getInputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;

//        用户代码执行时间
        Long actualMemory = judgeInfo.getMemory();
        Long actualTime = judgeInfo.getTime();

//        返回结果
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setTime(actualTime);
        judgeInfoResponse.setMemory(actualMemory);

        if (inputList.size() != outputList.size()){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        for (int i = 0; i < judgeCaseList.size(); i++){
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))){
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }

        }

//        判断题目限制.

        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);

        Long expectedMemory = judgeConfig.getMemoryLimit();
        Long expectedTime = judgeConfig.getTimeLimit();

        if (actualMemory > expectedMemory){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (actualTime > expectedTime){
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;

    }
}
