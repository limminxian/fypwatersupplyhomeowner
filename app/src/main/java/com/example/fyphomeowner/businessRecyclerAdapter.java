package com.example.fyphomeowner;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class businessRecyclerAdapter extends RecyclerView.Adapter<businessRecyclerAdapter.MyViewHolder> {
    private ArrayList<Company> businessList = new ArrayList<>();
    private Context context;
    private BusinessClickListner businessClickListner;

    public interface BusinessClickListner{
        void selectedBusiness(Company company);
    }

    public void setFilteredList(ArrayList<Company> filteredList){
        this.businessList = filteredList;
        notifyDataSetChanged();
    }

    public businessRecyclerAdapter(Context context, ArrayList<Company> businessList, BusinessClickListner businessClickListner) {
        this.context = context;
        this.businessList = businessList;
        this.businessClickListner = businessClickListner;
    }

    @NonNull
    @Override
    public businessRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflates the layout (give look to rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.business_recycler_view, parent, false);

        return new businessRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //assign values to views created in recycler layout file
        //based on position of recycler view
        Company company = businessList.get(position);
        holder.businessTitle.setText(businessList.get(position).getName());
        holder.businessDescription.setText(businessList.get(position).getDescription());
        holder.businessImage.setImageResource(R.drawable.ic_businesses);
        Integer rating = 0;
        if(businessList.get(position).getNoOfStars()!=null){
            rating = businessList.get(position).getNoOfStars();
        } else {
            rating = 0;
        }

        switch (rating){
            case 0:
                holder.star1.setImageResource(R.drawable.ic_empty_star);
                holder.star2.setImageResource(R.drawable.ic_empty_star);
                holder.star3.setImageResource(R.drawable.ic_empty_star);
                holder.star4.setImageResource(R.drawable.ic_empty_star);
                holder.star5.setImageResource(R.drawable.ic_empty_star);
                break;
            case 1:
                holder.star1.setImageResource(R.drawable.ic_star);
                holder.star2.setImageResource(R.drawable.ic_empty_star);
                holder.star3.setImageResource(R.drawable.ic_empty_star);
                holder.star4.setImageResource(R.drawable.ic_empty_star);
                holder.star5.setImageResource(R.drawable.ic_empty_star);
                break;
            case 2:
                holder.star1.setImageResource(R.drawable.ic_star);
                holder.star2.setImageResource(R.drawable.ic_star);
                holder.star3.setImageResource(R.drawable.ic_empty_star);
                holder.star4.setImageResource(R.drawable.ic_empty_star);
                holder.star5.setImageResource(R.drawable.ic_empty_star);
                break;
            case 3:
                holder.star1.setImageResource(R.drawable.ic_star);
                holder.star2.setImageResource(R.drawable.ic_star);
                holder.star3.setImageResource(R.drawable.ic_star);
                holder.star4.setImageResource(R.drawable.ic_empty_star);
                holder.star5.setImageResource(R.drawable.ic_empty_star);
                break;
            case 4:
                holder.star1.setImageResource(R.drawable.ic_star);
                holder.star2.setImageResource(R.drawable.ic_star);
                holder.star3.setImageResource(R.drawable.ic_star);
                holder.star4.setImageResource(R.drawable.ic_star);
                holder.star5.setImageResource(R.drawable.ic_empty_star);
                break;
            case 5:
                holder.star1.setImageResource(R.drawable.ic_star);
                holder.star2.setImageResource(R.drawable.ic_star);
                holder.star3.setImageResource(R.drawable.ic_star);
                holder.star4.setImageResource(R.drawable.ic_star);
                holder.star5.setImageResource(R.drawable.ic_star);
                break;
            default:
                holder.star1.setImageResource(R.drawable.ic_empty_star);
                holder.star2.setImageResource(R.drawable.ic_empty_star);
                holder.star3.setImageResource(R.drawable.ic_empty_star);
                holder.star4.setImageResource(R.drawable.ic_empty_star);
                holder.star5.setImageResource(R.drawable.ic_empty_star);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessClickListner.selectedBusiness(company);

            }
        });
    }

    @Override
    public int getItemCount() {
        // number of items wanted to be displayed
        return businessList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabs views from recycler layout file
        //similar to onCreate


        ImageView star1, star2, star3, star4, star5;
        ImageView businessImage;
        TextView businessTitle;
        TextView businessDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
            businessImage = itemView.findViewById(R.id.businessImage);
            businessTitle = itemView.findViewById(R.id.businessTitle);
            businessDescription = itemView.findViewById(R.id.businessDescription);
        }
    }
}

