package com.example.jeyanthasingam.busroutefinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class AddGeofenceFragment extends DialogFragment {
    private static final String Tag = "SingamsMsg";

    // region Properties

    private ViewHolder viewHolder;

    private ViewHolder getViewHolder() {
        Log.i(Tag, "AddGeofenceFragment   getViewHolder");
        return viewHolder;
    }

    AddGeofenceFragmentListener listener;

    public void setListener(AddGeofenceFragmentListener listener) {
        Log.i(Tag, "AddGeofenceFragment   setListener");
        this.listener = listener;
    }

    public void sample(String name, LatLng loc){


        Log.i(Tag,"11");
        NamedGeofence geofence = new NamedGeofence();
        Log.i(Tag,"12");
        geofence.name = name;
        Log.i(Tag,"13");
        geofence.latitude = loc.latitude;
        geofence.longitude = loc.longitude;
        geofence.radius = 500.0f;

        if (listener != null) {
            Log.i(Tag,"14");
            listener.onDialogPositiveClick(AddGeofenceFragment.this, geofence);
            Log.i(Tag,"15");
        }
    }

    // endregion

    // region Overrides

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(Tag, "AddGeofenceFragment   onCreateDialog");
        LayoutInflater inflater = getActivity().getLayoutInflater();
//    View view = inflater.inflate(R.layout.dialog_add_geofence, null);
//
//    viewHolder = new ViewHolder();
//    viewHolder.populate(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//    builder.setView(view)
//            .setPositiveButton(R.string.Add, null)
//            .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
//              public void onClick(DialogInterface dialog, int id) {
//                  Log.i(Tag,"AddGeofenceFragment   onCreateDialog   onClick");
//                AddGeofenceFragment.this.getDialog().cancel();
//
//                if (listener != null) {
//                  listener.onDialogNegativeClick(AddGeofenceFragment.this);
//                }
//              }
//            });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Log.i(Tag, "AddGeofenceFragment   onShow");
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Log.i(Tag, "AddGeofenceFragment   onShow   onClick");
                        NamedGeofence geofence = new NamedGeofence();
                        geofence.name = "singam";
                        geofence.latitude = 6.8731595f;
                        geofence.longitude = 79.8787443f;
                        geofence.radius = 20.0f;

                        if (listener != null) {
                            listener.onDialogPositiveClick(AddGeofenceFragment.this, geofence);
                            dialog.dismiss();
                        }
                    }

                });

            }
        });

        return dialog;
    }

    public interface AddGeofenceFragmentListener {
        void onDialogPositiveClick(DialogFragment dialog, NamedGeofence geofence);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    // endregion

    // region Inner classes

    static class ViewHolder {
        EditText nameEditText;
        EditText latitudeEditText;
        EditText longitudeEditText;
        EditText radiusEditText;

//    public void populate(View v) {
//        Log.i(Tag,"ViewHolder   populate");
//      nameEditText = (EditText) v.findViewById(R.id.fragment_add_geofence_name);
//      latitudeEditText = (EditText) v.findViewById(R.id.fragment_add_geofence_latitude);
//      longitudeEditText = (EditText) v.findViewById(R.id.fragment_add_geofence_longitude);
//     radiusEditText = (EditText) v.findViewById(R.id.fragment_add_geofence_radius);
//
//      latitudeEditText.setHint(String.format(v.getResources().getString(R.string.Hint_Latitude), Constants.Geometry.MinLatitude, Constants.Geometry.MaxLatitude));
//      longitudeEditText.setHint(String.format(v.getResources().getString(R.string.Hint_Longitude), Constants.Geometry.MinLongitude, Constants.Geometry.MaxLongitude));
//      radiusEditText.setHint(String.format(v.getResources().getString(R.string.Hint_Radius), Constants.Geometry.MinRadius, Constants.Geometry.MaxRadius));
//    }
//  }

        // endregion
    }
}