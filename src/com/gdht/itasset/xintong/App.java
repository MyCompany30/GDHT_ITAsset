/**
 * StUhf.getUhfInstance函数不一定要在Application中获取，在这儿获取和保存只是为了方便在各个Activity中引用
 */

package com.gdht.itasset.xintong;

import com.senter.support.openapi.StUhf;
import com.senter.support.openapi.StUhf.Function;
import com.senter.support.openapi.StUhf.InterrogatorModel;
import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * 说明：
 * com.senter.demo.uhf.modelA
 * com.senter.demo.uhf.modelB
 * com.senter.demo.uhf.modelC
 * 三个包分别适配了三个不同的硬件模块，各位如果要写程序，请先确认自己是哪种模块。
 * 如果同一个程序要同时支持两种不同模块的机器，可通过如下rfid.getInterrogatorModel()方式获知模块类型。
 * 
 * @author 
 * @version 创建时间：2013-7-11 上午10:38:56
 */
public class App extends Application
{
	public static final String TAG="MainApp";
	private static StUhf rfid;
	
	private static App mSinglton; 
	private static Configuration mAppConfiguration;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		mSinglton=this;
	}
	
	
	
	
	
	/**
	 * 初始化时用以生成超高频对象，以后就可以直接用rfid()来调用了。
	 */
	public static StUhf getRfid()
	{
		if (rfid == null)
		{
			StUhf rf = null;
			
			if (getSavedModel()==null)
			{
				rf = StUhf.getUhfInstance();//InterrogatorModel.InterrogatorModelD2
			}else {
				rf=StUhf.getUhfInstance(getSavedModel());
			}
			
			if (rf == null)
			{
				Log.e(TAG, "Rfid instance is null,exit");
				return null;
			}
			
			boolean b = rf.init();
			if (b == false)
			{
				Log.e(TAG, "cannot init rfid");
				return null;
			}
			rfid = rf;
			
			InterrogatorModel model=rfid.getInterrogatorModel();
			saveModelName(model);
			switch (model)
			{
				case InterrogatorModelA:
				case InterrogatorModelB:
				case InterrogatorModelC:
				case InterrogatorModelD2:
//				case InterrogatorModelE:
					break;
				default:
					throw new IllegalStateException("new rfid model found,please check your code for compatibility.");
			}
		}
		return rfid;
	}
	
	public static StUhf rfid()
	{
		return rfid;
	}
	
	void SimpleStringSplitter()
	{
		
		
		
		
		
	}
	
	
	
	
	
	

	/**
	 * 在这儿最多会执行三次Stop，只所以这样，只是为了方便各Activity不用担心执行一次不成功
	 */
	public static boolean stop()
	{
		if (rfid != null)
		{
			if (rfid.isFunctionSupported(com.senter.support.openapi.StUhf.Function.StopOperation))
			{
				for (int i = 0; i < 3; i++)
				{
					if (rfid().stopOperation())
					{
						Log.e(TAG, "stopOperation 成功");
						return true;
					}
				}
				Log.e(TAG, "stopOperation 不成功");
				return false;
			}
		}
		return true;
	}

	/**
	 * 清除Selection选定
	 */
	public static void clearMaskAndSelection()
	{
		if (rfid.isFunctionSupported(Function.DisableMaskSettings))
		{
			rfid.disableMaskSettings();
		}
	}
	
	

	private static final InterrogatorModel getSavedModel()
	{
		String modelName=getConfiguration().getString("modelName", "");
		if (modelName.length()!=0)
		{
			return InterrogatorModel.valueOf(modelName);
		}
		return null;
	}
	
	private static final void saveModelName(InterrogatorModel model)
	{
		if (model==null)
		{
			throw new NullPointerException();
		}
		getConfiguration().setString("modelName", model.name());
	}
	private static final Configuration getConfiguration()
	{
		if (mAppConfiguration==null)
		{
			mAppConfiguration=new Configuration(mSinglton, "settings", Context.MODE_PRIVATE);
		}
		return mAppConfiguration;
	}
}
