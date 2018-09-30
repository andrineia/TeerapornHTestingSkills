package com.example.andrineia.iscb;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.andrineia.iscb.Adapter.PhoneListAdapter;
import com.example.andrineia.iscb.Model.HttpHandler;
import com.example.andrineia.iscb.Model.Phones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyPhonesListFragment extends Fragment implements View.OnClickListener {
    private Button btMyFav;
    private ImageView icSort;
    private String url = "";
    private ProgressDialog pDialog;
    private RecyclerView phoneList;
    private ArrayList<Phones> arrayList = new ArrayList<>();
    private final String TAG = MyPhonesListFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        url = "https://scb-test-mobile.herokuapp.com/api/mobiles/";
        setView(view);
        return view;
    }

    private void setView(View view) {
        btMyFav = (Button) view.findViewById(R.id.btMyFav);
        phoneList = (RecyclerView) view.findViewById(R.id.phoneList);
        icSort = (ImageView) view.findViewById(R.id.icSort);
        new GetPhoneList().execute();


        btMyFav.setOnClickListener(this);
        icSort.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btMyFav:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btMyFav.setBackground(getResources().getDrawable(R.drawable.btn_right_border_selection));
                }
                android.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                MyFavoritesPhones favoritesPhones = new MyFavoritesPhones();
                fragmentTransaction.add(R.id.master_Fragment, favoritesPhones,"MyFavoritesPhones");
                fragmentTransaction.commit();
                break;
            case R.id.icSort:
                setDialog();
                break;
        }
    }

    private void setDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        final android.widget.CheckBox boxlow = (android.widget.CheckBox) dialogView.findViewById(R.id.boxlow);
        final android.widget.CheckBox boxhigh = (android.widget.CheckBox) dialogView.findViewById(R.id.boxhigh);
        android.widget.CheckBox boxratting = (android.widget.CheckBox) dialogView.findViewById(R.id.ratting);
        boxlow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                              @Override
                                              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                  if (isChecked) {
                                                      Log.e("boxlow","isChecked");
                                                      Collections.sort(arrayList, new Comparator<Phones>() {
                                                          @Override
                                                          public int compare(Phones o1, Phones o2) {
                                                              return Double.compare(o1.getPrice(), o2.getPrice());
                                                          }
                                                      });
                                                      setListView();
                                                      alertDialog.cancel();
                                                  }
                                              }
                                          }
        );

        boxhigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                   if (isChecked) {
                                                       Log.e("boxhigh","isChecked");
                                                       Collections.sort(arrayList, Collections.reverseOrder(new Comparator<Phones>() {
                                                                                                                @Override
                                                                                                                public int compare(Phones o1, Phones o2) {
                                                                                                                    return Double.compare(o1.getPrice(), o2.getPrice());
                                                                                                                }

                                                                                                            }));
                                                       setListView();
                                                       alertDialog.cancel();
                                                   }
                                               }
                                           }
        );

        boxratting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                  @Override
                                                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                      if (isChecked) {
                                                          Log.e("boxratting","isChecked");
                                                          Collections.sort(arrayList, Collections.reverseOrder(new Comparator<Phones>() {
                                                              @Override
                                                              public int compare(Phones o1, Phones o2) {
                                                                  return Double.compare(o1.getRating(), o2.getRating());
                                                              }
                                                          }));
                                                          setListView();
                                                          alertDialog.cancel();
                                                      }
                                                  }
                                              }
        );

        alertDialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }


    public class GetPhoneList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            if (getActivity() != null) {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCallHeader(url);

            Log.e(TAG + " ", "Response from url: " + jsonStr);
            Log.e(TAG, "URL:-- " + url);

            if (jsonStr != null) {
                try {
                    JSONArray array = new JSONArray(jsonStr);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Phones phones = new Phones();
                        phones.decode(jsonObject);
                        arrayList.add(phones);
                    }
                    Log.e("Size--", arrayList.size() + "");
                } catch (final JSONException e) {
                    e.printStackTrace();

                }
            } else {

                Log.e(TAG, "Couldn't get json from server.");


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            setListView();


        }

    }

    private void setListView() {
        PhoneListAdapter adapter = new PhoneListAdapter(getActivity(), arrayList, new PhoneListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {
                android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt("id", arrayList.get(item).getId());
                PhoneDetails phoneDetails = new PhoneDetails();
                phoneDetails.setArguments(bundle);
                transaction.add(R.id.master_Fragment, phoneDetails);
                transaction.commit();
            }
        });
        phoneList.setAdapter(adapter);
        phoneList.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }
}
