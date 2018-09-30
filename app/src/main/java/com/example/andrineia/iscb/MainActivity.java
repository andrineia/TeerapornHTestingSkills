package com.example.andrineia.iscb;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends Activity {
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_layout);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MyPhonesListFragment listFragment = new MyPhonesListFragment();
        transaction.replace(R.id.master_Fragment, listFragment);
        transaction.commit();


    }

}
