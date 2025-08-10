package com.a200888.ailisten;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryChatAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> bleDeviceList = new ArrayList<>();

    public HistoryChatAdapter(Context context) {
        this.context = context;
    }

    public void addData(String bleDevice) {
        removeDevice(bleDevice);
        bleDeviceList.add(bleDevice);
    }

    public void removeDevice(String bleDevice) {
//        for (int i = 0; i < bleDeviceList.size(); i++) {
//            BleDevice device = bleDeviceList.get(i);
//            if (bleDevice.getKey().equals(device.getKey())) {
//                bleDeviceList.remove(i);
//            }
//        }
    }

    public void clearConnectedDevice() {
//        for (int i = 0; i < bleDeviceList.size(); i++) {
//            BleDevice device = bleDeviceList.get(i);
//            if (BleManager.getInstance().isConnected(device)) {
//                bleDeviceList.remove(i);
//            }
//        }
    }

    public void clearScanDevice() {
//        for (int i = 0; i < bleDeviceList.size(); i++) {
//            BleDevice device = bleDeviceList.get(i);
//            if (!BleManager.getInstance().isConnected(device)) {
//                bleDeviceList.remove(i);
//            }
//        }
    }

    public void clear() {
        clearConnectedDevice();
        clearScanDevice();
    }

    @Override
    public int getCount() {
        return bleDeviceList.size();
    }

    @Override
    public String getItem(int position) {
        if (position > bleDeviceList.size())
            return null;
        return bleDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.item_list, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
//            holder.img_blue = (ImageView) convertView.findViewById(R.id.img_blue);
//            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.ll_root = (LinearLayout) convertView.findViewById(R.id.ll_root);
//            holder.layout_connected = (LinearLayout) convertView.findViewById(R.id.layout_connected);
//            holder.btn_disconnect = (Button) convertView.findViewById(R.id.btn_disconnect);
//            holder.btn_connect = (Button) convertView.findViewById(R.id.btn_connect);
//            holder.btn_detail = (Button) convertView.findViewById(R.id.btn_detail);
        }
        String item = getItem(position);

        holder.tv_title.setText(""+item);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        holder.tv_time.setText(year + "-" + month + "-" + day);
//        final BleDevice bleDevice = getItem(position);
//        if (bleDevice != null) {
//            boolean isConnected = BleManager.getInstance().isConnected(bleDevice);
//            String name = bleDevice.getName();
//            String mac = bleDevice.getMac();
//            int rssi = bleDevice.getRssi();
//            holder.txt_name.setText(name);
//            holder.txt_mac.setText(mac);
//            holder.txt_rssi.setText(String.valueOf(rssi));
//            if (isConnected) {
//                holder.img_blue.setImageResource(R.mipmap.ic_blue_connected);
//                holder.txt_name.setTextColor(0xFF1DE9B6);
//                holder.txt_mac.setTextColor(0xFF1DE9B6);
//                holder.layout_idle.setVisibility(View.GONE);
//                holder.layout_connected.setVisibility(View.VISIBLE);
//            } else {
//                holder.img_blue.setImageResource(R.mipmap.ic_blue_remote);
//                holder.txt_name.setTextColor(0xFF000000);
//                holder.txt_mac.setTextColor(0xFF000000);
//                holder.layout_idle.setVisibility(View.VISIBLE);
//                holder.layout_connected.setVisibility(View.GONE);
//            }
//        }

//        holder.btn_connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mListener != null) {
//                    mListener.onConnect(bleDevice);
//                }
//            }
//        });
//
//        holder.btn_disconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mListener != null) {
//                    mListener.onDisConnect(bleDevice);
//                }
//            }
//        });
//
        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClick("test");
                }
            }
        });



        return convertView;
    }

    static class ViewHolder {
        LinearLayout ll_root;
        TextView tv_time;
        TextView tv_title;
    }

    public interface OnChatClickListener {
        void onClick(String str);
    }

    private OnChatClickListener mListener;

    public void setOnChatClickListener(OnChatClickListener listener) {
        this.mListener = listener;
    }

}
