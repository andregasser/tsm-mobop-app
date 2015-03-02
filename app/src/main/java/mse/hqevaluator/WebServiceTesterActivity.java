package mse.hqevaluator;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


public class WebServiceTesterActivity extends ActionBarActivity
    implements WebServiceTestButtonsFragment.OnButtonPressedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service_tester);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_service_tester, menu);
        return true;
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
    public void onButtonGetAllNuclearPowerPlantsPressed() {
        WebServiceResponseFragment responseFrag = (WebServiceResponseFragment)
            getFragmentManager().findFragmentById(R.id.response_fragment);

        if (responseFrag != null && responseFrag.isInLayout())
        {
            //new GetAllNuclearPowerPlantsTask().execute();

            // Update response fragment
            responseFrag.updateResponse("The button has been pressed!");
        }
    }
}
