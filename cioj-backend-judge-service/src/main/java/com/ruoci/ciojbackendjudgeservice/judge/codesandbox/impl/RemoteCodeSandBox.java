package com.ruoci.ciojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ruoci.ciojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeResponse;
import com.ruoci.ciojbackendmodel.common.ErrorCode;
import com.ruoci.ciojbackendmodel.exception.BusinessException;

/**
 * @Author: ruoci
 **/
public class RemoteCodeSandBox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        String url = "http://localhost:8082/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .body(json)
                .execute()
                .body();

        if (StrUtil.isBlank(responseStr)){
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = "+ responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);


    }
}
