package com.ruoci.ciojbackendserviceclient.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoci.ciojbackendmodel.dto.question.QuestionQueryRequest;
import com.ruoci.ciojbackendmodel.entity.Question;
import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;
import com.ruoci.ciojbackendmodel.vo.QuestionVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
* @author 帅哥
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2024-03-14 10:31:18
*/
@FeignClient(name = "cioj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

//    questionService.getById(questionId);
//    questionSubmitService.getById(questionSubmitId)
//    questionSubmitService.updateById(questionSubmitUpdate)


    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);


    @GetMapping("question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);


    @PostMapping("question_submit/update")
    boolean updateById(@RequestBody QuestionSubmit questionSubmit);


    @PostMapping("/question/update")
    boolean updateQuestionById(@RequestBody Question question);

}
