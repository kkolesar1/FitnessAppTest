package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAdapter extends ArrayAdapter<String> {

    private int[] spinnerImages;
    private Context mContext;

    CustomAdapter(@NonNull Context context, int[] images) {
        super(context, R.layout.row);
        this.spinnerImages = images;
        this.mContext = context;
    }

    @Override
    public int getCount(){
        return spinnerImages.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null){
            LayoutInflater mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            try {
                convertView = mInflater.inflate(R.layout.row, parent, false);
                mViewHolder.mFlag = convertView.findViewById(R.id.ivFlag);
                convertView.setTag(mViewHolder);
            }
            catch(NullPointerException e){
                System.out.println("An Error Occurred: NullPointer_Exception_getView" + e);
            }
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(spinnerImages[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView mFlag;
    }
}
