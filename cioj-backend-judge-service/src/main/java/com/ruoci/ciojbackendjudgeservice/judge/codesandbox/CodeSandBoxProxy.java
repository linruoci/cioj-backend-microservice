package com.ruoci.ciojbackendjudgeservice.judge.codesandbox;

import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: ruoci
 **/
@Slf4j
@AllArgsConstructor
public class CodeSandBoxProxy implements CodeSandbox{

    private CodeSandbox codeSandbox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("请求信息:" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("响应信息: " + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
