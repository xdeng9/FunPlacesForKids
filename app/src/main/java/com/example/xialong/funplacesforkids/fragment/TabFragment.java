package com.example.xialong.funplacesforkids.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xialong.funplacesforkids.DetailActivity;
import com.example.xialong.funplacesforkids.R;
import com.example.xialong.funplacesforkids.data.Place;
import com.example.xialong.funplacesforkids.data.PlaceContract;
import com.example.xialong.funplacesforkids.util.Util;

public class TabFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_POSITION = "position";
    private int mPosition;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private PlaceAdapter mAdapter;
    private ProgressBar mProgressBar;

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
        mAdapter = new PlaceAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mTextView = (TextView) rootView.findViewById(R.id.no_result);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new PlaceAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        showLoading();
        getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args) {
        return new CursorLoader(
                getActivity(),
                PlaceContract.PlaceEntry.CONTENT_URI,
                null,
                PlaceContract.PlaceEntry.COLUMN_PLACE_TYPE+ " =?",
                new String[]{Util.getPlaceTypes().get(mPosition)},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(cursor!=null && cursor.getCount()!=0){
            mTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else{
                mTextView.setVisibility(View.VISIBLE);
            }
        mAdapter.swapCursor(cursor);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

        private Context mContext;
        private Cursor mCursor;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mAddress;
            public final TextView mAddressName;
            public final RatingBar mRatingBar;
            public final TextView mDistance;
            public final CardView mCardView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.place_image);
                mAddress = (TextView) view.findViewById(R.id.place_address);
                mAddressName = (TextView) view.findViewById(R.id.place_name);
                mRatingBar = (RatingBar) view.findViewById(R.id.place_rating);
                mDistance = (TextView) view.findViewById(R.id.distance);
                mCardView = (CardView) view.findViewById(R.id.card_view);
            }
        }

        public PlaceAdapter(Context context) {
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
            mCursor.moveToPosition(position);

            String name = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_NAME));
            String address = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_ADDRESS));
            String rating = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_RATING));
            String imageUrl = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_IMAGE_URL));
            double lat = mCursor.getDouble(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_LATITUDE));
            double lon = mCursor.getDouble(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_LONGITUDE));
            String id = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ID));
            int fav = mCursor.getInt(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_ISFAV));

            holder.mAddressName.setText(name);
            holder.mAddress.setText(address);
            holder.mRatingBar.setRating(Util.parseRating(rating));

            if (!imageUrl.equals("na")) {
                Glide.with(mContext)
                        .load(imageUrl)
                        .centerCrop()
                        .into(holder.mImageView);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.placeholder)
                        .centerCrop()
                        .into(holder.mImageView);
            }
            holder.mDistance.setText(Util.getDistance(lat, lon) + " mi");

            final Place place = new Place(name, imageUrl, rating, address, lat, lon, id, fav);
            holder.mCardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    bundle.putParcelable("key", place);
                    intent.putExtra("bundle", bundle);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mCursor == null) {
                return 0;
            }else{
                return mCursor.getCount();
            }
        }

        public void swapCursor(Cursor cursor){
            mCursor = cursor;
            notifyDataSetChanged();
        }
    }
}
