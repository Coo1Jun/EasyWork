# common包使用说明

## word（和pdf）导出

word导出为文本替换，因此需要一个模板，并且默认路径为`resources`下的`/template/word`.

模板中需要被替换的内容格式为：`${key}`，所有要被替换的文本应该是一个类的属性，也就是说模板中的数据都是一个实体类。比如：${userName}、${age}、${email}等都是User类的属性。

若需要导出图片，则实体类对应的属性的数据类型为`byte[]`，并且需要加上`@Image`注解:

```java
import cn.edu.hzu.common.annotation.Image;
/**
 * 图片
 */
@Image
private byte[] userImage;// 对应替换的格式：${userImage}
```

操作步骤：

1、首先声明一个枚举类实现OfficeTemplate接口，里面包含模板文件的名字、导出文件的名字，例如：

```java
@AllArgsConstructor
@Getter
public enum OfficeTemplateEnum implements OfficeTemplate {

    EXPORT_WORD(
            "word模板",
            "导出word文件",
            null
    )
    ;
    private String templateName;
    private String fileName;
    private String[] fullField;
}
```

2、调用ConvertOfficeFactory构建生成ConvertOffice：

```java
ConvertOffice convertOffice = ConvertOfficeFactory.build(参数1，参数2为步骤1的枚举类);
ConvertOffice convertOffice = ConvertOfficeFactory.build(参数0，参数1，参数2为步骤1的枚举类);
```

参数0是boolean类型：出现异常是否抛出，参数1是ExportType枚举类型，可以导出word和pdf：

```java
@Getter
@AllArgsConstructor
public enum ExportType {

    WORD("word",".docx"),
    PDF("pdf",".pdf");

    private String type;
    private String suffix;

}
```

如果是导出pdf，是内部是将word转html，再将html转pdf，因此生成的pdf可能格式不是很正确。

3、调用ConvertOffice对象的convert方法：

```java
public <T> ConvertOffice convert(boolean dateFmt, T bean, List tableData);// 需要再调用export方法
```

参数bean为实体类。tableData为自动添加表格的行时需要填充的数据，与OfficeTemplate搭配使用，一般用不到，传null即可。

4、再调用export方法

```java
public void export(HttpServletResponse response);// 仅替换内容
public void exportHeader(HttpServletResponse response);// 替换页眉和内容
```

## Excel导出

在需要导出的实体类的属性加上`@Excel`和`@Excels`注解，`@Excels`注解为多注解，可包含多个`@Excel`单注解。

```java
/**
* 姓名
*/
@Excel(name = "姓名", cellType = ColumnType.STRING)
private String userName;

/**
* 年龄
*/
@Excel(name = "年龄", cellType = ColumnType.STRING, suffix = "->假的", prompt = "年龄是假的")
private Integer age;

/** 创建时间 */
@Excel(name = "创建时间", cellType = ColumnType.STRING, dateFormat = "yyyy-MM-dd HH:mm:ss")
private Date createTime;
```

多注解`@Excels`作用在一个实体属性上，可以将实体的属性拆分导出，如：

```java
@Excels({
        @Excel(name = "姓名", cellType = ColumnType.STRING),
        @Excel(name = "创建时间", cellType = ColumnType.STRING, dateFormat = "yyyy-MM-dd HH:mm:ss")
})
User user;
```

可以将user属性的两个子属性导出。

使用步骤：

1、例如实体类是UserDto

```java
ExcelUtils<UserDto> utils = new ExcelUtils<>(UserDto.class);
```

2、实现调用即可。

```java
// public void exportExcelAndDownload(List<T> list, String sheetName, HttpServletResponse response);
utils.exportExcelAndDownload(list, fileName, response);
```

## Excel导入

导入和导出操作一样，在导入的实体类的属性加上注解，utils调用是的impoort：

```java
// public List<T> importExcel(MultipartFile file);
List<UserDto> list = utils.importExcel(file);
```

返回List就是从Excel表格读取到的数据。

## 代码生成

在common的test里，配置resources下的gen-info.json后，运行即可。

```java
public class CodeGen {
    public static void main(String[] args) throws IOException {
        CodeGenUtils.generator();
    }
}
```

```json
{
  "author": "LiZhengFan",
  "moduleName": "EmptyProject",
  "parentPackage": "com.example.emptyproject.test",
  "tables": "t_product",
  "tablePrex": "t_",
  "functionName": "产品信息",
  "jdbcUrl": "jdbc:mysql://localhost:3306/mybatis_plus?serverTimezone=GMT&useUnicode=true&characterEncoding=UTF-8",
  "jdbcUsername": "root",
  "jdbcPassword": "111777",
  "jdbcDriver": "com.mysql.cj.jdbc.Driver",
  "jdbcSchema": "",
  "businessName": "test/product"
}
```







