package com.sharnoxz.ambuplus.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sharnoxz.ambuplus.R;
import com.sharnoxz.ambuplus.data.HData;
import com.sharnoxz.ambuplus.data.VData;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<VData>> data;

    public HomeViewModel() {
        data = new MutableLiveData<>();
        DataSources DataSources = new DataSources();
        data.setValue(DataSources.loadVData());
    }

    public LiveData<ArrayList<VData>> getData() {
        return data;
    }
}

class DataSources{
    public ArrayList<VData> loadVData(){
        ArrayList<VData> data = new ArrayList<VData>();
        VData
                vData = new VData(loadHData1(),"Facilities","Introducing AmbuPlus - a smart Ambulance service, which brings the nearest rescue squad to your home. You can also request blood from other users and blood banks. We have used Solace extensively in order to track Ambulances realtime. Ambulances push their live locations to specific topics which the backend subscribes to. ",R.drawable.applogo,R.drawable.tipscorona,R.drawable.stats_covid2,"Dankuni 18",true);
        data.add(vData);
        vData = new VData(loadHData2(),"Preventions","hghhg",R.drawable.applogo,R.drawable.tipscorona,R.drawable.stats_covid2,"Dankuni 18",false);
        data.add(vData);
        vData = new VData(loadHData3(),"What Happened ?","hghhg",R.drawable.applogo,R.drawable.tipscorona,R.drawable.stats_covid2,"Dankuni 18",false);
        data.add(vData);
        vData = new VData(loadHData4(),"Additional Features","hghhg",R.drawable.applogo,R.drawable.tipscorona,R.drawable.stats_covid2,"Dankuni 18",false);
        data.add(vData);
        return data;
    }

    private ArrayList<HData> loadHData1() {
        ArrayList<HData> data = new ArrayList<HData>();
        HData
                hData = new HData(R.drawable.ambulance,R.drawable.ambulance_request,"Ambulance");
                data.add(hData);
        hData = new HData(R.drawable.corona,R.drawable.covid_card,"Covid-19");
        data.add(hData);
        hData = new HData(R.drawable.face_flu,R.drawable.face_mask,"Face Flu");
        data.add(hData);
        hData = new HData(R.drawable.temperature,R.drawable.temperature,"Temperature");
        data.add(hData);
        hData = new HData(R.drawable.other,R.drawable.other,"Other Facilities");
        data.add(hData);
        return data;
    }

    private ArrayList<HData> loadHData2() {
        ArrayList<HData> data = new ArrayList<HData>();
        HData
                hData = new HData(R.drawable.wfh,R.drawable.wfh,"Work From Home");
                data.add(hData);
        hData = new HData(R.drawable.medicine,R.drawable.medicine,"Medicine");
        data.add(hData);
        hData = new HData(R.drawable.social_dist,R.drawable.social_dist,"Social Distancing");
        data.add(hData);
        hData = new HData(R.drawable.mask,R.drawable.face_mask,"Mask");
        data.add(hData);
        hData = new HData(R.drawable.temperature,R.drawable.temperature,"Health Check-Up");
        data.add(hData);
        hData = new HData(R.drawable.other,R.drawable.other,"Other Preventions");
        data.add(hData);
        return data;
    }

    private ArrayList<HData> loadHData3() {
        ArrayList<HData> data = new ArrayList<HData>();
        HData
                hData = new HData(R.drawable.heart_attack,R.drawable.ambulance_request,"Heart Attack");
        data.add(hData);
        hData = new HData(R.drawable.accident,R.drawable.ambulance_request,"Accident");
        data.add(hData);
        hData = new HData(R.drawable.bleeding,R.drawable.ambulance_request,"Bleeding");
        data.add(hData);
        hData = new HData(R.drawable.headache,R.drawable.ambulance_request,"Headache");
        data.add(hData);
        hData = new HData(R.drawable.animal_bite,R.drawable.ambulance_request,"Animal Bite");
        data.add(hData);
        hData = new HData(R.drawable.heat_stroke,R.drawable.ambulance_request,"Heat Stoke");
        data.add(hData);
        hData = new HData(R.drawable.hyperthermic_syndrome,R.drawable.ambulance_request,"Hyperthermic Syndrome");
        data.add(hData);
        hData = new HData(R.drawable.urgent_dentistry,R.drawable.ambulance_request,"Urgent Dentistry");
        data.add(hData);
        hData = new HData(R.drawable.other,R.drawable.ambulance_request,"Other");
        data.add(hData);
        return data;
    }

    private ArrayList<HData> loadHData4() {
        ArrayList<HData> data = new ArrayList<HData>();
        HData
                hData = new HData(R.drawable.blood,R.drawable.blood,"Blood");
        data.add(hData);
        hData = new HData(R.drawable.bandage,R.drawable.bandage,"Bandage");
        data.add(hData);
        hData = new HData(R.drawable.chat,R.drawable.chat,"Messenger");
        data.add(hData);
        hData = new HData(R.drawable.medic_call,R.drawable.medic_call,"Medical Calling");
        data.add(hData);
        hData = new HData(R.drawable.other,R.drawable.other,"Other features");
        data.add(hData);
        return data;
    }

}