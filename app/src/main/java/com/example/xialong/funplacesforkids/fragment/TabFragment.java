package com.example.xialong.funplacesforkids.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xialong.funplacesforkids.R;
import com.example.xialong.funplacesforkids.data.Place;
import com.example.xialong.funplacesforkids.util.PlaceUtil;
import com.example.xialong.funplacesforkids.util.Util;

import org.json.JSONException;

public class TabFragment extends Fragment implements PlaceUtil.PlaceCallback {

    private static final String ARG_POSITION = "position";
    private int mPosition;
    private Place[] mPlaces;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private static final String TAG = TabFragment.class.getSimpleName();

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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mTextView = (TextView) rootView.findViewById(R.id.no_result);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        return rootView;
    }

    @Override
    public void getResponse(String result) {
        try {
            mPlaces = PlaceUtil.getPlaces(result);

            if(mPlaces!=null && mPlaces.length!=0){
                mTextView.setVisibility(View.GONE);
                Log.d(TAG, mPlaces.length + "");
                Log.d(TAG, mPlaces[0].getPlaceAddress() + " " + mPlaces[0].getPlaceName() + " " + mPlaces[0].getPlaceImageUrl()
                        + " " + mPlaces[0].getPlaceRating()+ " "+mPlaces[0].getLatitude()+" "+mPlaces[0].getLongitude()+" "+mPlaces[0].getPlaceId());
            }else{
                mTextView.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        PlaceAdapter adapter = new PlaceAdapter(getContext(), mPlaces);
        mRecyclerView.setAdapter(adapter);
    }

    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

        private Place[] items;
        private Context mContext;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mAddress;
            public final TextView mAddressName;
            public final RatingBar mRatingBar;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.place_image);
                mAddress = (TextView) view.findViewById(R.id.place_address);
                mAddressName = (TextView) view.findViewById(R.id.place_name);
                mRatingBar = (RatingBar) view.findViewById(R.id.place_rating);
            }
        }

        public PlaceAdapter(Context context, Place[] places) {
            items = places;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_place, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mAddressName.setText(items[position].getPlaceName());
            holder.mAddress.setText(items[position].getPlaceAddress());
            holder.mRatingBar.setRating(Util.parseRating(items[position].getPlaceRating()));
            String imageUrl = items[position].getPlaceImageUrl();
            if(!imageUrl.equals("na")){
                Glide.with(mContext)
                        .load(imageUrl)
                        .centerCrop()
                        .into(holder.mImageView);
            }else{
                Glide.with(mContext)
                        .load(R.drawable.placeholder)
                        .centerCrop()
                        .into(holder.mImageView);
            }
        }

        @Override
        public int getItemCount() {
            if (items != null) {
                return items.length;
            }
            return 0;
        }
    }
}
