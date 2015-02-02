package wells.tools.flashcardplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.view.View.OnClickListener;

public class SettingsActivity extends Activity{

	private MemAssistApp mApp = null;
	private Button mSettingsBtn = null;
	private RadioButton mTestR1 = null;
	private RadioButton mTestR2 = null;
	private RadioButton mTestR3 = null;
	private RadioButton mStudyR1 = null;
	private RadioButton mStudyR2 = null;
	private RadioButton mStudyR3 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings_layout);
		
		mApp = (MemAssistApp) getApplication();
		mSettingsBtn = (Button) this.findViewById(R.id.enterSettingsButton);
		mTestR1 = (RadioButton) this.findViewById(R.id.testNewestFirstRadio);
		mTestR2 = (RadioButton) this.findViewById(R.id.testOldestFirstRadio);
		mTestR3 = (RadioButton) this.findViewById(R.id.testRandomRadio);
		mStudyR1 = (RadioButton) this.findViewById(R.id.studyNewestFirstRadio);
		mStudyR2 = (RadioButton) this.findViewById(R.id.studyOldestFirstRadio);
		mStudyR3 = (RadioButton) this.findViewById(R.id.studyRandomRadio);
		
		checkOrder();
		
		mSettingsBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(mTestR1.isChecked())
					mApp.setTestOrder("new");
				else if(mTestR2.isChecked())
					mApp.setTestOrder("old");
				else
					mApp.setTestOrder("random");
				
				if(mStudyR1.isChecked())
					mApp.setStudyOrder("new");
				else if(mStudyR2.isChecked())
					mApp.setStudyOrder("old");
				else
					mApp.setStudyOrder("random");
				
				finish();
			}
			
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		checkOrder();
	}
	
	private void checkOrder()
	{
		String testOrder = mApp.getTestOrder();
		String studyOrder = mApp.getStudyOrder();
		
		switch (testOrder)
		{
			case "new":
				mTestR1.setChecked(true);
				break;
			case "old":
				mTestR2.setChecked(true);
				break;
			case "random":
				mTestR3.setChecked(true);
				break;
		}
		switch (studyOrder)
		{
			case "new":
				mStudyR1.setChecked(true);
				break;
			case "old":
				mStudyR2.setChecked(true);
				break;
			case "random":
				mStudyR3.setChecked(true);
				break;
		}
	}
}
