package org.activiti.explorer.junit;

/**
 * Created by cyrus on 2017/2/8.
 */
import org.junit.Test;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
public class Testactiviti {
    //使用配置文件创建数据库表
            @Test
public void createTable() {

                ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml") //
                .buildProcessEngine();
    }
}



