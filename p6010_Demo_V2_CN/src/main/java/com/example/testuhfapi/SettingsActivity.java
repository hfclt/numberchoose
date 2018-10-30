package com.example.testuhfapi;



import uhf.api.CommandType;
import uhf.api.Frequency_region;
import uhf.api.Gen2;
import uhf.api.Multi_interval;
import uhf.api.Power;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost.TabSpec;

public class SettingsActivity extends Activity implements OnClickListener, OnSeekBarChangeListener
{

	private SeekBar mSb_read;
	private Button mBt_setpower_get,mBt_setpower_set;
	private int int_power_read=5;
	private int int_power_read_temp=5;
	private TextView mTv_power_read;
	
	private Spinner mSp_calc,mSp_startq,mSp_minq,mSp_maxq;
	private ArrayAdapter<String> adapter_calc;
	private ArrayAdapter<String> adapter_startq;
	private ArrayAdapter<String> adapter_minq;
	private ArrayAdapter<String> adapter_maxq;
	
	private int int_calc=0,int_startq=0,int_minq=0,int_maxq=0;
	private int int_calc_temp,int_startq_temp,int_minq_temp,int_maxq_temp=0;
	
	private static final String[] st_calc ={ "固定", "动态"};
	private static final String[] st_q =
		{ 
			"0","1","2","3",
			"4","5","6","7",
			"8","9","10","11",
			"12","13","14","15"
		};
	private Button mBt_gen2_get,mBt_gen2_set;
	
	
	private Spinner mSp_save,mSp_region;
	private ArrayAdapter<String> adapter_save;
	private ArrayAdapter<String> adapter_region;
	private static final String[] st_save ={ "不保存", "保存"};
	private static final String[] st_region ={ "China1", "China2","Europe", "USA","Korea", "Japan"};
	private int int_save=0,int_region,int_region_tmp;
	private Button mBt_region_get,mBt_region_set;
	
	
	private SeekBar mSb_worktime,mSb_interval;
	private EditText mEt_worktime,mEt_interval;
	private TextView mTv_worktime,mTv_interval;
	private Button mBt_timer_get,mBt_timer_set;
	private int int_worktime=0,int_interval=0;
	private int int_worktime_temp=0,int_interval_temp=0;
	
	
	private Button mBt_control_io;
	//private String cmd_open="echo 1 > /sys/devices/platform/gpio_test/uart1power";
	//private String cmd_close="echo 0 > /sys/devices/platform/gpio_test/uart1power";
	
	
	public  Sound mSound;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);  
		tabHost.setup();  
		  
		TabSpec spec1=tabHost.newTabSpec("Tab 1");  
		spec1.setContent(R.id.tab1);  
		spec1.setIndicator(null, getResources().getDrawable(R.drawable.png1));

		  
		TabSpec spec2=tabHost.newTabSpec("Tab 2");  
		spec2.setIndicator(null, getResources().getDrawable(R.drawable.png2));
		spec2.setContent(R.id.tab2);  
		
		tabHost.addTab(spec1);  
		tabHost.addTab(spec2); 
		
		
		initFrame();
		mSound=new Sound(this);
		
		handleGetPower();
		handleGetGen2();
		handleGetRegion();
		handleGetTimer();
		
		
	}

	private void initFrame() 
	{
		// TODO Auto-generated method stub
		mSb_read=(SeekBar) this.findViewById(R.id.sb_read);
		mBt_setpower_get=(Button) this.findViewById(R.id.bt_setpower_get);
		mBt_setpower_set=(Button) this.findViewById(R.id.bt_setpower_set);
		mTv_power_read=(TextView) this.findViewById(R.id.tv_set_read);

		mBt_setpower_get.setOnClickListener(this);
		mBt_setpower_set.setOnClickListener(this);
		mSb_read.setOnSeekBarChangeListener(this);
		
		
		mSp_startq=(Spinner) this.findViewById(R.id.sp_set_startq);
		adapter_startq = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_q);
		adapter_startq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_startq.setAdapter(adapter_startq);
		mSp_startq.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  int_startq_temp=arg2;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		mSp_minq=(Spinner) this.findViewById(R.id.sp_set_minq);
		adapter_minq = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_q);
		adapter_minq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_minq.setAdapter(adapter_minq);
		mSp_minq.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  int_minq_temp=arg2;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		mSp_maxq=(Spinner) this.findViewById(R.id.sp_set_maxq);
		adapter_maxq = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_q);
		adapter_maxq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_maxq.setAdapter(adapter_maxq);
		mSp_maxq.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  int_maxq_temp=arg2;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		
		mSp_calc=(Spinner) this.findViewById(R.id.sp_set_cal);
		adapter_calc = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_calc);
		adapter_calc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_calc.setAdapter(adapter_calc);
		mSp_calc.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  int_calc_temp=arg2;
        	  if(arg2==0)
        	  {
        		  mSp_maxq.setEnabled(false);
        		  mSp_minq.setEnabled(false);
        	  }
        	  else
        	  {
        		  mSp_maxq.setEnabled(true);
        		  mSp_minq.setEnabled(true);
        	  }
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		
		mBt_gen2_get=(Button) this.findViewById(R.id.bt_setgen2_get);
		mBt_gen2_set=(Button) this.findViewById(R.id.bt_setgen2_set);
		mBt_gen2_get.setOnClickListener(this);
		mBt_gen2_set.setOnClickListener(this);
		
		
		
		
		
		//////////////////////////////
		mSp_save=(Spinner) this.findViewById(R.id.sp_region_save);
		adapter_save = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_save);
		adapter_save.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_save.setAdapter(adapter_save);
		mSp_save.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  int_save=arg2;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		
		mSp_region=(Spinner) this.findViewById(R.id.sp_region_region);
		adapter_region = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_region);
		adapter_region.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_region.setAdapter(adapter_region);
		mSp_region.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  int_region_tmp=arg2+1;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		
		
		mBt_region_get=(Button) this.findViewById(R.id.bt_region_get);
		mBt_region_set=(Button) this.findViewById(R.id.bt_region_set);
		mBt_region_get.setOnClickListener(this);
		mBt_region_set.setOnClickListener(this);
		
		
		
		//////////////////////////////
		mSb_worktime=(SeekBar) this.findViewById(R.id.sb_worktime);
		mSb_interval=(SeekBar) this.findViewById(R.id.sb_inter);
		mBt_timer_get=(Button) this.findViewById(R.id.bt_timer_get);
		mBt_timer_set=(Button) this.findViewById(R.id.bt_timer_set);
		
		mEt_worktime=(EditText) this.findViewById(R.id.et_worktime);
		mEt_interval=(EditText) this.findViewById(R.id.et_inter);
		
		mEt_worktime.setSelection(mEt_worktime.getText().length());
		mEt_interval.setSelection(mEt_interval.getText().length());
		
		mEt_worktime.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// TODO Auto-generated method stub
				mEt_worktime.setSelection(s.length());	
				String str_s=""+s;
				if(!str_s.equals(""))
				mSb_worktime.setProgress(Integer.parseInt(str_s));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		mEt_interval.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// TODO Auto-generated method stub
				mEt_interval.setSelection(s.length());	
				String str_s=""+s;
				if(!str_s.equals(""))
				mSb_interval.setProgress(Integer.parseInt(str_s));
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		mTv_worktime=(TextView) this.findViewById(R.id.tv_worktime);
		mTv_interval=(TextView) this.findViewById(R.id.tv_inter);
		
		mBt_timer_get.setOnClickListener(this);
		mBt_timer_set.setOnClickListener(this);
		mSb_worktime.setOnSeekBarChangeListener(this);
		mSb_interval.setOnSeekBarChangeListener(this);
		
		////////////////////////////
		mBt_control_io=(Button) this.findViewById(R.id.bt_control_io);
		mBt_control_io.setOnClickListener(this);
		
	}

	int count=0;
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		count=0;
		switch(v.getId())
		{
		case R.id.bt_setpower_get:
			handleGetPower();
			break;
		case R.id.bt_setpower_set:
			handleSetPower();
			
			break;
		case R.id.bt_setgen2_get:
			handleGetGen2();
			break;
		case R.id.bt_setgen2_set:
			handleSetGen2();
			break;

		case R.id.bt_region_set:
			handleSetRegion();
			break;
		case R.id.bt_region_get:
			handleGetRegion();
			break;
			
		case R.id.bt_timer_set:
			handleSetTimer();
			break;
		case R.id.bt_timer_get:
			handleGetTimer();
			break;
			
		case R.id.bt_control_io:
			handleControlIO();
			break;
			
		
		
		}
		
		
	}

	private void handleControlIO() 
	{
		// TODO Auto-generated method stub
		Boolean ret=ShellUtils.checkRootPermission();
		ShellUtils.execCommand(MainActivity.close_1,ret);
		ShellUtils.execCommand(MainActivity.close_2,ret);
		ShellUtils.execCommand(MainActivity.close_3,ret);
		long timer=System.currentTimeMillis();
		while(true)
		{
			if(System.currentTimeMillis()-timer>2000)
			{
				break;
			}
		}
		ShellUtils.execCommand(MainActivity.open_1,ret);
		ShellUtils.execCommand(MainActivity.open_2,ret);
		ShellUtils.execCommand(MainActivity.open_3,ret);
		
	}

	private void handleGetTimer() 
	{
		// TODO Auto-generated method stub
		Multi_interval mMulti_interval=new Multi_interval();
		mMulti_interval.com_type=CommandType.GET_MULTI_QUERY_TAGS_INTERVAL;
		mMulti_interval.work_time=0;
		mMulti_interval.interval=0;
		
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.GET_MULTI_QUERY_TAGS_INTERVAL, mMulti_interval);
			if(ret)
			{
				this.setTitle("Get WorkTimer OK");
				int_worktime=mMulti_interval.work_time;
				mSb_worktime.setProgress(int_worktime);
				
				int_interval=mMulti_interval.interval;
				mSb_interval.setProgress(int_interval);
				
				mSound.callAlarm(true, 100);
			}
			else
			{
				
				if(count<3)
				{
					count++;
					handleGetTimer();
				}
				else
				{
					this.setTitle("Get WorkTimer Fail");
					mSb_interval.setProgress(int_interval);
					mSb_worktime.setProgress(int_worktime);
					mSound.callAlarm(false, 100);
				}
			}
		}
		
		
		
	}

	private void handleSetTimer() 
	{
		// TODO Auto-generated method stub
		Multi_interval mMulti_interval=new Multi_interval();
		mMulti_interval.com_type=CommandType.SET_MULTI_QUERY_TAGS_INTERVAL;
		mMulti_interval.work_time=int_worktime_temp;
		mMulti_interval.interval=int_interval_temp;
		
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.SET_MULTI_QUERY_TAGS_INTERVAL, mMulti_interval);
			if(ret)
			{
				this.setTitle("Set WorkTimer Ok");
				int_worktime=int_worktime_temp;
				int_interval=int_interval_temp;
				mSound.callAlarm(true, 100);
			}
			else
			{
				if(count<3)
				{
					count++;
					handleSetTimer();
				}
				else
				{
					this.setTitle("Set WorkTimer Fail");
					mSb_interval.setProgress(int_interval);
					mSb_worktime.setProgress(int_worktime);
					mSound.callAlarm(false, 100);
				}
			}
		}
		
		
	}

	private void handleGetRegion() 
	{
		// TODO Auto-generated method stub
		//GET FREQUENCY_REGION
		Frequency_region mFrequency_region=new Frequency_region(CommandType.GET_FREQUENCY_REGION,int_save,0);
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.GET_FREQUENCY_REGION, mFrequency_region);
			if(ret)
			{
				this.setTitle("Get Region Ok");	
				if(mFrequency_region.region>0)
				{
					int_region=mFrequency_region.region-1;
				}
				else
				{
				int_region=mFrequency_region.region;
				}
				mSp_region.setSelection(int_region);
				mSound.callAlarm(true, 100);
			}
			else
			{
				if(count<3)
				{
					count++;
					handleGetRegion();
				}
				else
				{
					this.setTitle("Get Region Fail");	
					mSp_region.setSelection(int_region);
					mSound.callAlarm(false, 100);
				}
			}
		}
		
	}

	private void handleSetRegion() 
	{
		// TODO Auto-generated method stub
		Frequency_region mFrequency_region=new Frequency_region(CommandType.SET_FREQUENCY_REGION,int_save,int_region_tmp);
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.SET_FREQUENCY_REGION, mFrequency_region);
			if(ret)
			{
				this.setTitle("Set Region Ok");
				int_region=int_region_tmp;
				mSound.callAlarm(true, 100);
			}
			else
			{
				
				if(count<3)
				{
					count++;
					handleSetRegion();
				}
				else
				{
					this.setTitle("set Region Fail");	
					mSp_region.setSelection(int_region);
					mSound.callAlarm(false, 100);
				}
			}
		}
		
	}

	private void handleSetGen2() 
	{
		// TODO Auto-generated method stub
		Gen2 mGen2=new Gen2(CommandType.SET_GEN2_PARAM, int_calc_temp, int_startq_temp, int_minq_temp, int_maxq_temp);
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.SET_GEN2_PARAM, mGen2);
			if(ret)
			{
				this.setTitle("Set Gen2 Ok");
				//int_closeopen=int_closeopen_temp;
				int_calc=int_calc_temp;
				int_startq=int_startq_temp;
				int_minq=int_minq_temp;
				int_maxq=int_maxq_temp;
				mSound.callAlarm(true, 100);
			}
			else
			{
				
				if(count<3)
				{
					count++;
					handleSetGen2();
				}
				else
				{
					this.setTitle("Set Gen2 Fail");	
					mSp_calc.setSelection(int_calc);
					//mSp_closeopen.setSelection(int_closeopen);
					mSp_startq.setSelection(int_startq);
					mSp_minq.setSelection(int_minq);
					mSp_maxq.setSelection(int_maxq);
					mSound.callAlarm(false, 100);
				}
				
			}
		}
		
		
	}

	private void handleGetGen2() 
	{
		// TODO Auto-generated method stub
		Gen2 mGen2=new Gen2(CommandType.GET_GEN2_PARAM, int_calc_temp, int_startq_temp, int_minq_temp, int_maxq_temp);
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.GET_GEN2_PARAM, mGen2);
			if(ret)
			{
				this.setTitle("Get Gen2 Ok");
				
				int_calc=mGen2.Q;
				mSp_calc.setSelection(int_calc);
				
				int_startq=mGen2.startQ;
				mSp_startq.setSelection(int_startq);
				
				int_minq=mGen2.MinQ;
				mSp_minq.setSelection(int_minq);
				
				int_maxq=mGen2.MaxQ;
				mSp_maxq.setSelection(int_maxq);
				mSound.callAlarm(true, 100);
				
			}
			else
			{
				
				if(count<3)
				{
					count++;
					handleGetGen2();
				}
				else
				{
				
					this.setTitle("Get Gen2 Fail");
					//mSp_closeopen.setSelection(int_closeopen);
					mSp_calc.setSelection(int_calc);
					mSp_startq.setSelection(int_startq);
					mSp_minq.setSelection(int_minq);
					mSp_maxq.setSelection(int_maxq);
					mSound.callAlarm(false, 100);
				}
				
			}
		}
	}

	private void handleGetPower() 
	{
		// TODO Auto-generated method stub
		Power mPower=new Power();
		mPower.com_type	= CommandType.GET_POWER;
		mPower.loop		= 0;
		mPower.read		= 0;
		mPower.write	= 0;
		
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.GET_POWER, mPower);
			if(ret)
			{
				
				this.setTitle("Get Power Ok");
				int_power_read=mPower.read;
				mSb_read.setProgress(int_power_read);
				mSound.callAlarm(true, 100);
				//mSb_write.setProgress(int_power_write);
				
				//int_closeopen=mPower.loop;
				//mSp_closeopen.setSelection(int_closeopen);
			}
			else
			{
				
				if(count<3)
				{
					count++;
					handleGetPower();
				}
				else
				{
					this.setTitle("Get Power Fail");
					mSb_read.setProgress(int_power_read);
					mSound.callAlarm(false, 100);
				}
					
				//mSb_write.setProgress(int_power_write);
				//mSp_closeopen.setSelection(int_closeopen);
			}
		}
		
	}

	private void handleSetPower() 
	{
		// TODO Auto-generated method stub
		Power mPower=new Power();
		mPower.com_type	= CommandType.SET_POWER;
		mPower.loop		= 0;
		mPower.read		= int_power_read_temp;
		mPower.write	= int_power_read_temp;
		
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.SET_POWER, mPower);
			if(ret)
			{
				this.setTitle("Set Power Ok");
				int_power_read=int_power_read_temp;
				mSound.callAlarm(true, 100);
				//int_power_write=int_power_write_temp;
				//int_closeopen=int_closeopen_temp;
			}
			else
			{
				
				if(count<3)
				{
					count++;
					handleSetPower();
				}
				else
				{
					this.setTitle("Set Power Fail");
					mSb_read.setProgress(int_power_read);
					mSound.callAlarm(false, 100);
				}
				//mSb_write.setProgress(int_power_write);
				//mSp_closeopen.setSelection(int_closeopen);
			}
		}
		
		
		
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) 
	{
		// TODO Auto-generated method stub
		if(seekBar==mSb_read)
		{
			if(progress<5)
			{
				seekBar.setProgress(int_power_read);
				setTitle("power read is lowest 5");
				return;
			}
			int_power_read_temp=progress;
			mTv_power_read.setText(""+int_power_read_temp+"dBm");
			
			
		}
		else if(seekBar==mSb_worktime)
		{
			int_worktime_temp=progress;
			mEt_worktime.setText(""+int_worktime_temp);
			mTv_worktime.setText(""+int_worktime_temp+"ms");
		}
		else if(seekBar==mSb_interval)
		{
			int_interval_temp=progress;
			mEt_interval.setText(""+int_interval_temp);
			mTv_interval.setText(""+int_interval_temp+"ms");
		}
		
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// TODO Auto-generated method stub
		
	}
	
	

}
