package com.wdd.library.controller;

import com.wdd.library.pojo.Admin;
import com.wdd.library.pojo.LendInfo;
import com.wdd.library.pojo.Reader;
import com.wdd.library.service.AdminService;
import com.wdd.library.service.LendInfoSerivce;
import com.wdd.library.service.ReaderService;
import com.wdd.library.util.AjaxResult;
import com.wdd.library.util.Const;
import com.wdd.library.util.PageBean;
import com.wdd.library.util.StringUtil;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class commonController {

    @Autowired
    private ReaderService readerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private LendInfoSerivce lendInfoSerivce;


    @RequestMapping("/toLogin")
    public String login(){
        return "login";
    }


    @RequestMapping("/toRegister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping("/checkReader")
    @ResponseBody
    public AjaxResult checkreader(Integer reader_id) {
        AjaxResult ajaxResult = new AjaxResult();
        int count=readerService.checkReader(reader_id);
        if(count==0){
            ajaxResult.setSuccess(true);
        }else{
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("当前学号已被注册!");
        }
        return ajaxResult;
    }


    @RequestMapping("/submitAddReader")
    @ResponseBody
    public AjaxResult submitAddreader(Reader reader) {
        AjaxResult ajaxResult = new AjaxResult();
        try{
            readerService.addReader(reader);
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("注册成功");
        }catch(Exception e){
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("注册失败");
        }
        return ajaxResult;
    }


    @RequestMapping("/doLogin")
    @ResponseBody
    public AjaxResult list(String name, String password, String access, HttpSession session){
        AjaxResult ajaxResult = new AjaxResult();
        try{
            if(access.equals("0")){

                Admin ad = new Admin();
                ad.setName(name);
                ad.setPassword(Integer.parseInt(password));
                Admin admin = adminService.select(ad);
                if(admin != null){
                    session.setAttribute(Const.ADMIN,admin);
                    ajaxResult.setStatus("0");
                }else{
                    ajaxResult.setStatus("2");
                    ajaxResult.setMessage("用户名或密码错误");
                }
            }else{

                Reader rd = new Reader();
                rd.setReader_id(Integer.parseInt(name));
                rd.setPassword(Integer.parseInt(password));
                Reader reader = readerService.select(rd);
                if(reader != null){
                    session.setAttribute(Const.READER,reader);
                    ajaxResult.setStatus("1");
                }else{
                    ajaxResult.setStatus("2");
                    ajaxResult.setMessage("用户名或密码错误");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setStatus("3");
            ajaxResult.setMessage("服务器异常,请改天登录");
        }

        return ajaxResult;
    }


    @RequestMapping("/loginout")
    public String loginout(HttpSession session){
        session.invalidate();
        return "login";
    }

    @RequestMapping("/listDisBackAdmin")
    public  String listDisBackAdmin() {
        return "listDisBackAdmin";
    }


    @RequestMapping("/listDisBack")
    public  String listDisBack() {
        return "listDisBack";
    }

    @RequestMapping("/listDisBackBook")
    @ResponseBody
    public String listDisBackBook(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                                  @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                                  @RequestParam(value = "power",defaultValue = "0") Integer power,
                                  String rname,String bname,String state){

        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);
        if(StringUtil.isNotEmpty(bname))  paramMap.put("bname",bname);
        if(StringUtil.isNotEmpty(rname))  paramMap.put("rname",rname);
        if(StringUtil.isNotEmpty(state))  paramMap.put("state",state);

        if (power.equals(0)){

        }
        PageBean<LendInfo> pageBean = lendInfoSerivce.queryLeadInfoPage(paramMap);
        JSONObject obj = new JSONObject();

        obj.put("code", 0);
        obj.put("msg", "");
        obj.put("count",pageBean.getTotalsize());
        obj.put("data", pageBean.getDatas());
        return obj.toString();
    }

    @RequestMapping(value = "/backBook")
    @ResponseBody
    public AjaxResult backBook(@RequestParam(value = "reader_id" , defaultValue = "1") Integer reader_id,
                                         @RequestParam(value = "book_id" , defaultValue = "1") Integer book_id) {

        AjaxResult ajaxResult = new AjaxResult();
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("reader_id",reader_id);
        ret.put("book_id",book_id);
        try{
            lendInfoSerivce.backBook(ret);
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("已归还");
        }catch (Exception e){
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("归还失败");
        }
        return ajaxResult;
    }

    @RequestMapping("/toAlterpwdPage")
    public String toAlterpwdPage(String state, Model model) {
        model.addAttribute("state",state);
        return "alterPwd";
    }

    @RequestMapping("/checkPwd")
    @ResponseBody
    public AjaxResult checkPwd(Integer password,String state,HttpSession session){
        AjaxResult ajaxResult = new AjaxResult();
        if (state.equals("0")){
            Admin admin = (Admin)session.getAttribute(Const.ADMIN);
            if(admin.getPassword().equals(password)){
                ajaxResult.setSuccess(true);
            }else{
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("原密码错误");
            }
        }else{
            Reader reader = (Reader)session.getAttribute(Const.READER);
            if(reader.getPassword().equals(password)){
                ajaxResult.setSuccess(true);
            }else{
                ajaxResult.setSuccess(false);
                ajaxResult.setMessage("原密码错误");
            }
        }

        return ajaxResult;
    }


    @RequestMapping("/alterpwd")
    @ResponseBody
    public AjaxResult alterpwd(Integer password,String state,HttpSession session){
        AjaxResult ajaxResult = new AjaxResult();

        try{
            if(state.equals("0")){
                Admin admin = (Admin)session.getAttribute(Const.ADMIN);
                admin.setPassword(password);
                adminService.alterpwd(admin);
            }else{
                Reader reader = (Reader)session.getAttribute(Const.READER);
                reader.setPassword(password);
                readerService.alterpwd(reader);
            }
            ajaxResult.setSuccess(true);
            ajaxResult.setMessage("更改密码成功");
        }catch(Exception e){
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("更改密码失败");

        }
        return ajaxResult;
    }

}
