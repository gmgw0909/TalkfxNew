package com.xindu.talkfx_new.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.NoticeSettingActivity;
import com.xindu.talkfx_new.base.helper.ItemTouchHelperAdapter;
import com.xindu.talkfx_new.base.helper.ItemTouchHelperViewHolder;
import com.xindu.talkfx_new.base.helper.OnStartDragListener;
import com.xindu.talkfx_new.bean.TVInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class JYPZEditAdapter extends RecyclerView.Adapter<JYPZEditAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    List<TVInfo> mItems = new ArrayList<>();
    Context context;
    private final OnStartDragListener mDragStartListener;

    public JYPZEditAdapter(Context context, OnStartDragListener dragStartListener, List<TVInfo> mItems) {
        mDragStartListener = dragStartListener;
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_variety_edit, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.text.setText(mItems.get(position).name);
        if (mItems.get(position).isCheck) {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.jypz_checked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.text.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.jypz_unchecked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.text.setCompoundDrawables(drawable, null, null, null);
        }
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItems.get(position).isCheck) {
                    mItems.get(position).setCheck(false);
                } else {
                    mItems.get(position).setCheck(true);
                }
                notifyDataSetChanged();
            }
        });
        holder.notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NoticeSettingActivity.class));
            }
        });
        if (position % 2 == 0) {
            holder.ll.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.ll.setBackgroundColor(context.getResources().getColor(R.color.bg_normal));
        }
        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView text;
        public final TextView notice;
        public final ImageView handleView;
        public final LinearLayout ll;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            notice = (TextView) itemView.findViewById(R.id.notice);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
