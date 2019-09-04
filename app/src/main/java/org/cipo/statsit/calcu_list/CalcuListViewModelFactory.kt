package org.cipo.statsit.calcu_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.app.Application


class CalcuListViewModelFactory(private val application: Application, private val param: String) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (param.isBlank()) {
//            CalcuListAllViewModel(application) as T
            modelClass.getConstructor(CalcuListAllViewModel::class.java).newInstance(application)
        } else {
//            CalcuListByListNameViewModel(application, param) as T
            modelClass.getConstructor(CalcuListByListNameViewModel::class.java).newInstance(application, param)
        }
    }

//    return MyViewModel(handler) as T
//    return modelClass.getConstructor(Handler::class.java).newInstance(handler)
}