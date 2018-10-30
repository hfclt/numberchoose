package com.example.testuhfapi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uhf.api.CommandType;
import uhf.api.MultiLableCallBack;
import uhf.api.Multi_query_epc;
import uhf.api.Query_epc;
import uhf.api.ShareData;

import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

public class InventorActivity extends Activity implements MultiLableCallBack
{

	private static final String TAG = "MainActivity";
	public  Sound mSound;
	
	private ListView receptionLV;
	private TextView countTXT;
	private SimpleAdapter recptionSimpleAdapter;
	private ArrayList<Map<String, String>> receptionArrayList = new ArrayList<Map<String, String>>();
	private final String lvAdptrlabData = "Data";
	private String name="";
	private String barcode="";
	private final String lvAdptrlabRssi = "Rssi";
	private final String lvAdptrlabTimes = "Times";
	private CheckBox mCb_sound=null;
	
	private static Boolean isStart=false;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventory);
		initView();
		mSound=new Sound(this);
		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			
		}
		
	}
	
	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		
		if(isStart)
		Test((Button) findViewById(R.id.CycleBtn));
	}
	
	
	private void initView() 
	{
		receptionLV = (ListView) findViewById(R.id.lvReception);
		
		recptionSimpleAdapter = new SimpleAdapter(this, receptionArrayList, R.layout.activity_main_recodes_element,
				new String[] { lvAdptrlabData, lvAdptrlabRssi,lvAdptrlabTimes }, new int[] {
						R.id.idActivityMain_RecodesElement_tvData,R.id.idActivityMain_RecodesElement_rssi, R.id.idActivityMain_RecodesElement_tvTimes });
		receptionLV.setAdapter(recptionSimpleAdapter);
		
		countTXT = (TextView) findViewById(R.id.countTXT);
				

		
		
		
		//停止连续寻标签
		Button stopBtn = (Button) findViewById(R.id.StopBtn);
		stopBtn.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				UHFClient info=UHFClient.getInstance();
				if(info!=null)
				{
					Boolean ret=UHFClient.mUHF.command(CommandType.STOP_MULTI_QUERY_TAGS_EPC, null);
					if(ret)
					{
						if(mCb_sound.isChecked())
						mSound.callAlarm(true, 100);
						setTitle("Stop Ok");
						isStart=false;
					}
					else
					{
						if(mCb_sound.isChecked())
						mSound.callAlarm(false, 100);
						setTitle("Stop Fail");
					}
				}
			}
		});
		
		//清除信息
		Button cleanBtn = (Button) findViewById(R.id.CleanBtn);
		cleanBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				showDataClear();
			}
		});
		//保存数据
		Button SaveBtn = (Button) findViewById(R.id.SaveBtn);
		SaveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Context context = getApplicationContext();  
				String dir = "uhf";
				 String directoryPath="/storage/emulated/0";

				 if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
				 {
					 directoryPath =context.getExternalFilesDir(dir).getAbsolutePath();
				 }
				 
			     File filedir = new File(directoryPath);
			     if(!filedir.exists())
			     {//判断文件目录是否存在
			    	 filedir.mkdirs();		    	 
			     }
			     String filename = directoryPath+"/uhf.txt";
		         File file=new File(filename);
		         if(!file.exists())
		         {  
		             try {  
		                 file.createNewFile();  //创建文件  
		             } catch (IOException e) {  
		                 // TODO Auto-generated catch block  
		                 e.printStackTrace();		             
		             }	
		        }

		        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");        
		        Date date = new Date(System.currentTimeMillis());        
		        String times = simpleDateFormat.format(date); 

		        FileWriter writer = null;
                try {
					writer = new FileWriter(filename, true);//true表示在文件尾部追加内容			            
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        try {
					writer.write(times+ "  Tags:" + String.valueOf(receptionArrayList.size()-1)+"\r\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        String s;
				for(int size = 0;size < receptionArrayList.size();size ++)
				{
					try {
						s = receptionArrayList.get(size)+"\r\n";
						writer.write(s);					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(context, "saved succeed", 0).show();
			}
		}
		);
		
		
		mCb_sound=(CheckBox) findViewById(R.id.checkBox1);
				
		showMessage("EPC",lvAdptrlabRssi,true);
		
	}
	
	public void Test(View view)
	{

		//MULTI_QUERY_TAGS_EPC
		Multi_query_epc mMulti_query_epc=new Multi_query_epc();
		mMulti_query_epc.query_total=0;


		UHFClient info=UHFClient.getInstance();
		if(info!=null)
		{
			UHFClient.mUHF.setCallBack(this);
			Boolean ret=UHFClient.mUHF.command(CommandType.MULTI_QUERY_TAGS_EPC, mMulti_query_epc);

//			showMessage(lvAdptrlabData,lvAdptrlabRssi,false);

			isStart=true;
		}


		
		
		
	}
	

	@Override
	public void finish() 
	{
		// TODO Auto-generated method stub
		super.finish();	
		
		UHFClient info=UHFClient.getInstance();
		if(isStart&&info!=null)
		{
			Boolean ret=UHFClient.mUHF.command(CommandType.STOP_MULTI_QUERY_TAGS_EPC, null);
			if(ret)
			{
				if(mCb_sound.isChecked())
				mSound.callAlarm(true, 100);
				setTitle("Stop Ok");
				isStart=false;
			}
			else
			{
				if(mCb_sound.isChecked())
				mSound.callAlarm(false, 100);
				setTitle("Stop Fail");
			}
		}
		
		
		if(info!=null)
		{
			UHFClient.mUHF.setCallBack(null);
			isStart=false;
		}
		
		//mUHF.transfer_close(mUHF);
		
	}

	

	
	//epc数据
	//0-1：PC 
	//2-2+pc*2-1       EPC
	//2+pc*2-2+pc*2+1  RSSI 
	//2+pc*2+2         antid
	
	long currenttime=0;
	@Override
	public void method(char[] data) 
	{
		// TODO Auto-generated method stub
		//Log.e(TAG, "datalen="+data.length);
		if(data.length<=0)
		{
			return;
		}
		//处理这些数据，并且可以在这里加入声音提示。200ms,这个可以根据效果自己改间隔时间
		if(System.currentTimeMillis()-currenttime>200)
		{
		if(mCb_sound.isChecked())
		mSound.callAlarm(true, 100);
		currenttime=System.currentTimeMillis();
		}
		//把EPC拷贝出来显示
		
		char msb=data[0];
		char lsb=data[1];
		int pc=(msb&0x00ff)<<8|(lsb&0x00ff);
		pc=(pc&0xf800)>>11;
		
		char[] tmp=new char[pc*2];
		System.arraycopy(data,2, tmp, 0, tmp.length);
		String str_tmp=ShareData.CharToString(tmp, tmp.length);
		str_tmp=str_tmp.replace(" ", "");
		//Log.e(TAG, "str_tmp="+str_tmp);
		
		String str_rssi=""+
		rssi_calculate(data[2+pc*2],data[2+pc*2+1]);
		
		
		showMessage(str_tmp,str_rssi,false);
	}
	
	
	private void showMessage(final String data,final String rssi,final Boolean ishead) 
	{
		this.runOnUiThread(new Runnable() 
		{
			@Override
			public void run() {
			//显示标签

                Context context = getApplicationContext();
//				TextView textView=(TextView) findViewById(R.id.textViewtest);

                String dir = "uhf";
                String directoryPath="/storage/emulated/0";

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    directoryPath =context.getExternalFilesDir(dir).getAbsolutePath();
                }



                String filename = directoryPath+"/assets.db";
//		File file = new File(filename);

                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(filename, null);


//		String sql="insert into asset ";
//		db.execSQL(sql);
//

                Cursor cs=db.rawQuery("select * from asset where barcode=?",new String[] {data});  //循环取数据  if(cs.moveToNext())  {      String name=cs.getString(cs.getColumnIndex("name"));      int pic=cs.getInt(2);      //建议使用该方式      int _id=cs.getInt(cs.getColumnIndex("_id"));  }

                if(cs.moveToNext())  {
                    name=cs.getString(cs.getColumnIndex("name"));
                    int pic=cs.getInt(2);      //建议使用该方式
                    int _id=cs.getInt(cs.getColumnIndex("barcode"));

                    ;}

                db.close();

				for (int i = 0; i < receptionArrayList.size(); i++) 
				{
					if (data.equals(receptionArrayList.get(i).get(lvAdptrlabData)))
					{
						String t = receptionArrayList.get(i).get(lvAdptrlabTimes);
						if (TextUtils.isEmpty(t)) 
						{
							t = "1";	
							/*if(ishead)
							{
								t = "0";	
							}*/
						}
						
						t = String.valueOf(Integer.valueOf(t) + 1);
						receptionArrayList.get(i).put(lvAdptrlabTimes, t);
						receptionArrayList.get(i).put(lvAdptrlabRssi,name);
						recptionSimpleAdapter.notifyDataSetChanged();
						return;
					}
				}



				String countMessage = "标签个数：" + String.valueOf(receptionArrayList.size() );
				countTXT.setText(countMessage);
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put(lvAdptrlabData, data);
				hashMap.put(lvAdptrlabRssi,name );
				if(receptionArrayList.size()==0)
				{
					hashMap.put(lvAdptrlabTimes, "Count");
				}
				else
				{
					hashMap.put(lvAdptrlabTimes, "1");	
				}
				receptionArrayList.add(hashMap);
				recptionSimpleAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void showDataClear() 
	{
		this.runOnUiThread(new Runnable() 
		{
			@Override
			public void run() {
				String countMessage = "标签个数：0";
				countTXT.setText(countMessage);
				receptionArrayList.clear();
				showMessage("EPC",lvAdptrlabRssi,true);
				recptionSimpleAdapter.notifyDataSetChanged();
				
				
			}
		});
	}
	
	/**
	 * 请确认demo中的这个算法是否正确
	 * @param rssi_msb
	 * @param rssi_lsb
	 * @return
	 */
	private double rssi_calculate(char rssi_msb, char rssi_lsb)
    {
        int temp_rssi = (int)(((int)rssi_msb << 8) + (int)rssi_lsb);
        double sh_rssi = (double)(short)temp_rssi / 10;

        return sh_rssi;
    }


    public void Upload(View view) {
		Handler myHandler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle data = new Bundle();
				data=msg.getData();
				System.out.println("username" + data.get("useid").toString());
				System.out.println("userpass" + data.get("usepwd").toString());
			}
		};
		Context context2=getApplicationContext();
	    new Thread(){


			String  url="jdbc:mysql://119.3.20.212:3306/jdbctest";
			String   user = "root";
			String pass = "123456";
			Connection connection = null;
			public void run(){
				Context context1=getApplicationContext();
				try{
					Class.forName("com.mysql.jdbc.Driver");
					Log.w("yzy", "驱动注册成功"+"  ");
				}catch (ClassNotFoundException e)
				{ Log.w("yzy", "fail to connect!"+"  "+e.getMessage());
					Toast.makeText(context1,"驱动注册失败！",Toast.LENGTH_LONG).show();
				    e.printStackTrace();

				}
				try{
					connection  = DriverManager.getConnection(url, "root", "123456");
				}catch (SQLException e1)
				{ Log.w("yzy", "fail to connect!2"+"  "+e1.getMessage());
					Toast.makeText(context1,"数据库连接失败！",Toast.LENGTH_LONG).show();
				    e1.printStackTrace();

				}
				try{
					Statement stmt = connection.createStatement();
					String sql = "insert into testio (id,size,datetime) values ('199997',3352,'2018-10-02')";
					boolean rs1 = stmt.execute(sql);
				}catch (SQLException e2)
				{ Log.w("yzy", "fail to connect3!"+"  "+e2.getMessage());
					Toast.makeText(context1,"数据库连接失败2！",Toast.LENGTH_LONG).show();
					e2.printStackTrace();

				}
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			public void run2(){

			}


		}.start();
		Toast.makeText(context2, "数据上传成功！", 0).show();
    }


	public void single(View view) {
		//单次寻标签


				Query_epc mQuery_epc=new Query_epc();
				UHFClient info=UHFClient.getInstance();
				if(info!=null)
				{
					Boolean ret=UHFClient.mUHF.command(CommandType.SINGLE_QUERY_TAGS_EPC, mQuery_epc);
					if(ret)
					{
						String str_tmp=ShareData.CharToString(mQuery_epc.epc.epc, mQuery_epc.epc.epc.length);
						str_tmp=str_tmp.replace(" ", "");
						String str_rssi=""+
								rssi_calculate((char)mQuery_epc.rssi_msb,(char)mQuery_epc.rssi_lsb);
						showMessage(str_tmp,str_rssi,false);
						if(mCb_sound.isChecked())
							mSound.callAlarm(true, 100);
					}
					else
					{
						if(mCb_sound.isChecked())
							mSound.callAlarm(false, 100);
					}
				}


		}
}
