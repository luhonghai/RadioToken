package com.halosolutions.radiotoken;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.halosolutions.library.RadioToken;
import com.halosolutions.library.data.AuthToken;

import net.grandcentrix.thirtyinch.TiActivity;

public class MainActivity extends TiActivity<MainPresenter, MainView> implements MainView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> getPresenter().fetchToken(36.473144, 138.970151));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void showError(Throwable throwable) {
        Snackbar.make(findViewById(R.id.toolbar), "Error: " + throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showResult(AuthToken authToken) {
        Snackbar.make(findViewById(R.id.toolbar), "Token: " + authToken, Snackbar.LENGTH_LONG).show();
    }

    @NonNull
    @Override
    public MainPresenter providePresenter() {
        return new MainPresenter(new RadioToken(getApplicationContext()));
    }
}
