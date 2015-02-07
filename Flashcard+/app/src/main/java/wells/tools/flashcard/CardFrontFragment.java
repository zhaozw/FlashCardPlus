package wells.tools.flashcard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Joshua on 2/6/2015.
 */
public class CardFrontFragment extends Fragment {

    private LinedTextView mStudyText;
    private TextView mIndexText;
    private String mTextString;
    private String mIndexString;

    public CardFrontFragment(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View thisView = inflater.inflate(R.layout.card_front_fragment, container, false);

        mStudyText = (LinedTextView) thisView.findViewById(R.id.studyTextView);
        mIndexText = (TextView) thisView.findViewById(R.id.studyCountTextView);

        mStudyText.setText(mTextString);
        mIndexText.setText(mIndexString);

        return thisView;
    }

    public void changeText(String name, String text)
    {
        mTextString = text;
        mIndexString = name;
    }
}
