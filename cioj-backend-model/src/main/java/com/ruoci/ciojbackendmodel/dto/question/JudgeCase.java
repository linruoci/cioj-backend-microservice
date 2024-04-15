package com.ruoci.ciojbackendmodel.dto.question;

import lombok.Data;

/**
 * @Author: ruoci
 **/
@Data
public class JudgeCase {

    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;

}
