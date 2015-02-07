package wells.tools.flashcard;

import java.util.ArrayList;
import java.util.Random;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.GestureDetector.SimpleOnGestureListener;

public class StudyActivity extends Activity implements FragmentManager.OnBackStackChangedListener {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector mGesture = null;
    private FlashcardApp mApp = null;
    private String mOrder = null;
    private boolean mOnFront = false; // used to determine if on front or back side
    private String[] mCurStrings; // 0 has the name, 1 has the entry
    private ArrayList<String> mMasterListCopy = null;
    private int mMasterListIndex = -1;
    private Random mRand = null;
    private Handler mHandler = new Handler(); // A handler object, used for deferring UI operations.
    private CardBackFragment mCurCardBack;
    private CardFrontFragment mCurCardFront;
    private CardBackFragment mNextCardBack;
    private CardFrontFragment mNextCardFront;

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return mGesture.onTouchEvent(me);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_layout);
        getActionBar().hide();

        mApp = (FlashcardApp) getApplication();
        mGesture = new GestureDetector(this, new StudyGestureListener());

        mCurCardBack = new CardBackFragment();
        mCurCardFront = new CardFrontFragment();
        mNextCardBack = new CardBackFragment();
        mNextCardFront = new CardFrontFragment();

        mOrder = mApp.getStudyOrder();
        mMasterListCopy = mApp.getMasterList();
        mRand = new Random();

        nextCard();
    }

    @Override
    public void onBackStackChanged() {
        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mOrder = mApp.getStudyOrder();
        mMasterListCopy = mApp.getMasterList();
    }

    private void nextCard()
    {
        if(mApp.getMasterList().size() == 0)
        {
            mCurCardFront.changeText("#0", "Nothing to study yet, press back to go back to the main menu.");
            mCurCardBack.changeText("#0", "Nothing to study yet, press back to go back to the main menu.");
            getFragmentManager().beginTransaction().add(R.id.container, mCurCardBack).commit();
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

            //mStudyText.setText(mCurStrings[0]);
            //mCountText.setText("Entry " + Integer.toString(mMasterListIndex+1));

            mNextCardFront.changeText("#" + Integer.toString(mMasterListIndex+1), mCurStrings[1]);
            mNextCardBack.changeText("#" + Integer.toString(mMasterListIndex+1), mCurStrings[0]);


        }
    }

    private void prevCard()
    {
        if(mApp.getMasterList().size() == 0)
        {
            mCurCardFront.changeText("#0", "Nothing to study yet, press back to go back to the main menu.");
            mCurCardBack.changeText("#0", "Nothing to study yet, press back to go back to the main menu.");
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

            //mStudyText.setText(mCurStrings[0]);
            //mCountText.setText("Entry " + Integer.toString(mMasterListIndex+1));
        }
    }

    private void flipUp() {

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(
                R.animator.card_flip_bottom_in, R.animator.card_flip_bottom_out);

        if(mOnFront) {
            ft.replace(R.id.container, mCurCardBack)
                    .commit();
        }
        else
        {
            ft.replace(R.id.container, mCurCardFront)
                    .commit();
        }
        mOnFront = !mOnFront;

        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    private void flipDown() {
        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(
                R.animator.card_flip_top_in, R.animator.card_flip_top_out);

        if(mOnFront) {
            ft.replace(R.id.container, mCurCardBack)
                    .commit();
        }
        else
        {
            ft.replace(R.id.container, mCurCardFront)
                    .commit();
        }
        mOnFront = !mOnFront;

        // Defer an invalidation of the options menu (on modern devices, the action bar). This
        // can't be done immediately because the transaction may not yet be committed. Commits
        // are asynchronous in that they are posted to the main thread's message loop.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    private void setCurrentStrings()
    {
        mCurStrings = FlashcardApp.splitEntry(mMasterListCopy.get(mMasterListIndex));
        mOnFront = true;
    }


    public class StudyGestureListener extends SimpleOnGestureListener {
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
                        nextCard();
                    else
                        prevCard();
                }
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
            {
                if(diffY > 0)
                    flipDown();
                else
                    flipUp();
            }

            return true;
        }
    }

}
