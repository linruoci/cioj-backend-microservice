package com.ruoci.ciojbackendjudgeservice.judge.codesandbox;


import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * @Author: ruoci
 **/
public interface CodeSandbox {


    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
