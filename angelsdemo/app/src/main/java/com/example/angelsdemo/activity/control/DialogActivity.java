package com.example.angelsdemo.activity.control;

import android.os.Bundle;

import com.angels.util.ACDialogUtil;
import com.angels.util.ACToastUtils;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Created by Administrator on 2016/3/24.
 */
public class DialogActivity extends BaseActivity{

    private boolean[] flags=new boolean[]{false,true,false}; //初始复选情况
    private String[] items=null;

    private EditText etText=null;
    private Button btnNormal=null;
    private Button btnList=null;
    private Button btnRadio=null;
    private Button btnCheckBox=null;

    private static final int DIALOG_NORMAL=0; //普通对话框常量
    private static final int DIALOG_LIST=1; //列表对话框常量
    private static final int DIALOG_RADIO=2; //单选按钮对话框常量
    private static final int DIALOG_CHECKBOX=3; //复选对话框常量

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        items= new String[]{"游泳2333", "打篮球233", "登山233"};

        etText=(EditText)findViewById(R.id.etText);
        btnNormal=(Button)findViewById(R.id.btnNormal);
        btnList=(Button)findViewById(R.id.btnList);
        btnRadio=(Button)findViewById(R.id.btnRadio);
        btnCheckBox=(Button)findViewById(R.id.btnCheckBox);
        btnNormal.setOnClickListener(l);
        btnList.setOnClickListener(l);
        btnRadio.setOnClickListener(l);
        btnCheckBox.setOnClickListener(l);

        Button btn001 = (Button)findViewById(R.id.btn001);
        btn001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACDialogUtil.showDefaultDialog(DialogActivity.this, "标题-普通对话框", "对话框内容", "ok", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ACToastUtils.showMessage(DialogActivity.this, "确定");
                    }
                });
            }
        });
        Button btn002 = (Button)findViewById(R.id.btn002);
        btn002.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ACDialogUtil.showProgressDialog(DialogActivity.this,"标题-普通对话框",true);
            }
        });
        Button btn003 = (Button)findViewById(R.id.btn003);
        btn003.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ACDialogUtil.showDefaultDialog(DialogActivity.this, "标题-普通对话框", "对话框内容", "ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ACToastUtils.showMessage(DialogActivity.this, "确定");
                    }
                });
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;
        switch (id) {
            case DIALOG_NORMAL: //创建普通对话框
                dialog = ACDialogUtil.createNormalDialog(this,
                        R.drawable.ic_launcher,
                        "普通对话框",
                        "这是普通对话框中的内容！",
                        " 确 定 ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etText.setText("这是普通对话框中的内容！");
                                return;
                            }
                        }
                );
                break;
            case DIALOG_LIST: // 创建列表对话框
                dialog = ACDialogUtil.createListDialog(this,
                        R.drawable.ic_launcher,
                        "列表对话框",
                        R.array.hobby,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String hoddy=getResources().getStringArray(R.array.hobby)[which];
                                etText.setText("您选择了： "+hoddy);
                                return;
                            }
                        }
                );
                break;
            case DIALOG_RADIO: // 创建单选按钮对话框
                dialog=ACDialogUtil.createRadioDialog(this,
                        R.drawable.ic_launcher,
                        "单选按钮对话框",
                        R.array.hobby,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String hoddy = getResources().getStringArray(
                                        R.array.hobby)[which];
                                etText.setText("您选择了： " + hoddy);
                                return;
                            }
                        },
                        " 确 定 ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(DialogActivity.this,
                                        "您按了确定按钮！", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                );
                break;
            case DIALOG_CHECKBOX: // 创建复选框对话框
                dialog=ACDialogUtil.createCheckBoxDialog(this,
                        R.drawable.ic_launcher,
                        "复选对话框",
                        R.array.hobby,
                        flags,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                flags[which] = isChecked;
                                String result = "您选择了：";
                                for (int i = 0; i < flags.length; i++) {
                                    if (flags[i]) {
                                        result = result + items[i] + "、";
                                    }
                                }
                                etText.setText(result.substring(0, result.length() - 1));
                            }
                        },
                        " 确 认 ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(DialogActivity.this, "您按了确定按钮！", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                );
                break;
        }
        return dialog;
    }

    //按钮监听
    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            switch (btn.getId()) {
                case R.id.btnNormal: //普通对话框
                    //显示对话框
                    showDialog(DIALOG_NORMAL);
                    break;
                case R.id.btnList: //列表对话框
                    //显示对话框
                    showDialog(DIALOG_LIST);
                    break;
                case R.id.btnRadio: //单选按钮对话框
                    //显示对话框
                    showDialog(DIALOG_RADIO);
                    break;
                case R.id.btnCheckBox: //复选对话框
                    //显示对话框
                    showDialog(DIALOG_CHECKBOX);
                    break;
            }
        }
    };
}
