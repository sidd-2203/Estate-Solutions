package com.example.estatesolutions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UpdateData extends AppCompatActivity {
    EditText name,model,price,area,dimension,floor,docks,address,extra;
    String nameString;
    String linksArray[]=new String[8];
    ImageView imageViews[]=new ImageView[8];
    TextView frontTextView,rearTextView,rightTextView,leftTextView,innerTextView1,innerTextView2,innerTextView3,terraceTextView;
    String nameOfViewsArray[]={"frontView","rearView","rightView","leftView","terraceView","innerView1","innerView2","innerView3"};
    ImageView dropButtons[]=new ImageView[8];
    String documentId;
    String userIdOfPreviousUser;
    private final int requestCodes[]= {101, 102, 103, 104, 105, 106, 107, 108};
    Uri uri[]=new Uri[8];
    StorageReference storageReference;
    FirebaseUser user;
    PropertyDetails details;
    private final String TAG="Create Property Tag";
    FirebaseFirestore firestore;
    ProgressDialog progress ;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_property);
        initialise();
        progress=new ProgressDialog(this);
        details=new PropertyDetails();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            colorDrawable = new ColorDrawable(
                    Color.argb(0.71f, 0f,0f,0f));
        }
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(colorDrawable);

        }
        saveBtn.setText("Update");
        user= FirebaseAuth.getInstance().getCurrentUser();
        firestore=FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        documentId=intent.getStringExtra("documentId");
        progress.setMessage("Data downloading....");
        progress.setCancelable(false);
        progress.show();

        firestore.collection("PropertyDetails").document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                details=task.getResult().toObject(PropertyDetails.class);
                name.setText(details.getName());
                price.setText(details.getPrice());
                model.setText(details.getModel());
                area.setText(details.getArea());
                dimension.setText(details.getDimension());
                floor.setText(details.getFloor());
                docks.setText(details.getDocks());
                address.setText(details.getAddress());
                extra.setText(details.getExtras());
                userIdOfPreviousUser=details.getUserId();
                setImages(details);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateData.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateData.this,MainActivity.class));
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=checkComplete();
                if(check){
                    nameString=name.getText().toString().trim();
                    String priceString=price.getText().toString().trim();
                    String modelString=model.getText().toString().trim();
                    String areaString =area.getText().toString().trim();
                    String dimensionString=dimension.getText().toString().trim();
                    String floorString=floor.getText().toString().trim();
                    String docksString=docks.getText().toString().trim();
                    String addressString =address.getText().toString().trim();
                    String extraString=extra.getText().toString().trim();


                    details=new PropertyDetails(userIdOfPreviousUser,nameString,priceString,modelString,areaString,dimensionString,floorString,docksString,addressString,extraString);
                    setImageLinks(details);

                    firestore.collection("PropertyDetails").document(documentId).set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UpdateData.this, "Your data has been saved", Toast.LENGTH_SHORT).show();
                            reset();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });
        dropButtons[0].setOnClickListener(v->{openView(0);});
        dropButtons[1].setOnClickListener(v->{openView(1);});
        dropButtons[2].setOnClickListener(v->{openView(2);});
        dropButtons[3].setOnClickListener(v->{openView(3);});
        dropButtons[4].setOnClickListener(v->{openView(4);});
        dropButtons[5].setOnClickListener(v->{openView(5);});
        dropButtons[6].setOnClickListener(v->{openView(6);});
        dropButtons[7].setOnClickListener(v->{openView(7);});

        frontTextView.setOnClickListener(v->{inputImage(requestCodes[0]);});
        rearTextView.setOnClickListener(v -> {inputImage(requestCodes[1]);});
        rightTextView.setOnClickListener(v -> {inputImage(requestCodes[2]);});
        leftTextView.setOnClickListener(v -> {inputImage(requestCodes[3]);});
        terraceTextView.setOnClickListener(v -> {inputImage(requestCodes[4]);});
        innerTextView1.setOnClickListener(v -> {inputImage(requestCodes[5]);});
        innerTextView2.setOnClickListener(v -> {inputImage(requestCodes[6]);});
        innerTextView3.setOnClickListener(v -> {inputImage(requestCodes[7]);});

    }
    public  void openView(int index){
            if(imageViews[index].getVisibility()==View.VISIBLE){
                dropButtons[index].setImageResource(android.R.drawable.arrow_down_float);
                imageViews[index].setVisibility(View.GONE);
            }
            else {
                dropButtons[index].setImageResource(android.R.drawable.arrow_up_float);
                imageViews[index].setVisibility(View.VISIBLE);
            }
    }
    public void setImages(PropertyDetails details){

        if(details.getFrontViewImageLink()!=null){
            linksArray[0]=details.getFrontViewImageLink();
            loadParticularImage(details.getFrontViewImageLink(),0);

        }
        if(details.getRearViewImageLink()!=null){
            linksArray[1]=details.getRearViewImageLink();
            loadParticularImage(details.getRearViewImageLink(),1);
        }
        if(details.getRightViewImageLink()!=null){
            linksArray[2]=details.getRightViewImageLink();
            loadParticularImage(details.getRightViewImageLink(),2);
        }
        if(details.getLeftViewImageLink()!=null){
            linksArray[3]=details.getLeftViewImageLink();
            loadParticularImage(details.getLeftViewImageLink(),3);
        }
        if(details.getTerraceViewImageLink()!=null){
            linksArray[4]=details.getTerraceViewImageLink();
            loadParticularImage(details.getTerraceViewImageLink(),4);
        }
        if(details.getInnerView1ImageLink()!=null){
            linksArray[5]=details.getInnerView1ImageLink();
            loadParticularImage(details.getInnerView1ImageLink(),5);

        }
        if(details.getInnerView2ImageLink()!=null){
            linksArray[6]=details.getInnerView2ImageLink();
            loadParticularImage(details.getInnerView2ImageLink(),6);
        }
        if(details.getInnerView3ImageLink()!=null){
            linksArray[7]=details.getInnerView3ImageLink();
            loadParticularImage(details.getInnerView3ImageLink(),7);
        }
        progress.dismiss();
    }
    public void loadParticularImage(String link,int index){
        Glide.with(this)
                .load(link)
                .into(imageViews[index]);
        imageViews[index].setVisibility(View.VISIBLE);
        dropButtons[index].setImageResource(android.R.drawable.arrow_up_float);

    }
    public void initialise(){
        name=findViewById(R.id.input_name);
        model=findViewById(R.id.input_model);
        price=findViewById(R.id.input_price);
        area=findViewById(R.id.input_area);
        dimension=findViewById(R.id.input_dimension);
        floor=findViewById(R.id.input_floor);
        docks=findViewById(R.id.input_docks);
        address=findViewById(R.id.input_address);
        extra=findViewById(R.id.input_extras);
        saveBtn=findViewById(R.id.save_data);


        frontTextView=findViewById(R.id.textViewFront);
        rearTextView=findViewById(R.id.textViewRear);
        rightTextView=findViewById(R.id.textViewRV);
        leftTextView=findViewById(R.id.textViewLV);
        terraceTextView=findViewById(R.id.textViewTerraceView);
        innerTextView1=findViewById(R.id.textViewInnerView1);
        innerTextView2=findViewById(R.id.textViewInnerView2);
        innerTextView3=findViewById(R.id.textViewInnerView3);


        imageViews[0]=findViewById(R.id.frontViewImageShow);
        imageViews[1]=findViewById(R.id.rearViewImageShow);
        imageViews[2]=findViewById(R.id.rightViewImageShow);
        imageViews[3]=findViewById(R.id.leftViewImageShow);
        imageViews[4]=findViewById(R.id.terraceViewImageShow);
        imageViews[5]=findViewById(R.id.inner1ViewImageShow);
        imageViews[6]=findViewById(R.id.inner2ViewImageShow);
        imageViews[7]=findViewById(R.id.inner3ViewImageShow);

        dropButtons[0]=findViewById(R.id.show1);
        dropButtons[1]=findViewById(R.id.show2);
        dropButtons[2]=findViewById(R.id.show3RV);
        dropButtons[3]=findViewById(R.id.show4LV);
        dropButtons[4]=findViewById(R.id.show5TV);
        dropButtons[5]=findViewById(R.id.show6IV1);
        dropButtons[6]=findViewById(R.id.showIV2);
        dropButtons[7]=findViewById(R.id.showIV3);

        for(int i=0;i<8;i++) linksArray[i] = "";
    }
    public void setImageLinks(PropertyDetails details){
        if(!linksArray[0].isEmpty())details.setFrontViewImageLink(linksArray[0]);
        if(!linksArray[1].isEmpty())details.setRearViewImageLink(linksArray[1]);
        if(!linksArray[2].isEmpty())details.setRightViewImageLink(linksArray[2]);
        if(!linksArray[3].isEmpty())details.setLeftViewImageLink(linksArray[3]);
        if(!linksArray[4].isEmpty())details.setTerraceViewImageLink(linksArray[4]);
        if(!linksArray[5].isEmpty())details.setInnerView1ImageLink(linksArray[5]);
        if(!linksArray[6].isEmpty())details.setInnerView2ImageLink(linksArray[6]);
        if(!linksArray[7].isEmpty())details.setInnerView3ImageLink(linksArray[7]);
    }
    public boolean checkComplete(){
        if(name.getText().toString().trim().isEmpty()){
            putError(name);
            return  false;
        }
        if(price.getText().toString().trim().isEmpty()){
            putError(price);
            return false;
        }
        if(model.getText().toString().trim().isEmpty()){
            putError(model);
            return false;
        }
        if(area.getText().toString().trim().isEmpty()){
            putError(area);
            return false;
        }
        if(address.getText().toString().trim().isEmpty()){
            putError(address);
            return false;
        }
        return true;
    }
    public void putError(@NonNull EditText field){
        field.setError("This Field is mandatory");
        field.requestFocus();
    }
    public void reset(){
        name.setText("");
        price.setText("");
        model.setText("");
        area.setText("");
        dimension.setText("");
        floor.setText("");
        docks.setText("");
        address.setText("");
        extra.setText("");
        for(int i=0;i<8;i++){
            imageViews[i].setVisibility(View.GONE);
            dropButtons[i].setImageResource(android.R.drawable.arrow_down_float);
        }
        startActivity(new Intent(UpdateData.this,MainActivity.class));
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data!=null && requestCode>=101 && requestCode<=108){
            int index=requestCode-101;
            uri[index]=data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri[index]);
                imageViews[index].setImageBitmap(bitmap);
                imageViews[index].setVisibility(View.VISIBLE);
                dropButtons[index].setImageResource(android.R.drawable.arrow_up_float);
                uploadImage(uri[index],documentId,index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void uploadImage(Uri uri,String fileName,int index){
        progress.setMessage("Wait the photo is being uploaded");
        progress.setCancelable(false);
        progress.show();
        storageReference=FirebaseStorage.getInstance().getReference().child(userIdOfPreviousUser).child(fileName).child(nameOfViewsArray[index]);
        storageReference.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+e.getMessage());
                Toast.makeText(UpdateData.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UpdateData.this,"Successfully Uploaded ",Toast.LENGTH_SHORT).show();
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        linksArray[index]=uri.toString();
                        progress.dismiss();
                    }
                });
            }
        });
    }

    public  void inputImage(int requestCode){
        // Defining Implicit Intent to mobile gallery
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), requestCode);
        ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .cropSquare()
                .start(requestCode);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateData.this,MainActivity.class));
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.item2){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to delete this property ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            firestore.collection("PropertyDetails").document(documentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(UpdateData.this, "Successfully Deleted....", Toast.LENGTH_SHORT).show();
                                    FirebaseStorage.getInstance().getReference().child(userIdOfPreviousUser).child(documentId).delete();
                                    startActivity(new Intent(UpdateData.this,MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateData.this, "Something went Wrong....", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getApplicationContext(), "Deletion cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Delete");
         alert.show();

            return true;
        }
        else  return super.onOptionsItemSelected(item);
    }

}