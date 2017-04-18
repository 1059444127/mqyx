package com.psc.pt.thread;

import java.util.Date;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.psc.pt.constants.ConstantsInterior;
import com.psc.pt.model.pacs.CaseListInfo;
import com.psc.pt.model.pacs.CasePacs;
import com.psc.pt.service.pacs.CaseService;

/**
 * 抢单违约倒计时
 * @author YQ
 *
 */
public class CaseInvalidThread extends Thread {
	private Logger LOG = Logger.getLogger(CaseInvalidThread.class);
	private CaseService caseService;
	private CaseListInfo cli;
	private MemcachedClient memcachedClient;
	private boolean timePass = false;
	
	public CaseInvalidThread(CaseService caseService, CaseListInfo cli, MemcachedClient memcachedClient){
		this. caseService = caseService;
		this.cli = cli;
		this.memcachedClient = memcachedClient;
	}
	
	/*用于跳出计时*/
	public void setTimePass(boolean bool){
		this.timePass = bool;
	}
	
	//方法重写
	@Override
	public void run(){
		while(!timePass){
			if(cli.getMissingTime() <= new Date().getTime()){
				break;
			}
			try {
				Thread.sleep(1000);
				//LOG.info("CaseInvalidThread------"+this.getName()+"睡眠-----");
			} catch (InterruptedException e) {
				LOG.info("CaseInvalidThread------"+this.getName()+"睡眠失败");
			}
		}
		LOG.info("CaseInvalidThread------"+this.getName()+"睡眠结束-----");
		//验证是否依然已抢待诊断状态
		CaseListInfo cliNow = caseService.selectCaseListInfoByCaseId(cli.getId());
		CasePacs cp = caseService.selectCasePacsByCaseId(cliNow.getId());
		if("1".equals(cliNow.getStatus())){
			//设置已违约
			cliNow.setStatus("3");
			caseService.updateCaseListInfo(cliNow);
			//生成新的可抢case，放入db与cache
			cliNow.setId(null);
			cliNow.setStatus("0");
			cliNow.setDoctorId(null);
			caseService.addCaseListInfo(cliNow);
			cp.setCaseId(cliNow.getId());
			caseService.addCasePacs(cp);
			try {
				memcachedClient.set(ConstantsInterior.CASE_PACS + cliNow.getId(), 0, cliNow);
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MemcachedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
