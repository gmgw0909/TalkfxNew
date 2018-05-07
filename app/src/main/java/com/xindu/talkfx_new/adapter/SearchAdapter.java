package com.xindu.talkfx_new.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.ItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rusan on 2017/5/15.
 */

public class SearchAdapter extends SecondaryListAdapter<SearchAdapter.GroupItemViewHolder, SearchAdapter.SubItemViewHolder> {


    private Context context;

    private List<DataTree<String, ItemInfo>> dts = new ArrayList<>();

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public void setData(List data) {
        dts = data;
        notifyNewData(dts);
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_group, parent, false);

        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_sub, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex) {

        ((GroupItemViewHolder) holder).tvGroup.setText(dts.get(groupItemIndex).getGroupItem());

    }

    @Override
    public void onSubItemBindViewHolder(final RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {

        if (subItemIndex % 2 == 0) {
            ((SubItemViewHolder) holder).item.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            ((SubItemViewHolder) holder).item.setBackgroundColor(context.getResources().getColor(R.color.bg_normal));
        }
        ((SubItemViewHolder) holder).tvSub.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex).a);
        ((SubItemViewHolder) holder).tv1.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex).b);
        ((SubItemViewHolder) holder).add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SubItemViewHolder) holder).add.setText("已加入");
                ((SubItemViewHolder) holder).add.setTextColor(context.getResources().getColor(R.color.text_gray));
            }
        });

    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {

        Toast.makeText(context, "group item " + String.valueOf(groupItemIndex) + " is expand " +
                String.valueOf(isExpand), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {

        Toast.makeText(context, "sub item " + String.valueOf(subItemIndex) + " in group item " +
                String.valueOf(groupItemIndex), Toast.LENGTH_SHORT).show();

    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroup;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            tvGroup = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout item;
        TextView tvSub;
        TextView tv1;
        TextView add;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.item_bg);
            tvSub = (TextView) itemView.findViewById(R.id.tv);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            add = (TextView) itemView.findViewById(R.id.add);
        }
    }


}
