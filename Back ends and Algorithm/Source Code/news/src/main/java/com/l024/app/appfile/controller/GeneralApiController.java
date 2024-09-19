package com.l024.app.appfile.controller;

import china.mobile.common.demo.TtsHttpClient;
import cn.hutool.core.codec.Base64;
import com.l024.app.appfile.entity.History;
import com.l024.app.appfile.entity.News;
import com.l024.app.appfile.entity.resp.RestBean;
import com.l024.app.appfile.repo.NewsRepository;
import com.l024.app.appfile.service.ExportNewsService;
import com.l024.app.appfile.service.HistoryService;
import com.l024.app.appfile.service.NewsService;
import com.l024.app.appfile.utils.*;
import image.ItemRecognition;
import image.TextRecognition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.l024.app.appfile.service.Impl.NewsServiceImpl.getFileByByte;

@RestController
@RequestMapping("/api/general")
public class GeneralApiController {
    @Value("${path.baseUrl}")
    private String baseUrl;
    @Value("${path.url}")
    private String url;
    private final HistoryService historyService;

    public GeneralApiController(HistoryService historyService) {
        this.historyService = historyService;
    }

    //通用的文字转语音——物体识别，文字识别,参数为文字，返回bytes字符数组
    //也可以生成一些提示语音
    @RequestMapping("/ttsText")
    public RestBean<String> TtsText(String text) throws Exception {
        byte[] bytes= TtsHttpClient.TtsCall(text);
        String filetoName = FileUtil.filetoName();
        //将byte数组转换文件
        String filename =  getFileByByte(bytes,url,filetoName);
        return new RestBean<>(200,"请求成功！",baseUrl+filename);
    }

    //物体识别，参数为图片BASE64，返回识别出的数个特征词
    @RequestMapping("/itemRec")
    public RestBean<String> itemRec(String image){
        String result= ItemRecognition.ItemRecognition(image);
        return new RestBean<>(200,"识别成功！",result);
    }

    //文字识别，参数为图片BASE64，返回识别出的文字
    @RequestMapping("/textRec")
    public RestBean<String> textRec(@RequestParam(value = "file") MultipartFile file,@RequestParam(value = "uid")String uid) throws IOException {
        String decode = Base64.encode(file.getBytes());
        String result= TextRecognition.textGeneral(decode);
        History history = new History();
        history.setTypeName("拍照识别");
        history.setType(String.valueOf(3));
        history.setUid(Integer.valueOf(uid));
        history.setDetail(result);
        history.setCreateTime(new Date());
        historyService.saveHistory(history);
        return new RestBean<>(200,"识别成功！",result);
    }

    //街景识别，参数为图片BASE64
    @RequestMapping("/streetRec")
    public RestBean<String> streetRec(String image) throws IOException {
        String itemResult = ItemRecognition.ItemRecognition(image);
        String itemResult2 = "";
        String[] itemArray=itemResult.split(" ");
        if(itemArray.length<10){
            itemResult2=itemResult;
        }else{
            for (int i = 0; i < 10; i++) {
                itemResult2=itemResult2+itemArray[i]+" ";
            }
        }
        String textResult= TextRecognition.textGeneral(image);
        String translateResult = TranslateClient.Translate(textResult, "encn");
        String result;
        //英文转中文，如果原本就是中文，结果和原字符是相等的，返回textResult
        //如果不相等，说明原本是英文，返回英文和翻译值
        if(textResult.equals(translateResult)){
            result=itemResult2+"文字内容： "+textResult;
        }else{
            result=itemResult2+"文字内容： "+textResult+" "+translateResult;
        }

        return new RestBean<>(200, "识别成功！", result);
    }

    //图片描述
    @RequestMapping("/caption")
    public RestBean<String> getCaption(String image) throws IOException {
        String path="D:\\ImageCaption\\";
        String name=FileUtil.filetoName()+".jpg";
        ImageUtil.generateImage(image,path+name);
        System.out.println("python " +
                "D:\\PycharmProject\\a-PyTorch-Tutorial-to-Image-Captioning-master\\caption.py --img="+path+name);
        String result= PyUtil.GetPyFile("python " +
                "D:\\PycharmProject\\a-PyTorch-Tutorial-to-Image-Captioning-master\\caption.py --img="+path+name);
        //String result= PyUtil.GetPyFile("python " +
        //        "D:\\PycharmProject\\a-PyTorch-Tutorial-to-Image-Captioning-master\\t724.py -i=5");
        System.out.println("result"+result);
        result=result.replace("<start>","");
        result=result.replace("<end>","");
        String cnResult=TranslateClient.Translate(result,"encn");
        System.out.println("CNresult"+cnResult);
//        ImageUtil.delFile(path+name);
        return new RestBean<>(200, "识别成功！", cnResult);
    }

    @Resource
    NewsRepository newsRepository;
    @Resource
    NewsService newsService;
    @RequestMapping("/sound")
    public RestBean<String> GenerateSound() throws Exception {
        List<News> newsList=newsRepository.findAll();
        for (int i = 0; i < newsList.size(); i++) {
            News news=newsList.get(i);
            if(!SoundFileSearch.SoundSearch((news.getId()+".mp3"))){
                //没找到，根据id生成音频
                newsService.GetTtsNews(news.getId());
            }

        }

        return new RestBean<>(200, "音频生成成功！", null);
    }


}

