package com.ruoci.ciojbackendjudgeservice.judge.codesandbox.impl;


import com.ruoci.ciojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * @Author: ruoci
 **/
public class ThirdPartCodeSandBox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("thirdPart");
        return null;
    }
}
