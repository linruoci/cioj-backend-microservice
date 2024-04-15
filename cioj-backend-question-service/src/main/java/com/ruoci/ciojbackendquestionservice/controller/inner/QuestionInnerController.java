package com.ruoci.ciojbackendquestionservice.controller.inner;

import com.ruoci.ciojbackendmodel.entity.Question;
import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;
import com.ruoci.ciojbackendquestionservice.service.QuestionService;
import com.ruoci.ciojbackendquestionservice.service.QuestionSubmitService;
import com.ruoci.ciojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: ruoci
 **/

@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {


    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;


    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId){
        return questionService.getById(questionId);
    }


    @GetMapping("question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId){
        return questionSubmitService.getById(questionSubmitId);
    }


    @PostMapping("question_submit/update")
    @Override
    public boolean updateById(@RequestBody QuestionSubmit questionSubmit){
        return questionSubmitService.updateById(questionSubmit);
    }


    @PostMapping("/question/update")
    @Override
    public boolean updateQuestionById(@RequestBody Question question){
        return questionService.updateById(question);
    }


}
