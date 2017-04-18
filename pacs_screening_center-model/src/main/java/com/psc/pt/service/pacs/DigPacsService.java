package com.psc.pt.service.pacs;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psc.pt.dao.pacs.DigPacsMapper;
import com.psc.pt.model.pacs.DigPacs;


@Service
public class DigPacsService {
	private final static Logger LOG = Logger.getLogger(DigPacsService.class);
	@Autowired DigPacsMapper digPacsMapper;

	/**
	 * 新增
	 * @param dp
	 */
	public void addDigPacs(DigPacs dp) {
		// TODO Auto-generated method stub
		digPacsMapper.insertSelective(dp);
	}

	/**
	 * 获取节点
	 * @param paramsMap
	 * @return
	 */
	public List<DigPacs> getDigPacs(Map<String, Object> paramsMap) {
		// TODO Auto-generated method stub
		return digPacsMapper.getDigPacs(paramsMap);
	}

	/**
	 * 获取报告内容
	 * @param paramsMap
	 * @return
	 */
	public DigPacs getDigPacsDetails(Long id) {
		// TODO Auto-generated method stub
		return digPacsMapper.selectByPrimaryKey(id);
	}


}
