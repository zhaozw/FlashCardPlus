package wells.tools.flashcard;

import java.util.ArrayList;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewAllActivity extends ListActivity{

    private FlashcardApp mApp = null;
    ArrayList<String> mainList = null;
    ArrayAdapter<CharSequence> adptr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_layout);

        mApp = (FlashcardApp) getApplication();
        adptr = new ArrayAdapter<CharSequence>(this, R.layout.list_item_layout);
        mainList = mApp.getMasterList();

        for(int n=0; n<mainList.size(); n++)
        {
            adptr.add(FlashcardApp.splitEntry(mainList.get(n))[0]);
        }

        this.setListAdapter(adptr);
        this.registerForContextMenu(this.getListView());
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent i = new Intent(getApplicationContext(), AddNewActivity.class);
        i.putExtra("index", position);
        startActivity(i);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId() == R.id.delete)
        {
            if(info.position < mainList.size() && mApp.removeFromMasterList(info.position))
            {
                adptr.remove(FlashcardApp.splitEntry(mainList.get(info.position))[0]);
                Toast.makeText(
                        getApplicationContext(),
                        "Entry Successfuly Deleted",
                        Toast.LENGTH_SHORT
                ).show();
            }
            else
            {
                Toast.makeText(
                        getApplicationContext(),
                        "Entry Can't Be Deleted",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
        return super.onContextItemSelected(item);
    }
}
