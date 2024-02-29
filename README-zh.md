
# Mybatis Plus Util

[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis/badge.svg)](https://central.sonatype.com/artifact/io.github.heathchen/mybatis-plus-util)
[![License](https://img.shields.io/:license-apache-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

<p align="center">
   <img alt="Mybatis-Plus-Logo" src="https://img2.imgtp.com/2024/02/28/MWHYA6aK.png">
</p>




## 1. 什么是Mybatis Plus Util?
MyBatis Plus Util是MyBatis-Plus的增强工具类，主要通过注解标注实体类快速构筑单表QueryWrapper的简单查询，提高开发效率。

## 2. Links
- [EngLishDocument](https://github.com/HeathCHEN/mybatis-plus-util/blob/master/README.md)

## 2. 开始
- 增加 MyBatis-Plus-Util 依赖
  - Maven:
  - SpringBoot2

      ```xml    
      <dependency>
        <groupId>io.github.heathchen</groupId>
        <artifactId>mybatis-plus-util</artifactId>
        <version>Latest Version</version>    
      </dependency>    
    ```  

    - 修改实体类，标记@QueryField
  
      ```java  
         @TableName(value ="pdt_spec")  
         public class PdtSpec {    
            /**  
             * id 
             * 
             * */    
            @TableId(value = "spec_id")   
            private Long specId;    
            /**  
             * 规格名  
             */
            @QueryField(value = QueryType.LIKE)   
            @TableField(value = "spec_name")   
            private Long specName;    
            /**  
             * 创建时间 
             */
            @QueryField(orderType = OrderType.ASC)   
            @TableField(value = "create_time")   
            private Date createTime;  
        }    
    
        ```    
- 使用  
  
  ```java    
  @PostMapping("pdtSpec-list")  
  public TableDataInfo list(@RequestBody PdtSpec search) {    
      return getDataTable(MyBatisPlusUtil.queryByReflect(search)); 
  }  
  ```    

  - 示例 1:    
  请求体
    ```json    
    {    
    }    
    ```   
    MyBatis-Plus 会执行以下SQL
    ```sql    
    SELECT * FROM pdt_spec ORDER BY create_time ASC    
    ```          
  - 示例 2:    

    请求体  
    ```json    
    {         
       "specName": "小"    
    }    
    ```
    MyBatis-Plus 会执行以下SQL
    ``` sql  
    SELECT * FROM pdt_spec  WHERE (spec_name LIKE '%小%')  ORDER BY create_time ASC    
    ```    
  - 示例 3:    

    请求体
    ```json    
    {    
       "specId": 1  
    }  
    ```    
    MyBatis-Plus 会执行以下SQL
    ```sql    
    SELECT * FROM pdt_spec    
    WHERE ( spec_id = 1)  ORDER BY create_time ASC    
    ```    
  
## 3. 注解介绍

### 3.1. @QueryField

- 该注解用在实体类的属性上，用于标记对应字段的查询类型或排序。

#### 3.1.1. value
- 该参数用于设置对应字段的查询类型，默认为 QueryType.EQ。
  - 示例:
    ```java
    @TableField(value = "spec_name")  
    @QueryField(QueryType.LIKE)  
    private String specName;
    ```

#### 3.1.2. QueryType.EQ
- 等于查询，与 MyBatis-Plus 的 eq 对应
#### 3.1.3. QueryType.NOT_EQUAL
- 不等于，与 MyBatis-Plus 的 ne 对应
#### 3.1.4. QueryType.LIKE
- LIKE '%值%'，与 MyBatis-Plus 的 like 对应
#### 3.1.5. QueryType.NOT_LIKE
- NOT LIKE '%值%'，与 MyBatis-Plus 的 notLike 对应
#### 3.1.6. QueryType.LIKE_LEFT
- LIKE '%值'，与 MyBatis-Plus 的 likeLeft 对应
#### 3.1.7. QueryType.LIKE_RIGHT
- LIKE '值%'，与 MyBatis-Plus 的 likeRight 对应
#### 3.1.8. QueryType.BETWEEN
- BETWEEN 值1 AND 值2，与 MyBatis-Plus 的 between 对应
#### 3.1.9. QueryType.NOT_BETWEEN
- NOT BETWEEN 值1 AND 值2，与 MyBatis-Plus 的 notBetween 对应
#### 3.1.10. QueryType.LESS_THAN
- 小于 <，与 MyBatis-Plus 的 lt 对应
#### 3.1.11. QueryType.LESS_EQUAL
- 小于等于 <=，与 MyBatis-Plus 的 le 对应
#### 3.1.12. QueryType.GREATER_THAN
- 大于 >，与 MyBatis-Plus 的 gt 对应
#### 3.1.13. QueryType.GREATER_EQUAL
- 大于等于 >=，与 MyBatis-Plus 的 ge 对应
#### 3.1.14. QueryType.IN
- 字段 IN (v0， v1， ...)，与 MyBatis-Plus 的 in 对应
#### 3.1.15. QueryType.NOT_IN
- 字段 NOT IN (value.get(0)， value.get(1)， ...)，与 MyBatis-Plus 的 notIn 对应
#### 3.1.16. QueryType.SQL
- SQL 语句，与 MyBatis-Plus 的 sql 对应

#### 3.1.2. orColumns
- 或查询，查询时匹配多个字段。
  例如在在查询规格表时，需要同时用一个参数模糊查询规格名和重量码。

- 示例:
  实体类
    ```java
    @TableName(value ="pdt_spec")  
    @Data  
    public class PdtSpec{
    	/**  
    	 * 
    	 * id 
    	 * */
    	@TableId(value = "spec_id")  
    	private Long specId;
    
    	/**  
    	 * 规格名  
    	 */
    	@TableField(value = "spec_name")  
    	@QueryField(QueryType.LIKE，，orColumns = {"weight_code"})  
    	@Excel(name = "规格名")  
    	private String specName;
    
    	/**  
    	 * 重量码  
    	 */  
    	@TableField(value = "weight_code")  
    	@QueryField(QueryType.LIKE)  
    	@Excel(name = "重量码")  
    	private String weightCode;
    
    	/**  
    	 * 商品状态（0正常 1停用 2 淘汰）  
    	 */  
    	@TableField(value = "status")  
    	private Integer status;
    }
    ```

  查询参数
  
    ```json
    {
      "specName": "11"，
    }
    ```
  
  执行 SQL
  
    ```sql
    SELECT * FROM pdt_spec
     WHERE (spec_name LIKE '%11%' OR weight_code LIKE '%11%');
    ```

#### 3.1.3. columnName
- 字段名，用于指定对应属性在数据库表字段名，默认为空，优先级高于 @TableField 中 value。非必填，默认会从 mybatis 的缓存中取出属性值对应数据库表字段名。
- 示例
  实体类
    ```java
    @TableField(value = "spec_name_temp")
    @QueryField(value = QueryType.LIKE，columnName = "spec_name")
    private String specName;
    ```

  查询时执行 SQL
    ```sql
    SELECT * FROM pdt_spec
     WHERE (spec_name LIKE '%小%') 
    ```

#### 3.1.4. @orderType
- 该字段用于排序，默认为 OrderType.NONE，不进行排序。
- 示例
  实体类
    ```java
    	@TableName(value ="pdt_spec")  
    	public class PdtSpec  {    
    		/**  
    		 * id 
    		 * 
    		 * */    
    		@TableId(value = "spec_id")   
    		private Long specId;    
    		/**  
    		 * 规格名  
    		 */
    		@QueryField(value = QueryType.LIKE)   
    		@TableField(value = "spec_name")   
    		private Long specName;    
    		/**  
    		 * 创建时间 
    		 */
    		@QueryField(orderType = OrderType.ASC)   
    		@TableField(value = "create_time"，orderPriority = 100)   
    		private Date createTime;  
    		/**  
    		 * 商品状态（0正常 1停用 2 淘汰）  
    		 */  
    		@TableField(value = "status")  
    		@QueryField(orderType = OrderType.DESC，orderPriority = 111)
    		private Integer status;	
    	}    
    ```
  查询参数
    
    ```json
    {
      "specName": "11"，
    }
    ```
  
  执行 SQL
  
    ```sql
    SELECT * FROM pdt_spec
     ORDER BY create_time ASC， status DESC;
    ```

#### 3.1.5. orderPriority
- 用于设置排序优先级，默认为 0。

#### 3.1.6. betweenStartVal
- 当查询类型是 BETWEEN 类型时， BETWEEN 值1 AND 值2，betweenStartVal 表示值1 的属性名，默认为 startTime。
- 示例
  查询 DTO
  
    ```java
          @Data  
          public class PdtSpecRequestDto extends PdtSpec {  
              /**  
               * 开始时间  
               */  
              private Date startTime;  
            
              /**  
               * 结束时间  
               */  
              private Date endTime;
      }
    ```
  
    实体类
  
    ```java
      	@TableName(value ="pdt_spec")  
      	public class PdtSpec  {    
      		/**  
      		 * id 
      		 * 
      		 * */    
      		@TableId(value = "spec_id")   
      		private Long specId;    
      		/**  
      		 * 规格名  
      		 */
      		@QueryField(value = QueryType.LIKE)   
      		@TableField(value = "spec_name")   
      		private Long specName;    
      		/**  
      		 * 创建时间 
      		 */
      		@QueryField(orderType = OrderType.ASC)   
      		@TableField(value = QueryType.BETWEEN，value = "create_time")   
      		private Date createTime;  
      
      	}    
    ```
    
    查询参数1:
    
    ```json
      {
            "specName": "大",
            "startTime": "2023-07-06 11:12:01",
             "endTime": "2024-07-06 13:14:00"
      }
    ```
    
    执行 SQL
    
    ```sql
      SELECT * FROM pdt_spec
      WHERE (spec_name LIKE '%大%' AND create_time >= '2023-07-06 11:12:01.0' AND create_time <= '2024-07-06 13:14:00.0') ORDER BY create_time ASC;
    ```
    查询参数2:

    ```json
      {
            "specName": "大",
            "startTime": "2023-07-06 11:12:01"
      }
    ```

  执行 SQL

  ```sql
     SELECT * FROM pdt_spec
     WHERE (spec_name LIKE '%大%' AND create_time >= '2023-07-06 11:12:01.0')  ORDER BY create_time ASC;
     ```
    
#### 3.1.7. betweenEndVal
- 当查询类型是 BETWEEN 类型时， BETWEEN 值1 AND 值2，betweenEndVal 表示值 2 的属性名，默认为 endTime。

#### 3.1.8. notBetweenStartVal
- 当查询类型是 NOT_BETWEEN 类型时， BETWEEN 值1 AND 值2，notBetweenStartVal 表示值1 的属性名，默认为 startTime。
#### 3.1.9. notBetweenEndVal
- 当查询类型是 NOT_BETWEEN 类型时， BETWEEN 值1 AND 值2，notBetweenEndVal 表示值 2 的属性名，默认为 endTime。

#### 3.1.10. exist
- 当不希望字段参与查询时，将该值设置为 true 即可过滤。

#### 3.1.11. joinType
- 表连接(未实现)。

#### 3.1.12. joinEntityClass
- 表连接实体类(未实现)。

#### 3.1.13. sql
- 当查询类型是 SQL 类型时， 会将该参数拼接到 SQL 中，默认为空值。

### 3.2. @QueryConfig
- 该注解用于实体类上，用于设置排序。不会与@QueryField 中设置的排序冲突。
- 如果有字段重复排序，只会取最后识别的。

- 示例
    实体类
    ```java
    	@TableName(value ="pdt_spec")  
    	public class PdtSpec  {    
    		/**  
    		 * id 
    		 * 
    		 * */    
    		@TableId(value = "spec_id")   
    		private Long specId;    
    		/**  
    		 * 规格名  
    		 */
    		@QueryField(value = QueryType.LIKE)   
    		@TableField(value = "spec_name")   
    		private Long specName;    
    		/**  
    		 * 商品状态（0正常 1停用 2 淘汰）  
    		 */  
    		@TableField(value = "status")  
    		@QueryField(orderType = OrderType.DESC，orderPriority = 1111)  
    		private Integer status;
    		/**  
    		 * 创建时间 
    		 */
    		@QueryField(orderType = OrderType.ASC)   
    		@TableField(value = QueryType.BETWEEN，value = "create_time")   
    		private Date createTime;  
    
    	}    
    ```

#### 3.2.1. orderColumnNames
- 排序的字段名，最好直接填入表字段名。
#### 3.2.2. orderTypes
- 排序类型。
#### 3.2.3. orderColumn
- 开启排序，默认开启。

#### 3.2.4. matchMode
- 匹配模式，有两种全部匹配模式和精确匹配模式。
- 全部匹配模式: 就算实体列字段中没有QueryField注解,但只要查询参数中存在就做全等匹配。
- 精确匹配模式: 只对实体类中标记了QueryField注解的字段做对应类型的匹配。
- 优先级: 调用方法传入的匹配模式 > 注解上配置的匹配模式 > 全局设定的匹配模式
- @QueryConfig注解上matchMode默认值为USING_GLOBAL_MODE,即引用全局设定的匹配模式
- yaml配置文件全局设定示例

  ```yaml
     myBatisPlusUtil:
         globalMatchMode: allMatchMode #全部匹配
  ```
  
  或是  

  ```yaml
     myBatisPlusUtil:
         globalMatchMode: accurateMatchMode #精确匹配
  ```

### 3.3. @CachedTableId
- 自定义冗余字段关联键，配合@CachedTableField 用于快速更新数据库表中对其他表的冗余字段。

#### 3.3.1. value
- 分组 id，用于区分其他分组的@CachedTableId 和@CachedTableField。

### 3.4. @CachedTableField
- 自定义冗余字段关联字段
- 示例

  实体类

    ```java
    public class SysDept extends BaseEntity {  
        /**  
         * 部门id  
         */    @TableId(value = "dept_id"， type = IdType.AUTO)  
        private Long deptId;  
        /**  
         * 租户id  
         */    @TableField(value = "tenant_id")  
        private Long tenantId;  
      
        /**  
         * 父部门id  
         */    @TableField(value = "parent_id")  
        private Long parentId;  
      
        /**  
         * 祖级列表  
         */  
        @TableField(value = "ancestors")  
        private String ancestors;  
      
        /**  
         * 部门名称  
         */  
        @TableField(value = "dept_name")  
        private String deptName;  
      
        /**  
         * 显示顺序  
         */  
        @TableField(value = "order_num")  
        private Integer orderNum;  
      
        /**  
         * 部门负责人名称  
         */  
        @TableField(value = "leader_name")  
        @CachedTableField  
        private String leaderName;  
        /**  
         * 部门负责人id  
         */    
         @TableField(value = "leader_id")  
        @CachedTableId  
        private Long leaderId;
    }
  
    ```

  调用示例
  
    ```java
      public int updateUser(SysUser user) {  
          Long userId = user.getUserId();  
          // 删除用户与角色关联  
          userRoleMapper.deleteUserRoleByUserId(userId);  
          // 新增用户与角色管理  
          insertUserRole(user);  
          // 删除用户与岗位关联  
          userPostMapper.deleteUserPostByUserId(userId);  
          // 新增用户与岗位管理  
          insertUserPost(user);  
          //更新冗余  
          if (StrUtil.isBlank(user.getNickName())) {  
              user.setNickName("");  
          }  
          if (user.getNickName().equals(getById(userId).getNickName())) {  
              Class[] classArr = {SysDept.class};  
              MyBatisPlusUtil.updateCacheField( user.getUserId()， user.getNickName()，classArr);  
          }  
          
          return userMapper.updateById(user);  
      }
      
    ```

#### 3.4.1. value
- 分组 id，用于区分其他分组的@CachedTableId 和@CachedTableField

# License

MyBatis-Plus is under the Apache 2.0 license. See the Apache License 2.0 file for details.
