//package com.sun.content.common.script;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
//import com.baomidou.mybatisplus.generator.config.rules.DateType;
//import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
//import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class PlusGenerator {
//
//
//    public static void main(String[] args) {
//        PlusGenerator.generatorByType("sunshilong",
//                "D:\\workspace\\pig-thinktank\\pig-smart\\pig-smart-elastic\\src\\main\\java",
//        "com.sun.content",
//        "vnc_head_event"
//        );
//    }
//
//
//    /**
//     * @param author  作者
//     * @param packageName 包名
//     * @param tableName 表名
//     */
//    public static void generatorByType(String author,String outputDir, String packageName, String ... tableName) {
//        AutoGenerator mpg = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
////        System.out.println("D:\\lianen-ecerp\\lianen-main\\src\\main\\java\\");
//        gc.setOutputDir(outputDir);
//        gc.setFileOverride(true);
//        // ActiveRecord特性
//        gc.setActiveRecord(true);
//        // XML 二级缓存
//        gc.setEnableCache(false);
//        //XML ResultMap
//        gc.setBaseResultMap(true);
//        //XML columList
//        gc.setBaseColumnList(true);
//        gc.setDateType(DateType.ONLY_DATE);
//        gc.setServiceName("%sService");			//service 命名方式   默认值：null 例如：%sBusiness 生成 UserBusiness
////        gc.setServiceImplName("%sServiceImpl");	//service impl 命名方式  默认值：null 例如：%sBusinessImpl 生成 UserBusinessImpl
//
//        if(StringUtils.isBlank(author)){
//            author = "sunshilong";
//        }
//        gc.setAuthor(author);
//
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setDbType(DbType.MYSQL);
//        dsc.setTypeConvert(new OracleTypeConvert(){
//            // 自定义数据库表字段类型转换【可选】
//            @Override
//            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
//                //tinyint转换成Boolean
//                if(fieldType.toLowerCase().contains( "bigint" ) ){
//                    return DbColumnType.LONG;
//                }
//                if(fieldType.toLowerCase().contains( "decimal" ) ){
//                    return DbColumnType.BIG_DECIMAL;
//                }
//                //将数据库中datetime转换成date
//                if ( fieldType.toLowerCase().contains( "datetime" ) ) {
//                    return DbColumnType.DATE;
//                }
//                if(fieldType.toLowerCase().contains( "int" ) || fieldType.toLowerCase().contains( "tinyint" )){
//                    return DbColumnType.INTEGER;
//                }
//                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
//                return super.processTypeConvert(gc, fieldType);
//            }
//        });
//        dsc.setUrl("jdbc:mysql://10.60.22.139:3306/pms_dev?characterEncoding=utf8&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai");
//        dsc.setUsername("pms_rw");
//        dsc.setPassword("d1m_pms_dev");
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//        mpg.setDataSource(dsc);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
//        //strategy.setTablePrefix(new String[] { "tlog_", "tsys_" });// 此处可以修改为您的表前缀
//        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
//        strategy.setInclude(tableName); // 需要生成的表
//        mpg.setStrategy(strategy);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent(packageName);
//        pc.setController("controller");
//        pc.setEntity("entity");
//        pc.setMapper("mapper");
//        mpg.setPackageInfo(pc);
//
//        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
//        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
//        TemplateConfig tc = new TemplateConfig();
//        tc.setController("/vm/controller.java.vm");
//        tc.setService("/vm/service.java.vm");
////        tc.setServiceImpl("/vm/serviceImpl.java.vm");
//        tc.setEntity("/vm/entity.java.vm");
//        tc.setMapper("/vm/mapper.java.vm");
//        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
//        mpg.setTemplate(tc);
//
//        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                Map<String, Object> map = new HashMap<>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                this.setMap(map);
//            }
//        };
//        mpg.setCfg(cfg);
//
//        // 执行生成
//        mpg.execute();
//
//        // 打印注入设置【可无】
//        System.err.println("代码生成成功！！");
//    }
//
//}