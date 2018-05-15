package com.xindu.talkfx_new.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.bean.TVInfo;
import com.xindu.talkfx_new.bean.ViewPos;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rusan on 2017/5/15.
 */

public class CurrencyPairAdapter extends SecondaryListAdapter<CurrencyPairAdapter.GroupItemViewHolder, CurrencyPairAdapter.SubItemViewHolder> {


    private Context context;

    private List<DataTree<String, TVInfo>> dts = new ArrayList<>();

    public CurrencyPairAdapter(Context context) {
        this.context = context;
        nf.setGroupingUsed(false);
    }

    public void setData(List data) {
        dts = data;
        notifyNewData(dts);
    }

    NumberFormat nf = NumberFormat.getInstance();

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_group, parent, false);

        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_variety, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex) {

        ((GroupItemViewHolder) holder).tvGroup.setText(dts.get(groupItemIndex).getGroupItem());

    }

    @Override
    public void onSubItemBindViewHolder(final RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {
        holder.itemView.setTag(new ViewPos(groupItemIndex, subItemIndex));
        if (subItemIndex % 2 == 0) {
            ((SubItemViewHolder) holder).ll.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            ((SubItemViewHolder) holder).ll.setBackgroundColor(context.getResources().getColor(R.color.bg_normal));
        }
        ((SubItemViewHolder) holder).name.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex).name);
        ((SubItemViewHolder) holder).price.setText(new BigDecimal(dts.get(groupItemIndex).getSubItems().get(subItemIndex).last + "").toString());
        ((SubItemViewHolder) holder).up_down.setText(new BigDecimal(dts.get(groupItemIndex).getSubItems().get(subItemIndex).dailyChange + "").toString());
    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {
    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {
    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvGroup;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            tvGroup = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll;
        TextView name;
        TextView price;
        TextView up_down;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            up_down = (TextView) itemView.findViewById(R.id.up_down);
        }
    }


}
