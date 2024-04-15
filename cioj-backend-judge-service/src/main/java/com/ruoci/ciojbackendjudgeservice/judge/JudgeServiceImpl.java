package com.ruoci.ciojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.ruoci.ciojbackendjudgeservice.judge.codesandbox.CodeSandBoxFactory;
import com.ruoci.ciojbackendjudgeservice.judge.codesandbox.CodeSandBoxProxy;
import com.ruoci.ciojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.ruoci.ciojbackendjudgeservice.judge.strategy.JudgeContext;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.ruoci.ciojbackendmodel.codesandbox.ExecuteCodeResponse;
import com.ruoci.ciojbackendmodel.codesandbox.JudgeInfo;
import com.ruoci.ciojbackendmodel.common.ErrorCode;
import com.ruoci.ciojbackendmodel.dto.question.JudgeCase;
import com.ruoci.ciojbackendmodel.entity.Question;
import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;
import com.ruoci.ciojbackendmodel.enums.JudgeInfoMessageEnum;
import com.ruoci.ciojbackendmodel.enums.QuestionSubmitLanguageEnum;
import com.ruoci.ciojbackendmodel.enums.QuestionSubmitStatusEnum;
import com.ruoci.ciojbackendmodel.exception.BusinessException;
import com.ruoci.ciojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: ruoci
 **/
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private JudgeManager judgeManager;



    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {

//        1.查出对应的QuestionSubmit和Question
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在!!");
        }
        long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "问题不存在!!");
        }

//        2.如果不为等待判题， 直接返回.
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中!!!");
        }


//        3.更新题目状态.
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());

        boolean flag = questionFeignClient.updateById(questionSubmitUpdate);
        if (!flag){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统异常!!");
        }

//        4.调用沙箱, 获取执行结果.
        CodeSandbox codeSandbox = CodeSandBoxFactory.newInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandbox);

//        4.1 这一步是为了获取我们的输入案例.
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(judgeCase -> judgeCase.getInput()).collect(Collectors.toList());
        String code = questionSubmit.getCode();
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .inputList(inputList)
                .language(language)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
//        5.根据沙箱返回的结果进行判断. 其实也就是进行判题
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setOutputList(outputList);
        judgeContext.setInputList(inputList);
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo =  judgeManager.doJudge(judgeContext);

        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));

        String message = judgeInfo.getMessage();

        if (message.equals(JudgeInfoMessageEnum.ACCEPTED.getValue())){
            question.setAcceptedNum(question.getAcceptedNum() + 1);
            questionFeignClient.updateQuestionById(question);
        }

        flag = questionFeignClient.updateById(questionSubmitUpdate);
        if (!flag){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统异常!!");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        return questionSubmitResult;



    }
}
