package com.shadercat.havvka;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private FavouriteFragmentInteractionListener mListener;
    List<FavouriteSet> sets = new ArrayList<>();
    FavouriteListAdapter adapter;
    RecyclerView recyclerView;
    Context context;
    mWorkingThread parallelThread;
    Handler mUIHandler = new Handler();
    Runnable datadownloadTask;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.favouritesList);
        parallelThread = new mWorkingThread("favfragment");
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavouriteFragmentInteractionListener) {
            mListener = (FavouriteFragmentInteractionListener) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new FavouriteListAdapter(getContext(), sets);
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListeners(new FavouriteListAdapter.ClickListeners() {
            @Override
            public void onClick(int position) {
                Intent info = new Intent(getContext(), ShowFavouriteSetItems.class);
                info.putExtra(ShowFavouriteSetItems.FAVSET_ID, sets.get(position).getId());
                startActivity(info);
            }

            @Override
            public void longOnClick(int position) {
                DialogDelete(position);
            }

            @Override
            public void onClickAdd() {
                AddDialog();
            }
        });
        datadownloadTask = new Runnable() {
            @Override
            public void run() {
                sets = DataAdapter.GetFavouriteData(getContext());
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(sets);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        parallelThread.start();
        parallelThread.prepareHandler();
        parallelThread.postTask(datadownloadTask);
    }

    private void DialogDelete(final int pos) {
        AlertDialog.Builder ad;
        ad = new AlertDialog.Builder(context);
        ad.setMessage(getString(R.string.delete_question)); // сообщение
        ad.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Runnable deleteTask = new Runnable() {
                    @Override
                    public void run() {
                        final boolean flag = DataAdapter.DeleteSet(getContext(), sets.get(pos).getId());
                        mUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!flag) {
                                    Toast.makeText(getContext(), getString(R.string.delete_error), Toast.LENGTH_LONG).show();
                                } else {
                                    parallelThread.postTask(datadownloadTask);
                                }
                            }
                        });
                    }
                };
                parallelThread.postTask(deleteTask);
            }
        });
        ad.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.show();
    }

    @Override
    public void onResume() {
        if (parallelThread.isAlive() && datadownloadTask != null) {
            parallelThread.postTask(datadownloadTask);
        }
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        parallelThread.quit();
        super.onDestroy();
    }

    private void AddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View inflater = this.getLayoutInflater().inflate(R.layout.dialog_add_favourite_set, null);
        final EditText text = (EditText) inflater.findViewById(R.id.getNameDialog);
        builder.setView(inflater)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = text.getText().toString();
                        if (!name.isEmpty()) {
                            Runnable taskAdd = new Runnable() {
                                @Override
                                public void run() {
                                    FavouriteSet fs = new FavouriteSet(0, name, 0);
                                    final boolean flag = DataAdapter.SaveFavSet(getContext(), fs, true);
                                    mUIHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (flag) {
                                                Toast.makeText(getContext(), getString(R.string.addedNewFavSet), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), getString(R.string.add_error), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            };
                            if (parallelThread.isAlive() && datadownloadTask != null) {
                                parallelThread.postTask(taskAdd);
                                parallelThread.postTask(datadownloadTask);
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.notEmptyName), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public interface FavouriteFragmentInteractionListener {
        void FavouriteFragmentInteraction(Uri link);
    }
}
