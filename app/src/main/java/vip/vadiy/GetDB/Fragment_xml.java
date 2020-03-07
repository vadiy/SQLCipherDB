package vip.vadiy.GetDB;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import vip.vadiy.GetDB.Utils.RootCMD;

/**
 * Created by AiXin on 2019-10-25.
 */
public class Fragment_xml extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private final static String TAG = "Fragment_xml";
    private final static String XML_NAME = "tmp";

    private SharePreferenceConfig sConfig = null;
    private Context context;

    EditText et_xmlpath;
    EditText et_xmlkey;
    EditText et_xmlresult;

    EditText[] etArry;

    Button bt_xmlopen;
    Button bt_xmlshowall;
    Button bt_xmlserch;
    Button bt_xmlclear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_xml, container, false);
        context = rootView.getContext();
        InitView(rootView);
        InitEvent();
        return rootView;
    }


    private void InitView(View v) {
        et_xmlpath = v.findViewById(R.id.et_xmlpath);
        et_xmlkey = v.findViewById(R.id.et_xmlkey);
        et_xmlresult = v.findViewById(R.id.et_xmlresult);

        etArry = new EditText[]{et_xmlpath, et_xmlkey, et_xmlresult};
        bt_xmlopen = v.findViewById(R.id.bt_xmlopen);
        bt_xmlserch = v.findViewById(R.id.bt_xmlserch);
        bt_xmlshowall = v.findViewById(R.id.bt_xmlshowall);
        bt_xmlclear = v.findViewById(R.id.bt_xmlclear);
    }

    private void InitEvent() {
        bt_xmlopen.setOnClickListener(this);
        bt_xmlserch.setOnClickListener(this);
        bt_xmlshowall.setOnClickListener(this);
        bt_xmlclear.setOnClickListener(this);
        for (EditText et : etArry) {
            et.setOnFocusChangeListener(this);
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
        String val = "Nothing";
        switch (v.getId()) {
            case R.id.bt_xmlopen:
                openOrClossXML();
                break;
            case R.id.bt_xmlserch:
                val = sConfig.getString(et_xmlkey.getText().toString());
                Log.i(TAG, val + "---------------------------");
                et_xmlresult.setText(val);
                break;
            case R.id.bt_xmlshowall:
                val = sConfig.getShowAll();
                Log.i(TAG, val);
                et_xmlresult.setText(val);
                break;
            case R.id.bt_xmlclear:
                openOrClossXML();
                for (int i = 0; i < etArry.length; i++) {
                    etArry[i].setText("");
                }
                break;
        }
    }

    private boolean openXML(String name) {
        boolean result = false;
        if ("".equals(name) == false) {
            String xmlPath = "/data/data/" + context.getPackageName() + "/shared_prefs/" + XML_NAME + ".xml";
            Log.i(TAG, name);
            Log.i(TAG, xmlPath);
            if (RootCMD.copyFile(name, xmlPath,"777")) {
                sConfig = new SharePreferenceConfig(context, XML_NAME);
                et_xmlresult.setText("打开文件成功");
                result = true;
            } else {
                et_xmlresult.setText("打开文件失败");
            }
        } else {
            et_xmlresult.setText("打开文件失败");
        }
        return result;
    }

    private void openOrClossXML() {
        String xmlpath = et_xmlpath.getText().toString();
        Log.i(TAG, xmlpath);
        if ("关闭文件".equals(bt_xmlopen.getText().toString())) {
            bt_xmlopen.setText("打开文件");
        } else {
            if (openXML(xmlpath)) {
                bt_xmlopen.setText("关闭文件");
            } else {
                bt_xmlopen.setText("打开文件");
            }
        }

    }

}
