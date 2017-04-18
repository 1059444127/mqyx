package com.psc.pt.service.pacs;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psc.pt.dao.pacs.CaseDigMapper;
import com.psc.pt.dao.pacs.CaseListInfoMapper;
import com.psc.pt.dao.pacs.CasePacsMapper;
import com.psc.pt.model.pacs.CaseDig;
import com.psc.pt.model.pacs.CaseListInfo;
import com.psc.pt.model.pacs.CasePacs;

@Service
public class CaseService {
	@Autowired CaseListInfoMapper caseListInfoMapper;
	@Autowired CasePacsMapper casePacsMapper;
	@Autowired CaseDigMapper caseDigMapper;
	/**
	 * 插入caseListInfo
	 * @param cli
	 */
	public void addCaseListInfo(CaseListInfo cli) {
		// TODO Auto-generated method stub
		caseListInfoMapper.insertSelective(cli);
	}
	/**
	 * 插入casePacs
	 * @param cp
	 */
	public void addCasePacs(CasePacs cp) {
		// TODO Auto-generated method stub
		casePacsMapper.insertSelective(cp);
	}
	/**
	 * 根据caseId查询caseListInfo
	 * @param paramsMap
	 * @return
	 */
	public CaseListInfo selectCaseListInfoByCaseId(Long caseId) {
		// TODO Auto-generated method stub
		return caseListInfoMapper.selectByPrimaryKey(caseId);
	}
	/**
	 * 更新caseListInfo
	 * @param cli
	 */
	public void updateCaseListInfo(CaseListInfo cli) {
		// TODO Auto-generated method stub
		caseListInfoMapper.updateByPrimaryKeySelective(cli);
	}
	/**
	 * 根据caseId查询CasePacs信息
	 * @param id
	 * @return
	 */
	public CasePacs selectCasePacsByCaseId(Long id) {
		// TODO Auto-generated method stub
		return casePacsMapper.selectCasePacsByCaseId(id);
	}
	/**
	 * 列表总数
	 * @param paramsMap
	 * @return
	 */
	public Long selectListCount(Map<String, Object> paramsMap) {
		// TODO Auto-generated method stub
		return caseListInfoMapper.selectListCount(paramsMap);
	}
	/**
	 * 取列表
	 * @param paramsMap
	 * @return
	 */
	public List<CaseListInfo> selectList(Map<String, Object> paramsMap) {
		// TODO Auto-generated method stub
		return caseListInfoMapper.selectList(paramsMap);
	}
	/**
	 * 获取统计信息
	 * @param paramsMap
	 * @return
	 */
	public CaseListInfo selectAllCount(Map<String, Object> paramsMap) {
		// TODO Auto-generated method stub
		return caseListInfoMapper.selectAllCount(paramsMap);
	}
	/**
	 * 获取诊断
	 * @param parseLong
	 * @return
	 */
	public CaseDig selectCaseDigByCaseId(long parseLong) {
		// TODO Auto-generated method stub
		return caseDigMapper.selectCaseDigByCaseId(parseLong);
	}
	/**
	 * 添加诊断
	 * @param cd
	 */
	public void addCaseDig(CaseDig cd) {
		// TODO Auto-generated method stub
		caseDigMapper.insertSelective(cd);
	}

}
