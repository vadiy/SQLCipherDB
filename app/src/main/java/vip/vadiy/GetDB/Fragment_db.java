package vip.vadiy.GetDB;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import vip.vadiy.GetDB.Utils.RootCMD;

/**
 * Created by AiXin on 2019-10-25.
 */
public class Fragment_db extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    private final static String TAG = "Fragment_db";
    private final static String DB_NAME = "tmp.db";
    Context context = null;

    EditText et_dbsqltext;
    EditText et_dbresult;
    EditText et_dbpath;
    EditText et_dbpsw;

    EditText[] etArry;

    Button bt_dbsqlexc;
    Button bt_dbclear;
    Button bt_dbopen;
    StorageDBHelper dbHelper;
    String dfTable = "CREATE TABLE IF NOT EXISTS DEFAULTSTORAGETABLE (ID INTEGER primary key AUTOINCREMENT,KEY TEXT,VALUE TEXT);";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_db, container, false);
        context = rootView.getContext();
        InitView(rootView);
        InitEvent();
        return rootView;
    }


    private void InitView(View v) {
        et_dbsqltext = v.findViewById(R.id.et_dbsqltext);
        et_dbresult = v.findViewById(R.id.et_dbresult);
        et_dbpath = v.findViewById(R.id.et_dbpath);
        et_dbpsw = v.findViewById(R.id.et_dbpsw);

        etArry = new EditText[]{et_dbresult, et_dbsqltext, et_dbpath, et_dbpsw};

        bt_dbsqlexc = v.findViewById(R.id.bt_dbsqlexc);
        bt_dbclear = v.findViewById(R.id.bt_dbclear);
        bt_dbopen = v.findViewById(R.id.bt_dbopen);


    }

    private void InitEvent() {
        bt_dbsqlexc.setOnClickListener(this);
        bt_dbclear.setOnClickListener(this);
        bt_dbopen.setOnClickListener(this);
        for (int i = 0; i < etArry.length; i++) {
            etArry[i].setOnFocusChangeListener(this);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            v.setTag(((TextView) v).getHint().toString());
            ((TextView) v).setHint("");
        } else {
            ((TextView) v).setHint(v.getTag().toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_dbclear:
                openOrClossDB();
                for (int i = 0; i < etArry.length; i++) {
                    etArry[i].setText("");
                }
                break;
            case R.id.bt_dbopen:
                openOrClossDB();
                break;
            case R.id.bt_dbsqlexc:
                et_dbresult.setText("");
                rawQuery(et_dbsqltext.getText().toString());
                showToast("查询完毕");
                break;
        }
    }

    private void openOrClossDB() {
        String dbpath = et_dbpath.getText().toString();
        String psw = et_dbpsw.getText().toString();
        Log.i(TAG, dbpath);
        Log.i(TAG, psw);
        if ("关闭数据库".equals(bt_dbopen.getText().toString())) {
            if (dbHelper != null) {
                dbHelper.close();
            }
            bt_dbopen.setText("打开/创建数据库");
        } else {
            if (openDB(dbpath, psw)) {
                bt_dbopen.setText("关闭数据库");
            } else {
                bt_dbopen.setText("打开/创建数据库");
            }
        }

    }

    private boolean openDB(String name, String psw) {
        boolean result = false;
        if ("".equals(name) == false) {
            if (dbHelper != null) {
                dbHelper.close();
            }
            String databasesPath = context.getDatabasePath(DB_NAME).getPath();
            if (RootCMD.copyFile(name, databasesPath,"777")) {
                dbHelper = new StorageDBHelper(context, databasesPath, psw);
                et_dbresult.setText("打开数据库成功");
                result = true;
            } else {
                et_dbresult.setText("打开数据库失败");
            }
            /*RootCMD.upgradeRootPermission(name);
            dbHelper = new StorageDBHelper(context,  name, psw);
            et_result.setText("打开数据库成功");
            result = true;*/
        } else {
            et_dbresult.setText("打开数据库失败");
        }
        return result;
    }

    private void rawQuery(String sql) {
        String dbpath = et_dbpath.getText().toString();
        String psw = et_dbpsw.getText().toString();
        String json = "";
        if (dbHelper == null) {
            openDB(dbpath, psw);
        }
        if (dbHelper != null) {
            if ("".equals(sql) == false) {
                json = dbHelper.rawQueryJson(sql);
            }
        }
        et_dbresult.setText(json);
    }

    private void getValue(String sql) {
        List<String> listStr = new ArrayList<String>();
        String dbpath = et_dbpath.getText().toString();
        String psw = et_dbpsw.getText().toString();
        if (dbHelper == null) {
            openDB(dbpath, psw);
        }
        if (dbHelper != null) {
            if ("".equals(sql) == false) {
                listStr = dbHelper.getValue(sql);
            }
        }
        StringBuilder backstr = new StringBuilder();

        for (String s : listStr) {
            backstr.append(s).append("\n");
        }

        et_dbresult.setText(backstr.toString());

    }


    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     *  * 请求权限回调
     *  *
     *  * @param requestCode
     *  * @param permissions
     *  * @param grantResults
     *  
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("授权成功");
            } else {
                showToast("授权失败,请去设置打开权限");
            }
        }
    }

    private void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }

}
