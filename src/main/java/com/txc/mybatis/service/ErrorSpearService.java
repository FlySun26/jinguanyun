package com.txc.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txc.mybatis.bean.ErrorSpear;
import com.txc.mybatis.bean.SpearParam;
import com.txc.mybatis.mapper.ErrorSpearMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ErrorSpearService
 * @Description TODO
 * @Date 2023/8/23 08:44
 * @Vertion 1.0
 **/
@Service
@Slf4j
public class ErrorSpearService extends ServiceImpl<ErrorSpearMapper, ErrorSpear> {

    public void saveErrorCode(int devAddr, SpearParam spearParam, byte i2) {
        List<ErrorSpear> list = this.list(new QueryWrapper<ErrorSpear>().lambda()
                .eq(ErrorSpear::getDevAddr, devAddr)
                .eq(ErrorSpear::getSpearCode, (int) i2)
                .eq(ErrorSpear::getErrorCode, spearParam.getParam1())
        );
        if (CollectionUtils.isEmpty(list)) {
            ErrorSpear errorSpear = new ErrorSpear();
            errorSpear.setDevAddr(devAddr);
            errorSpear.setStatus(spearParam.getChargeStatus());
            errorSpear.setErrorCode(spearParam.getParam1());
            errorSpear.setParam1(spearParam.getParam2());
            errorSpear.setParam2(spearParam.getParam3());
            errorSpear.setSpearCode((int) i2);
            this.save(errorSpear);
        } else {
            ErrorSpear errorSpear = list.stream().findFirst().orElse(null);
            errorSpear.setStatus(spearParam.getChargeStatus());
            errorSpear.setErrorCode(spearParam.getParam1());
            errorSpear.setParam1(spearParam.getParam2());
            errorSpear.setParam2(spearParam.getParam3());
            this.updateById(errorSpear);
        }

    }
}
