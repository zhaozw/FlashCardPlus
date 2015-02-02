package wells.tools.flashcard;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;
import android.view.GestureDetector.SimpleOnGestureListener;

public class StudyActivity extends Activity{

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector mGesture = null;
    private FlashcardApp mApp = null;
    private TextView mCountText = null;
    private TextView mStudyText = null;
    private String mOrder = null;
    private boolean mOnNameSide = true; // used to determine if on name or entry side
    private String[] mCurStrings; // 0 has the name, 1 has the entry
    private ArrayList<String> mMasterListCopy = null;
    private int mMasterListIndex = -1;
    private Random mRand = null;

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return mGesture.onTouchEvent(me);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_land_layout);

        mApp = (FlashcardApp) getApplication();
        mGesture = new GestureDetector(this, new StudyGestureListener());
        mCountText = (TextView) this.findViewById(R.id.studyCountTextView);
        mStudyText = (TextView) this.findViewById(R.id.studyTextView);

        mOrder = mApp.getStudyOrder();
        mMasterListCopy = mApp.getMasterList();
        mRand = new Random();

        nextStudy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mOrder = mApp.getStudyOrder();
        mMasterListCopy = mApp.getMasterList();
        mMasterListIndex = -1;
        nextStudy();
    }

    private void nextStudy()
    {
        if(mApp.getMasterList().size() == 0)
        {
            mStudyText.setText("Nothing to study yet, press back to go back to the main menu.");
        }
        else
        {
            switch (mOrder)
            {
                case "new":
                    if(mMasterListIndex == -1 || mMasterListIndex-1 == -1)
                        mMasterListIndex = mMasterListCopy.size()-1;
                    else
                        mMasterListIndex--;
                    break;

                case "old":
                    if(mMasterListIndex == -1 || mMasterListIndex+1 == mMasterListCopy.size())
                        mMasterListIndex = 0;
                    else
                        mMasterListIndex++;
                    break;

                case "random":
                    mMasterListIndex = mRand.nextInt(mMasterListCopy.size());
                    break;
            }

            setCurrentStrings();
            mStudyText.setText(mCurStrings[0]);
            mCountText.setText("Entry " + Integer.toString(mMasterListIndex+1));
        }
    }

    private void lastStudy()
    {
        if(mApp.getMasterList().size() == 0)
        {
            mStudyText.setText("Nothing to study yet, press back to go back to the main menu.");
        }
        else
        {
            switch (mOrder)
            {
                case "new":
                    if(mMasterListIndex == -1 || mMasterListIndex+1 == mMasterListCopy.size())
                        mMasterListIndex = 0;
                    else
                        mMasterListIndex++;
                    break;

                case "old":
                    if(mMasterListIndex == -1 || mMasterListIndex-1 == -1)
                        mMasterListIndex = mMasterListCopy.size()-1;
                    else
                        mMasterListIndex--;
                    break;

                case "random":
                    mMasterListIndex = mRand.nextInt(mMasterListCopy.size());
                    break;
            }

            setCurrentStrings();
            mStudyText.setText(mCurStrings[0]);
            mCountText.setText("Entry " + Integer.toString(mMasterListIndex+1));
        }
    }

    private void setCurrentStrings()
    {
        mCurStrings = FlashcardApp.splitEntry(mMasterListCopy.get(mMasterListIndex));
        mOnNameSide = true;
    }


    private final class StudyGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {return false; }

        @Override
        public void onShowPress(MotionEvent e) { }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {return false; }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) { return false; }

        @Override
        public void onLongPress(MotionEvent e) { }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY))
            {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                {
                    if(diffX > 0)
                        nextStudy();
                    else
                        lastStudy();
                }
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
            {
                mOnNameSide = !mOnNameSide;
                if(mOnNameSide)
                    mStudyText.setText(mCurStrings[0]);
                else
                    mStudyText.setText(mCurStrings[1]);
            }

            return true;
        }
    }

}
