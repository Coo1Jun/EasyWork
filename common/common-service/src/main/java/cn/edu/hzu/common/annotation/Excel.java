package cn.edu.hzu.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义导出Excel数据注解
 *
 * @author lzf
 * @Date 2022/8/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    /**
     * 导出到Excel中的名字
     */
    String name() default "";

    /**
     * 日期格式，如：yyyy-MM-dd
     */
    String dateFormat() default "";

    /**
     * 读取内容转换表达式 (如: "0=男,1=女"；则0会转换成男，一般用于处理枚举类型)
     */
    String readConverterExp() default "";

    /**
     * 导出类型（0数字 1字符串）
     */
    ColumnType cellType() default ColumnType.STRING;

    /**
     * 导出时在Excel中每个列的高度 单位为字符
     */
    double height() default 14;

    /**
     * 导出时在Excel中每个列的宽 单位为字符
     */
    double width() default 16;

    /**
     * 文字后缀，如%90 变成90%
     */
    String suffix() default "";

    /**
     * 当值为空时，字段的默认值
     */
    String defaultValue() default "";

    /**
     * 提示信息的内容
     */
    String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容（下拉框）
     */
    String[] combo() default {};

    /**
     *  是否导出数据,应对需求:有时我们需要导出一份模板,这时标题需要但内容需要用户手工填写
     */
    boolean isExportData() default true;

    /**
     * 另一个类中的属性名称,支持多级获取,以小数点隔开，如"对象名.对象名.属性"。
     * 如：该注解作用在User user上，User有个student类（Student student），Student有name属性，
     * 导出的时候想导出student的name属性，则targetAttr元素的值为 targetAttr = "Student.name"
     * 第一级不写（如这里的User不写，不能写成 targetAttr = "User.Student.name"）
     */
    String targetAttr() default "";

    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     */
    Type type() default Type.ALL;

    enum Type {
        /**
         * 导出导入
         */
        ALL(0),
        /**
         * 导出
         */
        EXPORT(1),

        /**
         * 导入
         */
        IMPORT(2);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    enum ColumnType {
        /**
         * 数字
         */
        NUMERIC(0),

        /**
         * 字符串
         */
        STRING(1);
        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
