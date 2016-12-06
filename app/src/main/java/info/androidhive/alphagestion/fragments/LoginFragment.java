package info.androidhive.alphagestion.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import info.androidhive.alphagestion.R;
import info.androidhive.alphagestion.activity.MainActivity;
import info.androidhive.alphagestion.activity.PrefManager;

/**
 * Created by Jean Philippe on 07/06/2016.
 */
public class LoginFragment extends Fragment {

    public Button login;
    public  PrefManager  prefManager;
    public LoginFragment () {
    // Required empty public constructor
}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.welcome_slide2, container, false);
           prefManager = new PrefManager(getActivity());
            login = (Button) rootView.findViewById(R.id.btnLogin);
               Context context = getActivity();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    // make first time launch TRUE
                    launchHomeScreen();

                }
            });


            // Inflate the layout for this fragment
            return rootView;
        }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(getActivity(), MainActivity.class));

    }

}

