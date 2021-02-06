package org.cipo.statsit.calcu_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.app.Application


class CalcuListViewModelFactory(private val application: Application, private val param: String) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalcuListAllViewModel::class.java)) {
            return CalcuListAllViewModel(application) as T
        } else if (modelClass.isAssignableFrom(CalcuListByListNameViewModel::class.java)) {
            return CalcuListByListNameViewModel(application,param) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
//        return if (param.isBlank()) {
////            CalcuListAllViewModel(application) as T
//            modelClass.getConstructor(CalcuListAllViewModel::class.java).newInstance(application) as T
//        } else {
////            CalcuListByListNameViewModel(application, param) as T
//            modelClass.getConstructor(CalcuListByListNameViewModel::class.java).newInstance(application, param) as T
//        }
    }

//    return MyViewModel(handler) as T
//    return modelClass.getConstructor(Handler::class.java).newInstance(handler)
}