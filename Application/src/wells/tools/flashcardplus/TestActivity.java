package wells.tools.flashcardplus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class TestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_layout);
		
	}
}
