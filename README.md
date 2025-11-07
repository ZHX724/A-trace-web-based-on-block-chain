项目总览
本仓库是一个基于 Spring Boot 的多层 Web 应用，pom.xml 定义了 Web、Thymeleaf、JPA、MySQL、Web3j、ZXing 和 Lombok 等依赖，目标是构建农产品溯源系统并支持二维码生成。

应用入口类 FengDengApplication 位于根包下，负责启动 Spring Boot 并输出启动日志。

核心区块链模型
com.fengdeng.blockchain 包实现了简化的链式存储：Block 管理索引、时间戳、交易集合和前后哈希，使用 HashUtil 计算区块哈希；Blockchain 维护链表结构、创世区块和链完整性校验逻辑；Transaction 则封装单条农事记录的数据结构。

业务服务层
BlockService 作为核心服务，将表单提交的农事信息封装为链上交易、创建新块并追加到内存链，同时映射为 BlockEntity/TransactionEntity 持久化到数据库，生成防伪码并允许重复使用；还提供链查询和校验功能。

数据持久层
model 包下的实体类对应数据库表：BlockEntity 与 TransactionEntity 形成一对多关联，保存区块元信息和交易详情；UserEntity 存储登录用户及其角色、注册时间。

repository 包提供基于 Spring Data JPA 的接口，支持按产品、农户或防伪码检索交易记录，以及统计农户数量等操作。

Web 控制层
HomeController 渲染首页仪表盘，统计农户数量与上链次数；LoginController/RegisterController 管理用户认证；QRCodeController 负责二维码生成、溯源展示、历史记录页面以及录入表单；BlockchainController 暴露 REST 接口处理上链、链查询与校验；VerifyController 提供防伪码验证流程。

配置与工具
AuthFilter 是一个 Servlet 过滤器，检查 session 登录状态并为首页、注册、验证等公开路径设置放行逻辑（当前示例尚未补充未登录时的拦截动作）。

QRCodeUtil 基于 ZXing 既能生成本地文件二维码，也能返回 Base64 字符串供前端展示。

application.yml 配置数据源、JPA 策略、Thymeleaf 缓存与服务器端口等运行参数。

前端视图
templates 目录包含全部 Thymeleaf 模板：index.html 的落地页展示统计数据；login.html/register.html 提供表单页面；add.html 集成异步上链与二维码展示；history.html 列出农户历史上链记录；trace.html 面向消费者展示某产品的完整溯源；verify.html 用于防伪码校验。

其他文件
BlockchainTest 位于根包中，提供一个简单的 main 方法演示链的创建、添加交易和校验流程，可用于理解区块链模型的运作。

src/main/resources/static 保留静态资源目录（示例中仅含占位文件），可扩展为存放 CSS、JS 或图片。
