package redeem.androidjava;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

import static redeem.androidjava.ContentForAndroid.*;
import static redeem.androidjava.RawDatas.Cat_packages;
import static redeem.androidjava.RawDatas.libraries;

public class A_Adapter_forLibraries extends RecyclerView.Adapter<A_Adapter_forLibraries.ViewHolderForLibraries> {
    private ArrayList<A_libraryInfo> datas = new ArrayList<>();
    private A_Adapter_forLibraries.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(A_Adapter_forLibraries.OnItemClickListener listener) {
        this.listener = listener;
    }


    public A_Adapter_forLibraries(ArrayList<A_libraryInfo> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public ViewHolderForLibraries onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.a_l_libraries, viewGroup, false);
        //for theming
        crdSetTheme(view);
        ViewHolderForLibraries vh = new ViewHolderForLibraries(view, (OnItemClickListener) this.listener);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderForLibraries viewHolderForLibraries, int i) {
        viewHolderForLibraries.LibraryName.setText(datas.get(i).getLibraryName());
        viewHolderForLibraries.Descrition.setText(datas.get(i).getDescLibrary());
        viewHolderForLibraries.Categoriz.setText("Categories:\n"+Cat_packages[0]);
        for (int l=1;l<8;l++)
        viewHolderForLibraries.Categoriz.append("\n"+Cat_packages[l]);




    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolderForLibraries extends RecyclerView.ViewHolder {
        TextView LibraryName;
        TextView Descrition, Categoriz;

        public ViewHolderForLibraries(View view, final OnItemClickListener listener) {
            super(view);
            LibraryName = view.findViewById(R.id.ALibraryName);
            Descrition = view.findViewById(R.id.ALibraryDescrition);
            Categoriz = view.findViewById(R.id.AlibraryCats);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bounch);
                    animation.setDuration(1000);
                    v.startAnimation(animation);
                    return false;
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        int i = getAdapterPosition();
                        if (i != RecyclerView.NO_POSITION) {
                            listener.onItemClick(i);
                        }
                    }

                }
            });


        }
    }


}
