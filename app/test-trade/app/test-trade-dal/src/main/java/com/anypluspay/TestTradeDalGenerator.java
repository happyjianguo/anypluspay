package com.anypluspay;


import cn.hutool.setting.yaml.YamlUtil;
import com.anypluspay.component.generator.DalGenerator;
import com.anypluspay.component.generator.config.GenConfig;

import java.io.File;

/**
 * mybatis自动生成代码
 */
public class TestTradeDalGenerator {

    public static void main(String[] args) {
        GenConfig genConfig = YamlUtil.loadByPath("config.yaml", GenConfig.class);
        DalGenerator dalGenerator = new DalGenerator(genConfig);
        File f = new File(TestTradeDalGenerator.class.getResource("/").getPath());
        String projectPath = f.getParentFile().getParentFile().getParentFile().getParentFile().getPath() + "/app/test-trade-dal";
        dalGenerator.execute(projectPath, "");
    }
}
