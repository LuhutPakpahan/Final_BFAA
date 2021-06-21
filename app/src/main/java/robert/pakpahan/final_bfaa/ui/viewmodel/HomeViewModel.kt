package robert.pakpahan.final_bfaa.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import robert.pakpahan.final_bfaa.source.repository.SearchRepository

class HomeViewModel (application: Application): AndroidViewModel(application) {

    private val searchRepository: SearchRepository =
        SearchRepository()

    fun getUserBySearch(query: String) = searchRepository.getUserBySearch(query = query)

}