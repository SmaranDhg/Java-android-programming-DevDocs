package redeem.androidjava;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.listener.ITableViewListener;

import java.util.ArrayList;

public class AdapterForTable extends AbstractTableAdapter{

    private  TableInfo tableInfo;
    public AdapterForTable(Context context,TableInfo tableInfo) {
        super(context);
        this.tableInfo=tableInfo;
    }

    //these are for the diffrent layout for diffrent coulumn index
    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        return new AdapterForTable.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.l_table_cell,parent,false));
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        ((ViewHolder) holder).cell.setText(tableInfo.datAt(columnPosition,rowPosition));//here view holder is sub class of abracteviewholder so anyway
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        return  new AdapterForTable.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.l_table_cell,parent,false));
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {

        ((ViewHolder) holder).cell.setText(tableInfo.titleOf(columnPosition));

    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        return  new AdapterForTable.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.l_table_cell,parent,false));
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {

        ((ViewHolder) holder).cell.setText(String.valueOf(rowPosition));
    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.l_table_cell,null,false);
    }


    public class ViewHolder extends AbstractViewHolder
    {
        TextView cell;
        public ViewHolder(View itemView) {
            super(itemView);
            cell=itemView.findViewById(R.id.Cell);

        }
    }


}
class TableActionListener implements ITableViewListener {
    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {

    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {

    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {

    }

}

