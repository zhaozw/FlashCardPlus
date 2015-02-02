package wells.tools.flashcard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class FlashcardPlusMainActivity extends Activity {

    private FlashcardApp mApp = null;
    private SharedPreferences mPref = null;
    private Button mStudyBtn = null;
    private Button mTestYourselfBtn = null;
    private Button mAddNewBtn = null;
    private Button mViewAllBtn = null;
    private Button mSettingBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_main);

        mApp = (FlashcardApp) getApplication();
        mPref = getPreferences(MODE_PRIVATE);
        mApp.setSharedPreferences(mPref);

        int i = mPref.getInt("NumDataEntries", -1);
        Log.i("NumDataEntries", Integer.toString(i));
        mApp.setStartIndex(i);
        if(i != -1)
        {
            for(int n=0; n<=i; n++)
            {
                mApp.addToMasterList(mPref.getString(Integer.toString(n), ""));
            }
        }

        mApp.setTestOrder(mPref.getString("TestOrder", "new"));
        mApp.setStudyOrder(mPref.getString("StudyOrder", "new"));

        mStudyBtn = (Button) this.findViewById(R.id.studyButton);
        mTestYourselfBtn = (Button) this.findViewById(R.id.testButton);
        mAddNewBtn = (Button) this.findViewById(R.id.addNewButton);
        mViewAllBtn = (Button) this.findViewById(R.id.viewAllButton);
        mSettingBtn = (Button) this.findViewById(R.id.settingsButton);


        mStudyBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), StudyActivity.class);
                startActivity(i);
            }
        });

        mTestYourselfBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), TestActivity.class);
                startActivity(i);
            }
        });

        mAddNewBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), AddNewActivity.class);
                startActivity(i);
            }
        });

        mViewAllBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ViewAllActivity.class);
                startActivity(i);
            }
        });

        mSettingBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        SharedPreferences.Editor mEdit = mPref.edit();
        mEdit.putInt("NumDataEntries", mApp.getMasterList().size()-1);
        mEdit.putString("TestOrder", mApp.getTestOrder());
        mEdit.putString("StudyOrder", mApp.getStudyOrder());
        mEdit.commit();
    }
}
