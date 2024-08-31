import androidx.compose.ui.window.ComposeUIViewController
import com.martin.bibleapp.ui.App
import com.martin.bibleapp.di.initializeKoin

fun MainViewController() = ComposeUIViewController {
    initializeKoin()
    App()
}