package com.wdd.library.service.impl;

import com.wdd.library.dao.BookMapper;
import com.wdd.library.dao.LendInfoMapper;
import com.wdd.library.pojo.LendInfo;
import com.wdd.library.service.LendInfoSerivce;
import com.wdd.library.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LendInfoSerivceImpl implements LendInfoSerivce {

    @Autowired
    private LendInfoMapper lendInfoMapper;
    @Autowired
    private BookMapper bookMapper;

    @Override 
    public PageBean<LendInfo> queryLeadInfoPage(Map<String, Object> paramMap) {
        PageBean<LendInfo> pageBean = new PageBean<LendInfo>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<LendInfo> datas = lendInfoMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = lendInfoMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    @Override
    public void backBook(Map<String, Object> ret) {
        lendInfoMapper.backbook(ret);
    }

    @Override
    public boolean isLended(LendInfo lendInfo) {
        return lendInfoMapper.isLended(lendInfo)>0 ? true:false;
    }

    @Override
    public Integer cardState(Integer reader_id) {
        return lendInfoMapper.cardState(reader_id);
    }

    @Override
    public void lendBook(LendInfo lendInfo) {

        Date date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        lendInfo.setLend_date(sdf.format(date));

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        cal.add(Calendar.DATE, 28);

        Date newdate=cal.getTime();
        lendInfo.setBack_date(sdf.format(newdate));

        lendInfo.setFine(0.00);
        lendInfoMapper.addLead(lendInfo);

        bookMapper.reduceStock(lendInfo.getBook_id());
    }
}
