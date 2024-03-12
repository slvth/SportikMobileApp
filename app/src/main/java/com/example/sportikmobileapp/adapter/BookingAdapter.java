package com.example.sportikmobileapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.BookingDetailModel;
import com.example.sportikmobileapp.database.model.BookingModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder>{
    Context context;
    Connection connection;
    ArrayList<BookingModel> bookingList;


    public BookingAdapter(Context context, ArrayList<BookingModel> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_booking, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yy");
        Date dateStart, dateEnd;
        try {
            dateStart = dateFormat1.parse(bookingList.get(position).getDateStart());
            dateEnd = dateFormat1.parse(bookingList.get(position).getDateEnd());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        //берем данные из списка
        int bookingId = bookingList.get(position).getBooking_id();
        String bookingIdString = "Заявка №" + bookingId;
        String dateStartString = "Дата начала: " + dateFormat2.format(dateStart);
        String dateEndString = "Дата окончания: " + dateFormat2.format(dateEnd);
        String status = "Статус: " + bookingList.get(position).getStatus();

        //заполняем в шаблон бронирования item_booking
        holder.txtBookingIdItemBooking.setText(bookingIdString);
        holder.txtDateStartItemBooking.setText(dateStartString);
        holder.txtDateEndItemBooking.setText(dateEndString);
        holder.txtStatusItemBooking.setText(status);

        //Скрываем кнопку "Отмена", если статус "Отменен", "Выдан" или "Выполнен"
        if(bookingList.get(position).getStatus().equals("Отменен") || bookingList.get(position).getStatus_id()==2)
            holder.btnCancelBooking.setVisibility(View.GONE);
        if(bookingList.get(position).getStatus().equals("Выдан") || bookingList.get(position).getStatus_id()==5)
            holder.btnCancelBooking.setVisibility(View.GONE);
        if(bookingList.get(position).getStatus().equals("Выполнен") || bookingList.get(position).getStatus_id()==3)
            holder.btnCancelBooking.setVisibility(View.GONE);

        //Скрываем кнопку "QR-код для выдачи", если дата выдачи не соотвествует текущему дню
        Date currentDate = new Date();
        String currentDateString2 = dateFormat2.format(currentDate);
        String dateStartString2 = dateFormat2.format(dateStart);
        if(!currentDateString2.equals(dateStartString2) ||
                !bookingList.get(position).getStatus().equals("Забронирован"))
            holder.btnQRCodeItemBooking.setVisibility(View.GONE);

        // Отмена бронирования
        holder.btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBooking(holder, bookingIdString);
            }
        });

        //Генерация QR-кода для выдачи
        holder.btnQRCodeItemBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //generateQRCode();
                StringBuilder qrCodeText = new StringBuilder("Booking N" + bookingId + ":\n");
                ArrayList<BookingDetailModel> bookingDetails = bookingList.get(holder.getAdapterPosition()).getDetails();
                for(int i = 0; i<bookingDetails.size(); i++)
                    qrCodeText.append((i+1)+") inventory N"+bookingDetails.get(i).getInventoryId()+" count="+bookingDetails.get(i).getCount()+"\n");
                Locale myLocale = new Locale("ru","RU");
                QRGEncoder qrgEncoder = new QRGEncoder(qrCodeText.toString(), null, QRGContents.Type.TEXT, 700);
                Uri uri = null;
                try {
                    Bitmap bitmap = qrgEncoder.getBitmap(0);
                    File imageFolder = new File(context.getCacheDir(), "images");

                    imageFolder.mkdirs();
                    File file = new File(imageFolder, "qr_code.jpg");
                    FileOutputStream stream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    stream.flush();
                    stream.close();
                    uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.example.sportikmobileapp"+".provider", file);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }
                showImage(uri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    private void showImage(Uri imageUri) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(context);
        imageView.setImageURI(imageUri);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    // Отмена бронирования
    private void cancelBooking(BookingViewHolder holder, String bookingIdString){
        new AlertDialog.Builder(context).setTitle("Отмена бронирования")
                .setMessage("Вы точно хотите отменить бронирование?\n"+bookingIdString)
                .setPositiveButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ConnectionDatabase connectionSQL = new ConnectionDatabase();
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                connection = connectionSQL.connectionClass();
                                if (connection != null) {
                                    String sqlQueryUpdate = "UPDATE Заявка SET Статус_бронированияID = 2 WHERE ЗаявкаID="+bookingList.get(holder.getAdapterPosition()).getBooking_id();
                                    Statement statement = null;
                                    try {
                                        statement = connection.createStatement();
                                        statement.executeQuery(sqlQueryUpdate);
                                        connection.close();
                                    } catch (Exception e) {
                                        Log.e("Error: ", e.getMessage());
                                    }
                                    dialog.dismiss();
                                }
                                //обновление списка
                                int actualPosition = holder.getAdapterPosition();
                                bookingList.get(actualPosition).setStatus_id(2);
                                bookingList.get(actualPosition).setStatus("Отменен");
                                notifyItemChanged(actualPosition);
                            }
                        })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}
