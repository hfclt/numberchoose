package com.example.testuhfapi;


import uhf.api.CommandType;
import uhf.api.Ware;

import com.example.testuhfapi.ShellUtils.CommandResult;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener
{

	private Button mBt_inventor,mBt_accessTag,mBt_filter,mBt_settings;
	private TextView minfos=null;
	

	//public static String open_1="echo on >/proc/usb_dc_en";
	//public static String open_2="echo on >/proc/vbat_en";
	//public static String open_3="echo on >/proc/gpio127_ctl";
	
	//public static String close_3="echo off >/proc/usb_dc_en";
	//public static String close_2="echo off >/proc/vbat_en";
	//public static String close_1="echo off >/proc/gpio127_ctl";
	
	
	//public static String open="echo 1 > /sys/devices/soc.0/xt_dev.67/xt_uart_3v3_en";
	//public static String open1="echo 1 >/sys/devices/soc.0/xt_dev.67/xt_vbat_out_en";
	//public static String open2="echo 1 >/sys/devices/soc.0/xt_dev.67/xt_out_5v_en";
	
	//public static String close="echo 0 > /sys/devices/soc.0/xt_dev.67/xt_uart_3v3_en";
	//public static String close1="echo 0 >/sys/devices/soc.0/xt_dev.67/xt_vbat_out_en";
	//public static String close2="echo 0 >/sys/devices/soc.0/xt_dev.67/xt_out_5v_en";
	
	//山东信通
	public static String open_1="echo 1 > /sys/devices/soc.0/xt_dev.68/xt_uhf_en";
    public static String open_2="echo 0 >/sys/devices/soc.0/xt_dev.68/xt_uart_a";
	public static String open_3="echo 1 >/sys/devices/soc.0/xt_dev.68/xt_uart_b";
		
    public static String close_1="echo 0 > /sys/devices/soc.0/xt_dev.68/xt_uhf_en";
    public static String close_2="echo 0 >/sys/devices/soc.0/xt_dev.68/xt_uart_a";
	public static String close_3="echo 0 >/sys/devices/soc.0/xt_dev.68/xt_uart_b";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initFrame();
		//打开GPIO
		Boolean ret=ShellUtils.checkRootPermission();
		Log.e("TAG", "ret="+ret);
		ShellUtils.execCommand(open_1,ret);
		ShellUtils.execCommand(open_2,ret);
		ShellUtils.execCommand(open_3,ret);
		
		
		//ShellUtils.execCommand(open,ret);
		//ShellUtils.execCommand(open1,ret);
		//ShellUtils.execCommand(open2,ret);
		Handler handler = new Handler();  
		handler.postDelayed(new Runnable()
		{   
		    public void run() 
		    { 				
				Ware mWare=new Ware(CommandType.GET_FIRMWARE_VERSION, 0, 0, 0);
				int count=0;
				while(true)
				{
					UHFClient info=UHFClient.getInstance();
					if(info!=null)
					{
						Boolean rett=UHFClient.mUHF.command(CommandType.GET_FIRMWARE_VERSION, mWare);
						if(rett)
						{
							Log.e("TAG", "Ver."+mWare.major_version+"."+mWare.minor_version+"."+mWare.revision_version);

							setTitle("P6010 V2 设备获取成功");
							minfos.setText("Ver."+mWare.major_version+"."+mWare.minor_version+"."+mWare.revision_version);
							break;
						}
					}
					
					count++;
					if(count>5)
					{
						setTitle("P6010 V2  连接失败，请重启设备");
						break;
					}
				}
			}
		}, 300);
	}
	private void initFrame() 
	{
		// TODO Auto-generated method stub
		mBt_inventor=(Button) this.findViewById(R.id.bt_inventor);
		mBt_accessTag=(Button) this.findViewById(R.id.bt_accesstag);
		mBt_filter=(Button) this.findViewById(R.id.bt_filter);
		mBt_settings=(Button) this.findViewById(R.id.bt_settings);
		
		mBt_inventor.setOnClickListener(this);
		mBt_accessTag.setOnClickListener(this);
		mBt_filter.setOnClickListener(this);
		mBt_settings.setOnClickListener(this);
		minfos=(TextView) this.findViewById(R.id.infos);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		Intent intent;
		switch(v.getId())
		{
		case R.id.bt_inventor:
			intent=new Intent(MainActivity.this,InventorActivity.class);
			startActivityForResult(intent, R.id.bt_inventor);
			break;
		case R.id.bt_accesstag:
			intent=new Intent(MainActivity.this,AccessTagActivity.class);
			startActivityForResult(intent, R.id.bt_accesstag);
			break;
		case R.id.bt_filter:
			intent=new Intent(MainActivity.this,FilterActivity.class);
			startActivityForResult(intent, R.id.bt_filter);
			break;
		case R.id.bt_settings:
			intent=new Intent(MainActivity.this,SettingsActivity.class);
			startActivityForResult(intent, R.id.bt_settings);
			break;
		
		}
		
		
	}
	
	
	protected void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		 finish();
		 //关闭GPIO
		//打开GPIO
		Boolean ret=ShellUtils.checkRootPermission();
		Log.e("TAG", "ret="+ret);
		ShellUtils.execCommand(close_1,ret);
		ShellUtils.execCommand(close_2,ret);
		ShellUtils.execCommand(close_3,ret);
		
		//ShellUtils.execCommand(close,ret);
		//ShellUtils.execCommand(close1,ret);
		//ShellUtils.execCommand(close2,ret);
		
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	// 按键捕获
		public boolean onKeyDown(int keyCode, KeyEvent event) 
		{
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				exit();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}

		// 退出系统提示
		private void exit() {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("提示").setMessage("是否退出系统?").setCancelable(true)
					.setIcon(R.drawable.ic_launcher).setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									finish();
								}
							}).setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

	
	
	

}
