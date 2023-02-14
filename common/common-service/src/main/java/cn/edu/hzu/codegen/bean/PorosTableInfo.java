package cn.edu.hzu.codegen.bean;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lzf
 * @date 2022-8-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PorosTableInfo extends TableInfo {
    private final Set<String> customPackages = new HashSet<>();
    private List<TableField> allFields;

    public PorosTableInfo setCustomPackages(String pkg) {
        customPackages.add(pkg);
        return this;
    }


}
