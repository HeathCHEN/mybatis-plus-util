

# Mybatis Plus Util
[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis/badge.svg)](https://central.sonatype.com/artifact/io.github.heathchen/mybatis-plus-util)
[![License](https://img.shields.io/:license-apache-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

<p align="center">
   <img alt="Mybatis-Plus-Logo" src="https://img2.imgtp.com/2024/02/28/MWHYA6aK.png">
</p>




## 1. What is Mybatis Plus Util?
MyBatis Plus Util is an enhanced toolkit of MyBatis-Plus for simplify development. Mainly using annotations to annotate entity classes to build QueryWrapper for quick query construction, improving development efficiency.

## 2. Links
- [中文文档](https://github.com/HeathCHEN/mybatis-plus-util/blob/master/README-zh.md)

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
- Modify Entity file marks @QueryField annotation

  ```java  
        @TableName(value ="pdt_spec")  
        public class PdtSpec  {  
          
            @TableId(value = "spec_id")   
            private Long specId;  
  
            @QueryField(value = QueryType.LIKE)   
            @TableField(value = "spec_name")   
            private Long specName;  
  
            @QueryField(orderType = OrderType.ASC)   
            @TableField(value = "create_time")   
            private Date createTime;  
        }  
          
        ```  

- Use it
  
  ```java
        @PostMapping("pdtSpec-list")  
        public TableDataInfo list(@RequestBody PdtSpec search) {    
           return getDataTable(MyBatisPlusUtil.queryByReflect(search));  
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

MyBatis Plus Util is under the Apache 2.0 license. See the Apache License 2.0 file for details.
