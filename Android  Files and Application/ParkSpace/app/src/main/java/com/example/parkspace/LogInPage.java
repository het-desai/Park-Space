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

public class LogInPage extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnLogin;

    private View loadingIndicator;

    private UserSharedPreferencesInfo userSharedPreferencesInfo;

    private static final String requestedURL = URLHandler.requestedURL + "login_ps.php";

    private String validEmail;
    private String validPassword;

    private static final int LOGIN_LOADER_ID = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_page, container, false);

        edtUserName = rootView.findViewById(R.id.username);
        edtPassword = rootView.findViewById(R.id.password);
        btnLogin = rootView.findViewById(R.id.signin);

        loadingIndicator = rootView.findViewById(R.id.loading_indicator_login);
        loadingIndicator.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginprocess();
            }
        });

        return rootView;
    }

    private void loginprocess() {

        validEmail = edtUserName.getText().toString().trim();
        validPassword = edtPassword.getText().toString();

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();


        if (loaderManager.getLoader(LOGIN_LOADER_ID) != null) {
            loaderManager.destroyLoader(LOGIN_LOADER_ID);
        }

        if (!validEmail.isEmpty() && !validPassword.isEmpty()) {
            loaderManager.initLoader(LOGIN_LOADER_ID, null, LogInPage.this);
        } else {
            Toast.makeText(getContext(), "Please enter username or password.", Toast.LENGTH_SHORT).show();
        }

    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        Uri baseUri = Uri.parse(requestedURL);

        Uri.Builder uriBuilder = baseUri.buildUpon();
        String actualUrl = uriBuilder.toString() + "?email_id=" + validEmail + "&password=" + validPassword;

        loadingIndicator.setVisibility(View.VISIBLE);

        return new LoginLoader(getContext(), actualUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        /*if (cm.isActiveNetworkMetered()) {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }*/

        edtUserName.setText("");
        edtPassword.setText("");

        if (data == null) {
            Toast.makeText(getContext(), "Server not respond", Toast.LENGTH_SHORT).show();

        } else if (data.equals("Success")) {

            userSharedPreferencesInfo = new UserSharedPreferencesInfo(getContext());

            userSharedPreferencesInfo.setUsername(validEmail);
            userSharedPreferencesInfo.setPassword(validPassword);

            startActivity(new Intent(getContext(), BookParking.class));
            getActivity().finish();

        } else if (data.equals("Failure")) {
            Toast.makeText(getContext(), R.string.user_not_found, Toast.LENGTH_SHORT).show();
        }

        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        edtUserName.setText("");
        edtPassword.setText("");
    }
}
