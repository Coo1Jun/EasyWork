package cn.edu.hzu.codegen.utils;


import cn.edu.hzu.codegen.bean.CodeGenProp;
import cn.edu.hzu.codegen.bean.PorosGenerator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lzf
 * @date 2022-8-12
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CodeGenUtils {

    private CodeGenProp codeGenProp;

    public void init() throws IOException {
        InputStream asStream = CodeGenUtils.class.getClassLoader().getResourceAsStream("gen-info.json");
        String genJsonStr = IOUtils.toString(asStream);
        JSONObject genJson = (JSONObject) JSONObject.parse(genJsonStr);
        codeGenProp.setAuthor(genJson.getString("author"));
        codeGenProp.setModuleName(genJson.getString("moduleName"));
        codeGenProp.setParentPackageName(genJson.getString("parentPackage"));
        codeGenProp.setFunctionName(genJson.getString("functionName"));
        codeGenProp.setServiceName(genJson.getString("serviceName"));
        codeGenProp.setBusinessName(genJson.getString("businessName"));
        codeGenProp.setJdbcUrl(genJson.getString("jdbcUrl"));
        codeGenProp.setJdbcUsername(genJson.getString("jdbcUsername"));
        codeGenProp.setJdbcPassword(genJson.getString("jdbcPassword"));
        codeGenProp.setJdbcDriverClass(genJson.getString("jdbcDriver"));
        codeGenProp.setJdbcSchema(genJson.getString("jdbcSchema"));
        codeGenProp.setTables(genJson.getString("tables"));

        String idType = genJson.getString("idType");
        if (StringUtils.isNotEmpty(idType)){
            codeGenProp.setIdType(idType);
        }else{
            codeGenProp.setIdType(IdType.AUTO.name());
        }

        String tablePrex = genJson.getString("tablePrex");
        if (StringUtils.isNoneBlank(tablePrex)){
            codeGenProp.setTablePrex(tablePrex);
        }

        boolean tenantMode = genJson.getBooleanValue("tenantMode");
        codeGenProp.setTenantMode(tenantMode);

        InputStream tableIs = CodeGenUtils.class.getClassLoader().getResourceAsStream("gen-table.json");
        String tableJson = IOUtils.toString(tableIs);

        JSONObject genTableJson = (JSONObject) JSONObject.parse(tableJson);
        if (null != genTableJson.getJSONArray("queryFields")){
            codeGenProp.setQueryFieldList(genTableJson.getJSONArray("queryFields").toArray(new String[]{}));
        }

        if (null != genTableJson.getJSONArray("insertFields")){
            codeGenProp.setAddFieldList(genTableJson.getJSONArray("insertFields").toArray(new String[]{}));
        }
        if (null != genTableJson.getJSONArray("updateFields")){
            codeGenProp.setEditFieldList(genTableJson.getJSONArray("updateFields").toArray(new String[]{}));
        }

        if (null != genTableJson.getJSONArray("resultFields")){
            codeGenProp.setResultFieldList(genTableJson.getJSONArray("resultFields").toArray(new String[]{}));
        }

        if (null != genTableJson.getJSONArray("requiredFields")){
            codeGenProp.setRequiredFieldList(genTableJson.getJSONArray("requiredFields").toArray(new String[]{}));
        }

        if (null != genTableJson.getJSONObject("columnLables")){
            codeGenProp.setColumnLabels(JSONObject.parseObject(genTableJson.getJSONObject("columnLables").toJSONString(), new TypeReference<Map<String, String>>(){}));
        }

    }

    public void codeGen(CodeGenProp codeGenProp) throws IOException{
        log.info("codegen properties:{}", JSON.toJSONString(codeGenProp));
        PorosGenerator mpg = new PorosGenerator();
        mpg.setCodeGenProp(codeGenProp);
        // 全局配置
        GlobalConfig gc = getGlobalConfig(codeGenProp.getProjectPath(),codeGenProp);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = getDataSourceConfig(codeGenProp);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // pc.setModuleName(MODULE_NAME);
        pc.setParent(codeGenProp.getParentPackageName());
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = getInjectionConfig(codeGenProp.getProjectPath(),codeGenProp);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setEntity("templates/entity.java");
        templateConfig.setController("templates/controller.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = getStrategyConfig(codeGenProp);

        mpg.setStrategy(strategy);
        mpg.execute();


    }



    public static void generator() throws IOException {
        CodeGenUtils codeGen =new CodeGenUtils();
        codeGen.setCodeGenProp(new CodeGenProp());
        codeGen.init();
        // 代码生成器
        PorosGenerator mpg = new PorosGenerator();
        mpg.setCodeGenProp(codeGen.getCodeGenProp());
        String projectPath = System.getProperty("user.dir");

        // 全局配置
        GlobalConfig gc = codeGen.getGlobalConfig(projectPath,codeGen.getCodeGenProp());
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = codeGen.getDataSourceConfig(codeGen.getCodeGenProp());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // pc.setModuleName(MODULE_NAME);
        pc.setParent(codeGen.getCodeGenProp().getParentPackageName());
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = codeGen.getInjectionConfig(projectPath,codeGen.getCodeGenProp());
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setEntity("templates/entity.java");
        templateConfig.setController("templates/controller.java");
        templateConfig.setService("templates/service.java");
        templateConfig.setServiceImpl("templates/serviceImpl.java");

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = codeGen.getStrategyConfig(codeGen.getCodeGenProp());

        mpg.setStrategy(strategy);
        mpg.execute();
    }


    private StrategyConfig getStrategyConfig(CodeGenProp codeGenProp) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityTableFieldAnnotationEnable(true);

        if (codeGenProp.isTenantMode()){
//            strategy.setSuperEntityClass("cn.getech.poros.framework.common.bean.TenantEntity");
            strategy.setSuperEntityColumns(new String[]{"tenant_id","remark","create_by","update_by","create_time","update_time"});
        }else{
            strategy.setSuperEntityClass("cn.edu.hzu.common.entity.BaseEntity");
            strategy.setSuperEntityColumns(new String[]{"remark","create_by","update_by","create_time","update_time"});
        }


        strategy.setSuperServiceClass("cn.edu.hzu.common.service.IBaseService");
        strategy.setSuperServiceImplClass("cn.edu.hzu.common.service.impl.BaseServiceImpl");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(codeGenProp.getTables());
        strategy.setControllerMappingHyphenStyle(true);
        if (StringUtils.isNotEmpty(codeGenProp.getTablePrex())){
            strategy.setTablePrefix(codeGenProp.getTablePrex());
        }

        return strategy;
    }

    private InjectionConfig getInjectionConfig(String projectPath,CodeGenProp codeGenProp) {

        InjectionConfig cfg = new InjectionConfig() {

            @Override
            public void initMap() {
                String prex = StringUtils.isNotBlank(codeGenProp.getTablePrex())?codeGenProp.getTablePrex(): null;
                log.info("table:{},prex:{}",codeGenProp.getTables(),prex);
                String tableNameAfterPrex = codeGenProp.getTables();
                if (StringUtils.isNotBlank(prex)){
                    tableNameAfterPrex = NamingStrategy.removePrefix(codeGenProp.getTables(), prex);
                }

                String camelTableName = underlineToCamel(tableNameAfterPrex);
                String pascalTableName = underlineToPascal(tableNameAfterPrex);
                String colonTableName = underlineToColon(tableNameAfterPrex);

                Map<String, Object> map = new HashMap<>();

                map.put("queryFieldList" , codeGenProp.getQueryFieldList());
                map.put("addFieldList" , codeGenProp.getAddFieldList());
                map.put("editFieldList" , codeGenProp.getEditFieldList());
                map.put("resultFieldList" , codeGenProp.getResultFieldList());
                map.put("requiredFieldList" , codeGenProp.getRequiredFieldList());
                map.put("columnLabels" , codeGenProp.getColumnLabels());

                map.put("queryParamType" , pascalTableName + "QueryParam");
                map.put("addParamType" , pascalTableName + "AddParam");
                map.put("editParamType" , pascalTableName + "EditParam");
                map.put("dtoType" , pascalTableName + "Dto");

                map.put("queryParamName" , camelTableName + "QueryParam");
                map.put("addParamName" , camelTableName + "AddParam");
                map.put("editParamName" , camelTableName + "EditParam");
                map.put("dtoName" , camelTableName + "Dto");


                String queryParamPackage = codeGenProp.getParentPackageName() + StringPool.DOT + "dto";
                map.put("queryParamPackage" , queryParamPackage);

                // 查询参数类路径
                map.put("queryParamPath" , queryParamPackage + StringPool.DOT + pascalTableName + "QueryParam");

                // 新增参数类路径
                map.put("addParamPath" , queryParamPackage + StringPool.DOT + pascalTableName + "AddParam");

                // 编辑参数类路径
                map.put("editParamPath" , queryParamPackage + StringPool.DOT + pascalTableName + "EditParam");

                // 参数Mapper类路径
                map.put("paramMapperPath" , queryParamPackage + StringPool.DOT + pascalTableName + "ParamMapper");

                // dto类路径
                map.put("dtoPath" , queryParamPackage + StringPool.DOT + pascalTableName + "Dto");

                map.put("camelTableName" , camelTableName);
                map.put("pascalTableName" , pascalTableName);
                // 冒号连接的表名称
                map.put("colonTableName" , colonTableName);
                map.put("functionName" , codeGenProp.getFunctionName());
                map.put("businessName" , codeGenProp.getBusinessName());
                map.put("serviceName" , codeGenProp.getServiceName());
                this.setMap(map);
            }
        };


        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        //生成mapper.xml
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/" + codeGenProp.getModuleName() + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        focList.add(new FileOutConfig("/templates/queryParam.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/" + codeGenProp.getModuleName() + "/src/main/java/" + StringUtils.replaceAll(codeGenProp.getParentPackageName(), "\\." , "/") + "/dto/" + tableInfo.getEntityName() + "QueryParam" + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/templates/addParam.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return projectPath + "/" + codeGenProp.getModuleName() + "/src/main/java/" + StringUtils.replaceAll(codeGenProp.getParentPackageName(), "\\." , "/") + "/dto/" + tableInfo.getEntityName() + "AddParam" + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/templates/editParam.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/" + codeGenProp.getModuleName() + "/src/main/java/" + StringUtils.replaceAll(codeGenProp.getParentPackageName(), "\\." , "/") + "/dto/" + tableInfo.getEntityName() + "EditParam" + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/templates/paramMapper.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/" + codeGenProp.getModuleName() + "/src/main/java/" + StringUtils.replaceAll(codeGenProp.getParentPackageName(), "\\." , "/") + "/dto/" + tableInfo.getEntityName() + "ParamMapper" + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/templates/resultDto.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/" + codeGenProp.getModuleName() + "/src/main/java/" + StringUtils.replaceAll(codeGenProp.getParentPackageName(), "\\." , "/") + "/dto/" + tableInfo.getEntityName() + "Dto" + StringPool.DOT_JAVA;
            }
        });

//        focList.add(new FileOutConfig("/templates/index.vue.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return projectPath + "/" + codeGenProp.getModuleName() + "/views/vue/"  + tableInfo.getEntityName() + ".vue";
//            }
//        });

        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    private DataSourceConfig getDataSourceConfig(CodeGenProp codeGenProp) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(codeGenProp.getJdbcUrl());
        if (StringUtils.isNoneBlank(codeGenProp.getJdbcSchema())){
            dsc.setSchemaName(codeGenProp.getJdbcSchema());
        }

        dsc.setDriverName(codeGenProp.getJdbcDriverClass());
        dsc.setUsername(codeGenProp.getJdbcUsername());
        dsc.setPassword(codeGenProp.getJdbcPassword());
        return dsc;
    }

    private GlobalConfig getGlobalConfig(String projectPath,CodeGenProp codeGenProp) {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(projectPath + "/" + codeGenProp.getModuleName() + "/src/main/java");
        gc.setAuthor(codeGenProp.getAuthor());
        gc.setOpen(false);
        gc.setFileOverride(true);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setSwagger2(true);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setIdType(IdType.valueOf(codeGenProp.getIdType()));
        return gc;
    }


    /**
     * 下划线转换成冒号连接命名
     * sys_user --> sys:user
     *
     * @param underline
     * @return
     */
    public static String underlineToColon(String underline) {
        if (StringUtils.isNotBlank(underline)) {
            if (StringUtils.startsWith(underline, "_")) {
                underline = StringUtils.substring(underline, 1);
            }
            String string = underline.toLowerCase();
            return string.replaceAll("_" , ":");
        }
        return null;
    }

    /**
     * 下划线转换成帕斯卡命名
     * sys_user --> SysUser
     *
     * @param underline
     * @return
     */
    public static String underlineToPascal(String underline) {
        if (StringUtils.isNotBlank(underline)) {
            return NamingStrategy.capitalFirst(NamingStrategy.underlineToCamel(underline));
        }
        return null;
    }

    /**
     * 下划线专程驼峰命名
     * sys_user --> sysUser
     *
     * @param underline
     * @return
     */
    public static String underlineToCamel(String underline) {
        if (StringUtils.isNotBlank(underline)) {
            return NamingStrategy.underlineToCamel(underline);
        }
        return null;
    }

    public static void main(String[] args) {
        String s = underlineToCamel(NamingStrategy.removePrefix("app_ad", ""));

        System.out.println(s);
    }
}
