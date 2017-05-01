package com.mindmesolo.mindme.Notifcations;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindmesolo.mindme.R;
import com.mindmesolo.mindme.RecycleViewHelper.Extension;
import com.mindmesolo.mindme.RecycleViewHelper.ItemTouchHelperExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_RECYCLER_WIDTH = 1000;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;
    private final DeleteListener deleteListener;
    private List<NotificationModal> ListItems;
    private Context mContext;
    private ItemTouchHelperExtension mItemTouchHelperExtension;

    public MainRecyclerAdapter(Context context, DeleteListener deleteListener, ItemTouchHelperExtension itemTouchHelperExtension) {
        this.ListItems = new ArrayList<>();
        this.mContext = context;
        this.deleteListener = deleteListener;
        this.mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    public void updateData(List<NotificationModal> data) {
        ListItems.clear();
        ListItems.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_main, parent, false);
        if (viewType == ITEM_TYPE_ACTION_WIDTH) return new ItemSwipeWithActionWidthViewHolder(view);
        if (viewType == ITEM_TYPE_RECYCLER_WIDTH) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_with_single_delete, parent, false);
            return new ItemViewHolderWithRecyclerWidth(view);
        }
        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemBaseViewHolder baseViewHolder = (ItemBaseViewHolder) holder;
        baseViewHolder.bind(ListItems.get(position));
        baseViewHolder.MainRecycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIntent(holder.getAdapterPosition());
            }
        });
        if (holder instanceof ItemViewHolderWithRecyclerWidth) {
            ItemViewHolderWithRecyclerWidth viewHolder = (ItemViewHolderWithRecyclerWidth) holder;
            viewHolder.mActionViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doDelete(holder.getAdapterPosition());
                }
            });
        } else if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
//            viewHolder.mActionViewRefresh.setOnClickListener(
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Toast.makeText(mContext, "Refresh Click" + holder.getAdapterPosition()
//                                    , Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//            );
            viewHolder.mActionViewDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doDelete(holder.getAdapterPosition());
                        }
                    }

            );
        }
    }

    private void openIntent(int Position) {
        deleteListener.GetDataFromServer(ListItems.get(Position));
    }

    //confirm if user wants remove List
    private void doDelete(int adapterPosition) {
        final NotificationModal item = ListItems.get(adapterPosition);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setMessage("Are you sure want to remove this notification?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteListener.NotifyDataDelete(item);
                ListItems.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    public void move(int fromPosition, int toPosition) {
        Collections.swap(ListItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemViewType(int position) {
        if (ListItems.get(position).position == 1) {
            return ITEM_TYPE_ACTION_WIDTH_NO_SPRING;
        }
        if (ListItems.get(position).position == 2) {
            return ITEM_TYPE_RECYCLER_WIDTH;
        }
        return ITEM_TYPE_ACTION_WIDTH;
    }

    @Override
    public int getItemCount() {
        return ListItems.size();
    }

    public class ItemBaseViewHolder extends RecyclerView.ViewHolder {
        View MainRecycleView;
        View mActionContainer;
        TextView tv_main_title;
        TextView tv_main_date;
        ImageView imageView;
        Button button;

        public ItemBaseViewHolder(View itemView) {

            super(itemView);

            tv_main_title = (TextView) itemView.findViewById(R.id.tv_main_title);

            tv_main_date = (TextView) itemView.findViewById(R.id.tv_main_date);

            imageView = (ImageView) itemView.findViewById(R.id.dot);

            button = (Button) itemView.findViewById(R.id.btntype);

            MainRecycleView = itemView.findViewById(R.id.MainRecycleView);

            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        }

        public void bind(NotificationModal notificationModal) {
            tv_main_title.setText(notificationModal.title);

            // SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy @ hh:mm a");
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd yyyy @ hh:mm a");

            final Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(Long.parseLong(notificationModal.getDate()));

            tv_main_date.setText(formatter.format(calendar.getTime()));

            boolean b = notificationModal.isRead();
            if (b == false) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mItemTouchHelperExtension.startDrag(ItemBaseViewHolder.this);
                    }
                    return true;
                }
            });
        }
    }

    class ItemViewHolderWithRecyclerWidth extends ItemBaseViewHolder {

        View mActionViewDelete;

        public ItemViewHolderWithRecyclerWidth(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
        }

    }

    class ItemSwipeWithActionWidthViewHolder extends ItemBaseViewHolder implements Extension {

        View mActionViewDelete;
        View mActionViewRefresh;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
            //mActionViewRefresh = itemView.findViewById(R.id.view_list_repo_action_update);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    public class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

}
