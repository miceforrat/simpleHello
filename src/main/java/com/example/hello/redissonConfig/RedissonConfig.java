package com.example.hello.redissonConfig;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.RandomAccessFile;

//@Configuration
public class RedissonConfig {

//    public static boolean modifyFileContent(String fileName, String oldstr, String newStr) {
//        RandomAccessFile raf = null;
//        try {
//            raf = new RandomAccessFile("D:\\" + fileName, "rw");
//            String line = null;
//            // 记住上一次的偏移量
//            long lastPoint = 0;
//            while ((line = raf.readLine()) != null) {
//                // 文件当前偏移量
//                final long ponit = raf.getFilePointer();
//                // 查找要替换的内容
//                if (line.contains(oldstr)) {
//                    String str = line.replace(oldstr, newStr);
//                    System.err.println(str);
//                    raf.seek(lastPoint);
//                    raf.writeBytes(str);
//                }
//                lastPoint = ponit;
//                System.err.println(lastPoint);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                raf.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return true;
//    }
    @Bean
    public RedissonClient redisson() throws IOException {
        System.err.println("Get Stage:");
        System.err.println(System.getProperty("cur_stage"));
        String toVal = System.getProperty("cur_stage");
        if (toVal == null){
            return Redisson.create(
                    Config.fromYAML(new ClassPathResource("redisson-dev.yaml").getInputStream()));
        }
        return Redisson.create(
                Config.fromYAML(new ClassPathResource("redisson-dev-test.yaml").getInputStream()));
    }
}
