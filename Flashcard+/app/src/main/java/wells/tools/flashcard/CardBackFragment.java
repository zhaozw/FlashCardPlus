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
public class CardBackFragment extends Fragment {

    private TextView mStudyText;
    private TextView mIndexText;
    private String mTextString;
    private String mIndexString;

    public CardBackFragment(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View thisView = lf.inflate(R.layout.card_back_fragment, container, false);

        mStudyText = (TextView) thisView.findViewById(R.id.studyTextView);
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
