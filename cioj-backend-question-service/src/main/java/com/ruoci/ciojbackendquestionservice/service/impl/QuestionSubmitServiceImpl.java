package com.ruoci.ciojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoci.ciojbackendmodel.common.ErrorCode;
import com.ruoci.ciojbackendmodel.constant.CommonConstant;
import com.ruoci.ciojbackendmodel.dto.questionsubmit.QuestionSubmitAddRequest;
import com.ruoci.ciojbackendmodel.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.ruoci.ciojbackendmodel.entity.Question;
import com.ruoci.ciojbackendmodel.entity.QuestionSubmit;
import com.ruoci.ciojbackendmodel.entity.User;
import com.ruoci.ciojbackendmodel.enums.QuestionSubmitLanguageEnum;
import com.ruoci.ciojbackendmodel.enums.QuestionSubmitStatusEnum;
import com.ruoci.ciojbackendmodel.exception.BusinessException;
import com.ruoci.ciojbackendmodel.utils.SqlUtils;
import com.ruoci.ciojbackendmodel.vo.QuestionSubmitVO;
import com.ruoci.ciojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.ruoci.ciojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.ruoci.ciojbackendquestionservice.service.QuestionService;
import com.ruoci.ciojbackendquestionservice.service.QuestionSubmitService;
import com.ruoci.ciojbackendserviceclient.service.JudgeFeignClient;
import com.ruoci.ciojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author 帅哥
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-03-14 10:33:02
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;


    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeService;


    @Resource
    private MyMessageProducer myMessageProducer;


    /**
     * 提交题目
     *
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {

//        判断编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        // 锁必须要包裹住事务方法
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);


        question.setSubmitNum(question.getSubmitNum() + 1);

//        设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean result = this.save(questionSubmit);
        questionService.updateById(question);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交失败!");
        }

//        todo: 执行判题任务.
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmit.getId()));
//        CompletableFuture.runAsync(() -> judgeService.doJudge(questionSubmit.getId()));
        return questionSubmit.getId();
    }


    /**
     * 用户根据哪些字段查询
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();

        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        queryWrapper.eq(StringUtils.isNotBlank(language), "id", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "userId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
//        User loginUser = userService.getLoginUser(request);
        long userId = loginUser.getId();
        if (userId != questionSubmit.getUserId() && !userFeignClient.isAdmin(loginUser)){
            questionSubmitVO.setCode("");
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> {
                    return getQuestionSubmitVO(questionSubmit, loginUser);
                })
                .collect(Collectors.toList());

        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
//        // 1. 关联查询用户信息
//        Set<Long> userIdSet = questionSubmitList.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
//        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
//                .collect(Collectors.groupingBy(User::getId));

        // 2. 已登录，获取用户点赞、收藏状态
//        Map<Long, Boolean> questionSubmitIdHasThumbMap = new HashMap<>();
//        Map<Long, Boolean> questionSubmitIdHasFavourMap = new HashMap<>();
//        User loginUser = userService.getLoginUserPermitNull(request);
        // 填充信息
//        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
//            QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
//            Long userId = questionSubmit.getUserId();
//            User user = null;
//            if (userIdUserListMap.containsKey(userId)) {
//                user = userIdUserListMap.get(userId).get(0);
//            }
//            questionSubmitVO.setUserVO(userService.getUserVO(user));
//            return questionSubmitVO;
//        }).collect(Collectors.toList());

    }

    
    
}




