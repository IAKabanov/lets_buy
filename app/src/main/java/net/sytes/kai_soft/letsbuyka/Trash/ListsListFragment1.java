package net.sytes.kai_soft.letsbuyka.Trash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sytes.kai_soft.letsbuyka.CRUDdb;
import net.sytes.kai_soft.letsbuyka.IListActivityContract;
import net.sytes.kai_soft.letsbuyka.Lists.AdapterListsList;
import net.sytes.kai_soft.letsbuyka.Lists.List;
import net.sytes.kai_soft.letsbuyka.SwipeController;
import net.sytes.kai_soft.letsbuyka.Application;
import net.sytes.kai_soft.letsbuyka.Constants;
import net.sytes.kai_soft.letsbuyka.DataBase;
import net.sytes.kai_soft.letsbuyka.IFilterContract;
import net.sytes.kai_soft.letsbuyka.R;
import net.sytes.kai_soft.letsbuyka.SwipeControllerActions;

import java.util.ArrayList;

/**
 * Created by Лунтя on 06.06.2018.
 */
/*  Фрагмент списка списков */
public class ListsListFragment1 extends Fragment implements View.OnClickListener, IFilterContract {

    //private Button goToDetailBtn;
    private RecyclerView recyclerView;
    private TextView emptyList;
    private DataBase dbList;
    private FloatingActionButton fabAdd;
    IListActivityContract iListActivityContract;
    SwipeController swipeController = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lists_list, container, false);

        fabAdd = rootView.findViewById(R.id.fabAddList);
        fabAdd.setOnClickListener(this);
        emptyList = rootView.findViewById(R.id.emptyList);

        recyclerView = rootView.findViewById(R.id.rvListsList);


        dbList = Application.Companion.getDB();

        refresh(null);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IListActivityContract) {
            iListActivityContract = (IListActivityContract) context;
        }
    }

    @Override
    public void onDetach() {
        iListActivityContract = null;
        super.onDetach();
    }

    private void refresh(@Nullable ArrayList<List> newLists) {

        if (newLists == null) {

            ArrayList<List> lists = CRUDdb.Companion.readFromTableLists(null);

            if (lists.size() == 0){
                emptyList.setVisibility(View.VISIBLE);
            }else{
                emptyList.setVisibility(View.GONE);
            }
            displayRW(lists);
        } else {
            displayRW(newLists);
        }
    }

    public void displayRW(ArrayList<List> lists) {

        AdapterListsList adapter = new AdapterListsList(lists, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        /*swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightSwiped(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.delete))
                        .setMessage(getString(R.string.deleteList))
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //swipeController.setNeedSwipeBack(true);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                CRUDdb.Companion.deleteItemLists(lists.get(position));
                                lists.remove(position);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onLeftSwiped(int position) {
                iListActivityContract.onListItemLongClick(lists.get(position));
            }
        });*/

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);



        //recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // необязательно
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        //RecyclerTouchListener touchListener = new RecyclerTouchListener(this.getActivity(), recyclerView);
        /*touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                Toast.makeText(getContext(),"onRowClicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {
                Toast.makeText(getContext(),"onIndependentViewClicked", Toast.LENGTH_SHORT).show();
            }
        });
                .setSwipeOptionViews(R.id.delete_task, R.id.edit_task);*/

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.fabAddList):
                iListActivityContract.onListFragmentButtonClick();
                break;
        }

    }

    @Override
    public void onFilterMake(String filter) {
        setFilter(filter);
    }

    private void setFilter(String filter) {
        if (filter.length() == 0) {
            refresh(null);
        } else {
            SQLiteDatabase db = dbList.getWritableDatabase();
            ArrayList<List> lists = new ArrayList<>();

            Cursor c = db.rawQuery("select * from " + Constants.TABLE_NAME_LISTS +
                            " where " + Constants.TABLE_ITEM_NAME + " like '%"
                            + filter + "%'",
                    null);
            if (c.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex(Constants.TABLE_ID);
                int nameColIndex = c.getColumnIndex(Constants.TABLE_ITEM_NAME);
                do {
                    lists.add(new List(c.getInt(idColIndex), c.getString(nameColIndex)));
                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                } while (c.moveToNext());


            }
            c.close();
            refresh(lists);
        }
    }
}
