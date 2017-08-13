package com.example.xialong.funplacesforkids.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xialong.funplacesforkids.R;
import com.example.xialong.funplacesforkids.data.Place;
import com.example.xialong.funplacesforkids.util.PlaceUtil;
import com.example.xialong.funplacesforkids.util.Util;

public class TabFragment extends Fragment implements  PlaceUtil.PlaceCallback{

    private static final String ARG_POSITION = "position";
    private int mPosition;
    private Place[] mPlaces;

    public static TabFragment newInstance(int position) {
        TabFragment f = new TabFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
        PlaceUtil.startVolleyRequest(getContext(), TabFragment.this, Util.getPlaceTypes().get(mPosition));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);

        return rootView;
    }

    @Override
    public void getResponse(String result) {
        Log.d("api response=",result);
        mPlaces = PlaceUtil.getPlaces(result);
    }

    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder>{

        public class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View view){
                super(view);

            }
        }

        public PlaceAdapter(){

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_place,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position){

        }

        @Override
        public int getItemCount(){
            return 0;
        }
    }
}
