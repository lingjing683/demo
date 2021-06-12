package com.example.demo;

import com.example.demo.util.ExcelReaderUtils;
import com.example.demo.util.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockMultipartFile;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource(value= {"classpath:/application.yml"},encoding="UTF-8")
@AutoConfigureMockMvc
        //need this in Spring Boot test
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    /**
     * 测试单文件上传
     */
    @Test
    @DisplayName("测试上传单个文件")
    public void whenUploadSingleFileSuccess() throws Exception{
        File file = new File("C:/Users/LENOVO/Desktop/生产工序表.xlsx");
        //File file = new File("C:/Users/LENOVO/Desktop/1.txt");

        MultipartFile mulFile = new MockMultipartFile(
                "生产工序表.xlsx", //文件名
                "生产工序表.xlsx", //originalName 相当于上传文件在客户机上的文件名
                "multipart/form-data",
                new FileInputStream(file) //文件流
        );
        String result= FileUtils.upload(mulFile);
        System.out.println(result);
    }

    /**
     * 测试文件下载
     */
    @Test
    @DisplayName("测试下载文件")
    void downExcel() throws Exception{
        mockMvc.perform(get("/file/download")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult mvcResult) throws Exception {
                        //保存为文件
                        File file  = new File("D:/download/生产工序表.xlsx");
                        file.delete();
                        FileOutputStream fout = new FileOutputStream(file);
                        ByteArrayInputStream bin = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray());
                        StreamUtils.copy(bin,fout);
                        fout.close();
                        System.out.println("is exist:"+file.exists());
                        //assert
                        System.out.println("file length:"+file.length());
                    }
                });
    }

    /**
     * 测试Excel文件读取
     */

    @Test
    @DisplayName("测试Excel文件读取")
    void readExcel(){
        String path = "C:/Users/LENOVO/Desktop/生产工序表.xlsx";
        ExcelReaderUtils reader = new ExcelReaderUtils(path);
        List<List<String>> list = reader.readExcel();
        System.out.println("-----------开始读取Excel文件------------");
        for (int i = 0; i < list.size(); i++) {
            List<String> context = list.get(i);
            for (int j = 0; j < context.size(); j++) {
                System.out.println(context.get(j)+"  ");
            }
        }
        System.out.println("-----------读取结束-----------");

        String outPath = "D:/ExcelOut";
        reader.writeFile(list,outPath);
    }
}
