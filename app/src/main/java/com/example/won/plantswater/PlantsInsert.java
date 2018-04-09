package com.example.won.plantswater;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlantsInsert extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_FROM_CAMERA = 1; //카메라 촬영으로 사진 가져오기
    private static final int PICK_FROM_ALBUM = 2; //앨범에서 사진 가져오기
    private static final int CROP_FROM_CAMERA = 3; //가져온 사진을 자르기 위한 변수

    public static final String TAG = "PlantsInsert";

    Button btn_capture, btn_album;
    ImageView iv_view;

    String mCurrentPhotoPath;

    Uri photoURI;
    int flag;
    int pr;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}; //권한 설정 변수
    private static final int MULTIPLE_PERMISSIONS = 101; //권한 동의 여부 문의 후 CallBack 함수에 쓰일 변수

    //아래는 권한 요청 Callback 함수입니다. PERMISSION_GRANTED로 권한을 획득했는지 확인할 수 있습니다. 아래에서는 !=를 사용했기에
//권한 사용에 동의를 안했을 경우를 if문으로 코딩되었습니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }


    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄우며 해당 Activity를 종료시킵니다.
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_insert);

        Spinner Main_spinner = (Spinner)findViewById(R.id.spinner);
        pr = 12;

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.period, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_list);
        Main_spinner.setAdapter(adapter);

        Main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                   @Override
                                                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                       //각 항목 클릭시 포지션값을 토스트에 띄운다.
                                                       //Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();

                                                       if(position == 0)
                                                           pr = 12;
                                                       else
                                                           pr = position * 24;
                                                   }
                                                   @Override
                                                   public void onNothingSelected(AdapterView<?> parent) {

                                                       }}
            );

        btn_capture = (Button) this.findViewById(R.id.button);
        btn_album = (Button) findViewById(R.id.button4);
        iv_view = (ImageView) findViewById(R.id.imageView2);
        flag = 0;

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });

        checkPermissions();

        Button btn_add = (Button) this.findViewById(R.id.button1);
        final EditText et_name = (EditText) this.findViewById(R.id.editText);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_name.getText().toString().length() == 0) {
                    Toast.makeText(PlantsInsert.this, "이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, " pr 테스트 "+pr);

                if(flag == 0)
                {
                    MainActivity.mDatabase.insertData(et_name.getText().toString(), null, pr);
                }
                else
                {
                    MainActivity.mDatabase.insertData(et_name.getText().toString(),mCurrentPhotoPath.toString(), pr);
                }

                Log.d(TAG, " pr 테스트 "+pr);

                Toast.makeText(PlantsInsert.this, "식물 추가 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantsInsert.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //btn_agreeJoin.setOnClickListener(this);

    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(PlantsInsert.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();              finish();
        }
        if (photoFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//sdk 24 이상, 누가(7.0)
                photoURI = FileProvider.getUriForFile(this, getApplication().getPackageName()+".provider", photoFile);
            } else {//sdk 23 이하, 7.0 미만
                photoURI = Uri.fromFile(photoFile);
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/test/"); //test라는 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK); //ACTION_PICK 즉 사진을 고르겠다!
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다." + contentUri, Toast.LENGTH_SHORT).show();
        flag=1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(PlantsInsert.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == PICK_FROM_ALBUM) {
            if(data==null){
                return;
            }
            photoURI = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            MediaScannerConnection.scanFile(PlantsInsert.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
                    new String[]{photoURI.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            try { //저는 bitmap 형태의 이미지로 가져오기 위해 아래와 같이 작업하였으며 Thumbnail을 추출하였습니다.

                if (resultCode == Activity.RESULT_OK) {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
                    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축

                    iv_view.setImageBitmap(thumbImage);
                    galleryAddPic();

                }
                //여기서는 ImageView에 setImageBitmap을 활용하여 해당 이미지에 그림을 띄우시면 됩니다.

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage().toString());
            }
        }
    }

    public void cropImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermission("com.android.camera", photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoURI, "image/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermission(list.get(0).activityInfo.packageName, photoURI,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);

        }
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {

            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/test/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//sdk 24 이상, 누가(7.0)
                photoURI = FileProvider.getUriForFile(this, getApplication().getPackageName()+".provider", tempFile);
            } else {//sdk 23 이하, 7.0 미만
                photoURI = Uri.fromFile(tempFile);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                grantUriPermission(res.activityInfo.packageName, photoURI,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(i, CROP_FROM_CAMERA);
        }

    }

    }






