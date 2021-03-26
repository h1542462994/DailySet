import androidx.compose.runtime.Composable
import org.tty.dailyset.MainViewModel
import org.tty.dailyset.provider.LocalMainViewModel

@Composable
fun mainViewModel(): MainViewModel {
    return LocalMainViewModel.current
}