package com.xindu.talkfx_new.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.adapter.JYEditAdapter;
import com.xindu.talkfx_new.adapter.JYPZEditAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.base.helper.OnStartDragListener;
import com.xindu.talkfx_new.base.helper.SimpleItemTouchHelperCallback;
import com.xindu.talkfx_new.bean.TVInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2018/3/12.
 */

public class TransactionVarietyFragment extends BaseFragment implements OnStartDragListener {


    @Bind(R.id.recycler_view1)
    RecyclerView recyclerView1;
    @Bind(R.id.recycler_view2)
    RecyclerView recyclerView2;
    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.ll2)
    LinearLayout ll2;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.edit)
    ImageView edit;
    @Bind(R.id.search)
    ImageView search;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.delete)
    ImageView delete;

    List<TVInfo> list = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private boolean hasLoad = false;

    JYPZEditAdapter adapter;
    JYEditAdapter adapter2;

    @Override
    protected int setContentView() {
        return R.layout.fragment_transaction_variety;
    }

    @Override
    protected void lazyLoad() {
        if (!hasLoad) {


            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));
            list.add(new TVInfo("欧元/美元", "1.23978", "1.53899", "+0.19383"));

            recyclerView1.setHasFixedSize(true);
            recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new JYPZEditAdapter(list);
            recyclerView1.setAdapter(adapter);

            recyclerView2.setHasFixedSize(true);
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter2 = new JYEditAdapter(getActivity(), this, list);
            recyclerView2.setAdapter(adapter2);
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter2);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recyclerView2);

            hasLoad = true;
        }
    }

    @OnClick({R.id.add, R.id.edit, R.id.search, R.id.back})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.add:
                break;
            case R.id.edit:
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                add.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                break;
            case R.id.search:
                break;
            case R.id.back:
                adapter.notifyDataSetChanged();
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
