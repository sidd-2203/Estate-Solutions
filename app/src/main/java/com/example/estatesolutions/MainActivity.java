package com.example.estatesolutions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aspose.slides.IAutoShape;
import com.aspose.slides.ICell;
import com.aspose.slides.ICellFormat;
import com.aspose.slides.IPPImage;
import com.aspose.slides.ISlide;
import com.aspose.slides.ISlideCollection;
import com.aspose.slides.ITable;
import com.aspose.slides.ITextFrame;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import com.aspose.slides.ShapeType;
import com.aspose.slides.android.SizeF;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView noPropertyText;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    FirestoreRecyclerAdapter recyclerAdapter;
    ProgressDialog progress;
    FirebaseAuth auth;

    String nameOfViewsArray[] = {"Name", "Price", "Model", "Area", "Dimension", "Floor", "Docks", "Address", "Extras"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            colorDrawable = new ColorDrawable(
                    Color.argb(0.71f, 0f, 0f, 0f));
        }
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(colorDrawable);
        }

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));

        auth=FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.main_recyclerView);
        noPropertyText = findViewById(R.id.noPropertiesText);
        firestore = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        progress.show();
        Query query = FirebaseFirestore.getInstance().collection("PropertyDetails");
        FirestoreRecyclerOptions<PropertyDetails> options = new FirestoreRecyclerOptions.Builder<PropertyDetails>()
                .setQuery(query, PropertyDetails.class)
                .build();
        recyclerAdapter = new FirestoreRecyclerAdapter<PropertyDetails, ProductViewHolder>(options) {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_show_details, parent, false);
                return new ProductViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull PropertyDetails singleDetail) {
                if (noPropertyText.getVisibility() == View.VISIBLE) {
                    noPropertyText.setVisibility(View.GONE);
                }
                holder.nameOfProject.setText(singleDetail.getName());
                holder.address.setText(singleDetail.getAddress());
                holder.price.setText(singleDetail.getPrice());
                String model = singleDetail.getModel();
                String area = singleDetail.getArea();

                String btnText = model + " | " + area;

                holder.modelBtn.setText(btnText);

                holder.modelBtn.setSelected(true);
                holder.address.setSelected(true);
                holder.nameOfProject.setSelected(true);
                holder.price.setSelected(true);

                holder.modelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, UpdateData.class);
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                        String documentId = snapshot.getId();
                        intent.putExtra("documentId", documentId);
                        startActivity(intent);
                        finish();
                    }
                });

                holder.getPPT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Wait PPT is getting generated...", Toast.LENGTH_SHORT).show();
                        requestPermission();
                        progress.setMessage("PPT Generating.....");
//                        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        progress.setIndeterminate(true);
                        progress.setCancelable(false);
                        progress.show();

                        generatePPT(singleDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
        progress.dismiss();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateProperty.class));
                finish();
            }
        });
    }

    private class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameOfProject, price, address;
        MaterialButton modelBtn, getPPT;
        CardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.singleCardView);
            nameOfProject = itemView.findViewById(R.id.nameTextViewSingle);
            price = itemView.findViewById(R.id.priceTextViewSingle);
            address = itemView.findViewById(R.id.addressTextViewSingle);
            modelBtn = itemView.findViewById(R.id.modelSizeButton);
            getPPT = itemView.findViewById(R.id.getPPTBtnSingleView);

        }
    }

    public void generatePPT(PropertyDetails details) {
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Presentation presentation = new Presentation();
                int counter = 0;
                ISlideCollection slides = presentation.getSlides();
                for (int i = 0; i < 9; i++) {
                    slides.addEmptySlide(presentation.getLayoutSlides().get_Item(0));
                }
                try {
                    if (details.getFrontViewImageLink() != null) {
                        setImageOnSlide(new URL(details.getFrontViewImageLink()), counter++, presentation,"Front View");
                    }
                    if (details.getRearViewImageLink() != null) {
                        setImageOnSlide(new URL(details.getRearViewImageLink()), counter++, presentation,"Rear View");
                    }
                    if (details.getRightViewImageLink() != null) {
                        setImageOnSlide(new URL(details.getRightViewImageLink()), counter++, presentation,"Right View");
                    }
                    if (details.getLeftViewImageLink() != null) {
                        setImageOnSlide(new URL(details.getLeftViewImageLink()), counter++, presentation,"Left View");
                    }
                    if (details.getTerraceViewImageLink() != null) {
                        setImageOnSlide(new URL(details.getTerraceViewImageLink()), counter++, presentation,"Terrace View");
                    }
                    if (details.getInnerView1ImageLink() != null) {
                        setImageOnSlide(new URL(details.getInnerView1ImageLink()), counter++, presentation,"Inner View ");
                    }
                    if (details.getInnerView2ImageLink() != null) {
                        setImageOnSlide(new URL(details.getInnerView2ImageLink()), counter++, presentation,"Inner View");
                    }
                    if (details.getInnerView3ImageLink() != null) {
                        setImageOnSlide(new URL(details.getInnerView3ImageLink()), counter++, presentation,"Inner View");
                    }

                    ISlide sld = presentation.getSlides().get_Item(counter);
                    float colWidth = presentation.getSlideSize().getSize().getWidth() / 2 - 5;

                    // Define columns with widths and rows with heights

                    double[] dblCols = {colWidth, colWidth};

                    HashMap<String, String> valuesList = new HashMap<>();
                    valuesList.put(nameOfViewsArray[0], details.getName());
                    valuesList.put(nameOfViewsArray[1], details.getPrice());
                    valuesList.put(nameOfViewsArray[2], details.getModel());
                    valuesList.put(nameOfViewsArray[3], details.getArea());

                    if (details.getDimension() != null) {
                        valuesList.put(nameOfViewsArray[4], details.getDimension());
                    }
                    if (details.getFloor() != null) {
                        valuesList.put(nameOfViewsArray[5], details.getFloor());
                    }
                    if (details.getDocks() != null) {
                        valuesList.put(nameOfViewsArray[6], details.getDocks());
                    }
                    valuesList.put(nameOfViewsArray[7], details.getAddress());
                    if (details.getExtras() != null) {
                        valuesList.put(nameOfViewsArray[8], details.getExtras());
                    }

                    int numberOfEntries = valuesList.size();
                    double[] dblRows = new double[numberOfEntries];// each row height

                    for (int i = 0; i < numberOfEntries; i++) {
                        dblRows[i] = 30;
                    }
                    int curIndex = 0;
                    String valuesCol[][] = new String[numberOfEntries][2];

                    for (Map.Entry<String, String> entry : valuesList.entrySet()) {
                        valuesCol[curIndex][0] = entry.getKey();
                        valuesCol[curIndex][1] = entry.getValue();
                        curIndex++;
                    }
                    // Add table shape to slide
                    ITable tbl = sld.getShapes().addTable(5, 0, dblCols, dblRows);
                    for (int row = 0; row < tbl.getRows().size(); row++) {
                        for (int cell = 0; cell < tbl.getRows().get_Item(row).size(); cell++) {
                            //ICellFormat cellFormat = tbl.getRows().get_Item(row).get_Item(cell).getCellFormat();
                            ICell cell1 = tbl.getRows().get_Item(row).get_Item(cell);
                            Log.d("TAGG", "run: " + valuesCol[row][cell]);
                            cell1.getTextFrame().setText(valuesCol[row][cell]);
                        }
                    }
                    String sdcardPath = Environment.getExternalStorageDirectory().getPath() + File.separator;
                    String pathOfFile=sdcardPath+details.getName()+".pptx";
                    presentation.save(pathOfFile, SaveFormat.Pptx);

                    progress.cancel();
                    Log.d("TAGG", "PPT SAVED " + details.getName());

                    File file=new File(pathOfFile);
                    if(file.exists()){
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        share.setType("application/pptx");
                        startActivity(Intent.createChooser(share,"Share using"));
                    }
                    else{
                        Log.d("TAGG", "run: Could not find file");
                    }
                } catch (Exception e) {
                    Log.e("TAGGG", "generatePPT: " + e.getMessage());
                    progress.dismiss();
                    //Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                } finally {
                    presentation.dispose();
                }
            }
        });
        thread.start();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }


    public void setImageOnSlide(URL url, int slideNumber, Presentation presentation,String viewName) {
        ISlide slide = presentation.getSlides().get_Item(slideNumber);
        URLConnection connection = null;
        try {
            connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer, 0, buffer.length)) != -1)
                    outputStream.write(buffer, 0, read);
                outputStream.flush();
                IPPImage image = presentation.getImages().addImage(outputStream.toByteArray());
                SizeF sizeF = presentation.getSlideSize().getSize();
                slide.getShapes().addPictureFrame(ShapeType.Rectangle, 0, 0, sizeF.getWidth(), sizeF.getHeight(), image);
                IAutoShape ashp = slide.getShapes().addAutoShape(ShapeType.Rectangle, 0, 0, 150, 50);
                ashp.addTextFrame("");
                ITextFrame txtFrame = ashp.getTextFrame();
                txtFrame.setText(viewName);

                Log.d("TAGG", "setImageOnSlide: image Added");
            } finally {
                if (inputStream != null) inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.sample_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.item1){
            auth.signOut();
            startActivity(new Intent(MainActivity.this,LoginScreen.class));
            finish();
            return true;
        }
        else  return super.onOptionsItemSelected(item);
    }
}