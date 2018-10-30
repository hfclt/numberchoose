package com.example.testuhfapi;

import uhf.api.CommandType;
import uhf.api.Kill;
import uhf.api.Lock;
import uhf.api.ShareData;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class FilterActivity extends Activity implements OnClickListener
{

	private Spinner mSp_filter_filter;
	private ArrayAdapter<String> adapter_filter;
	private static final String[] st_filter_filter =
		  { "EPC", "TID"};
	private int int_filter=0;
	private EditText mEt_filter_pwd,mEt_filter_filter;
	
	private CheckBox mCb_M_kill_1,mCb_M_kill_2,mCb_M_acc_1,mCb_M_acc_2,
	mCb_M_epc_1,mCb_M_epc_2,mCb_M_tid_1,mCb_M_tid_2,mCb_M_user_1,mCb_M_user_2;
	
	private CheckBox mCb_A_kill_1,mCb_A_kill_2,mCb_A_acc_1,mCb_A_acc_2,
	mCb_A_epc_1,mCb_A_epc_2,mCb_A_tid_1,mCb_A_tid_2,mCb_A_user_1,mCb_A_user_2;
	
	private Button mBt_lock,mBt_kill;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filtertag);
		initFrame();
		
	}

	private void initFrame() 
	{
		// TODO Auto-generated method stub
		mSp_filter_filter=(Spinner) this.findViewById(R.id.sp_filter_filter);
		mEt_filter_pwd=(EditText) this.findViewById(R.id.et_filter_pwd);
		mEt_filter_filter=(EditText) this.findViewById(R.id.et_filter_filter);
		
		mCb_M_kill_1=(CheckBox) this.findViewById(R.id.cb_filter_killpwd_1);
		mCb_M_kill_2=(CheckBox) this.findViewById(R.id.cb_filter_killpwd_2);
		
		mCb_M_acc_1=(CheckBox) this.findViewById(R.id.cb_filter_accpwd_1);
		mCb_M_acc_2=(CheckBox) this.findViewById(R.id.cb_filter_accpwd_2);
		
		mCb_M_epc_1=(CheckBox) this.findViewById(R.id.cb_filter_epc_1);
		mCb_M_epc_2=(CheckBox) this.findViewById(R.id.cb_filter_epc_2);
		
		mCb_M_tid_1=(CheckBox) this.findViewById(R.id.cb_filter_tid_1);
		mCb_M_tid_2=(CheckBox) this.findViewById(R.id.cb_filter_tid_2);
		
		mCb_M_user_1=(CheckBox) this.findViewById(R.id.cb_filter_user_1);
		mCb_M_user_2=(CheckBox) this.findViewById(R.id.cb_filter_user_2);
		
		
		mCb_A_kill_1=(CheckBox) this.findViewById(R.id.cb_filter_killpwd_1_a);
		mCb_A_kill_2=(CheckBox) this.findViewById(R.id.cb_filter_killpwd_2_a);
		
		mCb_A_acc_1=(CheckBox) this.findViewById(R.id.cb_filter_accpwd_1_a);
		mCb_A_acc_2=(CheckBox) this.findViewById(R.id.cb_filter_accpwd_2_a);
		
		mCb_A_epc_1=(CheckBox) this.findViewById(R.id.cb_filter_epc_1_a);
		mCb_A_epc_2=(CheckBox) this.findViewById(R.id.cb_filter_epc_2_a);
		
		mCb_A_tid_1=(CheckBox) this.findViewById(R.id.cb_filter_tid_1_a);
		mCb_A_tid_2=(CheckBox) this.findViewById(R.id.cb_filter_tid_2_a);
		
		mCb_A_user_1=(CheckBox) this.findViewById(R.id.cb_filter_user_1_a);
		mCb_A_user_2=(CheckBox) this.findViewById(R.id.cb_filter_user_2_a);
		
		
		adapter_filter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, st_filter_filter);
		adapter_filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSp_filter_filter.setAdapter(adapter_filter);
		mSp_filter_filter.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
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
		
		
		mBt_lock=(Button) this.findViewById(R.id.bt_filter_lock);
		mBt_kill=(Button) this.findViewById(R.id.bt_filter_kill);
		mBt_lock.setOnClickListener(this);
		mBt_kill.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.bt_filter_lock:
			handleLock();
			break;
		case R.id.bt_filter_kill:
			handleKill();
			break;
		
		}
		
	}
	
	
	private void handleKill() 
	{
		// TODO Auto-generated method stub
		String psd = mEt_filter_pwd.getText().toString();
		if (psd.length() != 8)
		{
			//Toast.makeText(this,"输入密码必须为8位" Toast.LENGTH_SHORT);
			this.setTitle("输入密码必须为8位");
			return;
		}
		
		Kill mKill=new Kill();
		mKill.password=psd;
		mKill.FMB=int_filter;
		
		String str_filter=mEt_filter_filter.getText().toString();
		if(str_filter.equals(""))
		{
			mKill.filterData_len=0;
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
			
			mKill.filterData_len=data.length;
			mKill.filterData=data;
		}
		
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.KILL_TAGS, mKill);
			if(ret)
			{
				//Log.e(TAG, "mLock.ant_id="+mLock.ant_id);
				setTitle("ant_id"+mKill.ant_id);
			}
			else
			{
				setTitle("Kill Tag Fail");
			}
		}
		
	}

	private void handleLock() 
	{
		// TODO Auto-generated method stub
		String psd = mEt_filter_pwd.getText().toString();
		if (psd.length() != 8)
		{
			//Toast.makeText(this,"输入密码必须为8位" Toast.LENGTH_SHORT);
			this.setTitle("输入密码必须为8位");
			return;
		}
		
		Lock mLock=new Lock();
		mLock.password=psd;
		mLock.FMB=int_filter;
		
		String str_filter=mEt_filter_filter.getText().toString();
		if(str_filter.equals(""))
		{
			mLock.filterData_len=0;
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
			
			mLock.filterData_len=data.length;
			mLock.filterData=data;
			
		}
		
//		mLock.lData_Mask=_lock_mask();
//		Log.e("", "lData="+mLock.lData_Mask);
//
//		mLock.lData_Action=_lock_action();
//		Log.e("", "lData="+mLock.lData_Action);
		
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.LOCK_TAGS, mLock);
			if(ret)
			{
				//Log.e(TAG, "mLock.ant_id="+mLock.ant_id);
				setTitle("ant_id"+mLock.ant_id);
				
			}
			else
			{
				setTitle("Lock Tag Fail");
			}
		}
	}

	private int _lock_action()
	{
		// TODO Auto-generated method stub
		int shStata=0x0;
		int shTmp=0x1;
		//CheckBox[] array_action = { mCb_A_kill_1, mCb_A_kill_2,mCb_A_acc_1,mCb_A_acc_2,mCb_A_epc_1,mCb_A_epc_2,mCb_A_tid_1,mCb_A_tid_2,mCb_A_user_1,mCb_A_user_2 };
		CheckBox[] array_action = { mCb_A_user_1,mCb_A_user_2,mCb_A_tid_1,mCb_A_tid_2,mCb_A_epc_1,mCb_A_epc_2,mCb_A_acc_1,mCb_A_acc_2,mCb_A_kill_1, mCb_A_kill_2};
		for (int index = 0; index < array_action.length; index++)
		{
			if (array_action[index].isChecked() == true)
			{
				shStata+=(shTmp<<index);
			}
		}
		
		return shStata;
	}

	private int _lock_mask() 
	
	{
		// TODO Auto-generated method stub
		int shTmp=0x1;
		int shMask=0x0;
		
		//CheckBox[] array_bank = { mCb_M_kill_1, mCb_M_kill_2,mCb_M_acc_1,mCb_M_acc_2,mCb_M_epc_1,mCb_M_epc_2,mCb_M_tid_1,mCb_M_tid_2,mCb_M_user_1,mCb_M_user_2 };
		CheckBox[] array_bank = { mCb_M_user_1,mCb_M_user_2,mCb_M_tid_1,mCb_M_tid_2,mCb_M_epc_1,mCb_M_epc_2,mCb_M_acc_1,mCb_M_acc_2,mCb_M_kill_1, mCb_M_kill_2};
		//int idsMask[]={IDC_LOCK_MASK_1, IDC_LOCK_MASK_2, IDC_LOCK_MASK_3, IDC_LOCK_MASK_4, IDC_LOCK_MASK_5, IDC_LOCK_MASK_6, IDC_LOCK_MASK_7, IDC_LOCK_MASK_8, IDC_LOCK_MASK_9, IDC_LOCK_MASK_10};
		//for (int indexMask=0;indexMask<sizeof(idsMask)/sizeof(idsMask[0]);indexMask++)
		for (int index = 0; index < array_bank.length; index++)
		{
			if (array_bank[index].isChecked() == true)
			{
				shMask+=(shTmp<<index);
			}
		}
		return shMask;
	}

}
