package com.wdd.library.controller;

import com.wdd.library.pojo.Category;
import com.wdd.library.service.TypeService;
import com.wdd.library.util.AjaxResult;
import org.activiti.engine.impl.util.json.JSONObject;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/type")
public class TypeController {


    @Autowired
    private TypeService typeService;


    @RequestMapping("/bookType")
    public String bookType() {
        return "/book/bookType";
    }


    @RequestMapping("/bookTypeList")
    @ResponseBody
    public String listCategory() {
        ArrayList<Category> categorylist= typeService.listCategory();
        JSONObject obj = new JSONObject();
        obj.put("code", 0);
        obj.put("msg", "");
        obj.put("count",categorylist.size());
        obj.put("data", categorylist);
        return obj.toString();
    }



    @RequestMapping("/editBookType")
    @ResponseBody
    public AjaxResult editBookType(Category category) {
        AjaxResult ajaxResult = new AjaxResult();
        try{
            typeService.updateBookType(category);
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("修改失败");
        }
        return ajaxResult;
    }

    @RequestMapping("/delBookType")
    @ResponseBody
    public AjaxResult delBookType(Integer cid) {
        AjaxResult ajaxResult = new AjaxResult();
        try{
            typeService.delBookType(cid);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("删除失败");
        }
        return ajaxResult;
    }



    @RequestMapping("/addBookType")
    @ResponseBody
    public AjaxResult addBookType(String cname) {
        AjaxResult ajaxResult = new AjaxResult();
        try{
            typeService.addBookType(cname);
            ajaxResult.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("添加失败");
        }
        return ajaxResult;
    }
}
