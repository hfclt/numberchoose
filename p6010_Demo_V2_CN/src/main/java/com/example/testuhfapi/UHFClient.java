package com.example.testuhfapi;


import jni.Linuxc;
import uhf.api.UHF;
public class UHFClient 
{

	public static UHF mUHF;
	private static UHFClient instance = null;
	
	
	/**
	 * 单子模式，防止重复使用串口
	 * @return
	 */
	public static UHFClient getInstance() 
	{
		if (instance == null) 
		{
			//mUHF=new UHF("/dev/ttyMT0",Linuxc.BAUD_RATE_115200,1,0);
			//mUHF=new UHF("/dev/ttySAC3",Linuxc.BAUD_RATE_115200,1,0);
			//mUHF=new UHF("/dev/ttyMSM2",Linuxc.BAUD_RATE_115200,1,0);//0 2
			//mUHF=new UHF("/dev/ttyHSL0",Linuxc.BAUD_RATE_115200,1,0);
			
			//mUHF=new UHF("/dev/ttyS5",Linuxc.BAUD_RATE_115200,1,0);//0 2
			  mUHF=new UHF("/dev/ttyHSL1",Linuxc.BAUD_RATE_115200,1,0);
			
			mUHF.com_fd=mUHF.transfer_open(mUHF);
			if(mUHF.com_fd>0)
			{
				instance=new UHFClient();
			}
		}
		return instance;
	}
	
	
	public static void Disconnect()
	{
		if(instance!=null)
		{
			if(mUHF!=null)
			{
				mUHF.transfer_close(mUHF);
				mUHF=null;
			}
			instance=null;
		}
	}
	
	
	

}
