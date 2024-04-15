package com.ruoci.ciojbackendjudgeservice.judge;


import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;

/**
 * @Author: ruoci
 **/
public interface JudgeService {


    QuestionSubmit doJudge(long questionSubmitId);

}
