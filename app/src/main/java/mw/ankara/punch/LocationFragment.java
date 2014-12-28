package mw.ankara.punch;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationFragment.
     */
    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.location_ib_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocationClick(v);
            }
        });
    }

    public void onLocationClick(View view) {
        LocationManager manager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        manager.requestSingleUpdate(LocationManager.GPS_PROVIDER,
                new LocationSingleListener(), null);
    }

    private void updateLocation(Location location) {
        Toast.makeText(getActivity(), "latitude:" + location.getLatitude()
                + ", longitude:" + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    private class LocationSingleListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);

//            LocationManager manager = (LocationManager) getActivity()
//                    .getSystemService(Context.LOCATION_SERVICE);
//            manager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
