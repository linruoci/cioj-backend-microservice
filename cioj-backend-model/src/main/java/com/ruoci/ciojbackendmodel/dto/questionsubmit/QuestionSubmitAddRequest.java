package com.ruoci.ciojbackendmodel.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建题目
 *
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {


    /**
     * 提交语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;


    /**
     * 题目id
     */
    private Long questionId;


    private static final long serialVersionUID = 1L;
}