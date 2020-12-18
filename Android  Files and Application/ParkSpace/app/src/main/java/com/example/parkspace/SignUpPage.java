package com.example.parkspace;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.parkspace.appsharedpreferences.UserSharedPreferencesInfo;

public class SignUpPage extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private static final String requestedURL = URLHandler.requestedURL + "register_ps.php";

    private EditText edtAccName;
    private String name;

    private EditText edtAccEmail;
    private String email;

    private EditText edtAccRegisterNo;
    private String registerno;

    private EditText edtAccPassword;
    private String password;

    private EditText edtAccPasswordConfirm;
    private String passwordConfirm;

    private String currentMode;
    private String mode;

    private Button btnSignUp;
    private Button btnUserType;

    private UserSharedPreferencesInfo userSharedPreferencesInfo;

    private View loadingIndicator;

    private static final int REGISTRATION_LOADER_ID = 121;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_page, container, false);

        edtAccName = rootView.findViewById(R.id.acc_username);
        edtAccEmail = rootView.findViewById(R.id.acc_email_id);
        edtAccRegisterNo = rootView.findViewById(R.id.acc_register_no);
        edtAccPassword = rootView.findViewById(R.id.acc_password);
        edtAccPasswordConfirm = rootView.findViewById(R.id.acc_password_confirm);

        loadingIndicator = rootView.findViewById(R.id.loading_indicator_create_account);
        loadingIndicator.setVisibility(View.GONE);

        btnUserType = rootView.findViewById(R.id.acc_type);
        btnUserType.setText(R.string.user_type_visitor);
        currentMode = "REGISTERED";

        btnSignUp = rootView.findViewById(R.id.acc_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnUserType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMode.equals("VISITOR")) {
                    btnUserType.setText(R.string.user_type_visitor);
                    currentMode = "REGISTERED";
                    edtAccRegisterNo.setVisibility(View.VISIBLE);
                } else if (currentMode.equals("REGISTERED")){
                    btnUserType.setText(R.string.user_type_registered);
                    currentMode = "VISITOR";
                    edtAccRegisterNo.setVisibility(View.GONE);
                }
            }
        });

        return rootView;
    }

    private void registerUser() {
        mode = currentMode;
        name = edtAccName.getText().toString();
        email = edtAccEmail.getText().toString();

        if (mode.equals("REGISTERED")) {
            registerno = edtAccRegisterNo.getText().toString();
        }

        password = edtAccPassword.getText().toString();
        passwordConfirm = edtAccPasswordConfirm.getText().toString();

        loadingIndicator.setVisibility(View.VISIBLE);

        //Here we checking Registered user details are empty or not, And Visitor user details are empty or not.
        if ((mode.equals("REGISTERED") &&
                (name.equals("") || email.equals("") || registerno.equals("") || password.equals("") || passwordConfirm.equals(""))) ||
                (mode.equals("VISITOR") &&
                        (name.equals("") || email.equals("") || password.equals("") || passwordConfirm.equals("")))) {
            Toast.makeText(getContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();

        } else {

            LoaderManager loaderManager = getActivity().getSupportLoaderManager();

            if(loaderManager.getLoader(REGISTRATION_LOADER_ID) != null) {
                loaderManager.destroyLoader(REGISTRATION_LOADER_ID);
            }

            if (password.equals(passwordConfirm)) {
                loaderManager.initLoader(REGISTRATION_LOADER_ID, null, SignUpPage.this);
            } else {
                Toast.makeText(getContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        Uri baseUri = Uri.parse(requestedURL);

        String actualUrl = "";

        Uri.Builder uriBuilder = baseUri.buildUpon();
        if (mode.equals("REGISTERED")) {
            actualUrl = uriBuilder.toString() + "?name=" + name + "&email_id=" + email + "&register_no=" + registerno + "&password=" + password + "&mode=" + mode;
        } else if (mode.equals("VISITOR")) {
            actualUrl = uriBuilder.toString() + "?name=" + name + "&email_id=" + email + "&register_no=*VISITOR*" + "&password=" + password + "&mode=" + mode;
        }

        loadingIndicator.setVisibility(View.VISIBLE);

        return new CreateAccountLoader(getContext(), actualUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        /*if (cm.isActiveNetworkMetered()) {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }*/

        edtAccName.setText("");
        edtAccEmail.setText("");
        edtAccRegisterNo.setText("");
        edtAccPassword.setText("");
        edtAccPasswordConfirm.setText("");

        loadingIndicator.setVisibility(View.GONE);

        if (data == null || data.equals("Unable to Connect")) {
            Toast.makeText(getContext(), "Server not respond.", Toast.LENGTH_SHORT).show();
        } else if (data.equals("Successfully registered")) {

            userSharedPreferencesInfo = new UserSharedPreferencesInfo(getContext());

            userSharedPreferencesInfo.setUsername(email);
            userSharedPreferencesInfo.setPassword(password);

            startActivity(new Intent(getContext(), BookParking.class));
            getActivity().finish();

        } else if (data.equals("Oops! Please try again!")) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

        } else if (data.equals("Registerno or Email already exist")) {
            Toast.makeText(getContext(), "Register No. and Email already used", Toast.LENGTH_SHORT).show();

        } else if (data.equals("Please fill all values")) {
            Toast.makeText(getContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
        }

        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        edtAccName.setText("");
        edtAccEmail.setText("");
        edtAccRegisterNo.setText("");
        edtAccPassword.setText("");
        edtAccPasswordConfirm.setText("");
    }
}
