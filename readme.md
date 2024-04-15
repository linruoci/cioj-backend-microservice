## OJ微服务版本
**核心业务流程:**
![image.png](https://cdn.nlark.com/yuque/0/2024/png/34300248/1713068236701-03800aab-2ab7-463d-adc2-40d9957ae818.png#averageHue=%23fafafa&clientId=u09e35dd8-f35f-4&from=paste&height=571&id=u390a63ed&originHeight=756&originWidth=1097&originalType=binary&ratio=1.3249999284744263&rotation=0&showTitle=false&size=113819&status=done&style=none&taskId=uaa6038ce-106b-4fe9-b940-32aa09f209d&title=&width=827.924572994551)

### 1.功能拆分.

1. **用户模块**
- 注册
- 登录（后端已实现， 前端已实现）
2. **题目模块**
- 创建题目(管理员)
- 删除题目(管理员)
- 修改题目(管理员)
- 搜索题目(用户)
- 在线做题(题目详情页)
3. **判题模块**
- 提交判题（结果是否正确与错误）
- 错误处理（内存溢出， 安全性， 超时）
- 自主实现代码沙箱
- **开放接口（其它人也可以调用）**


### 2.模块拆分
**用户模块**（oj-backend-user-service 8102）
**题目模块**  (oj-backend-question-service 8103)
**判题模块**  (oj-backend-user-service 8104)
此外, 还有一些共有的公共模块

- **common** 公共模块(oj-backend-common)： 全局异常处理器， 请求响应封装类， 公用的工具类等
- **model **模型模块(oj-backend-model)：服务公用实体类
- **公用接口**（oj-backend-service-client）：只存放接口， 不存放实现， （**供各服务之间私下调用！**）

### 3.路由划分.
使用context-path统一修改各项目前缀.
**用户服务**
/api/user
/api/user/inner（网关层面进行限制.）

**题目服务**
/api/question
/api/question/inner（网关层面进行限制.）

**判题服务**
/api/judge
/api/judge/inner（网关层面进行限制.）

### 4.功能截图
![image.png](https://cdn.nlark.com/yuque/0/2024/png/34300248/1713169851595-97e3aeb3-acb0-4a09-9112-2156bb829540.png#averageHue=%23f9f8f8&clientId=u8f16d3c7-8277-4&from=paste&height=646&id=u72d4e0d5&originHeight=856&originWidth=1910&originalType=binary&ratio=1.3249999284744263&rotation=0&showTitle=false&size=309920&status=done&style=none&taskId=u821c8054-3ec2-4454-8784-68f63c314eb&title=&width=1441.5095117772037)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/34300248/1713169867606-dcc00152-7f6f-4ce2-88c8-669eb346fb3a.png#averageHue=%23fbfbfb&clientId=u8f16d3c7-8277-4&from=paste&height=653&id=u759516f1&originHeight=865&originWidth=1920&originalType=binary&ratio=1.3249999284744263&rotation=0&showTitle=false&size=62365&status=done&style=none&taskId=u8bf21568-f220-4433-9626-ea19c68b775&title=&width=1449.0566819959324)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/34300248/1713169976480-8595aaf4-4ec6-480d-8b1a-11b351363b20.png#averageHue=%23b8b8b8&clientId=u8f16d3c7-8277-4&from=paste&height=643&id=ua65ca3b3&originHeight=852&originWidth=1915&originalType=binary&ratio=1.3249999284744263&rotation=0&showTitle=false&size=55340&status=done&style=none&taskId=u4a1f2652-4573-4e5f-8bb5-264768eee82&title=&width=1445.283096886568)
![image.png](https://cdn.nlark.com/yuque/0/2024/png/34300248/1713170308400-a6d70d3c-80cf-4304-9595-5d9af2a8c3ee.png#averageHue=%23fbfaf9&clientId=u8f16d3c7-8277-4&from=paste&height=650&id=u38140a68&originHeight=861&originWidth=1886&originalType=binary&ratio=1.3249999284744263&rotation=0&showTitle=false&size=105868&status=done&style=none&taskId=u00b63ab2-1a66-42af-97f6-e2209bb52b7&title=&width=1423.3963032522545)
