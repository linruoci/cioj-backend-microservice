package com.ruoci.ciojbackendjudgeservice.controller.inner;

import com.ruoci.ciojbackendjudgeservice.judge.JudgeService;
import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;
import com.ruoci.ciojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: ruoci
 **/

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {


    @Resource
    private JudgeService judgeService;

    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId){
        return judgeService.doJudge(questionSubmitId);
    }


}
