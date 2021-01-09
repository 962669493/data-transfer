package zz;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import zz.service.DataTransferService;

/**
 * 应用入口
 *
 * @author zhangzheng
 * @date 2020-10-28 17:21
 **/
@SpringBootApplication
public class Main implements CommandLineRunner, ApplicationContextAware {
    public static void main(String[] args) {
        new SpringApplication(Main.class).run(args);
    }

    private static ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        applicationContext.getBean(DataTransferService.class).export();
        //applicationContext.getBean(DataTransferService.class).export1();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Main.applicationContext = applicationContext;
    }
}
