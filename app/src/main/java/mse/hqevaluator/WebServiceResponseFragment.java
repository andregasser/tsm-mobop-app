package mse.hqevaluator;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WebServiceResponseFragment extends Fragment {

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_service_response, container, false);
    }

    @Override
    public void onPause ()
    {
        super.onPause();
    }

    public void updateResponse(String text)
    {
        TextView response = (TextView) getView().findViewById(R.id.response);
        response.setText(text);
    }
}
