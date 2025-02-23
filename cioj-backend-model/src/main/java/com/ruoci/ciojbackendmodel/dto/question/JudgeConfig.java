package com.ruoci.ciojbackendmodel.dto.question;

import lombok.Data;

/**
 * @Author: ruoci
 **/
@Data
public class JudgeConfig {

    /**
     * 时间限制(ms)
     */
    private Long timeLimit;

    /**
     * 空间限制(kb)
     */
    private Long memoryLimit;
    /**
     * 堆栈限制.(kb)
     */
    private Long stackLimit;

}
