
# mybatis-plus-util

## 1. 什么是MyBatis-Plus?
MyBatis-Plus-Util是MyBatis-Plus的增强工具类,主要通过注解标注实体类快速构筑单表QueryWrapper的简单查询,提高开发效率。

# 开始
- 增加 MyBatis-Plus-Util 依赖
  - Maven:
  - SpringBoot2

      ```xml    
      <dependency>        <groupId>io.github.heathchen</groupId>        <artifactId>mybatis-plus-util</artifactId>        <version>Latest Version</version>    </dependency>    
```  - Modify Entity file marks @CustomerQuery annotation  
  
```java  @TableName(value ="pdt_spec")  public class PdtSpec  {    
    
   @TableId(value = "spec_id")   private Long specId;    
   @CustomerQuery(value = QueryType.LIKE)   @TableField(value = "spec_name")   private Long specName;    
@CustomerQuery(orderType = OrderType.ASC)   @TableField(value = "create_time")   private Date createTime;  }    
    
```    
- 使用
```java    
    
@PostMapping("pdtSpec-list")  public TableDataInfo list(@RequestBody PdtSpec search) {    return getDataTable(queryByReflect(search));  }  ```    
  
- 示例 1:    
  RequestBody  
    ```json    
    {    
    }    
    ```   
MyBatis-Plus will execute the following SQL  
    ```sql    
    SELECT * FROM pdt_spec ORDER BY create_time ASC    
```          - 示例 2:    
  RequestBody  
    ```json    
    {         
       specName: "小"    
    }    
    ```     
MyBatis-Plus will execute the following SQL  
  ``` sql  
  SELECT * FROM pdt_spec  WHERE (spec_name LIKE '%小%')  ORDER BY create_time ASC    
```    
- 示例 3:    
  RequestBody
  ```json    
  {    
     specId: 1  
  }  ```    
MyBatis-Plus will execute the following SQL
  ```sql    
  SELECT * FROM pdt_spec    
  WHERE ( spec_id = 1)  ORDER BY create_time ASC    
```    



# 注解介绍

##  CustomerQuery

- 该注解用在实体类的属性上,用于标记对应字段的查询类型或排序。

###  value
- 该参数用于设置对应字段的查询类型,默认为 QueryType.EQ
- 示例:
```java
@TableField(value = "spec_name")  
@CustomerQuery(QueryType.LIKE)  
private String specName;
```

#### 0.1.1. QueryType.EQ
- 等于查询,与 MyBatis-Plus 的 eq 对应
#### 0.1.2. QueryType.NOT_EQUAL
- 不等于,与 MyBatis-Plus 的 ne 对应
#### 0.1.3. QueryType.LIKE
- LIKE '%值%',与 MyBatis-Plus 的 like 对应
#### 0.1.4. QueryType.NOT_LIKE
- NOT LIKE '%值%',与 MyBatis-Plus 的 notLike 对应

#### 0.1.5. QueryType.LIKE_LEFT

#### 0.1.6. QueryType.LIKE_RIGHT

#### 0.1.7. QueryType.BETWEEN

#### 0.1.8. QueryType.NOT_BETWEEN

#### 0.1.9. QueryType.LESS_THAN

#### 0.1.10. QueryType.LESS_EQUAL

#### 0.1.11. QueryType.GREATER_THAN

#### 0.1.12. QueryType.GREATER_EQUAL

#### 0.1.13. QueryType.IN

#### 0.1.14. QueryType.NOT_IN

#### 0.1.15. QueryType.SQL

### 0.2. orColumns


# License

MyBatis-Plus is under the Apache 2.0 license. See the Apache License 2.0 file for details.
