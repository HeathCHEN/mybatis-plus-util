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

##### 3.1.1.1. QueryType.EQ

- 等于查询，与 MyBatis-Plus 的 eq 对应

##### 3.1.1.2. QueryType.NOT_EQUAL

- 不等于，与 MyBatis-Plus 的 ne 对应

##### 3.1.1.3. QueryType.LIKE

- LIKE '%值%'，与 MyBatis-Plus 的 like 对应

##### 3.1.1.4. QueryType.NOT_LIKE

- NOT LIKE '%值%'，与 MyBatis-Plus 的 notLike 对应

##### 3.1.1.5. QueryType.LIKE_LEFT

- LIKE '%值'，与 MyBatis-Plus 的 likeLeft 对应

##### 3.1.1.6. QueryType.LIKE_RIGHT

- LIKE '值%'，与 MyBatis-Plus 的 likeRight 对应

##### 3.1.1.7. QueryType.BETWEEN

- BETWEEN 值1 AND 值2，与 MyBatis-Plus 的 between 对应

##### 3.1.1.8. QueryType.NOT_BETWEEN

- NOT BETWEEN 值1 AND 值2，与 MyBatis-Plus 的 notBetween 对应

##### 3.1.1.9. QueryType.LESS_THAN

- 小于 <，与 MyBatis-Plus 的 lt 对应

##### 3.1.1.10. QueryType.LESS_EQUAL

- 小于等于 <=，与 MyBatis-Plus 的 le 对应

##### 3.1.1.11. QueryType.GREATER_THAN

- 大于 >，与 MyBatis-Plus 的 gt 对应

##### 3.1.1.12. QueryType.GREATER_EQUAL

- 大于等于 >=，与 MyBatis-Plus 的 ge 对应

##### 3.1.1.13. QueryType.IN

- 字段 IN (v0， v1， ...)，与 MyBatis-Plus 的 in 对应

##### 3.1.1.14. QueryType.NOT_IN

- 字段 NOT IN (value.get(0)， value.get(1)， ...)，与 MyBatis-Plus 的 notIn 对应

##### 3.1.1.15. QueryType.SQL

- SQL 语句，与 MyBatis-Plus 的 sql 对应

#### 3.1.2. orColumns

- 或查询，查询时匹配多个字段。 例如在在查询规格表时，需要同时用一个参数模糊查询规格名和重量码。

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
      "specName": "11"
    }
    ```

  执行 SQL

    ```sql
    SELECT * FROM pdt_spec
     WHERE (spec_name LIKE '%11%' OR weight_code LIKE '%11%');
    ```

#### 3.1.3. columnName

- 字段名，用于指定对应属性在数据库表字段名，默认为空，优先级高于 @TableField 中 value。非必填，默认会从 mybatis 的缓存中取出属性值对应数据库表字段名。
- 示例 实体类
    ```java
    @TableField(value = "spec_name_temp")
    @QueryField(value = QueryType.LIKE，columnName = "spec_name")
    private String specName;
    ```

  查询参数

    ```json
    {
      "specName": "小"
    }
    ```
  查询时执行 SQL
    ```sql
    SELECT * FROM pdt_spec
     WHERE (spec_name LIKE '%小%') 
    ```

#### 3.1.4. @orderType

- 该字段用于排序，默认为 OrderType.NONE，不进行排序。
- 示例 实体类
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
    		@QueryField(orderType = OrderType.ASC，orderPriority = 100)   
    		@TableField(value = "create_time")   
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
- 示例 查询 DTO

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

#### 3.1.14. groupId

- 用于分组查询,调用查询时指定分组只对了分组的@QueryField生效

### 3.2. @QueryConfig

- 该注解用于实体类上，用于设置排序。不会与@QueryField 中设置的排序冲突。
- 如果有字段重复排序，只会取最后识别的。

- 示例 实体类
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
[匹配模式](#4.1)
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

## 4. 全局配置

```yaml
myBatisPlusUtil:
  globalMatchMode: allMatchMode #全部匹配
  starPagePropertyName: startPage #是否开启分页名
  isAscPropertyName: isAsc #是否自然排序名
  pageSizePropertyName: pageSize #页面大小名
  pageNumPropertyName: pageNum #页码名
  orderByColumnPropertyName: orderByColumn #排序字段名
  reasonablePropertyName: reasonable #分页合理化名
  starPagePropertyDefaultValue: true #默认开启分页
  isAscPropertyDefaultValue: false #默认倒叙
  pageSizePropertyDefaultValue: 15 #默认页面大小
  pageNumPropertyDefaultValue: 1 #默认页码
  orderByColumnPropertyDefaultValue: create_time #默认排序字段
  reasonablePropertyDefaultValue: true #默认分页合理

```
### 4.1 全局匹配模式(globalMatchMode)
<a id="4.1"></a>
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
### 4.2 是否开启分页名(starPagePropertyName)
是否开启分页的参数名,用于指定GET\POST\DELETE\PUT请求的RequestBody\RequestParam中是否开启分页的参数名,
通过该参数会读取前端传来的值(没有则读取默认值或全局配置的默认值),获取是否开启分页值。默认值 **startPage**

### 4.3 是否自然排序名(isAscPropertyName)
是否开启分页的参数名,用于指定GET\POST\DELETE\PUT请求的RequestBody\RequestParam中是否自然排序的参数名,
通过该参数会读取前端传来的值(没有则读取默认值或全局配置的默认值),获取是否自然排序值。

### 4.4 页面大小名(pageSizePropertyName)
是否开启分页的参数名,用于指定GET\POST\DELETE\PUT请求的RequestBody\RequestParam中页面大小名的参数名,
通过该参数会读取前端传来的值(没有则读取默认值或全局配置的默认值),获取页面大小值。

### 4.5 页码名(pageNumPropertyName)
是否开启分页的参数名,用于指定GET\POST\DELETE\PUT请求的RequestBody\RequestParam中页码名的参数名,
通过该参数会读取前端传来的值(没有则读取默认值或全局配置的默认值),获取页码值。

### 4.6 排序字段名(orderByColumnPropertyName)
是否开启分页的参数名,用于指定GET\POST\DELETE\PUT请求的RequestBody\RequestParam中排序字段的参数名,
通过该参数会读取前端传来的值(没有则读取默认值或全局配置的默认值),获取排序字段值。

### 4.7 分页合理化名(reasonablePropertyName)
是否开启分页的参数名,用于指定GET\POST\DELETE\PUT请求的RequestBody\RequestParam中是否分页合理化的参数名,
通过该参数会读取前端传来的值(没有则读取默认值或全局配置的默认值),获取是否分页合理化值。

### 4.8 是否开启分页默认值(starPagePropertyDefaultValue)
是否开启分页的默认值,当无法从RequestBody\RequestParam中获取参数值时,取该默认值。
默认为false,不分页。

### 4.9 是否自然排序默认值(isAscPropertyDefaultValue)
是否自然排序默认值,当无法从RequestBody\RequestParam中获取参数值时,取该默认值。
默认为倒叙。

### 4.10 页面大小默认值(pageSizePropertyDefaultValue)
页面大小的默认值,当无法从RequestBody\RequestParam中获取参数值时,取该默认值。
默认为15。

### 4.11 页码大小默认值(pageNumPropertyDefaultValue)
页码的默认值,当无法从RequestBody\RequestParam中获取参数值时,取该默认值。
默认为1。

### 4.12 排序字段默认值(orderByColumnPropertyDefaultValue)
排序字段的默认值,当无法从RequestBody\RequestParam中获取参数值时,取该默认值。
默认为按时间create_time字段排序。

### 4.13 分页合理化默认值(reasonablePropertyDefaultValue)
是否分页合理化的默认值,当无法从RequestBody\RequestParam中获取参数值时,取该默认值。
默认为开启分页合理化。


## 5. 使用方法

### 5.1. 查询

#### 5.1.1 MyBatisPlusUtil.queryByReflect(E e, Class<T> clazz, MatchMode matchMode, String[] groupIds, Consumer<QueryWrapper<?>> consumer, String... ignoreParams)方法

该方法通过递归查询参数的类根据配置构建查询的*QueryWrapper*, 在递归反射从查询参数的类中获取@TableName()注解,从而找到对应BaseMapper的Bean调用list()由MyBatisPlus生成SQL获取结果。

##### 5.1.1.1 E e

查询的参数类,实体类或者*Dto*类(最好是实体类,如果是*Dto*类需要在类上标记**@TableName("table_name"))**。

##### 5.1.1.2 Class<T> clazz

返回的类型,默认返回类型与查询参数类一致,可以指定对应类以排除需要返回的字段。

##### 5.1.1.3 MatchMode matchMode

[匹配模式](#4.1)

##### 5.1.1.3 String[] groupIds

分组id,构建的查询只对对应分组的@QueryField生效。 *@QueryField*中可以设置查询类型的分组。

##### 5.1.1.4 Consumer<QueryWrapper<?>> consumer

*QueryWrapper*消费者,可以获取*QueryWrapper*并自定义。

##### 5.1.1.5 String... ignoreParams

可以指定忽略哪些参数的过滤

##### 5.1.1.6 Boolean withOutLike

将查询LIKE相关类型的查询全部改为EQ查询

#### 5.1.2 QueryBuilder.doQuery()方法

与queryByReflect()方法相同,但是可以链式构建查询配置

### 5.2. 校验唯一性

#### 5.2.1 MyBatisPlusUtil.checkUniqueByReflect(E e, Integer limit, String... groupIds) 方法

配合@UniqueValue注解可以查询对应是否某字段唯一或小于指定个数,可以指定limit最大个数。 groupIds用于指定由哪些字段组合唯一型。

示例1

```java
    @Override
@Transactional
public Boolean editSave(BaseDevice baseDevice){
        String deviceId=baseDevice.getDeviceId();

        if(!MyBatisPlusUtil.checkUniqueByReflect(baseDevice,0,
        queryWrapper->queryWrapper.ne("device_id",deviceId),"device_name")){
        throw new GsException("已存在相同设备名称!");
        }

        boolean result=updateById(baseDevice);

        return result;
        }

```

示例2

```java

@Override
@Transactional
public Boolean addSave(BaseDevice baseDevice){
        if(!MyBatisPlusUtil.checkUniqueByReflect(baseDevice,0,"device_name")){
            throw new GsException("已存在相同设备名称!");
        }
}
```

### 5.3. 更新冗余字段

#### 5.3.1 MyBatisPlusUtil.updateCacheField(Object associationKey, Object newCacheFieldValue, String groupId, Class<?>... clazzArr)方法

配合@CachedTableField和@CachedTableId注解更新冗余值。 @CachedTableField标记在冗余值上,@CachedTableId标记在关联键上。 Object
associationKey表示需要更新的关联键。 Object newCacheFieldValue表示冗余值的新值。 String groupId表述需要更新的冗余分组 Class<?>... clazzArr表示需要更新类

示例 实体类

```java

@TableName(value = "base_device_branch")
@Data
@Accessors(chain = true)
public class BaseDeviceBranch {
    /**
     * 设备绑定id
     */
    @TableId(value = "device_branch_id")
    private Long deviceBranchId;

    /**
     * 设备号
     */
    @TableField(value = "device_id")
    @NotBlank(message = "设备号不能为空")
    @CachedTableId(value = "deviceName")
    private String deviceId;
    /**
     * 设备名称
     */
    @TableField(value = "device_name")
    @NotBlank(message = "设备名称不能为空")
    @CachedTableField(value = "deviceName")
    private String deviceName;

}
```

调用

  ```java
       MyBatisPlusUtil.updateCacheField(baseDevice.getDeviceId(),baseDevice.getDeviceName(),
        "deviceName",
        BaseDeviceBranch.class,BasePosBranchUser.class);
  ```
### 5.4. 便捷方法

#### 5.4.1 补全时间

MyBatisPlusUtil.getNewDateTo235959999FromDate(Date date)
该方法可以将某天设为23时59分59秒9999毫秒。

MyBatisPlusUtil.getNewDateTimeTo000000000FromDate(Date date)
该方法可以将某天设为00时00分00秒0000毫秒。

#### 5.4.2 List转Map

MyBatisPlusUtil.listToDataMapping(List<T> list)
该方法可以将查询出来的结果转为以主键为*key*,实体为*value*。
**此方法只适用于含有@TableId实体类**

#### 5.4.3 构筑方法

MyBatisPlusUtil中包含许多方法可直接对本次查询参数进行修改

- closePage() 关闭分页
- closeOrder() 关闭排序
- setIgnoreParams(String... ignoreParams) 设置忽略参数
- setLimitValue(Integer limit) 设置最大个数(用于检查唯一相关方法)
- setMatchMode(MatchMode matchMode) 设置匹配模式
- setConsumer(Consumer<QueryWrapper<?>> consumer) 设置消费者
- setGroupIds(String... groupIds) 设置分组ID
- setWithoutLike(Boolean withoutLike) 设置是否排除模糊查询参数
- setQueryParam(String key,Object value) 设置查询参数
- setStartPage(Boolean startPage) 是否开启分页
- setIsAsc(Boolean isAsc) 设置排序顺序
- setPageSize(Integer pageSize) 设置页面大小参数
- setPageNum(Integer pageNum) 设置分页页码参数
- setOrderByColumn(String orderByColumn) 设置排序字段参数
- setReasonable(Boolean reasonable) 设置是否分页合理参数
- setOrderColumn(Boolean orderColumn) 设置是否排序

# License

MyBatis Plus Util is under the Apache 2.0 license. See the Apache License 2.0 file for details.
