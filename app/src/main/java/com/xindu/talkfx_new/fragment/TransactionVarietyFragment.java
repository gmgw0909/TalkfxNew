package com.xindu.talkfx_new.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.R;
import com.xindu.talkfx_new.activity.AddMoreJYPZActivity;
import com.xindu.talkfx_new.activity.SearchActivity;
import com.xindu.talkfx_new.adapter.CurrencyPairAdapter;
import com.xindu.talkfx_new.adapter.JYPZEditAdapter;
import com.xindu.talkfx_new.adapter.SecondaryListAdapter;
import com.xindu.talkfx_new.base.BaseFragment;
import com.xindu.talkfx_new.base.BaseResponse;
import com.xindu.talkfx_new.base.Constants;
import com.xindu.talkfx_new.base.MJsonCallBack;
import com.xindu.talkfx_new.base.helper.OnStartDragListener;
import com.xindu.talkfx_new.base.helper.SimpleItemTouchHelperCallback;
import com.xindu.talkfx_new.bean.CurrencyResponse;
import com.xindu.talkfx_new.bean.TVInfo;
import com.xindu.talkfx_new.bean.WebSocketInfo;
import com.xindu.talkfx_new.utils.Utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
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
    private List<SecondaryListAdapter.DataTree<String, TVInfo>> dataTrees = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private boolean hasLoad = false;

    CurrencyPairAdapter adapter;
    JYPZEditAdapter adapter2;
    LinearLayoutManager linearLayoutManager;
    Gson gson = new Gson();

    @Override
    protected int setContentView() {
        return R.layout.fragment_transaction_variety;
    }

    @Override
    protected void lazyLoad() {
        if (!hasLoad) {
            getMajorPair();
            recyclerView1.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView1.setLayoutManager(linearLayoutManager);
            adapter = new CurrencyPairAdapter(getActivity());
            adapter.setItemOpen(true);
            adapter.setData(dataTrees);
            adapter.setCanClosed(false);
            recyclerView1.setAdapter(adapter);

            recyclerView2.setHasFixedSize(true);
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter2 = new JYPZEditAdapter(getActivity(), this, list);
            recyclerView2.setAdapter(adapter2);
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter2);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recyclerView2);
            // 保留两位小数
            nf.setMaximumFractionDigits(5);
            // 如果不需要四舍五入，可以使用RoundingMode.DOWN
            nf.setRoundingMode(RoundingMode.UP);
            hasLoad = true;
        }
    }

    private void getMajorPair() {
        OkGo.<BaseResponse<CurrencyResponse>>get(Constants.baseDataUrl + "/dCurrency")
                .execute(new MJsonCallBack<BaseResponse<CurrencyResponse>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<CurrencyResponse>> response) {
                        if (response.body().datas != null) {
                            CurrencyResponse results = response.body().datas;
                            if (results != null && results.currencys != null && results.currencys.size() > 0) {
                                for (int i = 0; i < results.currencys.size(); i++) {
                                    TVInfo info = new TVInfo();
                                    info.name = "HaHa";
                                    list.add(info);
                                    list.addAll(results.currencys.get(i).list);
                                    dataTrees.add(new SecondaryListAdapter.DataTree<String, TVInfo>(results.currencys.get(i).group, results.currencys.get(i).list));
                                }
                                adapter.notifyNewData(dataTrees);
                                getWebsocket();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<CurrencyResponse>> response) {
                        //显示数据加载失败,点击重试
                        Utils.errorResponse(getActivity(), response);
                    }
                });
    }

    private void getWebsocket() {
        try {
            ExampleClient c = new ExampleClient(new URI("ws://gwt.talkfx.com:3677"), new Draft_17());
            c.connectBlocking();
            c.send("我是Android");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    NumberFormat nf = NumberFormat.getNumberInstance();

    class ExampleClient extends WebSocketClient {

        public ExampleClient(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        public ExampleClient(URI serverURI) {
            super(serverURI);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            System.out.println("opened connection");
            // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
        }

        WebSocketInfo info;

        @Override
        public void onMessage(final String message) {
            info = gson.fromJson(message, WebSocketInfo.class);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < list.size(); i++) {
                        if (info.i.equals(list.get(i).name)) {
                            if (i == 0 || i == 7 || i == 15 || i == 22 || i == 30) {
                                Log.d("Ok_====::", "i == 0 || i == 7 || i == 15 || i == 22 || i == 30");
                            } else {
                                View view = linearLayoutManager.findViewByPosition(i);
                                if (view != null) {
                                    LinearLayout layout = (LinearLayout) view;
                                    TextView upDown = (TextView) layout.findViewById(R.id.up_down);
                                    TextView price = (TextView) layout.findViewById(R.id.price);
                                    price.setText(nf.format(info.price));
                                    if (info.nch < 0.00000D) {
                                        Log.d("Ok_===0===::", "<<<<<<0");
                                        upDown.setTextColor(getActivity().getResources().getColor(R.color.red));
                                    } else {
                                        Log.d("Ok_===0===:", ">>>>>>0");
                                        upDown.setTextColor(getActivity().getResources().getColor(R.color.green));
                                    }
                                    upDown.setText(nf.format(info.nch));
                                }
                            }
                        }
                    }
                }
            });
            Log.d("Ok_received:", message);
        }

        @Override
        public void onFragment(Framedata fragment) {
            System.out.println("received fragment: " + new String(fragment.getPayloadData().array()));
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            // The codecodes are documented in class org.java_websocket.framing.CloseFrame
            System.out.println("Connection closed by " + (remote ? "remote peer" : "us"));
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
            // if the error is fatal then onClose will be called additionally
        }
    }

    @OnClick({R.id.add, R.id.edit, R.id.search, R.id.back, R.id.delete})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.add:
                startActivity(new Intent(getActivity(), AddMoreJYPZActivity.class));
                break;
            case R.id.edit:
                adapter2.notifyDataSetChanged();
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
                startActivity(new Intent(getActivity(), SearchActivity.class));
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
            case R.id.delete:
                List<TVInfo> list_ = new ArrayList<>();
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isCheck) {
                            list_.add(list.get(i));
                        }
                    }
                    list.removeAll(list_);
                    adapter2.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
