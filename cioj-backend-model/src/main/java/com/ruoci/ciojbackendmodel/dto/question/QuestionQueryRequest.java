package com.ruoci.ciojbackendmodel.dto.question;

import com.ruoci.ciojbackendmodel.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 */
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案()
     */
    private String answer;

    /**
     * 创建用户 id
     */
    private Long userId;


}