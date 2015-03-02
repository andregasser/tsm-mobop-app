package mse.hqevaluator;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class WebServiceTestButtonsFragment extends Fragment implements View.OnClickListener {

    OnButtonPressedListener mCallback;

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button1:
                mCallback.onButtonGetAllNuclearPowerPlantsPressed();
                break;
        }
    }

    // Container Activity must implement this interface
    public interface OnButtonPressedListener {
        public void onButtonGetAllNuclearPowerPlantsPressed();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnButtonPressedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonPressedListener");
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_web_service_test_buttons, container, false);

        Button b = (Button) v.findViewById(R.id.button1);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onPause ()
    {
        super.onPause();
    }
}
