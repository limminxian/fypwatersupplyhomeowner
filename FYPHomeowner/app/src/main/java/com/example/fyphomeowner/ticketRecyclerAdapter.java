package com.example.fyphomeowner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ticketRecyclerAdapter extends RecyclerView.Adapter<ticketRecyclerAdapter.MyViewHolder> {
    private ArrayList<Ticket> ticketList;
    private Context context;
    private TicketClickListener ticketClickListener;

    public interface TicketClickListener {
        void selectedTicket(Ticket ticket);
    }

    public void setFilteredList(ArrayList<Ticket> filteredList){
        this.ticketList = filteredList;
        notifyDataSetChanged();
    }

    public ticketRecyclerAdapter(Context context, ArrayList<Ticket> ticketList, TicketClickListener ticketClickListener) {
        this.context = context;
        this.ticketList = ticketList;
        this.ticketClickListener = ticketClickListener;
    }

    @NonNull
    @Override
    public ticketRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflates the layout (give look to rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.ticket_recycler_view, parent, false);

        return new ticketRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //assign values to views created in recycler layout file
        //based on position of recycler view
        Ticket ticket = ticketList.get(position);
        holder.dateTxt.setText("Date created: " + ticketList.get(position).getDate());
        holder.serviceTypeTxt.setText("Service Type: " + ticketList.get(position).getServiceType());
        holder.descriptionTxt.setText("Description: " + ticketList.get(position).getDescription());
        holder.statusTxt.setText("Status: " + ticketList.get(position).getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ticketClickListener.selectedTicket(ticket);

            }
        });
    }

    @Override
    public int getItemCount() {
        // number of items wanted to be displayed
        return ticketList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabs views from recycler layout file
        //similar to onCreate
        TextView dateTxt;
        TextView serviceTypeTxt;
        TextView descriptionTxt;
        TextView statusTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            serviceTypeTxt = itemView.findViewById(R.id.serviceTypeTxt);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
        }
    }
}

