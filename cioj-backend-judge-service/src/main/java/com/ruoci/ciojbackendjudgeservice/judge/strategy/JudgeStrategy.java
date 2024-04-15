package com.ruoci.ciojbackendjudgeservice.judge.strategy;

import com.ruoci.ciojbackendmodel.codesandbox.JudgeInfo;

/**
 * @Author: ruoci
 * 判题策略
 **/
public interface JudgeStrategy {


    JudgeInfo doJudge(JudgeContext judgeContext);

}
