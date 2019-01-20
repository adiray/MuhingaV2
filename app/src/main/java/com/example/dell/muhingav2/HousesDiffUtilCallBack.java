package com.example.dell.muhingav2;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.mikepenz.fastadapter.commons.utils.DiffCallback;

import java.util.ArrayList;

public class HousesDiffUtilCallBack extends DiffUtil.Callback {


    ArrayList<HousesResponse> filteredHousesResponseArray = new ArrayList<HousesResponse>(), allHousesResponseArray = new ArrayList<HousesResponse>();

    public HousesDiffUtilCallBack(ArrayList<HousesResponse> filteredHousesResponseArray, ArrayList<HousesResponse> allHousesResponseArray) {
        this.filteredHousesResponseArray = filteredHousesResponseArray;
        this.allHousesResponseArray = allHousesResponseArray;
    }

    @Override
    public int getOldListSize() {
        return allHousesResponseArray.size();
    }

    @Override
    public int getNewListSize() {
        return filteredHousesResponseArray.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return allHousesResponseArray.get(oldItemPosition).getObjectId() == filteredHousesResponseArray.get(newItemPosition).getObjectId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return allHousesResponseArray.get(oldItemPosition).getTitle().equals(filteredHousesResponseArray.get(newItemPosition).getTitle());
    }


}



