package com.ruoci.ciojbackendmodel.dto.questionsubmit;

import com.ruoci.ciojbackendmodel.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {


    /**
     * 提交语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;


    /**
     * 题目id
     */
    private Long questionId;


    private Long userId;


    private static final long serialVersionUID = 1L;
}