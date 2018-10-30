package com.example.testuhfapi;

import uhf.api.CommandType;
import uhf.api.ShareData;
import uhf.api.Tags_data;
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
import android.widget.Spinner;
import android.widget.TextView;

public class AccessTagActivity extends Activity implements OnClickListener
{

	private Spinner mSp_acc_filter,mSp_acc_bank,mSp_acc_offset,mSp_acc_length,mSp_acc_operate;
	
	private EditText mEt_acc_pwd,mEt_acc_filter;
	private TextView mTv_acc_filter_count;
	
	private EditText mEt_acc_write_read;
	private Button mBt_acc_write,mBt_acc_read;
	
	private ArrayAdapter<String> adapter_filter;
	private ArrayAdapter<String> adapter_bank;
	private ArrayAdapter<String> adapter_offset;
	private ArrayAdapter<String> adapter_length;
	private ArrayAdapter<String> adapter_operate;
	
	private static final String[] st_acc_filter =
		  { "EPC", "TID"};
	
	private static final String[] st_acc_bank =
		  { "RFU", "EPC","TID","USER"};
	
	private static final String[] st_acc_offset =
		  { 
		"0","1", "2","3","4","5", "6","7","8","9",
		"10","11", "12","13","14","15", "16","17","18","19",
		"20","21", "22","23","24","25", "26","27","28","29",
		"30","31", "32","33","34","35", "36","37","38","39",
		"40","41", "42","43","44","45", "46","47","48","49",
		"50","51", "52","53","54","55", "56","57","58","59",
		"60","61", "62","63","64","65", "66","67","68","69",
		"70","71", "72","73","74","75", "76","77","78","79",
		"80","81", "82","83","84","85", "86","87","88","89",
		"90","91", "92","93","94","95", "96","97","98","99",
		"100","101", "102","103","104","105", "106","107","108","109",
		"110","111", "112","113","114","115", "116","117","118","119",
		"120","121", "122","123","124","125", "126","127","128"	
		};
	
	private static final String[] st_acc_length =
		  { 
		"1", "2","3","4","5", "6","7","8","9",
		"10","11", "12","13","14","15", "16","17","18","19",
		"20","21", "22","23","24","25", "26","27","28","29",
		"30","31", "32","33","34","35", "36","37","38","39",
		"40","41", "42","43","44","45", "46","47","48","49",
		"50","51", "52","53","54","55", "56","57","58","59",
		"60","61", "62","63","64","65", "66","67","68","69",
		"70","71", "72","73","74","75", "76","77","78","79",
		"80","81", "82","83","84","85", "86","87","88","89",
		"90","91", "92","93","94","95", "96","97","98","99",
		"100","101", "102","103","104","105", "106","107","108","109",
		"110","111", "112","113","114","115", "116","117","118","119",
		"120","121", "122","123","124","125", "126","127","128"	
		};
	
	private static final String[] st_acc_operate =
		  { "read","write"};
	private int int_filter=0,int_bank,int_offset,int_length;
	public  Sound mSound;
	
	private int count_filter=0;
	private TextView mTv_acc_data_count;
	private int count_data=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accesstag);
		initFrame();
		mSound=new Sound(this);
		
	}

	private void initFrame() 
	{
		// TODO Auto-generated method stub
		
		mSp_acc_filter=(Spinner) this.findViewById(R.id.sp_acc_filter);
		mSp_acc_bank=(Spinner) this.findViewById(R.id.sp_acc_bank);
		mSp_acc_offset=(Spinner) this.findViewById(R.id.sp_acc_offset);
		mSp_acc_length=(Spinner) this.findViewById(R.id.sp_acc_length);
		mSp_acc_operate=(Spinner) this.findViewById(R.id.sp_acc_oper);
		
		mEt_acc_pwd=(EditText) this.findViewById(R.id.et_acc_pwd);
		mEt_acc_filter=(EditText) this.findViewById(R.id.et_acc_filter);
		mTv_acc_filter_count=(TextView) this.findViewById(R.id.tv_acc_fileter_count);
		mTv_acc_data_count=(TextView) this.findViewById(R.id.tv_acc_data_count);
		
		mEt_acc_filter.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// TODO Auto-generated method stub
				
				if (count >= 1) 
				{
					if ((s.length() + 1) % 3 == 0) 
					{
						s=s + " ";
						mEt_acc_filter.setText(s);
						mEt_acc_filter.setSelection(s.length());
					}
				}  
				else if (count == 0) 
				{
					if (s.length() > 0 ) 
					{
						if((s.length() ) % 3 != 0)
						{
						s=s.subSequence(0, s.length() - 2);
						mEt_acc_filter.setText(s);
						mEt_acc_filter.setSelection(s.length());
						}
					}
					else if (s.length() == 0)
					{
						s="";
						mEt_acc_filter.setSelection(s.length());
					}
				}
				
				
				if(s.length()<=0)
				{
					mTv_acc_filter_count.setText("长度=0");
					count_filter=0;
				}
				else if(s.length()>0&&s.length()<=2)
				{
					count_filter=1;
					mTv_acc_filter_count.setText("长度=0");
				}
				else
				{
					//mTv_acc_filter_count.setText("Len="+(int)((s.length() + 1)/3));
					count_filter=(int)((s.length() + 1)/3);
					mTv_acc_filter_count.setText("长度="+count_filter/2);
				}
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
		
		
		mEt_acc_write_read=(EditText) this.findViewById(R.id.et_acc_write_read);
		mEt_acc_write_read.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// TODO Auto-generated method stub
				
				if (count >= 1) 
				{
					if ((s.length() + 1) % 3 == 0) 
					{
						s=s + " ";
						mEt_acc_write_read.setText(s);
						mEt_acc_write_read.setSelection(s.length());
					}
				}  
				else if (count == 0) 
				{
					if (s.length() > 0 ) 
					{
						if((s.length() ) % 3 != 0)
						{
						s=s.subSequence(0, s.length() - 2);
						mEt_acc_write_read.setText(s);
						mEt_acc_write_read.setSelection(s.length());
						}
					}
					else if (s.length() == 0)
					{
						s="";
						mEt_acc_write_read.setSelection(s.length());
					}
				}
				
				
				if(s.length()<=0)
				{
					mTv_acc_data_count.setText("长度=0");
					count_data=0;
				}
				else if(s.length()>0&&s.length()<=2)
				{
					//mTv_acc_data_count.setText("Len=1");
					count_data=1;
					mTv_acc_data_count.setText("长度=0");
				}
				else
				{
					//mTv_acc_data_count.setText("Len="+(int)((s.length() + 1)/3));
					count_data=(int)((s.length() + 1)/3);
					mTv_acc_data_count.setText("长度="+count_data/2);
				}
				
				
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
		
		
		
		mBt_acc_read=(Button) this.findViewById(R.id.bt_acc_read);
		mBt_acc_write=(Button) this.findViewById(R.id.bt_acc_write);
		
		mBt_acc_read.setOnClickListener(this);
		mBt_acc_write.setOnClickListener(this);
		
		
		
		adapter_operate = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_acc_operate);
		adapter_operate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_acc_operate.setAdapter(adapter_operate);
		mSp_acc_operate.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  
        	  if(arg2==0)
        	  {
        		  //mEt_acc_write_read.setEnabled(false);
        		  //mBt_acc_write.setEnabled(false);
        		  //mBt_acc_read.setEnabled(true);
        	  }
        	  else
        	  {
        		  //mEt_acc_write_read.setEnabled(true);
        		  //mBt_acc_write.setEnabled(true);
        		  //mBt_acc_read.setEnabled(false);
        	  }
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		
		
		adapter_filter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_acc_filter);
		adapter_filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_acc_filter.setAdapter(adapter_filter);
		mSp_acc_filter.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  
        	  int_filter=arg2;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		
		
		adapter_bank = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_acc_bank);
		adapter_bank.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_acc_bank.setAdapter(adapter_bank);
		mSp_acc_bank.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  
        	  int_bank=arg2;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		
		adapter_offset = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_acc_offset);
		adapter_offset.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_acc_offset.setAdapter(adapter_offset);
		mSp_acc_offset.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  int_offset=arg2;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });
		
		adapter_length = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_acc_length);
		adapter_length.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_acc_length.setAdapter(adapter_length);
		mSp_acc_length.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
        	
          public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
        	  
        	  int_length=arg2+1;
          }

          public void onNothingSelected(AdapterView<?> arg0)
          {
            // TODO Auto-generated method stub
          }

        });

	
		
	}

	
	int count=0;
	@Override
	public void onClick(View v)
	{
		count=0;
		// TODO Auto-generated method stub
		if(v==mBt_acc_read)
		{
			handleAccRead();
		}
		else if(v==mBt_acc_write)
		{
			handleAccWrite();
		}
		
		
	}

	private void handleAccWrite() 
	{
		// TODO Auto-generated method stub

		String psd = mEt_acc_pwd.getText().toString();
		if (psd.length() != 8)
		{
			//Toast.makeText(this,"输入密码必须为8位" Toast.LENGTH_SHORT);
			this.setTitle("输入密码必须为8位");
			return;
		}
		
		Tags_data mTags_data=new Tags_data();
		mTags_data.password=psd;
		mTags_data.FMB=int_filter;
		
		String str_filter=mEt_acc_filter.getText().toString();
		if(str_filter.equals(""))
		{
			mTags_data.filterData_len=0;
			//mTags_data.filterData=new char[]{0x11,0x22,0x33,0x44,0x55,0x66};
		}
		else
		{
			String[] strArray=str_filter.split(" ");
			char[] data = new char[strArray.length];
			if(!ShareData.StringToChar(str_filter, data,strArray.length))
			{
				this.setTitle("請輸入十六進制數據，以空格間隔，例如：00 11 22 33 44");
				return;
			}
			
			
			if(count_filter%2!=0)
			{
				this.setTitle("Filter Hex number must be multiples of 4");
				return;
			}
			
			mTags_data.filterData_len=data.length;
			mTags_data.filterData=data;
			
		}
		
		
		mTags_data.start_addr=int_offset;
		mTags_data.data_len=int_length;
		mTags_data.mem_bank=int_bank;
		
		
		String str_data=mEt_acc_write_read.getText().toString();
		if(str_data.equals(""))
		{
			mTags_data.filterData_len=0;
			//mTags_data.filterData=new char[]{0x11,0x22,0x33,0x44,0x55,0x66};
			this.setTitle("输入的长度不对，请输入大于等于"+mTags_data.data_len);
			return;
		}
		else
		{
			String[] strArray=str_data.split(" ");
			char[] data = new char[strArray.length];
			if(!ShareData.StringToChar(str_data, data,strArray.length))
			{
				this.setTitle("請輸入十六進制數據，以空格間隔，例如：00 11 22 33 44");
				return;
			}
			
			if(mTags_data.data_len*2>data.length)
			{
				this.setTitle("输入的长度不对，请输入大于等于"+mTags_data.data_len);
				return;
			}
			
			if(count_data%2!=0||((count_data/2)!=int_length))
			{
				this.setTitle("Write Data Hex number must be multiples of 4 and = Length");
				return;
			}
			
			
			mTags_data.data=data;
			
		}
		
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.WRITE_TAGS_DATA, mTags_data);
			if(ret)
			{
				setTitle("Write TAG OK");
				mSound.callAlarm(true, 100);
			}
			else
			{
				
				if(count<3)
				{
					count++;
					handleAccWrite();
				}
				else
				{
					setTitle("Write TAG Fail");
					mSound.callAlarm(false, 100);
				}
			}
		}

	
		
	}

	private void handleAccRead() 
	{
		// TODO Auto-generated method stub


		String psd = mEt_acc_pwd.getText().toString();
		if (psd.length() != 8)
		{
			//Toast.makeText(this,"输入密码必须为8位" Toast.LENGTH_SHORT);
			this.setTitle("输入密码必须为8位");
			return;
		}
		
		Tags_data mTags_data=new Tags_data();
		mTags_data.password=psd;
		mTags_data.FMB=int_filter;
		
		String str_filter=mEt_acc_filter.getText().toString();
		if(str_filter.equals(""))
		{
			mTags_data.filterData_len=0;
			//mTags_data.filterData=new char[]{0x11,0x22,0x33,0x44,0x55,0x66};
		}
		else
		{
			String[] strArray=str_filter.split(" ");
			char[] data = new char[strArray.length];
			if(!ShareData.StringToChar(str_filter, data,strArray.length))
			{
				this.setTitle("請輸入十六進制數據，以空格間隔，例如：00 11 22 33 44");
				return;
			}
			
			if(count_filter%2!=0)
			{
				this.setTitle("Filter Hex number must be multiples of 4");
				return;
			}
			
			mTags_data.filterData_len=data.length;
			mTags_data.filterData=data;
			
		}
		mTags_data.start_addr=int_offset;
		mTags_data.data_len=int_length;
		mTags_data.mem_bank=int_bank;
		
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.READ_TAGS_DATA, mTags_data);
			if(ret)
			{
				//显示
				setTitle("Read TAG OK");
				String str=ShareData.CharToString(mTags_data.data, mTags_data.data.length);
				mEt_acc_write_read.setText(str);
				mSound.callAlarm(true, 100);
				
			}
			else
			{
				
				if(count<3)
				{
					count++;
					handleAccRead();
				}
				else
				{
					setTitle("Read TAG Fail");
					mSound.callAlarm(false, 100);
				}
				
			}
		}
	}

}
