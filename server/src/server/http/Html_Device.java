package server.http;

import common.Log;

import server.mj.ServerTimer;
import business.CountDao;
import business.UserDao;
import business.conut.Sts_MJDevice;
import business.entity.MJ_Device;

/**
 * 设备ID
 * @author xue
 *
 */
public class Html_Device implements IHtml{

	@Override
	public String getHtml(String content) {

		String result = "";	
		int deviceId =  0;
		int version = 0;
		String[] contents = content.split("&");
		
		for(int i = 0 ; i < contents.length; i++)
		{
			if(contents[i].startsWith("device"))
			{
				String[] tmp = contents[i].split("=");
				 deviceId =  Integer.parseInt(tmp[1]);
			}else if(contents[i].startsWith("version"))
			{
				String[] tmp = contents[i].split("=");
				version = Integer.parseInt(tmp[1]);
			}
		}
		Log.info("Html_Device : " + deviceId + "   version:" + version );
		
		CountDao cdao = new CountDao();
		Sts_MJDevice sts_device = cdao.findTodayDevice();
		
		
		UserDao udao  = new UserDao();
		
		MJ_Device device = null;
		if(deviceId == 0)
		{
			//生成新的设备号
			device = new MJ_Device();
			device.setRegTime(ServerTimer.getNowString());
			device.setLastLoginTime(ServerTimer.getNowString());
			device.setOpenTimes(1);
			udao.saveObject(device);
			device.setDeviceId(device.getId() + 10000);
			
			sts_device.setDuliDevice(sts_device.getDuliDevice() + 1);
			sts_device.setNewAdd(sts_device.getNewAdd() + 1);
			sts_device.setOpenDevice(sts_device.getOpenDevice() + 1);
			
		}else
		{
			device = udao.findDevice(deviceId);
			if(device != null)
			{
				
				String lastLogin = device.getLastLoginTime().substring(0, 10);
				device.setLastLoginTime(ServerTimer.getNowString());
				device.setOpenTimes(device.getOpenTimes() + 1);
				udao.saveObject(device);
				
				if(!ServerTimer.getDay().equals(lastLogin))sts_device.setDuliDevice(sts_device.getDuliDevice() + 1);
				sts_device.setOpenDevice(sts_device.getOpenDevice() + 1);
			}else 
			{
				device = new MJ_Device();
				device.setRegTime(ServerTimer.getNowString());
				device.setLastLoginTime(ServerTimer.getNowString());
				device.setOpenTimes(1);
				udao.saveObject(device);
				device.setDeviceId(device.getId() + 10000);
				
				sts_device.setDuliDevice(sts_device.getDuliDevice() + 1);
				sts_device.setNewAdd(sts_device.getNewAdd() + 1);
				sts_device.setOpenDevice(sts_device.getOpenDevice() + 1);
			}
		}
		cdao.saveSts_Object(sts_device);
		cdao.saveSts_Object(device);
		Log.info("返回设备ID：" + device.getDeviceId());
		return device.getDeviceId() + "";
	}

}
