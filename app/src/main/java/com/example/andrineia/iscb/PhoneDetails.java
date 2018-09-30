package com.example.andrineia.iscb;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andrineia.iscb.Model.HttpHandler;
import com.example.andrineia.iscb.Model.PhoneImages;
import com.example.andrineia.iscb.Model.Phones;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class PhoneDetails extends Fragment {
    private String url, imgUrl = "";
    private int id = 0;
    private TextView title, tvNameHeader, tvPrice, tvRatting, tvDetails;
    private ImageView imgCredit;
    private Phones phones;
    private ProgressDialog pDialog;
    private ViewPager lnBanner;
    private ViewPagerAdapter mAdapter;
    LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    private ArrayList<PhoneImages> phoneImages = new ArrayList<>();
    private final String TAG = PhoneDetails.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout, container, false);
        url = "https://scb-test-mobile.herokuapp.com/api/mobiles/";
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
        if (id != 0) {
            url = url + id;
            imgUrl = "https://scb-test-mobile.herokuapp.com/api/mobiles/" + id + "/images/";
        }

        setView(view);
        return view;
    }

    private void setView(View view) {
        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        title = (TextView) view.findViewById(R.id.title);
        tvNameHeader = (TextView) view.findViewById(R.id.tvNameHeader);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvRatting = (TextView) view.findViewById(R.id.tvRatting);
        tvDetails = (TextView) view.findViewById(R.id.tvDetails);
        lnBanner = (ViewPager) view.findViewById(R.id.lnBanner);

        new GetPhoneImages().execute();
        new GetPhoneDetails().execute();
    }

    public class GetPhoneDetails extends AsyncTask<Void, Void, Void> {

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
//            Log.e(TAG + " ", "Response from url: " + jsonStr);
            Log.e(TAG, "URL:-- " + url);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonStr);
                    phones = new Phones();
                    phones.decode(jsonObject);

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
            setContent();


        }

    }

    public class GetPhoneImages extends AsyncTask<Void, Void, Void> implements ViewPager.OnPageChangeListener {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCallHeader(imgUrl);
            Log.e(TAG + " ", "Response from url: " + jsonStr);
            Log.e(TAG, "URL:-- " + url);

            if (jsonStr != null) {
                try {

                    JSONArray array = new JSONArray(jsonStr);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        PhoneImages phones = new PhoneImages();
                        phones.decode(jsonObject);
                        phoneImages.add(phones);

                    }

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
            if (phoneImages.size() > 0) {
                mAdapter = new ViewPagerAdapter(phoneImages);
                lnBanner.setAdapter(mAdapter);
                lnBanner.setCurrentItem(0);
                lnBanner.setOnPageChangeListener(this);
                setUiPageViewController();
            }
        }


        private void setUiPageViewController() {
            dotsCount = mAdapter.getCount();
            dots = new ImageView[dotsCount];
            if (dots.length > 0) {
                if (getActivity() != null) {
                    for (int i = 0; i < dotsCount; i++) {

                        dots[i] = new ImageView(getActivity());
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        params.setMargins(4, 0, 4, 0);

                        pager_indicator.addView(dots[i], params);
                    }
                }
            }
            try {
                dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            for (int j = 0; j < dotsCount; j++) {
                dots[j].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    private void setContent() {
//        try {
//            Picasso.with(getActivity()).load(imgUrl).into(imgCredit);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        title.setText(phones.getName());
        tvNameHeader.setText(phones.getName());
        tvPrice.setText("Price : " + phones.getPrice() + " $ ");
        tvRatting.setText("Ratting : " + phones.getRating());
        tvDetails.setText(phones.getDescription());

    }

    public class ViewPagerAdapter extends PagerAdapter {
        ImageView imageView;

        //        private Context mContext;
        private LayoutInflater layoutInflater;
        private ArrayList<PhoneImages> mResources = new ArrayList<>();


        public ViewPagerAdapter(ArrayList<PhoneImages> mImageResources) {
            this.mResources = mImageResources;
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.home_pager_items, container, false);
            imageView = (ImageView) view.findViewById(R.id.img_pager_item);
            String url = "";
            url = mResources.get(position).getUrl();
            Log.e("urlBefore", url);
            if (!url.substring(0, 5).equals("https")) { //
                if (url.substring(0, 5).equals("http:")) {
                    Log.e("2", url.substring(0, 5));
                    url = url.replace("http://", "https://");
                } else {
                    Log.e("1", url.substring(0, 5));
                    url = "https://" + mResources.get(position).getUrl();
                }
            }
            Log.e("url", url);
            try {
                Picasso.with(getActivity()).load(url).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            new AsyncTaskLoadImage().execute(mResources.get(position).getUrl());


            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

//        class AsyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {
//            private final static String TAG = "AsyncTaskLoadImage";
//
//            @Override
//            protected Bitmap doInBackground(String... params) {
//                Bitmap bitmap = null;
//                try {
//                    Log.e(TAG, "Worked");
//                    URL url = new URL(params[0]);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoInput(true);
//                    connection.connect();
//                    InputStream input = connection.getInputStream();
//                    bitmap = BitmapFactory.decodeStream(input);
//                    return bitmap;
//                } catch (IOException e) {
//                    Log.e(TAG, e.getMessage());
//                }
//                return bitmap;
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                imageView.setImageBitmap(bitmap);
//            }
//        }
    }


}
