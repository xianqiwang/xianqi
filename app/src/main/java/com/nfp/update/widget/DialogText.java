package com.nfp.update.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nfp.update.R;

public class DialogText extends Activity implements View.OnClickListener {
    private Button btn_Normal; // 普通
    private Button btn_List; // 列表
    private Button btn_Choice; // 单选
    private Button btn_MoreChoice; // 多选
    private Button btn_EditText;//可输入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoglist);

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder (this);
        builder.setTitle("fota");

        initview();
    }

    private void initview() {
        btn_Normal = (Button) findViewById(R.id.normal_btn);
        btn_List = (Button) findViewById(R.id.list_btn);
        btn_Choice = (Button) findViewById(R.id.choice_btn);
        btn_MoreChoice = (Button) findViewById(R.id.more_choice_btn);
        btn_EditText = (Button) findViewById(R.id.editext_btn);
        btn_Normal.setOnClickListener(this);
        btn_List.setOnClickListener(this);
        btn_Choice.setOnClickListener(this);
        btn_MoreChoice.setOnClickListener(this);
        btn_EditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.normal_btn:
                dialogNormal();// 普通
                break;
            case R.id.list_btn:
                dialogList(); // 列表
                break;
            case R.id.choice_btn:
                dialogChoice(); // 单选
                break;
            case R.id.more_choice_btn:
                dialogMoreChoice();// 多选
                break;
            case R.id.editext_btn:
                dialogEditText();//可编辑
                break;
            default:
                break;
        }


    }


    /**
     * 普通
     */
    private void dialogNormal() {

        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        Toast.makeText(DialogText.this, "确认",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        Toast.makeText(DialogText.this, "取消",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        Toast.makeText(DialogText.this, "忽略",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
/*
        builder.setIcon(R.mipmap.ic_launcher);
*/
        builder.setTitle("普通对话框");
        builder.setMessage("是否确认退出?");
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.setNeutralButton("忽略", dialogOnclicListener);
        builder.create().show();
    }

    /**
     * 列表
     */
    private void dialogList() {
        final String items[] = {"刘德华", "张柏芝", "蔡依林", "张学友"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("列表");
        // builder.setMessage("是否确认退出?"); //设置内容
/*
        builder.setIcon(R.mipmap.ic_launcher);
*/
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(DialogText.this, items[which],
                        Toast.LENGTH_SHORT).show();

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(DialogText.this, "确定", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        builder.create().show();
    }

    /**
     * 单选
     */
    private void dialogChoice() {
        final String items[] = {"男", "女", "其他"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("单选");
/*
        builder.setIcon(R.mipmap.ic_launcher);
*/
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(DialogText.this, items[which],
                                Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(DialogText.this, "确定", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        builder.create().show();
    }

    /**
     * 多选
     */
    private void dialogMoreChoice() {
        final String items[] = {"JAVA", "C++", "JavaScript", "MySQL"};
        final boolean selected[] = {true, false, true, false};
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("多选");
/*
        builder.setIcon(R.mipmap.ic_launcher);
*/
        builder.setMultiChoiceItems(items, selected,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {

                        Toast.makeText(DialogText.this,
                                items[which] + isChecked, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(DialogText.this, "确定", Toast.LENGTH_SHORT)
                        .show();
                // android会自动根据你选择的改变selected数组的值。
//                for (int i = 0; i < selected.length; i++) {
//                    Log.e("hongliang", "" + selected[i]);
//                }
            }
        });
        builder.create().show();
    }

    /**
     * 可输入的对框框
     */
    private void dialogEditText() {
        final EditText editText = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("可编辑");
/*
        builder.setIcon(R.mipmap.ic_launcher);
*/
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DialogText.this, editText.getText().toString() + "", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }





}
