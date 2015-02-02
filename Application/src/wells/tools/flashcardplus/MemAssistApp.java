package wells.tools.flashcardplus;

import java.util.ArrayList;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class MemAssistApp extends Application
{
	// 'm' in front of private variable names for 'Main'
	private SharedPreferences mPref = null;
	private SharedPreferences.Editor mEdit = null;
	private ArrayList<String> mMasterList = new ArrayList<String>();
	private Integer mStartIndex = -1;
	private String mStudyOrder = "new"; // default setting (newest first)
	private String mTestOrder = "new";
	
	// getters and setters of non-primitive types set and return copies, not references
	// in case i ever want to add concurrent processes.
	
	public void setSharedPreferences(SharedPreferences p)
	{
		this.mPref = p;
		this.mEdit = p.edit();
	}
	
	public SharedPreferences getSharedPreferences()
	{
		return this.mPref;
	}
	
	public void setMasterList(ArrayList<String> list)
	{
		this.mMasterList = new ArrayList<String>(list);
	}
	
	public ArrayList<String> getMasterList()
	{
		return new ArrayList<String>(this.mMasterList);
	}
	
	public void addToMasterList(String s)
	{
		if(!this.mMasterList.contains(s))
		{
			this.mMasterList.add(s);
			
			this.mEdit.putString(Integer.toString(this.mMasterList.size()-1), s);
			this.mEdit.commit();
			Log.i("SharedPreferences", "Added to");
		}
	}
	
	public void updateMasterList(String s, int i)
	{
		this.mMasterList.set(i, s);
		this.mEdit.remove(Integer.toString(i));
		this.mEdit.putString(Integer.toString(i), s);
	}
	
	public boolean removeFromMasterList(int i)
	{
		if(i < this.mMasterList.size())
		{
			this.mMasterList.remove(i);
			
			// update the preference file, anything after the removed element will have
			// to be re-indexed
			for(int n=i; n<this.mMasterList.size(); n++)
			{
				this.mEdit.putString(Integer.toString(n), this.mMasterList.get(n));
			}
			
			// get rid of the last index because it's paired with duplicate data now
			this.mEdit.remove(Integer.toString(this.mMasterList.size()));
			this.mEdit.commit();
			
			return true;
		}
		else
			return false;
	}
	
	public void setStartIndex(Integer i)
	{
		this.mStartIndex = i;
	}
	
	public Integer getStartIndex()
	{
		return this.mStartIndex;
	}
	
	public void setStudyOrder(String s)
	{
		this.mStudyOrder = s;
	}
	
	public String getStudyOrder()
	{
		return this.mStudyOrder;
	}
	
	public void setTestOrder(String s)
	{
		this.mTestOrder = s;
	}
	
	public String getTestOrder()
	{
		return this.mTestOrder;
	}
	
	public static String[] splitEntry(String entry)
	{
		return entry.split("\\|");
	}
}
