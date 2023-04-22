package com.example.estatesolutions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    @NonNull
    Context context;
    ArrayList<PropertyDetails> propertyDetails;
    String currentUserUId;

    public MyAdapter(@NonNull Context context, ArrayList<PropertyDetails> propertyDetails,String currentUserUId) {
        this.context = context;
        this.propertyDetails = propertyDetails;
        this.currentUserUId=currentUserUId;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.single_show_details,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        PropertyDetails singleDetail=propertyDetails.get(position);

        holder.nameOfProject.setText(singleDetail.getName());
        holder.address.setText(singleDetail.getAddress());
        holder.price.setText(singleDetail.getPrice());
        String model = singleDetail.getModel();
        String area = singleDetail.getArea();
        String btnText = model + " | " + area;
        holder.modelBtn.setText(btnText);
    }

    @Override
    public int getItemCount() {
        return propertyDetails.size();
    }

    protected class MyViewHolder extends  RecyclerView.ViewHolder
    {
        TextView nameOfProject,price,address;
        MaterialButton modelBtn,getPPT;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.singleCardView);
            nameOfProject=itemView.findViewById(R.id.nameTextViewSingle);
            price=itemView.findViewById(R.id.priceTextViewSingle);
            address=itemView.findViewById(R.id.addressTextViewSingle);
            modelBtn=itemView.findViewById(R.id.modelSizeButton);
            getPPT=itemView.findViewById(R.id.getPPTBtnSingleView);

        }
    }
}
