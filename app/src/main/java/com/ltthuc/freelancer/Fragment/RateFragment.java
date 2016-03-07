package com.ltthuc.freelancer.Fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.ltthuc.freelancer.R;

/**
 * Created by Thuc on 3/7/2016.
 */
public class RateFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "RateFragment";
    private ImageButton closeBtn;
    private RatingBar ratingBar;
    RateListener rateListener;
    public RateFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Dialog dlg = super.onCreateDialog(savedInstanceState);
        dlg.getWindow().setBackgroundDrawableResource(
                R.drawable.nupp_taust_kitsas);
        return dlg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rate_dlg, container);
        closeBtn = (ImageButton) view.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(this);
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        rateListener.onRate();
        ratingBar
                .setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating, boolean fromUser) {
                        ratingBar.setRating(rating);

                        if (rating == 4 || rating == 5) {
                            Uri uri = Uri
                                    .parse("market://details?id=com.taavi.bikinibuttnew");
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW,
                                    uri);
                            try {
                                startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=com.taavi.bikinibuttnew")));
                            }
                        } else {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                            emailIntent
                                    .setData(Uri.parse("mailto:" + "info@babyweightapp.com"));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                                    "Feedback From " + getResources().getString(R.string.app_name));
                            try {
                                startActivity(Intent.createChooser(emailIntent,
                                        "Send email using..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getActivity(), "No email clients installed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(rateListener==null){
            rateListener = (RateListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        rateListener = null;
    }
    @Override
    public void onClick(View v) {
        if (v.equals(closeBtn)) {
            getDialog().dismiss();
            rateListener.onRate();
        }
    }
    public interface  RateListener{
        void onRate();

    }
}
