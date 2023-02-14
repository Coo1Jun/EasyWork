package cn.edu.hzu.codegen.bean;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.*;

/**
 * @author lzf
 * @date 2022-8-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PorosGenerator extends AutoGenerator {

    private CodeGenProp codeGenProp;

    /**
     * 预处理配置
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     */
    @Override
    protected ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
        /*
         * 注入自定义配置
         */
        if (null != injectionConfig) {
            injectionConfig.initMap();
            config.setInjectionConfig(injectionConfig);
        }
        /*
         * 表信息列表
         */
        List<PorosTableInfo> porosTableInfos = getPorosTableInfoList();

        for (PorosTableInfo tableInfo : porosTableInfos) {
            /* ---------- 添加导入包 ---------- */
            if (config.getGlobalConfig().isActiveRecord()) {
                // 开启 ActiveRecord 模式
                tableInfo.setImportPackages(Model.class.getCanonicalName());
            }
            if (tableInfo.isConvert()) {
                // 表注解
                tableInfo.setImportPackages(TableName.class.getCanonicalName());
            }
            if (config.getStrategyConfig().getLogicDeleteFieldName() != null && tableInfo.isLogicDelete(config.getStrategyConfig().getLogicDeleteFieldName())) {
                // 逻辑删除注解
                tableInfo.setImportPackages(TableLogic.class.getCanonicalName());
            }
            if (StringUtils.isNotBlank(config.getStrategyConfig().getVersionFieldName())) {
                // 乐观锁注解
                tableInfo.setImportPackages(Version.class.getCanonicalName());
            }
            boolean importSerializable = true;
            if (StringUtils.isNotBlank(config.getStrategyConfig().getSuperEntityClass())) {
                // 父实体
                tableInfo.setImportPackages(config.getStrategyConfig().getSuperEntityClass());
                importSerializable = false;
            }
            if (config.getGlobalConfig().isActiveRecord()) {
                importSerializable = true;
            }
            if (importSerializable) {
                tableInfo.setImportPackages(Serializable.class.getCanonicalName());
            }
            // Boolean类型is前缀处理
            if (config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()
                    && CollectionUtils.isNotEmpty(tableInfo.getFields())) {
                tableInfo.getFields().stream().filter(field -> "boolean".equalsIgnoreCase(field.getPropertyType()))
                        .filter(field -> field.getPropertyName().startsWith("is"))
                        .forEach(field -> {
                            field.setConvert(true);
                            field.setPropertyName(StringUtils.removePrefixAfterPrefixToLower(field.getPropertyName(), 2));
                        });
            }

            Map<String,String> labelMap = codeGenProp.getColumnLabels();
            // custom field comment
            if (CollectionUtils.isNotEmpty(tableInfo.getAllFields()) && null != labelMap){
                Set<String> labelKeys = labelMap.keySet();

                tableInfo.getAllFields().stream()
                        .filter(field -> labelKeys.contains(field.getName()) && StringUtils.isNotBlank(labelMap.get(field.getName())))
                        .forEach(field -> {
                            field.setComment(labelMap.get(field.getName()));

                        });
            }
            // custom packages

            if (CollectionUtils.isNotEmpty(tableInfo.getAllFields())){
                tableInfo.getAllFields().stream()
                        .forEach(field -> {
                            if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
                                tableInfo.setCustomPackages(field.getColumnType().getPkg());
                            }
                        });
            }
        }
        return config.setTableInfoList(Collections.unmodifiableList(porosTableInfos));
    }

    private List<PorosTableInfo> getPorosTableInfoList(){
        List<TableInfo> tableList = this.getAllTableInfoList(config);
        List<PorosTableInfo> porosTableInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tableList)){
            tableList.forEach(tableInfo -> {
                PorosTableInfo porosTableInfo = new PorosTableInfo();
                porosTableInfo.setName(tableInfo.getName());
                porosTableInfo.setServiceName(tableInfo.getServiceName());
                porosTableInfo.setComment(tableInfo.getComment());
                porosTableInfo.setCommonFields(tableInfo.getCommonFields());
                porosTableInfo.setFields(tableInfo.getFields());
                porosTableInfo.setControllerName(tableInfo.getControllerName());
                porosTableInfo.setEntityName(tableInfo.getEntityName());
                porosTableInfo.setConvert(tableInfo.isConvert());
                porosTableInfo.setFieldNames(tableInfo.getFieldNames());
                porosTableInfo.setMapperName(tableInfo.getMapperName());
                porosTableInfo.setXmlName(tableInfo.getXmlName());
                porosTableInfo.setServiceImplName(tableInfo.getServiceImplName());
                List<TableField> fields = tableInfo.getFields();
                List<TableField> commonFields = tableInfo.getCommonFields();
                List<TableField> allFields = new ArrayList<>();
                allFields.addAll(fields);
                allFields.addAll(commonFields);
                porosTableInfo.setAllFields(allFields);

                porosTableInfos.add(porosTableInfo);
            });
        }
        return porosTableInfos;
    }
}
