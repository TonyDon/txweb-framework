txweb-framework
====================
txweb-framework 封装了DAO, Action, Validator,常用功能<br/>
GenericBaseDAO : <br/> 
 - 封装常用的数据库操作，支持Mybatis与JdbcTemplate<br/>
BaseAction ： <br/>
 - 封装查询操作的方法queryAction 如 分页查询 <br/>
 - 提供模版方法updateAction 用于更新操作<br/>
 ValidateUtil:<br/>
 - 封装 javax.Validation常用方法