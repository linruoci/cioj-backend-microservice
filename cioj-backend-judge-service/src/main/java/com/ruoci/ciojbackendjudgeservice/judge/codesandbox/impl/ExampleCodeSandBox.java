package com.ruoci.ciojbackendjudgeservice.judge.codesandbox.impl;


import com.ruoci.ciojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeResponse;
import com.ruoci.ciojbackendmodel.codesandbox.JudgeInfo;
import com.ruoci.ciojbackendmodel.enums.JudgeInfoMessageEnum;
import com.ruoci.ciojbackendmodel.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @Author: ruoci
 **/
public class ExampleCodeSandBox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();


        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);

        ExecuteCodeResponse executeCodeResponse = ExecuteCodeResponse.builder()
                .outputList(inputList)
                .message("测试执行成功")
                .status(QuestionSubmitStatusEnum.SUCCEED.getValue())
                .judgeInfo(judgeInfo)
                .build();
        return executeCodeResponse;

    }
}
