package com.ruoci.ciojbackendserviceclient.service;


import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: ruoci
 **/
@FeignClient(name = "cioj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignClient {

    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);

}
