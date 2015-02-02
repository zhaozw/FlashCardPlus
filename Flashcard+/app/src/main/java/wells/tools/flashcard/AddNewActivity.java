package wells.tools.flashcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class AddNewActivity extends Activity{

    private FlashcardApp mApp = null;
    private EditText mNameET = null;
    private EditText mEntryET = null;
    private Button mEntryBtn = null;
    private boolean isEdit = false;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_new_data_layout);

        mApp = (FlashcardApp) getApplication();
        mNameET = (EditText) this.findViewById(R.id.dataNameEditText);
        mEntryET = (EditText) this.findViewById(R.id.dataEntryEditText);
        mEntryBtn = (Button) this.findViewById(R.id.newEntryButton);

        Intent profile = getIntent();
        index = profile.getIntExtra("index", -1);
        if(index != -1)
        {
            isEdit = true;
            String[] temp = FlashcardApp.splitEntry(mApp.getMasterList().get(index));
            mNameET.setText(temp[0]);
            mEntryET.setText(temp[1]);
        }

        mEntryBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String name = mNameET.getText().toString().trim();
                String entry = mEntryET.getText().toString().trim();

                if(name.isEmpty() || entry.isEmpty())
                {
                    Toast.makeText(
                            getApplicationContext(),
                            "You must enter something into both fields.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                else if(name.contains("|") || entry.contains("|"))
                {
                    Toast.makeText(
                            getApplicationContext(),
                            "You can't have the '|' character in your entries.",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                else if(isEdit)
                {
                    String newEntry = name + "|" + entry;
                    mApp.updateMasterList(newEntry, index);
                    finish();
                }
                else
                {
                    String newEntry = name + "|" + entry;
                    mApp.addToMasterList(newEntry);
                    finish();
                }
            }
        });
    }
}
