package com.example.h_sed.test2;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.h_sed.test2.manager.ApiUtils;
import com.example.h_sed.test2.mapping.Navigation;

import java.util.List;

import static com.example.h_sed.test2.ApiApplication.getAppContext;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ProgressBar progressBar;
    private Button navigate_button;
    private Spinner spinner;
    List<Navigation> mydata;
    private int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
        navigate_button = (Button) findViewById(R.id.nav_button);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        if (isConnected()){
            callAPI();
        } else {
            showMessage("No network connection.");
            showFetchingProgress(false);
        }
    }

    private void callAPI() {
        //First we need a listener to errors, It can be in a different class
        // when we have lots of error codes to handle
        final Response.ErrorListener errorHandler = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //todo: Handle error better than showing a single message
                showMessage(error.toString());
                showFetchingProgress(false);
            }

        };
        showFetchingProgress(true);
        ApiUtils.getData(new Response.Listener<List<Navigation>>() {
            @Override
            public void onResponse(List<Navigation> response) {
                // background task is done
                if (response != null) {
                    mydata = response;
                    showFetchingProgress(false);
                    activateButton(true);
                    updateUI();
                } else {
                    showMessage(getString(R.string.general_error));
                }
            }
        }, errorHandler);

    }


    private void updateUI() {
        //Managing of User Interface
        String[] names = new String[mydata.size()];
        Navigation.FromCentral[] fromcentral = new Navigation.FromCentral[mydata.size()];
        for (int i = 0; i < mydata.size(); i++) {
            names[i] = (mydata.get(i).getMyName());
            fromcentral[i] = (mydata.get(i).getMyFromCentral());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getAppContext(),
               R.layout.spinner_item, names);
        adapter.setDropDownViewResource
                (R.layout.spinner_item);
        spinner.setAdapter(adapter);

    }

    public void onNavigate(View v){
        String uri = "http://maps.google.com/maps?q=loc:" + mydata.get(selectedItem)
                .getMyLocation().getMyLatitude() + "," +
                mydata.get(selectedItem).getMyLocation().getMyLongitude() + " (" +
                mydata.get(selectedItem).getMyName() + ")" + "&z=17";
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        if (mapIntent.resolveActivity(getAppContext().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            showMessage("No intent to handle maps");
        }
    }


    private void showFetchingProgress(boolean isFetching) {
        progressBar.setVisibility(isFetching ? View.VISIBLE : View.GONE);
    }

    private void activateButton(boolean isEnable) {
        navigate_button.setEnabled(isEnable ? true : false);
    }

    private void showMessage(String message) {
        Toast.makeText(getAppContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = position;
        String item = parent.getItemAtPosition(selectedItem).toString();
        TextView description = (TextView) findViewById(R.id.description);
        String selected_description = (mydata.get(selectedItem).myFromCentral.getMyCar()
                == null ? "" : mydata.get(selectedItem).myFromCentral.getMyCar()) + "\n" +
                (mydata.get(selectedItem).myFromCentral.getMyTrain() == null ? ""
                        : mydata.get(selectedItem).myFromCentral.getMyTrain());
        description.setText(selected_description);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean isConnected() {
        // Check if there is network connection then we call api
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
