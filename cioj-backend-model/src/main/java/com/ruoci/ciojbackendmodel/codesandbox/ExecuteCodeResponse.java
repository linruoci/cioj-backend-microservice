package com.ruoci.ciojbackendmodel.codesandbox;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: ruoci
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {


    private List<String> outputList;
    private String message;

    private Integer status;

    private JudgeInfo judgeInfo;

}
