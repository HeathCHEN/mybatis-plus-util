
# mybatis-plus-util

## 1. What is MyBatis-Plus-Util?
MyBatis-Plus-Util is an enhanced toolkit of MyBatis-Plus for simplify development. Mainly using annotations to annotate entity classes to build QueryWrapper for quick query construction, improving development efficiency.

## 2. Links
- [中文文档](https://github.com/HeathCHEN/mybatis-plus-util/blob/dev/README-zh.md)

# Getting started
- Add MyBatis-Plus-Util dependency
    - Maven:
    - SpringBoot2

      ```xml  
      <dependency>       
        <groupId>io.github.heathchen</groupId>        
        <artifactId>mybatis-plus-util</artifactId>        
        <version>Latest Version</version>    
      </dependency>  
      ```  
- Modify Entity file marks @CustomerQuery annotation

  ```java  
        @TableName(value ="pdt_spec")  
        public class PdtSpec  {  
          
            @TableId(value = "spec_id")   
            private Long specId;  
  
            @CustomerQuery(value = QueryType.LIKE)   
            @TableField(value = "spec_name")   
            private Long specName;  
  
            @CustomerQuery(orderType = OrderType.ASC)   
            @TableField(value = "create_time")   
            private Date createTime;  
        }  
          
        ```  

- Use it
  
  ```java
        @PostMapping("pdtSpec-list")  
        public TableDataInfo list(@RequestBody PdtSpec search) {    
           return getDataTable(queryByReflect(search));  
        }  
        ```  


- case 1:  
  RequestBody
    ```json  
    {  
    }  
    ``` 
  MyBatis-Plus will execute the following SQL
    ```sql  
    SELECT * FROM pdt_spec ORDER BY create_time ASC  
    ```          
- case 2:  
  RequestBody
    ```json  
    {       
	    "specName": "小"  
    }  
    ```   
  MyBatis-Plus will execute the following SQL
  ``` sql
  SELECT * FROM pdt_spec  
  WHERE (spec_name LIKE '%小%')  ORDER BY create_time ASC  
  ```  

- case 3:  
  RequestBody
  ```json  
  {  
     "specId": 1
  }  
  ```  
  MyBatis-Plus will execute the following SQL
  ```sql  
  SELECT * FROM pdt_spec  
  WHERE ( spec_id = 1)  ORDER BY create_time ASC  
  ```  


# License

MyBatis-Plus is under the Apache 2.0 license. See the Apache License 2.0 file for details.
