package cn.edu.hzu.config;

import cn.edu.hzu.common.api.utils.UserUtils;
import cn.edu.hzu.common.entity.SsoUser;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @Author: lzf
 * @CreateTime: 2022/07/10
 * @Description: MybatisPlus配置
 */
@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 添加乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalConfig globalConfig() {
        return (new GlobalConfig()).setMetaObjectHandler(this.metaObjectHandler());
    }

    @Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            // 插入时的填充
            @Override
            public void insertFill(MetaObject metaObject) {
                SsoUser currentUser = UserUtils.getCurrentUser();
                this.strictInsertFill(metaObject, "deleteFlag", Integer.class, 0);//逻辑删除
                this.strictInsertFill(metaObject, "version", Integer.class, 1);//乐观锁机制
                // 还没配置登录人的信息等 先默认
                this.setFieldValByName("createBy", currentUser.getRealName(), metaObject);//创建人
                this.setFieldValByName("createId", currentUser.getEngName(), metaObject);//创建人ID
                this.setFieldValByName("updateBy", currentUser.getRealName(), metaObject);//更新人
                this.setFieldValByName("updateId", currentUser.getEngName(), metaObject);//更新人ID

                this.setFieldValByName("createTime", new Date(), metaObject);
                this.setFieldValByName("updateTime", new Date(), metaObject);
            }

            // 更新时的填充
            @Override
            public void updateFill(MetaObject metaObject) {
                SsoUser currentUser = UserUtils.getCurrentUser();
                this.setFieldValByName("updateBy", currentUser.getRealName(), metaObject);//更新人
                this.setFieldValByName("updateId", currentUser.getEngName(), metaObject);//更新人ID

                this.setFieldValByName("updateTime", new Date(), metaObject);
            }
        };
    }
}
