package com.gitee.pifeng.monitoring.ui.business.web;

import cn.hutool.core.lang.Console;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * <p>
 * MyBatis-Plus代码生成器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/6/2 15:58
 */
public class MyBatisPlusAutoGenerator {

    /**
     * <p>
     * 读取控制台输入的表名
     * </p>
     *
     * @return 输入的表名
     * @author 皮锋
     * @custom.date 2020/7/1 15:56
     */
    @SuppressWarnings("resource")
    private static String scanner() {
        Scanner scanner = new Scanner(System.in);
        Console.log("请输入表名，多个表名用英文逗号分割：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的表名，多个英文逗号分割！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + File.separator + "phoenix-ui";
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("皮锋");
        gc.setOpen(false);
        // 是否覆盖已有文件
        gc.setFileOverride(false);
        // 实体属性Swagger2注解
        gc.setSwagger2(true);
        // 自定义文件命名，注意 %s会自动填充表实体属性
        gc.setMapperName("I%sDao");
        gc.setServiceName("I%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/phoenix?useSSL=false&serverTimezone=GMT%2b8&useUnicode=true&characterEncoding=utf-8");
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.gitee.pifeng.monitoring.ui.business.web");
        pc.setEntity("entity");
        pc.setController("controller");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("dao");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        List<FileOutConfig> focList = Lists.newArrayList();
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + "/src/main/java/com/gitee/pifeng/monitoring/ui/business/web/mapper/" +
                        tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 不需要其他的类型时，直接设置为null就不会成对应的模版了
        // templateConfig.setEntity(null);
        // templateConfig.setService(null);
        // templateConfig.setController(null);
        // templateConfig.setServiceImpl(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setEntityLombokModel(true);
        strategy.setEntitySerialVersionUID(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner().split(","));
        // 驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());

        mpg.execute();
    }
}
