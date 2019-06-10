package com.shadercat.havvka;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private ListFragmentInteractionListener mListener;
    List<Item> items = new ArrayList<>();
    ItemListAdapter adapter;
    RecyclerView listView;
    Context context;
    Animation rotateAnim;
    Animation stop;
    FloatingActionButton fab;
    mWorkingThread parallelThread;
    Handler mUIHandler = new Handler();

    int sortInt = 0;

    public ListFragment() {
        // Required empty public constructor
    }


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listView = (RecyclerView) view.findViewById(R.id.itemslist);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListFragmentInteractionListener) {
            mListener = (ListFragmentInteractionListener) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parallelThread = new mWorkingThread("listitems");
        parallelThread.start();
        parallelThread.prepareHandler();
        rotateAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_logo);
        stop = AnimationUtils.loadAnimation(context, R.anim.shake_logo);
        adapter = new ItemListAdapter(context, items);
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(itemDecor);
        listView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortDialog();
            }
        });
        adapter.setOnClickListeners(new ItemListAdapter.ClickListeners() {
            @Override
            public void OnClick(int position) {
                Intent product_info = new Intent(context, InformationActivity.class);
                product_info.putExtra(Item.class.getSimpleName(), items.get(position).getID());
                startActivity(product_info);
            }

            @Override
            public void StartAnim(View view) {
                if (rotateAnim != null) {
                    ((ImageView) view).startAnimation(rotateAnim);
                }
            }

            @Override
            public void StopAnim(View view) {
                if (stop != null) {
                    ((ImageView) view).startAnimation(stop);
                }
            }

            @Override
            public void LoadingImage(View view, final int pos) {
                new LoadImage((ImageView) view, new IPermissionForSet() {
                    @Override
                    public boolean isInView() {
                        return isVisibleItem(pos);
                    }
                }).execute(items.get(pos));
            }
        });

        Runnable task = new Runnable() {
            @Override
            public void run() {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.StartAnim();
                    }
                });
                items = DataAdapter.GetProductList(context, DataAdapter.SORT_MODE_DEFAULT);
                adapter.setItems(items);
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.StopAnim();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        parallelThread.postTask(task);
    }

    private void SortDialog() {
        final String[] sortName = {getString(R.string.sort_rating),
                getString(R.string.sort_first),
                getString(R.string.sort_second),
                getString(R.string.sort_desert),
                getString(R.string.sort_drinks)};

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.choose_sort)); // заголовок для диалога
        builder.setItems(sortName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int item) {
                Runnable taskSort = new Runnable() {
                    @Override
                    public void run() {
                        items = DataAdapter.GetProductList(context, item);
                        adapter.setItems(items);
                        mUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
                parallelThread.postTask(taskSort);
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        if (parallelThread != null) {
            parallelThread.quit();
        }
        super.onDestroy();
    }

    public boolean isVisibleItem(int i) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) listView.getLayoutManager());
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        return (first <= i && i <= last);
    }

    public interface ListFragmentInteractionListener {
        void ListFragmentInteraction(Uri link);
    }
}
